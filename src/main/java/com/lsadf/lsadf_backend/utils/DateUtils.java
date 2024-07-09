package com.lsadf.lsadf_backend.utils;

import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for date transformation operations
 */
@UtilityClass
public class DateUtils {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter exportDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Convert LocalDateTime to String
     * @param dateTime LocalDateTime to convert
     * @return Converted Date in String format
     */
    public static String dateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    /**
     * Convert Date to String
     * @param date Date to convert
     * @return Converted Date in String format
     */
    public static String dateToString(Date date) {
        LocalDateTime dateTime = dateToLocalDateTime(date);
        return dateTimeToString(dateTime);
    }

    /**
     * Convert LocalDateTime to String
     * @param dateTime LocalDateTime to convert
     * @return Converted Date
     */
    public static String dateTimeToExportString(LocalDateTime dateTime) {
        return dateTime.format(exportDateFormatter);
    }

    /**
     * Convert Date to String
     * @param localDateTime
     * @return Converted Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date
                .from(localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    /**
     * Convert Date to LocalDateTime
     * @param dateToConvert Date to convert
     * @return Converted LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }

    /**
     * Convert timestamp to LocalDateTime
     * @param timestamp timestamp to convert
     * @param isSeconds true if timestamp is in seconds, false if in milliseconds
     * @return Converted LocalDateTime
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp, boolean isSeconds) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
