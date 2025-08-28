package kr.ac.dankook.SokGangPetTour.repository.chatBot;

import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface ChatBotRoomRepository extends JpaRepository<ChatBotRoom,Long> {
    List<ChatBotRoom> findChatBotRoomByMember(Member member);
}
