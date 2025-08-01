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

    @NotBlank(message = "세션 아이디는 필수입니다.")
    private String sessionId;
    @NotBlank(message = "챗봇 질문 및 응답 내역은 필수입니다.")
    private String content;
    @NotNull(message = "채팅 내역에 대한 역할은 필수입니다.")
    private ChatBotRole role;
}
