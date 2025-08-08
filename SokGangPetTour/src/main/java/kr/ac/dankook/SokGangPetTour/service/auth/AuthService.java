package kr.ac.dankook.SokGangPetTour.service.auth;

import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.LoginRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.MailRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.SignupRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.TokenResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.Role;
import kr.ac.dankook.SokGangPetTour.entity.TokenType;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import kr.ac.dankook.SokGangPetTour.jwt.JwtTokenProvider;
import kr.ac.dankook.SokGangPetTour.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthCacheService authCacheService;
    private final AuthMailService authMailService;

    private MailRequest generateMailRequest(String email,String randomCode) {
        String content = "해당 인증번호를 통해 인증을 완료해주세요. - " + randomCode;
        String title = "[PetGang Tour] 인증번호 안내 이메일입니다.";
        return new MailRequest(email,title,content);
    }

    public void sendCertificateCode(Member member){
        String randomCode = RandomStringUtils.randomAlphanumeric(6);
        MailRequest mail = generateMailRequest(member.getEmail(),randomCode);
        authMailService.sendMail(mail);
        authCacheService.saveCertificateCode(member,randomCode);
    }

    public List<String> getUserIdByNameAndEmail(String name, String email){
        List<Member> members = memberRepository.findByNameAndEmail(name, email);
        return members.stream().map(Member::getUserId).toList();
    }

    public boolean isDuplicatedId(String userId){
        return memberRepository.existsByUserId(userId);
    }

    @Transactional
    public void signup(SignupRequest signupRequest){
        if (memberRepository.existsByUserId(signupRequest.getUserId())){
            throw new CustomException(ErrorCode.DUPLICATE_ID);
        }
        Member newMember = Member.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .userId(signupRequest.getUserId())
                .role(Role.USER).build();
        memberRepository.save(newMember);
    }

    public TokenResponse login(LoginRequest loginRequest){

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getUserId(),loginRequest.getPassword());
        Authentication authentication = authenticationManager.getObject()
                .authenticate(authenticationToken);

        TokenResponse tokens = new TokenResponse(
                jwtTokenProvider.generateToken(authentication, TokenType.ACCESS_TOKEN),
                jwtTokenProvider.generateToken(authentication,TokenType.REFRESH_TOKEN)
        );
        return tokens;
    }
}
