package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourDetailIntroFoodResponse {
    private String chkCreditCardFood;   // 신용카드 가능 여부
    private String firstMenu;           // 대표 메뉴
    private String infoCenterFood;      // 문의 및 안내
    private String kidsFacility;        // 어린이 놀이방 여부
    private String openTimeFood;        // 영업시간
    private String packing;             // 포장 가능
    private String parkingFood;         // 주차시설
    private String reservationFood;     // 예약안내
    private String restDateFood;        // 쉬는날
    private String smoking;             // 금연/흡연 여부
    private String treatMenu;           // 취급 메뉴
}
