package kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse;

import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRoom;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
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

    @Builder
    public ChatBotRoomResponse(ChatBotRoom chatBotRoom){
        this.roomId = EncryptionUtil.encrypt(chatBotRoom.getId());
        this.title = chatBotRoom.getTitle();
        this.lastMessage = chatBotRoom.getLastMessage();
        this.lastMessageTime = chatBotRoom.getLastMessageTime();
    }
}
