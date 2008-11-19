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
 * Describes an open access.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = BooksNamespace.GBS_ALIAS,
    nsUri = BooksNamespace.GBS,
    localName = OpenAccess.XML_NAME)
public class OpenAccess extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "openAccess";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /** Programmatic value that describes whether the volume is in Open Access. */
  public static final class Value {

    /** Open access disabled. */
    public static final String DISABLED = BooksNamespace.GBS_PREFIX +
        "disabled";

    /** Open access enabled. */
    public static final String ENABLED = BooksNamespace.GBS_PREFIX + "enabled";

  }

  /**
   * Default mutable constructor.
   */
  public OpenAccess() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable programmatic value that describes whether the volume
   *     is in Open Access or <code>null</code> for a mutable programmatic value
   *     that describes whether the volume is in Open Access
   */
  public OpenAccess(String value) {
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
        ExtensionDescription.getDefaultDescription(OpenAccess.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{OpenAccess value=" + getValue() + "}";
  }

}
