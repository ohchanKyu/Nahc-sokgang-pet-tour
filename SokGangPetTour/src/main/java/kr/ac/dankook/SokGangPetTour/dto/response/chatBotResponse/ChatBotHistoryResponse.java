package kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse;

import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotHistory;
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

    @Builder
    public ChatBotHistoryResponse(ChatBotHistory chatBotHistory) {
        this.sessionId = chatBotHistory.getSessionId();
        this.role = chatBotHistory.getRole();
        this.content = chatBotHistory.getContent();
        this.time = chatBotHistory.getTime();
    }
}
