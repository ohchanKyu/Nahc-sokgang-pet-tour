package kr.ac.dankook.SokGangPetTour.dto.response.tour;

import lombok.Data;

import java.util.List;

@Data
public class TourDetailImageResponse {
    private Response response;
    @Data
    public static class Response {
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
        private String originimgurl;
        private String smallimageurl;
        private String imgname;
        private String cpyrhtDivCd;
    }
}