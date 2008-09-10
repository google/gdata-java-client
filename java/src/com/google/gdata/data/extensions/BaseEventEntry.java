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


package com.google.gdata.data.extensions;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Customizes the base entry to define an event entry.
 *
 * @param <E> concrete event entry type
 */
public class BaseEventEntry<E extends BaseEventEntry<E>> extends BaseEntry<E> {

  /**
   * Constructs a new EventEntry with the appropriate kind category
   * to indicate that it is an event.
   */
  public BaseEventEntry() {
    super();
    getCategories().add(EventEntry.EVENT_CATEGORY);
  }

  /**
   * Constructs a new EventEntry instance by doing a shallow copy of data from
   * an existing BaseEntry instance.
   *
   * @param sourceEntry entry to copy
   */
  public BaseEventEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(EventEntry.EVENT_CATEGORY);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    @SuppressWarnings("unchecked")
    Class<? extends BaseEventEntry> eventEntryClass = getClass();
    extProfile.declare(eventEntryClass,
        RecurrenceException.getDefaultDescription());
    extProfile.declare(eventEntryClass, Reminder.getDefaultDescription());
    extProfile.declare(eventEntryClass, Recurrence.getDefaultDescription());
    extProfile.declare(eventEntryClass, Where.getDefaultDescription());
    extProfile.declare(eventEntryClass, EventStatus.getDefaultDescription());
    extProfile.declare(eventEntryClass, Visibility.getDefaultDescription());
    extProfile.declare(eventEntryClass, Transparency.getDefaultDescription());
    extProfile.declare(eventEntryClass, When.getDefaultDescription());
    extProfile.declare(eventEntryClass, OriginalEvent.getDefaultDescription());
    extProfile.declare(eventEntryClass, Comments.getDefaultDescription());
    extProfile.declare(When.class, Reminder.getDefaultDescription());
  }

  /**
   * Returns the list of event times.
   */
  public List<When> getTimes() {
    return getRepeatingExtension(When.class);
  }

  /**
   * Adds a new event time.
   */
  public void addTime(When time) {
    getTimes().add(time);
  }

  /**
   * Returns the list of event locations
   */
  public List<Where> getLocations() {
    return getRepeatingExtension(Where.class);
  }

  /**
   * Adds a new event location.
   */
  public void addLocation(Where location) {
    getLocations().add(location);
  }

  /**
   * Defines the base set of values for event status.
   */
  public static class EventStatus extends ValueConstruct {

    public EventStatus() {
      super(Namespaces.gNs, "eventStatus", "value");
    }

    public EventStatus(String value) {
      super(Namespaces.gNs, "eventStatus", "value", value);
    }

    public static final String CONFIRMED_VALUE =
        Namespaces.gPrefix + "event.confirmed";
    public static final String TENTATIVE_VALUE =
        Namespaces.gPrefix + "event.tentative";
    public static final String CANCELED_VALUE =
        Namespaces.gPrefix + "event.canceled";

    public static final EventStatus CONFIRMED =
        new EventStatus(CONFIRMED_VALUE);

    public static final EventStatus TENTATIVE =
        new EventStatus(TENTATIVE_VALUE);

    public static final EventStatus CANCELED =
        new EventStatus(CANCELED_VALUE);

    public static ExtensionDescription getDefaultDescription() {
      ExtensionDescription desc = new ExtensionDescription();
      desc.setExtensionClass(EventStatus.class);
      desc.setNamespace(Namespaces.gNs);
      desc.setLocalName("eventStatus");
      desc.setRepeatable(false);
      return desc;
    }
  }

  /**
   * Returns the event status.
   */
  public EventStatus getStatus() {
    return getExtension(EventStatus.class);
  }

  /**
   * Sets the event status.
   */
  public void setStatus(EventStatus status) {
    setExtension(status);
  }

  /**
   * Defines the base set of values for event visibility.
   * Similar to CLASS in RFC 2445.
   */
  public static class Visibility extends ValueConstruct {

    public Visibility() {
      super(Namespaces.gNs, "visibility", "value");
    }

    public Visibility(String value) {
      super(Namespaces.gNs, "visibility", "value", value);
    }

    public static final String DEFAULT_VALUE =
        Namespaces.gPrefix + "event.default";

    public static final String PUBLIC_VALUE =
        Namespaces.gPrefix + "event.public";

    public static final String CONFIDENTIAL_VALUE =
        Namespaces.gPrefix + "event.confidential";

    public static final String PRIVATE_VALUE =
        Namespaces.gPrefix + "event.private";

    public static final Visibility DEFAULT =
        new Visibility(DEFAULT_VALUE);

    public static final Visibility PUBLIC =
        new Visibility(PUBLIC_VALUE);

    public static final Visibility CONFIDENTIAL =
        new Visibility(CONFIDENTIAL_VALUE);

    public static final Visibility PRIVATE =
        new Visibility(PRIVATE_VALUE);

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(Visibility.class, Namespaces.gNs,
          "visibility");
    }
  }

  /**
   * Returns the event visibility.
   */
  public Visibility getVisibility() {
    return getExtension(Visibility.class);
  }

  /**
   * Sets the event visibility.
   */
  public void setVisibility(Visibility v) {
    setExtension(v);
  }

  /**
   * Defines the base set of values for event transparency.
   */
  public static class Transparency extends ValueConstruct {

    public Transparency() {
      super(Namespaces.gNs, "transparency", "value");
    }

    public Transparency(String value) {
      super(Namespaces.gNs, "transparency", "value", value);
    }

    public static final String OPAQUE_VALUE =
        Namespaces.gPrefix + "event.opaque";
    public static final String TRANSPARENT_VALUE =
        Namespaces.gPrefix + "event.transparent";


    public static final Transparency OPAQUE = new Transparency(OPAQUE_VALUE);
    public static final Transparency TRANSPARENT =
        new Transparency(TRANSPARENT_VALUE);

    public static ExtensionDescription getDefaultDescription() {
      return new ExtensionDescription(Transparency.class, Namespaces.gNs,
          "transparency");
    }
  }

  /**
   * Returns the event transparency.
   */
  public Transparency getTransparency() {
    return getExtension(Transparency.class);
  }

  /**
   * Sets the event transparency.
   */
  public void setTransparency(Transparency transparency) {
    setExtension(transparency);
  }

  /**
   * Returns the event recurrence.
   */
  public Recurrence getRecurrence() {
    return getExtension(Recurrence.class);
  }

  /**
   * Sets the event recurrence.
   */
  public void setRecurrence(Recurrence v) {
    setExtension(v);
  }

  /**
   * Returns the list of recurrence exceptions.
   */
  public List<RecurrenceException> getRecurrenceException() {
    return getRepeatingExtension(RecurrenceException.class);
  }

  /**
   * Adds a new recurrence exception.
   */
  public void addRecurrenceException(RecurrenceException exception) {
    getRecurrenceException().add(exception);
  }

  /**
   * Returns the event original start time.
   */
  public OriginalEvent getOriginalEvent() {
    return getExtension(OriginalEvent.class);
  }

  /**
   * Sets the event original start time.
   */
  public void setOriginalEvent(OriginalEvent v) {
    setExtension(v);
  }

  /**
   * Returns event reminders.
   */
  public List<Reminder> getReminder() {
    if (getRecurrence() != null) {
      // recurrence event, g:reminder is in top level
      return getRepeatingExtension(Reminder.class);
    } else {
      // single event, g:reminder is inside g:when
      List<When> whenList = getTimes();
      if (whenList.size() >= 1) {
        return whenList.get(0).getRepeatingExtension(Reminder.class);
      }
      return null;
    }
  }
}
