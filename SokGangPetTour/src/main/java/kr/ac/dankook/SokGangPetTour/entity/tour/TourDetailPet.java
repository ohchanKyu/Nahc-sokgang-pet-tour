package kr.ac.dankook.SokGangPetTour.entity.tour;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tour_detail_pet")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TourDetailPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "content_id", nullable = false)
    private TourContent content;


    // 관련 사고 대비사항
    @Column(columnDefinition = "TEXT")
    private String relaAcdntRiskMtr;
    // 동반 구분 String
    @Column(columnDefinition = "TEXT")
    private String acmpyTypeCd;
    // 관련 구비 시설
    @Column(columnDefinition = "TEXT")
    private String relaPosesFclty;
    // 관련 비치 품목
    @Column(columnDefinition = "TEXT")
    private String relaFrnshPrdlst;
    // 기타 동반 정보
    @Column(columnDefinition = "TEXT")
    private String etcAcmpyInfo;
    // 동반 가능 동물 정보
    @Column(columnDefinition = "TEXT")
    private String acmpyPsblCpam;
    // 관련 렌탈 품목
    @Column(columnDefinition = "TEXT")
    private String relaRntlPrdlst;
    // 동반 시 필요사항
    @Column(columnDefinition = "TEXT")
    private String acmpyNeedMtr;

    @Builder
    public TourDetailPet(TourContent tourContent, String relaAcdntRiskMtr,String acmpyTypeCd,
                         String relaPosesFclty,String relaFrnshPrdlst,String etcAcmpyInfo,
                         String acmpyPsblCpam, String relaRntlPrdlst, String acmpyNeedMtr){
        this.content = tourContent;
        this.relaAcdntRiskMtr = relaAcdntRiskMtr;
        this.acmpyTypeCd = acmpyTypeCd;
        this.relaPosesFclty = relaPosesFclty;
        this.relaFrnshPrdlst = relaFrnshPrdlst;
        this.etcAcmpyInfo = etcAcmpyInfo;
        this.acmpyPsblCpam = acmpyPsblCpam;
        this.relaRntlPrdlst = relaRntlPrdlst;
        this.acmpyNeedMtr = acmpyNeedMtr;
    }
}