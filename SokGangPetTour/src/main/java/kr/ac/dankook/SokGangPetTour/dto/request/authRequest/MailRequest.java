package kr.ac.dankook.SokGangPetTour.dto.request.authRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MailRequest {
    private String email;
    private String title;
    private String content;
}
