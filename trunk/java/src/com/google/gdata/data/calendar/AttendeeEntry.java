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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;

/**
 * This is used to represent an entry in an attendee feed.
 *
 * 
 */
public class AttendeeEntry extends BaseEntry<AttendeeEntry> {

  public AttendeeEntry() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    // When inviting/removing attendees we can control whether to send
    // them notifications or not.  We re-purpose
    // SendEventNotificationsProperty for this task.
    extProfile.declare(CalendarEventEntry.class,
        SendEventNotificationsProperty.getDefaultDescription());
  }

  /**
   * Whether to send event notifications or not. Default is to not send
   * notifications to the attendee involved.
   */
  public boolean getSendEventNotifications() {
    SendEventNotificationsProperty send = getExtension(
        SendEventNotificationsProperty.class);
    return send != null && Boolean.parseBoolean(send.getValue());
  }

  /**
   * Sets whether event notifications are to be sent to attendee or
   * not.
   */
  public void setSendEventNotifications(boolean send) {
    setExtension(send ? SendEventNotificationsProperty.TRUE
        : SendEventNotificationsProperty.FALSE);
  }
}
