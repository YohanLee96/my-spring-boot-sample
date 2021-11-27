package com.gsd.global.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;

/**
 * 날짜 관련 유틸 클래스
 * Created by Yohan lee
 * Created on 2021-02-16.
 **/
public class DateTimeUtil {


    /**
     * 년-월의 첫 일자를 가져온다.
     * @param yearMonth 년-월
     * @return 년-월의 첫일자를 담은 LocalDateTime
     */
    public static LocalDateTime getMonthFirstTime(YearMonth yearMonth) {
        return yearMonth.atDay(1).atTime(LocalTime.MIN);
    }

    /**
     * 년-월의 마지막 일자를 가져온다.
     * @param yearMonth 년-월
     * @return 년-월의 마지막 일자를 담은 LocalDateTime
     */
    public static LocalDateTime getMonthLastTime(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
    }

}
