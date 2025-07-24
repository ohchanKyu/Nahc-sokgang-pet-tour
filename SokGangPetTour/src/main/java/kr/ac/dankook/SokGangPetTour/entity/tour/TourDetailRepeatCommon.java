package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tour_detail_repeat_common")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TourDetailRepeatCommon extends TourDetailRepeat {
    private String infoName;
    private String infoText;
}
