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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * A keyword in a site or in a link to a site.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = Keyword.XML_NAME)
public class Keyword extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "keyword";

  /** XML "source" attribute name */
  private static final String SOURCE = "source";

  /** Source */
  private String source = null;

  /** Value */
  private String value = null;

  /** Source. */
  public static final class Source {

    /** The keyword is from a link to the site. */
    public static final String EXTERNAL = "external";

    /** The keyword is internal to the site. */
    public static final String INTERNAL = "internal";

  }

  /**
   * Default mutable constructor.
   */
  public Keyword() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param source source.
   * @param value value.
   */
  public Keyword(String source, String value) {
    super();
    setSource(source);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the source.
   *
   * @return source
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the source.
   *
   * @param source source or <code>null</code> to reset
   */
  public void setSource(String source) {
    throwExceptionIfImmutable();
    this.source = source;
  }

  /**
   * Returns whether it has the source.
   *
   * @return whether it has the source
   */
  public boolean hasSource() {
    return getSource() != null;
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
    if (source == null) {
      throwExceptionForMissingAttribute(SOURCE);
    }
    if (value == null) {
      throw new IllegalStateException("Missing text content");
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
        ExtensionDescription.getDefaultDescription(Keyword.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(SOURCE, source);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    source = helper.consume(SOURCE, true);
    value = helper.consume(null, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Keyword other = (Keyword) obj;
    return eq(source, other.source)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (source != null) {
      result = 37 * result + source.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Keyword source=" + source + " value=" + value + "}";
  }

}

