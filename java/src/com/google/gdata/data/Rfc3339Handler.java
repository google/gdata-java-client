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


package com.google.gdata.data;

import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

/**
 * Parser for the {@code atomPerson} type.
 *
 * 
 */
public class Rfc3339Handler extends XmlParser.ElementHandler {

  /** Date/time value being parsed. */
  private DateTime dateTime;
  /** @return   the timestamp */
  public DateTime getDateTime() { return dateTime; }

  /**
   * Processes this element; overrides inherited method.
   */
  @Override
  public void processEndElement() throws ParseException {
    try {
      dateTime = DateTime.parseDateTime(value);
    } catch (NumberFormatException e) {
      throw new ParseException(
          CoreErrorDomain.ERR.invalidDatetime.withInternalReason(
              "Invalid date/time format: '" + value + "'."));
    }
  }
}
