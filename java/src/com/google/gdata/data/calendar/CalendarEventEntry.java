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


package com.google.gdata.data.calendar;

import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.BaseEventEntry;
import com.google.gdata.data.extensions.ExtendedProperty;

import java.util.List;

/**
 * This extends the EventEntry class for event entries in Google Calendars,
 *
 * 
 */
public class CalendarEventEntry extends BaseEventEntry<CalendarEventEntry> {

  /**
   * Constructs a new {@code CalendarEventEntry} instance .
   */
  public CalendarEventEntry() { super(); }


  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(CalendarEventEntry.class,
        QuickAddProperty.getDefaultDescription());
    extProfile.declare(CalendarEventEntry.class,
        ExtendedProperty.getDefaultDescription());
    extProfile.declare(CalendarEventEntry.class,
        SendEventNotificationsProperty.getDefaultDescription());

    // Override gd:who processing for calender events to handle extended
    // semantics
    extProfile.declare(CalendarEventEntry.class,
        EventWho.getDefaultDescription());
    extProfile.declare(EventWho.class,
        ResourceProperty.getDefaultDescription());

    // Extend Link semantics for WebContent
    extProfile.declare(Link.class, WebContent.getDefaultDescription());
  }

  /**
   * Returns the list of event participants.
   */
  public List<EventWho> getParticipants() {
    return getRepeatingExtension(EventWho.class);
  }

  /**
   * Adds a new event participant.
   */
  public void addParticipant(EventWho participant) {
    getParticipants().add(participant);
  }

  /**
   * Returns extended properties (gd:extendedProperty)
   */
  public List<ExtendedProperty> getExtendedProperty() {
    return getRepeatingExtension(ExtendedProperty.class);
  }

  /**
   * Add a new extended property (gd:extendedProperty)
   */
  public void addExtendedProperty(ExtendedProperty prop) {
    getExtendedProperty().add(prop);
  }

  /**
   * if gd:content contains quickadd info
   */
  public boolean getQuickAdd() {
    QuickAddProperty quickAdd = getExtension(QuickAddProperty.class);
    return quickAdd != null && "true".equalsIgnoreCase(quickAdd.getValue());
  }

  /**
   * set whether gd:content is quickadd info
   * This will only effects Insert semantics.  QuickAddProperty should never
   * appear in GData query result
   * Not all GData services that expose EventEntry data format may support
   * quickadd
   * @param quickAdd
   */
  public void setQuickAdd(boolean quickAdd) {
    setExtension(quickAdd ? QuickAddProperty.TRUE : QuickAddProperty.FALSE);
  }

  /**
   * Whether to send event notifications or not. Default is to not send
   * notifications to the other participants.
   */
  public boolean getSendEventNotifications() {
    SendEventNotificationsProperty send = getExtension(
        SendEventNotificationsProperty.class);
    return send != null && Boolean.parseBoolean(send.getValue());
  }

  /**
   * Sets whether event notifications are to be sent to the other participants
   * or not.
   */
  public void setSendEventNotifications(boolean send) {
    setExtension(send ? SendEventNotificationsProperty.TRUE
        : SendEventNotificationsProperty.FALSE);
  }

  /**


   * Retrieves the web content link
   */
  public Link getWebContentLink() {
    return getLink(WebContent.REL, null);
  }

  /**
   * Get the web content for this entry (may return null).
   *
   * Changes made to the WebContent object returned by this method
   * will be reflected in this CalendarEventEntry.
   */
  public WebContent getWebContent() {
    Link webContentLink = getWebContentLink();
    if (webContentLink == null) {
      return null;
    }
    return webContentLink.getExtension(WebContent.class);
  }

  /**
   * Set the web content for this entry
   */
  public void setWebContent(WebContent wc) {
    Link oldWebContentLink = getWebContentLink();
    if (wc == null) {
      // option 1: remove old Link
      if (oldWebContentLink != null) {
        getLinks().remove(oldWebContentLink);
      }
    } else if (oldWebContentLink == null) {
      // option 2: add new Link
      getLinks().add(wc.getLink());
    } else if (oldWebContentLink != wc.getLink()) {
      // option 3: replace old Link
      getLinks().remove(oldWebContentLink);
      getLinks().add(wc.getLink());
    }
  }

}

