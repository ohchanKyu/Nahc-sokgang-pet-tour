package kr.ac.dankook.SokGangPetTour;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.dankook.SokGangPetTour.dto.request.authRequest.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 통합 테스트 - 성공")
    void signupIntegrationTest() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        SignupRequest request = SignupRequest.builder()
                .userId("testUser")
                .password("testUser1234!")
                .email("test123@test.com")
                .name("test")
                .build();
        mockMvc.perform(post("/auth/identity/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andExpect(jsonPath("$.data.refreshToken").exists());
    }
}
