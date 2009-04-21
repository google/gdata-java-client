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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gdata.model.ContentModel.Cardinality;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link ElementCreator}. This class is thread-safe for
 * writing, so multiple threads can modify the same element metadata without
 * introducing race conditions.
 *
 * 
 */
final class ElementCreatorImpl extends MetadataCreatorImpl
    implements ElementCreator {

  /** The action to take with a new attribute or element. */
  enum Action { ADD, REPLACE }

  /** An invalid name for the markers. */
  private static final QName UNDECLARED = new QName("**UNDECLARED**");

  /** Marker, see {@link #addUndeclaredAttributeMarker()} */
  static final AttributeKey<Void> ATTRIBUTE_MARKER =
      AttributeKey.of(UNDECLARED, Void.class);

  /** Marker, see {@link #addUndeclaredElementMarker()} */
  static final ElementKey<Void, Element> ELEMENT_MARKER =
      ElementKey.of(UNDECLARED, Void.class, Element.class);

  // The element key for this builder, so we know have the parent key for
  // child elements and attributes.
  private final ElementKey<?, ?> key;

  // The metadata context that is being built for.  Used when adding attributes
  // or child elements so we can add them only in particular contexts.
  private final MetadataContext context;

  // These fields are modifiable, and must only be changed via the set* methods.
  // They are package-private to allow ElementTransform access to the fields.
  private Cardinality cardinality;
  private Boolean contentRequired;
  private ElementValidator validator;
  private Object properties;
  private ValueTransform valueTransform;

  private final Map<QName, AttributeInfo> attributes = Maps.newLinkedHashMap();
  private final Map<QName, ElementInfo> elements = Maps.newLinkedHashMap();
  private final Map<String, ElementKey<?, ?>> adaptations =
      Maps.newLinkedHashMap();

  private Set<AttributeKey<?>> attributeWhitelist;
  private Set<ElementKey<?, ?>> elementWhitelist;

  /**
   * Constructs an empty element metadata builder.
   */
  ElementCreatorImpl(MetadataRegistryBuilder registry,
      ElementKey<?, ?> key, MetadataContext context) {
    super(registry);

    this.key = key;
    this.context = context;
  }

  /**
   * Copy constructor.  Creates a copy of the given element metadata.
   */
  ElementCreatorImpl(MetadataRegistryBuilder registry,
      ElementCreatorImpl source) {
    super(registry, source);

    this.key = source.key;
    this.context = source.context;

    this.cardinality = source.cardinality;
    this.contentRequired = source.contentRequired;
    this.validator = source.validator;
    this.properties = source.properties;
    this.valueTransform = source.valueTransform;

    // We copy the attributes and elements over by re-adding them, as they need
    // to get the actual metadata builders from the registry.
    for (AttributeInfo info : source.attributes.values()) {
      addAttribute(info.key, info.action);
    }
    for (ElementInfo info : source.elements.values()) {
      addElement(info.key, info.action);
    }

    // Adaptations map contains only immutable objects, copy away!
    adaptations.putAll(source.adaptations);
  }

  /**
   * Adds an adaptation to the given kind.  We only add adaptations when the
   * adaptation is a subtype of the current type.
   */
  public ElementCreatorImpl addAdaptation(String kind,
      ElementKey<?, ?> adaptation) {
    synchronized (registry) {
      adaptations.put(kind, adaptation);
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the cardinality of the element.  The cardinality can be
   * {@link Cardinality#SINGLE} for a single element,
   * {@link Cardinality#MULTIPLE} for repeating elements, or
   * {@link Cardinality#SET} for a set of elements (duplicates not allowed).
   *
   * @param cardinality the cardinality of the element.
   * @return this element metadata builder for chaining.
   */
  public ElementCreatorImpl setCardinality(Cardinality cardinality) {
    synchronized (registry) {
      this.cardinality = cardinality;
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets whether the element's content is required.  By default the content is
   * required if the datatype is non-null.
   *
   * @param contentRequired true to set the content to required, false to set
   *     it to optional.
   * @return this element metadata builder for chaining.
   */
  public ElementCreatorImpl setContentRequired(boolean contentRequired) {
    synchronized (registry) {
      this.contentRequired = contentRequired;
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the element's validator.  The validator is used to check that the
   * element has all required attributes/elements/values.  By default an
   * instance of {@link MetadataValidator} is used.
   *
   * @param validator element validator to use when validating the element, or
   *     null if no validation is needed (for undeclared metadata).
   * @return this element metadata builder for chaining.
   */
  public ElementCreatorImpl setValidator(ElementValidator validator) {
    synchronized (registry) {
      this.validator = validator;
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the element's properties.  This is used to provide additional
   * information during parsing/generation, and is specific to the wire format.
   * Because we are creating the default metadata here, this will be the
   * properties for the default wireformat.
   *
   * @param properties default properties for the element.
   * @return this element metadata builder for chaining.
   */
  public ElementCreatorImpl setProperties(Object properties) {
    synchronized (registry) {
      this.properties = properties;
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the value transform for the element.  This is used to retrieve the
   * value of the element if the normal text content is not appropriate.
   */
  public ElementCreatorImpl setValueTransform(ValueTransform valueTransform) {
    synchronized (registry) {
      this.valueTransform = valueTransform;
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the location of the undeclared attributes. By default, undeclared
   * attributes appear after all declared attributes, this lets them appear
   * earlier in the list.
   */
  public ElementCreatorImpl addUndeclaredAttributeMarker() {
    addAttribute(ATTRIBUTE_MARKER);
    return this;
  }

  /**
   * Add the key for an attribute.  If an attribute with the same ID
   * already exists, the previous attribute will be removed, and the new
   * attribute will be added to the end of the list.  If you want to replace the
   * existing element, use {@link #replaceAttribute(AttributeKey)}.
   *
   * @param attributeKey the key to the attribute that is being added.
   * @return an attribute builder that can be used to set the attribute fields.
   */
  public AttributeCreator addAttribute(AttributeKey<?> attributeKey) {
    return addAttribute(attributeKey, Action.ADD);
  }

  /**
   * Replaces the existing metadata for an attribute.
   *
   * @param attributeKey the key to the attribute that is being replaced.
   * @return an attribute builder that can be used to modify the attribute.
   */
  public AttributeCreator replaceAttribute(AttributeKey<?> attributeKey) {
    return addAttribute(attributeKey, Action.REPLACE);
  }

  /**
   * Whitelists a set of attributes for this element metadata.  This will hide
   * all declared attributes on the metadata instance that will be created from
   * this builder.
   */
  public ElementCreatorImpl whitelistAttributes(
      AttributeKey<?>... attributeKeys) {
    synchronized (registry) {
      if (attributeWhitelist == null) {
        attributeWhitelist = Sets.newHashSet();
      }
      attributeWhitelist.addAll(Lists.newArrayList(attributeKeys));
      registry.dirty();
    }
    return this;
  }

  /**
   * Adds an attribute to the map of attributes for this element.  If this is
   * an add we will place the new attribute on the end of the list, and remove
   * any existing attributes with the same key.  If this is not an add, it is
   * a replace, which will fail if the existing element does not exist.
   */
  private AttributeCreator addAttribute(
      AttributeKey<?> attributeKey, Action action) {
    synchronized (registry) {
      QName id = attributeKey.getId();
      if (action == Action.ADD) {
        attributes.remove(id);
      }
      attributes.put(id, new AttributeInfo(attributeKey, action));
      return registry.build(key, attributeKey, context);
    }
  }

  /**
   * Sets the location of the undeclared elements. By default, undeclared
   * elements appear after all declared elements, this lets them appear
   * earlier in the list.
   */
  public ElementCreatorImpl addUndeclaredElementMarker() {
    addElement(ELEMENT_MARKER);
    return this;
  }

  /**
   * Add the metadata for a child element.  If an element with the same ID
   * already exists, the previous element will be removed, and the new element
   * will be added to the end of the list.  If you want to replace the existing
   * element, use {@link #replaceElement(ElementKey)}.
   *
   * @param elementKey the key we are adding or pushing to the end.
   * @return the builder for the child element.
   */
  public  ElementCreator addElement(ElementKey<?, ?> elementKey) {
    return addElement(elementKey, Action.ADD);
  }

  /**
   * Replaces the existing metadata for a child element.
   *
   * @param elementKey the key we are replacing.
   * @return this element metadata builder for chaining.
   * @throws IllegalArgumentException if the child metadata doesn't exist.
   */
  public ElementCreator replaceElement(ElementKey<?, ?> elementKey) {
    return addElement(elementKey, Action.REPLACE);
  }

  /**
   * Whitelists a set of child elements for this element metadata.  This will
   * hide all declared child elements on the metadata instance that will be
   * created from this builder.
   */
  public ElementCreatorImpl whitelistElements(ElementKey<?, ?>... elementKeys) {
    synchronized (registry) {
      if (elementWhitelist == null) {
        elementWhitelist = Sets.newHashSet();
      }
      elementWhitelist.addAll(Lists.newArrayList(elementKeys));
      registry.dirty();
    }
    return this;
  }

  /**
   * Blacklist a set of keys, these keys will be explicitly hidden from view.
   */
  public ElementCreatorImpl blacklistElements(ElementKey<?, ?>... elementKeys) {
    synchronized (registry) {
      for (ElementKey<?, ?> elementKey : elementKeys) {
        addElement(elementKey).setVisible(false);
      }
    }
    return this;
  }

  /**
   * Adds an element to the map of elements we are modifying with this builder.
   */
  private ElementCreator addElement(
      ElementKey<?, ?> elementKey, Action action) {
    synchronized (registry) {
      QName id = elementKey.getId();
      if (action == Action.ADD) {
        elements.remove(id);
      }
      elements.put(id, new ElementInfo(elementKey, action));
      return registry.build(key, elementKey, context);
    }
  }

  /**
   * Sets the name of the element.  This can be used after copying some other
   * metadata to change the name.
   *
   * @param name the new name of the element.
   * @return this element metadata builder for chaining.
   */
  @Override
  public ElementCreatorImpl setName(QName name) {
    return (ElementCreatorImpl) super.setName(name);
  }

  /**
   * Sets the requiredness of this element.  This means that the element
   * must appear in the parent element for the parent element to be valid.
   *
   * @param required true to set the element to required, false to set it
   *     to optional (the default).
   * @return this element metadata builder for chaining.
   */
  @Override
  public ElementCreatorImpl setRequired(boolean required) {
    return (ElementCreatorImpl) super.setRequired(required);
  }

  /**
   * Sets whether this element is visible.  If the element is not visible
   * then it will not be included in the output.  This can be used to set the
   * state of an element to invisible if it is not part of the default set
   * of metadata for its parent.
   *
   * @param visible true to make the element visible (the default), false to
   *     hide it from the output.
   * @return this element metadata builder for chaining.
   */
  @Override
  public ElementCreatorImpl setVisible(boolean visible) {
    return (ElementCreatorImpl) super.setVisible(visible);
  }

  // Package-level read-only access to the fields of this creator.

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

  ValueTransform getValueTransform() {
    return valueTransform;
  }

  /**
   * Returns an immutable copy of the attributes in this creator.
   */
  Map<QName, AttributeInfo> getAttributes() {
    return ImmutableMap.copyOf(attributes);
  }

  /**
   * Returns an immutable set of attribute keys stored in this creator.
   */
  ImmutableSet<AttributeKey<?>> getAttributeSet() {
    Collection<AttributeInfo> infos = attributes.values();
    Builder<AttributeKey<?>> builder = ImmutableSet.builder();
    for (AttributeInfo info : infos) {
      builder.add(info.key);
    }
    return builder.build();
  }

  /**
   * Returns an immutable copy of the elements in this creator.
   */
  Map<QName, ElementInfo> getElements() {
    return ImmutableMap.copyOf(elements);
  }

  /**
   * Returns an immutable set of element keys stored in this creator.
   */
  ImmutableSet<ElementKey<?, ?>> getElementSet() {
    Collection<ElementInfo> infos = elements.values();
    Builder<ElementKey<?, ?>> builder = ImmutableSet.builder();
    for (ElementInfo info : infos) {
      builder.add(info.key);
    }
    return builder.build();
  }

  /**
   * Returns an immutable copy of the adaptations in this creator.
   */
  Map<String, ElementKey<?, ?>> getAdaptations() {
    return ImmutableMap.copyOf(adaptations);
  }

  /**
   * Returns an immutable copy of the whitelist of attributes in this creator.
   */
  Set<AttributeKey<?>> getAttributeWhitelist() {
    return attributeWhitelist == null ? null
        : ImmutableSet.copyOf(attributeWhitelist);
  }

  /**
   * Returns an immutable copy of the whitelist of elements in this creator.
   */
  Set<ElementKey<?, ?>> getElementWhitelist() {
    return elementWhitelist == null ? null
        : ImmutableSet.copyOf(elementWhitelist);
  }

  /**
   * Create a transform from this element metadata builder.
   */
  ElementTransform toTransform() {
    return ElementTransform.create(this);
  }

  /**
   * Holder for attribute information.  Stores the builder + any additional
   * information we want to keep track of.  Currently this is just whether this
   * was an add or a replace.
   */
  static final class AttributeInfo {
    final AttributeKey<?> key;
    final Action action;

    AttributeInfo(AttributeKey<?> key) {
      this(key, Action.REPLACE);
    }

    AttributeInfo(AttributeKey<?> key, Action action) {
      this.key = key;
      this.action = action;
    }
  }

  /**
   * Holder for element information.  Stores the builder + any additional
   * information we want to keep track of.  Currently this is just whether this
   * was an add or a replace.
   */
  static final class ElementInfo {
    final ElementKey<?, ?> key;
    final Action action;

    ElementInfo(ElementKey<?, ?> key) {
      this(key, Action.REPLACE);
    }

    ElementInfo(ElementKey<?, ?> key, Action action) {
      this.key = key;
      this.action = action;
    }
  }
}
