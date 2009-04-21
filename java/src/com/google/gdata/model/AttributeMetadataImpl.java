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
 * Immutable implementation of the attribute metadata.  Delegates to the
 * registry for binding to an alternate context, so this class can be extremely
 * simple and fast.
 *
 * 
 */
final class AttributeMetadataImpl<D> extends MetadataImpl<D>
    implements AttributeMetadata<D> {

  // The key for the attribute this metadata represents.
  final AttributeKey<D> attKey;

  /**
   * Construct a new immutable attribute metadata for the given declared
   * data.
   */
  AttributeMetadataImpl(MetadataRegistry registry,
      AttributeTransform transform, ElementKey<?, ?> parent,
      AttributeKey<D> key, MetadataContext context) {
    super(registry, transform, parent, key, context);

    this.attKey = key;
  }

  /**
   * Construct a new undeclared attribute metadata instance for the given keys.
   */
  AttributeMetadataImpl(AttributeKey<D> key) {
    super(key);

    this.attKey = key;
  }

  /**
   * Binds this attribute metadata to a different context.  Reuses the current
   * parent and attribute keys and just has the registry bind to a different
   * context.  If this is an undeclared metadata, just returns the same
   * instance (undeclared metadata cannot have transforms).
   */
  public AttributeMetadata<D> bind(MetadataContext context) {
    return (registry == null) ? this : registry.bind(parent, attKey, context);
  }

  @Override
  public AttributeKey<D> getKey() {
    return attKey;
  }
}
