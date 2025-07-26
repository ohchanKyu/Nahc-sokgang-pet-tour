package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatCommonRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatRoomRequest;
import kr.ac.dankook.SokGangPetTour.service.TourContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour")
@RequiredArgsConstructor
@Slf4j
public class TourApiController {

    private final TourContentService tourContentService;

    @PostMapping("/sync")
    public ResponseEntity<?> saveSyncData(@RequestBody List<TourContentRequest> list){
        tourContentService.saveAllSyncPetTourData(list);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/common/{contentId}")
    public ResponseEntity<?> saveOverviewData(@PathVariable String contentId , @RequestBody String overview){
        tourContentService.saveOverviewData(overview,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/pet/{contentId}")
    public ResponseEntity<?> savePetData(@PathVariable String contentId, @RequestBody List<TourDetailPetRequest> list){
        tourContentService.savePetTourAccompanyingData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/image/{contentId}")
    public ResponseEntity<?> saveImageData(@PathVariable String contentId, @RequestBody List<TourDetailImageRequest> list){
        tourContentService.savePetTourImageData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/repeat/common/{contentId}")
    public ResponseEntity<?> saveRepeatCommonData(
            @PathVariable String contentId, @RequestBody List<TourDetailRepeatCommonRequest> list){
        tourContentService.savePetTourRepeatCommonData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/repeat/room/{contentId}")
    public ResponseEntity<?> saveRepeatRoomData(
            @PathVariable String contentId, @RequestBody List<TourDetailRepeatRoomRequest> list){
        tourContentService.savePetTourRepeatRoomData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/intro/culture/{contentId}")
    public ResponseEntity<?> saveIntroCultureData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroCultureRequest> list) {

        tourContentService.savePetTourIntroCultureData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/intro/food/{contentId}")
    public ResponseEntity<?> saveIntroFoodData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroFoodRequest> list) {

        tourContentService.savePetTourIntroFoodData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/intro/leports/{contentId}")
    public ResponseEntity<?> saveIntroLeportsData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroLeportsRequest> list) {

        tourContentService.savePetTourIntroLeportsData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/intro/lodging/{contentId}")
    public ResponseEntity<?> saveIntroLodgingData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroLodgingRequest> list) {

        tourContentService.savePetTourIntroLodgingData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/intro/shopping/{contentId}")
    public ResponseEntity<?> saveIntroShoppingData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroShoppingRequest> list) {

        tourContentService.savePetTourIntroShoppingData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
    @PostMapping("/intro/tourist/{contentId}")
    public ResponseEntity<?> saveIntroTouristData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroTouristRequest> list) {

        tourContentService.savePetTourIntroTouristData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
}
