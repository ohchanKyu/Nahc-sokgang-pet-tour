package kr.ac.dankook.SokGangPetTour.entity;

import jakarta.persistence.*;
import kr.ac.dankook.SokGangPetTour.entity.chat.ChatRoom;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRoom;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @Builder
    public Member(String userId, String password, String email,
                  String name, Role role) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public void updatePassword(String newPassword){
        this.password = newPassword;
    }

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<ChatBotRoom> chatBotRooms = new ArrayList<>();

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private final List<ChatRoom> chatRooms = new ArrayList<>();
}
