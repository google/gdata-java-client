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
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes a period of time.
 *
 * 
 */
public class When extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      When> KEY = ElementKey.of(new QName(Namespaces.gNs, "when"), Void.class,
      When.class);

  /**
   * Event end time.
   */
  public static final AttributeKey<DateTime> END_TIME = AttributeKey.of(new
      QName(null, "endTime"), DateTime.class);

  /**
   * Event start time.
   */
  public static final AttributeKey<DateTime> START_TIME = AttributeKey.of(new
      QName(null, "startTime"), DateTime.class);

  /**
   * String description of the event times.
   */
  public static final AttributeKey<String> VALUE_STRING = AttributeKey.of(new
      QName(null, "valueString"), String.class);

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
    builder.addAttribute(END_TIME);
    builder.addAttribute(START_TIME).setRequired(true);
    builder.addAttribute(VALUE_STRING);
    builder.addElement(Reminder.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
  }

  /**
   * Constructs an instance using the default key.
   */
  public When() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected When(ElementKey<?, ? extends When> key) {
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
  protected When(ElementKey<?, ? extends When> key, Element source) {
    super(key, source);
  }

  @Override
  public When lock() {
    return (When) super.lock();
  }

  /**
   * Returns the event end time.
   *
   * @return event end time
   */
  public DateTime getEndTime() {
    return super.getAttributeValue(END_TIME);
  }

  /**
   * Sets the event end time.
   *
   * @param endTime event end time or {@code null} to reset
   * @return this to enable chaining setters
   */
  public When setEndTime(DateTime endTime) {
    super.setAttributeValue(END_TIME, endTime);
    return this;
  }

  /**
   * Returns whether it has the event end time.
   *
   * @return whether it has the event end time
   */
  public boolean hasEndTime() {
    return super.hasAttribute(END_TIME);
  }

  /**
   * Returns the event reminders.
   *
   * @return event reminders
   */
  public List<Reminder> getReminder() {
    return super.getElements(Reminder.KEY);
  }

  /**
   * Adds a new event reminder.
   *
   * @param reminder event reminder
   */
  public When addReminder(Reminder reminder) {
    super.addElement(Reminder.KEY, reminder);
    return this;
  }

  /**
   * Removes an existing event reminder.
   *
   * @param reminder event reminder
   * @return true if the reminder was removed
   */
  public boolean removeReminder(Reminder reminder) {
    return super.removeElement(reminder);
  }

  /**
   * Removes all existing event reminder instances.
   */
  public void clearReminder() {
    super.removeElement(Reminder.KEY);
  }

  /**
   * Returns whether it has the event reminders.
   *
   * @return whether it has the event reminders
   */
  public boolean hasReminder() {
    return super.hasElement(Reminder.KEY);
  }

  /**
   * Returns the event start time.
   *
   * @return event start time
   */
  public DateTime getStartTime() {
    return super.getAttributeValue(START_TIME);
  }

  /**
   * Sets the event start time.
   *
   * @param startTime event start time or {@code null} to reset
   * @return this to enable chaining setters
   */
  public When setStartTime(DateTime startTime) {
    super.setAttributeValue(START_TIME, startTime);
    return this;
  }

  /**
   * Returns whether it has the event start time.
   *
   * @return whether it has the event start time
   */
  public boolean hasStartTime() {
    return super.hasAttribute(START_TIME);
  }

  /**
   * Returns the string description of the event times.
   *
   * @return string description of the event times
   */
  public String getValueString() {
    return super.getAttributeValue(VALUE_STRING);
  }

  /**
   * Sets the string description of the event times.
   *
   * @param valueString string description of the event times or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public When setValueString(String valueString) {
    super.setAttributeValue(VALUE_STRING, valueString);
    return this;
  }

  /**
   * Returns whether it has the string description of the event times.
   *
   * @return whether it has the string description of the event times
   */
  public boolean hasValueString() {
    return super.hasAttribute(VALUE_STRING);
  }

}


