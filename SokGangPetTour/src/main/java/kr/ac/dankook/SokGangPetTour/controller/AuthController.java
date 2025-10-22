package kr.ac.dankook.SokGangPetTour.controller;

import jakarta.validation.Valid;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.LoginRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.PasswordChangeRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.SignupRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.TokenResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.service.auth.AuthCacheService;
import kr.ac.dankook.SokGangPetTour.service.auth.AuthService;
import kr.ac.dankook.SokGangPetTour.service.auth.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;
    private final AuthCacheService authCacheService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiMessageResponse> signup(
            @RequestBody @Valid SignupRequest signupRequest
    ){
        authService.signup(signupRequest);
        return ResponseEntity.status(201)
                .body(new ApiMessageResponse(true,201,"회원가입을 완료하였습니다."));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestBody @Valid LoginRequest loginRequest
    ){
        return ResponseEntity.status(200)
                .body(new ApiResponse<>(true,200,
                        authService.login(loginRequest)));
    }

    // 아이디 중복 체크
    @GetMapping("/duplicate")
    public ResponseEntity<ApiResponse<Boolean>> checkDuplicatedId(
            @RequestParam("userId") String userId){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                authService.isDuplicatedId(userId)));
    }

    // 아이디 찾기 - 이름,이메일 -> 아이디 리스트 ( 아이디만 중복 X )
    @GetMapping("/find-id")
    public ResponseEntity<ApiResponse<List<String>>> findId(
            @RequestParam("name") String name,
            @RequestParam("email") String email
    ){
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                authService.getUserIdByNameAndEmail(name,email)));
    }
    // 비밀번호 찾기를 위한 인증번호 전송
    @PostMapping("/find-password/certificate")
    public ResponseEntity<ApiResponse<String>> sendCertificateCode(
            @RequestParam("userId") String userId
    ){
        Member member = memberService.getCurrentMember(userId);
        authService.sendCertificateCode(member);
        return ResponseEntity.status(201).body(new ApiResponse<>(true,200,
                userId));
    }

    // 인증번호 검사
    @GetMapping("/find-password/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifyCertificateCode(
            @RequestParam("userId") String userId,
            @RequestParam("code") String code
    ){
        Member member = memberService.getCurrentMember(userId);
        return ResponseEntity.status(200).body(new ApiResponse<>(true,200,
                authCacheService.isVerifyCode(member,code)));
    }

    // 인증 완료 후 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<ApiMessageResponse> changePassword(
            @RequestParam("userId") String userId,
            @RequestBody @Valid PasswordChangeRequest request
    ){
        Member member = memberService.getCurrentMember(userId);
        memberService.updatePassword(member,request.getNewPassword());
        authCacheService.deleteKey(authCacheService.generateKey(member));
        return ResponseEntity.status(200).body(new ApiMessageResponse(true,200,
                "비밀번호 변경을 완료하였습니다."));
    }

    // 토큰 갱신
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(
            @RequestParam("refreshToken") String refreshToken
    ){
        return ResponseEntity.status(201).body(new ApiResponse<>(true,200,
                authService.reissueToken(refreshToken)));
    }
}
