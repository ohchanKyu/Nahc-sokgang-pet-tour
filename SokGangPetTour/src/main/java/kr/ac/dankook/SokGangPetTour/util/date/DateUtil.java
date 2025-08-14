package kr.ac.dankook.SokGangPetTour.util.date;

import kr.ac.dankook.SokGangPetTour.entity.DayType;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceOperatingHour;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DateUtil {

    public static boolean isOpenNow(
            List<VetPlaceOperatingHour> hours,
            LocalDateTime now, DayOfWeek today) {
        DayType currentDayType = Holiday.isHoliday(now.toLocalDate()) ?
                DayType.HOLIDAY
                : switch(today) {
            case MONDAY -> DayType.MON;
            case TUESDAY -> DayType.TUE;
            case WEDNESDAY -> DayType.WED;
            case THURSDAY -> DayType.THU;
            case FRIDAY -> DayType.FRI;
            case SATURDAY -> DayType.SAT;
            case SUNDAY -> DayType.SUN;
        };
        LocalTime currentTime = now.toLocalTime();
        return hours.stream()
                .filter(h -> h.getDayType() == currentDayType)
                .filter(VetPlaceOperatingHour::isOpen)
                .anyMatch(h ->
                        h.getOpenTime() != null &&
                                h.getCloseTime() != null &&
                                !currentTime.isBefore(h.getOpenTime()) &&
                                currentTime.isBefore(h.getCloseTime())
                );
    }
}
