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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gdata.model.ElementCreatorImpl.Action;
import com.google.gdata.model.ElementCreatorImpl.AttributeInfo;
import com.google.gdata.model.ElementCreatorImpl.ElementInfo;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.model.ElementMetadata.VirtualElement;

import java.util.Map;

/**
 * An immutable transform of an element. Simply holds the state of the transform
 * in nullable fields.  Used to create the actual metadata, see
 * {@link ElementMetadataImpl}.
 *
 * 
 */
final class ElementTransform extends Transform {

  /**
   * The empty element transform, used when the builder is empty to save space.
   */
  static final ElementTransform EMPTY = new ElementTransform();

  /**
   * Creates an element transform for the given builder.
   */
  static ElementTransform create(ElementCreatorImpl creator) {
    ElementTransform transform = new ElementTransform(creator);
    if (isEmptyElementTransform(transform)) {
      return EMPTY;
    }
    return transform;
  }

  /**
   * Creates a composite element transform from the given parts.
   */
  static ElementTransform create(
      ElementKey<?, ?> key, Iterable<ElementTransform> parts) {
    ElementTransform composite = new ElementTransform(key, parts);
    if (isEmptyElementTransform(composite)) {
      return EMPTY;
    }
    return composite;
  }

  /**
   * Returns {@code true} if the given element transform is empty.
   */
  static boolean isEmptyElementTransform(ElementTransform transform) {
    if (!Transform.isEmptyTransform(transform)) {
      return false;
    }

    return transform.cardinality == null
        && transform.contentRequired == null
        && transform.validator == null
        && transform.properties == null
        && transform.virtualElement == null
        && transform.attributes.isEmpty()
        && transform.elements.isEmpty()
        && transform.adaptations.isEmpty();
  }

  // Immutable nullable fields that contain the transform information.
  final Cardinality cardinality;
  final Boolean contentRequired;
  final ElementValidator validator;
  final Object properties;
  final VirtualElement virtualElement;

  // The maps will be immutable empty maps, and not null, if they contain
  // no values.  All immutable maps use the same instance so this doesn't waste
  // any space, and makes merging simpler (no null checks).
  final Map<QName, AttributeInfo> attributes;
  final Map<QName, ElementInfo> elements;
  final Map<String, ElementKey<?, ?>> adaptations;

  /**
   * A default element transform, used when an element is registered but
   * contains no metadata.
   */
  private ElementTransform() {
    super();

    this.cardinality = null;
    this.contentRequired = null;
    this.validator = null;
    this.properties = null;
    this.virtualElement = null;

    this.attributes = ImmutableMap.of();
    this.elements = ImmutableMap.of();
    this.adaptations = ImmutableMap.of();
  }

  /**
   * Constructs an element transform for the declared element metadata described
   * by the given builder.
   */
  private ElementTransform(ElementCreatorImpl builder) {
    super(builder);

    this.cardinality = builder.getCardinality();
    this.contentRequired = builder.getContentRequired();
    this.validator = builder.getValidator();
    this.properties = builder.getProperties();
    this.virtualElement = builder.getVirtualElement();
    this.attributes = ImmutableMap.copyOf(builder.getAttributes());
    this.elements = ImmutableMap.copyOf(builder.getElements());
    this.adaptations = ImmutableMap.copyOf(builder.getAdaptations());
  }

  /**
   * Constructs a composite element transform from the given parts.
   */
  private ElementTransform(ElementKey<?, ?> key,
      Iterable<ElementTransform> parts) {
    super(parts);

    Cardinality compositeCardinality = null;
    Boolean compositeContentRequired = null;
    ElementValidator compositeValidator = null;
    Object compositeProperties = null;
    VirtualElement compositeVirtualElement = null;

    Map<QName, AttributeInfo> compositeAttributes = Maps.newLinkedHashMap();
    Map<QName, ElementInfo> compositeElements = Maps.newLinkedHashMap();
    Map<String, ElementKey<?, ?>> compositeAdaptors = Maps.newLinkedHashMap();

    for (ElementTransform part : parts) {
      if (part.cardinality != null) {
        compositeCardinality = part.cardinality;
      }
      if (part.contentRequired != null) {
        compositeContentRequired = part.contentRequired;
      }
      if (part.validator != null) {
        compositeValidator = part.validator;
      }
      if (part.properties != null) {
        compositeProperties = part.properties;
      }
      if (part.virtualElement != null) {
        compositeVirtualElement = part.virtualElement;
      }
      for (Map.Entry<QName, AttributeInfo> entry
          : part.attributes.entrySet()) {
        QName attId = entry.getKey();
        AttributeInfo attInfo = entry.getValue();
        if (attInfo.action == Action.ADD) {
          compositeAttributes.remove(attId);
        }
        compositeAttributes.put(attId, attInfo);
      }
      for (Map.Entry<QName, ElementInfo> entry
          : part.elements.entrySet()) {
        QName childId = entry.getKey();
        ElementInfo childInfo = entry.getValue();
        if (childInfo.action == Action.ADD) {
          compositeElements.remove(childId);
        }
        compositeElements.put(childId, childInfo);
      }
      for (Map.Entry<String, ElementKey<?, ?>> entry
          : part.adaptations.entrySet()) {
        ElementKey<?, ?> adaptor = entry.getValue();
        if (isValidAdaptation(key, adaptor)) {
          compositeAdaptors.put(entry.getKey(), adaptor);
        }
      }
    }

    // Assign the final fields.
    this.cardinality = compositeCardinality;
    this.contentRequired = compositeContentRequired;
    this.validator = compositeValidator;
    this.properties = compositeProperties;
    this.virtualElement = compositeVirtualElement;
    this.attributes = ImmutableMap.copyOf(compositeAttributes);
    this.elements = ImmutableMap.copyOf(compositeElements);
    this.adaptations = ImmutableMap.copyOf(compositeAdaptors);
  }

  /**
   * Checks if the given adaptation is valid.  An adaptation is only valid as
   * part of a composite if the adaptor type is a subtype of the source type.
   */
  private static boolean isValidAdaptation(ElementKey<?, ?> source,
      ElementKey<?, ?> adaptor) {
    Class<?> sourceType = source.getElementType();
    Class<?> adaptorType = adaptor.getElementType();
    if (sourceType == adaptorType) {
      return false;
    }
    return sourceType.isAssignableFrom(adaptorType);
  }

  /**
   * Creates a new element metadata instance based on this transform.
   */
  <D, E extends Element> ElementMetadata<D, E> toMetadata(
      MetadataRegistry registry, ElementKey<?, ?> parent, ElementKey<D, E> key,
      MetadataContext context) {
    return new ElementMetadataImpl<D, E>(registry, this, parent, key, context);
  }
}
