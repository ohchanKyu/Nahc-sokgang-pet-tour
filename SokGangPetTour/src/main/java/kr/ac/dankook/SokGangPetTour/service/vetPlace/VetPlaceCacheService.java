package kr.ac.dankook.SokGangPetTour.service.vetPlace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.repository.vetPlace.VetPlaceRepository;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import kr.ac.dankook.SokGangPetTour.util.converter.VetPlaceEntityConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class VetPlaceCacheService {

    private static final String NULL_VALUE = "__NULL__";
    // 하루
    private static final long TTL_BASE = TimeUnit.DAYS.toMillis(1);
    // Jitter 1분
    private static final long JITTER_RANGE = TimeUnit.MINUTES.toMillis(1);
    private final VetPlaceRepository vetPlaceRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    @SuppressWarnings({"ConstantConditions","UnreachableCode"})
    public VetPlaceResponse getVetPlacesById(String id) throws JsonProcessingException {
        String cacheResult = redisTemplate.opsForValue().get(id);
        if (cacheResult != null && cacheResult.equals(NULL_VALUE)){
            return null;
        }
        if (cacheResult != null){
            return objectMapper.readValue(cacheResult, new TypeReference<>(){});
        }
        Long decryptId = EncryptionUtil.decrypt(id);
        VetPlace vetPlaceFromDb = vetPlaceRepository
                .findByVetPlaceIdWithFetchJoin(decryptId).orElse(null);

        if (vetPlaceFromDb == null){
            long ttl = TTL_BASE + ThreadLocalRandom.current().nextLong(JITTER_RANGE);
            redisTemplate.opsForValue().set(id,NULL_VALUE,ttl,TimeUnit.MILLISECONDS);
            return null;
        }
        VetPlaceResponse vetPlaceResponse = VetPlaceEntityConverter.convertToVetPlaceResponse(vetPlaceFromDb,true);
        String serializedVetPlace = objectMapper.writeValueAsString(vetPlaceResponse);
        long ttl = TTL_BASE + ThreadLocalRandom.current().nextLong(JITTER_RANGE);
        redisTemplate.opsForValue().set(id,serializedVetPlace,ttl,TimeUnit.MILLISECONDS);
        return vetPlaceResponse;
    }
}
