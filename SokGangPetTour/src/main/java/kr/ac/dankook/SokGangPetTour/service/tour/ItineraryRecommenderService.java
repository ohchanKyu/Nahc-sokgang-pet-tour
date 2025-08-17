package kr.ac.dankook.SokGangPetTour.service.tour;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.ItineraryRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.tourResponse.ItineraryResponse;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.repository.tour.TourContentRepository;
import kr.ac.dankook.SokGangPetTour.util.converter.TourEntityConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryRecommenderService {

    private final TourContentRepository tourContentRepository;

    public enum TravelMode {
        WALK(0.075),        // 4.5 km/h
        TRANSIT(0.333),     // 20 km/h
        DRIVE(0.5);         // 30 km/h
        public final double kmPerMin;
        TravelMode(double kmPerMin) { this.kmPerMin = kmPerMin; }
        public static TravelMode of(String s) {
            if (s == null) return DRIVE;
            try { return TravelMode.valueOf(s.trim().toUpperCase()); }
            catch (Exception e) { return DRIVE; }
        }
    }

    /** 내부 선택용 POI */
    public static class POI {
        public String contentId;
        public String address;
        public String contentTypeId;
        public String title;
        public String overview;
        public String thumb;
        public double lat, lon;

        /** 응답에 그대로 실어줄 원본 DTO 보관 */
        public TourContentResponse src;
    }

    public static class Stop {
        public POI poi;
        public double distFromPrevKm;
        public int travelMin;
        public int stayMin;
        public String arrival;
        public String depart;
    }

    public static class DayPlan {
        public int dayIndex;
        public List<Stop> stops = new ArrayList<>();
        public int totalTravelMin;
        public int totalStayMin;
    }

    public ItineraryResponse recommend(ItineraryRequest req) {
        List<TourContent> entities = tourContentRepository.findAll();
        List<TourContentResponse> all = entities.stream()
                .map(TourEntityConverter::convertToTourContentResponse)
                .toList();

        List<TourContentResponse> filtered = all.stream()
                .filter(t -> req.getExcludeContentTypeIds() == null
                        || !req.getExcludeContentTypeIds().contains(safe(t.getContentTypeId())))
                .filter(t -> haversineKm(req.getStartLatitude(), req.getStartLongitude(),
                        t.getLatitude(), t.getLongitude()) <= req.getMaxRadiusKm())
                .toList();
        if (filtered.isEmpty()) {
            return ItineraryResponse.builder().days(List.of()).build();
        }

        TravelMode mode = TravelMode.of(req.getTravelMode());
        ScoringParams sp = ScoringParams.from(req);

        Map<String, Double> scoreMap = new HashMap<>();
        for (TourContentResponse t : filtered) {
            POI p = toPOI(t);
            double s = scorePOI(p, req.getStartLatitude(), req.getStartLongitude(), sp, req.isIncludeFoodStops());
            scoreMap.put(p.contentId, s);
        }

        int N = Math.min(80, filtered.size());
        List<TourContentResponse> candidates = filtered.stream()
                .sorted((a, b) -> Double.compare(
                        scoreMap.getOrDefault(b.getContentId(), 0.0),
                        scoreMap.getOrDefault(a.getContentId(), 0.0)))
                .limit(N)
                .toList();

        int k = Math.max(1, Math.min(req.getDays(), candidates.size()));
        List<List<TourContentResponse>> clusters = kmeansByLatLon(candidates, k);

        List<ItineraryResponse.DayPlanResponse> outDays = new ArrayList<>();
        for (int d = 0; d < k; d++) {
            DayPlan plan = buildDayPlan(
                    clusters.get(d), req.getStartLatitude(), req.getStartLongitude(),
                    mode, req, scoreMap, d + 1);
            outDays.add(toResponse(plan));
        }
        return ItineraryResponse.builder().days(outDays).build();
    }

    /** 스코어링 파라미터 */
    private static final class ScoringParams {
        double wDist = 0.5, wKw = 0.5;
        double distDecayKm = 5.0;
        Set<String> keywords = Set.of();
        static ScoringParams from(ItineraryRequest r) {
            ScoringParams p = new ScoringParams();
            if (r.getWDist() != null) p.wDist = r.getWDist();
            if (r.getWKw() != null)   p.wKw = r.getWKw();
            if (r.getDistDecayKm() != null) p.distDecayKm = r.getDistDecayKm();
            if (r.getKeywords() != null)   p.keywords = lower(r.getKeywords());
            return p;
        }
    }

    /** 기본 스코어 + 음식점/숙박 보정 */
    private double scorePOI(POI t, double startLat, double startLon, ScoringParams p, boolean includeFood) {
        double distKm = haversineKm(startLat, startLon, t.lat, t.lon);
        double distScore = Math.exp(-distKm / p.distDecayKm);

        double kwScore = 0.0;
        String text = (safe(t.title) + " " + safe(t.overview)).toLowerCase();
        for (String kw : p.keywords) if (!kw.isBlank() && text.contains(kw)) kwScore += 1.0;
        kwScore = Math.min(kwScore, 1.5);

        double adj = 0.0;
        if ("39".equals(safe(t.contentTypeId))) {
            adj += includeFood ? 0.1 : -1.0; // 음식 포함 옵션 아닐 땐 강한 페널티
        }
        if ("32".equals(safe(t.contentTypeId))) {
            adj -= 0.5; // 숙박은 기본적으로 남용되지 않도록 감점
        }

        double s = p.wDist * distScore + p.wKw * kwScore + adj;
        return Math.max(s, 0.0);
    }

    /** 하루 계획 생성 (숙박 1곳 제한 + 시간대 가중치) */
    protected DayPlan buildDayPlan(List<TourContentResponse> dayPOIs,
                                   double startLat, double startLon,
                                   TravelMode mode, ItineraryRequest req,
                                   Map<String, Double> scoreMap, int dayIndex) {

        DayPlan plan = new DayPlan();
        plan.dayIndex = dayIndex;
        if (dayPOIs.isEmpty()) return plan;

        List<POI> remain = dayPOIs.stream().map(this::toPOI).collect(Collectors.toCollection(ArrayList::new));
        double curLat = startLat, curLon = startLon;
        int accMin = 0;
        boolean hadLunch = false;
        boolean hadLodging = false;     // ★ 하루 숙박 1곳 제한
        int lunchStart = 12 * 60, lunchEnd = 14 * 60;
        Integer maxStops = req.getMaxStopsPerDay();

        while (!remain.isEmpty()) {
            POI best = null;
            double bestGain = -1e9;
            int bestTravel = 0, bestStay = 0;

            for (POI p : remain) {
                String type = safe(p.contentTypeId);
                // 숙박은 하루에 1곳만 허용
                if (hadLodging && "32".equals(type)) {
                    continue;
                }
                int stay = Math.max(estimateDwellMin(p), 0);
                double dKm = haversineKm(curLat, curLon, p.lat, p.lon);
                int tmin = (int) Math.round(dKm / mode.kmPerMin);

                double gain = scoreMap.getOrDefault(p.contentId, 0.0) / Math.max(1, tmin + stay);

                // 음식점: 점심 시간대 선호
                if ("39".equals(type)) {
                    int currentTime = req.getDayStartHour() * 60 + accMin + tmin;
                    boolean inLunch = currentTime >= lunchStart && currentTime <= lunchEnd;
                    if (req.isIncludeFoodStops() && !hadLunch) gain += inLunch ? 0.5 : -0.3;
                    else gain -= 0.8;
                }

                // 숙박: 오후/저녁 시간대 선호, 한낮에는 강한 페널티
                if ("32".equals(type)) {
                    int currentTime = req.getDayStartHour() * 60 + accMin + tmin; // 도착 예상 시각
                    if (currentTime < 16 * 60) {
                        gain -= 2.0;          // 16시 이전엔 사실상 제외
                    } else if (currentTime < 18 * 60) {
                        gain -= 0.7;          // 16~18시는 다소 감점
                    } else if (currentTime <= 22 * 60) {
                        gain += 0.4;          // 18~22시는 체크인 타임대 선호
                    } else {
                        gain -= 0.2;          // 너무 늦으면 소폭 감점
                    }
                }

                if (gain > bestGain) {
                    bestGain = gain; best = p; bestTravel = tmin; bestStay = stay;
                }
            }
            if (best == null) break;

            if (accMin + bestTravel + bestStay > req.getDailyTimeLimitMin()) break;

            Stop s = new Stop();
            s.poi = best;
            s.distFromPrevKm = haversineKm(curLat, curLon, best.lat, best.lon);
            s.travelMin = bestTravel;
            s.stayMin = bestStay;
            plan.stops.add(s);

            accMin += bestTravel + bestStay;
            curLat = best.lat; curLon = best.lon;

            if ("39".equals(safe(best.contentTypeId)) && req.isIncludeFoodStops()) hadLunch = true;
            if ("32".equals(safe(best.contentTypeId))) hadLodging = true; // ★ 숙박 선택됨

            remain.remove(best);
            if (maxStops != null && plan.stops.size() >= maxStops) break;
        }

        timeSchedule(plan, req, mode, startLat, startLon);
        return plan;
    }

    /** 체류시간(분) 추정 */
    protected int estimateDwellMin(POI t) {
        String type = safe(t.contentTypeId);
        return switch (type) {
            case "12" -> 60;    // 관광지
            case "14" -> 90;    // 문화시설
            case "15" -> 90;    // 행사
            case "28" -> 120;   // 레포츠
            case "32" -> 0;     // 숙박(루트 채우기용, 시간표는 별도 가중치로 제어)
            case "38" -> 60;    // 쇼핑
            case "39" -> 60;    // 음식
            default -> 60;
        };
    }

    /** 도착/출발 시각 및 합계 계산 */
    protected void timeSchedule(DayPlan plan, ItineraryRequest req, TravelMode mode,
                                double startLat, double startLon) {
        int clock = req.getDayStartHour() * 60;
        plan.totalTravelMin = 0; plan.totalStayMin = 0;

        for (int i = 0; i < plan.stops.size(); i++) {
            Stop s = plan.stops.get(i);

            if (i == 0) {
                double dKm = haversineKm(startLat, startLon, s.poi.lat, s.poi.lon);
                s.travelMin = (int) Math.round(dKm / mode.kmPerMin);
                s.distFromPrevKm = dKm;
            } else {
                Stop prev = plan.stops.get(i - 1);
                double dKm = haversineKm(prev.poi.lat, prev.poi.lon, s.poi.lat, s.poi.lon);
                s.travelMin = (int) Math.round(dKm / mode.kmPerMin);
                s.distFromPrevKm = dKm;
            }

            clock += s.travelMin;
            s.arrival = toHHmm(clock);
            plan.totalTravelMin += s.travelMin;

            if (s.stayMin <= 0) s.stayMin = Math.max(estimateDwellMin(s.poi), 0);
            clock += s.stayMin;
            s.depart = toHHmm(clock);
            plan.totalStayMin += s.stayMin;
        }
    }

    /** 위경도 K-means */
    protected List<List<TourContentResponse>> kmeansByLatLon(List<TourContentResponse> input, int k) {
        if (k <= 1 || input.size() <= k) {
            List<List<TourContentResponse>> out = new ArrayList<>();
            for (TourContentResponse t : input) out.add(new ArrayList<>(List.of(t)));
            while (out.size() < k) out.add(new ArrayList<>());
            return out;
        }
        List<double[]> centers = new ArrayList<>();
        Set<Integer> picked = new HashSet<>();
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        while (centers.size() < k) {
            int idx = rnd.nextInt(input.size());
            if (picked.add(idx)) {
                centers.add(new double[]{ input.get(idx).getLatitude(), input.get(idx).getLongitude() });
            }
        }
        List<Integer> assign = new ArrayList<>(Collections.nCopies(input.size(), -1));
        boolean changed = true; int it = 0, maxIt = 30;
        while (changed && it++ < maxIt) {
            changed = false;
            for (int i = 0; i < input.size(); i++) {
                TourContentResponse t = input.get(i);
                int best = -1; double bestD = 1e18;
                for (int c = 0; c < k; c++) {
                    double d = dist2(centers.get(c)[0], centers.get(c)[1], t.getLatitude(), t.getLongitude());
                    if (d < bestD) { bestD = d; best = c; }
                }
                if (assign.get(i) == null || assign.get(i) != best) {
                    assign.set(i, best); changed = true;
                }
            }
            double[][] sum = new double[k][2]; int[] cnt = new int[k];
            for (int i = 0; i < input.size(); i++) {
                int a = assign.get(i);
                sum[a][0] += input.get(i).getLatitude();
                sum[a][1] += input.get(i).getLongitude();
                cnt[a]++;
            }
            for (int c = 0; c < k; c++) if (cnt[c] > 0) {
                centers.get(c)[0] = sum[c][0] / cnt[c];
                centers.get(c)[1] = sum[c][1] / cnt[c];
            }
        }
        List<List<TourContentResponse>> clusters = new ArrayList<>();
        for (int c = 0; c < k; c++) clusters.add(new ArrayList<>());
        for (int i = 0; i < input.size(); i++) clusters.get(assign.get(i)).add(input.get(i));
        for (int c = 0; c < k; c++) if (clusters.get(c).isEmpty()) {
            int src = largestClusterIndex(clusters);
            clusters.get(c).add(clusters.get(src).removeLast());
        }
        return clusters;
    }

    private int largestClusterIndex(List<List<TourContentResponse>> clusters) {
        int idx = 0, sz = -1;
        for (int i = 0; i < clusters.size(); i++) {
            if (clusters.get(i).size() > sz) { sz = clusters.get(i).size(); idx = i; }
        }
        return idx;
    }

    private POI toPOI(TourContentResponse t) {
        POI p = new POI();
        p.contentId = t.getContentId();
        p.address = t.getAddress();
        p.contentTypeId = t.getContentTypeId();
        p.title = t.getTitle();
        p.overview = t.getOverview();
        p.thumb = t.getThumbnailImageUrl();
        p.lat = t.getLatitude();
        p.lon = t.getLongitude();
        p.src = t; // 원본 보관
        return p;
    }

    /** 응답: StopResponse에 TourContentResponse 포함 */
    private ItineraryResponse.DayPlanResponse toResponse(DayPlan plan) {
        List<ItineraryResponse.StopResponse> stops = plan.stops.stream().map(s ->
                ItineraryResponse.StopResponse.builder()
                        .tourContentResponse(s.poi.src)                   // ★ 전체 객체 포함
                        .distFromPrevKm(round1(s.distFromPrevKm))
                        .travelMin(s.travelMin)
                        .stayMin(s.stayMin)
                        .arrival(s.arrival)
                        .depart(s.depart)
                        .build()
        ).toList();

        return ItineraryResponse.DayPlanResponse.builder()
                .dayIndex(plan.dayIndex)
                .stops(stops)
                .totalTravelMin(plan.totalTravelMin)
                .totalStayMin(plan.totalStayMin)
                .build();
    }

    private static double round1(double v) { return Math.round(v * 10.0) / 10.0; }

    private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0088;
        double dLat = Math.toRadians(lat2 - lat1), dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2)*Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2)*Math.sin(dLon/2);
        return 2 * R * Math.asin(Math.sqrt(a));
    }
    private static double dist2(double a, double b, double c, double d) {
        double dx = a - c, dy = b - d; return dx*dx + dy*dy;
    }
    private static String toHHmm(int minutesOfDay) {
        int h = (minutesOfDay / 60) % 24;
        int m = minutesOfDay % 60;
        return String.format("%02d:%02d", h, m);
    }
    private static String safe(String s) { return s == null ? "" : s; }
    private static Set<String> lower(Set<String> src) {
        return src == null ? Set.of() : src.stream().filter(Objects::nonNull)
                .map(String::toLowerCase).collect(Collectors.toSet());
    }
}
