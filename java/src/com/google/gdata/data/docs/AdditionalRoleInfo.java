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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;

import java.util.List;

/**
 * A collection of valid additional roles for a document type.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = AdditionalRoleInfo.XML_NAME)
public class AdditionalRoleInfo extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "additionalRoleInfo";

  /** XML "kind" attribute name */
  private static final String KIND = "kind";

  /** Kind */
  private String kind = null;

  /**
   * Default mutable constructor.
   */
  public AdditionalRoleInfo() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param kind kind.
   */
  public AdditionalRoleInfo(String kind) {
    super();
    setKind(kind);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(AdditionalRoleInfo.class)) {
      return;
    }
    extProfile.declare(AdditionalRoleInfo.class,
        AdditionalRoleSet.getDefaultDescription(false, true));
    new AdditionalRoleSet().declareExtensions(extProfile);
  }

  /**
   * Returns the additional role sets.
   *
   * @return additional role sets
   */
  public List<AdditionalRoleSet> getAdditionalRoleSets() {
    return getRepeatingExtension(AdditionalRoleSet.class);
  }

  /**
   * Adds a new additional role set.
   *
   * @param additionalRoleSet additional role set
   */
  public void addAdditionalRoleSet(AdditionalRoleSet additionalRoleSet) {
    getAdditionalRoleSets().add(additionalRoleSet);
  }

  /**
   * Returns whether it has the additional role sets.
   *
   * @return whether it has the additional role sets
   */
  public boolean hasAdditionalRoleSets() {
    return hasRepeatingExtension(AdditionalRoleSet.class);
  }

  /**
   * Returns the kind.
   *
   * @return kind
   */
  public String getKind() {
    return kind;
  }

  /**
   * Sets the kind.
   *
   * @param kind kind or <code>null</code> to reset
   */
  public void setKind(String kind) {
    throwExceptionIfImmutable();
    this.kind = kind;
  }

  /**
   * Returns whether it has the kind.
   *
   * @return whether it has the kind
   */
  public boolean hasKind() {
    return getKind() != null;
  }

  @Override
  protected void validate() {
    if (kind == null) {
      throwExceptionForMissingAttribute(KIND);
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
        ExtensionDescription.getDefaultDescription(AdditionalRoleInfo.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(KIND, kind);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    kind = helper.consume(KIND, true);
  }

  @Override
  public String toString() {
    return "{AdditionalRoleInfo kind=" + kind + "}";
  }

}

