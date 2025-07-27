package kr.ac.dankook.SokGangPetTour.repository;

import kr.ac.dankook.SokGangPetTour.entity.VetPlaceMigration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetPlaceMigrationRepository extends JpaRepository<VetPlaceMigration, Long> {
}
