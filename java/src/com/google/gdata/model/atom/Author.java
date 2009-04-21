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

import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
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
  
  /*
   * Generate the default metadata for this element.
   */
  static {
    DefaultRegistry.build(KEY);
  }
  
  /**
   * Constructs a new Author instance. Default metadata is associated with this
   * instance.
   */
  public Author() {
    super(DefaultRegistry.get(KEY));
  }
  
  /**
   * Lets subclasses create an instance using custom metadata.
   */
  protected Author(ElementMetadata<?, ?> elementMetadata) {
    super(elementMetadata);
  }
  
  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Person} instance. Will use the given {@link ElementMetadata} as the
   * metadata for the element.
   *
   * @param metadata metadata to use for this element.
   * @param source source element
   */
  public Author(ElementMetadata<Void, ? extends Author> metadata,
      Person source) {
    super(metadata, source);
  }

  /**
   * Constructs a new Author instance with the given name.
   */
  public Author(String name) {
    super(DefaultRegistry.get(KEY), name);
  }

  /**
   * Constructs a new author instance with the given name, uri, and email.
   */
  public Author(String name, URI uri, String email) {
    super(DefaultRegistry.get(KEY), name, uri, email);
  }
}
