package kr.ac.dankook.SokGangPetTour.repository.vetPlace;

import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceMigration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetPlaceMigrationRepository extends JpaRepository<VetPlaceMigration, Long> {
}
