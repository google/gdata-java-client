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


package com.google.gdata.data.extensions;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;


/**
 * GData schema extension describing a period of time.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = When.WHEN,
    isRepeatable = true)
public class When extends ExtensionPoint implements Extension {

  /** XML "when" element name */
  static final String WHEN = "when";

  /** XML "startTime" attribute name */
  private static final String START_TIME = "startTime";

  /** XML "endTime" attribute name */
  private static final String END_TIME = "endTime";

  /** XML "valueString" attribute name */
  private static final String VALUE_STRING = "valueString";

  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }  
  
  /** Event start time (required). */
  protected DateTime startTime;
  public DateTime getStartTime() { return startTime; }
  public void setStartTime(DateTime v) { startTime = v; }


  /** Event end time (optional). */
  protected DateTime endTime;
  public DateTime getEndTime() { return endTime; }
  public void setEndTime(DateTime v) { endTime = v; }


  /** String description of the event times. */
  protected String valueString;
  public String getValueString() { return valueString; }
  public void setValueString(String v) { valueString = v; }


  /**
   * Returns the suggested extension description with configurable
   * repeatabilty.
   */
  public static ExtensionDescription getDefaultDescription(boolean repeatable) {
    ExtensionDescription desc = ExtensionDescription
        .getDefaultDescription(When.class);
    desc.setRepeatable(repeatable);
    return desc;
  }

  /** Returns the suggested extension description and is repeatable. */
  public static ExtensionDescription getDefaultDescription() {
    return getDefaultDescription(true);
  }

  @Override
  protected void validate() throws IllegalStateException {
    if (startTime == null) {
      throwExceptionForMissingAttribute(START_TIME);
    }
    if (endTime != null) {
      if (startTime.compareTo(endTime) > 0) {
        throw new IllegalStateException(
            "g:when/@startTime must be less than or equal to g:when/@endTime.");
      }
      if (startTime.isDateOnly() != endTime.isDateOnly()) {
        throw new IllegalStateException(
            (startTime.isDateOnly() ? "Date" : "Date/time")
            + " value expected.");
      }
    }
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(START_TIME, startTime);
    generator.put(END_TIME, endTime);
    generator.put(VALUE_STRING, valueString);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    startTime = helper.consumeDateTime(START_TIME, true);
    endTime = helper.consumeDateTime(END_TIME, false);
    valueString = helper.consume(VALUE_STRING, false);
  }

}
