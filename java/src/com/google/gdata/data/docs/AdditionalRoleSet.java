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


package com.google.gdata.data.docs;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.acl.AdditionalRole;
import com.google.gdata.util.ParseException;

import java.util.List;

/**
 * A set of valid additional roles for a primary role within a document type.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = AdditionalRoleSet.XML_NAME)
public class AdditionalRoleSet extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "additionalRoleSet";

  /** XML "primaryRole" attribute name */
  private static final String PRIMARYROLE = "primaryRole";

  /** PrimaryRole */
  private String primaryRole = null;

  /**
   * Default mutable constructor.
   */
  public AdditionalRoleSet() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param primaryRole primaryRole.
   */
  public AdditionalRoleSet(String primaryRole) {
    super();
    setPrimaryRole(primaryRole);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(AdditionalRoleSet.class)) {
      return;
    }
    extProfile.declare(AdditionalRoleSet.class,
        new ExtensionDescription(AdditionalRole.class, new XmlNamespace("gAcl",
        "http://schemas.google.com/acl/2007"), "additionalRole", false, true,
        false));
  }

  /**
   * Returns the additional roles.
   *
   * @return additional roles
   */
  public List<AdditionalRole> getAdditionalRoles() {
    return getRepeatingExtension(AdditionalRole.class);
  }

  /**
   * Adds a new additional role.
   *
   * @param additionalRole additional role
   */
  public void addAdditionalRole(AdditionalRole additionalRole) {
    getAdditionalRoles().add(additionalRole);
  }

  /**
   * Returns whether it has the additional roles.
   *
   * @return whether it has the additional roles
   */
  public boolean hasAdditionalRoles() {
    return hasRepeatingExtension(AdditionalRole.class);
  }

  /**
   * Returns the primaryRole.
   *
   * @return primaryRole
   */
  public String getPrimaryRole() {
    return primaryRole;
  }

  /**
   * Sets the primaryRole.
   *
   * @param primaryRole primaryRole or <code>null</code> to reset
   */
  public void setPrimaryRole(String primaryRole) {
    throwExceptionIfImmutable();
    this.primaryRole = primaryRole;
  }

  /**
   * Returns whether it has the primaryRole.
   *
   * @return whether it has the primaryRole
   */
  public boolean hasPrimaryRole() {
    return getPrimaryRole() != null;
  }

  @Override
  protected void validate() {
    if (primaryRole == null) {
      throwExceptionForMissingAttribute(PRIMARYROLE);
    }
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
        ExtensionDescription.getDefaultDescription(AdditionalRoleSet.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(PRIMARYROLE, primaryRole);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    primaryRole = helper.consume(PRIMARYROLE, true);
  }

  @Override
  public String toString() {
    return "{AdditionalRoleSet primaryRole=" + primaryRole + "}";
  }

}

