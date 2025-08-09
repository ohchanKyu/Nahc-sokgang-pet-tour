package kr.ac.dankook.SokGangPetTour.repository.chat;

import kr.ac.dankook.SokGangPetTour.entity.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    Page<ChatMessage> findByRoomId(String roomId, Pageable pageable);
    List<ChatMessage> findByRoomId(String roomId);
}
