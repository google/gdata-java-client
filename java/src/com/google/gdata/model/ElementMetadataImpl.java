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

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gdata.model.ContentModel.Cardinality;
import com.google.gdata.model.ElementCreatorImpl.AttributeInfo;
import com.google.gdata.model.ElementCreatorImpl.ElementInfo;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ContentCreationException;
import com.google.gdata.wireformats.ObjectConverter;

import java.util.Collection;
import java.util.Iterator;

/**
 * Immutable implementation of the element metadata.  This class delegates to
 * the registry for binding to other contexts and for retrieving children, and
 * uses an {@link AdaptationRegistry} for dealing with adaptations.
 *
 * 
 */
final class ElementMetadataImpl<D, E extends Element> extends MetadataImpl<D>
    implements ElementMetadata<D, E> {

  private static final ElementValidator DEFAULT_VALIDATOR =
      new MetadataValidator();

  // Element metadata properties.
  private final ElementKey<D, E> elemKey;
  private final Cardinality cardinality;
  private final boolean isContentRequired;
  private final ElementValidator validator;
  private final Object properties;
  private final VirtualElement virtualElement;

  /** Metadata for element's attributes and child elements. */
  private final ImmutableMap<QName, AttributeKey<?>> attributes;
  private final ImmutableMap<QName, ElementKey<?, ?>> elements;

  /** Adaptation helper for dealing with adaptors on this element. */
  private final AdaptationRegistry adaptations;

  /**
   * Constructs a new immutable element metadata instance from the given
   * declared metadata.
   */
  ElementMetadataImpl(MetadataRegistry registry, ElementTransform transform,
      ElementKey<?, ?> parent, ElementKey<D, E> key, MetadataContext context) {
    super(registry, transform, parent, key, context);

    this.elemKey = key;
    this.cardinality = nullToDefault(transform.cardinality, Cardinality.SINGLE);
    this.isContentRequired = nullToDefault(transform.contentRequired, true);
    this.validator = nullToDefault(transform.validator, DEFAULT_VALIDATOR);
    this.properties = transform.properties;
    this.virtualElement = transform.virtualElement;

    this.attributes = getAttributes(transform.attributes.values());
    this.elements = getElements(transform.elements.values());

    if (transform.adaptations.isEmpty()) {
      this.adaptations = null;
    } else {
      this.adaptations = AdaptationRegistryFactory.create(registry, transform);
    }
  }

  /**
   * Constructs a new immutable undeclared element metadata for the given
   * key.
   */
  ElementMetadataImpl(ElementKey<D, E> key) {
    super(key);

    this.elemKey = key;
    this.cardinality = Cardinality.MULTIPLE;
    this.isContentRequired = false;
    this.validator = null;
    this.properties = null;
    this.virtualElement = null;

    this.attributes = ImmutableMap.of();
    this.elements = ImmutableMap.of();
    this.adaptations = null;
  }

  /**
   * Creates an immutable map of attributes from the given collection of
   * attribute info objects.  The info objects are used in transforms to allow
   * bumping attributes to the end of the list, to change their order, but once
   * we are creating the element metadata the order is set and we just need the
   * keys.
   */
  private ImmutableMap<QName, AttributeKey<?>> getAttributes(
      Collection<AttributeInfo> infos) {
    Builder<QName, AttributeKey<?>> builder = ImmutableMap.builder();
    for (AttributeInfo info : infos) {
      builder.put(info.key.getId(), info.key);
    }
    return builder.build();
  }

  /**
   * Creates an immutable map of child elements from the given collection of
   * element info objects.  The info objects are used in transforms to allow
   * bumping elements to the end of the list, to change their order, but once
   * we are creating the element metadata the order is set and we just need the
   * keys.
   */
  private ImmutableMap<QName, ElementKey<?, ?>> getElements(
      Collection<ElementInfo> infos) {
    Builder<QName, ElementKey<?, ?>> builder = ImmutableMap.builder();
    for (ElementInfo info : infos) {
      builder.put(info.key.getId(), info.key);
    }
    return builder.build();
  }

  /**
   * Adapts this element metadata to a different kind, using the provided
   * key.  Will return {@code null} if no adaptation on the given kind exists.
   * If an adaptation does exist it will bind the adaptation under the same
   * parent and context as this element.
   */
  public ElementMetadata<?, ?> adapt(String kind) {
    if (adaptations != null) {
      ElementKey<?, ?> adaptorKey = adaptations.getAdaptation(kind);
      if (adaptorKey != null) {
        return registry.bind(parent, adaptorKey, context);
      }
    }
    return null;
  }

  /**
   * Returns true if the attributes contain the given attribute key, based on
   * its ID.
   */
  public boolean isDeclared(AttributeKey<?> key) {
    return attributes.containsKey(key.getId());
  }

  /**
   * Finds the most appropriate attribute metadata for parsing the given ID.
   * First looks locally, if that fails it then looks locally for a attribute
   * with the "*" local name (used to allow foo:* attribute metadata), and then
   * if that fails it looks in the adaptations.  This allows us to parse an
   * attribute that is declared in adaptations as declared metadata instead
   * of undeclared metadata.
   */
  public AttributeMetadata<?> findAttribute(QName id) {
    if (!attributes.isEmpty()) {
      AttributeKey<?> attributeKey = attributes.get(id);
      if (attributeKey != null) {
        return registry.bind(elemKey, attributeKey, context);
      }

      // See if there is a foo:* match for the given id.
      if (!"*".equals(id.getLocalName())) {
        attributeKey = attributes.get(new QName(id.getNs(), "*"));

        if (attributeKey != null) {
          attributeKey = AttributeKey.of(id, attributeKey.getDatatype());
          return registry.bind(elemKey, attributeKey, context);
        }
      }
    }

    if (adaptations != null) {
      AttributeMetadata<?> adapted = adaptations.findAttribute(id);
      if (adapted != null) {
        return adapted.bind(context);
      }
    }

    return null;
  }

  /**
   * Returns {@code true} if the given element key is declared, by ID.
   */
  public boolean isDeclared(ElementKey<?, ?> element) {
    return elements.containsKey(element.getId());
  }

  /**
   * Finds the most appropriate child metadata for parsing the given ID.
   * First looks locally, if that fails it then looks locally for an element
   * with the "*" local name (used to allow foo:* element metadata), and then
   * if that fails it looks in the adaptations.  This allows us to parse an
   * element that is declared in adaptations as declared metadata instead
   * of undeclared metadata.
   */
  public ElementMetadata<?, ?> findElement(QName id) {
    if (!elements.isEmpty()) {
      ElementKey<?, ?> childKey = elements.get(id);
      if (childKey != null) {
        return registry.bind(elemKey, childKey, context);
      }

      // See if there is a foo:* match for the given id.
      if (!"*".equals(id.getLocalName())) {
        childKey = elements.get(new QName(id.getNs(), "*"));

        if (childKey != null) {
          childKey = ElementKey.of(
              id, childKey.getDatatype(), childKey.getElementType());
          return registry.bind(elemKey, childKey, context);
        }
      }
    }

    if (adaptations != null) {
      ElementMetadata<?, ?> adapted = adaptations.findElement(id);
      if (adapted != null) {
        return adapted.bind(context);
      }
    }

    return null;
  }

  /**
   * Binds this metadata to a different context.  This just asks the registry
   * to handle the binding for us.
   */
  public ElementMetadata<D, E> bind(
      MetadataContext context) {
    return (registry == null) ? this : registry.bind(parent, elemKey, context);
  }

  @Override
  public ElementKey<D, E> getKey() {
    return elemKey;
  }

  public Cardinality getCardinality() {
    return cardinality;
  }

  public boolean isContentRequired() {
    return isContentRequired;
  }

  public boolean isReferenced() {
    return isVisible();
  }

  public Object getProperties() {
    return properties;
  }

  public ElementValidator getValidator() {
    return validator;
  }

  public void validate(ValidationContext vc, Element e) {
    if (validator != null) {
      validator.validate(vc, e);
    }
  }

  public Iterator<Attribute> getAttributeIterator(Element element) {
    return element.getAttributeIterator(this);
  }

  public ImmutableCollection<AttributeKey<?>> getAttributes() {
    return attributes.values();
  }

  public <K> AttributeMetadata<K> bindAttribute(AttributeKey<K> key) {
    return (registry == null) ? null : registry.bind(elemKey, key, context);
  }

  public Iterator<Element> getElementIterator(Element element) {
    return element.getElementIterator(this);
  }

  public ImmutableCollection<ElementKey<?, ?>> getElements() {
    return elements.values();
  }

  @SuppressWarnings("unchecked")
  public <K, L extends Element> ElementMetadata<K, L> bindElement(
      ElementKey<K, L> key) {
    return (registry == null) ? null : registry.bind(elemKey, key, context);
  }

  @Override
  public Object generateValue(Element element) {
    Object result = super.generateValue(element);
    if (result == null) {
      result = element.getTextValue(elemKey);
    }
    return result;
  }

  @Override
  public void parseValue(Element element, Object value) throws ParseException {
    if (virtualValue != null) {
      super.parseValue(element, value);
    } else {
      element.setTextValue(
          ObjectConverter.getValue(value, elemKey.getDatatype()));
    }
  }

  public VirtualElement getVirtualElement() {
    return virtualElement;
  }

  public E createElement() throws ContentCreationException {
    return Element.createElement(this);
  }
}
