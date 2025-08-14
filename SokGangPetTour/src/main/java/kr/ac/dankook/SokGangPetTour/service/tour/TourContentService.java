package kr.ac.dankook.SokGangPetTour.service.tour;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.*;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailRepeatCommon;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailRepeatRoom;
import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.*;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import kr.ac.dankook.SokGangPetTour.repository.tour.TourContentRepository;
import kr.ac.dankook.SokGangPetTour.util.converter.TourEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static kr.ac.dankook.SokGangPetTour.util.DistCalculationUtil.getDistance;


@Slf4j
@RequiredArgsConstructor
@Service
public class TourContentService {

    private final TourContentRepository tourContentRepository;

    public List<String> getCategoryByParentCategory(int type,String cat1,String cat2){
        if (type == 1){
            return tourContentRepository.findCat2ByCat1(cat1);
        }else if (type == 2){
            if (cat2 == null) throw new CustomException(ErrorCode.INVALID_REQUEST_PARAM);
            return tourContentRepository.findCat3ByCat2AndCat1(cat1,cat2);
        }
        throw new CustomException(ErrorCode.INVALID_REQUEST_PARAM);
    }

    public List<TourContentResponse> getAllTourContent(){
        List<TourContent> lists = tourContentRepository.findAll();
        return lists.stream().map(TourEntityConverter::convertToTourContentResponse).toList();
    }

    public List<TourContentResponse> getTourContentByKeyword(String keyword){
        List<TourContent> lists = tourContentRepository.findTourContentWithKeyword(keyword);
        return lists.stream().map(TourEntityConverter::convertToTourContentResponse).toList();
    }

    public List<TourContentResponse> getTourContentByFilter(
            String cat1, String cat2, String cat3, Integer sigunguCode, String contentTypeId) {

        final String c2 = StringUtils.hasText(cat2) ? cat2 : null;
        final String c3 = StringUtils.hasText(cat3) ? cat3 : null;
        final String ct = StringUtils.hasText(contentTypeId) ? contentTypeId : null;
        final Integer sgg = (sigunguCode != null && sigunguCode != 0) ? sigunguCode : null;
        List<TourContent> lists = tourContentRepository.findTourContentByCat1(cat1);

        return lists.stream()
                .filter(item ->
                        (sgg == null || Objects.equals(item.getSigunguCode(), sgg)) &&
                        (c2 == null || Objects.equals(item.getCat2(), c2)) &&
                        (c3 == null || Objects.equals(item.getCat3(), c3)) &&
                        (ct == null || Objects.equals(item.getContentTypeId(), ct))
                )
                .map(TourEntityConverter::convertToTourContentResponse)
                .toList();
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

    @Transactional(readOnly = true)
    public TourContentDetailResponse convertToDetailResponse(String key) {

        TourContent tourContent = tourContentRepository.findById(key).orElse(null);
        if (tourContent == null) return null;

        TourContentResponse tourContentResponse = TourEntityConverter.convertToTourContentResponse(tourContent);
        List<TourDetailImageResponse> images = tourContent.getDetailImages().stream()
                .map(TourEntityConverter::convertToTourDetailImageResponse)
                .toList();
        List<TourDetailPetResponse> pets = tourContent.getDetailPet().stream()
                .map(TourEntityConverter::convertToTourDetailPetResponse)
                .toList();

        List<TourDetailRepeatCommonResponse> repeatCommons = tourContent.getDetailRepeats().stream()
                .filter(repeat -> repeat instanceof TourDetailRepeatCommon)
                .map(repeat -> TourEntityConverter.convertToTourDetailRepeatCommonResponse((TourDetailRepeatCommon) repeat))
                .toList();
        List<TourDetailRepeatRoomResponse> repeatRooms = tourContent.getDetailRepeats().stream()
                .filter(repeat -> repeat instanceof TourDetailRepeatRoom)
                .map(repeat -> TourEntityConverter.convertToTourDetailRepeatRoomResponse((TourDetailRepeatRoom) repeat))
                .toList();

        List<TourDetailIntroCultureResponse> introCultures = tourContent.getDetailIntros().stream()
                .filter(intro -> intro instanceof TourDetailIntroCulture)
                .map(intro -> TourEntityConverter.convertToTourDetailIntroCultureResponse((TourDetailIntroCulture) intro))
                .toList();
        List<TourDetailIntroFoodResponse> introFoods = tourContent.getDetailIntros().stream()
                .filter(intro -> intro instanceof TourDetailIntroFood)
                .map(intro -> TourEntityConverter.convertToTourDetailIntroFoodResponse((TourDetailIntroFood) intro))
                .toList();
        List<TourDetailIntroLeportsResponse> introLeports = tourContent.getDetailIntros().stream()
                .filter(intro -> intro instanceof TourDetailIntroLeports)
                .map(intro -> TourEntityConverter.convertToTourDetailIntroLeportsResponse((TourDetailIntroLeports) intro))
                .toList();
        List<TourDetailIntroLodgingResponse> introLodgings = tourContent.getDetailIntros().stream()
                .filter(intro -> intro instanceof TourDetailIntroLodging)
                .map(intro -> TourEntityConverter.convertToTourDetailIntroLodgingResponse((TourDetailIntroLodging) intro))
                .toList();
        List<TourDetailIntroShoppingResponse> introShopping = tourContent.getDetailIntros().stream()
                .filter(intro -> intro instanceof TourDetailIntroShopping)
                .map(intro -> TourEntityConverter.convertToTourDetailIntroShoppingResponse((TourDetailIntroShopping) intro))
                .toList();
        List<TourDetailIntroTouristResponse> introTourists = tourContent.getDetailIntros().stream()
                .filter(intro -> intro instanceof TourDetailIntroTourist)
                .map(intro -> TourEntityConverter.convertToTourDetailIntroTouristResponse((TourDetailIntroTourist) intro))
                .toList();

        return TourContentDetailResponse.builder()
                .tourContentResponse(tourContentResponse)
                .imagesInfo(images)
                .petsInfo(pets)
                .repeatCommons(repeatCommons.isEmpty() ? null : repeatCommons)
                .repeatRooms(repeatRooms.isEmpty() ? null : repeatRooms)
                .introCultures(introCultures.isEmpty() ? null : introCultures)
                .introFoods(introFoods.isEmpty() ? null : introFoods)
                .introLeports(introLeports.isEmpty() ? null : introLeports)
                .introLodgings(introLodgings.isEmpty() ? null : introLodgings)
                .introShopping(introShopping.isEmpty() ? null : introShopping)
                .introTourists(introTourists.isEmpty() ? null : introTourists)
                .build();
    }
}
