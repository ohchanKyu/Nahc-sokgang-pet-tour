package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourDetailIntroTouristResponse {

    private String accomCount;       // 수용인원
    private String chkCreditCard;    // 신용카드 가능 여부
    private String expAgeRange;      // 체험가능 연령
    private String expGuide;         // 체험안내
    private String heritage1;        // 세계 문화유산 유무
    private String heritage2;        // 세계 자연유산 유무
    private String heritage3;        // 세계 기록유산 유무
    private String infoCenter;       // 문의 및 안내
    private String openDate;         // 개장일
    private String parking;          // 주차시설
    private String restDate;         // 쉬는날
    private String useSeason;        // 이용시기
    private String useTime;          // 이용시간
}
