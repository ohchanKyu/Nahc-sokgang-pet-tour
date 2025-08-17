package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "tour_static",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_content_day",
                        columnNames = { "content_id", "day" }
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourStatic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate day;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private TourContent content;

    @Column(nullable = false)
    private Long count;
}
