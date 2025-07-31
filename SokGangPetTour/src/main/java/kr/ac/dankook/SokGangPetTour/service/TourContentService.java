package kr.ac.dankook.SokGangPetTour.service;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentDistResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentResponse;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.repository.tour.TourContentRepository;
import kr.ac.dankook.SokGangPetTour.util.converter.TourEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static kr.ac.dankook.SokGangPetTour.util.DistCalculationUtil.getDistance;


@Slf4j
@RequiredArgsConstructor
@Service
public class TourContentService {

    private final TourContentRepository tourContentRepository;

    public List<TourContentResponse> getAllTourContent(){
        List<TourContent> lists = tourContentRepository.findAll();
        return lists.stream().map(TourEntityConverter::convertToTourContentResponse).toList();
    }

    public List<TourContentResponse> getTourContentByKeyword(String keyword){
        List<TourContent> lists = tourContentRepository.findTourContentWithKeyword(keyword);
        return lists.stream().map(TourEntityConverter::convertToTourContentResponse).toList();
    }

    public List<TourContentResponse> getTourContentByFilter(
            String cat1,String cat2,String cat3, int sigunguCode, String contentTypeId){
        // Using Index - cat1
        List<TourContent> lists = tourContentRepository.findTourContentByCat1(cat1);
        return lists.stream().filter(item ->
            (sigunguCode == 0 || item.getSigunguCode() == sigunguCode) &&
            (cat2 == null || item.getCat2().equals(cat2)) &&
            (cat3 == null || item.getCat3().equals(cat3)) &&
            (contentTypeId == null || item.getContentTypeId().equals(contentTypeId))
        ).map(TourEntityConverter::convertToTourContentResponse).toList();
    }

    public List<TourContentDistResponse> getTourPlaceOrderByDist(
            double desLatitude,double desLongitude){
        List<TourContentResponse> lists = getAllTourContent();
        List<TourContentDistResponse> distResponses = new ArrayList<>(lists.stream()
                .map(item -> {
                    double dist = getDistance(item.getLatitude(), item.getLongitude(), desLatitude, desLongitude);
                    TourContentDistResponse distItems =
                            TourContentDistResponse.builder()
                                    .tourContentResponse(item)
                                    .distance(dist).build();
                    if (dist >= 1) distItems.setDistanceStringFormat(String.format("%.1fkm", dist));
                    else distItems.setDistanceStringFormat(String.format("%.1fm", dist * 1000));
                    return distItems;
                }).toList());
        distResponses.sort(Comparator.comparingDouble(TourContentDistResponse::getDistance));
        return distResponses;
    }
}
