package kr.ac.dankook.SokGangPetTour.dto.response.authResponse;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
}
