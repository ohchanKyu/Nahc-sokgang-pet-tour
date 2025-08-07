package kr.ac.dankook.SokGangPetTour.dto.request.chatRequest;

import kr.ac.dankook.SokGangPetTour.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageRequest {

    private MessageType type;
    private String memberId;
    private String roomId;
    private String message;
    private LocalDateTime time;

    public ChatMessageRequest(MessageType type, String memberId, String roomId, String message) {
        this.type = type;
        this.memberId = memberId;
        this.roomId = roomId;
        this.message = message;
        this.time = LocalDateTime.now();
    }

}
