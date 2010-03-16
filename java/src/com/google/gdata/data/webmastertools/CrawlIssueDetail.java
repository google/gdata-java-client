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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Detail of the crawl issue.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = CrawlIssueDetail.XML_NAME)
public class CrawlIssueDetail extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "detail";

  /**
   * Default mutable constructor.
   */
  public CrawlIssueDetail() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param detail immutable value or <code>null</code> for a mutable value
   */
  public CrawlIssueDetail(String detail) {
    super(Namespaces.WT_NS, XML_NAME, null, detail);
    setRequired(false);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getDetail() {
    return getValue();
  }

  /**
   * Sets the value.
   *
   * @param detail value or <code>null</code> to reset
   */
  public void setDetail(String detail) {
    setValue(detail);
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasDetail() {
    return hasValue();
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
        ExtensionDescription.getDefaultDescription(CrawlIssueDetail.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{CrawlIssueDetail detail=" + getValue() + "}";
  }

}

