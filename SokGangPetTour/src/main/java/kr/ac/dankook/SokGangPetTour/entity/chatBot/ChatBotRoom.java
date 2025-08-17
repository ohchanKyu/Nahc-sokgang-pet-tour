package kr.ac.dankook.SokGangPetTour.entity.chatBot;

import jakarta.persistence.*;
import kr.ac.dankook.SokGangPetTour.entity.BaseEntity;
import kr.ac.dankook.SokGangPetTour.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_bot_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatBotRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "member_id", nullable = false)
    private Member member;
    private String title;

    @Builder
    public ChatBotRoom(Member member, String title) {
        this.member = member;
        this.title = title;
    }

    public void updateTitle(String title){
        this.title = title;
    }
}
