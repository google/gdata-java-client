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
 * fields.  These represent the immutable version of {@link AttributeCreator}
 * instances.
 *
 * 
 */
final class AttributeTransform extends Transform {

  /**
   * The empty attribute transform, used to save space by not creating many
   * empty transforms.
   */
  static final AttributeTransform EMPTY = new AttributeTransform();

  /**
   * Creates an attribute transform for the given creator.  This is used to turn
   * our mutable {@link AttributeCreator} instances into immutable transforms
   * that we can store in the metadata registry.
   */
  static AttributeTransform create(AttributeCreatorImpl creator) {
    AttributeTransform transform = new AttributeTransform(creator);
    if (transform.isEmpty()) {
      return EMPTY;
    }
    return transform;
  }

  /**
   * Create a composite attribute transform from the given parts.  This is used
   * to turn a collection of matching transforms into a single composite
   * transform that can then be turned into metadata.  See
   * {@link #AttributeTransform(Iterable)} for details.
   */
  static AttributeTransform create(Iterable<AttributeTransform> parts) {
    AttributeTransform composite = new AttributeTransform(parts);
    if (composite.isEmpty()) {
      return EMPTY;
    }
    return composite;
  }
  
  /**
   * Creates a transform that includes source information, if the transform
   * has been moved from a different location.  This allows modifications to the
   * source to also affect any transforms that reference that source. See
   * {@link #AttributeTransform(Transform, Transform)} for details.
   */
  static Transform mergeSource(Schema schema, Transform transform,
      MetadataContext context) {
    TransformKey sourceKey = transform.getSource();
    if (sourceKey != null) {
      Transform source = schema.getTransform(
          sourceKey.getParent(), sourceKey.getKey(), context);
      if (source != null) {
        return new AttributeTransform(transform, source);
      }
    }
    return transform;
  }

  /**
   * Constructs an empty attribute transform. Because transforms are immutable
   * this method is only called to construct the {@link #EMPTY} instance.
   */
  private AttributeTransform() {
    super();
  }

  /**
   * Constructs a transform from the given attribute creator, this will just
   * copy all of the creator fields.
   */
  private AttributeTransform(AttributeCreatorImpl creator) {
    super(creator);
  }

  /**
   * Constructs a composite transform from the given parts.  See
   * {@link Transform#Transform(Iterable)} for details.
   */
  private AttributeTransform(Iterable<AttributeTransform> parts) {
    super(parts);
  }
  
  /**
   * Constructs a transform that overrides the regular transform data with data
   * from the source when appropriate.  See
   * {@link Transform#Transform(Transform, Transform)} for details.
   */
  private AttributeTransform(Transform transform, Transform source) {
    super(transform, source);
  }

  /**
   * Creates a new attribute metadata instance based on this transform.
   */
  <D> AttributeMetadata<D> toMetadata(Schema schema,
      ElementKey<?, ?> parent, AttributeKey<D> key, MetadataContext context) {
    return new AttributeMetadataImpl<D>(schema, this, parent, key, context);
  }
}
