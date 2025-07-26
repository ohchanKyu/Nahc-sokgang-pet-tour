package kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailIntro;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tour_detail_intro_shopping") // 쇼핑
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class TourDetailIntroShopping extends TourDetailIntro {

    private String chkCreditCard;     // 신용카드가능 정보
    @Column(length = 2000)
    private String infoCenterShopping; // 문의 및 안내
    private String openTime;          // 영업시간
    private String parkingShopping;   // 주차시설
    private String restDateShopping;  // 쉬는날
    @Column(length = 2000)
    private String restRoom;          // 화장실 설명
    private String saleItem;          // 판매 품목
    private String scaleShopping;     // 규모
}

