package kr.ac.dankook.SokGangPetTour.dto.response.tourResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TourSyncResponse {

    SUCCESS_SYNC_DATA("데이터 전체 동기화 완료."),
    SUCCESS_COMMON_DATA("데이터 공통 정보 동기화 완료."),
    SUCCESS_PET_DATA("데이터 반려동물 동반 정보 동기화 완료,"),
    SUCCESS_IMAGE_DATA("데이터 이미지 정보 동기화 완료,"),
    SUCCESS_REPEAT_DATA("데이터 반복 정보 동기화 완료"),
    SUCCESS_INTRO_DATA("데이터 소개 정보 동기화 완료");
    private final String message;
}
