package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tour_detail_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDetailImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private TourContent content;

    // 원본 이미지
    private String originImgUrl;
    // 썸네일 이미지
    private String smallImgUrl;
    // 이미지 이름
    private String imgName;
}