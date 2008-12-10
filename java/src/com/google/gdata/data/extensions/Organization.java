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


package com.google.gdata.data.extensions;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * Describes an organization (like Company).
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = Organization.XML_NAME)
public class Organization extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "organization";

  /** XML "label" attribute name */
  private static final String LABEL = "label";

  /** XML "primary" attribute name */
  private static final String PRIMARY = "primary";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  /** Label */
  private String label = null;

  /** Whether this is the primary organization */
  private Boolean primary = null;

  /** Organization type */
  private String rel = null;

  /** Organization type. */
  public static final class Rel {

    /** Other organization. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work organization. */
    public static final String WORK = Namespaces.gPrefix + "work";

  }

  /**
   * Default mutable constructor.
   */
  public Organization() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param label label.
   * @param primary whether this is the primary organization.
   * @param rel organization type.
   */
  public Organization(String label, Boolean primary, String rel) {
    super();
    setLabel(label);
    setPrimary(primary);
    setRel(rel);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Organization.class)) {
      return;
    }
    extProfile.declare(Organization.class, OrgName.class);
    extProfile.declare(Organization.class, OrgTitle.class);
  }

  /**
   * Returns the label.
   *
   * @return label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label.
   *
   * @param label label or <code>null</code> to reset
   */
  public void setLabel(String label) {
    throwExceptionIfImmutable();
    this.label = label;
  }

  /**
   * Returns whether it has the label.
   *
   * @return whether it has the label
   */
  public boolean hasLabel() {
    return getLabel() != null;
  }

  /**
   * Returns the name of organization.
   *
   * @return name of organization
   */
  public OrgName getOrgName() {
    return getExtension(OrgName.class);
  }

  /**
   * Sets the name of organization.
   *
   * @param orgName name of organization or <code>null</code> to reset
   */
  public void setOrgName(OrgName orgName) {
    if (orgName == null) {
      removeExtension(OrgName.class);
    } else {
      setExtension(orgName);
    }
  }

  /**
   * Returns whether it has the name of organization.
   *
   * @return whether it has the name of organization
   */
  public boolean hasOrgName() {
    return hasExtension(OrgName.class);
  }

  /**
   * Returns the position in organization.
   *
   * @return position in organization
   */
  public OrgTitle getOrgTitle() {
    return getExtension(OrgTitle.class);
  }

  /**
   * Sets the position in organization.
   *
   * @param orgTitle position in organization or <code>null</code> to reset
   */
  public void setOrgTitle(OrgTitle orgTitle) {
    if (orgTitle == null) {
      removeExtension(OrgTitle.class);
    } else {
      setExtension(orgTitle);
    }
  }

  /**
   * Returns whether it has the position in organization.
   *
   * @return whether it has the position in organization
   */
  public boolean hasOrgTitle() {
    return hasExtension(OrgTitle.class);
  }

  /**
   * Returns the whether this is the primary organization.
   *
   * @return whether this is the primary organization
   */
  public Boolean getPrimary() {
    return primary;
  }

  /**
   * Sets the whether this is the primary organization.
   *
   * @param primary whether this is the primary organization or
   *     <code>null</code> to reset
   */
  public void setPrimary(Boolean primary) {
    throwExceptionIfImmutable();
    this.primary = primary;
  }

  /**
   * Returns whether it has the whether this is the primary organization.
   *
   * @return whether it has the whether this is the primary organization
   */
  public boolean hasPrimary() {
    return getPrimary() != null;
  }

  /**
   * Returns the organization type.
   *
   * @return organization type
   */
  public String getRel() {
    return rel;
  }

  /**
   * Sets the organization type.
   *
   * @param rel organization type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the organization type.
   *
   * @return whether it has the organization type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  @Override
  protected void validate() {
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
        ExtensionDescription.getDefaultDescription(Organization.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(LABEL, label);
    generator.put(PRIMARY, primary);
    generator.put(REL, rel);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    label = helper.consume(LABEL, false);
    primary = helper.consumeBoolean(PRIMARY, false);
    rel = helper.consume(REL, false);
  }

  @Override
  public String toString() {
    return "{Organization label=" + label + " primary=" + primary + " rel=" +
        rel + "}";
  }

}
