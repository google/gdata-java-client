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


package com.google.gdata.data.photos;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Simple value element for kml snippets.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.KML_ALIAS,
    nsUri = Namespaces.KML,
    localName = KmlSnippet.XML_NAME)
public class KmlSnippet extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "Snippet";

  /**
   * Default mutable constructor.
   */
  public KmlSnippet() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable kml snippet or <code>null</code> for a mutable kml
   *     snippet
   */
  public KmlSnippet(String value) {
    super(Namespaces.KML_NS, XML_NAME, null, value);
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
        ExtensionDescription.getDefaultDescription(KmlSnippet.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{KmlSnippet value=" + getValue() + "}";
  }

}
