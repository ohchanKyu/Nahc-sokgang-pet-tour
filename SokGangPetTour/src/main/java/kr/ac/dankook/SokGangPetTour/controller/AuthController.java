package kr.ac.dankook.SokGangPetTour.controller;

import jakarta.validation.Valid;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.LoginRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.SignupRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.MemberResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.TokenResponse;
import kr.ac.dankook.SokGangPetTour.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiMessageResponse> signup(
            @RequestBody @Valid SignupRequest signupRequest
            ){
        authService.signup(signupRequest);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201,"회원가입을 완료하였습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestBody @Valid LoginRequest loginRequest
    ){
        return ResponseEntity.status(200)
                .body(new ApiResponse<>(true,200,
                        authService.login(loginRequest)));
    }

}
