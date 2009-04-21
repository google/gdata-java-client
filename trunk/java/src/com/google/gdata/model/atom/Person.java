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
import com.google.gdata.model.ContentModel.Cardinality;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
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

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY)
        .setCardinality(Cardinality.MULTIPLE);
    builder.addElement(NAME).setRequired(true);
    builder.addElement(URI);
    builder.addElement(EMAIL);
  }

  /**
   * Constructs a new Person instance. Default metadata without an ID is
   * associated with this instance.
   */
  public Person() {
    super(DefaultRegistry.get(KEY));
  }

  /**
   * Lets subclasses create an instance using custom metadata.
   *
   * @param elementMetadata metadata describing the expected attributes and
   *        child elements.
   */
  protected Person(ElementMetadata<?, ?> elementMetadata) {
    super(elementMetadata);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Author} instance. Will use the given {@link ElementMetadata} as the
   * metadata for the element.
   *
   * @param metadata metadata to use for this element.
   * @param source source element
   */
  public Person(ElementMetadata<Void, ? extends Person> metadata,
      Element source) {
    super(metadata, source);
  }

  /**
   * Constructs a new Person instance with the specified name
   * and metadata.
   */
  public Person(ElementMetadata<?, ?> elementMetadata, String name) {
    super(elementMetadata);
    if (name == null) {
      throw new NullPointerException("Name must have a value");
    }
    setName(name);
  }

  /**
   * Constructs a new Person instance with the specified name, URI,
   * and email address.
   *
   * @param elementMetadata element metadata
   * @param name person's name
   * @param uri person's URI
   * @param email person's email address
   */
  public Person(ElementMetadata<?, ?> elementMetadata, String name,
                URI uri, String email) {
    super(elementMetadata);
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
    addElement(NAME, new Element(DefaultRegistry.get(NAME)).setTextValue(v));
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
    if (!hasElement(URI)) {
      return null;
    }
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
    addElement(URI, new Element(DefaultRegistry.get(URI)).setTextValue(v));
  }

  /** Email address. */
  public String getEmail() {
    if (!hasElement(EMAIL)) {
      return null;
    }
    return getElementValue(EMAIL);
  }

  public void setEmail(String v) {
    addElement(EMAIL, new Element(DefaultRegistry.get(EMAIL)).setTextValue(v));
  }
}
