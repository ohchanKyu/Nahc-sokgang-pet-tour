package kr.ac.dankook.SokGangPetTour.util.converter;

import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceOperatingHourResponse;
import kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse.VetPlaceResponse;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceOperatingHour;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import kr.ac.dankook.SokGangPetTour.util.date.DateUtil;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class VetPlaceEntityConverter {

    public static VetPlaceResponse convertToVetPlaceResponse(VetPlace vetPlace,boolean isIncludeOperatingHours) {

        VetPlaceResponse vetPlaceResponse = VetPlaceResponse.builder()
                .id(EncryptionUtil.encrypt(vetPlace.getId()))
                .category(vetPlace.getCategory())
                .placeName(vetPlace.getPlaceName())
                .longitude(vetPlace.getLongitude())
                .latitude(vetPlace.getLatitude())
                .address(vetPlace.getAddress())
                .holidayInfo(vetPlace.getHolidayInfo())
                .phoneNumber(vetPlace.getPhoneNumber())
                .maxSizeInfo(vetPlace.getMaxSizeInfo())
                .isParking(vetPlace.isParking()).build();

        if (!isIncludeOperatingHours) return vetPlaceResponse;

        List<VetPlaceOperatingHourResponse> operatingHours = vetPlace.getOperatingHours()
                .stream().map(VetPlaceEntityConverter::convertToVetPlaceOperatingHourResponse).toList();
        vetPlaceResponse.setOperatingHours(operatingHours);

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();
        boolean isOpen = DateUtil.isOpenNow(vetPlace.getOperatingHours(),now,today);
        vetPlaceResponse.setOpen(isOpen);
        return vetPlaceResponse;
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
