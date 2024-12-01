package com.lsadf.core.utils;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import lombok.experimental.UtilityClass;

/** Utility class for date transformation operations */
@UtilityClass
public class DateUtils {
  public static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
  public static final DateTimeFormatter exportDateFormatter =
      DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
  public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Convert LocalDateTime to String
   *
   * @param dateTime LocalDateTime to convert
   * @return Converted Date in String format
   */
  public static String dateTimeToString(LocalDateTime dateTime) {
    return dateTime.format(formatter);
  }

  /**
   * Convert Date to String
   *
   * @param date Date to convert
   * @return Converted Date in String format
   */
  public static String dateToString(Date date) {
    return dateToLocalDateTime(date).format(formatter);
  }

  /**
   * Convert Date to LocalDateTime
   *
   * @param dateToConvert Date to convert
   * @return Converted LocalDateTime
   */
  public static LocalDateTime dateToLocalDateTime(Date dateToConvert) {
    return new Timestamp(dateToConvert.getTime()).toLocalDateTime();
  }

  /**
   * Convert timestamp to LocalDateTime
   *
   * @param timestamp timestamp to convert
   * @return Converted LocalDateTime
   */
  public static LocalDateTime timestampToLocalDateTime(long timestamp) {
    Instant instant = Instant.ofEpochSecond(timestamp);
    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
  }

  /**
   * Convert String to Date
   *
   * @param date String to convert
   * @return Converted Date
   */
  public static Date stringToDate(String date) {
    return Date.from(
        LocalDateTime.parse(date, formatter).atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Convert Date from clock
   *
   * @param clock Clock to use
   * @return Date
   */
  public static Date dateFromClock(Clock clock) {
    return Date.from(Instant.now(clock));
  }
}
