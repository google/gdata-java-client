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


package com.google.gdata.data.contacts;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.ParseException;

/**
 * Contact's events.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = Event.XML_NAME)
public class Event extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "event";

  /** XML "label" attribute name */
  private static final String LABEL = "label";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  /** Label */
  private String label = null;

  /** Event type */
  private String rel = null;

  /** Event type. */
  public static final class Rel {

    /** Anniversary event. */
    public static final String ANNIVERSARY = "anniversary";

    /** Other event. */
    public static final String OTHER = "other";

  }

  /**
   * Default mutable constructor.
   */
  public Event() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param label label.
   * @param rel event type.
   */
  public Event(String label, String rel) {
    super();
    setLabel(label);
    setRel(rel);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Event.class)) {
      return;
    }
    extProfile.declare(Event.class, new ExtensionDescription(When.class,
        new XmlNamespace("gd", "http://schemas.google.com/g/2005"), "when",
        true, false, false));
    new When().declareExtensions(extProfile);
  }

  /**
   * Returns the label.
   *
   * @return label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label.
   *
   * @param label label or <code>null</code> to reset
   */
  public void setLabel(String label) {
    throwExceptionIfImmutable();
    this.label = label;
  }

  /**
   * Returns whether it has the label.
   *
   * @return whether it has the label
   */
  public boolean hasLabel() {
    return getLabel() != null;
  }

  /**
   * Returns the event type.
   *
   * @return event type
   */
  public String getRel() {
    return rel;
  }

  /**
   * Sets the event type.
   *
   * @param rel event type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the event type.
   *
   * @return whether it has the event type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  /**
   * Returns the time period description.
   *
   * @return time period description
   */
  public When getWhen() {
    return getExtension(When.class);
  }

  /**
   * Sets the time period description.
   *
   * @param when time period description or <code>null</code> to reset
   */
  public void setWhen(When when) {
    if (when == null) {
      removeExtension(When.class);
    } else {
      setExtension(when);
    }
  }

  /**
   * Returns whether it has the time period description.
   *
   * @return whether it has the time period description
   */
  public boolean hasWhen() {
    return hasExtension(When.class);
  }

  @Override
  protected void validate() {
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(Event.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(LABEL, label);
    generator.put(REL, rel);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    label = helper.consume(LABEL, false);
    rel = helper.consume(REL, false);
  }

  @Override
  public String toString() {
    return "{Event label=" + label + " rel=" + rel + "}";
  }

}

