package kr.ac.dankook.SokGangPetTour.repository.chatBot;

import kr.ac.dankook.SokGangPetTour.entity.chat.ChatMessage;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotHistoryRepository extends MongoRepository<ChatBotHistory,String> {
    List<ChatBotHistory> findBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
    List<ChatBotHistory> findBySessionIdOrderByTimeAsc(String sessionId);
}
