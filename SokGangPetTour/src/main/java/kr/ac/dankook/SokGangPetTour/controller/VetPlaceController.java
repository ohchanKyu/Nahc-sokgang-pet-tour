package kr.ac.dankook.SokGangPetTour.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceDistResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.service.VetPlaceCacheService;
import kr.ac.dankook.SokGangPetTour.service.VetPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vet")
@RequiredArgsConstructor
@Slf4j
public class VetPlaceController {

    private final VetPlaceService vetPlaceService;
    private final VetPlaceCacheService vetPlaceCacheService;

    @GetMapping("/places")
    public ResponseEntity<ApiResponse<List<VetPlaceResponse>>> getAllVetPlaces(){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getAllVetPlace()));
    }

    @GetMapping("/place/{id}")
    public ResponseEntity<ApiResponse<VetPlaceResponse>> getVetPlace(@PathVariable String id) throws JsonProcessingException {
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceCacheService.getVetPlacesById(id)));
    }

    @GetMapping("/place/search")
    public ResponseEntity<ApiResponse<List<VetPlaceResponse>>> getVetPlacesByFilter(
            @RequestParam("keyword") String keyword
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getVetPlacesByKeyword(keyword)));
    }

    @GetMapping("/place/dist")
    public ResponseEntity<ApiResponse<List<VetPlaceDistResponse>>> getVetPlaceByDist(
            @RequestParam(value = "latitude", required = true) double latitude,
            @RequestParam(value = "longitude", required = true) double longitude
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getVetPlaceOrderByDist(latitude,longitude)));
    }
}
