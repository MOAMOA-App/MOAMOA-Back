package org.zerock.moamoa.common.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomNick {
    public static String printRandNick() {
        ArrayList<String> nickarr1 = new ArrayList<>(
                Arrays.asList("무지개", "분홍", "오렌지", "개나리", "연두", "해변의", "퍼렁", "보라", "갈색", "하얀"));
        ArrayList<String> nickarr2 = new ArrayList<>(
                Arrays.asList("웨옹", "곰돌이", "귀긴곰", "꽥", "고양이", "냥이", "곰", "토끼", "오리"));

        Random rnd = new Random();
        int rnum1 = rnd.nextInt(nickarr1.size());
        int rnum2 = rnd.nextInt(nickarr2.size());

        return nickarr1.get(rnum1) + nickarr2.get(rnum2) + rnd.nextInt(99999999);
    }
}
