package org.zerock.moamoa.domain.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final Map<Integer, String> CODE_MAP = Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(ProductStatus::getCode, ProductStatus::name)));
    private static final Map<String, String> MESSAGE_MAP = Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(ProductStatus::getMessage, ProductStatus::name)));

    public static ProductStatus fromCode(int code) {
        if (!CODE_MAP.containsKey(code)) return null;
        return ProductStatus.valueOf(CODE_MAP.get(code));
    }

    public static ProductStatus fromMessage(String message) {
        if (!MESSAGE_MAP.containsKey(message)) return null;
        return ProductStatus.valueOf(MESSAGE_MAP.get(message));
    }
}
