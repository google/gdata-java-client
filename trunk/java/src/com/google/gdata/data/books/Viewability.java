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


package com.google.gdata.data.books;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Describes a viewability.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = BooksNamespace.GBS_ALIAS,
    nsUri = BooksNamespace.GBS,
    localName = Viewability.XML_NAME)
public class Viewability extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "viewability";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Programmatic value that describes the viewability of a volume in Google
   * Book Search. */
  public static final class Value {

    /** Full viewability. */
    public static final String VIEW_ALL_PAGES = BooksNamespace.GBS_PREFIX +
        "view_all_pages";

    /** No viewability. */
    public static final String VIEW_NO_PAGES = BooksNamespace.GBS_PREFIX +
        "view_no_pages";

    /** Partial viewability. */
    public static final String VIEW_PARTIAL = BooksNamespace.GBS_PREFIX +
        "view_partial";

    /** Unknown viewability. */
    public static final String VIEW_UNKNOWN = BooksNamespace.GBS_PREFIX +
        "view_unknown";

  }

  /**
   * Default mutable constructor.
   */
  public Viewability() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable programmatic value that describes the viewability of
   *     a volume in Google Book Search or <code>null</code> for a mutable
   *     programmatic value that describes the viewability of a volume in Google
   *     Book Search
   */
  public Viewability(String value) {
    super(BooksNamespace.GBS_NS, XML_NAME, VALUE, value);
    setRequired(false);
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
        ExtensionDescription.getDefaultDescription(Viewability.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{Viewability value=" + getValue() + "}";
  }

}
