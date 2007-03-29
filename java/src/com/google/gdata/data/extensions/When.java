/* Copyright (c) 2006 Google Inc.
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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;


/**
 * GData schema extension describing a period of time.
 *
 * 
 */
public class When extends ExtensionPoint implements Extension {


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
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(When.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("when");
    desc.setRepeatable(repeatable);
    return desc;
  }

  /** Returns the suggested extension description and is repeatable. */
  public static ExtensionDescription getDefaultDescription() {
    return getDefaultDescription(true);
  }


  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (startTime != null) {
      attrs.add(new XmlWriter.Attribute("startTime", startTime.toString()));
    }

    if (endTime != null) {
      attrs.add(new XmlWriter.Attribute("endTime", endTime.toString()));
    }

    if (valueString != null) {
      attrs.add(new XmlWriter.Attribute("valueString", valueString));
    }

    generateStartElement(w, Namespaces.gNs, "when", attrs, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "when");
  }


  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs)
      throws ParseException, IOException {

    return new Handler(extProfile);
  }


  /** <g:when> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile)
        throws ParseException, IOException {

      super(extProfile, When.class);
    }


    /** Keeps track of the startTime/endTime format. */
    private boolean wholeDays = false;
    private boolean wholeDaysKnown = false;


    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("startTime")) {
          startTime = parseDateTime(value);
        } else if (localName.equals("endTime")) {
          endTime = parseDateTime(value);
        } else if (localName.equals("valueString")) {
          valueString = value;
        }
      }
    }


    private DateTime parseDateTime(String value) throws ParseException {

      DateTime dateTime;

      try {
        dateTime = DateTime.parseDateTimeChoice(value);
      } catch (NumberFormatException e) {
        throw new ParseException("Invalid date/time value.", e);
      }

      if (wholeDaysKnown && dateTime.isDateOnly() != wholeDays) {
        if (wholeDays) {
          throw new ParseException("Date value expected.");
        } else {
          throw new ParseException("Date/time value expected.");
        }
      }

      wholeDaysKnown = true;
      wholeDays = dateTime.isDateOnly();

      return dateTime;
    }


    public void processEndElement() throws ParseException {

      if (startTime == null) {
        throw new ParseException("g:when/@startTime is required.");
      }

      if (startTime != null && endTime != null &&
          startTime.compareTo(endTime) > 0) {

        throw new ParseException(
          "g:when/@startTime must be less than or equal to g:when/@endTime.");
      }

      super.processEndElement();
    }
  }
}
