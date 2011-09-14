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

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Contact's relation.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = Relation.XML_NAME)
public class Relation extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "relation";

  /** XML "label" attribute name */
  private static final String LABEL = "label";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  private static final AttributeHelper.EnumToAttributeValue<Rel>
      REL_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.EnumToAttributeValue<Rel>() {
    public String getAttributeValue(Rel enumValue) {
      return enumValue.toValue();
    }
  };

  /** Label */
  private String label = null;

  /** Relation type */
  private Rel rel = null;

  /** Value */
  private String value = null;

  /** Relation type. */
  public enum Rel {

    /** Assistant relation. */
    ASSISTANT("assistant"),

    /** Brother relation. */
    BROTHER("brother"),

    /** Child relation. */
    CHILD("child"),

    /** Domestic-partner relation. */
    DOMESTIC_PARTNER("domestic-partner"),

    /** Father relation. */
    FATHER("father"),

    /** Friend relation. */
    FRIEND("friend"),

    /** Manager relation. */
    MANAGER("manager"),

    /** Mother relation. */
    MOTHER("mother"),

    /** Parent relation. */
    PARENT("parent"),

    /** Partner relation. */
    PARTNER("partner"),

    /** Referred-by relation. */
    REFERRED_BY("referred-by"),

    /** Relative relation. */
    RELATIVE("relative"),

    /** Sister relation. */
    SISTER("sister"),

    /** Spouse relation. */
    SPOUSE("spouse");

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
  public Relation() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param label label.
   * @param rel relation type.
   * @param value value.
   */
  public Relation(String label, Rel rel, String value) {
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
   * Returns the relation type.
   *
   * @return relation type
   */
  public Rel getRel() {
    return rel;
  }

  /**
   * Sets the relation type.
   *
   * @param rel relation type or <code>null</code> to reset
   */
  public void setRel(Rel rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the relation type.
   *
   * @return whether it has the relation type
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
        ExtensionDescription.getDefaultDescription(Relation.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(LABEL, label);
    generator.put(REL, rel, REL_ENUM_TO_ATTRIBUTE_VALUE);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    label = helper.consume(LABEL, false);
    rel = helper.consumeEnum(REL, false, Rel.class, null,
        REL_ENUM_TO_ATTRIBUTE_VALUE);
    value = helper.consume(null, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Relation other = (Relation) obj;
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
    return "{Relation label=" + label + " rel=" + rel + " value=" + value + "}";
  }

}

