package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailRepeatCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TourDetailRepeatCommonRepository extends JpaRepository<TourDetailRepeatCommon, Long> {
    void deleteByContent(TourContent content);
}
