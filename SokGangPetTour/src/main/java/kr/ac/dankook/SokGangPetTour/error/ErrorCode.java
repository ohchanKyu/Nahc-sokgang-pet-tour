package kr.ac.dankook.SokGangPetTour.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 회원관련 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"M001","회원을 찾을 수 없습니다."),
    DUPLICATE_ID(HttpStatus.BAD_REQUEST,"M002","이미 존재하는 아이디입니다."),

    // 인증관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"A001","인증되지 않은 접근입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"A002","유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"A003","만료된 토큰입니다."),
    BAD_CREDENTIAL(HttpStatus.UNAUTHORIZED,"A004","자격증명에 실패하였습니다."),
    CERTIFICATE_SEND_MAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"A005","이메일 인증 과정 중 오류가 발생하였습니다."),

    // 채팅관련 에러
    EXCEED_PARTICIPANT(HttpStatus.INTERNAL_SERVER_ERROR,"C001","최대 참가 인원을 초과하였습니다."),
    RANGE_ERROR_PARTICIPANT(HttpStatus.NOT_FOUND,"C002","채팅방에 참여한 참가자가 존재하지 않습니다"),
    DELAY_JOIN_CHATROOM(HttpStatus.INTERNAL_SERVER_ERROR,"C003","채팅방 참여가 잠시 지연되었습니다. 3분 내로 입장 처리 하겠습니다."),
    DELAY_LEAVE_CHATROOM(HttpStatus.INTERNAL_SERVER_ERROR,"C004","채팅방 나가기가 잠시 지연되었습니다. 잠시만 기다려주세요."),
    ALREADY_JOIN_CHATROOM(HttpStatus.INTERNAL_SERVER_ERROR,"C005","이미 해당 채팅방에 참여중입니다."),

    // 권한관련 에러
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"P001","접근 권한이 존재하지 않습니다."),

    // 입력값 검증 에러
    INVALID_ENCRYPT_PK(HttpStatus.BAD_REQUEST,"V001", "유효하지 않은 데이터 아이디 형식입니다."),
    INVALID_REQUEST_PARAM(HttpStatus.BAD_REQUEST,"V002","필수 입력 항목이 누락되었습니다."),
    // 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"S001","서버 내부 오류가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
