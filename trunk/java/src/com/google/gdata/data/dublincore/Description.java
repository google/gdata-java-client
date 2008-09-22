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


package com.google.gdata.data.dublincore;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Account of the resource.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DublincoreNamespace.DC_ALIAS,
    nsUri = DublincoreNamespace.DC,
    localName = Description.XML_NAME)
public class Description extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "description";

  /**
   * Default mutable constructor.
   */
  public Description() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable value or <code>null</code> for a mutable value
   */
  public Description(String value) {
    super(DublincoreNamespace.DC_NS, XML_NAME, null, value);
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
        ExtensionDescription.getDefaultDescription(Description.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{Description value=" + getValue() + "}";
  }

}
