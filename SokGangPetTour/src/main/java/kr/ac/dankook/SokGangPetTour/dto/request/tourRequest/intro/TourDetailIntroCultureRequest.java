package kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TourDetailIntroCultureRequest {

    private String contentTypeId;
    private String accomCountCulture;       // 수용인원
    private String chkCreditCardCulture;    // 신용카드 가능 여부
    private String discountInfo;            // 할인정보
    private String infoCenterCulture;       // 문의 및 안내
    private String parkingCulture;          // 주차시설
    private String parkingFee;              // 주차요금
    private String restDateCulture;         // 쉬는날
    private String useFee;                  // 이용요금
    private String useTimeCulture;          // 이용시간
    private String scale;                   // 규모
}
