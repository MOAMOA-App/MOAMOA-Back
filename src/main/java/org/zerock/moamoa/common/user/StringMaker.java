package org.zerock.moamoa.common.user;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@Slf4j
public class StringMaker {

    private static final String BASE62_CHARS = "7WhX2etEHIbs8mCAOVZc1nuD5F3fSUrdgwqQ4oPRzTJjGplN6Likxy90vMaBYK";
//    "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" 랜덤돌림
    private static final int BASE62 = 62;
    private static final int minLength = 4;

    public static String verifyEmptyNick(String nick){
        if (nick == null || nick.isEmpty()){
            return printRandNick();
        } else return nick;
    }

    public static String printRandNick() {
        ArrayList<String> nickarr1 = new ArrayList<>(
                Arrays.asList("무지개", "분홍", "오렌지", "개나리", "연두", "해변의", "퍼렁", "보라", "갈색", "하얀"));
        ArrayList<String> nickarr2 = new ArrayList<>(
                Arrays.asList("웨옹", "곰돌이", "귀긴곰", "꽥", "고양이", "냥이", "곰", "토끼", "오리"));

        Random rnd = new Random();
        int rnum1 = rnd.nextInt(nickarr1.size());
        int rnum2 = rnd.nextInt(nickarr2.size());

        return nickarr1.get(rnum1) + nickarr2.get(rnum2);
    }

//    public static String makeUserCode(long value){
//        return printRandom62(value) + printIdto62(value);
//    }
//
//    public static String printRandom62(long value){
//        // 첫 두글자는 62진수 랜덤
//        // 마지막 글자는 id 받아서 62진수 변환: 1~3844 반복
//        Random rnd = new Random();
//        StringBuilder key = new StringBuilder();
//
//        for (int i = 0; i < 2; i++) {
//            key.append(BASE62_CHARS.charAt(rnd.nextInt(BASE62)));
//        }
//        return key.toString();
//    }
//
//    public static String printIdto62(long value) {
//        value %= 3844;
//        StringBuilder sb = new StringBuilder();
//        do {
//            int remainder = (int) (value % BASE62);
//            sb.insert(0, BASE62_CHARS.charAt(remainder));
//            value = value / BASE62;
//        } while (value > 0);
//
//        if (sb.length() < 2){
//            return "0".repeat(2 - sb.length()) + sb;
//        }
//
//        return sb.toString();
//    }

    public static String idto62Code(long value) {
//        value = (value * 9) + 256;
        StringBuilder sb = new StringBuilder();
        do {
            int remainder = (int) (value % BASE62);
            sb.insert(0, BASE62_CHARS.charAt(remainder));
            value = value / BASE62;
        } while (value > 0);

        if (sb.length() < minLength){
            return String.valueOf(BASE62_CHARS.charAt(0)).repeat(minLength - sb.length()) + sb;
        }

        return sb.toString();
    }

//    public static long fromBase62(String str) {
//        long result = 0;
//
//        for (int i = 0; i < str.length(); i++) {
//            char c = str.charAt(i);
//            int digit = BASE62_CHARS.indexOf(c);
//            result = result * BASE62 + digit;
//        }
//
//        return result;
//    }
}
