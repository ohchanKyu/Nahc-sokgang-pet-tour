package kr.ac.dankook.SokGangPetTour.repository;

import kr.ac.dankook.SokGangPetTour.entity.VetPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VetPlaceRepository extends JpaRepository<VetPlace,Long> {
}
