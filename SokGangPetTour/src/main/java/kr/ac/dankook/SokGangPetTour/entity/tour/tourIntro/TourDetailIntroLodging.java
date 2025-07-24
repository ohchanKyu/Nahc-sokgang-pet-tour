package kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailIntro;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tour_detail_intro_lodging") // 숙박
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TourDetailIntroLodging extends TourDetailIntro {

    private String accomCountLodging;  // 수용 가능인원
    private String benikia;            // 베니키아 여부
    private String checkInTime;        // 입실 시간
    private String checkOutTime;       // 퇴실 시간
    private String chkCooking;         // 객실내 취사 여부
    private String foodPlace;          // 식음료장
    private String goodStay;           // 굿스테이 여부
    private String hanok;              // 한옥 여부
    private String infoCenterLodging;  // 문의 및 안내
    private String parkingLodging;     // 주차시설
    private String pickup;             // 픽업 서비스
    private String roomCount;          // 객실수
    private String reservationLodging; // 예약안내
    private String reservationUrl;     // 예약안내 홈페이지
    private String roomType;           // 객실유형
    private String scaleLodging;       // 규모
    private String subFacility;        // 부대시설 (기타)
    private String barbecue;           // 바비큐장 여부
    private String beauty;             // 뷰티시설 정보
    private String beverage;           // 식음료장 여부
    private String bicycle;            // 자전거 대여 여부
    private String campfire;           // 캠프파이어 여부
    private String fitness;            // 휘트니스 센터 여부
    private String karaoke;            // 노래방 여부
    private String publicBath;         // 공용 샤워실 여부
    private String publicPc;           // 공용 PC실 여부
    private String sauna;              // 사우나실 여부
    private String seminar;            // 세미나실 여부
    private String sports;             // 스포츠 시설 여부
    private String refundRegulation;   // 환불규정
}
