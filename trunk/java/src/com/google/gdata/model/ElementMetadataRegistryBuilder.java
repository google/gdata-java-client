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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * A mutable, thread-safe builder for the metadata for a particular element.
 * This can be used to construct the default metadata for an element as well
 * as add transforms and adaptations on that element.  The {@link #create}
 * method can be used to create the immutable {@link ElementMetadataRegistry}
 * from this builder.
 *
 * 
 */
final class ElementMetadataRegistryBuilder {

  // The root registry that created this element registry.
  private final MetadataRegistry root;

  // A map of creators for this element.
  private final Map<TransformKey, ElementCreatorImpl> creators
      = Maps.newTreeMap();

  /**
   * Creates a new element metadata registry builder as part of the given
   * metadata registry.
   */
  ElementMetadataRegistryBuilder(MetadataRegistry root) {
    this.root = root;
  }

  /**
   * Merges the values from an existing element registry builder.
   */
  void merge(ElementMetadataRegistryBuilder other) {
    for (Map.Entry<TransformKey, ElementCreatorImpl> entry
        : other.creators.entrySet()) {
      TransformKey key = entry.getKey();
      ElementCreatorImpl creator = creators.get(key);
      if (creator == null) {
        creator = new ElementCreatorImpl(root, key);
        creators.put(key, creator);
      }
      creator.merge(entry.getValue());
    }
  }

  /**
   * Create an immutable element metadata registry from this builder.
   */
  ElementMetadataRegistry create(Schema schema) {
    return new ElementMetadataRegistry(schema, this);
  }

  /**
   * Returns {@code true} if a metadata creator exists for the given combination
   * of parent, key, and context.
   */
  boolean isRegistered(ElementKey<?, ?> parent, ElementKey<?, ?> key,
      MetadataContext context) {
    Preconditions.checkNotNull(key, "key");
    TransformKey transformKey = TransformKey.forTransform(parent, key, context);
    return creators.containsKey(transformKey);
  }

  /**
   * Builds the metadata for this element within the given context when it is
   * placed inside the parent.
   */
  ElementCreatorImpl build(ElementKey<?, ?> parent,
      ElementKey<?, ?> key, MetadataContext context) {
    Preconditions.checkNotNull(key, "key");
    TransformKey transformKey = TransformKey.forTransform(parent, key, context);

    synchronized (root) {
      ElementCreatorImpl creator = creators.get(transformKey);
      if (creator == null) {
        creator = new ElementCreatorImpl(root, transformKey);
        creators.put(transformKey, creator);
        root.dirty();
      }
      return creator;
    }
  }

  /**
   * Returns an immutable copy of the creators in this builder.
   */
  Map<TransformKey, ElementCreatorImpl> getCreators() {
    return ImmutableMap.copyOf(creators);
  }
}
