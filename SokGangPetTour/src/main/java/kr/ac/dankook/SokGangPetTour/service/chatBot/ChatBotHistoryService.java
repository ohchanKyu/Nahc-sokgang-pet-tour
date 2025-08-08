package kr.ac.dankook.SokGangPetTour.service.chatBot;

import kr.ac.dankook.SokGangPetTour.dto.request.chatBotRequest.ChatBotCreateRequest;
import kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse.ChatBotHistoryResponse;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotHistory;
import kr.ac.dankook.SokGangPetTour.repository.chatBot.ChatBotHistoryRepository;
import kr.ac.dankook.SokGangPetTour.service.chatBot.event.EventChatBotUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatBotHistoryService {

    private final ChatBotHistoryRepository chatBotHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public List<ChatBotHistoryResponse> getAllChatBotHistory(String sessionId){
        List<ChatBotHistory> lists = chatBotHistoryRepository.findBySessionId(sessionId);
        return lists.stream().map(ChatBotHistoryResponse::new).toList();
    }

    @Transactional
    public void saveChatBotHistory(ChatBotCreateRequest chatBotCreateRequest){

        ChatBotHistory chatBotHistory = ChatBotHistory.builder()
                .sessionId(chatBotCreateRequest.getSessionId())
                .role(chatBotCreateRequest.getRole())
                .content(chatBotCreateRequest.getContent()).build();
        chatBotHistoryRepository.save(chatBotHistory);
        eventPublisher.publishEvent(new EventChatBotUpdateEvent(chatBotCreateRequest.getSessionId(),chatBotCreateRequest.getContent()));
    }

    public void deleteChatBotHistory(String sessionId){
        chatBotHistoryRepository.deleteBySessionId(sessionId);
    }
}
