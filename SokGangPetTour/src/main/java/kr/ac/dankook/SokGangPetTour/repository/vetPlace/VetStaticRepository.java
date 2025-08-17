package kr.ac.dankook.SokGangPetTour.repository.vetPlace;

import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetStatic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VetStaticRepository extends JpaRepository<VetStatic,Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
        INSERT INTO vet_static (vet_place_id, day, count)
        VALUES (:vetPlaceId, :day, 1)
        ON DUPLICATE KEY UPDATE count = count + 1
        """, nativeQuery = true)
    void upsertCount(@Param("vetPlaceId") Long vetPlaceId,
                     @Param("day") LocalDate day);

    @Query("select s.vetPlace from VetStatic s where s.day = :day order by s.count desc")
    List<VetPlace> findByDayOrderByCount(LocalDate day);
}
