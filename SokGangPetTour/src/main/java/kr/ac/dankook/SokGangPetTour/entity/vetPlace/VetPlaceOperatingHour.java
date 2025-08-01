package kr.ac.dankook.SokGangPetTour.entity.vetPlace;

import jakarta.persistence.*;
import kr.ac.dankook.SokGangPetTour.entity.DayType;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "vet_place_operating_hour")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VetPlaceOperatingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayType dayType;

    private LocalTime openTime;
    private LocalTime closeTime;
    private boolean isOpen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vet_place_id")
    @Setter
    private VetPlace vetPlace;

    @Builder
    public VetPlaceOperatingHour(DayType dayType, LocalTime openTime,
                                 LocalTime closeTime, boolean isOpen, VetPlace vetPlace) {
        this.dayType = dayType;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOpen = isOpen;
        this.vetPlace = vetPlace;
    }
}

