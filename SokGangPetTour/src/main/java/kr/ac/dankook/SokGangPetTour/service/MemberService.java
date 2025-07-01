package kr.ac.dankook.SokGangPetTour.service;

import kr.ac.dankook.SokGangPetTour.config.converter.MemberEntityConverter;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.FindIdRequest;
import kr.ac.dankook.SokGangPetTour.entity.*;
import kr.ac.dankook.SokGangPetTour.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberEntityConverter memberEntityConverter;

    @Transactional
    public boolean deleteMemberProcess(Long memberId){
        Member member = memberEntityConverter.getMemberByMemberId(memberId);
        memberRepository.deleteById(memberId);
        return true;
    }

    @Transactional(readOnly = true)
    public List<String> findUserIdProcess(FindIdRequest findIdRequest){
        return memberRepository.findByNameAndEmail(findIdRequest.getName(), findIdRequest.getEmail())
                .stream()
                .map(Member::getUserId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Member findMemberByUserIdProcess(String userId){
        Optional<Member> member = memberRepository.findByUserId(userId);
        return member.orElse(null);
    }
}
