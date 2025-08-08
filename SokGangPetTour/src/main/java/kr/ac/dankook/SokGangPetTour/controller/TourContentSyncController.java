package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatCommonRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatRoomRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.tourResponse.TourSyncResponse;
import kr.ac.dankook.SokGangPetTour.service.tour.TourContentUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tour/sync")
@RequiredArgsConstructor
@Slf4j
public class TourContentSyncController {

    private final TourContentUpdateService tourContentUpdateService;

    @PostMapping("")
    public ResponseEntity<ApiMessageResponse> saveSyncData(
            @RequestBody List<TourContentRequest> list){
        tourContentUpdateService.saveAllSyncPetTourData(list);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_SYNC_ALL_DATE.getMessage()));
    }

    @PostMapping("/common/{contentId}")
    public ResponseEntity<?> saveOverviewData(
            @PathVariable String contentId , @RequestBody String overview){
        tourContentUpdateService.saveOverviewData(overview,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/pet/{contentId}")
    public ResponseEntity<?> savePetData(
            @PathVariable String contentId, @RequestBody List<TourDetailPetRequest> list){
        tourContentUpdateService.savePetTourAccompanyingData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/image/{contentId}")
    public ResponseEntity<?> saveImageData(
            @PathVariable String contentId, @RequestBody List<TourDetailImageRequest> list){
        tourContentUpdateService.savePetTourImageData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/repeat/common/{contentId}")
    public ResponseEntity<?> saveRepeatCommonData(
            @PathVariable String contentId, @RequestBody List<TourDetailRepeatCommonRequest> list){
        tourContentUpdateService.savePetTourRepeatCommonData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/repeat/room/{contentId}")
    public ResponseEntity<?> saveRepeatRoomData(
            @PathVariable String contentId, @RequestBody List<TourDetailRepeatRoomRequest> list){
        tourContentUpdateService.savePetTourRepeatRoomData(list,contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/intro/culture/{contentId}")
    public ResponseEntity<?> saveIntroCultureData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroCultureRequest> list) {

        tourContentUpdateService.savePetTourIntroCultureData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/intro/food/{contentId}")
    public ResponseEntity<?> saveIntroFoodData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroFoodRequest> list) {

        tourContentUpdateService.savePetTourIntroFoodData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/intro/leports/{contentId}")
    public ResponseEntity<?> saveIntroLeportsData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroLeportsRequest> list) {

        tourContentUpdateService.savePetTourIntroLeportsData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/intro/lodging/{contentId}")
    public ResponseEntity<?> saveIntroLodgingData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroLodgingRequest> list) {
        tourContentUpdateService.savePetTourIntroLodgingData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/intro/shopping/{contentId}")
    public ResponseEntity<?> saveIntroShoppingData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroShoppingRequest> list) {
        tourContentUpdateService.savePetTourIntroShoppingData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }

    @PostMapping("/intro/tourist/{contentId}")
    public ResponseEntity<?> saveIntroTouristData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroTouristRequest> list) {
        tourContentUpdateService.savePetTourIntroTouristData(list, contentId);
        return ResponseEntity.ok("데이터 저장 완료");
    }
}
