package kr.ac.dankook.SokGangPetTour.dto.response.tourResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TourSyncResponse {

    SUCCESS_SYNC_ALL_DATE("데이터 전체 동기화를 완료하였습니다.");

    private final String message;
}
