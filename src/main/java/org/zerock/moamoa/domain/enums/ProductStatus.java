package org.zerock.moamoa.domain.enums;

public enum ProductStatus {
    
    RECRUITING(1, "참여 모집"),
    IN_PROGRESS(2, "거래 진행"),
    COMPLETED(3, "거래 완료");

    private final int number;
    private final String text;

    ProductStatus(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }
}
