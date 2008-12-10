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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.ValueConstruct;

/**
 * GData schema extension describing a node with a {@code DateTime} value. The
 * class is abstract, derive from this class and define default constructor
 * which hardcodes the node name.
 *
 * 
 */
public abstract class DateTimeValueConstruct extends ValueConstruct {

  /** Variable to cache the date/time value. */
  private DateTime dateTimeValue;

  /** Constructs {@link ValueConstruct} to represent DateTime value. */
  public DateTimeValueConstruct(String nodeName) {
    super(Namespaces.WT_NAMESPACE, nodeName, null);
    this.dateTimeValue = null;
  }

  /**
   * Compares {@link DateTimeValueConstruct} objects based on the date/time
   * value that they hold.
   */
  @Override
  public boolean equals(Object rhs) {
    if (!super.equals(rhs)) {
      return false;
    }

    DateTimeValueConstruct r = (DateTimeValueConstruct) rhs;
    if (dateTimeValue == null) {
      return r.dateTimeValue == null;
    }

    return dateTimeValue.equals(r.dateTimeValue);
  }

  /**
   * Returns hash code that is based on the date/time value that the object
   * holds.
   */
  @Override
  public int hashCode() {
    if (dateTimeValue == null) {
      return 0;
    }

    return dateTimeValue.hashCode();
  }

  /**
   * Override {@link ValueConstruct#setValue(String)} to validate that
   * supplied value is valid date.
   *
   * @throws NullPointerException if argument is null.
   * @throws IllegalArgumentException if argument is not a valid date.
   */
  @Override
  public void setValue(String value) {
    if (value == null) {
      throw new NullPointerException("value must not be null");
    }

    try {
      DateTime parsedValue = DateTime.parseDateTime(value);
      setDateTime(parsedValue);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid date", e);
    }
  }

  /**
   * Set date/time value.
   * 
   * @throws NullPointerException if argument is null.
   */
  public void setDateTime(DateTime value) {
    if (value == null) {
      throw new NullPointerException("value must not be null");
    }
    dateTimeValue = value;
    super.setValue(value.toString());
  }

  /** Get date/time value. */
  public DateTime getDateTime() {
    return dateTimeValue;
  }
}
