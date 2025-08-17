package kr.ac.dankook.SokGangPetTour.dto.response.chatBotResponse;

import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRoom;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatBotRoomResponse {

    private String id;
    private String title;
    private LocalDateTime time;

    @Builder
    public ChatBotRoomResponse(ChatBotRoom chatBotRoom){
        this.id = EncryptionUtil.encrypt(chatBotRoom.getId());
        this.title = chatBotRoom.getTitle();
        this.time = chatBotRoom.getCreatedDateTime();
    }
}
