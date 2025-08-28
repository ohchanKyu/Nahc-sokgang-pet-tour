package kr.ac.dankook.SokGangPetTour.repository.tour;

import jakarta.persistence.LockModeType;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourStatic;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TourStaticRepository extends JpaRepository<TourStatic,Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        INSERT INTO tour_static (content_id, day, count)
        VALUES (:tourContentId, :day, 1)
        ON DUPLICATE KEY UPDATE count = count + 1
        """, nativeQuery = true)
    void upsertCount(@Param("tourContentId") String tourContentId,
                     @Param("day") LocalDate day);

    @Query("select s.content from TourStatic s where s.day = :day order by s.count desc")
    List<TourContent> findByDayOrderByCount(LocalDate day);
}
