package kr.ac.dankook.SokGangPetTour.repository;

import kr.ac.dankook.SokGangPetTour.entity.VetPlace;
import kr.ac.dankook.SokGangPetTour.entity.VetPlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VetPlaceRepository extends JpaRepository<VetPlace,Long> {
    List<VetPlace> findByCategory(VetPlaceCategory category);

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours")
    List<VetPlace> findAllVetPlaceByFetchJoin();

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours where p.id = :vetPlaceId ")
    Optional<VetPlace> findByVetPlaceIdAndFetchJoin(Long vetPlaceId);

    @Query("select p from VetPlace p JOIN FETCH p.operatingHours " +
            "where p.placeName like %:keyword% or p.address like %:keyword%")
    List<VetPlace> findByKeywordAndJoinFetch(String keyword);
}
