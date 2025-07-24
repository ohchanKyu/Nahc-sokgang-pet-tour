package kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailIntro;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tour_detail_intro_food") // 음식점
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TourDetailIntroFood extends TourDetailIntro {

    private String chkCreditCardFood;   // 신용카드 가능 여부
    private String discountInfoFood;    // 할인정보
    private String firstMenu;           // 대표 메뉴
    private String infoCenterFood;      // 문의 및 안내
    private String kidsFacility;        // 어린이 놀이방 여부
    private String openDateFood;        // 개업일
    private String openTimeFood;        // 영업시간
    private String packing;             // 포장 가능
    private String parkingFood;         // 주차시설
    private String reservationFood;     // 예약안내
    private String restDateFood;        // 쉬는날
    private String scaleFood;           // 규모
    private String seat;                // 좌석수
    private String smoking;             // 금연/흡연 여부
    private String treatMenu;           // 취급 메뉴
}

