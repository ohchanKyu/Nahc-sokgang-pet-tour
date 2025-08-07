package kr.ac.dankook.SokGangPetTour.controller;

import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import kr.ac.dankook.SokGangPetTour.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    // 사용자 -> WebSocket -> MessageMapping Controller
    // -> RedisPublisher.publish() -> Redis Channel("chatRoom")
    // -> RedisMessageListenerContainer -> RedisSubscriber (MessageListenerAdapter)
    // -> SimpMessageTemplate -> 모든 WebSocket 구독자에게 전송
    @MessageMapping("/chat/message")
    public void sendMessage(
            @Payload ChatMessageRequest request) {
        chatService.sendChatMessage(request);
    }

}
