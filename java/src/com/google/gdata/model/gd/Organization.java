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


package com.google.gdata.model.gd;

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes an organization (like Company).
 *
 * 
 */
public class Organization extends Element {

  /** Organization type. */
  public static final class Rel {

    /** Other organization. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work organization. */
    public static final String WORK = Namespaces.gPrefix + "work";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      OTHER,
      WORK};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Rel() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Organization> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "organization"), Void.class, Organization.class);

  /**
   * Label.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * Whether this is the primary organization.
   */
  public static final AttributeKey<Boolean> PRIMARY = AttributeKey.of(new
      QName(null, "primary"), Boolean.class);

  /**
   * Organization type.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY);
    builder.addAttribute(LABEL);
    builder.addAttribute(PRIMARY);
    builder.addAttribute(REL);
    builder.addElement(OrgDepartment.KEY);
    builder.addElement(OrgJobDescription.KEY);
    builder.addElement(OrgName.KEY);
    builder.addElement(OrgSymbol.KEY);
    builder.addElement(OrgTitle.KEY);
    builder.addElement(Where.KEY);
  }

  /**
   * Default mutable constructor.
   */
  public Organization() {
    super(KEY);
  }

  /**
   * Lets subclasses create an instance using a custom key.
   */
  protected Organization(ElementKey<?, ? extends Organization> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the
   * key for the element.
   *
   * @param key key to use for this element.
   * @param source source element
   */
  protected Organization(ElementKey<?, ? extends Organization> key,
      Element source) {
    super(key, source);
  }

  @Override
  public Organization lock() {
    return (Organization) super.lock();
  }

  /**
   * Returns the label.
   *
   * @return label
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the label.
   *
   * @param label label or <code>null</code> to reset
   */
  public void setLabel(String label) {
    super.setAttributeValue(LABEL, label);
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
   * Returns the department name in organization.
   *
   * @return department name in organization
   */
  public OrgDepartment getOrgDepartment() {
    return super.getElement(OrgDepartment.KEY);
  }

  /**
   * Sets the department name in organization.
   *
   * @param orgDepartment department name in organization or <code>null</code>
   *     to reset
   */
  public void setOrgDepartment(OrgDepartment orgDepartment) {
    super.setElement(OrgDepartment.KEY, orgDepartment);
  }

  /**
   * Returns whether it has the department name in organization.
   *
   * @return whether it has the department name in organization
   */
  public boolean hasOrgDepartment() {
    return super.hasElement(OrgDepartment.KEY);
  }

  /**
   * Returns the job description.
   *
   * @return job description
   */
  public OrgJobDescription getOrgJobDescription() {
    return super.getElement(OrgJobDescription.KEY);
  }

  /**
   * Sets the job description.
   *
   * @param orgJobDescription job description or <code>null</code> to reset
   */
  public void setOrgJobDescription(OrgJobDescription orgJobDescription) {
    super.setElement(OrgJobDescription.KEY, orgJobDescription);
  }

  /**
   * Returns whether it has the job description.
   *
   * @return whether it has the job description
   */
  public boolean hasOrgJobDescription() {
    return super.hasElement(OrgJobDescription.KEY);
  }

  /**
   * Returns the name of organization.
   *
   * @return name of organization
   */
  public OrgName getOrgName() {
    return super.getElement(OrgName.KEY);
  }

  /**
   * Sets the name of organization.
   *
   * @param orgName name of organization or <code>null</code> to reset
   */
  public void setOrgName(OrgName orgName) {
    super.setElement(OrgName.KEY, orgName);
  }

  /**
   * Returns whether it has the name of organization.
   *
   * @return whether it has the name of organization
   */
  public boolean hasOrgName() {
    return super.hasElement(OrgName.KEY);
  }

  /**
   * Returns the organization symbol/ticker.
   *
   * @return organization symbol/ticker
   */
  public OrgSymbol getOrgSymbol() {
    return super.getElement(OrgSymbol.KEY);
  }

  /**
   * Sets the organization symbol/ticker.
   *
   * @param orgSymbol organization symbol/ticker or <code>null</code> to reset
   */
  public void setOrgSymbol(OrgSymbol orgSymbol) {
    super.setElement(OrgSymbol.KEY, orgSymbol);
  }

  /**
   * Returns whether it has the organization symbol/ticker.
   *
   * @return whether it has the organization symbol/ticker
   */
  public boolean hasOrgSymbol() {
    return super.hasElement(OrgSymbol.KEY);
  }

  /**
   * Returns the position in organization.
   *
   * @return position in organization
   */
  public OrgTitle getOrgTitle() {
    return super.getElement(OrgTitle.KEY);
  }

  /**
   * Sets the position in organization.
   *
   * @param orgTitle position in organization or <code>null</code> to reset
   */
  public void setOrgTitle(OrgTitle orgTitle) {
    super.setElement(OrgTitle.KEY, orgTitle);
  }

  /**
   * Returns whether it has the position in organization.
   *
   * @return whether it has the position in organization
   */
  public boolean hasOrgTitle() {
    return super.hasElement(OrgTitle.KEY);
  }

  /**
   * Returns the whether this is the primary organization.
   *
   * @return whether this is the primary organization
   */
  public Boolean getPrimary() {
    return super.getAttributeValue(PRIMARY);
  }

  /**
   * Sets the whether this is the primary organization.
   *
   * @param primary whether this is the primary organization or
   *     <code>null</code> to reset
   */
  public void setPrimary(Boolean primary) {
    super.setAttributeValue(PRIMARY, primary);
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
    return getAttributeValue(REL);
  }

  /**
   * Sets the organization type.
   *
   * @param rel organization type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    super.setAttributeValue(REL, rel);
  }

  /**
   * Returns whether it has the organization type.
   *
   * @return whether it has the organization type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  /**
   * Returns the office location.
   *
   * @return office location
   */
  public Where getWhere() {
    return super.getElement(Where.KEY);
  }

  /**
   * Sets the office location.
   *
   * @param where office location or <code>null</code> to reset
   */
  public void setWhere(Where where) {
    super.setElement(Where.KEY, where);
  }

  /**
   * Returns whether it has the office location.
   *
   * @return whether it has the office location
   */
  public boolean hasWhere() {
    return super.hasElement(Where.KEY);
  }

  @Override
  public String toString() {
    return "{Organization label=" + getAttributeValue(LABEL) + " primary=" +
        getAttributeValue(PRIMARY) + " rel=" + getAttributeValue(REL) + "}";
  }
}
