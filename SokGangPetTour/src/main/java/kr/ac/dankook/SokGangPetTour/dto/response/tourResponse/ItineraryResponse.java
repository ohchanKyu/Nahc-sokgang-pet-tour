package kr.ac.dankook.SokGangPetTour.dto.response.tourResponse;

import kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse.TourContentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItineraryResponse {
    private List<DayPlanResponse> days;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DayPlanResponse {
        private int dayIndex;
        private List<StopResponse> stops;
        private int totalTravelMin;
        private int totalStayMin;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StopResponse {

        private TourContentResponse tourContentResponse;
        private double distFromPrevKm;
        private int travelMin;
        private int stayMin;

        private String arrival; // HH:mm
        private String depart;  // HH:mm
    }
}
