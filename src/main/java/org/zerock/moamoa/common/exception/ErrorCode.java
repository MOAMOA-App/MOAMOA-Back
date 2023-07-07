package org.zerock.moamoa.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "US_001", "회원을 찾을 수 없습니다."),

	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PD_001", "상품을 찾을 수 없습니다."),

	ANNOUNCE_NOT_FOUND(HttpStatus.NOT_FOUND, "AN_001", "공지을 찾을 수 없습니다."),

	PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "PT_001", "파티을 찾을 수 없습니다."),

	NOTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "NO_001", "알림을 찾을 수 없습니다."),

	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL_OO1", "파일 업로드 중 오류가 발생하였습니다."),
	FILE_READING_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL_OO2", "파일 읽기 중 오류가 발생하였습니다."),
	FILE_STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FL_OO3", "파일 저장 중 오류가 발생하였습니다."),
	FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "F_004", "파일을 찾을 수 없습니다."),
	FILE_SIZE_ZERO(HttpStatus.BAD_REQUEST, "F_005", "파일 크기가 0입니다."),

	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "S_002", "잘못된 요청 값입니다."),
	;

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
