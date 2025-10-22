package kr.ac.dankook.SokGangPetTour.repository;


import kr.ac.dankook.SokGangPetTour.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUserId(String userId);
    boolean existsByUserId(String userId);
    List<Member> findByNameAndEmail(String name, String email);
}
