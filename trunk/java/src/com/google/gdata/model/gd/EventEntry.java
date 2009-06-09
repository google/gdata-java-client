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

import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.atom.Author;
import com.google.gdata.model.atom.Category;
import com.google.gdata.model.atom.Entry;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes an event entry.
 *
 * 
 */
public class EventEntry extends Entry {

  /**
   * Event kind term value.
   */
  public static final String KIND = Namespaces.gPrefix + "event";

  /**
   * Event kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind,
      KIND).lock();

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<Void,
      EventEntry> KEY = ElementKey.of(Entry.KEY.getId(), Void.class,
      EventEntry.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.replaceElement(Author.KEY).setRequired(true);
    builder.replaceElement(Category.KEY).setRequired(true);
    builder.addElement(Comments.KEY);
    builder.addElement(EventStatus.KEY);
    builder.addElement(Where.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
    builder.addElement(OriginalEvent.KEY);
    builder.addElement(Who.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
    builder.addElement(Recurrence.KEY);
    builder.addElement(RecurrenceException.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
    builder.addElement(Reminder.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
    builder.addElement(When.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
    builder.addElement(Transparency.KEY);
    builder.addElement(Visibility.KEY);
    DefaultRegistry.adapt(Entry.KEY, KIND, KEY);
  }

  /**
   * Default mutable constructor.
   */
  public EventEntry() {
    this(KEY);
  }

  /**
   * Create an instance using a different key.
   */
  public EventEntry(ElementKey<Void, ? extends EventEntry> key) {
    super(key);
    addCategory(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Entry} instance. Will use the given {@link ElementKey} as the key
   * for the element.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  public EventEntry(ElementKey<Void, ? extends EventEntry> key, Entry source) {
    super(key, source);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Entry} instance.
   *
   * @param sourceEntry source entry
   */
  public EventEntry(Entry sourceEntry) {
    super(KEY, sourceEntry);
  }

   @Override
   public EventEntry lock() {
     return (EventEntry) super.lock();
   }

  /**
   * Returns the nested comments feed.
   *
   * @return nested comments feed
   */
  public Comments getComments() {
    return super.getElement(Comments.KEY);
  }

  /**
   * Sets the nested comments feed.
   *
   * @param comments nested comments feed or <code>null</code> to reset
   */
  public EventEntry setComments(Comments comments) {
    super.setElement(Comments.KEY, comments);
    return this;
  }

  /**
   * Returns whether it has the nested comments feed.
   *
   * @return whether it has the nested comments feed
   */
  public boolean hasComments() {
    return super.hasElement(Comments.KEY);
  }

  /**
   * Returns the event status.
   *
   * @return event status
   */
  public EventStatus getEventStatus() {
    return super.getElement(EventStatus.KEY);
  }

  /**
   * Sets the event status.
   *
   * @param eventStatus event status or <code>null</code> to reset
   */
  public EventEntry setEventStatus(EventStatus eventStatus) {
    super.setElement(EventStatus.KEY, eventStatus);
    return this;
  }

  /**
   * Returns whether it has the event status.
   *
   * @return whether it has the event status
   */
  public boolean hasEventStatus() {
    return super.hasElement(EventStatus.KEY);
  }

  /**
   * Returns the event locations.
   *
   * @return event locations
   */
  public List<Where> getLocations() {
    return super.getElements(Where.KEY);
  }

  /**
   * Adds a new event location.
   *
   * @param location event location
   */
  public EventEntry addLocation(Where location) {
    super.addElement(Where.KEY, location);
    return this;
  }

  /**
   * Removes an existing event location.
   *
   * @param location event location
   * @return true if the location was removed
   */
  public boolean removeLocation(Where location) {
    return super.removeElement(Where.KEY, location);
  }

  /**
   * Returns whether it has the event locations.
   *
   * @return whether it has the event locations
   */
  public boolean hasLocations() {
    return super.hasElement(Where.KEY);
  }

  /**
   * Returns the event original start time.
   *
   * @return event original start time
   */
  public OriginalEvent getOriginalEvent() {
    return super.getElement(OriginalEvent.KEY);
  }

  /**
   * Sets the event original start time.
   *
   * @param originalEvent event original start time or <code>null</code> to
   *     reset
   */
  public EventEntry setOriginalEvent(OriginalEvent originalEvent) {
    super.setElement(OriginalEvent.KEY, originalEvent);
    return this;
  }

  /**
   * Returns whether it has the event original start time.
   *
   * @return whether it has the event original start time
   */
  public boolean hasOriginalEvent() {
    return super.hasElement(OriginalEvent.KEY);
  }

  /**
   * Returns the event participants.
   *
   * @return event participants
   */
  public List<Who> getParticipants() {
    return super.getElements(Who.KEY);
  }

  /**
   * Adds a new event participant.
   *
   * @param participant event participant
   */
  public EventEntry addParticipant(Who participant) {
    super.addElement(Who.KEY, participant);
    return this;
  }

  /**
   * Removes an existing event participant.
   *
   * @param participant event participant
   * @return true if the participant was removed
   */
  public boolean removeParticipant(Who participant) {
    return super.removeElement(Who.KEY, participant);
  }

  /**
   * Returns whether it has the event participants.
   *
   * @return whether it has the event participants
   */
  public boolean hasParticipants() {
    return super.hasElement(Who.KEY);
  }

  /**
   * Returns the event recurrence.
   *
   * @return event recurrence
   */
  public Recurrence getRecurrence() {
    return super.getElement(Recurrence.KEY);
  }

  /**
   * Sets the event recurrence.
   *
   * @param recurrence event recurrence or <code>null</code> to reset
   */
  public EventEntry setRecurrence(Recurrence recurrence) {
    super.setElement(Recurrence.KEY, recurrence);
    return this;
  }

  /**
   * Returns whether it has the event recurrence.
   *
   * @return whether it has the event recurrence
   */
  public boolean hasRecurrence() {
    return super.hasElement(Recurrence.KEY);
  }

  /**
   * Returns the recurrence exceptions.
   *
   * @return recurrence exceptions
   */
  public List<RecurrenceException> getRecurrenceException() {
    return super.getElements(RecurrenceException.KEY);
  }

  /**
   * Adds a new recurrence exception.
   *
   * @param recurrenceException recurrence exception
   */
  public EventEntry addRecurrenceException(RecurrenceException
      recurrenceException) {
    super.addElement(RecurrenceException.KEY, recurrenceException);
    return this;
  }

  /**
   * Removes an existing recurrence exception.
   *
   * @param recurrenceException recurrence exception
   * @return true if the recurrenceException was removed
   */
  public boolean removeRecurrenceException(RecurrenceException
      recurrenceException) {
    return super.removeElement(RecurrenceException.KEY, recurrenceException);
  }

  /**
   * Returns whether it has the recurrence exceptions.
   *
   * @return whether it has the recurrence exceptions
   */
  public boolean hasRecurrenceException() {
    return super.hasElement(RecurrenceException.KEY);
  }

  /**
   * Returns the event reminders.
   *
   * @return event reminders
   */
  public List<Reminder> getReminders() {
    return super.getElements(Reminder.KEY);
  }

  /**
   * Adds a new event reminder.
   *
   * @param reminder event reminder
   */
  public EventEntry addReminder(Reminder reminder) {
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
    return super.removeElement(Reminder.KEY, reminder);
  }

  /**
   * Returns whether it has the event reminders.
   *
   * @return whether it has the event reminders
   */
  public boolean hasReminders() {
    return super.hasElement(Reminder.KEY);
  }

  /**
   * Returns the event times.
   *
   * @return event times
   */
  public List<When> getTimes() {
    return super.getElements(When.KEY);
  }

  /**
   * Adds a new event time.
   *
   * @param time event time
   */
  public EventEntry addTime(When time) {
    super.addElement(When.KEY, time);
    return this;
  }

  /**
   * Removes an existing event time.
   *
   * @param time event time
   * @return true if the time was removed
   */
  public boolean removeTime(When time) {
    return super.removeElement(When.KEY, time);
  }

  /**
   * Returns whether it has the event times.
   *
   * @return whether it has the event times
   */
  public boolean hasTimes() {
    return super.hasElement(When.KEY);
  }

  /**
   * Returns the event transparency.
   *
   * @return event transparency
   */
  public Transparency getTransparency() {
    return super.getElement(Transparency.KEY);
  }

  /**
   * Sets the event transparency.
   *
   * @param transparency event transparency or <code>null</code> to reset
   */
  public EventEntry setTransparency(Transparency transparency) {
    super.setElement(Transparency.KEY, transparency);
    return this;
  }

  /**
   * Returns whether it has the event transparency.
   *
   * @return whether it has the event transparency
   */
  public boolean hasTransparency() {
    return super.hasElement(Transparency.KEY);
  }

  /**
   * Returns the event visibility.
   *
   * @return event visibility
   */
  public Visibility getVisibility() {
    return super.getElement(Visibility.KEY);
  }

  /**
   * Sets the event visibility.
   *
   * @param visibility event visibility or <code>null</code> to reset
   */
  public EventEntry setVisibility(Visibility visibility) {
    super.setElement(Visibility.KEY, visibility);
    return this;
  }

  /**
   * Returns whether it has the event visibility.
   *
   * @return whether it has the event visibility
   */
  public boolean hasVisibility() {
    return super.hasElement(Visibility.KEY);
  }

  @Override
  public String toString() {
    return "{EventEntry " + super.toString() + "}";
  }

}
