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

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ObjectConverter;

/**
 * A data attribute in an instance document.
 */
public class Attribute {

  /**
   * Untyped attribute value.
   */
  private Object value;

  /**
   * Attribute metadata.
   */
  private AttributeMetadata<?> metadata;

  /**
   * Metadata context this attribute is operating in.
   */
  private MetadataContext context;

  /**
   * Construct attribute value using given metadata and value.
   *
   * @param metadata attribute metadata
   * @param value attribute value
   * @throws IllegalArgumentException if the metadata is invalid for the
   * given value.
   */
  Attribute(AttributeMetadata<?> metadata, Object value) {
    setMetadata(metadata);
    setValue(value);
  }

  /**
   * Returns the attribute key of this attribute.
   */
  public AttributeKey<?> getAttributeKey() {
    return getMetadata().getKey();
  }

  /**
   * Returns the id of this attribute.
   */
  QName getAttributeId() {
    return getAttributeKey().getId();
  }

  /**
   * @return attribute metadata
   */
  public AttributeMetadata<?> getMetadata() {
    return metadata;
  }

  /**
   * Sets the metadata for this attribute.
   */
  void setMetadata(AttributeMetadata<?> newMeta) {
    Preconditions.checkNotNull(newMeta, "Metadata cannot be null.");
    this.metadata = newMeta;
    this.context = newMeta.getContext();

    resolveValue(newMeta);
  }

  /**
   * Resolve the value of the attribute against the metadata.  If the value is
   * not convertible to the metadata type this will throw an illegal argument
   * exception, because it was caused by setting invalid metadata.
   */
  private void resolveValue(AttributeMetadata<?> newMeta) {
    try {
      value = ObjectConverter.getValue(value, newMeta.getKey().getDatatype());
    } catch (ParseException e) {
      throw new IllegalArgumentException("Invalid metadata", e);
    }
  }

  /**
   * Binds the context the attribute is being used in.
   *
   * @param newContext the new context to bind to.
   */
  void bind(MetadataContext newContext) {
    if (context == newContext
        || (context != null && context.equals(newContext))) {
      return;
    }

    setMetadata(getMetadata().bind(newContext));
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
   * Returns the attribute value cast to the appropriate type, based on the
   * given metadata.
   *
   * @param <V> metadata datatype
   * @param key the attribute key to use to cast the attribute type
   * @return typed attribute value
   * @throws IllegalArgumentException if the value cannot be converted to the
   *     metadata type.
   */
  public <V> V getValue(AttributeKey<V> key) {
    try {
      return ObjectConverter.getValue(value, key.getDatatype());
    } catch (ParseException e) {
      throw new IllegalArgumentException("Unable to convert value " + e
          + " to datatype " + key.getDatatype());
    }
  }

  /**
   * Sets the value of the attribute.
   *
   * @param value attribute value
   * @throws NullPointerException if the value was null
   * @throws IllegalArgumentException if the value is not of a valid type
   */
  public void setValue(Object value) {
    if (value == null) {
      throw new NullPointerException("Attribute value cannot be null.");
    }
    if (!getAttributeKey().getDatatype().isAssignableFrom(value.getClass())) {
      throw new IllegalArgumentException(
          "Cannot assign a value of type " + value.getClass());
    }
    this.value = value;
  }

  @Override
  public String toString() {
    return "{" + getAttributeId() + "=" + getValue() + "}";
  }
}
