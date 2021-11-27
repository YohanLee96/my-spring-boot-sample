package com.gsd.global.util;

import java.time.*;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Objects;

/**
 * 날짜 관련 유틸 클래스
 * Created by Yohan lee
 * Created on 2021-02-16.
 **/
public class DateTimeUtil {


    public static LocalDateTime getMinLocalDateTime() {
        return LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    }

    public static LocalDateTime getMaxLocalDateTime() {
        return LocalDateTime.of(2099, 12, 31, 23, 59, 59);
    }

    /**
     * 두 날짜의 차이를 구한다.
     * @param startDate 시작날짜
     * @param endDate 마지막 날짜
     * @return 날짜 차이
     */
    public static int betweenDays(LocalDate startDate, LocalDate endDate) {
        return Period.between(startDate, endDate).getDays();
    }

    /**
     * ex) before <= targetDate <= after
     * @param targetDate 비교할 날짜
     * @param beforeDate 이후
     * @param afterDate 이하
     * @return 비교 결과
     */
    public static boolean isBetweenDate(LocalDate targetDate, LocalDate beforeDate, LocalDate afterDate) {
        if(isIncludeNull(targetDate, beforeDate, afterDate)) {
            throw new IllegalArgumentException("One parameter must not be null.");
        }

        if(targetDate.isBefore(afterDate) && targetDate.isAfter(beforeDate)) {
            return true;
        }

        return targetDate.equals(beforeDate) || targetDate.equals(afterDate);
    }

    /**
     * ex) before <= targetDateTime <= after
     * @param targetDateTime 비교할 날짜시간
     * @param beforeDateTime 이후
     * @param afterDateTime 이하
     * @return 비교 결과
     */
    public static boolean isBetweenDateTime(LocalDateTime targetDateTime,
                                            LocalDateTime beforeDateTime,
                                            LocalDateTime afterDateTime) {
        if(isIncludeNull(targetDateTime, beforeDateTime, afterDateTime)) {
            throw new IllegalArgumentException("One parameter must not be null.");
        }

        if(targetDateTime.isBefore(afterDateTime) && targetDateTime.isAfter(beforeDateTime)) {
            return true;
        }

        return targetDateTime.equals(beforeDateTime) || targetDateTime.equals(afterDateTime);
    }

    private static boolean isIncludeNull(Temporal... vars) {
        return Arrays.stream(vars).anyMatch(Objects::isNull);
    }

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
