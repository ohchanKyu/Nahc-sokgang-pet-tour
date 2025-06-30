package kr.ac.dankook.SokGangPetTour.config.principal;

import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.exception.ApiErrorCode;
import kr.ac.dankook.SokGangPetTour.exception.ApiException;
import kr.ac.dankook.SokGangPetTour.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberEntity = memberRepository.findByUserId(username);
        return memberEntity.map(PrincipalDetails::new)
                .orElseThrow(() -> new ApiException(
                        ApiErrorCode.MEMBER_NOT_FOUND
                ));
    }
}
