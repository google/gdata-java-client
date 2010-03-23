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

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Maximum upload file size.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = MaxUploadSize.XML_NAME)
public class MaxUploadSize extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "maxUploadSize";

  /** XML "kind" attribute name */
  private static final String KIND = "kind";

  /** Kind */
  private String kind = null;

  /** Value */
  private Long value = null;

  /**
   * Default mutable constructor.
   */
  public MaxUploadSize() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param kind kind.
   * @param value value.
   */
  public MaxUploadSize(String kind, Long value) {
    super();
    setKind(kind);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the kind.
   *
   * @return kind
   */
  public String getKind() {
    return kind;
  }

  /**
   * Sets the kind.
   *
   * @param kind kind or <code>null</code> to reset
   */
  public void setKind(String kind) {
    throwExceptionIfImmutable();
    this.kind = kind;
  }

  /**
   * Returns whether it has the kind.
   *
   * @return whether it has the kind
   */
  public boolean hasKind() {
    return getKind() != null;
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
    if (kind == null) {
      throwExceptionForMissingAttribute(KIND);
    }
    if (value == null) {
      throw new IllegalStateException("Missing text content");
    } else if (value < 0) {
      throw new IllegalStateException("Text content must be non-negative: " +
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
        ExtensionDescription.getDefaultDescription(MaxUploadSize.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(KIND, kind);
    generator.setContent(value.toString());
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    kind = helper.consume(KIND, true);
    value = helper.consumeLong(null, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    MaxUploadSize other = (MaxUploadSize) obj;
    return eq(kind, other.kind)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (kind != null) {
      result = 37 * result + kind.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{MaxUploadSize kind=" + kind + " value=" + value + "}";
  }

}

