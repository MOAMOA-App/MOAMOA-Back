package org.zerock.moamoa.domain.enums;

import lombok.Getter;

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

	Category(int code, String name) {
		this.code = code;
		this.name = name;
	}

	private final int code;
	private final String name;
}
