package kr.ac.dankook.SokGangPetTour.service;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceDistResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.VetPlaceCategory;
import kr.ac.dankook.SokGangPetTour.repository.VetPlaceRepository;
import kr.ac.dankook.SokGangPetTour.util.converter.VetPlaceEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VetPlaceService {

    private final VetPlaceRepository vetPlaceRepository;

    public List<VetPlaceResponse> getAllVetPlace(){
        List<VetPlace> lists = vetPlaceRepository.findAllVetPlaceByFetchJoin();
        return lists.stream().map(item ->
                VetPlaceEntityConverter.convertToVetPlaceResponse(item,true)).toList();
    }

    public List<VetPlaceResponse> getVetPlacesByKeyword(String keyword){
        List<VetPlace> lists = vetPlaceRepository.findByKeywordAndJoinFetch(keyword);
        return lists.stream().map(item ->
                VetPlaceEntityConverter.convertToVetPlaceResponse(item,true)).toList();
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

//    @Transactional(readOnly = true)
//    public List<VetPlaceResponse> getVetPlacesByCategory(String category){
//        if (!category.equals(VetPlaceCategory.HOSPITAL.toString()) ||
//            !category.equals(VetPlaceCategory.PHARMACY.toString())){
//            log.error("Not found category - {}",category);
//        }
//        List<VetPlace> lists = vetPlaceRepository.findByCategory(VetPlaceCategory.valueOf(category));
//        return lists.stream().map(VetPlaceEntityConverter::convertToVetPlaceResponse).toList();
//    }

    private double getDistance(
            double statLatitude, double statLongitude, double desLatitude, double desLongitude
    ) {
        double theta = desLongitude - statLongitude;
        double dist = Math.sin(deg2rad(statLatitude)) *
                Math.sin(deg2rad(desLatitude)) +
                Math.cos(deg2rad(statLatitude)) *
                        Math.cos(deg2rad(desLatitude)) *
                        Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1609.344;
        return dist / 1000;
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
