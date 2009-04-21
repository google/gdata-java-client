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


package com.google.gdata.model.atompub;

import com.google.common.collect.Maps;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

import java.util.Map;

/**
 * Value of the app:draft tag.
 *
 * 
 */
public class Draft extends Element {

  /** Value. */
  public enum Value {

    /** Is not a draft. */
    NO,

    /** Is a draft. */
    YES;

    /**
     * Returns the string representation of this instance, suitable for use in
     * output. This is a lowercase version of the name.
     */
    @Override
    public String toString() {
      return name().toLowerCase();
    }

    private static final Map<String, Value> VALUE_MAP = Maps.newHashMap();
    static {
      for (Value value : Value.values()) {
        VALUE_MAP.put(value.toString(), value);
      }
    }

    /**
     * Convert from a string representation of Value to an instance. Unlike
     * {@code valueOf(String)} this method will return null if an enum for the
     * given value does not exist.
     *
     * @param value the string representation to look up.
     * @return an instance if one matched the given string, or {@code null} if
     *     none was found.
     */
    public static Value fromString(String value) {
      return VALUE_MAP.get(value);
    }
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Draft.Value,
      Draft> KEY = ElementKey.of(new QName(Namespaces.atomPubStandardNs,
      "draft"), Draft.Value.class, Draft.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
  }

  /**
   * Default mutable constructor.
   */
  public Draft() {
    this(DefaultRegistry.get(KEY));
  }

  /**
   * Lets subclasses create an instance using custom metadata.
   */
  protected Draft(ElementMetadata<Draft.Value, ? extends Draft> metadata) {
    super(metadata);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementMetadata} as the
   * metadata for the element.
   *
   * @param metadata metadata to use for this element.
   * @param source source element
   */
  public Draft(ElementMetadata<Draft.Value, ? extends Draft> metadata,
      Element source) {
    super(metadata, source);
  }

  /**
   * Immutable constructor.
   *
   * @param value value.
   */
  public Draft(Value value) {
    this();
    setValue(value);
    setImmutable(true);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public Value getValue() {
    return super.getTextValue(KEY);
  }

  /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
  public void setValue(Value value) {
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Draft other = (Draft) obj;
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
    return "{Draft value=" + getTextValue() + "}";
  }

}
