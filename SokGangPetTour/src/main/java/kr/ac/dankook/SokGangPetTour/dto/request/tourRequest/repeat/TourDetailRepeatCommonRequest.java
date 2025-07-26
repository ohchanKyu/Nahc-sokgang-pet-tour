package kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.repeat;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class TourDetailRepeatCommonRequest{

    private String infoName;
    private String infoText;
    private String contentTypeId;
}