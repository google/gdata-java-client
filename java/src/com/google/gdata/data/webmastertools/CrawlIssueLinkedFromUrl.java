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
 * Source URL that links to the issue URL.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = CrawlIssueLinkedFromUrl.XML_NAME)
public class CrawlIssueLinkedFromUrl extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "linked-from";

  /**
   * Default mutable constructor.
   */
  public CrawlIssueLinkedFromUrl() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param linkedFromUrl immutable value or <code>null</code> for a mutable
   *     value
   */
  public CrawlIssueLinkedFromUrl(String linkedFromUrl) {
    super(Namespaces.WT_NS, XML_NAME, null, linkedFromUrl);
    setRequired(false);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getLinkedFromUrl() {
    return getValue();
  }

  /**
   * Sets the value.
   *
   * @param linkedFromUrl value or <code>null</code> to reset
   */
  public void setLinkedFromUrl(String linkedFromUrl) {
    setValue(linkedFromUrl);
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasLinkedFromUrl() {
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
        ExtensionDescription.getDefaultDescription(
        CrawlIssueLinkedFromUrl.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{CrawlIssueLinkedFromUrl linkedFromUrl=" + getValue() + "}";
  }

}

