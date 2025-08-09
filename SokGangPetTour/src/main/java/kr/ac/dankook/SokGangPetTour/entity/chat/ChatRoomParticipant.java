package kr.ac.dankook.SokGangPetTour.entity.chat;

import jakarta.persistence.*;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import lombok.*;

@Entity
@Table(name="chat_room_participant")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Setter
    private Long currentReadNumber;
    @Setter
    private String nickname;

    @Builder
    public ChatRoomParticipant(Member member,ChatRoom chatRoom,String nickname){
        this.member = member;
        this.chatRoom = chatRoom;
        this.nickname = nickname;
        this.currentReadNumber = 0L;
    }
}
