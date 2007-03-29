/* Copyright (c) 2006 Google Inc.
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

package com.google.api.gbase.client;

import com.google.gdata.data.DateTime;

/**
 * A date or date/time range, with a start and end {@link DateTime}.
 * 
 * Since <code>dateTimeRange</code> is a superclass of <code>date</code>
 * and <code>dateTime</code>, a DateTimeRange object can sometimes be 
 * used to represent a single date or dateTime. When this is the case,
 * the start and end value will be identical and {@link #isDateTimeOnly()} 
 * will return true. You can then get the {@link DateTime} the 'range'
 * corresponds to using {@link #toDateTime()}.
 */
public class DateTimeRange {
  /**
   * The start date/time of this range.
   */
  private final DateTime start;
  
  /**
   * The end date/time of this range.
   */
  private final DateTime end;
  
  /**
   * Creates a new range.
   *
   * @param start
   * @param end
   * @exception IllegalArgumentException if either start or end is null
   */
  public DateTimeRange(DateTime start, DateTime end) {
    assertArgumentNotNull(start, "start");
    assertArgumentNotNull(end, "end");
    this.start = start;
    this.end = end;
  }

  /**
   * Creates a new range that is actually a date or a dateTime. 
   * 
   * Empty ranges (with start date the same as the end dates) can
   * be detected using {@link #isDateTimeOnly()} and then converted
   * using {@link #toDateTime()}.
   * 
   * @param dateTime
   */
  public DateTimeRange(DateTime dateTime) {
    this(dateTime, dateTime);
  }

  /**
   * Checks whether this is a real range, with a start and an
   * end date, or a date/dateTime posing as a range.
   * 
   * This hack is necessary to handle treating DateTime as
   * a DateTimeRange, because dateTimeRange is a supertype of 
   * date and dateTime. 
   * 
   * If this method returns true, you can get the {@link DateTime} 
   * object this object corresponds to using {@link #toDateTime()}
   *  
   * @return true if start is the same as end
   */
  public boolean isDateTimeOnly() {
    return start.equals(end);
  }

  /**
   * Converts empty ranges into {@link DateTime}.
   * 
   * @return a DateTime object 
   * @exception IllegalStateException if the start and end date are
   *   distinct, which means the dateTimeRange is neither a date
   *   nor a dateTime (see {@link #isDateTimeOnly()})
   */
  public DateTime toDateTime() {
    if (!isDateTimeOnly()) {
      throw new IllegalStateException("This is a valid range, with distinct " +
          "start and end date. It cannot be converted to one DateTime value. " +
          "(Check with isDateTimeOnly() first): " + this);
    }
    return start;
  }

  private void assertArgumentNotNull(Object object, String name) {
    if (object == null) {
      throw new NullPointerException(name + " should not be null");
    }
  }

  /** 
   * Gets the start date or date+time. 
   */
  public DateTime getStart() {
    return start;
  }

  /**
   * Gets the end date or date/time.
   */
  public DateTime getEnd() {
    return end;
  }

  /**
   * Returns the canonical string representation for a range, as used
   * in the XML (start " " end).
   */
  @Override
  public String toString() {
    return start + " " + end;
  }
  
  @Override
  public int hashCode() {
    return 37 * start.hashCode() + end.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof DateTimeRange)) {
      return false;
    } else {
      DateTimeRange other = (DateTimeRange)o;
      return other.start.equals(start) && other.end.equals(end);
    }
  }
}
