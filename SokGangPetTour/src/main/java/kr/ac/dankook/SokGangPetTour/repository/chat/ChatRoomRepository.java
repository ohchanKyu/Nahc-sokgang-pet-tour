package kr.ac.dankook.SokGangPetTour.repository.chat;

import jakarta.persistence.LockModeType;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    @Query("select c from ChatRoom c where c.name LIKE %:keyword% or c.description LIKE %:keyword%")
    Page<ChatRoom> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select distinct c from ChatRoom c where c.member = :member")
    Page<ChatRoom> findByMember(@Param("member") Member member, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select c from ChatRoom c where c.id = :id")
    Optional<ChatRoom> findByIdWithOptimisticLock(Long id);

}
