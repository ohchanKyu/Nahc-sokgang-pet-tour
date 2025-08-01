package kr.ac.dankook.SokGangPetTour.dto.response.authResponse;

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

    private String id;
    private String name;
    private String userId;
    private LocalDateTime createTime;
    private String email;
    private Role role;
}
