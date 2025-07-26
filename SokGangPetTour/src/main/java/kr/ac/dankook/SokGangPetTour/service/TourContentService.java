package kr.ac.dankook.SokGangPetTour.service;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourContentRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailImageRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.TourDetailPetRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatCommonRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatRoomRequest;
import kr.ac.dankook.SokGangPetTour.entity.tour.*;
import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.*;
import kr.ac.dankook.SokGangPetTour.repository.tour.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.ac.dankook.SokGangPetTour.util.converter.TourEntityConverter.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourContentService {

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

    @Transactional
    public void saveOverviewData(String overview,String contentId){
        try{
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            tourContent.setOverview(overview);
            tourContentRepository.save(tourContent);
        }catch (Exception e){
            log.error("Error during tour content overview save. contentId - {} / - {}",contentId, e.getMessage());
        }
        log.info("Successfully save tour content overview.");
    }

    @Transactional
    public void saveAllSyncPetTourData(List<TourContentRequest> requestList){

        try{
            requestList.forEach(item -> {
                TourContent newContent = convertToTourContent(item);
                tourContentRepository.save(newContent);
            });
        }catch (Exception e) {
            log.error("Error during tour content save. - {}",e.getMessage());
        }
        log.info("Successfully save tour content.");
    }

    @Transactional
    public void savePetTourAccompanyingData(List<TourDetailPetRequest> requests,String contentId){

        try{
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailPet> lists = requests.stream()
                    .map(item -> convertToTourDetailPet(item,tourContent))
                    .toList();
            tourDetailPetRepository.saveAll(lists);
        }catch (Exception e){
            log.error("Error during tour pet detail save. contentId - {} / - {}",contentId, e.getMessage());
        }
        log.info("Successfully save tour pet detail.");
    }

    @Transactional
    public void savePetTourImageData(List<TourDetailImageRequest> requests,String contentId){
        try{
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailImage> lists = requests.stream()
                    .map(item -> convertToTourDetailImage(item,tourContent))
                    .toList();
            tourDetailImageRepository.saveAll(lists);
        }catch (Exception e){
            log.error("Error during tour pet image save. contentId - {} / - {}",contentId, e.getMessage());
        }
        log.info("Successfully save tour pet image.");
    }

    @Transactional
    public void savePetTourRepeatCommonData(List<TourDetailRepeatCommonRequest> requests, String contentId){
        try{
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailRepeatCommon> lists = requests.stream()
                    .map(item -> convertToTourDetailRepeatCommon(item,tourContent))
                    .toList();
            tourDetailRepeatCommonRepository.saveAll(lists);
        }catch (Exception e){
            log.error("Error during tour pet repeat common save. contentId - {} / - {}",contentId, e.getMessage());
        }
        log.info("Successfully save tour pet repeat common.");
    }

    @Transactional
    public void savePetTourRepeatRoomData(List<TourDetailRepeatRoomRequest> requests, String contentId){
        try{
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailRepeatRoom> lists = requests.stream()
                    .map(item -> convertToTourDetailRepeatRoom(item,tourContent))
                    .toList();
            tourDetailRepeatRoomRepository.saveAll(lists);
        }catch (Exception e){
            log.error("Error during tour pet repeat room save. contentId - {} / - {}",contentId, e.getMessage());
        }
        log.info("Successfully save tour pet repeat room.");
    }

    @Transactional
    public void savePetTourIntroCultureData(List<TourDetailIntroCultureRequest> requests, String contentId) {
        try {
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailIntroCulture> lists = requests.stream()
                    .map(item -> convertToTourDetailIntroCulture(item, tourContent))
                    .toList();
            tourDetailIntroCultureRepository.saveAll(lists);
        } catch (Exception e) {
            log.error("Error during tour intro culture save. contentId - {} / - {}", contentId, e.getMessage());
        }
        log.info("Successfully saved tour intro culture.");
    }

    @Transactional
    public void savePetTourIntroFoodData(List<TourDetailIntroFoodRequest> requests, String contentId) {
        try {
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailIntroFood> lists = requests.stream()
                    .map(item -> convertToTourDetailIntroFood(item, tourContent))
                    .toList();
            tourDetailIntroFoodRepository.saveAll(lists);
        } catch (Exception e) {
            log.error("Error during tour intro food save. contentId - {} / - {}", contentId, e.getMessage());
        }
        log.info("Successfully saved tour intro food.");
    }

    @Transactional
    public void savePetTourIntroLeportsData(List<TourDetailIntroLeportsRequest> requests, String contentId) {
        try {
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailIntroLeports> lists = requests.stream()
                    .map(item -> convertToTourDetailIntroLeports(item, tourContent))
                    .toList();
            tourDetailIntroLeportsRepository.saveAll(lists);
        } catch (Exception e) {
            log.error("Error during tour intro leports save. contentId - {} / - {}", contentId, e.getMessage());
        }
        log.info("Successfully saved tour intro leports.");
    }

    @Transactional
    public void savePetTourIntroLodgingData(List<TourDetailIntroLodgingRequest> requests, String contentId) {
        try {
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailIntroLodging> lists = requests.stream()
                    .map(item -> convertToTourDetailIntroLodging(item, tourContent))
                    .toList();
            tourDetailIntroLodgingRepository.saveAll(lists);
        } catch (Exception e) {
            log.error("Error during tour intro lodging save. contentId - {} / - {}", contentId, e.getMessage());
        }
        log.info("Successfully saved tour intro lodging.");
    }

    @Transactional
    public void savePetTourIntroShoppingData(List<TourDetailIntroShoppingRequest> requests, String contentId) {
        try {
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailIntroShopping> lists = requests.stream()
                    .map(item -> convertToTourDetailIntroShopping(item, tourContent))
                    .toList();
            tourDetailIntroShoppingRepository.saveAll(lists);
        } catch (Exception e) {
            log.error("Error during tour intro shopping save. contentId - {} / - {}", contentId, e.getMessage());
        }
        log.info("Successfully saved tour intro shopping.");
    }

    @Transactional
    public void savePetTourIntroTouristData(List<TourDetailIntroTouristRequest> requests, String contentId) {
        try {
            TourContent tourContent = tourContentRepository.findById(contentId)
                    .orElseThrow(() -> new RuntimeException("엔티티가 존재하지 않습니다."));
            List<TourDetailIntroTourist> lists = requests.stream()
                    .map(item -> convertToTourDetailIntroTourist(item, tourContent))
                    .toList();
            tourDetailIntroTouristRepository.saveAll(lists);
        } catch (Exception e) {
            log.error("Error during tour intro tourist save. contentId - {} / - {}", contentId, e.getMessage());
        }
        log.info("Successfully saved tour intro tourist.");
    }


}
