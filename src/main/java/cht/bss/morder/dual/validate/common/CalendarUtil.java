package cht.bss.morder.dual.validate.common;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

/**
 * @author 1909002
 * CalendarUtil 轉換 Calendar 至  OffsetDateTime
 */
public class CalendarUtil {

    /**
     * 轉換 Calendar 至 OffsetDateTime
     *
     * @param calendar 要轉換的Calendar物件
     * @return offsetDateTime 預設為UTC zero
     */
    public static OffsetDateTime calendarToOffsetDateTime(final Calendar calendar) {
        final Instant instant = calendar.getTime().toInstant();
        return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
}
