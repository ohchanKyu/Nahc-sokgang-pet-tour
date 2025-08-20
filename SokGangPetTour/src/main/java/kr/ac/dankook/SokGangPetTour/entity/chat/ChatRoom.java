package kr.ac.dankook.SokGangPetTour.entity.chat;

import jakarta.persistence.*;
import kr.ac.dankook.SokGangPetTour.entity.BaseEntity;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.error.ErrorCode;
import kr.ac.dankook.SokGangPetTour.error.exception.CustomException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int maxParticipants = 300;
    private int currentParticipants;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomStatus status;

    @Column(columnDefinition = "TEXT")
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Long lastMessageNumber;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<ChatRoomParticipant> chatRoomParticipants = new ArrayList<>();

    @Builder
    public ChatRoom(String name, String description, int maxParticipants,Member createdMember) {
        this.name = name;
        this.description = description;
        this.maxParticipants = maxParticipants;
        this.member = createdMember;
        this.currentParticipants = 1;
        this.status = ChatRoomStatus.READ_WRITE;
        this.lastMessageNumber = 0L;
    }

    public void increaseParticipants() {
        if (this.currentParticipants >= this.maxParticipants){
            throw new CustomException(ErrorCode.EXCEED_PARTICIPANT);
        }
        this.currentParticipants++;
    }

    public int decreaseParticipants() {
        if (this.currentParticipants <= 0){
            throw new CustomException(ErrorCode.RANGE_ERROR_PARTICIPANT);
        }
        return --this.currentParticipants;
    }

    public void updateMessages(String lastMessage){
        this.lastMessage = lastMessage;
        this.lastMessageTime = LocalDateTime.now();
        this.lastMessageNumber++;
    }

    public void updateChatRoomStatus(ChatRoomStatus status){
        this.status = status;
    }
}
