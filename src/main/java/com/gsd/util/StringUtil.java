package com.gsd.util;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * String 관련 유틸 클래스
 * Created by Yohan lee
 * Created on 2021-02-16.
 **/
public class StringUtil {

    /**
     * 시간 초를 00:00포맷으로 반환한다.
     * @param seconds 초
     * @return 00:00 포맷 문자열
     */
    public static String toStringBySeconds(long seconds) {
        if(seconds == 0) {
            return "00:00";
        }
        return String.format("%02d:%02d",  seconds/60, seconds % 60);
    }

    /**
     * 시간 초를 0분0초 포맷으로 반환한다.
     * @param seconds 초
     * @return 0분0초 포맷 문자열
     */
    public static String toKoreanBySeconds(long seconds) {
        if(seconds == 0) {
            return "0분0초";
        }
        return String.format("%d분%d초",  seconds/60, seconds % 60);
    }

    /**
     * YYYY-MM-DD 형식으로 포맷팅한다.
     * @param birth 생년월일
     * @return 포맷팅한 생년월일
     */
    public static String toBirthFormat(String birth) {
        if(StringUtils.isEmpty(birth)) {
            return birth;
        }

        String[] birthArr = birth.split("-");

        if(birthArr.length != 3) {
            return birth;
        }
        return String.format("%s-%02d-%02d", birthArr[0], Integer.parseInt(birthArr[1]), Integer.parseInt(birthArr[2]));
    }

    /**
     * 휴대폰 번호 형식을 010-0000-0000로 포맷팅한다.
     * @param phoneNumber 휴대폰 번호
     * @return 포맷팅한 휴대폰 번호
     */
    public static String toPhoneNumberFormat(String phoneNumber) {
        if(phoneNumber == null || phoneNumber.isEmpty()) {
            return "";
        }

        String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
        return phoneNumber.replaceAll(regEx, "$1-$2-$3");
    }


    /**
     * 구분자가 있는 문자열에서 중복 데이터를 제거한다.
     * ex) a,b,d,c,d -> a,b,d,c
     * @param str 대상 문자열
     * @param separator 구분자
     * @return 중복이 제거된 문자열
     */
    public static String removeDuplicateStrings(String str, String separator) {
        if(str == null || str.isEmpty()) {
            return  "";
        }

        Set<String> strSet = Arrays.stream(str.split(separator))
                .collect(Collectors.toSet());

        return  String.join(separator, strSet);
    }


    /**
     * [0-9][A-Z][a-z]를  이용하여 랜덤 문자열을 생성한다.
     * @param length 생성할 문자열 길이
     * @return  랜덤 문자열
     */
    public static String makeRandomString(int length) {
        char[] charArr = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (charArr.length * Math.random());
            sb.append(charArr[index]);
        }

        return sb.toString();
    }
}
