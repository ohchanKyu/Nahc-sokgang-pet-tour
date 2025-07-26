package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro.TourDetailIntroShopping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourDetailIntroShoppingRepository extends JpaRepository<TourDetailIntroShopping, Long> {
}
