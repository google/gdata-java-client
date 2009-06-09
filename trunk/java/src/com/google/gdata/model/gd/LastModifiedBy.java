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

import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.QName;
import com.google.gdata.model.atom.Person;
import com.google.gdata.util.Namespaces;

/**
 * The user who last modified the object.
 *
 * 
 */
public class LastModifiedBy extends Person {

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<Void,
      LastModifiedBy> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "lastModifiedBy"), Void.class, LastModifiedBy.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
  }

  /**
   * Default mutable constructor.
   */
  public LastModifiedBy() {
    this(KEY);
  }

  /**
   * Create an instance using a different key.
   */
  public LastModifiedBy(ElementKey<Void, ? extends LastModifiedBy> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Person} instance. Will use the given {@link ElementKey} as the key
   * for the element.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  public LastModifiedBy(ElementKey<Void, ? extends LastModifiedBy> key,
      Person source) {
    super(key, source);
  }

   @Override
   public LastModifiedBy lock() {
     return (LastModifiedBy) super.lock();
   }

  @Override
  public String toString() {
    return "{LastModifiedBy " + super.toString() + "}";
  }

}
