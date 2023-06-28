package org.zerock.moamoa.common.exception;

/** 상위 상속 Exception class */
public class BusinessException extends RuntimeException {

	private final String detail;
	//에러의 상세 정보

	private final ErrorCode errorCode;
	//에러 코드

	public BusinessException(ErrorCode errorCode, String detail) {
		super(errorCode.getMessage() + (detail == null ? "" : "(" + detail + ")"));
		this.detail = detail;
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode) {
		this(errorCode, null);
	}
}
