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

import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.atom.Entry;

import java.util.List;

/**
 * Describes an entry used by a recurrence exception entry link.
 *
 * 
 */
public class RecurrenceExceptionEntry extends Entry {

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<Void,
      RecurrenceExceptionEntry> KEY = ElementKey.of(Entry.KEY.getId(),
      Void.class, RecurrenceExceptionEntry.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // Register superclass metadata.
    Entry.registerMetadata(registry);

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addElement(Comments.KEY);
    builder.addElement(EventStatus.KEY);
    builder.addElement(OriginalEvent.KEY);
    builder.addElement(Transparency.KEY);
    builder.addElement(Visibility.KEY);
    builder.addElement(When.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
    builder.addElement(Where.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
    builder.addElement(Who.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);
  }

  /**
   * Constructs an instance using the default key.
   */
  public RecurrenceExceptionEntry() {
    super(KEY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Entry} instance.
   *
   * @param sourceEntry source entry
   */
  public RecurrenceExceptionEntry(Entry sourceEntry) {
    super(KEY, sourceEntry);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected RecurrenceExceptionEntry(ElementKey<?,
      ? extends RecurrenceExceptionEntry> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Entry} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected RecurrenceExceptionEntry(ElementKey<?,
      ? extends RecurrenceExceptionEntry> key, Entry source) {
    super(key, source);
  }

  @Override
  public RecurrenceExceptionEntry lock() {
    return (RecurrenceExceptionEntry) super.lock();
  }

  /**
   * Returns the Comments class.
   *
   * @return Comments class
   */
  public Comments getComments() {
    return super.getElement(Comments.KEY);
  }

  /**
   * Sets the Comments class.
   *
   * @param comments Comments class or {@code null} to reset
   * @return this to enable chaining setters
   */
  public RecurrenceExceptionEntry setComments(Comments comments) {
    super.setElement(Comments.KEY, comments);
    return this;
  }

  /**
   * Returns whether it has the Comments class.
   *
   * @return whether it has the Comments class
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
   * @param eventStatus event status or {@code null} to reset
   * @return this to enable chaining setters
   */
  public RecurrenceExceptionEntry setEventStatus(EventStatus eventStatus) {
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
   * Returns the original event.
   *
   * @return original event
   */
  public OriginalEvent getOriginalEvent() {
    return super.getElement(OriginalEvent.KEY);
  }

  /**
   * Sets the original event.
   *
   * @param originalEvent original event or {@code null} to reset
   * @return this to enable chaining setters
   */
  public RecurrenceExceptionEntry setOriginalEvent(OriginalEvent originalEvent)
      {
    super.setElement(OriginalEvent.KEY, originalEvent);
    return this;
  }

  /**
   * Returns whether it has the original event.
   *
   * @return whether it has the original event
   */
  public boolean hasOriginalEvent() {
    return super.hasElement(OriginalEvent.KEY);
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
   * @param transparency event transparency or {@code null} to reset
   * @return this to enable chaining setters
   */
  public RecurrenceExceptionEntry setTransparency(Transparency transparency) {
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
   * @param visibility event visibility or {@code null} to reset
   * @return this to enable chaining setters
   */
  public RecurrenceExceptionEntry setVisibility(Visibility visibility) {
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

  /**
   * Returns the time period descriptions.
   *
   * @return time period descriptions
   */
  public List<When> getWhen() {
    return super.getElements(When.KEY);
  }

  /**
   * Adds a new time period description.
   *
   * @param when time period description
   */
  public RecurrenceExceptionEntry addWhen(When when) {
    super.addElement(When.KEY, when);
    return this;
  }

  /**
   * Removes an existing time period description.
   *
   * @param when time period description
   * @return true if the when was removed
   */
  public boolean removeWhen(When when) {
    return super.removeElement(when);
  }

  /**
   * Removes all existing time period description instances.
   */
  public void clearWhen() {
    super.removeElement(When.KEY);
  }

  /**
   * Returns whether it has the time period descriptions.
   *
   * @return whether it has the time period descriptions
   */
  public boolean hasWhen() {
    return super.hasElement(When.KEY);
  }

  /**
   * Returns the place descriptions.
   *
   * @return place descriptions
   */
  public List<Where> getWhere() {
    return super.getElements(Where.KEY);
  }

  /**
   * Adds a new place description.
   *
   * @param where place description
   */
  public RecurrenceExceptionEntry addWhere(Where where) {
    super.addElement(Where.KEY, where);
    return this;
  }

  /**
   * Removes an existing place description.
   *
   * @param where place description
   * @return true if the where was removed
   */
  public boolean removeWhere(Where where) {
    return super.removeElement(where);
  }

  /**
   * Removes all existing place description instances.
   */
  public void clearWhere() {
    super.removeElement(Where.KEY);
  }

  /**
   * Returns whether it has the place descriptions.
   *
   * @return whether it has the place descriptions
   */
  public boolean hasWhere() {
    return super.hasElement(Where.KEY);
  }

  /**
   * Returns the person descriptions.
   *
   * @return person descriptions
   */
  public List<Who> getWho() {
    return super.getElements(Who.KEY);
  }

  /**
   * Adds a new person description.
   *
   * @param who person description
   */
  public RecurrenceExceptionEntry addWho(Who who) {
    super.addElement(Who.KEY, who);
    return this;
  }

  /**
   * Removes an existing person description.
   *
   * @param who person description
   * @return true if the who was removed
   */
  public boolean removeWho(Who who) {
    return super.removeElement(who);
  }

  /**
   * Removes all existing person description instances.
   */
  public void clearWho() {
    super.removeElement(Who.KEY);
  }

  /**
   * Returns whether it has the person descriptions.
   *
   * @return whether it has the person descriptions
   */
  public boolean hasWho() {
    return super.hasElement(Who.KEY);
  }

}


