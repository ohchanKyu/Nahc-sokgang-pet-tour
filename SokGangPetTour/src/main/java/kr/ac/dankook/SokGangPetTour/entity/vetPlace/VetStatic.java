package kr.ac.dankook.SokGangPetTour.entity.vetPlace;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vet_static")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VetStatic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate day;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_place_id")
    private VetPlace vetPlace;
    private Long count;

    public VetStatic(VetPlace vetPlace) {
        this.vetPlace = vetPlace;
        this.count = 1L;
        this.day = LocalDate.now();
    }

    public void countUp(){
        this.count++;
    }
}
