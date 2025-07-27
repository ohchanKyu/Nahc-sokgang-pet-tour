package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.service.VetPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vet")
@RequiredArgsConstructor
@Slf4j
public class VetPlaceController {

    private final VetPlaceService vetPlaceService;

    @GetMapping("/places")
    public ResponseEntity<ApiResponse<List<VetPlaceResponse>>> getAllVetPlaces(){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                vetPlaceService.getAllVetPlace()));
    }
}
