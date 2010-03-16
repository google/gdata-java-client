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
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Detection date for the issue.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = CrawlIssueDateDetected.XML_NAME)
public class CrawlIssueDateDetected extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "date-detected";

  /** Value */
  private DateTime dateDetected = null;

  /**
   * Default mutable constructor.
   */
  public CrawlIssueDateDetected() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param dateDetected value.
   */
  public CrawlIssueDateDetected(DateTime dateDetected) {
    super();
    setDateDetected(dateDetected);
    setImmutable(true);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public DateTime getDateDetected() {
    return dateDetected;
  }

  /**
   * Sets the value.
   *
   * @param dateDetected value or <code>null</code> to reset
   */
  public void setDateDetected(DateTime dateDetected) {
    throwExceptionIfImmutable();
    this.dateDetected = dateDetected;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasDateDetected() {
    return getDateDetected() != null;
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
        ExtensionDescription.getDefaultDescription(
        CrawlIssueDateDetected.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.setContent(dateDetected.toString());
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    dateDetected = helper.consumeDateTime(null, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    CrawlIssueDateDetected other = (CrawlIssueDateDetected) obj;
    return eq(dateDetected, other.dateDetected);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (dateDetected != null) {
      result = 37 * result + dateDetected.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{CrawlIssueDateDetected dateDetected=" + dateDetected + "}";
  }

}

