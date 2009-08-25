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

import com.google.gdata.model.Element;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

import java.net.URI;

/**
 * Author element, modifies {@link Person} to add author QName.
 *
 * 
 */
public class Author extends Person {

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<Void, Author> KEY = ElementKey.of(
      new QName(Namespaces.atomNs, "author"), Author.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // Register superclass metadata.
    Person.registerMetadata(registry);

    registry.build(KEY);
  }

  /**
   * Constructs a new Author instance. The default key is associated with this
   * instance.
   */
  public Author() {
    super(KEY);
  }

  /**
   * Lets subclasses create an instance using a custom key.
   */
  protected Author(ElementKey<?, ?> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the
   * key for the element.
   *
   * @param key the element key to use for this element.
   * @param source source element
   */
  protected Author(ElementKey<?, ? extends Author> key, Element source) {
    super(key, source);
  }

  /**
   * Constructs a new Author instance with the given name.
   */
  public Author(String name) {
    super(KEY, name);
  }

  /**
   * Constructs a new author instance with the given name, uri, and email.
   */
  public Author(String name, URI uri, String email) {
    super(KEY, name, uri, email);
  }
}
