package kr.ac.dankook.SokGangPetTour;

import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
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

    @Test
    @DisplayName("모든 장소 데이터 반환 응답 테스트")
    public void getAllVetPlaceTest(){

        long startTime = System.currentTimeMillis();
        List<VetPlaceResponse> places = vetPlaceService.getAllVetPlace();
        long executionTime = System.currentTimeMillis() - startTime;
        log.info("총 실행 시간 - {}ms",executionTime);
        assertThat(places.size()).isEqualTo(267);
    }
}
