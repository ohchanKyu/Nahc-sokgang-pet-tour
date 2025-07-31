package kr.ac.dankook.SokGangPetTour.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class TourContentCacheService {

    private static final String NULL_VALUE = "__NULL__";
    // 하루
    private static final long TTL_BASE = TimeUnit.DAYS.toMillis(1);
    // Jitter 1분
    private static final long JITTER_RANGE = TimeUnit.MINUTES.toMillis(1);
    private static final long LOCK_TIMEOUT_MS = 400;
    private static final int MAX_RETRY_COUNT = 40;
    private static final long RETRY_DELAY_MS = 5;
    private static final long RETRY_DELAY_JITTER_MS = 15;

    private final TourContentService tourContentService;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final DistributedLockProvider distributedLockProvider;

    // 1) 캐시 우선 확인
    // 2) 캐시에 없으면 락 획득 시도
    // 3) 락 획득 후에도 캐시 재확인 ( double check locking )
    // 4) 최종적으로도 캐시가 없으면 DB 조회 후 캐시에 저장
    @SuppressWarnings({"ConstantConditions","UnreachableCode"})
    public TourContentDetailResponse findTourContentDetailById(String key) throws JsonProcessingException {

        String cached = redisTemplate.opsForValue().get(key);
        if (cached != null && cached.equals(NULL_VALUE)) {
            return null;
        }
        if (cached != null){
            return objectMapper.readValue(cached,new TypeReference<>(){});
        }
        String lockKey = makeLockKey(key);
        for(int retry = 0; retry < MAX_RETRY_COUNT;retry++) {
            boolean lockAcquired = distributedLockProvider.tryLock(lockKey,LOCK_TIMEOUT_MS);
            if (!lockAcquired){
                long jitter = ThreadLocalRandom.current().nextLong(RETRY_DELAY_JITTER_MS);
                sleepRetryForLock(RETRY_DELAY_MS + jitter);
                continue;
            }
            try{
                cached = redisTemplate.opsForValue().get(key);
                if (cached != null && cached.equals(NULL_VALUE)) {
                    return null;
                }
                if (cached != null){
                    return objectMapper.readValue(cached,new TypeReference<>(){});
                }
                TourContentDetailResponse response = tourContentService.convertToDetailResponse(key);
                if (response == null) {
                    long ttl = TTL_BASE + ThreadLocalRandom.current().nextLong(JITTER_RANGE);
                    redisTemplate.opsForValue().set(key,NULL_VALUE,ttl,TimeUnit.MILLISECONDS);
                    return null;
                }
                String serialized = objectMapper.writeValueAsString(response);
                long ttl = TTL_BASE + ThreadLocalRandom.current().nextLong(JITTER_RANGE);
                redisTemplate.opsForValue().set(key,serialized,ttl,TimeUnit.MILLISECONDS);
                return response;
            }finally {
                distributedLockProvider.releaseLock(lockKey);
            }
        }
        throw new IllegalStateException("Failed to acquire lock after " + MAX_RETRY_COUNT + " - key : " + lockKey);
    }

    private void sleepRetryForLock(long ms){
        try{
            TimeUnit.MILLISECONDS.sleep(ms);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    private String makeLockKey(String key){
        return "lock-tour-content:" + key;
    }


}
