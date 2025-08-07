package kr.ac.dankook.SokGangPetTour.config;

import kr.ac.dankook.SokGangPetTour.jwt.JwtTokenProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @NonNull
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // WebSocket 최초 연결
        if (StompCommand.COMMIT == accessor.getCommand()) {

        }else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

        }else if (StompCommand.SEND == accessor.getCommand()){

        }else if (StompCommand.DISCONNECT == accessor.getCommand()){

        }
        return message;
    }
}
