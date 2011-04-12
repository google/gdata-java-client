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
 * Describes the role of an entry in an access control list.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = AclNamespace.gAclAlias,
    nsUri = AclNamespace.gAcl,
    localName = AclRole.ROLE)
public class AclRole extends ValueConstruct {

  /**
   * predefined value "none" to indicate that a user has no rights
   */
  public static final AclRole NONE = new AclRole("none");

  /**
   * predefined value "peeker" to indicate that a user has the right to know of
   * the existance of the controlled object
   */
  public static final AclRole PEEKER = new AclRole("peeker");

  /**
   * predefined value "reader" to indicate that a user has the right to read the
   * controlled object
   */
  public static final AclRole READER = new AclRole("reader");

  /**
   * predefined value "none" to indicate that a user has the right to read and
   * modify the controlled object
   */
  public static final AclRole WRITER = new AclRole("writer");

  /**
   * predefined value "owner" to indicate that a user has the right to read,
   * modify and delete the controlled object
   */
  public static final AclRole OWNER = new AclRole("owner");

  /**
   * Predefined value 'commenter' to indicate that a user has the right to
   * comment on the controlled object.
   */
  public static final AclRole COMMENTER = new AclRole("commenter");

  /** XML "role" element name */
  static final String ROLE = "role";

  /** XML "value" attribute name */
  private static final String VALUE = "value";

  /**
   * Default constructor for a mutable role.
   */
  public AclRole() {
    this(null);
  }

  /**
   * Constructor for either a mutable or immutable role.
   *
   * @param value immutable value to use for this role or <code>null</code> for
   *              a mutable role.
   */
  public AclRole(String value) {
    super(AclNamespace.gAclNs, ROLE, VALUE, value);
  }
}
