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
   * The empty element transform, used to save space by not creating many empty
   * transforms.
   */
  static final ElementTransform EMPTY = new ElementTransform();

  /**
   * Creates an element transform for the given creator.  This is used to turn
   * our mutable {@link ElementCreator} instances into immutable transforms that
   * we can store in the metadata registry.
   */
  static ElementTransform create(ElementCreatorImpl creator) {
    ElementTransform transform = new ElementTransform(creator);
    if (transform.isEmpty()) {
      return EMPTY;
    }
    return transform;
  }

  /**
   * Creates a composite element transform from the given parts. This is used to
   * turn a collection of matching element transforms into a single composite
   * transform that can then be turned into metadata. See
   * {@link #ElementTransform(ElementKey, Iterable)} for details.
   */
  static ElementTransform create(
      ElementKey<?, ?> key, Iterable<ElementTransform> parts) {
    ElementTransform composite = new ElementTransform(key, parts);
    if (composite.isEmpty()) {
      return EMPTY;
    }
    return composite;
  }

  /**
   * Creates an element transform that includes source information, if the
   * transform has been moved from a different location. This allows
   * modifications to the source to also affect any transforms that reference
   * that source. See
   * {@link #ElementTransform(ElementKey, ElementTransform, ElementTransform)}
   * for details.
   */
  static ElementTransform mergeSource(Schema schema, ElementKey<?, ?> key,
      ElementTransform transform, MetadataContext context) {
    TransformKey sourceKey = transform.getSource();
    if (sourceKey != null) {
      ElementTransform source = schema.getTransform(sourceKey.getParent(),
          (ElementKey<?, ?>) sourceKey.getKey(), context);
      if (source != null) {
        return new ElementTransform(key, transform, source);
      }
    }
    return transform;
  }
  
  // Immutable nullable fields that contain the transform information.
  private final Cardinality cardinality;
  private final Boolean contentRequired;
  private final ElementValidator validator;
  private final Object properties;
  private final VirtualElementHolder virtualElementHolder;
  private final boolean flattened;

  // The maps will be immutable empty maps, and not null, if they contain
  // no values.  All immutable maps use the same instance so this doesn't waste
  // any space, and makes merging simpler (no null checks).
  private final Map<QName, AttributeInfo> attributes;
  private final Map<QName, ElementInfo> elements;
  private final Map<String, ElementKey<?, ?>> adaptations;

  /**
   * Constructs an empty element transform. Because transforms are immutable
   * this method is only called to construct the {@link #EMPTY} instance.
   */
  private ElementTransform() {
    super();

    this.cardinality = null;
    this.contentRequired = null;
    this.validator = null;
    this.properties = null;
    this.virtualElementHolder = null;
    this.flattened = false;

    this.attributes = ImmutableMap.of();
    this.elements = ImmutableMap.of();
    this.adaptations = ImmutableMap.of();
  }

  /**
   * Constructs an element transform for the declared element metadata described
   * by the given element creator.
   */
  private ElementTransform(ElementCreatorImpl builder) {
    super(builder);

    this.cardinality = builder.getCardinality();
    this.contentRequired = builder.getContentRequired();
    this.validator = builder.getValidator();
    this.properties = builder.getProperties();
    this.virtualElementHolder = builder.getVirtualElementHolder();
    this.flattened = builder.isFlattened();
    this.attributes = ImmutableMap.copyOf(builder.getAttributes());
    this.elements = ImmutableMap.copyOf(builder.getElements());
    this.adaptations = ImmutableMap.copyOf(builder.getAdaptations());
  }

  /**
   * Constructs a composite element transform from the given parts.  Transforms
   * are combined by allowing values that appear later in the iterable to
   * override values that appear earlier.
   */
  private ElementTransform(ElementKey<?, ?> key,
      Iterable<ElementTransform> parts) {
    super(parts);

    Cardinality compositeCardinality = null;
    Boolean compositeContentRequired = null;
    ElementValidator compositeValidator = null;
    Object compositeProperties = null;
    VirtualElementHolder compositeVirtualElementHolder = null;
    boolean compositeFlattened = false;

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
      if (part.virtualElementHolder != null) {
        compositeVirtualElementHolder = part.virtualElementHolder;
      }
      if (part.flattened) {
        compositeFlattened = true;
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
    this.virtualElementHolder = compositeVirtualElementHolder;
    this.flattened = compositeFlattened;
    this.attributes = ImmutableMap.copyOf(compositeAttributes);
    this.elements = ImmutableMap.copyOf(compositeElements);
    this.adaptations = ImmutableMap.copyOf(compositeAdaptors);
  }
  
  /**
   * Constructs a composite element transform from the transform and source.
   * This differs from the {@link #ElementTransform(ElementKey, Iterable)}
   * constructor by only using certain values from the source.  The differences
   * are:
   * <ul>
   * <li>Whether the content is required is not pulled from the source.</li>
   * <li>Attributes are first added from the source, and then any attributes
   * that were in the transform but not in the source are added to the end.  In
   * particular this means that any attributes that were marked as "add" in the
   * transform are not moved to the end of the order, as the would be in the
   * composite constructor.</li>
   * <li>Elements are added in the same way as attributes, first from the source
   * and then any new attributes added at the end.</li>
   * <li>Adaptors are handled similarly, first from the source and then from the
   * transform.</li>
   * </ul>
   * 
   * <p>See {@link Transform#Transform(Transform, Transform)} for the changes to
   * fields shared between attribute and element transforms.
   */
  private ElementTransform(ElementKey<?, ?> key,
      ElementTransform transform, ElementTransform source) {
    super(transform, source);
    
    this.cardinality = first(transform.cardinality, source.cardinality);
    this.contentRequired = transform.contentRequired;
    this.validator = first(transform.validator, source.validator);
    this.properties = first(transform.properties, source.properties);
    this.virtualElementHolder = first(
        transform.virtualElementHolder, source.virtualElementHolder);
    this.flattened = transform.isFlattened() || source.isFlattened();
    
    // For now we only support appending properties onto the source, so we use
    // the source first and then add any previously unseen properties onto the
    // end.
    
    Map<QName, AttributeInfo> compositeAttributes = Maps.newLinkedHashMap();
    compositeAttributes.putAll(source.getAttributes());
    for (Map.Entry<QName, AttributeInfo> entry
        : transform.attributes.entrySet()) {
      QName attId = entry.getKey();
      if (!compositeAttributes.containsKey(attId)) {
        compositeAttributes.put(entry.getKey(), entry.getValue());
      }
    }
    this.attributes = ImmutableMap.copyOf(compositeAttributes);
    
    Map<QName, ElementInfo> compositeElements = Maps.newLinkedHashMap();
    compositeElements.putAll(source.getElements());
    for (Map.Entry<QName, ElementInfo> entry
        : transform.elements.entrySet()) {
      QName childId = entry.getKey();
      if (!compositeElements.containsKey(childId)) {
        compositeElements.put(childId, entry.getValue());
      }
    }
    this.elements = ImmutableMap.copyOf(compositeElements);
    
    Map<String, ElementKey<?, ?>> compositeAdaptors = Maps.newLinkedHashMap();
    compositeAdaptors.putAll(source.getAdaptations());
    for (Map.Entry<String, ElementKey<?, ?>> entry
        : transform.adaptations.entrySet()) {
      String kind = entry.getKey();
      ElementKey<?, ?> adaptor = entry.getValue();
      if (!compositeAdaptors.containsKey(kind)
          && isValidAdaptation(key, adaptor)) {
        compositeAdaptors.put(kind, adaptor);
      }
    }
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
      Schema schema, ElementKey<?, ?> parent, ElementKey<D, E> key,
      MetadataContext context) {
    return new ElementMetadataImpl<D, E>(schema, this, parent, key, context);
  }

  Cardinality getCardinality() {
    return cardinality;
  }

  Boolean getContentRequired() {
    return contentRequired;
  }

  ElementValidator getValidator() {
    return validator;
  }

  Object getProperties() {
    return properties;
  }

  VirtualElementHolder getVirtualElementHolder() {
    return virtualElementHolder;
  }

  boolean isFlattened() {
    return flattened;
  }

  Map<QName, AttributeInfo> getAttributes() {
    return attributes;
  }

  Map<QName, ElementInfo> getElements() {
    return elements;
  }

  Map<String, ElementKey<?, ?>> getAdaptations() {
    return adaptations;
  }
  
  @Override
  boolean isEmpty() {
    return super.isEmpty()
      && cardinality == null
      && contentRequired == null
      && validator == null
      && properties == null
      && virtualElementHolder == null
      && !flattened
      && attributes.isEmpty()
      && elements.isEmpty()
      && adaptations.isEmpty();
  }
}
