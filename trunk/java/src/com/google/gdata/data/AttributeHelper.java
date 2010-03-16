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

import org.xml.sax.Attributes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Helps accessing tag attributes.
 *
 * The helper only checks attributes in the default namespace ("") and
 * rejects unknown attributes.
 *
 * The idea is to remove (consume) attributes as they are read
 * from the list and at the end make sure that all attributes have
 * been read, to detect whether unknown attributes have been
 * specified. This is done by the method {@link #assertAllConsumed()} usually
 * called from
 * {@link com.google.gdata.util.XmlParser.ElementHandler#processEndElement()}.
 *
 * 
 */
public class AttributeHelper {

  /** Maps attribute local name to string value. */
  protected final Map<String, String> attrs = new HashMap<String, String>();

  /** set of attributes that are duplicated */
  private Set<String> dups = new HashSet<String>();

  /** if the content has been consumed */
  private boolean contentConsumed = false;

  /** element's text content or {@code null} for no text content */
  private String content = null;

  /**
   * Creates a helper tied to a specific set of SAX attributes.
   *
   * @param attrs the SAX attributes to be processed
   */
  public AttributeHelper(Attributes attrs) {
    // attributes
    for (int i = 0; i < attrs.getLength(); i++) {
      if (attrs.getURI(i).length() != 0) {
        String attrLocalName = attrs.getLocalName(i);
        if (this.attrs.put(attrLocalName, attrs.getValue(i)) != null) {
          dups.add(attrLocalName);
        }
      } else {
        this.attrs.put(attrs.getQName(i), attrs.getValue(i));
      }
    }
  }

  /**
   * Gets the element's text content and removes it from the list.
   *
   * @param required indicates attributes is required
   * @return element's text content or {@code null} for no text content
   * @exception ParseException if required is set and the text content
   *   is not defined
   */
  public String consumeContent(boolean required) throws ParseException {
    return consume(null, required);
  }

  /**
   * Gets the value of an attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attributes is required
   * @return attribute value or null if not available
   * @exception ParseException if required is set and the attribute
   *   is not defined
   */
  public String consume(String name, boolean required) throws ParseException {
    if (name == null) {
      if (content == null && required) {
        throw new ParseException(
            CoreErrorDomain.ERR.missingRequiredContent);
      }
      contentConsumed = true;
      return content;
    }
    String value = attrs.get(name);
    if (value == null) {
      if (required) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.missingAttribute);
       pe.setInternalReason("Missing attribute: '" + name + "'");
       throw pe;
      }
      return null;
    }
    attrs.remove(name);
    return value;
  }

  /**
   * Gets the value of a byte attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used if
   *     not present)
   * @return the byte value of this attribute
   * @exception ParseException if required is set and the attribute
   *     is not defined, or if the attribute value is not a valid byte
   */
  public byte consumeByte(String name, boolean required, byte defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Byte.parseByte(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidByteAttribute);
      pe.setInternalReason("Invalid byte value for attribute: '" + name + "'");
      throw pe;
    }
  }

  /**
   * Gets the value of a byte attribute and remove it from the list.
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @return the byte value of this attribute, 0 by default
   * @exception ParseException if required is set and the attribute is not
   *     defined, or if the attribute value is not a valid byte
   */
  public byte consumeByte(String name, boolean required) throws ParseException {
    return consumeByte(name, required, (byte) 0);
  }

  /**
   * Gets the value of a short attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used if
   *     not present)
   * @return the short value of this attribute
   * @exception ParseException if required is set and the attribute
   *     is not defined, or if the attribute value is not a valid short
   */
  public short consumeShort(String name, boolean required, short defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Short.parseShort(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidShortAttribute);
      pe.setInternalReason("Invalid short value for attribute: '" + name + "'");
      throw pe;
    }
  }

  /**
   * Gets the value of a short attribute and remove it from the list.
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @return the short value of this attribute, 0 by default
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid short
   */
  public short consumeShort(String name, boolean required)
      throws ParseException {
    return consumeShort(name, required, (short) 0);
  }

  /**
   * Gets the value of an integer attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used
   *        if not present)
   * @return the integer value of this attribute
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid integer
   */
  public int consumeInteger(String name, boolean required, int defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidIntegerAttribute);
      pe.setInternalReason("Invalid integer value for attribute: '" +
          name + "'");
      throw pe;
    }
  }

  /**
   * Gets the value of an integer attribute and remove it from the list.
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @return the integer value of this attribute, 0 by default
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid integer
   */
  public int consumeInteger(String name, boolean required)
      throws ParseException {
    return consumeInteger(name, required, 0);
  }

  /**
   * Gets the value of a long attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used
   *        if not present)
   * @return the long value of this attribute
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid long
   */
  public long consumeLong(String name, boolean required, long defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidLongAttribute, e);
     pe.setInternalReason("Invalid long value for attribute: '" +
          name + "'");
     throw pe;
    }
  }

  /**
   * Gets the value of a long attribute and remove it from the list.
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @return the long value of this attribute, 0 by default
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid long
   */

  public long consumeLong(String name, boolean required)
      throws ParseException {
    return consumeLong(name, required, 0);
  }

  /**
   * Gets the value of a big integer attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used if
   *     not present)
   * @return the big integer value of this attribute
   * @exception ParseException if required is set and the attribute
   *     is not defined, or if the attribute value is not a valid big integer
   */
  public BigInteger consumeBigInteger(String name, boolean required,
      BigInteger defaultValue) throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    try {
      return new BigInteger(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidBigIntegerAttribute);
      pe.setInternalReason("Invalid big integer value for attribute: '" + name
          + "'");
      throw pe;
    }
  }

  /**
   * Gets the value of a big integer attribute and remove it from the list.
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @return the big integer value of this attribute, 0 by default
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid big integer
   */
  public BigInteger consumeBigInteger(String name, boolean required)
      throws ParseException {
    return consumeBigInteger(name, required, BigInteger.ZERO);
  }

  /**
   * Gets the value of a big decimal attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used if
   *     not present)
   * @return the big decimal value of this attribute
   * @exception ParseException if required is set and the attribute
   *     is not defined, or if the attribute value is not a valid big decimal
   */
  public BigDecimal consumeBigDecimal(String name, boolean required,
      BigDecimal defaultValue) throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    try {
      return new BigDecimal(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidBigDecimalAttribute);
      pe.setInternalReason("Invalid big decimal value for attribute: '" + name
          + "'");
      throw pe;
    }
  }

  /**
   * Gets the value of a big decimal attribute and remove it from the list.
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @return the big decimal value of this attribute, 0 by default
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid big decimal
   */
  public BigDecimal consumeBigDecimal(String name, boolean required)
      throws ParseException {
    return consumeBigDecimal(name, required, BigDecimal.ZERO);
  }

  /**
   * Gets the value of a double attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required     indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used if
   *                     not present)
   * @return the double value of this attribute
   * @throws ParseException if required is set and the attribute is not defined,
   *                        or if the attribute value is not a valid double
   */
  public double consumeDouble(
      String name, boolean required, double defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    if ("INF".equals(value)) {
      return Double.POSITIVE_INFINITY;
    } else if ("-INF".equals(value)) {
      return Double.NEGATIVE_INFINITY;
    }
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidDoubleAttribute, e);
      pe.setInternalReason("Invalid double value for attribute: '" +
          name + "'");
      throw pe;
    }
  }

  /**
   * Gets the value of a double attribute and remove it from the list.
   *
   * @param name     attribute name
   * @param required indicates attribute is required
   * @return the double value of this attribute, 0 by default
   * @throws ParseException if required is set and the attribute is not defined,
   *                        or if the attribute value is not a valid double
   */
  public double consumeDouble(String name, boolean required)
      throws ParseException {
    return consumeDouble(name, required, 0);
  }

  /**
   * Gets the value of a float attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required     indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used if
   *                     not present)
   * @return the float value of this attribute
   * @throws ParseException if required is set and the attribute is not defined,
   *                        or if the attribute value is not a valid float
   */
  public float consumeFloat(
      String name, boolean required, float defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    if ("INF".equals(value)) {
      return Float.POSITIVE_INFINITY;
    } else if ("-INF".equals(value)) {
      return Float.NEGATIVE_INFINITY;
    }
    try {
      return Float.parseFloat(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidFloatAttribute, e);
      pe.setInternalReason("Invalid float value for attribute: '" +
          name + "'");
      throw pe;
    }
  }

  /**
   * Gets the value of a float attribute and remove it from the list.
   *
   * @param name     attribute name
   * @param required indicates attribute is required
   * @return the float value of this attribute, 0 by default
   * @throws ParseException if required is set and the attribute is not defined,
   *                        or if the attribute value is not a valid float
   */
  public float consumeFloat(String name, boolean required)
      throws ParseException {
    return consumeFloat(name, required, 0);
  }

  /**
   * Gets the value of a boolean attribute and remove it from the list. The
   * accepted values are based upon xsd:boolean syntax (true, false, 1, 0).
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param defaultValue the default value for an optional attribute (used
   *        if not present)
   * @return the boolean value of this attribute
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is neither {@code true}
   *   nor {@code false}.
   */
  public boolean consumeBoolean(String name, boolean required,
                                boolean defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    if ("true".equals(value) || "1".equals(value)) {
      return true;
    } else if ("false".equals(value) || "0".equals(value)) {
      return false;
    } else {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidBooleanAttribute);
      pe.setInternalReason("Invalid boolean value for attribute: '" +
          name + "'");
      throw pe;
    }

  }

  /**
   * Gets the value of a boolean attribute and remove it from the list. The
   * accepted values are based upon xsd:boolean syntax (true, false, 1, 0).
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @return the boolean value of this attribute, false by default
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is neither {@code true}
   *   nor {@code false}.
   */
  public boolean consumeBoolean(String name, boolean required)
      throws ParseException {
    return consumeBoolean(name, required, false);
  }

  /**
   * Gets the value of a {@link DateTime} attribute and remove it from the list.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @return the date-time value of this attribute, {@code null} by default
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the date-time attribute cannot be parsed
   */
  public DateTime consumeDateTime(String name, boolean required)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return null;
    }
    try {
      return DateTime.parseDateTimeChoice(value);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidDatetime, e);
      pe.setInternalReason("Badly formatted datetime in attribute: " + name);
      throw pe;
    }
  }

  /**
   * Defines a custom mapping of an enum value to an attribute value (similar to
   * a closure).
   */
  public static interface EnumToAttributeValue<T extends Enum<T>> {
    String getAttributeValue(T enumValue);
  }

  /**
   * Implements the most common custom mapping of an enum value to an attribute
   * value using the lower-case form of the enum name.
   */
  public static class LowerCaseEnumToAttributeValue<T extends Enum<T>>
      implements EnumToAttributeValue<T> {

    public String getAttributeValue(T enumValue) {
      return enumValue.name().toLowerCase();
    }
  }

  /**
   * Gets the value of an enumerated attribute and remove it from the list,
   * using a custom mapping of enum to attribute value.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required             indicates attribute is required
   * @param enumClass            enumeration class
   * @param defaultValue         the default value for an optional attribute
   *                             (used if not present)
   * @param enumToAttributeValue custom mapping of enum to attribute value
   * @return an enumerated value
   * @throws ParseException if required is set and the attribute is not defined,
   *                        or if the attribute value is not a valid enumerated
   *                        value
   */
  public <T extends Enum<T>> T consumeEnum(String name, boolean required,
      Class<T> enumClass, T defaultValue,
      EnumToAttributeValue<T> enumToAttributeValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }
    for (T enumValue : enumClass.getEnumConstants()) {
      if (enumToAttributeValue.getAttributeValue(enumValue).equals(value)) {
        return enumValue;
      }
    }
    ParseException pe = new ParseException(
        CoreErrorDomain.ERR.invalidAttributeValue);
    pe.setInternalReason("Invalid value for attribute : '" + name + "'");
    throw pe;
  }

  /**
   * Gets the value of an enumerated attribute and remove it from the list.
   *
   * Enumerated values are case-insensitive.
   *
   * @param name attribute name or <code>null</code> for text content
   * @param required indicates attribute is required
   * @param enumClass enumeration class
   * @param defaultValue the default value for an optional attribute (used
   *        if not present)
   * @return an enumerated value
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid
   *   enumerated value
   */
  public <T extends Enum<T>> T consumeEnum(String name, boolean required,
      Class<T> enumClass, T defaultValue)
      throws ParseException {
    String value = consume(name, required);
    if (value == null) {
      return defaultValue;
    }

    try {
      return Enum.valueOf(enumClass, value.toUpperCase());
    } catch (IllegalArgumentException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidAttributeValue, e);
     pe.setInternalReason("Invalid value for attribute : '" + name + "'");
     throw pe;
    }
  }

  /**
   * Gets the value of an enumerated attribute and remove it from the list.
   *
   * Enumerated values are case-insensitive.
   *
   * @param name attribute name
   * @param required indicates attribute is required
   * @param enumClass enumeration class
   * @return an enumerated value or null if not present
   * @exception ParseException if required is set and the attribute
   *   is not defined, or if the attribute value is not a valid
   *   enumerated value
   */
  public <T extends Enum<T>> T consumeEnum(String name, boolean required,
      Class<T> enumClass)
      throws ParseException {
    return consumeEnum(name, required, enumClass, null);
  }

  /**
   * Makes sure all attributes have been removed from the list.
   *
   * To all attribute in the default namespace must correspond exactly
   * one call to consume*().
   *
   * @exception ParseException if an attribute in the default namespace 
   *    hasn't been removed
   */
  public void assertAllConsumed() throws ParseException {
    StringBuffer message = new StringBuffer();
    if (!attrs.isEmpty()) {
      message.append("Unknown attribute");
      if (attrs.size() > 1) {
        message.append('s');
      }
      message.append(':');
      for (String name : attrs.keySet()) {
        message.append(" '");
        message.append(name);
        message.append("' ");
      }
    }
    if (!dups.isEmpty()) {
      message.append("Duplicate attribute");
      if (dups.size() > 1) {
        message.append('s');
      }
      message.append(':');
      for (String dup : dups) {
        message.append(" '");
        message.append(dup);
        message.append("' ");
      }
    }
    if (!contentConsumed && content != null && content.length() != 0) {
      message.append("Unexpected text content ");
    }
    if (message.length() != 0) {
      throw new ParseException(message.toString());
    }
  }

  /**
   * Sets the content.
   *
   * @param content element's text content
   */
  void setContent(String content) {
    // text content
    this.content = content == null ? null : content.trim();
  }

}
