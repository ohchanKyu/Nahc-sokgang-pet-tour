package kr.ac.dankook.SokGangPetTour.dto.request.tourRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TourContentRequest {

    private String contentId;
    private String address;
    private String detailAddress;
    private String postCode;
    private int areaCode;
    private int sigunguCode;
    private String cat1;
    private String cat2;
    private String cat3;
    private String contentTypeId;
    private String originalImageUrl;
    private String thumbnailImageUrl;
    private double longitude;
    private double latitude;
    private String telephone;
    private String title;
    private String overview;
}
