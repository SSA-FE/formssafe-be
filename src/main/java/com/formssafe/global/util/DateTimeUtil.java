package com.formssafe.global.util;

import java.time.LocalDateTime;

public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    /**
     * 현재 시각을 분 단위로 가져온다.
     *
     * @return 분 단위 현재 시각
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now().withSecond(0).withNano(0);
    }

    public static LocalDateTime truncateSecondsAndNanos(LocalDateTime dateTime) {
        return dateTime.withSecond(0).withNano(0);
    }
}
