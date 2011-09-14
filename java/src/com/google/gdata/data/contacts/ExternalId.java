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
 * Contact's external id field.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = ExternalId.XML_NAME)
public class ExternalId extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "externalId";

  /** XML "label" attribute name */
  private static final String LABEL = "label";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Label */
  private String label = null;

  /** Id type */
  private String rel = null;

  /** Value */
  private String value = null;

  /** Id type. */
  public static final class Rel {

    /** Account external id. */
    public static final String ACCOUNT = "account";

    /** Customer external id. */
    public static final String CUSTOMER = "customer";

    /** Network external id. */
    public static final String NETWORK = "network";

    /** Organization external id. */
    public static final String ORGANIZATION = "organization";

  }

  /**
   * Default mutable constructor.
   */
  public ExternalId() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param label label.
   * @param rel id type.
   * @param value value.
   */
  public ExternalId(String label, String rel, String value) {
    super();
    setLabel(label);
    setRel(rel);
    setValue(value);
    setImmutable(true);
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
   * Returns the id type.
   *
   * @return id type
   */
  public String getRel() {
    return rel;
  }

  /**
   * Sets the id type.
   *
   * @param rel id type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the id type.
   *
   * @return whether it has the id type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
  public void setValue(String value) {
    throwExceptionIfImmutable();
    this.value = value;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return getValue() != null;
  }

  @Override
  protected void validate() {
    if (value == null) {
      throwExceptionForMissingAttribute(VALUE);
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
        ExtensionDescription.getDefaultDescription(ExternalId.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(LABEL, label);
    generator.put(REL, rel);
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    label = helper.consume(LABEL, false);
    rel = helper.consume(REL, false);
    value = helper.consume(VALUE, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    ExternalId other = (ExternalId) obj;
    return eq(label, other.label)
        && eq(rel, other.rel)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (label != null) {
      result = 37 * result + label.hashCode();
    }
    if (rel != null) {
      result = 37 * result + rel.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{ExternalId label=" + label + " rel=" + rel + " value=" + value +
        "}";
  }

}

