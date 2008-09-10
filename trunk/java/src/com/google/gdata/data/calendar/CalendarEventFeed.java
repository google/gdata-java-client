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
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.extensions.BaseEventFeed;

/**
 * This extends the EventFeed class to feeds for Google Calendars,
 * which is a list of events.
 *
 * 
 */
public class CalendarEventFeed
    extends BaseEventFeed<CalendarEventFeed, CalendarEventEntry> {

  /**
   * Constructs a new {@code CalendarEventFeed} instance that is
   * parameterized to contain {@code EventEntry} instances.
   */
  public CalendarEventFeed() {
    super(CalendarEventEntry.class);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {

    // Add any feed-level extension declarations here.

    super.declareExtensions(extProfile);
    // extProfile.declareFeedExtension(
    //      AccessLevelProperty.getDefaultDescription());
    extProfile.declare(CalendarEventFeed.class,
        TimeZoneProperty.getDefaultDescription());
    extProfile.declare(CalendarEventFeed.class,
        TimesCleanedProperty.getDefaultDescription(false, false));
    // extProfile.declareFeedExtension(When.getDefaultDescription());
    BatchUtils.declareExtensions(extProfile);
  }

  /** TimeZone associated with the feed. */
  protected String tzName;
  public String getTimeZone() { return tzName; }
  public void setTimeZone(String v) { tzName = v; }

  // Any additional feed-level extension accessor APIs go here
}
