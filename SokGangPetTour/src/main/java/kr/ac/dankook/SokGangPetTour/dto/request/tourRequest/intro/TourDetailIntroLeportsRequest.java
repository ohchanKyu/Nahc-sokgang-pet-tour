package kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TourDetailIntroLeportsRequest {

    private String contentTypeId;
    private String accomCountLeports;       // 수용인원
    private String chkCreditCardLeports;    // 신용카드가능 정보
    private String expAgeRangeLeports;      // 체험 가능연령
    private String infoCenterLeports;       // 문의 및 안내
    private String openPeriod;              // 개장기간
    private String parkingFeeLeports;       // 주차요금
    private String parkingLeports;          // 주차시설
    private String reservation;             // 예약안내
    private String restDateLeports;         // 쉬는날
    private String scaleLeports;            // 규모
    private String useFeeLeports;           // 입장료
    private String useTimeLeports;          // 이용시간
}
