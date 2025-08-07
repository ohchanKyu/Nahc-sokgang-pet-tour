package kr.ac.dankook.SokGangPetTour.service.auth;

import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.LoginRequest;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.SignupRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.MemberResponse;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

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
