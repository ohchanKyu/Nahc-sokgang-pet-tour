package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourContentRepository extends JpaRepository<TourContent, String> {

    @Query("select c from TourContent c JOIN FETCH c.detailImages")
    List<TourContent> findAllTourContentWithDetailImages();

    @Query("select c from TourContent c where c.title like %:keyword% or c.address like %:keyword% ")
    List<TourContent> findTourContentWithKeyword(@Param("keyword") String keyword);

    List<TourContent> findTourContentByCat1(String cat1);

    @Query("select distinct c.cat2 from TourContent c where c.cat1 = :cat1")
    List<String> findCat2ByCat1(@Param("cat1") String cat1);

    @Query("select distinct c.cat3 from TourContent c where c.cat2 = :cat2 and c.cat1 = :cat1")
    List<String> findCat3ByCat2AndCat1(@Param("cat1") String cat1, @Param("cat2") String cat2);
}
