package org.zerock.moamoa.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class TimeUtils {
    public final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
    public final static ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");

    // Instant -> LocalDataTime 변환
    public static String toLocalTime(Instant instant) {
        return instant.atZone(koreaZoneId).toLocalDateTime().format(formatter);
    }

    public static Instant toInstant(String dateString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(koreaZoneId);

        return zonedDateTime.toInstant();
    }

}
