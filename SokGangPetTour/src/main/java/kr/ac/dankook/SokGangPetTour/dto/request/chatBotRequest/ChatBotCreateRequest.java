package kr.ac.dankook.SokGangPetTour.dto.request.chatBotRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.ac.dankook.SokGangPetTour.entity.chatBot.ChatBotRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class ChatBotCreateRequest {

    private String sessionId;
    private String content;
    private ChatBotRole role;
}
