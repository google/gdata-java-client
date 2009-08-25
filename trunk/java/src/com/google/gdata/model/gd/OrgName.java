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
 * Name of organization.
 *
 * 
 */
public class OrgName extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<String,
      OrgName> KEY = ElementKey.of(new QName(Namespaces.gNs, "orgName"),
      String.class, OrgName.class);

  /**
   * Yomi name of the organization.
   */
  public static final AttributeKey<String> YOMI = AttributeKey.of(
      new QName("yomi"));

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY).setContentRequired(true);
    builder.addAttribute(YOMI);
  }

  /**
   * Default mutable constructor.
   */
  public OrgName() {
    super(KEY);
  }

  /**
   * Lets subclasses create an instance using custom key.
   */
  protected OrgName(ElementKey<String, ? extends OrgName> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the
   * key for the element.
   *
   * @param key element key to use for this element.
   * @param source source element
   */
  protected OrgName(ElementKey<String, ? extends OrgName> key, Element source) {
    super(key, source);
  }

  /**
   * Constructs a new instance with the given value.
   *
   * @param value value.
   */
  public OrgName(String value) {
    this();
    setValue(value);
  }

  @Override
  public OrgName lock() {
    return (OrgName) super.lock();
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getValue() {
    return (String) super.getTextValue();
  }

  /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
  public void setValue(String value) {
    super.setTextValue(value);
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return super.hasTextValue();
  }

  /**
   * Returns the yomi name of organization.
   *
   * @return yomi name of organization
   */
  public String getYomi() {
    return getAttributeValue(YOMI);
  }

  /**
   * Sets the yomi name of organization.
   *
   * @param yomi yomi name of organization or <code>null</code> to reset
   */
  public void setYomi(String yomi) {
    setAttributeValue(YOMI, yomi);
  }

  /**
   * Returns whether it has the yomi name of organization.
   *
   * @return whether it has the yomi name of organization
   */
  public boolean hasYomi() {
    return getYomi() != null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    OrgName other = (OrgName) obj;
    return eq(getValue(), other.getValue());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getValue() != null) {
      result = 37 * result + getValue().hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{OrgName value=" + getTextValue() + "}";
  }
}
