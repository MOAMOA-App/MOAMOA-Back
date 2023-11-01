package org.zerock.moamoa.domain.enums;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Category {
    LIFE(1, "생활"),
    CLOTH(2, "의류"),
    FOOD(3, "음식"),
    HOUSEHOLD(4, "가전"),
    HOBBY(5, "취미"),
    SPORT(6, "운동"),
    GOODS(7, "굿즈"),
    ANIMAL(8, "동물"),
    OVERSEA(9, "해외"),
    ETC(10, "기타");

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
