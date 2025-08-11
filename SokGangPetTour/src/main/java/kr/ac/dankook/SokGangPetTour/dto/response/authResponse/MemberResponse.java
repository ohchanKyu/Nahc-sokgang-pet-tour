package kr.ac.dankook.SokGangPetTour.dto.response.authResponse;

import kr.ac.dankook.SokGangPetTour.entity.Member;
import kr.ac.dankook.SokGangPetTour.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {

    private String name;
    private String userId;
    private String email;
    private LocalDateTime createTime;
    private String role;

    public MemberResponse(Member member) {
        this.name = member.getName();
        this.userId = member.getUserId();
        this.email = member.getEmail();
        this.createTime = LocalDateTime.now();
        this.role = member.getRole().name();
    }
}
