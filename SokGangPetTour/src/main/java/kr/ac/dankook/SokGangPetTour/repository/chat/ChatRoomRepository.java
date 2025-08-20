package kr.ac.dankook.SokGangPetTour.repository.chat;

import jakarta.persistence.LockModeType;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    @Query("select c from ChatRoom c where c.name LIKE %:keyword% or c.description LIKE %:keyword%")
    List<ChatRoom> searchByKeyword(@Param("keyword") String keyword);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select c from ChatRoom c where c.id = :id")
    Optional<ChatRoom> findByIdWithOptimisticLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ChatRoom c where c.id = :id")
    Optional<ChatRoom> findByIdWithPessimisticLock(Long id);

}
