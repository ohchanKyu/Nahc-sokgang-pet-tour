package kr.ac.dankook.SokGangPetTour.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.repository.tour.TourContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class TourContentService {

    private static final String NULL_VALUE = "__NULL__";
    private static final long TTL_BASE = 1_000;
    private static final int JITTER_RANGE = 500;

    private final TourContentRepository tourContentRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    @SuppressWarnings("ConstantConditions")
    public TourContent findTourContentByIdWithJitter(String contentId) throws JsonProcessingException {

        String cacheResult = redisTemplate.opsForValue().get(contentId);
        if (cacheResult != null && cacheResult.equals(NULL_VALUE)) {
            return null;
        }
        if(cacheResult != null){
            return objectMapper.readValue(cacheResult, new TypeReference<>(){});
        }
        TourContent contentFromDb = tourContentRepository.findById(contentId)
                .orElse(null);
        if (contentFromDb == null) {
            // java.util.Random -> 내부적으로 하나의 seed 상태를 공유하면서 난수를 생성
            // ThreadLocalRandom.current()는 스레드별로 Random 상태를 유지하므로 동시성 안전 + 고성능
            long ttl = TTL_BASE + ThreadLocalRandom.current().nextLong(JITTER_RANGE);
            redisTemplate.opsForValue().set(contentId,NULL_VALUE,ttl, TimeUnit.MILLISECONDS);
            return contentFromDb;
        }
        String serializedTourContent = objectMapper.writeValueAsString(contentFromDb);

        long ttl = TTL_BASE + ThreadLocalRandom.current().nextLong(JITTER_RANGE);
        redisTemplate.opsForValue().set(contentId,serializedTourContent,ttl, TimeUnit.MILLISECONDS);
        return contentFromDb;
    }
}
