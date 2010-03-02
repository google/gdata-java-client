// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Immutable representation of a date with an optional time and an optional time
 * zone.
 */
public class DateTime {

  /** RFC 822 date/time format. */
  private static final SimpleDateFormat dateTimeFormat822 =
      new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

  private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

  static {
    dateTimeFormat822.setTimeZone(GMT);
  }

  /**
   * Date/time value expressed as the number of ms since the Unix epoch.
   * 
   * If the time zone is specified, this value is normalized to UTC, so to
   * format this date/time value, the time zone shift has to be applied.
   */
  public final long value;

  /** Specifies whether this is a date-only value. */
  public final boolean dateOnly;

  /**
   * Time zone shift from UTC in minutes. If {@code null}, no time zone is set,
   * and the time is always interpreted as local time.
   */
  public final Integer tzShift;

  public DateTime(Date date, TimeZone zone) {
    long value = date.getTime();
    this.dateOnly = false;
    this.value = value;
    this.tzShift = zone.getOffset(value) / 60000;
  }

  public DateTime(long value) {
    this(false, value, null);
  }

  public DateTime(Date value) {
    this(value.getTime());
  }

  public DateTime(boolean dateOnly, long value, Integer tzShift) {
    this.dateOnly = false;
    this.value = value;
    this.tzShift = tzShift;
  }

  /** Formats the value as an RFC 3339 date/time string. */
  public String toStringRfc3339() {

    StringBuilder sb = new StringBuilder();

    Calendar dateTime = new GregorianCalendar(GMT);
    long localTime = this.value;
    if (this.tzShift != null) {
      localTime += this.tzShift.longValue() * 60000;
    }

    dateTime.setTimeInMillis(localTime);

    appendInt(sb, dateTime.get(Calendar.YEAR), 4);
    sb.append('-');
    appendInt(sb, dateTime.get(Calendar.MONTH) + 1, 2);
    sb.append('-');
    appendInt(sb, dateTime.get(Calendar.DAY_OF_MONTH), 2);

    if (!dateOnly) {

      sb.append('T');
      appendInt(sb, dateTime.get(Calendar.HOUR_OF_DAY), 2);
      sb.append(':');
      appendInt(sb, dateTime.get(Calendar.MINUTE), 2);
      sb.append(':');
      appendInt(sb, dateTime.get(Calendar.SECOND), 2);

      if (dateTime.isSet(Calendar.MILLISECOND)) {
        sb.append('.');
        appendInt(sb, dateTime.get(Calendar.MILLISECOND), 3);
      }
    }

    if (tzShift != null) {

      if (tzShift.intValue() == 0) {

        sb.append('Z');

      } else {

        int absTzShift = tzShift.intValue();
        if (tzShift > 0) {
          sb.append('+');
        } else {
          sb.append('-');
          absTzShift = -absTzShift;
        }

        int tzHours = absTzShift / 60;
        int tzMinutes = absTzShift % 60;
        appendInt(sb, tzHours, 2);
        sb.append(':');
        appendInt(sb, tzMinutes, 2);
      }
    }

    return sb.toString();
  }

  @Override
  public String toString() {
    return toStringRfc822();
  }

  /** Formats the value as an RFC 822 date/time. */
  public String toStringRfc822() {
    if (this.dateOnly) {
      throw new IllegalArgumentException();
    }
    return dateTimeFormat822.format(this.value);
  }

  /**
   * Parses an RFC 3339 date/time value.
   */
  public static DateTime parseRfc3339(String str) throws NumberFormatException {
    Calendar dateTime = new GregorianCalendar(GMT);
    int year = Integer.parseInt(str.substring(0, 4));
    int month = Integer.parseInt(str.substring(5, 7)) - 1;
    int day = Integer.parseInt(str.substring(8, 10));
    int tzIndex;
    int length = str.length();
    boolean dateOnly =
        length <= 10 || Character.toUpperCase(str.charAt(10)) != 'T';
    if (dateOnly) {
      dateTime.set(year, month, day);
      tzIndex = 10;
    } else {
      int hourOfDay = Integer.parseInt(str.substring(11, 13));
      int minute = Integer.parseInt(str.substring(14, 16));
      int second = Integer.parseInt(str.substring(17, 19));
      dateTime.set(year, month, day, hourOfDay, minute, second);
      if (str.charAt(19) == '.') {
        int milliseconds = Integer.parseInt(str.substring(20, 23));
        dateTime.set(Calendar.MILLISECOND, milliseconds);
        tzIndex = 23;
      } else {
        tzIndex = 19;
      }
    }
    Integer tzShiftInteger = null;
    long value = dateTime.getTimeInMillis();
    if (length > tzIndex) {
      int tzShift;
      if (Character.toUpperCase(str.charAt(tzIndex)) == 'Z') {
        tzShift = 0;
      } else {
        tzShift =
            Integer.parseInt(str.substring(tzIndex + 1, tzIndex + 3)) * 60
                + Integer.parseInt(str.substring(tzIndex + 4, tzIndex + 6));
        if (str.charAt(tzIndex) == '-') {
          tzShift = -tzShift;
        }
        value -= tzShift * 60000;
      }
      tzShiftInteger = tzShift;
    }
    return new DateTime(dateOnly, value, tzShiftInteger);
  }

  /** Appends a zero-padded number to a string builder. */
  private static void appendInt(StringBuilder sb, int num, int numDigits) {
    if (num < 0) {
      sb.append('-');
      num = -num;
    }
    int x = num;
    while (x > 0) {
      x /= 10;
      numDigits--;
    }
    for (int i = 0; i < numDigits; i++) {
      sb.append('0');
    }
    if (num != 0) {
      sb.append(num);
    }
  }
}
