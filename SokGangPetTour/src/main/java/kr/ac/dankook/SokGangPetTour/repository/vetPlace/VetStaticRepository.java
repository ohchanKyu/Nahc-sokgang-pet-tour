package kr.ac.dankook.SokGangPetTour.repository.vetPlace;

import jakarta.persistence.LockModeType;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetStatic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VetStaticRepository extends JpaRepository<VetStatic,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from VetStatic s where s.vetPlace = :vetPlace and s.day = :day")
    Optional<VetStatic> findByVetPlaceAndDayWithPessimisticLock(VetPlace vetPlace, LocalDate day);

    @Query("select s.vetPlace from VetStatic s where s.day = :day order by s.count desc")
    List<VetPlace> findByDayOrderByCount(LocalDate day);
}
