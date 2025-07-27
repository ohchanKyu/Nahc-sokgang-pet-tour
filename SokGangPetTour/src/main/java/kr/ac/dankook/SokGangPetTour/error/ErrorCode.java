package kr.ac.dankook.SokGangPetTour.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 회원관련 에러
    MEMBER_NOT_FOUND("M001","회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL("M002","이미 존재하는 이메일입니다."),
    INVALID_PASSWORD("M003","비밀번호가 일치하지 않습니다."),

    // 인증관련 에러
    UNAUTHORIZED("A001","인증되지 않은 접근입니다."),
    INVALID_TOKEN("A002","유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("A003","만료된 토큰입니다."),

    // 권한관련 에러
    ACCESS_DENIED("P001","접근 권한이 존재하지 않습니다."),

    // 입력값 검증 에러
    INVALID_INPUT_VALUE("V001","잘못된 입력값입니다."),
    INVALID_EMAIL_FORMAT("V002","잘못된 이메일 형식입니다."),

    // 서버 에러
    INTERNAL_SERVER_ERROR("S001","서버 내부 오류가 발생하였습니다.");

    private final String code;
    private final String message;
}
