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


package com.google.gdata.model;

import com.google.gdata.util.common.base.Objects;
import com.google.gdata.util.common.base.Preconditions;

/**
 * A data attribute in an instance document.  Contains the key that identifies
 * the attribute (name + type) and the value of the attribute.
 */
public final class Attribute {

  /**
   * Key to this attribute.
   */
  private final AttributeKey<?> key;

  /**
   * Untyped attribute value.
   */
  private Object value;

  /**
   * Indicates that the attribute has been locked.
   */
  private volatile boolean locked;

  /**
   * Construct an attribute using the given key and value.
   *
   * @param key the key to this attribute, contains the id and datatype.
   * @param value the attribute value
   * @throws IllegalArgumentException if the key is invalid for the
   *         value (incompatible datatype).
   * @throws NullPointerException if the key or value is null
   */
  Attribute(AttributeKey<?> key, Object value) {
    this.key = Preconditions.checkNotNull(key, "key");
    setValue(value);
  }

  /**
   * Returns true if this attribute has been locked using {@link #lock}. Once an
   * attribute has been locked it cannot be unlocked.
   */
  public boolean isLocked() {
    return locked;
  }

  /**
   * Locks this attribute.  Once an attribute has been locked its value cannot
   * be modified.
   */
  public Attribute lock() {
    locked = true;
    return this;
  }

  /**
   * Returns the attribute key of this attribute.
   */
  public AttributeKey<?> getAttributeKey() {
    return key;
  }

  /**
   * Returns the untyped attribute value.
   *
   * @return untyped attribute value
   */
  public Object getValue() {
    return value;
  }

  /**
   * Sets the value of the attribute.
   *
   * @param value attribute value
   * @throws NullPointerException if the value was null
   * @throws IllegalArgumentException if the value is not of a valid type
   * @throws IllegalStateException if this attribute is locked
   */
  public Attribute setValue(Object value) {
    Preconditions.checkState(!locked, key.getId() + " attribute is read only");
    Preconditions.checkNotNull(value, "Attribute value cannot be null.");
    Preconditions.checkArgument(
        key.getDatatype().isAssignableFrom(value.getClass()),
        "Cannot assign a value of type %s", value.getClass());
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
      .add(key.getId() + "@" + Integer.toHexString(hashCode()), value)
      .toString();
  }
}
