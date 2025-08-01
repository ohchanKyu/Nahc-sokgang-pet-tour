package kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse;

import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatBotHistoryResponse {

    private String sessionId;
    private ChatBotRole role;
    private String content;
    private LocalDateTime time;
}
