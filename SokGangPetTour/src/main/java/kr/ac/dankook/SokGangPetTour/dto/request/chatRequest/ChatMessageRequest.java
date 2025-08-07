package kr.ac.dankook.SokGangPetTour.dto.request.chatRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatMessageRequest {
    private String message;
    private String memberId;
}
