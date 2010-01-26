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

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes a person associated with the containing entity.
 *
 * 
 */
public class Who extends Element {

  /** Relationship between the containing entity and the contained person. */
  public static final class Rel {

    /** A general meeting/event attendee. */
    public static final String EVENT_ATTENDEE = Namespaces.gPrefix +
        "event.attendee";

    /** Event organizer (an organizer is not necessarily an attendee). */
    public static final String EVENT_ORGANIZER = Namespaces.gPrefix +
        "event.organizer";

    /** Performer (similar to speaker, but with more emphasis on art than speech
     * delivery). */
    public static final String EVENT_PERFORMER = Namespaces.gPrefix +
        "event.performer";

    /** Speaker. */
    public static final String EVENT_SPEAKER = Namespaces.gPrefix +
        "event.speaker";

    /** Message BCC recipient. */
    public static final String MESSAGE_BCC = Namespaces.gPrefix + "message.bcc";

    /** Message CC recipient. */
    public static final String MESSAGE_CC = Namespaces.gPrefix + "message.cc";

    /** Message primary recipient. */
    public static final String MESSAGE_FROM = Namespaces.gPrefix +
        "message.from";

    /** Intended recipient of a reply message. */
    public static final String MESSAGE_REPLY_TO = Namespaces.gPrefix +
        "message.reply-to";

    /** Message (email or IM) sender. */
    public static final String MESSAGE_TO = Namespaces.gPrefix + "message.to";

    /** Person to whom task is assigned. */
    public static final String TASK_ASSIGNED_TO = Namespaces.gPrefix +
        "task.assigned-to";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      EVENT_ATTENDEE,
      EVENT_ORGANIZER,
      EVENT_PERFORMER,
      EVENT_SPEAKER,
      MESSAGE_BCC,
      MESSAGE_CC,
      MESSAGE_FROM,
      MESSAGE_REPLY_TO,
      MESSAGE_TO,
      TASK_ASSIGNED_TO};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Rel() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Who> KEY = ElementKey.of(new QName(Namespaces.gNs, "who"), Void.class,
      Who.class);

  /**
   * Email address.
   */
  public static final AttributeKey<String> EMAIL = AttributeKey.of(new
      QName(null, "email"), String.class);

  /**
   * Relationship between the containing entity and the contained person.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * Simple string value that can be used as a representation of this person.
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
    builder.addAttribute(EMAIL);
    builder.addAttribute(REL);
    builder.addAttribute(VALUE_STRING);
    builder.addElement(AttendeeStatus.KEY);
    builder.addElement(AttendeeType.KEY);
    builder.addElement(EntryLink.KEY);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Who() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Who(ElementKey<?, ? extends Who> key) {
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
  protected Who(ElementKey<?, ? extends Who> key, Element source) {
    super(key, source);
  }

  @Override
  public Who lock() {
    return (Who) super.lock();
  }

  /**
   * Returns the event attendee status.
   *
   * @return event attendee status
   */
  public AttendeeStatus getAttendeeStatus() {
    return super.getElement(AttendeeStatus.KEY);
  }

  /**
   * Sets the event attendee status.
   *
   * @param attendeeStatus event attendee status or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Who setAttendeeStatus(AttendeeStatus attendeeStatus) {
    super.setElement(AttendeeStatus.KEY, attendeeStatus);
    return this;
  }

  /**
   * Returns whether it has the event attendee status.
   *
   * @return whether it has the event attendee status
   */
  public boolean hasAttendeeStatus() {
    return super.hasElement(AttendeeStatus.KEY);
  }

  /**
   * Returns the event attendee type.
   *
   * @return event attendee type
   */
  public AttendeeType getAttendeeType() {
    return super.getElement(AttendeeType.KEY);
  }

  /**
   * Sets the event attendee type.
   *
   * @param attendeeType event attendee type or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Who setAttendeeType(AttendeeType attendeeType) {
    super.setElement(AttendeeType.KEY, attendeeType);
    return this;
  }

  /**
   * Returns whether it has the event attendee type.
   *
   * @return whether it has the event attendee type
   */
  public boolean hasAttendeeType() {
    return super.hasElement(AttendeeType.KEY);
  }

  /**
   * Returns the email address.
   *
   * @return email address
   */
  public String getEmail() {
    return super.getAttributeValue(EMAIL);
  }

  /**
   * Sets the email address.
   *
   * @param email email address or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Who setEmail(String email) {
    super.setAttributeValue(EMAIL, email);
    return this;
  }

  /**
   * Returns whether it has the email address.
   *
   * @return whether it has the email address
   */
  public boolean hasEmail() {
    return super.hasAttribute(EMAIL);
  }

  /**
   * Returns the nested person entry.
   *
   * @return nested person entry
   */
  public EntryLink getEntryLink() {
    return super.getElement(EntryLink.KEY);
  }

  /**
   * Sets the nested person entry.
   *
   * @param entryLink nested person entry or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Who setEntryLink(EntryLink entryLink) {
    super.setElement(EntryLink.KEY, entryLink);
    return this;
  }

  /**
   * Returns whether it has the nested person entry.
   *
   * @return whether it has the nested person entry
   */
  public boolean hasEntryLink() {
    return super.hasElement(EntryLink.KEY);
  }

  /**
   * Returns the relationship between the containing entity and the contained
   * person.
   *
   * @return relationship between the containing entity and the contained person
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the relationship between the containing entity and the contained
   * person.
   *
   * @param rel relationship between the containing entity and the contained
   *     person or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Who setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the relationship between the containing entity and
   * the contained person.
   *
   * @return whether it has the relationship between the containing entity and
   *     the contained person
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  /**
   * Returns the simple string value that can be used as a representation of
   * this person.
   *
   * @return simple string value that can be used as a representation of this
   *     person
   */
  public String getValueString() {
    return super.getAttributeValue(VALUE_STRING);
  }

  /**
   * Sets the simple string value that can be used as a representation of this
   * person.
   *
   * @param valueString simple string value that can be used as a representation
   *     of this person or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Who setValueString(String valueString) {
    super.setAttributeValue(VALUE_STRING, valueString);
    return this;
  }

  /**
   * Returns whether it has the simple string value that can be used as a
   * representation of this person.
   *
   * @return whether it has the simple string value that can be used as a
   *     representation of this person
   */
  public boolean hasValueString() {
    return super.hasAttribute(VALUE_STRING);
  }

}


