package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourDetailIntroCultureResponse {

    private String accomCountCulture;       // 수용인원
    private String chkCreditCardCulture;    // 신용카드 가능 여부
    private String infoCenterCulture;       // 문의 및 안내
    private String parkingCulture;          // 주차시설
    private String parkingFee;              // 주차요금
    private String restDateCulture;         // 쉬는날
    private String useFee;                  // 이용요금
    private String useTimeCulture;          // 이용시간
    private String scale;                   // 규모
}
