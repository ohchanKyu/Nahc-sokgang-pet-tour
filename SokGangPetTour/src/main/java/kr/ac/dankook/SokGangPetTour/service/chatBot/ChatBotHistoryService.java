package kr.ac.dankook.SokGangPetTour.service.chatBot;

import kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse.ChatBotHistoryResponse;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotHistory;
import kr.ac.dankook.SokGangPetTour.repository.chatBot.ChatBotHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotHistoryService {

    private final ChatBotHistoryRepository chatBotHistoryRepository;

    public List<ChatBotHistoryResponse> getAllChatBotHistory(String sessionId){
        List<ChatBotHistory> lists = chatBotHistoryRepository.findBySessionId(sessionId);
        return lists.stream().map(ChatBotHistoryResponse::new).toList();
    }

    public void deleteChatBotHistory(String sessionId){
        chatBotHistoryRepository.deleteBySessionId(sessionId);
    }
}
