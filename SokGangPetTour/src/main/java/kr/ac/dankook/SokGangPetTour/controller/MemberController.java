package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.config.principal.PrincipalDetails;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.MemberResponse;
import kr.ac.dankook.SokGangPetTour.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class MemberController {

    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getCurrentMember(
        @AuthenticationPrincipal PrincipalDetails user
    )
    {
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                new MemberResponse(user.getMember())));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiMessageResponse> logout(
            @AuthenticationPrincipal PrincipalDetails user){
        authService.logout(user.getUsername());
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,
                "로그아웃에 성공하였습니다."));
    }
}
