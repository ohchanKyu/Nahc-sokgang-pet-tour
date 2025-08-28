package kr.ac.dankook.SokGangPetTour.repository.chat;

import kr.ac.dankook.SokGangPetTour.entity.chat.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findByRoomId(String roomId);
}
