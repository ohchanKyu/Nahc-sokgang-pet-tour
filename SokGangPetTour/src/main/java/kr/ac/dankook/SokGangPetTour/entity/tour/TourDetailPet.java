package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tour_detail_pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourDetailPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private TourContent content;

    // 관련 사고 대비사항
    private String relaAcdntRiskMtr;
    // 동반 구분 String
    private String acmpyTypeCd;
    // 관련 구비 시설
    private String relaPosesFclty;
    // 관련 비치 품목
    private String relaFrnshPrdlst;
    // 기타 동반 정보
    private String etcAcmpyInfo;
    // 동반 가능 동물 정보
    private String acmpyPsblCpam;
    // 관련 렌탈 품목
    private String relaRntlPrdlst;
    // 동반 시 필요사항
    private String acmpyNeedMtr;
}