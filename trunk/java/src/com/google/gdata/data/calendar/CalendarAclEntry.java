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

// manual changes (dimas):
// 1. extends BaseAclEntry<CalendarAclEntry> -> extends AclEntry (+/- imports)
// 2. thus removing public CalendarAclEntry(BaseEntry<?> sourceEntry) { super }

package com.google.gdata.data.calendar;

import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.acl.AclEntry;

/**
 * Describes an entry in a feed of a Calendar access control list (ACL).
 *
 * 
 */
public class CalendarAclEntry extends AclEntry {

  /**
   * Default mutable constructor.
   */
  public CalendarAclEntry() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(CalendarAclEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(CalendarAclEntry.class,
        SendAclNotificationsProperty.class);
  }

  /**
   * Returns the send acl notifications property.
   *
   * @return send acl notifications property
   */
  public SendAclNotificationsProperty getSendAclNotifications() {
    return getExtension(SendAclNotificationsProperty.class);
  }

  /**
   * Sets the send acl notifications property.
   *
   * @param sendAclNotifications send acl notifications property or
   *     <code>null</code> to reset
   */
  public void setSendAclNotifications(SendAclNotificationsProperty
      sendAclNotifications) {
    if (sendAclNotifications == null) {
      removeExtension(SendAclNotificationsProperty.class);
    } else {
      setExtension(sendAclNotifications);
    }
  }

  /**
   * Returns whether it has the send acl notifications property.
   *
   * @return whether it has the send acl notifications property
   */
  public boolean hasSendAclNotifications() {
    return hasExtension(SendAclNotificationsProperty.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{CalendarAclEntry " + super.toString() + "}";
  }

}
