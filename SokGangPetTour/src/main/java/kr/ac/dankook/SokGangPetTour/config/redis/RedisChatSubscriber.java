package kr.ac.dankook.SokGangPetTour.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedisChatSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final static String DESTINATION_PREFIX = "/sub/chat/room/";

    public void onMessage(String publishMessage){
        try{
            ChatMessageRequest chatMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);
            messagingTemplate.convertAndSend(
                    DESTINATION_PREFIX + chatMessage.getRoomId() , chatMessage
            );
        }catch (Exception e){
            log.error("Exception during sending chatting - {}", e.getMessage());
        }
    }
}
