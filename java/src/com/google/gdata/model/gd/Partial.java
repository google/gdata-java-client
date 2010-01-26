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
 * Contains a partial resource representation.
 *
 * 
 */
public class Partial extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Partial> KEY = ElementKey.of(new QName(Namespaces.gNs, "partial"),
      Void.class, Partial.class);

  /**
   * Fields selection for this partial representation.
   */
  public static final AttributeKey<String> FIELDS = AttributeKey.of(new
      QName(null, "fields"), String.class);

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
    builder.addAttribute(FIELDS);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Partial() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Partial(ElementKey<?, ? extends Partial> key) {
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
  protected Partial(ElementKey<?, ? extends Partial> key, Element source) {
    super(key, source);
  }

  @Override
  public Partial lock() {
    return (Partial) super.lock();
  }

  /**
   * Returns the fields selection for this partial representation.
   *
   * @return fields selection for this partial representation
   */
  public String getFields() {
    return super.getAttributeValue(FIELDS);
  }

  /**
   * Sets the fields selection for this partial representation.
   *
   * @param fields fields selection for this partial representation or {@code
   *     null} to reset
   * @return this to enable chaining setters
   */
  public Partial setFields(String fields) {
    super.setAttributeValue(FIELDS, fields);
    return this;
  }

  /**
   * Returns whether it has the fields selection for this partial
   * representation.
   *
   * @return whether it has the fields selection for this partial representation
   */
  public boolean hasFields() {
    return super.hasAttribute(FIELDS);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Partial other = (Partial) obj;
    return eq(getFields(), other.getFields());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getFields() != null) {
      result = 37 * result + getFields().hashCode();
    }
    return result;
  }
}


