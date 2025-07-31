package kr.ac.dankook.SokGangPetTour.dto.response.tourPlaceResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TourDetailPetResponse {

    private String relaAcdntRiskMtr;
    private String acmpyTypeCd;
    private String relaPosesFclty;
    private String relaFrnshPrdlst;
    private String etcAcmpyInfo;
    private String acmpyPsblCpam;
    private String relaRntlPrdlst;
    private String acmpyNeedMtr;
}
