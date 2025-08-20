package kr.ac.dankook.SokGangPetTour.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import kr.ac.dankook.SokGangPetTour.entity.MessageType;
import kr.ac.dankook.SokGangPetTour.service.chat.ChatService;
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
    private final ChatService chatService;

    public void handleExitAndEnterMessage(String roomId, String nickname, MessageType type){
        String messages;
        if (type == MessageType.ENTER){
            messages = String.format("%s님이 입장하였습니다.",nickname);
        }else messages = String.format("%s님이 퇴장하였습니다.",nickname);
        ChatMessageRequest chatMessage = ChatMessageRequest.builder()
                .type(type).message(messages).roomId(roomId).build();
        chatService.saveChatMessage(chatMessage);
        try{
            messagingTemplate.convertAndSend(
                    DESTINATION_PREFIX + roomId , chatMessage
            );
        }catch (Exception e){
            log.error("Failed to send info message: {}", e.getMessage());
        }
    }

    public void onMessage(String publishMessage){
        try{
            ChatMessageRequest chatMessage = objectMapper.readValue(publishMessage, ChatMessageRequest.class);
            messagingTemplate.convertAndSend(
                    DESTINATION_PREFIX + chatMessage.getRoomId() , chatMessage
            );
        }catch (Exception e){
            log.error("Failed to deserialize chat message: {}", e.getMessage());
        }
    }
}
