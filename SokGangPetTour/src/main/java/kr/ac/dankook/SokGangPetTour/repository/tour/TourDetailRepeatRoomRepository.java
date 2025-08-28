package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailRepeatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TourDetailRepeatRoomRepository extends JpaRepository<TourDetailRepeatRoom,Long> {
    void deleteByContent(TourContent content);
}
