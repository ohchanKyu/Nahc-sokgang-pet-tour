package kr.ac.dankook.SokGangPetTour.service.auth;

import kr.ac.dankook.SokGangPetTour.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthCacheService {

    private final RedisTemplate<String,String> redisTemplate;
    private static final long TTL_BASE = TimeUnit.MINUTES.toMillis(10);
    // 인증메일 15분동안 유효
    private static final long CERTIFICATE_EXPIRE_TIME = 1000 * 60 * 15;

    public String generateKey(Member member){
        return member.getEmail()+"_"+member.getUserId();
    }

    @SuppressWarnings({"ConstantConditions"})
    public void saveCertificateCode(Member member,String code){
        String key = generateKey(member);
        String cacheResult = redisTemplate.opsForValue().get(key);
        if (cacheResult != null && !cacheResult.isBlank()){
            deleteKey(key);
        }
        redisTemplate.opsForValue().set(key,code,CERTIFICATE_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @SuppressWarnings({"ConstantConditions"})
    public boolean isVerifyCode(Member member, String code){
        String key = generateKey(member);
        String cacheResult = redisTemplate.opsForValue().get(key);
        return cacheResult != null && cacheResult.equals(code);
    }

    public void deleteKey(String key){
        redisTemplate.delete(key);
    }

}
