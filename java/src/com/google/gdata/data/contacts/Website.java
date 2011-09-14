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
 * Contact related website.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = Website.XML_NAME)
public class Website extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "website";

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

  /** URI of the website */
  private String href = null;

  /** User defined website label */
  private String label = null;

  /** Denotes primary website */
  private Boolean primary = null;

  /** Known website type */
  private Rel rel = null;

  /** Known website type. */
  public enum Rel {

    /** Blog website. */
    BLOG("blog"),

    /** Ftp website. */
    FTP("ftp"),

    /** Home website. */
    HOME("home"),

    /** Home-page website. */
    HOME_PAGE("home-page"),

    /** Other website. */
    OTHER("other"),

    /** Profile website. */
    PROFILE("profile"),

    /** Work website. */
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
  public Website() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param href URI of the website.
   * @param label user defined website label.
   * @param primary denotes primary website.
   * @param rel known website type.
   */
  public Website(String href, String label, Boolean primary, Rel rel) {
    super();
    setHref(href);
    setLabel(label);
    setPrimary(primary);
    setRel(rel);
    setImmutable(true);
  }

  /**
   * Returns the URI of the website.
   *
   * @return URI of the website
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the URI of the website.
   *
   * @param href URI of the website or <code>null</code> to reset
   */
  public void setHref(String href) {
    throwExceptionIfImmutable();
    this.href = href;
  }

  /**
   * Returns whether it has the URI of the website.
   *
   * @return whether it has the URI of the website
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the user defined website label.
   *
   * @return user defined website label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the user defined website label.
   *
   * @param label user defined website label or <code>null</code> to reset
   */
  public void setLabel(String label) {
    throwExceptionIfImmutable();
    this.label = label;
  }

  /**
   * Returns whether it has the user defined website label.
   *
   * @return whether it has the user defined website label
   */
  public boolean hasLabel() {
    return getLabel() != null;
  }

  /**
   * Returns the denotes primary website.
   *
   * @return denotes primary website
   */
  public Boolean getPrimary() {
    return primary;
  }

  /**
   * Sets the denotes primary website.
   *
   * @param primary denotes primary website or <code>null</code> to reset
   */
  public void setPrimary(Boolean primary) {
    throwExceptionIfImmutable();
    this.primary = primary;
  }

  /**
   * Returns whether it has the denotes primary website.
   *
   * @return whether it has the denotes primary website
   */
  public boolean hasPrimary() {
    return getPrimary() != null;
  }

  /**
   * Returns the known website type.
   *
   * @return known website type
   */
  public Rel getRel() {
    return rel;
  }

  /**
   * Sets the known website type.
   *
   * @param rel known website type or <code>null</code> to reset
   */
  public void setRel(Rel rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the known website type.
   *
   * @return whether it has the known website type
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
        ExtensionDescription.getDefaultDescription(Website.class);
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
    Website other = (Website) obj;
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
    return "{Website href=" + href + " label=" + label + " primary=" + primary +
        " rel=" + rel + "}";
  }

}

