package kr.ac.dankook.SokGangPetTour.dto.response.chatResponse;

import kr.ac.dankook.SokGangPetTour.entity.MessageType;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatResponse {

    private String memberId;
    private String roomId;
    private String message;
    private String nickname;
    private LocalDateTime time;
    private MessageType type;

    @Builder
    public ChatResponse(ChatMessage chatMessage) {
        this.memberId = chatMessage.getMemberId();
        this.roomId = chatMessage.getRoomId();
        this.nickname = chatMessage.getNickname();
        this.message = chatMessage.getContent();
        this.time = chatMessage.getTime();
        this.type = chatMessage.getType();
    }
}
