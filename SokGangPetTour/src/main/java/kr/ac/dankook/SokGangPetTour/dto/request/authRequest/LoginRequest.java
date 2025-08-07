package kr.ac.dankook.SokGangPetTour.dto.request.authRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "ID is Required.")
    private String userId;
    @NotBlank(message = "Password is Required.")
    private String password;
}
