package org.zerock.moamoa.domain.enums;

import lombok.Getter;

@Getter
public enum ProductStatus {
	READY(0, "거래 준비"),
	IN_PROGRESS(1, "거래 진행"),
	COMPLETED(2, "거래 완료"),

	;

	ProductStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}

	private final int code;
	private final String message;

}
