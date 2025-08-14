package kr.ac.dankook.SokGangPetTour.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceDistResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.PlaceEntityType;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceCategory;
import kr.ac.dankook.SokGangPetTour.service.PlaceStaticService;
import kr.ac.dankook.SokGangPetTour.service.vetPlace.VetPlaceCacheService;
import kr.ac.dankook.SokGangPetTour.service.vetPlace.VetPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vet")
@RequiredArgsConstructor
@Slf4j
public class VetPlaceController {

    private final VetPlaceService vetPlaceService;
    private final VetPlaceCacheService vetPlaceCacheService;
    private final PlaceStaticService placeStaticService;

    // 모든 장소 데이터
    @GetMapping("/places")
    public ResponseEntity<ApiResponse<List<VetPlaceResponse>>> getAllVetPlaces(){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getAllVetPlace()));
    }
    // 특정 장소 데이터
    @GetMapping("/place/{id}")
    public ResponseEntity<ApiResponse<VetPlaceResponse>> getVetPlace(@PathVariable String id) throws JsonProcessingException {
        // Async
        placeStaticService.countUp(id, PlaceEntityType.VET);
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceCacheService.getVetPlacesById(id)));
    }
    // 키워드로 검색
    @GetMapping("/place/search")
    public ResponseEntity<ApiResponse<List<VetPlaceResponse>>> getVetPlacesByFilter(
            @RequestParam("keyword") String keyword
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getVetPlacesByKeyword(keyword)));
    }
    // 좌표를 통해 거리순으로 데이터 반환 -> 거리 정보 포함
    @GetMapping("/place/dist")
    public ResponseEntity<ApiResponse<List<VetPlaceDistResponse>>> getVetPlaceByDist(
            @RequestParam(value = "latitude") double latitude,
            @RequestParam(value = "longitude") double longitude
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getVetPlaceOrderByDist(latitude,longitude)));
    }

    // 카테고리, 주차 유무, 현재 운영중인 데이터를 필터를 통해 반환
    @GetMapping("/place/filter")
    public ResponseEntity<ApiResponse<List<VetPlaceResponse>>> getVetPlaceByFilter(
            @RequestParam("category") VetPlaceCategory category,
            @RequestParam(value = "isParking", required = false) boolean isParking,
            @RequestParam(value = "isOpen", required = false) boolean isOpen
            ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getVetPlacesByFilter(category,isParking,isOpen)));
    }
    // 오늘 하루 조회 순
    @GetMapping("/place/count")
    public ResponseEntity<ApiResponse<List<VetPlaceResponse>>> getVetPlaceCount(){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                placeStaticService.getVetPlaceByCount()));
    }
}
