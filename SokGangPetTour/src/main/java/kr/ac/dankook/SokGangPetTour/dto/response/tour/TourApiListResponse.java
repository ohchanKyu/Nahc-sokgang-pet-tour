package kr.ac.dankook.SokGangPetTour.dto.response.tour;

import lombok.Data;

import java.util.List;

@Data
public class TourApiListResponse {
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
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }
    @Data public static class Items {
        private List<Item> item;
    }
    @Data public static class Item {
        private String contentid;
        private String title;
        private String addr1;
        private String addr2;
        private String mapx;
        private String mapy;
        private String firstimage;
        private String areacode;
        private String sigungucode;
        private String contenttypeid;
        private String modifiedtime;
    }
}
