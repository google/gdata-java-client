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
 * Describes the base set of values for event transparency.
 *
 * 
 */
public class Transparency extends Element {

  /** Value. */
  public static final class Value {

    /** Indicates event consumes time on calendar; event time will be marked as
     * busy in a free/busy search. */
    public static final String OPAQUE = Namespaces.gPrefix + "event.opaque";

    /** Indicates event doesn't consume any time on calendar; event time will
     * not be marked as busy in a free/busy search. */
    public static final String TRANSPARENT = Namespaces.gPrefix +
        "event.transparent";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      OPAQUE,
      TRANSPARENT};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Value() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Transparency> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "transparency"), Void.class, Transparency.class);

  /**
   * Value.
   */
  public static final AttributeKey<String> VALUE = AttributeKey.of(new
      QName(null, "value"), String.class);

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
    builder.addAttribute(VALUE).setRequired(true);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Transparency() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Transparency(ElementKey<?, ? extends Transparency> key) {
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
  protected Transparency(ElementKey<?, ? extends Transparency> key,
      Element source) {
    super(key, source);
  }

  @Override
  public Transparency lock() {
    return (Transparency) super.lock();
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getValue() {
    return super.getAttributeValue(VALUE);
  }

  /**
   * Sets the value.
   *
   * @param value value or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Transparency setValue(String value) {
    super.setAttributeValue(VALUE, value);
    return this;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasValue() {
    return super.hasAttribute(VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Transparency other = (Transparency) obj;
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
}


