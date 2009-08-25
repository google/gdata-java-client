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

/**
 * Immutable base implementation of {@link Metadata}.  Each metadata instance
 * is bound to a specific registry, parent key, metadata key, and
 * metadata context.
 *
 * 
 */
abstract class MetadataImpl<D> implements Metadata<D> {

  final Schema schema;
  final MetadataKey<D> key;
  final ElementKey<?, ?> parent;
  final MetadataContext context;

  private final QName name;
  private final boolean isRequired;
  private final boolean isVisible;
  private final VirtualValue virtualValue;

  /**
   * Constructs a new immutable metadata instance with the given declared data.
   * Any information not contained in the transform will use the default values.
   */
  MetadataImpl(Schema schema, Transform transform,
      ElementKey<?, ?> parent, MetadataKey<D> key, MetadataContext context) {
    this.schema = Preconditions.checkNotNull(schema, "schema");
    this.key = Preconditions.checkNotNull(key, "key");
    this.parent = parent;
    this.context = context;
    
    transform = AttributeTransform.mergeSource(schema, transform, context);
    
    this.name = firstNonNull(
        transform.getName(), key.getId());
    this.isRequired = firstNonNull(
        transform.getRequired(), false);
    
    Path path = transform.getPath();
    if (transform.isMoved()) {
      
      // Metadata that has been moved is always hidden.
      this.isVisible = false;
    } else if (path != null) {
        
      // Metadata on a path may be hidden.
      this.isVisible = isVisible(path, schema, parent, context);
    } else {
      
      // Normal metadata uses the visibility of the transform directly.
      this.isVisible = firstNonNull(transform.getVisible(), true);
    }
    this.virtualValue = transform.getVirtualValue();
  }

  /**
   * Returns {@code true} if this path is visible starting at the given parent,
   * under the given context.  Returns {@code false} if any parts of the path
   * have been hidden, which means that the path itself should be hidden as
   * well.
   */
  static boolean isVisible(Path path, Schema schema, ElementKey<?, ?> parent,
      MetadataContext context) {
    for (MetadataKey<?> part : path.getSteps()) {
      Transform transform = schema.getTransform(parent, part, context);
      Boolean visible = transform.getVisible();
      if (visible != null && !visible.booleanValue()) {
        return false;
      }
      if (part instanceof ElementKey<?, ?>) {
        parent = (ElementKey<?, ?>) part;
      }
    }
    return true;
  }
  
  /**
   * Provides the first non-null value out of a varargs of values.  Will always
   * return a non-null value or throw an IAE.
   */
  static <T> T firstNonNull(T... values) {
    for (T value : values) {
      if (value != null) {
        return value;
      }
    }
    throw new IllegalArgumentException(
        "Values must contain at least a single non-null value.");
  }

  public Schema getSchema() {
    return schema;
  }

  public MetadataKey<D> getKey() {
    return key;
  }

  public ElementKey<?, ?> getParent() {
    return parent;
  }

  public MetadataContext getContext() {
    return context;
  }

  public QName getName() {
    return name;
  }

  public boolean isRequired() {
    return isRequired;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public Object generateValue(Element element, ElementMetadata<?, ?> metadata) {
    if (virtualValue != null) {
      return virtualValue.generate(element, metadata);
    }
    return null;
  }

  public void parseValue(Element element, ElementMetadata<?, ?> metadata,
      Object value) throws ParseException {
    parse(element, metadata, value);
  }
  
  boolean parse(Element element, ElementMetadata<?, ?> metadata, Object value)
      throws ParseException {
    if (virtualValue != null) {
      virtualValue.parse(element, metadata, value);
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + getKey() + "}@" +
        Integer.toHexString(hashCode());
  }
}
