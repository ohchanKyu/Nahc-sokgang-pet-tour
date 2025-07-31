package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourDetailImageResponse {

    private String originImgUrl;
    private String smallImgUrl;
    private String imgName;
}
