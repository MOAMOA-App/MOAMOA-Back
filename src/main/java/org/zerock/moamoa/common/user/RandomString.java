package org.zerock.moamoa.common.user;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomString {
    public static String printRandNick() {
        ArrayList<String> nickarr1 = new ArrayList<>(
                Arrays.asList("무지개", "분홍", "오렌지", "개나리", "연두", "해변의", "퍼렁", "보라", "갈색", "하얀"));
        ArrayList<String> nickarr2 = new ArrayList<>(
                Arrays.asList("웨옹", "곰돌이", "귀긴곰", "꽥", "고양이", "냥이", "곰", "토끼", "오리"));

        java.util.Random rnd = new java.util.Random();
        int rnum1 = rnd.nextInt(nickarr1.size());
        int rnum2 = rnd.nextInt(nickarr2.size());

        return nickarr1.get(rnum1) + nickarr2.get(rnum2) + createCode(8);
    }

    public static String createCode(int num) {
        java.util.Random rand = new java.util.Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < num; i++) {
            key.append(rand.nextInt(10));
        }
        return key.toString();
    }
}
