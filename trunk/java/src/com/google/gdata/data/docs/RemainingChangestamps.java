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
 * Number of remaining changestamps.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = RemainingChangestamps.XML_NAME)
public class RemainingChangestamps extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "remainingChangestamps";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Value */
  private Long value = null;

  /**
   * Default mutable constructor.
   */
  public RemainingChangestamps() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param value value.
   */
  public RemainingChangestamps(Long value) {
    super();
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public Long getValue() {
    return value;
  }

  /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
  public void setValue(Long value) {
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
    if (value != null && value < 0) {
      throw new IllegalStateException("value attribute must be non-negative: " +
          value);
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
        ExtensionDescription.getDefaultDescription(RemainingChangestamps.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(VALUE, value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    value = helper.consumeLong(VALUE, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    RemainingChangestamps other = (RemainingChangestamps) obj;
    return eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{RemainingChangestamps value=" + value + "}";
  }

}

