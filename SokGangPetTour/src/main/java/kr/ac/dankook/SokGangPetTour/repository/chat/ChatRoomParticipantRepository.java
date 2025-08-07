package kr.ac.dankook.SokGangPetTour.repository.chat;

import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant,Long> {
    Optional<ChatRoomParticipant> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
