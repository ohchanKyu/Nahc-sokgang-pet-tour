package kr.ac.dankook.SokGangPetTour.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentDetailResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentDistResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.PlaceEntityType;
import kr.ac.dankook.SokGangPetTour.service.PlaceStaticService;
import kr.ac.dankook.SokGangPetTour.service.tour.TourContentCacheService;
import kr.ac.dankook.SokGangPetTour.service.tour.TourContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tour")
@RequiredArgsConstructor
@Slf4j
public class TourContentController {

    private final TourContentService tourContentService;
    private final TourContentCacheService tourContentCacheService;
    private final PlaceStaticService placeStaticService;

    // 모든 기본 데이터 반환
    @GetMapping("/places")
    public ResponseEntity<ApiResponse<List<TourContentResponse>>> getAllPlaces(){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                tourContentService.getAllTourContent()));
    }
    // 장소에 대한 상세정보 반환 -> 캐시 사용
    @GetMapping("/place/{placeId}")
    public ResponseEntity<ApiResponse<TourContentDetailResponse>> getPlaceWithDetail(
            @PathVariable String placeId) throws JsonProcessingException {
        // Async
        placeStaticService.countUp(placeId, PlaceEntityType.TOUR);
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                tourContentCacheService.findTourContentDetailById(placeId)));
    }

    // 장소 이름 또는 주소 검색을 통해 반환
    @GetMapping("/place/search")
    public ResponseEntity<ApiResponse<List<TourContentResponse>>> getPlacesByKeyword(
            @RequestParam("keyword") String keyword
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                tourContentService.getTourContentByKeyword(keyword)));
    }

    // 좌표를 통해 거리순으로 데이터 반환 -> 거리 정보 포함
    @GetMapping("/place/dist")
    public ResponseEntity<ApiResponse<List<TourContentDistResponse>>> getVetPlaceByDist(
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                tourContentService.getTourPlaceOrderByDist(latitude, longitude)));
    }

    @GetMapping("/place/filter")
    public ResponseEntity<ApiResponse<List<TourContentResponse>>> getVetPlaceByFilter(
            // 대분류는 필수 항목
            @RequestParam(value = "cat1") String cat1,
            @RequestParam(value = "cat2", required = false) String cat2,
            @RequestParam(value = "cat3", required = false) String cat3,
            @RequestParam(value = "sigunguCode", required = false ,defaultValue = "0") int sigunguCode,
            @RequestParam(value = "contentTypeId", required = false) String contentTypeId
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                tourContentService.getTourContentByFilter(cat1,cat2,cat3,sigunguCode, contentTypeId )));
    }

    @GetMapping("/category/{type}")
    public ResponseEntity<ApiResponse<List<String>>> getCategory(
            @PathVariable int type,
            @RequestParam("cat1") String cat1,
            @RequestParam(value = "cat2",required = false) String cat2
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                tourContentService.getCategoryByParentCategory(type,cat1,cat2)));
    }
    
    // 오늘 조회 순
    @GetMapping("/place/count")
    public ResponseEntity<ApiResponse<List<TourContentResponse>>> getTourPlaceCount(){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                placeStaticService.getTourPlaceByCount()));
    }
}
