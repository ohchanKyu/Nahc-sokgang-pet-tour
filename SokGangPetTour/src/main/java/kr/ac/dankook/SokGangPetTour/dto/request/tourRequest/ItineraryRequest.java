package kr.ac.dankook.SokGangPetTour.dto.request.tourRequest;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class ItineraryRequest {

    @DecimalMin(value = "-90.0", message = "출발위도는 -90.0 이상이어야 합니다.")
    @DecimalMax(value = "90.0",  message = "출발위도는 90.0 이하여야 합니다.")
    private double startLatitude;

    @DecimalMin(value = "-180.0", message = "출발경도는 -180.0 이상이어야 합니다.")
    @DecimalMax(value = "180.0",  message = "출발경도는 180.0 이하여야 합니다.")
    private double startLongitude;

    @Min(value = 2, message = "여행일수는 최소 2일 이상이어야 합니다.")
    @Max(value = 5, message = "여행일수는 최대 5일까지 허용됩니다.")
    private int days = 1;

    @Pattern(regexp = "(?i)^(WALK|TRANSIT|DRIVE)$",
            message = "이동 수단은 도보, 대중교통, 자동차 중 하나여야 합니다.")
    private String travelMode = "DRIVE";

    private Set<String> keywords;
    private Set<String> excludeContentTypeIds;

    private boolean includeFoodStops = true;

    @Min(value = 0,  message = "시작 시각은 0 이상이어야 합니다.")
    @Max(value = 23, message = "시작 시각은 23 이하여야 합니다.")
    private int dayStartHour = 9;

    @Min(value = 60,   message = "일일 시간은 최소 60분 이상이어야 합니다.")
    @Max(value = 1440, message = "일일 시간은 최대 1440분 이하여야 합니다.")
    private int dailyTimeLimitMin = 9 * 60;

    @DecimalMin(value = "0.1", message = "최대 반경은 최소 0.1km 이상이어야 합니다.")
    @DecimalMax(value = "500.0", message = "최대 반경은 최대 500km 이하여야 합니다.")
    private double maxRadiusKm = 30.0;

    private Double wDist;
    private Double wCat;
    private Double wKw;
    private Double distDecayKm;

    @Min(value = 1, message = "일자당 최대 방문지는 최소 1 이상이어야 합니다.")
    @Max(value = 30, message = "일자당 최대 방문지는 최대 30까지 허용됩니다.")
    private Integer maxStopsPerDay;

}
