package kr.ac.dankook.SokGangPetTour;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class PlaceTest {

    private MockWebServer mockWebServer;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    private static final String testUrl = "http://apis.data.go.kr/B551011/KorPetTourService/areaBasedList?ServiceKey=1V9C6uEjDwDGc%2FesUdArkydgirsHjgkfvYc30OOTYuG5HA%2FHZLro%2Fd3agpgZGoB05v1Pf7fPwq8%2Fg2rGyCOoxw%3D%3D&areaCode=32&sigunguCode=4&listYN=Y&arrange=A&pageNo=1&numOfRows=3&MobileOS=ETC&MobileApp=Test&_type=json";

    @Test
    @DisplayName("외부 API 호출 테스트")
    public void placeCallTest(){

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(200));

        String mockServerUrl = mockWebServer.url(testUrl).toString();

        webTestClient.get()
                .uri(mockServerUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(System.out::println);

    }
    @AfterEach
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }
}
