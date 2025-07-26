package kr.ac.dankook.SokGangPetTour.dto.request.tourRequest.intro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TourDetailIntroShoppingRequest {

    private String contentTypeId;
    private String chkCreditCard;     // 신용카드가능 정보
    private String cultureCenter;     // 문화센터 바로가기
    private String fairDay;           // 장서는 날
    private String infoCenterShopping; // 문의 및 안내
    private String openDateShopping;  // 개장일
    private String openTime;          // 영업시간
    private String parkingShopping;   // 주차시설
    private String restDateShopping;  // 쉬는날
    private String restRoom;          // 화장실 설명
    private String saleItem;          // 판매 품목
    private String saleItemCost;      // 판매 품목별 가격
    private String scaleShopping;     // 규모
    private String shopGuide;         // 매장안내
}
