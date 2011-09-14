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


package com.google.gdata.data.contacts;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

/**
 * Element that if present marks that a group is a system one.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ContactsNamespace.GCONTACT_ALIAS,
    nsUri = ContactsNamespace.GCONTACT,
    localName = SystemGroup.XML_NAME)
public class SystemGroup extends ValueConstruct {

  /** XML element name */
  static final String XML_NAME = "systemGroup";

  /** XML "id" attribute name */
  private static final String ID = "id";

  /**
   * Default mutable constructor.
   */
  public SystemGroup() {
    this(null);
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param id immutable identifier for distinguishing various system groups or
   *     <code>null</code> for a mutable identifier for distinguishing various
   *     system groups
   */
  public SystemGroup(String id) {
    super(ContactsNamespace.GCONTACT_NS, XML_NAME, ID, id);
  }

  /**
   * Returns the identifier for distinguishing various system groups.
   *
   * @return identifier for distinguishing various system groups
   */
  public String getId() {
    return getValue();
  }

  /**
   * Sets the identifier for distinguishing various system groups.
   *
   * @param id identifier for distinguishing various system groups or
   *     <code>null</code> to reset
   */
  public void setId(String id) {
    setValue(id);
  }

  /**
   * Returns whether it has the identifier for distinguishing various system
   * groups.
   *
   * @return whether it has the identifier for distinguishing various system
   *     groups
   */
  public boolean hasId() {
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
        ExtensionDescription.getDefaultDescription(SystemGroup.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{SystemGroup id=" + getValue() + "}";
  }

}

