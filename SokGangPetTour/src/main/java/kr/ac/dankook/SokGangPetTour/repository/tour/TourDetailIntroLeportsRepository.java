package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.TourDetailIntroLeports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourDetailIntroLeportsRepository extends JpaRepository<TourDetailIntroLeports, Long> {
    void deleteByContent(TourContent content);
}
