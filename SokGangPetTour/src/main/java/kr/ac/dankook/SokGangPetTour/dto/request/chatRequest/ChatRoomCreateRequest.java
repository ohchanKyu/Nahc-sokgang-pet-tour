package kr.ac.dankook.SokGangPetTour.dto.request.chatRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomCreateRequest {

    @NotBlank(message = "채팅방 이름은 필수항목입니다.")
    @Size(min = 2, max = 50, message = "채팅방 이름은 2~50 글자만 가능합니다.")
    private String name;

    @NotBlank(message = "채팅방 설명은 필수항목입니다.")
    @Size(min=10,max=80, message = "채팅방 설명은 10~80자만 가능합니다.")
    private String description;

    @Max(value = 1000,message = "최대 1000명까지만 수용가능합니다.")
    @Min(value = 2,message = "최소 채팅방 참가자 수 제한은 2명입니다.")
    private Integer maxParticipants;
    
    @NotBlank(message = "사용하실 닉네임은 필수항목입니다.")
    @Size(min=2, max=50, message = "사용하실 닉네임은 2~50 글자만 가능합니다.")
    private String nickname;
}
