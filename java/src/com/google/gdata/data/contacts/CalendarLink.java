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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Contact related calendar link.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = CalendarLink.XML_NAME)
public class CalendarLink extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "calendarLink";

  /** XML "href" attribute name */
  private static final String HREF = "href";

  /** XML "label" attribute name */
  private static final String LABEL = "label";

  /** XML "primary" attribute name */
  private static final String PRIMARY = "primary";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  private static final AttributeHelper.EnumToAttributeValue<Rel>
      REL_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.EnumToAttributeValue<Rel>() {
    public String getAttributeValue(Rel enumValue) {
      return enumValue.toValue();
    }
  };

  /** URI of the calendar */
  private String href = null;

  /** User defined calendar link label */
  private String label = null;

  /** Denotes primary calendar link */
  private Boolean primary = null;

  /** Known calendar link type */
  private Rel rel = null;

  /** Known calendar link type. */
  public enum Rel {

    /** Free-busy calendar link. */
    FREE_BUSY("free-busy"),

    /** Home calendar link. */
    HOME("home"),

    /** Work calendar link. */
    WORK("work");

    private final String value;

    private Rel(String value) {
      this.value = value;
    }

    /**
     * Returns the value used in the XML.
     *
     * @return value used in the XML
     */
    public String toValue() {
      return value;
    }

  }

  /**
   * Default mutable constructor.
   */
  public CalendarLink() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param href URI of the calendar.
   * @param label user defined calendar link label.
   * @param primary denotes primary calendar link.
   * @param rel known calendar link type.
   */
  public CalendarLink(String href, String label, Boolean primary, Rel rel) {
    super();
    setHref(href);
    setLabel(label);
    setPrimary(primary);
    setRel(rel);
    setImmutable(true);
  }

  /**
   * Returns the URI of the calendar.
   *
   * @return URI of the calendar
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the URI of the calendar.
   *
   * @param href URI of the calendar or <code>null</code> to reset
   */
  public void setHref(String href) {
    throwExceptionIfImmutable();
    this.href = href;
  }

  /**
   * Returns whether it has the URI of the calendar.
   *
   * @return whether it has the URI of the calendar
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the user defined calendar link label.
   *
   * @return user defined calendar link label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the user defined calendar link label.
   *
   * @param label user defined calendar link label or <code>null</code> to reset
   */
  public void setLabel(String label) {
    throwExceptionIfImmutable();
    this.label = label;
  }

  /**
   * Returns whether it has the user defined calendar link label.
   *
   * @return whether it has the user defined calendar link label
   */
  public boolean hasLabel() {
    return getLabel() != null;
  }

  /**
   * Returns the denotes primary calendar link.
   *
   * @return denotes primary calendar link
   */
  public Boolean getPrimary() {
    return primary;
  }

  /**
   * Sets the denotes primary calendar link.
   *
   * @param primary denotes primary calendar link or <code>null</code> to reset
   */
  public void setPrimary(Boolean primary) {
    throwExceptionIfImmutable();
    this.primary = primary;
  }

  /**
   * Returns whether it has the denotes primary calendar link.
   *
   * @return whether it has the denotes primary calendar link
   */
  public boolean hasPrimary() {
    return getPrimary() != null;
  }

  /**
   * Returns the known calendar link type.
   *
   * @return known calendar link type
   */
  public Rel getRel() {
    return rel;
  }

  /**
   * Sets the known calendar link type.
   *
   * @param rel known calendar link type or <code>null</code> to reset
   */
  public void setRel(Rel rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the known calendar link type.
   *
   * @return whether it has the known calendar link type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  @Override
  protected void validate() {
    if (href == null) {
      throwExceptionForMissingAttribute(HREF);
    }
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
        ExtensionDescription.getDefaultDescription(CalendarLink.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(HREF, href);
    generator.put(LABEL, label);
    generator.put(PRIMARY, primary);
    generator.put(REL, rel, REL_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    href = helper.consume(HREF, true);
    label = helper.consume(LABEL, false);
    primary = helper.consumeBoolean(PRIMARY, false);
    rel = helper.consumeEnum(REL, false, Rel.class, null,
        REL_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    CalendarLink other = (CalendarLink) obj;
    return eq(href, other.href)
        && eq(label, other.label)
        && eq(primary, other.primary)
        && eq(rel, other.rel);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (href != null) {
      result = 37 * result + href.hashCode();
    }
    if (label != null) {
      result = 37 * result + label.hashCode();
    }
    if (primary != null) {
      result = 37 * result + primary.hashCode();
    }
    if (rel != null) {
      result = 37 * result + rel.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{CalendarLink href=" + href + " label=" + label + " primary=" +
        primary + " rel=" + rel + "}";
  }

}

