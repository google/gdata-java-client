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


package com.google.gdata.data.docs;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * The query that doclist will use.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = QueryParameter.XML_NAME)
public class QueryParameter extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "query";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  private static final AttributeHelper.EnumToAttributeValue<Type>
      TYPE_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.EnumToAttributeValue<Type>() {
    public String getAttributeValue(Type enumValue) {
      return enumValue.toValue();
    }
  };

  /** Type */
  private Type type = null;

  /** Value */
  private String value = null;

  /** Type. */
  public enum Type {

    /** Author query parameter. */
    AUTHOR("author"),

    /** Category query parameter. */
    CATEGORY("category"),

    /** Opened-max query parameter. */
    OPENED_MAX("opened-max"),

    /** Opened-min query parameter. */
    OPENED_MIN("opened-min"),

    /** Owner query parameter. */
    OWNER("owner"),

    /** Q query parameter. */
    Q("q"),

    /** Reader query parameter. */
    READER("reader"),

    /** Title query parameter. */
    TITLE("title"),

    /** Title-exact query parameter. */
    TITLE_EXACT("title-exact"),

    /** Updated-max query parameter. */
    UPDATED_MAX("updated-max"),

    /** Updated-min query parameter. */
    UPDATED_MIN("updated-min"),

    /** Writer query parameter. */
    WRITER("writer");

    private final String value;

    private Type(String value) {
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
  public QueryParameter() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param type type.
   * @param value value.
   */
  public QueryParameter(Type type, String value) {
    super();
    setType(type);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the type.
   *
   * @return type
   */
  public Type getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type type or <code>null</code> to reset
   */
  public void setType(Type type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the type.
   *
   * @return whether it has the type
   */
  public boolean hasType() {
    return getType() != null;
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
    if (type == null) {
      throwExceptionForMissingAttribute(TYPE);
    }
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
        ExtensionDescription.getDefaultDescription(QueryParameter.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(TYPE, type, TYPE_ENUM_TO_ATTRIBUTE_VALUE);
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    type = helper.consumeEnum(TYPE, true, Type.class, null,
        TYPE_ENUM_TO_ATTRIBUTE_VALUE);
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
    QueryParameter other = (QueryParameter) obj;
    return eq(type, other.type)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{QueryParameter type=" + type + " value=" + value + "}";
  }

}

