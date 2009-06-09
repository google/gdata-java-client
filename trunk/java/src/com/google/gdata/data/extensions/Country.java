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

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * The country name.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = Country.XML_NAME)
public class Country extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "country";

  /** XML "code" attribute name */
  private static final String CODE = "code";

  /** The 3166-1 alpha-2 country code */
  private String code = null;

  /** Value */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public Country() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param code The 3166-1 alpha-2 country code.
   * @param value value.
   */
  public Country(String code, String value) {
    super();
    setCode(code);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the The 3166-1 alpha-2 country code.
   *
   * @return The 3166-1 alpha-2 country code
   */
  public String getCode() {
    return code;
  }

  /**
   * Sets the The 3166-1 alpha-2 country code.
   *
   * @param code The 3166-1 alpha-2 country code or <code>null</code> to reset
   */
  public void setCode(String code) {
    throwExceptionIfImmutable();
    this.code = code;
  }

  /**
   * Returns whether it has the The 3166-1 alpha-2 country code.
   *
   * @return whether it has the The 3166-1 alpha-2 country code
   */
  public boolean hasCode() {
    return getCode() != null;
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
        ExtensionDescription.getDefaultDescription(Country.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(CODE, code);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    code = helper.consume(CODE, false);
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
    Country other = (Country) obj;
    return eq(code, other.code)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (code != null) {
      result = 37 * result + code.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Country code=" + code + " value=" + value + "}";
  }

}
