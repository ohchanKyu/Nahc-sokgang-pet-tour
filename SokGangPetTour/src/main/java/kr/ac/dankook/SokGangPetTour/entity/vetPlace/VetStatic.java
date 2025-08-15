package kr.ac.dankook.SokGangPetTour.entity.vetPlace;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "vet_static",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vet_place_day",
                        columnNames = { "vet_place_id", "day" }
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VetStatic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate day;

    @ManyToOne
    @JoinColumn(name = "vet_place_id", nullable = false)
    private VetPlace vetPlace;

    @Column(nullable = false)
    private Long count;
}
