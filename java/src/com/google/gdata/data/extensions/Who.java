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
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ExtensionVisitor;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.ExtensionVisitor.StoppedException;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GData schema extension describing a person. It contains a {@code
 * <gd:entryLink>} element linking to (or containing) a Contact entry.
 *
 * 
 */
public class Who extends ExtensionPoint implements Extension {

  /** Relation type. Describes the meaning of this association. */
  public static final class Rel {
    public static final String EVENT_ATTENDEE =
        Namespaces.gPrefix + "event.attendee";
    public static final String EVENT_ORGANIZER =
        Namespaces.gPrefix + "event.organizer";
    public static final String EVENT_SPEAKER =
        Namespaces.gPrefix + "event.speaker";
    public static final String EVENT_PERFORMER =
        Namespaces.gPrefix + "event.performer";
    public static final String TASK_ASSIGNED_TO =
        Namespaces.gPrefix + "task.assigned-to";
    public static final String MESSAGE_FROM =
        Namespaces.gPrefix + "message.from";
    public static final String MESSAGE_REPLY_TO =
        Namespaces.gPrefix + "message.reply-to";
    public static final String MESSAGE_TO =
        Namespaces.gPrefix + "message.to";
    public static final String MESSAGE_CC =
        Namespaces.gPrefix + "message.cc";
    public static final String MESSAGE_BCC =
        Namespaces.gPrefix + "message.bcc";
  }

  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }

  /** Email of the person. */
  protected String email;
  public String getEmail() { return email; }
  public void setEmail(String v) { email = v; }

  /** String description of the person. */
  protected String valueString;
  public String getValueString() { return valueString; }
  public void setValueString(String v) { valueString = v; }

  /** Type of event attendee. */
  public static final class AttendeeType extends ValueConstruct {
    public AttendeeType() {
      super(Namespaces.gNs, "attendeeType", "value");
    }
    public static final String EVENT_REQUIRED =
        Namespaces.gPrefix + "event.required";
    public static final String EVENT_OPTIONAL =
        Namespaces.gPrefix + "event.optional";
  }

  protected AttendeeType attendeeType = new AttendeeType();
  public String getAttendeeType() { return attendeeType.getValue(); }
  public void setAttendeeType(String v) { attendeeType.setValue(v); }

  /** Status of event attendee. */
  public static final class AttendeeStatus extends ValueConstruct {
    public AttendeeStatus() {
      super(Namespaces.gNs, "attendeeStatus", "value");
    }

    public static final String EVENT_INVITED =
        Namespaces.gPrefix + "event.invited";
    public static final String EVENT_ACCEPTED =
        Namespaces.gPrefix + "event.accepted";
    public static final String EVENT_TENTATIVE =
        Namespaces.gPrefix + "event.tentative";
    public static final String EVENT_DECLINED =
        Namespaces.gPrefix + "event.declined";
  }

  protected AttendeeStatus attendeeStatus = new AttendeeStatus();
  public String getAttendeeStatus() { return attendeeStatus.getValue(); }
  public void setAttendeeStatus(String v) { attendeeStatus.setValue(v); }

  /** Nested person entry. */
  protected EntryLink<?> entryLink;
  public EntryLink<?> getEntryLink() { return entryLink; }
  public void setEntryLink(EntryLink<?> v) { entryLink = v; }

  /** Returns a suggested extension description, which is repeatable. */
  public static ExtensionDescription getDefaultDescription() {
    return getDefaultDescription(true);
  }

  /**
   * Returns a suggested extension description,
   * which may be repeatable or not. 
   */
  public static ExtensionDescription getDefaultDescription(boolean repeatable) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Who.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("who");
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void visitChildren(ExtensionVisitor ev) throws StoppedException {
    if (entryLink != null) {
      visitChild(ev, entryLink);
    }
    super.visitChildren(ev);
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (valueString != null) {
      attrs.add(new XmlWriter.Attribute("valueString", valueString));
    }

    if (email != null) {
      attrs.add(new XmlWriter.Attribute("email", email));
    }

    generateStartElement(w, Namespaces.gNs, "who", attrs, null);

    attendeeType.generate(w, extProfile);
    attendeeStatus.generate(w, extProfile);

    if (entryLink != null) {
      entryLink.generate(w, extProfile);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "who");
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <g:who> parser. */
  protected class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) {
      super(extProfile, Who.class);
    }

    protected Handler(ExtensionProfile extProfile,
        Class<? extends ExtensionPoint> extendedClass) {
      super(extProfile, extendedClass);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) {
      if (namespace.equals("")) {
        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("valueString")) {
          valueString = value;
        } else if (localName.equals("email")) {
          email = value;
        }
      }
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.g)) {
        if (localName.equals("attendeeType")) {
          return attendeeType.getHandler(extProfile, namespace, localName,
              attrs);
        } else if (localName.equals("attendeeStatus")) {
          return attendeeStatus.getHandler(extProfile, namespace, localName,
              attrs);
        } else if (localName.equals("entryLink")) {
          entryLink = new EntryLink<BaseEntry<?>>();
          return entryLink.getHandler(extProfile, namespace, localName, attrs);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}
