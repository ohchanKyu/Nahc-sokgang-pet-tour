package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro.*;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatCommonRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat.TourDetailRepeatRoomRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.tourResponse.TourSyncResponse;
import kr.ac.dankook.SokGangPetTour.service.tour.TourContentSyncService;
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

    private final TourContentSyncService tourContentSyncService;

    @PostMapping
    public ResponseEntity<ApiMessageResponse> saveSyncData(
            @RequestBody List<TourContentRequest> list){
        tourContentSyncService.saveAllSyncPetTourData(list);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_SYNC_DATA.getMessage()));
    }

    @PostMapping("/common/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveOverviewData(
            @PathVariable String contentId , @RequestParam("overview") String overview){
        tourContentSyncService.saveOverviewData(overview,contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_COMMON_DATA.getMessage()));
    }

    @PostMapping("/pet/{contentId}")
    public ResponseEntity<ApiMessageResponse> savePetData(
            @PathVariable String contentId, @RequestBody List<TourDetailPetRequest> list){
        tourContentSyncService.savePetTourAccompanyingData(list,contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_PET_DATA.getMessage()));
    }

    @PostMapping("/image/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveImageData(
            @PathVariable String contentId, @RequestBody List<TourDetailImageRequest> list){
        tourContentSyncService.savePetTourImageData(list,contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_IMAGE_DATA.getMessage()));
    }

    @PostMapping("/repeat/common/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveRepeatCommonData(
            @PathVariable String contentId, @RequestBody List<TourDetailRepeatCommonRequest> list){
        tourContentSyncService.savePetTourRepeatCommonData(list,contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_REPEAT_DATA.getMessage()));
    }

    @PostMapping("/repeat/room/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveRepeatRoomData(
            @PathVariable String contentId, @RequestBody List<TourDetailRepeatRoomRequest> list){
        tourContentSyncService.savePetTourRepeatRoomData(list,contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_REPEAT_DATA.getMessage()));
    }

    @PostMapping("/intro/culture/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveIntroCultureData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroCultureRequest> list) {

        tourContentSyncService.savePetTourIntroCultureData(list, contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_INTRO_DATA.getMessage()));
    }

    @PostMapping("/intro/food/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveIntroFoodData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroFoodRequest> list) {

        tourContentSyncService.savePetTourIntroFoodData(list, contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_INTRO_DATA.getMessage()));
    }

    @PostMapping("/intro/leports/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveIntroLeportsData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroLeportsRequest> list) {

        tourContentSyncService.savePetTourIntroLeportsData(list, contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_INTRO_DATA.getMessage()));
    }

    @PostMapping("/intro/lodging/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveIntroLodgingData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroLodgingRequest> list) {
        tourContentSyncService.savePetTourIntroLodgingData(list, contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_INTRO_DATA.getMessage()));
    }

    @PostMapping("/intro/shopping/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveIntroShoppingData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroShoppingRequest> list) {
        tourContentSyncService.savePetTourIntroShoppingData(list, contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_INTRO_DATA.getMessage()));
    }

    @PostMapping("/intro/tourist/{contentId}")
    public ResponseEntity<ApiMessageResponse> saveIntroTouristData(
            @PathVariable String contentId,
            @RequestBody List<TourDetailIntroTouristRequest> list) {
        tourContentSyncService.savePetTourIntroTouristData(list, contentId);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201, TourSyncResponse.SUCCESS_INTRO_DATA.getMessage()));
    }
}
