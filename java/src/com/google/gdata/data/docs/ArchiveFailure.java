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
 * Path of the failed document.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = ArchiveFailure.XML_NAME)
public class ArchiveFailure extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "archiveFailure";

  /** XML "reason" attribute name */
  private static final String REASON = "reason";

  /** Reason */
  private String reason = null;

  /** Value */
  private String value = null;

  /**
   * Default mutable constructor.
   */
  public ArchiveFailure() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param reason reason.
   * @param value value.
   */
  public ArchiveFailure(String reason, String value) {
    super();
    setReason(reason);
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the reason.
   *
   * @return reason
   */
  public String getReason() {
    return reason;
  }

  /**
   * Sets the reason.
   *
   * @param reason reason or <code>null</code> to reset
   */
  public void setReason(String reason) {
    throwExceptionIfImmutable();
    this.reason = reason;
  }

  /**
   * Returns whether it has the reason.
   *
   * @return whether it has the reason
   */
  public boolean hasReason() {
    return getReason() != null;
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
        ExtensionDescription.getDefaultDescription(ArchiveFailure.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(REASON, reason);
    generator.setContent(value);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    reason = helper.consume(REASON, false);
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
    ArchiveFailure other = (ArchiveFailure) obj;
    return eq(reason, other.reason)
        && eq(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (reason != null) {
      result = 37 * result + reason.hashCode();
    }
    if (value != null) {
      result = 37 * result + value.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{ArchiveFailure reason=" + reason + " value=" + value + "}";
  }

}

