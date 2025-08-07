package kr.ac.dankook.SokGangPetTour.service.auth;

import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.SignupRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.MemberResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.Role;
import kr.ac.dankook.SokGangPetTour.repository.MemberRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse saveNewMember(SignupRequest signupRequest){

        Member newMember = Member.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .userId(signupRequest.getUserId())
                .role(Role.USER).build();
        Member newEntity = memberRepository.save(newMember);
        return convertToDtoResponse(newEntity);
    }

    private MemberResponse convertToDtoResponse(Member member){
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .userId(member.getUserId())
                .email(member.getEmail())
                .role(member.getRole().name()).createTime(member.getCreatedDateTime()).build();
    }
}
