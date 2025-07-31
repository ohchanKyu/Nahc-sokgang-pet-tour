package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourContentResponse {

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
