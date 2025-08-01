package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.kakaoResponse.CoordinateResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.kakaoResponse.RouteResponse;
import kr.ac.dankook.SokGangPetTour.service.KakaoLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao")
@Slf4j
public class KakaoLocationController {

    private final KakaoLocationService kakaoLocationService;

    @GetMapping("/coordinate")
    public ResponseEntity<ApiResponse<CoordinateResponse>> getCoordinateByExternalApi(
            @RequestParam(value = "address") String address
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                kakaoLocationService.getCoordinateByAddress(address)));
    }

    @GetMapping("/route")
    public ResponseEntity<ApiResponse<RouteResponse>> getRouteByExternalApi(
            @RequestParam(value = "statLatitude") double statLatitude,
            @RequestParam(value = "statLongitude") double statLongitude,
            @RequestParam(value = "desLatitude") double desLatitude,
            @RequestParam(value = "desLongitude") double desLongitude
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                kakaoLocationService.getRouteByCoordinate(statLatitude,statLongitude,
                        desLatitude,desLongitude)));
    }
}
