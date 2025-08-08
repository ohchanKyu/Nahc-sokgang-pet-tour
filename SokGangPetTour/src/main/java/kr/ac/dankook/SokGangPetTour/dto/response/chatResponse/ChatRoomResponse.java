package kr.ac.dankook.SokGangPetTour.dto.response.chatResponse;

import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoomParticipant;
import kr.ac.dankook.SokGangPetTour.util.EncryptionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatRoomResponse {

    private String roomId;
    private String name;
    private String description;
    private int currentParticipants;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private LocalDateTime createdAt;
    private Long currentReadNumber;

    @Builder
    public ChatRoomResponse(ChatRoom chatRoom) {
        this.roomId = EncryptionUtil.encrypt(chatRoom.getId());
        this.name = chatRoom.getName();
        this.description = chatRoom.getDescription();
        this.currentParticipants = chatRoom.getCurrentParticipants();
        this.lastMessage = chatRoom.getLastMessage();
        this.createdAt = chatRoom.getCreatedDateTime();
        this.lastMessageTime = chatRoom.getLastMessageTime();
    }

    @Builder
    public ChatRoomResponse(ChatRoomParticipant participant){
        ChatRoom chatRoom = participant.getChatRoom();
        this.roomId = EncryptionUtil.encrypt(chatRoom.getId());
        this.name = chatRoom.getName();
        this.description = chatRoom.getDescription();
        this.currentParticipants = chatRoom.getCurrentParticipants();
        this.lastMessage = chatRoom.getLastMessage();
        this.createdAt = chatRoom.getCreatedDateTime();
        this.lastMessageTime = chatRoom.getLastMessageTime();
        this.currentReadNumber = participant.getCurrentReadNumber();
    }
}
