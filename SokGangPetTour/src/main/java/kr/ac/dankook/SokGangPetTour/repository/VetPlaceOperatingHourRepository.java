package kr.ac.dankook.SokGangPetTour.repository;

import kr.ac.dankook.SokGangPetTour.entity.VetPlaceOperatingHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetPlaceOperatingHourRepository extends JpaRepository<VetPlaceOperatingHour,Long> {
}
