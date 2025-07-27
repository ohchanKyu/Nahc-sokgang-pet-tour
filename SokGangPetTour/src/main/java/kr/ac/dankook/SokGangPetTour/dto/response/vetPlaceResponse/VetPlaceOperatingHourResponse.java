package kr.ac.dankook.SokGangPetTour.dto.response.vetPlaceResponse;

import kr.ac.dankook.SokGangPetTour.entity.DayType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class VetPlaceOperatingHourResponse {

    private DayType dayType;
    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isOpen;
}
