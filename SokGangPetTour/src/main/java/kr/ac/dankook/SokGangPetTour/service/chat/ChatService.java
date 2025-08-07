package kr.ac.dankook.SokGangPetTour.service.chat;

import kr.ac.dankook.SokGangPetTour.config.redis.RedisChatPublisher;
import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final RedisChatPublisher redisChatPublisher;

    public void sendChatMessage(ChatMessageRequest request){
        redisChatPublisher.publish(request);
    }
}
