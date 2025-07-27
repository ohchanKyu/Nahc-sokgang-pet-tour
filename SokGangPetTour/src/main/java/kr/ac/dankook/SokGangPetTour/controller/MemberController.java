package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.util.converter.MemberEntityConverter;
import kr.ac.dankook.SokGangPetTour.config.principal.PrincipalDetails;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiMessageResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.ApiResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.MemberResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.service.AuthService;
import kr.ac.dankook.SokGangPetTour.service.MemberService;
import kr.ac.dankook.SokGangPetTour.util.DecryptId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/api/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberEntityConverter memberEntityConverter;
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMember(@PathVariable @DecryptId Long id) {
        Member member = memberEntityConverter.getMemberByMemberId(id);
        MemberResponse apiResponse = memberEntityConverter.convertMemberEntity(member);
        return ResponseEntity.ok(
                new ApiResponse<>(true,200, apiResponse));
    }
    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteMember(@PathVariable @DecryptId Long memberId){
        return ResponseEntity.ok(new ApiResponse<>(true,200,
                memberService.deleteMemberProcess(memberId)));
    }
    @PostMapping("/logout")
    public ResponseEntity<ApiMessageResponse> logoutMember(@AuthenticationPrincipal PrincipalDetails userDetails){
        if (authService.logoutProcess(userDetails)){
            return ResponseEntity.ok(new ApiMessageResponse(true,200,"Logout Success"));
        }else{
            return ResponseEntity.ok(new ApiMessageResponse(false,401,"Already Log Out"));
        }
    }
}
