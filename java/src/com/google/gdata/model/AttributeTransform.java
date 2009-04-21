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

/**
 * An immutable transform on an attribute. See {@link Transform} for the actual
 * fields.
 *
 * 
 */
final class AttributeTransform extends Transform {

  /**
   * The empty attribute transform, used instead of an empty transform to
   * save space.
   */
  static final AttributeTransform EMPTY = new AttributeTransform();

  /**
   * Creates an attribute transform for the given builder.
   */
  static AttributeTransform create(AttributeCreatorImpl creator) {
    AttributeTransform transform = new AttributeTransform(creator);
    if (Transform.isEmptyTransform(transform)) {
      return EMPTY;
    }
    return transform;
  }

  /**
   * Create a composite attribute transform from the given parts.
   */
  static AttributeTransform create(Iterable<AttributeTransform> parts) {
    AttributeTransform composite = new AttributeTransform(parts);
    if (Transform.isEmptyTransform(composite)) {
      return EMPTY;
    }
    return composite;
  }

  /**
   * Constructs an empty attribute transform.  Use the {@link #EMPTY} instance
   * as transforms are immutable.
   */
  private AttributeTransform() {
    super();
  }

  /**
   * Constructs a transform from the given attribute creator.
   */
  private AttributeTransform(AttributeCreatorImpl creator) {
    super(creator);
  }

  /**
   * Constructs a composite transform from the given parts.
   */
  private AttributeTransform(Iterable<AttributeTransform> parts) {
    super(parts);
  }

  /**
   * Creates a new attribute metadata instance based on this transform.
   */
  <D> AttributeMetadata<D> toMetadata(MetadataRegistry registry,
      ElementKey<?, ?> parent, AttributeKey<D> key, MetadataContext context) {
    return new AttributeMetadataImpl<D>(registry, this, parent, key, context);
  }
}
