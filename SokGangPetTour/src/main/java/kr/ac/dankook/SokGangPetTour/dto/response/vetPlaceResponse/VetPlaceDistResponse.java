package kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VetPlaceDistResponse {

    private VetPlaceResponse vetPlaceResponse;
    private double distance;
    private String distanceStringFormat;
}
