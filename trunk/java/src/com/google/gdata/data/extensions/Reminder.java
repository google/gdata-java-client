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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
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
 * GData schema extension describing a reminder on an event.  You can
 * represent a set of reminders where each has a (1) reminder period
 * and (2) notification method.  The method can be either "sms",
 * "email", "alert", "none", "all".
 *
 * <p>
 * The meaning of this set of reminders differs based on whether you
 * are reading or writing feeds.  When reading, the set of reminders
 * returned on an event takes into account both defaults on a
 * parent recurring event (when applicable) as well as the user's
 * defaults on calendar.  If there are no gd:reminders returned that
 * means the event has absolutely no reminders.  "none" or "all" will
 * not apply in this case.
 *
 * <p>
 * Writing is different because we have to be backwards-compatible
 * (see *) with the old way of setting reminders.  For easier analysis
 * we describe all the behaviors defined in the table below.  (Notice
 * we only include cases for minutes, as the other cases specified in
 * terms of days/hours/absoluteTime can be converted to this case.)
 *
 * <p>
 * Notice method is case-sensitive: must be in lowercase!
 *
 * <pre>
 *                   no method      method         method=
 *                   or method=all  =none          email|sms|alert
 *  ____________________________________________________________________________
 *  no gd:rem        *no reminder    N/A            N/A
 *
 *  1 gd:rem         *use user's    no reminder    InvalidEntryException
 *                   def. settings
 *
 *  1 gd:rem min=0   *use user's    no reminder    InvalidEntryException
 *                   def. settings
 *
 *  1 gd:rem min=-1  *no reminder   no reminder    InvalidEntryException
 *
 *  1 gd:rem min=+n  *override with no reminder    set exactly one reminder
 *                   +n for user's                 on event at +n with given
 *                   selected                      method
 *                   methods
 *
 *  multiple gd:rem  InvalidEntry-  InvalidEntry-  copy this set exactly
 *                   Exception      Exception
 * </pre>
 *
 * Hence, to override an event with a set of reminder <time, method>
 * pairs, just specify them exactly.  To clear an event of all
 * overrides (and go back to inheriting the user's defaults), one can
 * simply specify a single gd:reminder with no extra attributes.  To
 * have NO event reminders on an event, either set a single
 * gd:reminder with negative reminder time, or simply update the event
 * with a single <gd:reminder method=none/>.
 *
 * 
 */
public class Reminder extends ExtensionPoint implements Extension {

  public enum Method {
    ALERT,
    ALL,
    EMAIL,
    NONE,
    SMS;

    /**
     * @throws IllegalArgumentException if it doesn't match a method
     */
    public static Method fromString(String s) {
      if (!s.equals(s.toLowerCase())) {
        throw new IllegalArgumentException("Bad input for method: " + s);
      }
      return Enum.<Method>valueOf(Method.class, s.toUpperCase());
    }

    /**
     * Creates the string value suitable for the value of "method" in
     * a GData feed.
     */
    public String generate() {
      return name().toLowerCase();
    }
  }

  /** Number of days before the start time. */
  protected Integer days;
  public Integer getDays() { return days; }
  public void setDays(Integer v) { days = v; }


  /** Number of hours before the start time. */
  protected Integer hours;
  public Integer getHours() { return hours; }
  public void setHours(Integer v) { hours = v; }


  /** Number of minute before the start times. */
  protected Integer minutes;
  public Integer getMinutes() { return minutes; }
  public void setMinutes(Integer v) { minutes = v; }


  /** Absolute time of the reminder. */
  protected DateTime absoluteTime;
  public DateTime getAbsoluteTime() { return absoluteTime; }
  public void setAbsoluteTime(DateTime v) { absoluteTime = v; }

  /** Optional: if not set we use the user's default methods on this calendar */
  protected Method method;
  public Method getMethod() {  return method; }
  public void setMethod(Method v) { method = v; }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Reminder.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("reminder");
    desc.setRepeatable(true);
    return desc;
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (days != null) {
      attrs.add(new XmlWriter.Attribute("days", days.toString()));
    }

    if (hours != null) {
      attrs.add(new XmlWriter.Attribute("hours", hours.toString()));
    }

    if (minutes != null) {
      attrs.add(new XmlWriter.Attribute("minutes", minutes.toString()));
    }

    if (absoluteTime != null) {
      attrs.add(new XmlWriter.Attribute("absoluteTime",
                                        absoluteTime.toString()));
    }

    if (method != null) {
      attrs.add(new XmlWriter.Attribute("method",
                                        method.generate()));
    }

    generateStartElement(w, Namespaces.gNs, "reminder", attrs, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "reminder");
  }


  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <g:reminder> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) {
      super(extProfile, Reminder.class);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals("")) {
        if (localName.equals("days")) {

          try {
            days = Integer.valueOf(value);
          } catch (NumberFormatException e) {
            throw new ParseException(
                CoreErrorDomain.ERR.invalidReminderDays, e);
          }

        } else if (localName.equals("hours")) {

          try {
            hours = Integer.valueOf(value);
          } catch (NumberFormatException e) {
            throw new ParseException(
                CoreErrorDomain.ERR.invalidReminderHours, e);
          }

        } else if (localName.equals("minutes")) {

          try {
            minutes = Integer.valueOf(value);
          } catch (NumberFormatException e) {
            throw new ParseException(
                CoreErrorDomain.ERR.invalidReminderMinutes, e);
          }

        } else if (localName.equals("absoluteTime")) {

          try {
            absoluteTime = DateTime.parseDateTime(value);
          } catch (NumberFormatException e) {
            throw new ParseException(
                CoreErrorDomain.ERR.invalidReminderAbsoluteTime, e);
          }

        } else if (localName.equals("method")) {

          try {
            method = Method.fromString(value);
          } catch (IllegalArgumentException e) {
            throw new ParseException(
                CoreErrorDomain.ERR.invalidReminderMethod, e);
          }
        }
      }
    }

    @Override
    public void processEndElement() throws ParseException {

      if ((days == null ? 0 : 1) +
          (hours == null ? 0 : 1) +
          (minutes == null ? 0 : 1) +
          (absoluteTime == null ? 0 : 1) > 1) {

        throw new ParseException(
            CoreErrorDomain.ERR.tooManyAttributes);
      }

      super.processEndElement();
    }
  }
}
