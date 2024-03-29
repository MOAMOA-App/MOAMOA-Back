package org.zerock.moamoa.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "US_001", "회원을 찾을 수 없습니다."),
    USER_EMAIL_USED(HttpStatus.BAD_REQUEST, "US_002", "이미 사용중인 이메일 입니다."),
    USER_ACCESS_REJECTED(HttpStatus.FORBIDDEN, "US_003", "접근 권한이 없습니다."),
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "AT_001", "인증을 찾을 수 없습니다."),
    AUTH_PASSWORD_UNEQUAL(HttpStatus.NOT_FOUND, "AT_002", "비밀번호가 일치하지 않습니다."),
    AUTH_REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AT_003", "리프레쉬 토큰을 찾을 수 없습니다."),


    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PD_001", "상품을 찾을 수 없습니다."),
    PRODUCT_AUTH_FAIL(HttpStatus.BAD_REQUEST, "PD_002", "상품 인증에 실패했습니다."),

    INVALID_VALUE_COUNT(HttpStatus.BAD_REQUEST, "PD_003", "상품 개수는 1개 이상이어야 합니다."),
    INVALID_VALUE_PRICE(HttpStatus.BAD_REQUEST, "PD_004", "상품 가격은 0원 이상이어야 합니다."),
    INVALID_VALUE_DATE(HttpStatus.BAD_REQUEST, "PD_005", "지난 날짜는 지정할 수 없습니다."),

    ANNOUNCE_NOT_FOUND(HttpStatus.NOT_FOUND, "AN_001", "공지를 찾을 수 없습니다."),

    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "PT_001", "파티를 찾을 수 없습니다."),

    NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NO_001", "알림을 찾을 수 없습니다."),

    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CH_001", "채팅방을 찾을 수 없습니다"),

    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL_OO1", "파일 업로드 중 오류가 발생하였습니다."),
    FILE_READING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL_OO2", "파일 읽기 중 오류가 발생하였습니다."),
    FILE_STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL_OO3", "파일 저장 중 오류가 발생하였습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F_004", "파일을 찾을 수 없습니다."),
    FILE_SIZE_ZERO(HttpStatus.BAD_REQUEST, "F_005", "파일 크기가 0입니다."),
    FILE_NOT_VALID_EXTENSION(HttpStatus.BAD_REQUEST, "F_006", "허용되지 않은 확장자 입니다."),

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "S_002", "잘못된 요청 값입니다."),
    ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "CR_002", "방을 찾을 수 없습니다."),

    INVALID_EMAIL_VALUE(HttpStatus.BAD_REQUEST, "EM_001", "존재하지 않는 이메일입니다."),
    INVALID_EMAIL_EXIST(HttpStatus.BAD_REQUEST, "EM_002", "이미 존재하는 이메일입니다."),
    INVALID_PW_VALUE(HttpStatus.BAD_REQUEST, "PW_001", "비밀번호가 맞지 않습니다."),


    SEARCH_KEYWORD_NOT_FOUND(HttpStatus.BAD_REQUEST, "SK_001", "검색어가 없습니다.");



    //------------------------------------------------------------------------//
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
