package kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse;

import kr.ac.dankook.SokGangPetTour.entity.VetPlaceCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class VetPlaceResponse {

    private String placeName;
    private String address;
    private String phoneNumber;
    private double latitude;
    private double longitude;
    private String holidayInfo;
    private String maxSizeInfo;
    private boolean isParking;
    private VetPlaceCategory category;
    private List<VetPlaceOperatingHourResponse> operatingHours;
}
