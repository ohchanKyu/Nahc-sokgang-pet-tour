package kr.ac.dankook.SokGangPetTour.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import kr.ac.dankook.SokGangPetTour.config.principal.PrincipalDetails;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.TokenType;
import kr.ac.dankook.SokGangPetTour.repository.MemberRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30 minutes
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30; // 30 day
    private final MemberRepository memberRepository;

    // 토큰 생성
    public String generateToken(Authentication authentication,TokenType tokenType) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();

        long now = (new Date()).getTime();

        Date expired = tokenType == TokenType.ACCESS_TOKEN ?
                new Date(now + ACCESS_TOKEN_EXPIRE_TIME) :
                new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(expired)
                // AES 암호화된 키
                .withClaim("key", EncryptionUtil.encrypt(member.getId()))
                // 사용자 권한
                .withClaim("role",member.getRole().getTitle())
                .sign(Algorithm.HMAC512(secretKey));
    }

    // Token 검증
    public Authentication validateToken(String jwtToken){
        String key = getUserKeyFromToken(jwtToken);
        Long decryptId = EncryptionUtil.decrypt(key);
        // DB 부하 감소를 위해 Redis 사용 고려
        Optional<Member> entity = memberRepository.findById(decryptId);
        if (entity.isPresent()) {
            PrincipalDetails principalDetails = new PrincipalDetails(entity.get());
            return new UsernamePasswordAuthenticationToken(
                    principalDetails, jwtToken, principalDetails.getAuthorities());
        }
        return null;
    }

    private String getUserKeyFromToken(String jwtToken){
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build().verify(jwtToken).getClaim("key").asString();
    }
}
