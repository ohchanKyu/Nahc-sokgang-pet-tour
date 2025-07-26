package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tour_detail_repeat_common")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class TourDetailRepeatCommon extends TourDetailRepeat {
    @Column(columnDefinition = "TEXT")
    private String infoName;
    @Column(columnDefinition = "TEXT")
    private String infoText;
}
