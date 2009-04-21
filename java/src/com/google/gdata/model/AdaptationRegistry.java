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

import com.google.gdata.util.common.base.Pair;

import java.util.Map;

/**
 * An immutable registry of adaptation information for a particular piece
 * of metadata.  Supports getting an adaptation and querying for attributes or
 * elements that match a particular name.
 *
 * <p>An adaptation is a customization of a base type to a variant of that type
 * based on a particular key, represented using a string.  For example, Content
 * elements can be adapted to particular Content variants based on the mime
 * type of the Content.  This is also used for adapting Entry elements to a
 * particular Entry subtype.  See
 * {@link ElementCreator#addAdaptation(String, ElementKey)} for more on
 * adaptations and their uses.
 *
 * <p>This registry is provided primarily to handle lookup of element and
 * attribute names during parsing. If an adaptation defines an attribute or
 * child element that the base class does not, the metadata for the adaptation
 * will be used for that attribute or element, rather than parsing it as
 * undeclared data. If multiple adaptations define the same attribute or child
 * element in incompatible ways, undeclared metadata will be used and the
 * correct metadata will be applied during resolution.
 *
 * 
 */
class AdaptationRegistry {

  /**
   * The metadata registry that this adaptation registry is a part of. This is
   * used to bind the metadata returned from the {@link #findAttribute} and
   * {@link #findElement} methods.
   */
  private final MetadataRegistry metadataRegistry;

  /**
   * Map from adaptation keys to the metadata for that adaptation.
   */
  private final Map<String, ElementKey<?, ?>> adaptations;

  /**
   * The union of attributes for this element and any adaptors.
   */
  private final Map<QName, Pair<ElementKey<?, ?>, AttributeKey<?>>> attributes;

  /**
   * The union of elements for this element and any adaptors.
   */
  private final Map<QName, Pair<ElementKey<?, ?>, ElementKey<?, ?>>> elements;

  /**
   * Constructs a new adaptation registry.
   */
  AdaptationRegistry(MetadataRegistry metadataRegistry,
      Map<String, ElementKey<?, ?>> adaptations,
      Map<QName, Pair<ElementKey<?, ?>, AttributeKey<?>>> attributes,
      Map<QName, Pair<ElementKey<?, ?>, ElementKey<?, ?>>> elements) {
    this.metadataRegistry = metadataRegistry;
    this.adaptations = adaptations;
    this.attributes = attributes;
    this.elements = elements;
  }

  /**
   * Returns the adaptation for a particular kind.
   */
  ElementKey<?, ?> getAdaptation(String kind) {
    return adaptations.get(kind);
  }

  /**
   * Finds the first matching parent/attribute pair with the given id.
   */
  AttributeMetadata<?> findAttribute(QName id) {
    Pair<ElementKey<?, ?>, AttributeKey<?>> found = attributes.get(id);
    if (found != null) {
      return metadataRegistry.bind(found.getFirst(), found.getSecond());
    }
    return null;
  }

  /**
   * Finds the first matching parent/child pair with the given id.
   */
  ElementMetadata<?, ?> findElement(QName id) {
    Pair<ElementKey<?, ?>, ElementKey<?, ?>> found = elements.get(id);
    if (found != null) {
      return metadataRegistry.bind(found.getFirst(), found.getSecond());
    }
    return null;
  }
}
