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


package com.google.gdata.data.acl;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Describes an additional role for an entry in an access control list.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AclNamespace.gAclAlias,
    nsUri = AclNamespace.gAcl,
    localName = AdditionalRole.XML_NAME)
public class AdditionalRole extends ValueConstruct {

  /**
   * Predefined value 'appender' to indicate that a user has the right to add
   * to the controlled object.
   */
  public static final AdditionalRole APPENDER = new AdditionalRole("appender");

  /**
   * Predefined value 'commenter' to indicate that a user has the right to
   * comment on the controlled object.
   */
  public static final AdditionalRole COMMENTER = new AdditionalRole("commenter");

  /**
   * Predefined value 'executer' to indicate that a user has the right to
   * execute the controlled object.
   */
  public static final AdditionalRole EXECUTER = new AdditionalRole("executer");

  /** XML element name */
  static final String XML_NAME = "additionalRole";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /**
   * Default mutable constructor.
   */
  public AdditionalRole() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param value immutable value or <code>null</code> for a mutable value
   */
  public AdditionalRole(String value) {
    super(AclNamespace.gAclNs, XML_NAME, VALUE, value);
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
        ExtensionDescription.getDefaultDescription(AdditionalRole.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }
}

