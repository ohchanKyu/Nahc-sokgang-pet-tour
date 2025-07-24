package kr.ac.dankook.SokGangPetTour.entity.tour.tourIntro;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.ac.dankook.SokGangPetTour.entity.tour.TourDetailIntro;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tour_detail_intro_festival") // 행사/공연/축제
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TourDetailIntroFestival extends TourDetailIntro {

    private String ageLimit;           // 관람 가능연령
    private String bookingPlace;       // 예매처
    private String discountInfoFestival; // 할인정보
    private String eventEndDate;       // 행사 종료일
    private String eventHomePage;      // 행사 홈페이지
    private String eventPlace;         // 행사 장소
    private String eventStartDate;     // 행사 시작일
    private String festivalGrade;      // 축제등급
    private String placeInfo;          // 행사장 위치안내
    private String playTime;           // 공연시간
    private String program;            // 행사 프로그램
    private String spendTimeFestival;  // 관람 소요시간
    private String sponsor1;           // 주최자 정보
    private String sponsor1Tel;        // 주최자 연락처
    private String sponsor2;           // 주관사 정보
    private String sponsor2Tel;        // 주관사 연락처
    private String subEvent;           // 부대행사
    private String useTimeFestival;    // 이용요금
}