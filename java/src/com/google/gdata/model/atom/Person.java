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


package com.google.gdata.model.atom;

import com.google.gdata.data.IPerson;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Base class for Atom person constructs.
 */
public class Person extends Element implements IPerson {

  /**
   * The key for person constructs.
   */
  public static final ElementKey<Void, Person> KEY = ElementKey.of(
      null, Person.class);

  /**
   * The atom:email element.
   */
  public static final ElementKey<String, Element> EMAIL = ElementKey.of(
      new QName(Namespaces.atomNs, "email"));

  /**
   * The atom:name element.
   */
  public static final ElementKey<String, Element> NAME = ElementKey.of(
      new QName(Namespaces.atomNs, "name"));

  /**
   * The atom:uri.
   */
  public static final ElementKey<URI, Element> URI = ElementKey.of(
      new QName(Namespaces.atomNs, "uri"), URI.class, Element.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // Register the elements that are included directly
    registry.build(NAME);
    registry.build(URI);
    registry.build(EMAIL);

    // The builder for this element
    ElementCreator builder = registry.build(KEY)
        .setCardinality(Cardinality.MULTIPLE);

    // Local properties.
    builder.addElement(NAME).setRequired(true);
    builder.addElement(URI);
    builder.addElement(EMAIL);
  }

  /**
   * Constructs a new Person instance. Default metadata without an ID is
   * associated with this instance.
   */
  public Person() {
    super(KEY);
  }

  /**
   * Lets subclasses create an instance using custom metadata.
   *
   * @param key element key for this element
   */
  protected Person(ElementKey<?, ?> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the
   * key for the element.
   *
   * @param key the key to use for this element.
   * @param source source element
   */
  protected Person(ElementKey<?, ? extends Person> key, Element source) {
    super(key, source);
  }

  /**
   * Constructs a new Person instance with the specified name
   * and key.
   */
  public Person(ElementKey<?, ?> key, String name) {
    super(key);
    if (name == null) {
      throw new NullPointerException("Name must have a value");
    }
    setName(name);
  }

  /**
   * Constructs a new Person instance with the specified name, URI,
   * and email address.
   *
   * @param key the element key
   * @param name person's name
   * @param uri person's URI
   * @param email person's email address
   */
  public Person(ElementKey<?, ?> key, String name, URI uri, String email) {
    super(key);
    setName(name);
    setUri(uri);
    setEmail(email);
  }

  /** Human-readable name. */
  public String getName() {
    if (!hasElement(NAME)) {
      return null;
    }
    return getElementValue(NAME);
  }

  public void setName(String v) {
    setElement(NAME, (v == null) ? null : new Element(NAME).setTextValue(v));
  }

  /** Language of name. Derived from the current state of {@code xml:lang}. */
  public String getNameLang() {
    throw new UnsupportedOperationException("Not supported yet");
  }

  public void setNameLang(String v) {
    throw new UnsupportedOperationException("Not supported yet");
  }

  /**
   * Gets URI associated with the person.
   *
   * @deprecated Use {@link #getUriUri()} instead.
   *
   * @return URI
   */
  @Deprecated
  public String getUri() {
    URI uri = getUriUri();
    if (uri == null) {
      return null;
    }
    return uri.toString();
  }

  /**
   * Gets URI associated with the person.
   *
   * @return URI
   */
  public URI getUriUri() {
    return getElementValue(URI);
  }

  /**
   * Sets URI associated with the person.
   *
   * @deprecated Use {@link #setUri(URI)} instead.
   *
   * @param v URI
   */
  @Deprecated
  public void setUri(String v) {
    try {
      setUri(v == null ? null : new URI(v));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets URI associated with the person.
   *
   * @param v URI
   */
  public void setUri(URI v) {
    setElement(URI, (v == null) ? null : new Element(URI).setTextValue(v));
  }

  /** Email address. */
  public String getEmail() {
    return getElementValue(EMAIL);
  }

  public void setEmail(String v) {
    setElement(EMAIL, (v == null) ? null : new Element(EMAIL).setTextValue(v));
  }
}
