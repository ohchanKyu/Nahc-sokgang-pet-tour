package kr.ac.dankook.SokGangPetTour.service.tour;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourContentRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailImageRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailPetRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatCommonRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatRoomRequest;
import kr.ac.dankook.SokGangPetTour.entity.tour.*;
import kr.ac.dankook.SokGangPetTour.error.exception.EntityNotFoundException;
import kr.ac.dankook.SokGangPetTour.repository.tour.*;
import kr.ac.dankook.SokGangPetTour.util.converter.TourEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static kr.ac.dankook.SokGangPetTour.util.converter.TourEntityConverter.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourContentSyncService {

    private final TourContentRepository tourContentRepository;
    private final TourDetailPetRepository tourDetailPetRepository;
    private final TourDetailImageRepository tourDetailImageRepository;
    private final TourDetailRepeatCommonRepository tourDetailRepeatCommonRepository;
    private final TourDetailRepeatRoomRepository tourDetailRepeatRoomRepository;
    private final TourDetailIntroCultureRepository tourDetailIntroCultureRepository;
    private final TourDetailIntroFoodRepository tourDetailIntroFoodRepository;
    private final TourDetailIntroLeportsRepository tourDetailIntroLeportsRepository;
    private final TourDetailIntroLodgingRepository tourDetailIntroLodgingRepository;
    private final TourDetailIntroShoppingRepository tourDetailIntroShoppingRepository;
    private final TourDetailIntroTouristRepository tourDetailIntroTouristRepository;
    private final TourDataSyncComponent tourDataSyncComponent;

    @Transactional
    public void saveAllSyncPetTourData(List<TourContentRequest> requestList){

        requestList.forEach(item -> {
                    TourContent tourContent = tourContentRepository
                            .findById(item.getContentId()).orElse(null);
                    if (tourContent != null) {
                        tourContentRepository.save(updateTourContentFromDto(tourContent, item));
                    } else {
                        TourContent newContent = convertToTourContent(item);
                        tourContentRepository.save(newContent);
                    }
        });
        log.info("Successfully save all tour content.");
    }

    @Transactional
    public void saveOverviewData(String overview,String contentId){

        TourContent tourContent = tourContentRepository.findById(contentId)
                .orElseThrow(() -> new EntityNotFoundException("해당 장소 데이터가 존재하지 않습니다."));
        tourContent.setOverview(overview);
        tourContentRepository.save(tourContent);
        log.info("Successfully save tour content overview.");
    }

    public void savePetTourImageData(List<TourDetailImageRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailImage,
                tourDetailImageRepository::deleteByContent,
                tourDetailImageRepository::saveAll,
                "Successfully saved tour pet image."
        );
    }

    public void savePetTourAccompanyingData(List<TourDetailPetRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailPet,
                tourDetailPetRepository::deleteByContent,
                tourDetailPetRepository::saveAll,
                "Successfully saved tour pet detail."
        );
    }

    public void savePetTourRepeatCommonData(List<TourDetailRepeatCommonRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailRepeatCommon,
                tourDetailRepeatCommonRepository::deleteByContent,
                tourDetailRepeatCommonRepository::saveAll,
                "Successfully saved tour pet repeat common."
        );
    }

    public void savePetTourRepeatRoomData(List<TourDetailRepeatRoomRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailRepeatRoom,
                tourDetailRepeatRoomRepository::deleteByContent,
                tourDetailRepeatRoomRepository::saveAll,
                "Successfully saved tour pet repeat room."
        );
    }

    public void savePetTourIntroCultureData(List<TourDetailIntroCultureRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailIntroCulture,
                tourDetailIntroCultureRepository::deleteByContent,
                tourDetailIntroCultureRepository::saveAll,
                "Successfully saved tour intro culture."
        );
    }

    public void savePetTourIntroFoodData(List<TourDetailIntroFoodRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailIntroFood,
                tourDetailIntroFoodRepository::deleteByContent,
                tourDetailIntroFoodRepository::saveAll,
                "Successfully saved tour intro food."
        );
    }

    public void savePetTourIntroLeportsData(List<TourDetailIntroLeportsRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailIntroLeports,
                tourDetailIntroLeportsRepository::deleteByContent,
                tourDetailIntroLeportsRepository::saveAll,
                "Successfully saved tour intro leports."
        );
    }

    public void savePetTourIntroLodgingData(List<TourDetailIntroLodgingRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailIntroLodging,
                tourDetailIntroLodgingRepository::deleteByContent,
                tourDetailIntroLodgingRepository::saveAll,
                "Successfully saved tour intro lodging."
        );
    }

    public void savePetTourIntroShoppingData(List<TourDetailIntroShoppingRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailIntroShopping,
                tourDetailIntroShoppingRepository::deleteByContent,
                tourDetailIntroShoppingRepository::saveAll,
                "Successfully saved tour intro shopping."
        );
    }

    public void savePetTourIntroTouristData(List<TourDetailIntroTouristRequest> requests, String contentId){
        tourDataSyncComponent.replaceChildren(
                contentId,
                requests,
                TourEntityConverter::convertToTourDetailIntroTourist,
                tourDetailIntroTouristRepository::deleteByContent,
                tourDetailIntroTouristRepository::saveAll,
                "Successfully saved tour intro tourist."
        );
    }
}
