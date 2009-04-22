/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.data;

import com.google.gdata.util.ParseException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Represents a date/time, or a date without a time.  Optionally
 * includes a time zone.
 */
public class DateTime implements Comparable<Object> {


  public DateTime() {}


  public DateTime(long value) {
    this.value = value;
  }


  public DateTime(Date value) {
    this.value = value.getTime();
  }


  public DateTime(long value, int tzShift) {
    this.value = value;
    this.tzShift = new Integer(tzShift);
  }


  public DateTime(Date value, TimeZone zone) {
    this.value = value.getTime();
    this.tzShift = zone.getOffset(value.getTime()) / 60000;
  }


  public static DateTime now() {
    return new DateTime(new Date(), GMT);
  }


  /**
   * Date/time value expressed as the number of ms since the Unix epoch.
   *
   * If the time zone is specified, this value is normalized to UTC, so
   * to format this date/time value, the time zone shift has to be applied.
   */
  protected long value = 0;
  public long getValue() { return value; }
  public void setValue(long v) { value = v; }


  /** Specifies whether this is a date-only value. */
  protected boolean dateOnly = false;
  public boolean isDateOnly() { return dateOnly; }
  public void setDateOnly(boolean v) { dateOnly = v; }


  /**
   * Time zone shift from UTC in minutes. If {@code null}, no time zone
   * is set, and the time is always interpreted as local time.
   */
  protected Integer tzShift = null;
  public Integer getTzShift() { return tzShift; }
  public void setTzShift(Integer v) { tzShift = v; }


  @Override
  public int hashCode() {
    return Long.valueOf(value).hashCode();
  }

  /**
   * Compares instance with DateTime or Date objects.
   *
   * Does not take the tzShift value into account.
   * Therefore, two DateTime objects are equal independent to
   * the time zone they refer to.
   * Equals with a instance of java.util.Date is asymmetric.
   */
  @Override
  public boolean equals(Object o) {

    if (o instanceof DateTime) {

      return this.value == ((DateTime) o).value;

    } else if (o instanceof Date) {

      return this.value == ((Date) o).getTime();

    } else {

      return false;
    }
  }


  public int compareTo(Object o) {

    if (o instanceof DateTime) {
      return new Long(value).compareTo(new Long(((DateTime) o).value));
    } else if (o instanceof Date) {
      return new Long(value).compareTo(new Long(((Date) o).getTime()));
    } else {
      throw new RuntimeException("Invalid type.");
    }
  }


  /** XML date/time pattern. */
  public static final Pattern dateTimePattern =
    Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)[Tt]" +
                    "(\\d\\d):(\\d\\d):(\\d\\d)(\\.(\\d+))?" +
                    "([Zz]|((\\+|\\-)(\\d\\d):(\\d\\d)))?");


  /** XML date pattern. */
  public static final Pattern datePattern =
    Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)" +
                    "([Zz]|((\\+|\\-)(\\d\\d):(\\d\\d)))?");


  /** XML date/time or date pattern. */
  public static final Pattern dateTimeChoicePattern =
    Pattern.compile("(\\d\\d\\d\\d)\\-(\\d\\d)\\-(\\d\\d)" +
                    "([Tt](\\d\\d):(\\d\\d):(\\d\\d)(\\.(\\d+))?)?" +
                    "([Zz]|((\\+|\\-)(\\d\\d):(\\d\\d)))?");


  /** RFC 822 date/time format. */
  private static final SimpleDateFormat dateTimeFormat822 =
    new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

  private static final TimeZone GMT = TimeZone.getTimeZone("GMT");

  static {
    dateTimeFormat822.setTimeZone(GMT);
  }


  /** Formats the value as an xs:date or xs:dateTime string. */
  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();

    Calendar dateTime = new GregorianCalendar(GMT);
    long localTime = value;
    if (tzShift != null) {
     localTime += tzShift.longValue() * 60000;
    }

    dateTime.setTimeInMillis(localTime);

    try {

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
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new RuntimeException(e);
    }

    return sb.toString();
  }


  /** Formats the value as an RFC 822 date/time. */
  public String toStringRfc822() {
    assert !dateOnly;
    synchronized (dateTimeFormat822) {
      return dateTimeFormat822.format(value);
    }
  }

  /** Parses the value as an RFC 822 date/time. */
  public static DateTime parseRfc822(String str) throws ParseException {
    Date date;
    synchronized (dateTimeFormat822) {
      try {
        date = dateTimeFormat822.parse(str);
      } catch (java.text.ParseException e) {
        throw new ParseException(e);
      }
    }

    return new DateTime(date);
  }

  /** Formats the value as a human-readable string. */
  public String toUiString() {

    StringBuilder sb = new StringBuilder();

    Calendar dateTime = new GregorianCalendar(GMT);
    long localTime = value;
    if (tzShift != null) {
     localTime += tzShift.longValue() * 60000;
    }

    dateTime.setTimeInMillis(localTime);

    try {

      appendInt(sb, dateTime.get(Calendar.YEAR), 4);
      sb.append('-');
      appendInt(sb, dateTime.get(Calendar.MONTH) + 1, 2);
      sb.append('-');
      appendInt(sb, dateTime.get(Calendar.DAY_OF_MONTH), 2);

      if (!dateOnly) {

        sb.append(' ');
        appendInt(sb, dateTime.get(Calendar.HOUR_OF_DAY), 2);
        sb.append(':');
        appendInt(sb, dateTime.get(Calendar.MINUTE), 2);
      }

    } catch (ArrayIndexOutOfBoundsException e) {
      throw new RuntimeException(e);
    }

    return sb.toString();
  }


  /** Parses an xs:dateTime string. */
  public static DateTime parseDateTime(String str)
      throws NumberFormatException {

    Matcher m = str == null ? null : dateTimePattern.matcher(str);

    if (str == null || !m.matches()) {
      throw new NumberFormatException("Invalid date/time format.");
    }

    /* Debugging help:
    System.out.println("Year: " + m.group(1));
    System.out.println("Month: " + m.group(2));
    System.out.println("Day: " + m.group(3));
    System.out.println("Hour: " + m.group(4));
    System.out.println("Minute: " + m.group(5));
    System.out.println("Second: " + m.group(6));
    System.out.println("Second Fraction: " + m.group(8));
    System.out.println("TZ: " + m.group(9));
    System.out.println("TZ Shift: " + m.group(11));
    System.out.println("TZ Hour: " + m.group(12));
    System.out.println("TZ Minute: " + m.group(13));
    */

    DateTime ret = new DateTime();
    ret.dateOnly = false;

    if (m.group(9) == null) {
      // No time zone specified.
    } else if (m.group(9).equalsIgnoreCase("Z")) {
      ret.tzShift = new Integer(0);
    } else {
      ret.tzShift = new Integer((Integer.valueOf(m.group(12)) * 60 +
                                 Integer.valueOf(m.group(13))));
      if (m.group(11).equals("-")) {
        ret.tzShift = new Integer(-ret.tzShift.intValue());
      }
    }

    Calendar dateTime = new GregorianCalendar(GMT);

    dateTime.clear();
    dateTime.set(Integer.valueOf(m.group(1)),
                 Integer.valueOf(m.group(2)) - 1,
                 Integer.valueOf(m.group(3)),
                 Integer.valueOf(m.group(4)),
                 Integer.valueOf(m.group(5)),
                 Integer.valueOf(m.group(6)));
    if (m.group(8) != null && m.group(8).length() > 0) {
      final BigDecimal bd = new BigDecimal("0." + m.group(8));
      // we care only for milliseconds, so movePointRight(3)
      dateTime.set(Calendar.MILLISECOND, bd.movePointRight(3).intValue());
    }

    ret.value = dateTime.getTimeInMillis();
    if (ret.tzShift != null) {
      ret.value -= ret.tzShift.intValue() * 60000;
    }

    return ret;
  }

  /** Parses an xs:date string. */
  public static DateTime parseDate(String str)
      throws NumberFormatException {

    Matcher m = str == null ? null : datePattern.matcher(str);

    if (str == null || !m.matches()) {
      throw new NumberFormatException("Invalid date format.");
    }

    /* Debugging help:
    System.out.println("Year: " + m.group(1));
    System.out.println("Month: " + m.group(2));
    System.out.println("Day: " + m.group(3));
    System.out.println("TZ: " + m.group(4));
    System.out.println("TZ Shift: " + m.group(6));
    System.out.println("TZ Hour: " + m.group(7));
    System.out.println("TZ Minute: " + m.group(8));
    */

    DateTime ret = new DateTime();
    ret.dateOnly = true;

    if (m.group(4) == null) {
      // No time zone specified.
    } else if (m.group(4).equalsIgnoreCase("Z")) {
      ret.tzShift = new Integer(0);
    } else {
      ret.tzShift = new Integer((Integer.valueOf(m.group(7)) * 60 +
                                 Integer.valueOf(m.group(8))));
      if (m.group(6).equals("-")) {
        ret.tzShift = new Integer(-ret.tzShift.intValue());
      }
    }

    Calendar dateTime = new GregorianCalendar(GMT);

    dateTime.clear();
    dateTime.set(Integer.valueOf(m.group(1)),
                 Integer.valueOf(m.group(2)) - 1,
                 Integer.valueOf(m.group(3)));

    ret.value = dateTime.getTimeInMillis();
    if (ret.tzShift != null) {
      ret.value -= ret.tzShift.intValue() * 60000;
    }

    return ret;
  }

  /**
   * Parses an XML value that's either an xs:date or xs:dateTime string.
   *
   * @throws  NumberFormatException
   *            Invalid RFC 3339 date or date/time string.
   */
  public static DateTime parseDateTimeChoice(String value)
      throws NumberFormatException {

    NumberFormatException exception;

    try {
      return DateTime.parseDateTime(value);
    } catch (NumberFormatException e) {
      exception = e;
    }

    try {
      return DateTime.parseDate(value);
    } catch (NumberFormatException e) {
      exception = e;
    }

    throw exception;
  }


  /** Appends a zero-padded number to a string builder. */
  private static void appendInt(StringBuilder sb, int num, int numDigits) {

    if (num < 0) {
      sb.append('-');
      num = -num;
    }

    char[] digits = new char[numDigits];
    for (int digit = numDigits - 1; digit >= 0; --digit) {
      digits[digit] = (char)('0' + num % 10);
      num /= 10;
    }

    sb.append(digits);
  }
}
