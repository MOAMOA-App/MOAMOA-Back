package org.zerock.moamoa.domain.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Category {
    ALL(0, "전체"),
    INTEREST(1, "관심"),
    LIFE(2, "생활"),
    CLOTH(3, "의류"),
    FOOD(4, "음식"),
    HOUSEHOLD(5, "가전"),
    HOBBY(6, "취미"),
    SPORT(7, "운동"),
    GOODS(8, "굿즈"),
    ANIMAL(9, "동물"),
    OVERSEA(10, "해외"),
    ETC(11, "기타");

    Category(int code, String label) {
        this.code = code;
        this.label = label;
    }

    private final int code;
    private final String label;
    private static final Map<Integer, String> CODE_MAP = Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(Category::getCode, Category::name)));
    private static final Map<String, String> LABEL_MAP = Collections.unmodifiableMap(Stream.of(values()).collect(Collectors.toMap(Category::getLabel, Category::name)));

    public static Category fromCode(int code) {
        if (!CODE_MAP.containsKey(code)) return null;
        return Category.valueOf(CODE_MAP.get(code));
    }

    public static Category fromLabel(String label) {
        if (!LABEL_MAP.containsKey(label)) return null;
        return Category.valueOf(LABEL_MAP.get(label));
    }
}
