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
import com.google.gdata.data.extensions.Where;
import com.google.gdata.data.extensions.When;

import java.util.List;

/**
 * This represents a single calendar in a list of calendars.  Use
 * CalendarEventEntry to represent a single event from a calendar.
 *
 * 
 */
public class CalendarEntry extends BaseEntry<CalendarEntry> {

  /**
   * Constructs a new CalendarEntry instance
   */
  public CalendarEntry() {
    super();
  }


  /**
   * Constructs a new CalendarEntry instance by doing a shallow copy of data
   * from an existing BaseEntry instance.
   */
  public CalendarEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
  }


  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by an EventEntry.
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(CalendarEntry.class,
      AccessLevelProperty.getDefaultDescription());
    extProfile.declare(CalendarEntry.class,
        ColorProperty.getDefaultDescription());
    extProfile.declare(CalendarEntry.class,
        HiddenProperty.getDefaultDescription());
    extProfile.declare(CalendarEntry.class,
      OverrideNameProperty.getDefaultDescription());
    extProfile.declare(CalendarEntry.class,
        SelectedProperty.getDefaultDescription());
    extProfile.declare(CalendarEntry.class,
        TimeZoneProperty.getDefaultDescription());
    extProfile.declare(CalendarEntry.class,
        TimesCleanedProperty.getDefaultDescription(false, false));
    extProfile.declare(CalendarEntry.class, When.getDefaultDescription());
    extProfile.declare(CalendarEntry.class, Where.getDefaultDescription());
  }


  /**
   * Returns the list of calendar locations
   */
  public List<Where> getLocations() {
    return getRepeatingExtension(Where.class);
  }


  /**
   * Adds a new calendar location.
   */
  public void addLocation(Where location) {
    getLocations().add(location);
  }

  /**
   * Returns the calendar accesslevel.
   */
  public AccessLevelProperty getAccessLevel() {
    return getExtension(AccessLevelProperty.class);
  }


  /**
   * Sets the calendar accesslevel.
   */
  public void setAccessLevel(AccessLevelProperty accesslevel) {
    setExtension(accesslevel);
  }

  /**
   * Returns the calendar color.
   */
  public ColorProperty getColor() {
    return getExtension(ColorProperty.class);
  }


  /**
   * Sets the calendar color.
   */
  public void setColor(ColorProperty color) {
    setExtension(color);
  }

  /**
   * Returns the calendar hidden property.
   */
  public HiddenProperty getHidden() {
    return getExtension(HiddenProperty.class);
  }


  /**
   * Sets the calendar hidden property.
   */
  public void setHidden(HiddenProperty hidden) {
    setExtension(hidden);
  }

  /**
   * Returns the override name property.
   */
  public OverrideNameProperty getOverrideName() {
    return getExtension(OverrideNameProperty.class);
  }


  /**
   * Sets the override name property.
   */
  public void setOverrideName(OverrideNameProperty name) {
    setExtension(name);
  }

  /**
   * Returns the calendar selected property.
   */
  public SelectedProperty getSelected() {
    return getExtension(SelectedProperty.class);
  }


  /**
   * Sets the calendar selected property.
   */
  public void setSelected(SelectedProperty selected) {
    setExtension(selected);
  }

  /**
   * Returns the calendar timeZone property.
   */
  public TimeZoneProperty getTimeZone() {
    return getExtension(TimeZoneProperty.class);
  }


  /**
   * Sets the calendar timeZone property.
   */
  public void setTimeZone(TimeZoneProperty timeZone) {
    setExtension(timeZone);
  }

  /**
   * Returns the calendar timesCleaned property.
   */
  public TimesCleanedProperty getTimesCleaned() {
    return getExtension(TimesCleanedProperty.class);
  }

  /**
   * Sets the calendar timesCleaned property.
   */
  public void setTimesCleaned(TimesCleanedProperty timesCleaned) {
    setExtension(timesCleaned);
  }
}
