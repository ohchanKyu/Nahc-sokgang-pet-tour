package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.TourDetailIntroTourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TourDetailIntroTouristRepository extends JpaRepository<TourDetailIntroTourist,Long> {
    void deleteByContent(TourContent content);
}
