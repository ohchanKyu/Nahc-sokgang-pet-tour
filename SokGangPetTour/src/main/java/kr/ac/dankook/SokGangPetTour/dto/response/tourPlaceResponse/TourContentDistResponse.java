package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourContentDistResponse {

    private TourContentResponse tourContentResponse;
    private double distance;
    private String distanceStringFormat;
}