package kr.ac.dankook.SokGangPetTour;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceOperatingHourResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.VetPlace;
import kr.ac.dankook.SokGangPetTour.repository.VetPlaceRepository;
import kr.ac.dankook.SokGangPetTour.service.VetPlaceCacheService;
import kr.ac.dankook.SokGangPetTour.service.VetPlaceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Slf4j
public class VetPlaceResponseTest {

    @Autowired
    private VetPlaceService vetPlaceService;

    @Autowired
    private VetPlaceCacheService vetPlaceCacheService;

    @Autowired
    private VetPlaceRepository vetPlaceRepository;

    @Test
    @DisplayName("모든 장소 데이터 반환 응답 테스트")
    public void getAllVetPlaceTest(){

        long startTime = System.currentTimeMillis();
        List<VetPlaceResponse> places = vetPlaceService.getAllVetPlace();
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("총 실행 시간 - {}ms", executionTime);
        assertThat(places.size()).isEqualTo(267);
    }

    @Test
    @DisplayName("카테고리별 장소 데이터 반환 응답 테스트")
    public void getVetPlaceByCategoryTest(){
        long startTime = System.currentTimeMillis();
        List<VetPlaceResponse> places = vetPlaceService.getVetPlacesByCategory("HOSPITAL");
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("총 실행 시간 - {}ms",executionTime);
    }

    @Test
    @DisplayName("하나의 장소 데이터 반환 응답 테스트")
    public void getVetPlaceTest() throws JsonProcessingException {
        VetPlaceResponse places = vetPlaceCacheService.getVetPlacesById("aAPzwpKf-0mFtlfbU1wCWA");
        log.info(places.getPlaceName());
        for(VetPlaceOperatingHourResponse response : places.getOperatingHours()){
            log.info("Day : {} Operating - {} ~ {}", response.getDayType(),response.getOpenTime(),response.getCloseTime());
        }
    }
}
