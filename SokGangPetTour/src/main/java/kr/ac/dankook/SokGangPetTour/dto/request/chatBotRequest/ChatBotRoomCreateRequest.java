package kr.ac.dankook.SokGangPetTour.dto.request.chatBotRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatBotRoomCreateRequest {

    @NotBlank(message = "챗봇방 이름은 필수입니다.")
    @Size(max = 50,min = 2)
    private String title;
    @NotBlank(message = "회원 정보가 필요합니다.")
    private String memberId;
}
