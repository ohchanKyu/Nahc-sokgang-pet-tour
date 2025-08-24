package kr.ac.dankook.SokGangPetTour.controller;

import jakarta.validation.Valid;
import kr.ac.dankook.SokGangPetTour.config.principal.PrincipalDetails;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.MemberInfoChangeRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.memberRequest.MemberPasswordChangeRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.MemberResponse;
import kr.ac.dankook.SokGangPetTour.service.auth.AuthService;
import kr.ac.dankook.SokGangPetTour.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

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

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Boolean>> editPassword(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody @Valid MemberPasswordChangeRequest request
            ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                memberService.editMemberPassword(user.getMember(), request.getOriginalPassword(),
                        request.getNewPassword())));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiMessageResponse> editUserInfo(
            @AuthenticationPrincipal PrincipalDetails user,
            @RequestBody @Valid MemberInfoChangeRequest request
    ){
        memberService.editMemberInfo(user.getMember(), request.getName(),request.getEmail());
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,"회원 정보를 변경하였습니다."));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiMessageResponse> deleteMember(
            @AuthenticationPrincipal PrincipalDetails user
    ) throws InterruptedException {
        memberService.deleteMember(user.getMember());
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,"회원 탈퇴가 완료되었습니다."));
    }
 }
