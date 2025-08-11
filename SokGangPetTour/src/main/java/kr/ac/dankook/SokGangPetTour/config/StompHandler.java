package kr.ac.dankook.SokGangPetTour.config;

import kr.ac.dankook.SokGangPetTour.config.principal.PrincipalDetails;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import kr.ac.dankook.SokGangPetTour.jwt.JwtTokenProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String NATIVE_HEADER_NAME = "token";

    @Override
    @NonNull
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // WebSocket 최초 연결 시 Token 인증 진행
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String token = accessor.getFirstNativeHeader(NATIVE_HEADER_NAME);
            if (token.isBlank()){
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }
            try{
                Authentication authentication = jwtTokenProvider.validateToken(token);
                PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
                log.info("Successfully validated JWT token and connect socket - {} ", principalDetails.getUsername());
            }catch (Exception e){
                log.error("Invalid Jwt Token or server error - {}", e.getMessage());
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }
        }
        return message;
    }
}
