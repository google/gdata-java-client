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
  public ElementKey<?, ?> adapt(String kind) {
    return (adaptations != null) ? adaptations.getAdaptation(kind) : null;
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
   * of undeclared metadata.  If it still hasn't found an attribute this method
   * will return null.
   */
  public AttributeKey<?> findAttribute(QName id) {
    if (!attributes.isEmpty()) {
      AttributeKey<?> attKey = attributes.get(id);
      if (attKey != null) {
        return attKey;
      }

      // See if there is a foo:* match for the given id.
      if (!isStarId(id)) {
        attKey = attributes.get(toStarId(id));

        if (attKey != null) {
          return AttributeKey.of(id, attKey.getDatatype());
        }
      }
    }

    if (adaptations != null) {
      AttributeKey<?> attKey = adaptations.findAttribute(id);
      if (attKey != null) {
        return attKey;
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
   * of undeclared metadata.  If after looking in the adaptations we still have
   * not found a key for the QName, we return null.
   */
  public ElementKey<?, ?> findElement(QName id) {
    if (!elements.isEmpty()) {
      ElementKey<?, ?> childKey = elements.get(id);
      if (childKey != null) {
        return childKey;
      }

      // See if there is a foo:* match for the given id.
      if (!isStarId(id)) {
        childKey = elements.get(toStarId(id));

        if (childKey != null) {
          return ElementKey.of(
              id, childKey.getDatatype(), childKey.getElementType());
        }
      }
    }

    if (adaptations != null) {
      ElementKey<?, ?> childKey = adaptations.findElement(id);
      if (childKey != null) {
        return childKey;
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
    return registry.bind(parent, elemKey, context);
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
      validator.validate(vc, e, this);
    }
  }

  public Iterator<Attribute> getAttributeIterator(Element element) {
    return element.getAttributeIterator(this);
  }

  public ImmutableCollection<AttributeKey<?>> getAttributes() {
    return attributes.values();
  }

  public <K> AttributeMetadata<K> bindAttribute(AttributeKey<K> key) {
    return registry.bind(elemKey, key, context);
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
    return registry.bind(elemKey, key, context);
  }

  @Override
  public Object generateValue(Element element, ElementMetadata<?, ?> metadata) {
    Object result = super.generateValue(element, metadata);
    if (result == null) {
      result = element.getTextValue(elemKey);
    }
    return result;
  }

  @Override
  public void parseValue(Element element, ElementMetadata<?, ?> metadata,
      Object value) throws ParseException {
    if (virtualValue != null) {
      super.parseValue(element, metadata, value);
    } else {
      element.setTextValue(
          ObjectConverter.getValue(value, elemKey.getDatatype()));
    }
  }

  public VirtualElement getVirtualElement() {
    return virtualElement;
  }

  public E createElement() throws ContentCreationException {
    return Element.createElement(elemKey);
  }

  /**
   * Returns true if this ID is a star ID, which represents any name in the
   * namespace.
   */
  private boolean isStarId(QName id) {
    return "*".equals(id.getLocalName());
  }

  /**
   * Returns an id of the form ns:*, if the given id does not already represent
   * the * localname.
   */
  private QName toStarId(QName id) {
    return new QName(id.getNs(), "*");
  }
}
