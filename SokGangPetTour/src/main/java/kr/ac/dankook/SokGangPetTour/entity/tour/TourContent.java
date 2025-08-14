package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.*;
import kr.ac.dankook.SokGangPetTour.entity.vetPlace.VetStatic;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tour_content")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TourContent {

    @Id
    private String contentId;
    // 주소
    private String address;
    // 상세주소
    private String detailAddress;
    // 우편번호
    private String postCode;
    // 지역코드
    private int areaCode;
    // 시군구 코드
    private int sigunguCode;
    // 대분류
    private String cat1;
    // 중분류
    private String cat2;
    // 소분류
    private String cat3;
    // 콘텐츠 타입 ID - 관광타입
    private String contentTypeId;
    private String originalImageUrl;
    private String thumbnailImageUrl;
    // 경도
    private double longitude; 
    // 위도
    private double latitude;
    // 전화번호
    private String telephone;
    // 장소 이름
    private String title;
    // 개요
    @Column(columnDefinition = "TEXT")
    private String overview;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourStatic> tourStatics = new ArrayList<>();

    // 소개 정보
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TourDetailIntro> detailIntros = new ArrayList<>();

    // 반복정보
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TourDetailRepeat> detailRepeats = new ArrayList<>();

    // 반려동물 동반 이미지
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TourDetailImage> detailImages = new ArrayList<>();

    // 반려동물 동반 정보
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TourDetailPet> detailPet;

}
