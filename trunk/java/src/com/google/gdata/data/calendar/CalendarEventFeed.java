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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.extensions.BaseEventFeed;

/**
 * Describes a Calendar event feed.
 *
 * 
 */
public class CalendarEventFeed extends BaseEventFeed<CalendarEventFeed,
    CalendarEventEntry> {

  /**
   * Default mutable constructor.
   */
  public CalendarEventFeed() {
    super(CalendarEventEntry.class);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseFeed} instance.
   *
   * @param sourceFeed source feed
   */
  public CalendarEventFeed(BaseFeed<?, ?> sourceFeed) {
    super(CalendarEventEntry.class, sourceFeed);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(CalendarEventFeed.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(CalendarEventFeed.class,
        new ExtensionDescription(TimeZoneProperty.class,
        new XmlNamespace("gCal", "http://schemas.google.com/gCal/2005"),
        "timezone", true, false, false));
    extProfile.declare(CalendarEventFeed.class, TimesCleanedProperty.class);
  }

  /**
   * Returns the time zone.
   *
   * @return time zone
   */
  public TimeZoneProperty getTimeZone() {
    return getExtension(TimeZoneProperty.class);
  }

  /**
   * Sets the time zone.
   *
   * @param timeZone time zone or <code>null</code> to reset
   */
  public void setTimeZone(TimeZoneProperty timeZone) {
    if (timeZone == null) {
      removeExtension(TimeZoneProperty.class);
    } else {
      setExtension(timeZone);
    }
  }

  /**
   * Returns whether it has the time zone.
   *
   * @return whether it has the time zone
   */
  public boolean hasTimeZone() {
    return hasExtension(TimeZoneProperty.class);
  }

  /**
   * Returns the times cleaned property.
   *
   * @return times cleaned property
   */
  public TimesCleanedProperty getTimesCleaned() {
    return getExtension(TimesCleanedProperty.class);
  }

  /**
   * Sets the times cleaned property.
   *
   * @param timesCleaned times cleaned property or <code>null</code> to reset
   */
  public void setTimesCleaned(TimesCleanedProperty timesCleaned) {
    if (timesCleaned == null) {
      removeExtension(TimesCleanedProperty.class);
    } else {
      setExtension(timesCleaned);
    }
  }

  /**
   * Returns whether it has the times cleaned property.
   *
   * @return whether it has the times cleaned property
   */
  public boolean hasTimesCleaned() {
    return hasExtension(TimesCleanedProperty.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{CalendarEventFeed " + super.toString() + "}";
  }

}
