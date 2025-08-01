package kr.ac.dankook.SokGangPetTour.service.vetPlace;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceDistResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.*;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceCategory;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceOperatingHour;
import kr.ac.dankook.SokGangPetTour.repository.vetPlace.VetPlaceRepository;
import kr.ac.dankook.SokGangPetTour.util.converter.VetPlaceEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static kr.ac.dankook.SokGangPetTour.util.DistCalculationUtil.getDistance;

@Service
@RequiredArgsConstructor
@Slf4j
public class VetPlaceService {

    private final VetPlaceRepository vetPlaceRepository;

    public List<VetPlaceResponse> getAllVetPlace(){
        List<VetPlace> lists = vetPlaceRepository.findAllVetPlaceWithFetchJoin();
        return lists.stream().map(item ->
                VetPlaceEntityConverter.convertToVetPlaceResponse(item,true)).toList();
    }

    public List<VetPlaceResponse> getVetPlacesByKeyword(String keyword){
        List<VetPlace> lists = vetPlaceRepository.findByKeywordWithJoinFetch(keyword);
        return lists.stream().map(item ->
                VetPlaceEntityConverter.convertToVetPlaceResponse(item,true)).toList();
    }

    public List<VetPlaceResponse> getVetPlacesByFilter(VetPlaceCategory category, boolean isParking, boolean isOpen){

        List<VetPlace> lists;
        // Using category and parking index;
        if (isParking)  lists = vetPlaceRepository.findByCategoryAndParkingWithFetchJoin(category);
        // Using category index;
        else lists = vetPlaceRepository.findByCategoryWithFetchJoin(category);

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();

        return lists.stream().filter(item ->
                (!isOpen || isOpenNow(item.getOperatingHours(),now,today))
        ).map(item -> VetPlaceEntityConverter.convertToVetPlaceResponse(item,true)).toList();
    }

    public List<VetPlaceDistResponse> getVetPlaceOrderByDist(
            double desLatitude,double desLongitude){
        List<VetPlaceResponse> lists = getAllVetPlace();
        List<VetPlaceDistResponse> distResponses = new ArrayList<>(lists.stream()
                .map(item -> {
                    double dist = getDistance(item.getLatitude(), item.getLongitude(), desLatitude, desLongitude);
                    VetPlaceDistResponse distItems =
                             VetPlaceDistResponse.builder()
                                     .vetPlaceResponse(item)
                                     .distance(dist).build();
                    if (dist >= 1) distItems.setDistanceStringFormat(String.format("%.1fkm", dist));
                    else distItems.setDistanceStringFormat(String.format("%.1fm", dist * 1000));
                    return distItems;
                }).toList());
        distResponses.sort(Comparator.comparingDouble(VetPlaceDistResponse::getDistance));
        return distResponses;
    }

    private boolean isOpenNow(
            List<VetPlaceOperatingHour> hours,
            LocalDateTime now, DayOfWeek today) {
        DayType currentDayType = Holiday.isHoliday(now.toLocalDate()) ?
                DayType.HOLIDAY
                : switch(today) {
                    case MONDAY -> DayType.MON;
                    case TUESDAY -> DayType.TUE;
                    case WEDNESDAY -> DayType.WED;
                    case THURSDAY -> DayType.THU;
                    case FRIDAY -> DayType.FRI;
                    case SATURDAY -> DayType.SAT;
                    case SUNDAY -> DayType.SUN;
        };
        LocalTime currentTime = now.toLocalTime();
        return hours.stream()
                .filter(h -> h.getDayType() == currentDayType)
                .filter(VetPlaceOperatingHour::isOpen)
                .anyMatch(h ->
                    h.getOpenTime() != null &&
                    h.getCloseTime() != null &&
                    !currentTime.isBefore(h.getOpenTime()) &&
                    currentTime.isBefore(h.getCloseTime())
                );
    }
}
