package org.zerock.moamoa.common.message;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum SuccessMessage {
	PRODUCT_DELETE(HttpStatus.OK, "상품을 삭제했습니다."),

	USER_DELETE(HttpStatus.OK, "회원 탈퇴가 완료되었습니다."),

	NOTICE_DELETE(HttpStatus.OK, "알림을 삭제했습니다."),

	WISH_DELETE(HttpStatus.OK, "찜하기 취소")
	;

	SuccessMessage(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	private final HttpStatus httpStatus;
	private final String message;
}
