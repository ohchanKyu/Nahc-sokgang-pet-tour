package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tour_static")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TourStatic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate day;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private TourContent content;

    private Long count;

    public TourStatic(TourContent tourContent) {
        this.content = tourContent;
        this.count = 1L;
        this.day = LocalDate.now();
    }

    public void countUp(){
        this.count++;
    }
}
