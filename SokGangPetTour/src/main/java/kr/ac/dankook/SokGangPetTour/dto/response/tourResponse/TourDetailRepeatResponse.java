package kr.ac.dankook.SokGangPetTour.dto.response.tourResponse;

import lombok.Data;

import java.util.List;

@Data
public class TourDetailRepeatResponse {
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
        private String infoname;
        private String infotext;
    }
}