package kr.ac.dankook.SokGangPetTour.config.converter;

import kr.ac.dankook.SokGangPetTour.dto.response.authResponse.MemberResponse;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.exception.ApiErrorCode;
import kr.ac.dankook.SokGangPetTour.exception.ApiException;
import kr.ac.dankook.SokGangPetTour.repository.MemberRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MemberEntityConverter {

    private final MemberRepository memberRepository;

    private Member getMemberFromRepository(Long memberId){
        Optional<Member> targetMember = memberRepository.findById(memberId);
        if(targetMember.isPresent()){
            return targetMember.get();
        }
        throw new ApiException(ApiErrorCode.MEMBER_NOT_FOUND);
    }

    @Transactional(readOnly = true)
    public Member getMemberByMemberId(Long memberId){
        return getMemberFromRepository(memberId);
    }

    @Transactional(readOnly = true)
    public Member getMemberByEncryptMemberId(String memberId){
        Long decryptMemberId = EncryptionUtil.decrypt(memberId);
        return getMemberFromRepository(decryptMemberId);
    }

    public MemberResponse convertMemberEntity(Member member){
        return MemberResponse.builder()
                .id(EncryptionUtil.encrypt(member.getId()))
                .name(member.getName())
                .userId(member.getUserId())
                .email(member.getEmail())
                .createTime(member.getCreatedDateTime())
                .roles(member.getRoles())
                .build();
    }
}
