package kr.ac.dankook.SokGangPetTour.repository.vetPlace;

import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetPlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VetPlaceRepository extends JpaRepository<VetPlace,Long> {

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours where p.category = :category and p.isParking = true")
    List<VetPlace> findByCategoryAndParkingWithFetchJoin(VetPlaceCategory category);

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours")
    List<VetPlace> findAllVetPlaceWithFetchJoin();

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours where p.category = :category")
    List<VetPlace> findByCategoryWithFetchJoin(VetPlaceCategory category);

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours where p.id = :vetPlaceId ")
    Optional<VetPlace> findByVetPlaceIdWithFetchJoin(Long vetPlaceId);

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours " +
            "where p.placeName like %:keyword% or p.address like %:keyword%")
    List<VetPlace> findByKeywordWithJoinFetch(String keyword);
}
