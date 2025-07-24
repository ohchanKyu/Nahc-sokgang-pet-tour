package kr.ac.dankook.SokGangPetTour.dto.response.tour;

import lombok.Data;

import java.util.List;

@Data
public class TourDetailIntroResponse {
    private Response response;
    @Data public static class Response {
        private Header header;
        private Body body;
    }
    @Data public static class Header {
        private String resultCode;
        private String resultMsg;
    }
    @Data public static class Body {
        private Items items;
    }
    @Data public static class Items {
        private List<Item> item;
    }
    @Data public static class Item {
        private String contentid;
        private String infocenter;
        private String parking;
        private String restdate;
        private String usetime;
        private String chkcreditcard;
        private String accomcount;
        // 타입별로 필드 다를 수 있음 (Map으로 받을 수도 있음)
    }
}
