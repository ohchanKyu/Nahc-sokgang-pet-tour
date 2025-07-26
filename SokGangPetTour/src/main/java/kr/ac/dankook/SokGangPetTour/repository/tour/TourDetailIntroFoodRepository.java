package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.TourDetailIntroFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourDetailIntroFoodRepository extends JpaRepository<TourDetailIntroFood, Long> {
}
