package kr.ac.dankook.SokGangPetTour.util.converter;

import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceOperatingHourResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.VetPlaceOperatingHour;

import java.util.List;

public class VetPlaceEntityConverter {

    public static VetPlaceResponse convertToVetPlaceResponse(VetPlace vetPlace) {

//        List<VetPlaceOperatingHour> lists = vetPlace.getOperatingHours();
//        List<VetPlaceOperatingHourResponse> responses =
//                lists.stream().map(VetPlaceEntityConverter::convertToVetPlaceOperatingHourResponse).toList();

        return VetPlaceResponse.builder()
                .category(vetPlace.getCategory())
                .placeName(vetPlace.getPlaceName())
                .longitude(vetPlace.getLongitude())
                .latitude(vetPlace.getLatitude())
                .address(vetPlace.getAddress())
                .holidayInfo(vetPlace.getHolidayInfo())
                .phoneNumber(vetPlace.getPhoneNumber())
                .maxSizeInfo(vetPlace.getMaxSizeInfo())
                .isParking(vetPlace.isParking()).build();
    }

    public static VetPlaceOperatingHourResponse convertToVetPlaceOperatingHourResponse(
            VetPlaceOperatingHour vetPlaceOperatingHour) {
        return VetPlaceOperatingHourResponse.builder()
                .dayType(vetPlaceOperatingHour.getDayType())
                .openTime(vetPlaceOperatingHour.getOpenTime())
                .closeTime(vetPlaceOperatingHour.getCloseTime())
                .isOpen(vetPlaceOperatingHour.isOpen()).build();
    }
}
