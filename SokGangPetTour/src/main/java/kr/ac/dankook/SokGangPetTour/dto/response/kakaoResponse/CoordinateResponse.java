package kr.ac.dankook.SokGangPetTour.dto.response.kakaoResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoordinateResponse {
    private double latitude;
    private double longitude;
}
