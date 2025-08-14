package kr.ac.dankook.SokGangPetTour.repository.tour;

import jakarta.persistence.LockModeType;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourStatic;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourStaticRepository extends JpaRepository<TourStatic,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from TourStatic s where s.content = :tourContent and s.day = :day")
    Optional<TourStatic> findByTourContentAndDayWithPessimisticLock(TourContent tourContent, LocalDate day);

    @Query("select s.content from TourStatic s where s.day = :day order by s.count desc")
    List<TourContent> findByDayOrderByCount(LocalDate day);
}
