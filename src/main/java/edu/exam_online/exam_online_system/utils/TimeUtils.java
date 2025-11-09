package edu.exam_online.exam_online_system.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtils {
    public static final String timeZone = "Asia/Ho_Chi_Minh";

    public static LocalDateTime getCurrentTime() {
        return ZonedDateTime.now(ZoneId.of(timeZone)).toLocalDateTime();
    }
}