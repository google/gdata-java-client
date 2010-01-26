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

import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Name of a person in a structured form.
 *
 * 
 */
public class Name extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Name> KEY = ElementKey.of(new QName(Namespaces.gNs, "name"), Void.class,
      Name.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addElement(AdditionalName.KEY);
    builder.addElement(FamilyName.KEY);
    builder.addElement(FullName.KEY);
    builder.addElement(GivenName.KEY);
    builder.addElement(NamePrefix.KEY);
    builder.addElement(NameSuffix.KEY);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Name() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Name(ElementKey<?, ? extends Name> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected Name(ElementKey<?, ? extends Name> key, Element source) {
    super(key, source);
  }

  @Override
  public Name lock() {
    return (Name) super.lock();
  }

  /**
   * Returns the additional name.
   *
   * @return additional name
   */
  public AdditionalName getAdditionalName() {
    return super.getElement(AdditionalName.KEY);
  }

  /**
   * Sets the additional name.
   *
   * @param additionalName additional name or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Name setAdditionalName(AdditionalName additionalName) {
    super.setElement(AdditionalName.KEY, additionalName);
    return this;
  }

  /**
   * Returns whether it has the additional name.
   *
   * @return whether it has the additional name
   */
  public boolean hasAdditionalName() {
    return super.hasElement(AdditionalName.KEY);
  }

  /**
   * Returns the family name.
   *
   * @return family name
   */
  public FamilyName getFamilyName() {
    return super.getElement(FamilyName.KEY);
  }

  /**
   * Sets the family name.
   *
   * @param familyName family name or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Name setFamilyName(FamilyName familyName) {
    super.setElement(FamilyName.KEY, familyName);
    return this;
  }

  /**
   * Returns whether it has the family name.
   *
   * @return whether it has the family name
   */
  public boolean hasFamilyName() {
    return super.hasElement(FamilyName.KEY);
  }

  /**
   * Returns the full name.
   *
   * @return full name
   */
  public FullName getFullName() {
    return super.getElement(FullName.KEY);
  }

  /**
   * Sets the full name.
   *
   * @param fullName full name or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Name setFullName(FullName fullName) {
    super.setElement(FullName.KEY, fullName);
    return this;
  }

  /**
   * Returns whether it has the full name.
   *
   * @return whether it has the full name
   */
  public boolean hasFullName() {
    return super.hasElement(FullName.KEY);
  }

  /**
   * Returns the given name.
   *
   * @return given name
   */
  public GivenName getGivenName() {
    return super.getElement(GivenName.KEY);
  }

  /**
   * Sets the given name.
   *
   * @param givenName given name or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Name setGivenName(GivenName givenName) {
    super.setElement(GivenName.KEY, givenName);
    return this;
  }

  /**
   * Returns whether it has the given name.
   *
   * @return whether it has the given name
   */
  public boolean hasGivenName() {
    return super.hasElement(GivenName.KEY);
  }

  /**
   * Returns the name prefix.
   *
   * @return name prefix
   */
  public NamePrefix getNamePrefix() {
    return super.getElement(NamePrefix.KEY);
  }

  /**
   * Sets the name prefix.
   *
   * @param namePrefix name prefix or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Name setNamePrefix(NamePrefix namePrefix) {
    super.setElement(NamePrefix.KEY, namePrefix);
    return this;
  }

  /**
   * Returns whether it has the name prefix.
   *
   * @return whether it has the name prefix
   */
  public boolean hasNamePrefix() {
    return super.hasElement(NamePrefix.KEY);
  }

  /**
   * Returns the name suffix.
   *
   * @return name suffix
   */
  public NameSuffix getNameSuffix() {
    return super.getElement(NameSuffix.KEY);
  }

  /**
   * Sets the name suffix.
   *
   * @param nameSuffix name suffix or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Name setNameSuffix(NameSuffix nameSuffix) {
    super.setElement(NameSuffix.KEY, nameSuffix);
    return this;
  }

  /**
   * Returns whether it has the name suffix.
   *
   * @return whether it has the name suffix
   */
  public boolean hasNameSuffix() {
    return super.hasElement(NameSuffix.KEY);
  }

}


