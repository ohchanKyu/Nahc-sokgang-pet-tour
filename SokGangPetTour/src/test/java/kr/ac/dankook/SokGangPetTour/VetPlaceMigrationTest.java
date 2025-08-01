package kr.ac.dankook.SokGangPetTour;

import kr.ac.dankook.SokGangPetTour.entity.*;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceCategory;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceMigration;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceOperatingHour;
import kr.ac.dankook.SokGangPetTour.repository.vetPlace.VetPlaceMigrationRepository;
import kr.ac.dankook.SokGangPetTour.repository.vetPlace.VetPlaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Slf4j
public class VetPlaceMigrationTest {

    @Autowired
    private VetPlaceMigrationRepository vetPlaceMigrationRepository;

    @Autowired
    private VetPlaceRepository vetPlaceRepository;

    @Test
    @DisplayName("반려동물 병원, 약국 데이터 마이그레이션 실행")
    @Transactional
    @Commit
    public void vetPlaceMigrationTest(){

        List<VetPlaceMigration> vetPlaceMigrations = vetPlaceMigrationRepository.findAll();
        for (VetPlaceMigration mig : vetPlaceMigrations) {
            VetPlace newPlace = VetPlace.builder()
                    .placeName(mig.getPlaceName())
                    .address(mig.getAddress())
                    .phoneNumber(mig.getPhoneNumber())
                    .latitude(mig.getLatitude())
                    .longitude(mig.getLongitude())
                    .holidayInfo(mig.getHolidayInfo())
                    .maxSizeInfo(mig.getMaxSizeInfo())
                    .isParking("주차가능".equals(mig.getIsParking()))
                    .category(convertCategory(mig.getCategory()))
                    .build();

            if (mig.getOperatingInfo() != null && !mig.getOperatingInfo().isBlank()) {
                List<VetPlaceOperatingHour> hours = parseOperatingInfo(mig.getOperatingInfo(), newPlace);
                hours.forEach(newPlace::addOperatingHour);
            }
            vetPlaceRepository.save(newPlace);
        }
        log.info("데이터 마이그레이션 완료");
    }
    private List<VetPlaceOperatingHour> parseOperatingInfo(String raw, VetPlace parent) {
        List<VetPlaceOperatingHour> result = new ArrayList<>();
        String[] chunks = raw.split(",");
        List<DayType> parsedDays = new ArrayList<>();

        for (String chunk : chunks) {
            try {
                String[] dayAndTime = chunk.split("-");
                if (dayAndTime.length != 2) continue;

                String dayStr = dayAndTime[0].trim();
                String[] timeRange = dayAndTime[1].split("~");
                if (timeRange.length != 2) continue;

                LocalTime openTime = safeParseTime(timeRange[0].trim());
                LocalTime closeTime = safeParseTime(timeRange[1].trim());

                DayType dayType = convertDayType(dayStr);
                parsedDays.add(dayType);

                VetPlaceOperatingHour hour = VetPlaceOperatingHour.builder()
                        .dayType(dayType)
                        .openTime(openTime)
                        .closeTime(closeTime)
                        .isOpen(true)
                        .vetPlace(parent)
                        .build();


                result.add(hour);
            } catch (Exception e) {
                log.warn("운영시간 파싱 실패: {} ({})", chunk, raw);
            }
        }
        for (DayType dayType : DayType.values()) {
            if (!parsedDays.contains(dayType)) {
                VetPlaceOperatingHour closed = VetPlaceOperatingHour.builder()
                        .dayType(dayType)
                        .openTime(null)
                        .closeTime(null)
                        .isOpen(false)
                        .vetPlace(parent)
                        .build();

                result.add(closed);
            }
        }

        return result;
    }

    private LocalTime safeParseTime(String timeStr) {
        if ("24:00".equals(timeStr)) {
            return LocalTime.of(23, 59, 59);
        }
        return LocalTime.parse(timeStr);
    }

    private DayType convertDayType(String d) {
        return switch (d) {
            case "월" -> DayType.MON;
            case "화" -> DayType.TUE;
            case "수" -> DayType.WED;
            case "목" -> DayType.THU;
            case "금" -> DayType.FRI;
            case "토" -> DayType.SAT;
            case "일" -> DayType.SUN;
            case "법정공휴일" -> DayType.HOLIDAY;
            default -> throw new IllegalArgumentException("알 수 없는 요일: " + d);
        };
    }
    private VetPlaceCategory convertCategory(String cat) {
        if (cat.contains("병원")) return VetPlaceCategory.HOSPITAL;
        else return VetPlaceCategory.PHARMACY;
    }
}
