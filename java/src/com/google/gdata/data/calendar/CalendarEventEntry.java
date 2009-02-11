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
    extProfile.declare(CalendarEventEntry.class,
        IcalUIDProperty.getDefaultDescription());
    extProfile.declare(CalendarEventEntry.class,
        SequenceNumberProperty.getDefaultDescription());
    extProfile.declare(CalendarEventEntry.class,
        SyncEventProperty.getDefaultDescription());
    extProfile.declare(CalendarEventEntry.class,
        PrivateCopyProperty.getDefaultDescription(false, false));

    // Override gd:who processing for calender events to handle extended
    // semantics
    extProfile.declare(CalendarEventEntry.class,
        EventWho.getDefaultDescription());
    extProfile.declare(EventWho.class,
        ResourceProperty.getDefaultDescription());

    // Extend Link semantics for WebContent
    extProfile.declare(Link.class, WebContent.getDefaultDescription());
    extProfile.declare(CalendarEventEntry.class,
        GuestsCanModifyProperty.getDefaultDescription(false, false));
    extProfile.declare(CalendarEventEntry.class,
        GuestsCanInviteOthersProperty.getDefaultDescription(false, false));
    extProfile.declare(CalendarEventEntry.class,
        GuestsCanSeeGuestsProperty.getDefaultDescription(false, false));
    extProfile.declare(CalendarEventEntry.class,
        AnyoneCanAddSelfProperty.getDefaultDescription(false, false));
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
   * If the event needs to be synced i.e., the Ical UID and Sequence number
   * need to be honored.
   */
  public boolean isSyncEvent() {
    SyncEventProperty syncEvent = getExtension(SyncEventProperty.class);
    return (syncEvent != null) && "true".equalsIgnoreCase(syncEvent.getValue());
  }

  /**
   * Set whether the event needs to be synced i.e., the Ical UID and Sequence
   * number need to be honored.
   */
  public void setSyncEvent(boolean syncEvent) {
    setExtension(syncEvent ? SyncEventProperty.TRUE : SyncEventProperty.FALSE);
  }

  /** Whether this is a private copy of the event. */
  public boolean isPrivateCopy() {
    PrivateCopyProperty privateCopy = getExtension(PrivateCopyProperty.class);
    return (privateCopy != null) && privateCopy.getValue();
  }

  /** Set whether this is a private copy of the event. */
  public void setPrivateCopy(boolean privateCopy) {
    setExtension(privateCopy ? PrivateCopyProperty.TRUE
                             : PrivateCopyProperty.FALSE);
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
   * Returns the Ical UID(RFC 2445) of the event if it exists otherwise returns
   * null.
   */
  public String getIcalUID() {
    IcalUIDProperty uid = getExtension(IcalUIDProperty.class);
    return uid == null ? null : uid.getValue();
  }

  /**
   * Sets the Ical UID(RFC 2445) of the event.
   * Note: Setting uid to null deletes the value.
   */
  public void setIcalUID(String uid) {
    if (uid == null) {
      removeExtension(IcalUIDProperty.class);
    } else {
      setExtension(new IcalUIDProperty(uid));
    }
  }

  /**
   * Returns whether a sequence number is present.
   */
  public boolean hasSequence() {
    return hasExtension(SequenceNumberProperty.class);
  }

  /**
   * Returns the sequence number of the event or 0 if not set.
   */
  public int getSequence() {
    SequenceNumberProperty seq = getExtension(SequenceNumberProperty.class);
    // Absence of sequence number implies that sequence number is 0.
    return seq == null ? 0 : Integer.parseInt(seq.getValue());
  }

  /**
   * Sets the sequence number of the event.
   */
  public void setSequence(int sequence) {
    setExtension(new SequenceNumberProperty(String.valueOf(sequence)));
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

  public void setGuestsCanModify(boolean guestsCanModify) {
    setExtension(guestsCanModify
        ? GuestsCanModifyProperty.TRUE
        : GuestsCanModifyProperty.FALSE);
  }

  public boolean isGuestsCanModify() {
    GuestsCanModifyProperty guestsCanModify =
        getExtension(GuestsCanModifyProperty.class);
    return (guestsCanModify != null) && guestsCanModify.getValue();
  }

  public boolean hasGuestsCanModify() {
    return hasExtension(GuestsCanModifyProperty.class);
  }

  public void setGuestsCanInviteOthers(boolean guestsCanInviteOthers) {
    setExtension(guestsCanInviteOthers
        ? GuestsCanInviteOthersProperty.TRUE
        : GuestsCanInviteOthersProperty.FALSE);
  }

  public boolean isGuestsCanInviteOthers() {
    GuestsCanInviteOthersProperty guestsCanInviteOthers =
        getExtension(GuestsCanInviteOthersProperty.class);
    return (guestsCanInviteOthers != null) && guestsCanInviteOthers.getValue();
  }

  public boolean hasGuestsCanInviteOthers() {
    return hasExtension(GuestsCanInviteOthersProperty.class);
  }

  public void setGuestsCanSeeGuests(boolean guestsCanSeeGuests) {
    setExtension(guestsCanSeeGuests
        ? GuestsCanSeeGuestsProperty.TRUE
        : GuestsCanSeeGuestsProperty.FALSE);
  }

  public boolean isGuestsCanSeeGuests() {
    GuestsCanSeeGuestsProperty guestsCanSeeGuests =
        getExtension(GuestsCanSeeGuestsProperty.class);
    return (guestsCanSeeGuests != null) && guestsCanSeeGuests.getValue();
  }

  public boolean hasGuestsCanSeeGuests() {
    return hasExtension(GuestsCanSeeGuestsProperty.class);
  }

  public void setAnyoneCanAddSelf(boolean anyoneCanAddSelf) {
    setExtension(anyoneCanAddSelf
        ? AnyoneCanAddSelfProperty.TRUE
        : AnyoneCanAddSelfProperty.FALSE);
  }

  public boolean isAnyoneCanAddSelf() {
    AnyoneCanAddSelfProperty anyoneCanAddSelf =
        getExtension(AnyoneCanAddSelfProperty.class);
    return (anyoneCanAddSelf != null) && anyoneCanAddSelf.getValue();
  }

  public boolean hasAnyoneCanAddSelf() {
    return hasExtension(AnyoneCanAddSelfProperty.class);
  }

}
