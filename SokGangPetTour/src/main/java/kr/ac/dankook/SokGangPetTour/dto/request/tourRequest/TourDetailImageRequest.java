package kr.ac.dankook.SokGangPetTour.dto.request.tourRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TourDetailImageRequest {

    private String originImgUrl;
    private String smallImgUrl;
    private String imgName;
}
