package org.zerock.moamoa.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class TimeUtils {
    public final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
    public final static ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");

    // Instant -> LocalDataTime 변환
    public static LocalDateTime toLocalTime(Instant instant) {
        return instant.atZone(koreaZoneId).toLocalDateTime();
    }

    public static Instant toInstant(String dateString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(koreaZoneId);

        return zonedDateTime.toInstant();
    }

}
