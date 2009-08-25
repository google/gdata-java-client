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
import java.util.SortedMap;

/**
 * A mutable, thread-safe builder for the metadata for a particular attribute.
 * This can be used to construct the default metadata for an attribute as well
 * as add transforms on the attribute.  The {@link #create} method can be used
 * to create an immutable {@link AttributeMetadataRegistry} from this builder.
 *
 * <p>As all attributes share a common type {@link Attribute}, attributes are
 * indexed only by their QName, so this registry stores all information for
 * attributes that share a QName.  To build a particular set of metadata, call
 * {@link #build} with the desired parent, attribute, and context.
 *
 * 
 */
final class AttributeMetadataRegistryBuilder {

  // The root metadata registry.
  private final MetadataRegistry root;

  // A map of creators by transformKey for the attribute.
  private final SortedMap<TransformKey, AttributeCreatorImpl> creators
      = Maps.newTreeMap();

  /**
   * Creates new empty attribute metadata registry builder as part of the
   * given root registry.
   */
  AttributeMetadataRegistryBuilder(MetadataRegistry root) {
    this.root = root;
  }

  /**
   * Merges the values from an existing attribute registry builder.
   */
  void merge(AttributeMetadataRegistryBuilder other) {
    for (Map.Entry<TransformKey, AttributeCreatorImpl> entry
        : other.creators.entrySet()) {
      TransformKey key = entry.getKey();
      AttributeCreatorImpl creator = creators.get(key);
      if (creator == null) {
        creator = new AttributeCreatorImpl(root, key);
        creators.put(key, creator);
      }
      creator.merge(entry.getValue());
    }
  }

  /**
   * Create an immutable attribute metadata registry from this builder.
   */
  AttributeMetadataRegistry create(Schema schema) {
    return new AttributeMetadataRegistry(schema, this);
  }

  /**
   * Creates the metadata for the attribute in the given parent and context.
   * Parent and attribute must not be null, but a null context means metadata
   * in the default context.
   */
  AttributeCreatorImpl build(ElementKey<?, ?> parent,
      AttributeKey<?> key, MetadataContext context) {
    Preconditions.checkNotNull(parent, "parent");
    Preconditions.checkNotNull(key, "key");
    TransformKey transformKey = TransformKey.forTransform(parent, key, context);

    synchronized (root) {
      AttributeCreatorImpl creator = creators.get(transformKey);
      if (creator == null) {
        creator = new AttributeCreatorImpl(root, transformKey);
        creators.put(transformKey, creator);
        root.dirty();
      }
      return creator;
    }
  }

  /**
   * Returns an immutable copy of the creators in this builder.
   */
  Map<TransformKey, AttributeCreatorImpl> getCreators() {
    return ImmutableMap.copyOf(creators);
  }
}
