package kr.ac.dankook.SokGangPetTour.config.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.dankook.SokGangPetTour.dto.request.chatRequest.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedisChatPublisher {

    private final ChannelTopic channelTopic;
    private final RedisTemplate<String,String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publish(ChatMessageRequest message){
        try{
            String serializedValue = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(channelTopic.getTopic(), serializedValue);
        }catch (JsonProcessingException e){
            log.error("Failed to serialize chat message: {}", e.getMessage());
        }
    }
}
