package kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatBotRoomResponse {

    private String roomId;
    private String title;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
}
