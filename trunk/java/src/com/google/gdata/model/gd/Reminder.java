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


package com.google.gdata.model.gd;

import com.google.gdata.data.DateTime;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes a reminder on an event.
 *
 * 
 */
public class Reminder extends Element {

  /** Method. */
  public static final class Method {

    /** Causes an alert to appear when a user is viewing the calendar in a
     * browser. */
    public static final String ALERT = "alert";

    /** All reminder methods. */
    public static final String ALL = "all";

    /** Sends the user an email message. */
    public static final String EMAIL = "email";

    /** No reminder methods. */
    public static final String NONE = "none";

    /** Sends the user an SMS text message. */
    public static final String SMS = "sms";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      ALERT,
      ALL,
      EMAIL,
      NONE,
      SMS};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Method() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Reminder> KEY = ElementKey.of(new QName(Namespaces.gNs, "reminder"),
      Void.class, Reminder.class);

  /**
   * Absolute time of the reminder.
   */
  public static final AttributeKey<DateTime> ABSOLUTE_TIME = AttributeKey.of(new
      QName(null, "absoluteTime"), DateTime.class);

  /**
   * Number of days before the start time.
   */
  public static final AttributeKey<Integer> DAYS = AttributeKey.of(new
      QName(null, "days"), Integer.class);

  /**
   * Number of hours before the start time.
   */
  public static final AttributeKey<Integer> HOURS = AttributeKey.of(new
      QName(null, "hours"), Integer.class);

  /**
   * Method.
   */
  public static final AttributeKey<String> METHOD = AttributeKey.of(new
      QName(null, "method"), String.class);

  /**
   * Number of minute before the start times.
   */
  public static final AttributeKey<Integer> MINUTES = AttributeKey.of(new
      QName(null, "minutes"), Integer.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addAttribute(ABSOLUTE_TIME);
    builder.addAttribute(DAYS);
    builder.addAttribute(HOURS);
    builder.addAttribute(METHOD);
    builder.addAttribute(MINUTES);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Reminder() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Reminder(ElementKey<?, ? extends Reminder> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected Reminder(ElementKey<?, ? extends Reminder> key, Element source) {
    super(key, source);
  }

  @Override
  public Reminder lock() {
    return (Reminder) super.lock();
  }

  /**
   * Returns the absolute time of the reminder.
   *
   * @return absolute time of the reminder
   */
  public DateTime getAbsoluteTime() {
    return super.getAttributeValue(ABSOLUTE_TIME);
  }

  /**
   * Sets the absolute time of the reminder.
   *
   * @param absoluteTime absolute time of the reminder or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Reminder setAbsoluteTime(DateTime absoluteTime) {
    super.setAttributeValue(ABSOLUTE_TIME, absoluteTime);
    return this;
  }

  /**
   * Returns whether it has the absolute time of the reminder.
   *
   * @return whether it has the absolute time of the reminder
   */
  public boolean hasAbsoluteTime() {
    return super.hasAttribute(ABSOLUTE_TIME);
  }

  /**
   * Returns the number of days before the start time.
   *
   * @return number of days before the start time
   */
  public Integer getDays() {
    return super.getAttributeValue(DAYS);
  }

  /**
   * Sets the number of days before the start time.
   *
   * @param days number of days before the start time or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Reminder setDays(Integer days) {
    super.setAttributeValue(DAYS, days);
    return this;
  }

  /**
   * Returns whether it has the number of days before the start time.
   *
   * @return whether it has the number of days before the start time
   */
  public boolean hasDays() {
    return super.hasAttribute(DAYS);
  }

  /**
   * Returns the number of hours before the start time.
   *
   * @return number of hours before the start time
   */
  public Integer getHours() {
    return super.getAttributeValue(HOURS);
  }

  /**
   * Sets the number of hours before the start time.
   *
   * @param hours number of hours before the start time or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Reminder setHours(Integer hours) {
    super.setAttributeValue(HOURS, hours);
    return this;
  }

  /**
   * Returns whether it has the number of hours before the start time.
   *
   * @return whether it has the number of hours before the start time
   */
  public boolean hasHours() {
    return super.hasAttribute(HOURS);
  }

  /**
   * Returns the method.
   *
   * @return method
   */
  public String getMethod() {
    return super.getAttributeValue(METHOD);
  }

  /**
   * Sets the method.
   *
   * @param method method or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Reminder setMethod(String method) {
    super.setAttributeValue(METHOD, method);
    return this;
  }

  /**
   * Returns whether it has the method.
   *
   * @return whether it has the method
   */
  public boolean hasMethod() {
    return super.hasAttribute(METHOD);
  }

  /**
   * Returns the number of minute before the start times.
   *
   * @return number of minute before the start times
   */
  public Integer getMinutes() {
    return super.getAttributeValue(MINUTES);
  }

  /**
   * Sets the number of minute before the start times.
   *
   * @param minutes number of minute before the start times or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public Reminder setMinutes(Integer minutes) {
    super.setAttributeValue(MINUTES, minutes);
    return this;
  }

  /**
   * Returns whether it has the number of minute before the start times.
   *
   * @return whether it has the number of minute before the start times
   */
  public boolean hasMinutes() {
    return super.hasAttribute(MINUTES);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Reminder other = (Reminder) obj;
    return eq(getAbsoluteTime(), other.getAbsoluteTime())
        && eq(getDays(), other.getDays())
        && eq(getHours(), other.getHours())
        && eq(getMethod(), other.getMethod())
        && eq(getMinutes(), other.getMinutes());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getAbsoluteTime() != null) {
      result = 37 * result + getAbsoluteTime().hashCode();
    }
    if (getDays() != null) {
      result = 37 * result + getDays().hashCode();
    }
    if (getHours() != null) {
      result = 37 * result + getHours().hashCode();
    }
    if (getMethod() != null) {
      result = 37 * result + getMethod().hashCode();
    }
    if (getMinutes() != null) {
      result = 37 * result + getMinutes().hashCode();
    }
    return result;
  }
}


