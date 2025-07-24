package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tour_detail_repeat_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TourDetailRepeatRoom extends TourDetailRepeat{

    // 객실정보번호
    private String roomInfoNo;
    // 객실명칭
    private String roomTitle;
    // 객실크기(평)
    private String roomSize1;
    // 객실크기(평방미터)
    private String roomSize2;
    // 객실수
    private String roomCount;
    // 기준인원
    private String roomBaseCount;
    // 최대인원
    private String roomMaxCount;

    // 비수기주중최소
    private String roomOffSeasonMinFee1;
    // 비수기주말최소
    private String roomOffSeasonMinFee2;
    // 성수기주중최소
    private String roomPeakSeasonMinFee1;
    // 성수기주말최소
    private String roomPeakSeasonMinFee2;

    // 객실소개
    @Column(length = 2000)
    private String roomIntro;

    // 목욕 시설 여부
    private String roomBathFacility;
    // 욕조 여부
    private String roomBath;
    // 홈시어터 여부
    private String roomHomeTheater;
    // 에어컨 여부
    private String roomAirCondition;
    // TV 여부
    private String roomTv;
    // PC 여부
    private String roomPc;
    // 케이블 설치 여부
    private String roomCable;
    // 인터넷 여부
    private String roomInternet;
    // 냉장고 여부
    private String roomRefrigerator;
    // 세면도구 여부
    private String roomToiletries;
    // 소파 여부
    private String roomSofa;
    // 취사용품 여부
    private String roomCook;
    // 테이블 여부
    private String roomTable;
    // 드라이기 여부
    private String roomHairDryer;

    // 객실사진1
    private String roomImg1;
    // 객실사진1 설명
    private String roomImg1Alt;
    // 객실사진2
    private String roomImg2;
    // 객실사진2 설명
    private String roomImg2Alt;
    // 객실사진3
    private String roomImg3;
    // 객실사진3 설명
    private String roomImg3Alt;
    // 객실사진4
    private String roomImg4;
    // 객실사진4 설명
    private String roomImg4Alt;
    // 객실사진5
    private String roomImg5;
    // 객실사진5 설명
    private String roomImg5Alt;
}
