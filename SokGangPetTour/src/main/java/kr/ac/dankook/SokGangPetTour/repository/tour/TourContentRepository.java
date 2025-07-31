package kr.ac.dankook.SokGangPetTour.repository.tour;

import kr.ac.dankook.SokGangPetTour.entity.tour.TourContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourContentRepository extends JpaRepository<TourContent, String> {

    @Query("select c from TourContent c JOIN FETCH c.detailImages")
    List<TourContent> findAllTourContentWithDetailImages();

    @Query("select c from TourContent c where c.title like %:keyword% or c.address like %:keyword% ")
    List<TourContent> findTourContentWithKeyword(@Param("keyword") String keyword);

    List<TourContent> findTourContentByCat1(String cat1);
}
