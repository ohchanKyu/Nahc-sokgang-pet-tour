package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourDetailIntroShoppingResponse {

    private String chkCreditCard;     // 신용카드가능 정보
    private String infoCenterShopping; // 문의 및 안내
    private String openTime;          // 영업시간
    private String parkingShopping;   // 주차시설
    private String restDateShopping;  // 쉬는날
    private String restRoom;          // 화장실 설명
    private String saleItem;          // 판매 품목
    private String scaleShopping;     // 규모
}
