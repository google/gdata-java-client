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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.model.ElementMetadata.SingleVirtualElement;
import com.google.gdata.model.ElementMetadata.MultipleVirtualElement;
import com.google.gdata.model.Metadata.VirtualValue;

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
  private final ElementKey<?, ?> elementKey;

  // These fields are modifiable, and may only be changed via the set* methods.
  private Cardinality cardinality;
  private Boolean contentRequired;
  private ElementValidator validator;
  private Object properties;
  private VirtualElementHolder virtualElementHolder;
  private boolean flattened;

  private final Map<QName, AttributeInfo> attributes = Maps.newLinkedHashMap();
  private final Map<QName, ElementInfo> elements = Maps.newLinkedHashMap();
  private final Map<String, ElementKey<?, ?>> adaptations =
      Maps.newLinkedHashMap();

  private Set<AttributeKey<?>> attributeWhitelist;
  private Set<ElementKey<?, ?>> elementWhitelist;

  /**
   * Constructs an empty element creator.
   */
  ElementCreatorImpl(MetadataRegistry registry, TransformKey transformKey) {
    super(registry, transformKey);

    MetadataKey<?> key = transformKey.getKey();
    Preconditions.checkArgument(key instanceof ElementKey<?, ?>,
        "Key must be to an element.");
    this.elementKey = (ElementKey<?, ?>) key;
  }

  /**
   * Merges the values from an existing element creator.
   */
  void merge(ElementCreatorImpl other) {
    super.merge(other);

    if (other.cardinality != null) {
      this.cardinality = other.cardinality;
    }
    if (other.contentRequired != null) {
      this.contentRequired = other.contentRequired;
    }
    if (other.validator != null) {
      this.validator = other.validator;
    }
    if (other.properties != null) {
      this.properties = other.properties;
    }
    if (other.virtualElementHolder != null) {
      this.virtualElementHolder = other.virtualElementHolder;
    }
    if (other.flattened) {
      this.flattened = true;
    }

    // We copy the attributes and elements over by re-adding them, as they need
    // to get the actual metadata builders from the registry.
    for (AttributeInfo info : other.attributes.values()) {
      addAttribute(info.key, info.action);
    }
    for (ElementInfo info : other.elements.values()) {
      addElement(info.key, info.action);
    }

    // Adaptations map contains only immutable objects, copy away!
    adaptations.putAll(other.adaptations);

    // Merge the local whitelists with the other whitelists.
    if (other.attributeWhitelist != null) {
      whitelistAttributes(other.attributeWhitelist);
    }
    if (other.elementWhitelist != null) {
      whitelistElements(other.elementWhitelist);
    }
  }

  /**
   * Adds an adaptation to the given kind.
   */
  public ElementCreatorImpl adapt(String kind,
      ElementKey<?, ?> adaptation) {
    synchronized (registry) {
      adaptations.put(kind, adaptation);
      registry.register(adaptation);
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
   * Sets the virtual element for the element.  This is used to create a
   * fully virtual element that doesn't map directly to the DOM.
   *
   * <p>This is used for single cardinality elements. Use
   * {@link MultipleVirtualElement} for multiple cardinality elements.
   */
  public ElementCreatorImpl setSingleVirtualElement(
      SingleVirtualElement singleVirtualElement) {
    synchronized (registry) {
      this.virtualElementHolder = VirtualElementHolder.of(singleVirtualElement);
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the virtual element for the element.  This is used to create a
   * fully virtual element that doesn't map directly to the DOM.
   *
   * <p>This is used for single cardinality elements. Use
   * {@link MultipleVirtualElement} for multiple cardinality elements.
   */
  public ElementCreatorImpl setMultipleVirtualElement(
      MultipleVirtualElement multipleVirtualElement) {
    synchronized (registry) {
      this.virtualElementHolder =
          VirtualElementHolder.of(multipleVirtualElement);
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the source path of an element.  An element that has a source will
   * use a virtual element based on the path.
   */
  @Override
  void setSource(Path path, TransformKey key) {
    super.setSource(path, key);
    setElementPath(path);
  }
  
  /**
   * Sets the virtual element for this creator based on the path.
   */
  private void setElementPath(Path path) {
    synchronized (registry) {
      this.virtualElementHolder = VirtualElementHolder.of(path);
      registry.dirty();
    }
  }
  
  /**
   * Flattens this element, which just marks the builder as flattened.
   */
  public ElementCreatorImpl flatten() {
    synchronized (registry) {
      this.flattened = true;
      registry.dirty();
    }
    return this;
  }

  /**
   * Adds a virtual attribute, which marks the source attribute as moved and
   * creates a new virtual attribute in this element with the source path.
   */
  public AttributeCreator moveAttribute(AttributeKey<?> attKey, Path path) {
    Preconditions.checkArgument(path.selectsAttribute(),
        "Path must refer to an attribute.");
    AttributeCreatorImpl dest = replaceAttribute(attKey);
    AttributeCreatorImpl source = getAttributeCreator(path);
    dest.setSource(path, source.getTransformKey());
    source.moved();
    return dest;
  }

  /**
   * Returns the attribute creator at the end of the path by looking it up
   * directly in the registry.
   */
  private AttributeCreatorImpl getAttributeCreator(Path path) {
    Preconditions.checkArgument(path.selectsAttribute(),
        "Must be an attribute path");
    ElementKey<?, ?> parent = path.getParentKey();
    if (parent == null) {
      parent = elementKey;
    }
    AttributeKey<?> selected = path.getSelectedAttributeKey();
    return (AttributeCreatorImpl) registry.build(
        parent, selected, transformKey.getContext());
  }
  
  /**
   * Adds a virtual element, which marks the source element as moved and creates
   * a new virtual child element with the source path.
   */
  public ElementCreator moveElement(ElementKey<?, ?> childKey, Path path) {
    Preconditions.checkArgument(path.selectsElement(),
        "Path must refer to an element.");
    ElementCreatorImpl dest = replaceElement(childKey);
    ElementCreatorImpl source = getElementCreator(path);
    dest.setSource(path, source.getTransformKey());
    source.moved();
    return dest;
  }
  
  /**
   * Returns the element creator at the end of the path by looking it up
   * directly in the registry (not traversing).
   */
  private ElementCreatorImpl getElementCreator(Path path) {
    Preconditions.checkArgument(path.selectsElement(),
        "Must be an element path");
    ElementKey<?, ?> parent = path.getParentKey();
    if (parent == null) {
      parent = elementKey;
    }
    ElementKey<?, ?> selected = path.getSelectedElementKey();
    return (ElementCreatorImpl) registry.build(
        parent, selected, transformKey.getContext());
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
  public AttributeCreatorImpl addAttribute(AttributeKey<?> attributeKey) {
    return addAttribute(attributeKey, Action.ADD);
  }

  /**
   * Replaces the existing metadata for an attribute.
   *
   * @param attributeKey the key to the attribute that is being replaced.
   * @return an attribute builder that can be used to modify the attribute.
   */
  public AttributeCreatorImpl replaceAttribute(AttributeKey<?> attributeKey) {
    return addAttribute(attributeKey, Action.REPLACE);
  }

  /**
   * Whitelists a set of attributes as well as setting their order.  Adding an
   * attribute pushes it to the end of the stack, so we just add the attributes
   * in the order given and then whitelist them.
   */
  public ElementCreatorImpl orderAndWhitelistAttributes(
      AttributeKey<?>... attributeKeys) {
    for (AttributeKey<?> attributeKey : attributeKeys) {
      addAttribute(attributeKey);
    }

    return whitelistAttributes(attributeKeys);
  }

  /**
   * Whitelists a set of attributes for this element metadata.  This will hide
   * all declared attributes on the metadata instance that will be created from
   * this builder.
   */
  public ElementCreatorImpl whitelistAttributes(
      AttributeKey<?>... attributeKeys) {
    return whitelistAttributes(Lists.newArrayList(attributeKeys));
  }

  /**
   * Whitelists a set of attributes for this element metadata.  This will hide
   * all declared attributes on the metadata instance that will be created from
   * this builder.
   */
  private ElementCreatorImpl whitelistAttributes(
      Collection<AttributeKey<?>> attributeKeys) {
    synchronized (registry) {
      if (attributeWhitelist == null) {
        attributeWhitelist = Sets.newHashSet();
      }
      attributeWhitelist.addAll(attributeKeys);
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
  private AttributeCreatorImpl addAttribute(
      AttributeKey<?> attributeKey, Action action) {
    synchronized (registry) {
      QName id = attributeKey.getId();
      if (action == Action.ADD) {
        attributes.remove(id);
      }
      attributes.put(id, new AttributeInfo(attributeKey, action));
      return (AttributeCreatorImpl) registry.build(
          elementKey, attributeKey, transformKey.getContext());
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
  public  ElementCreatorImpl addElement(ElementKey<?, ?> elementKey) {
    return addElement(elementKey, Action.ADD);
  }

  /**
   * Replaces the existing metadata for a child element.
   *
   * @param elementKey the key we are replacing.
   * @return this element metadata builder for chaining.
   * @throws IllegalArgumentException if the child metadata doesn't exist.
   */
  public ElementCreatorImpl replaceElement(ElementKey<?, ?> elementKey) {
    return addElement(elementKey, Action.REPLACE);
  }

  /**
   * Whitelists a set of child elements as well as setting their order.  Adding
   * an element pushes it to the end of the stack, so we just add the child
   * elements in the order given and then whitelist them.
   */
  public ElementCreatorImpl orderAndWhitelistElements(
      ElementKey<?,?>... elementKeys) {
    for (ElementKey<?, ?> elementKey : elementKeys) {
      addElement(elementKey);
    }

    return whitelistElements(elementKeys);
  }

  /**
   * Whitelists a set of child elements for this element metadata.  This will
   * hide all declared child elements on the metadata instance that will be
   * created from this builder.
   */
  public ElementCreatorImpl whitelistElements(ElementKey<?, ?>... elementKeys) {
    return whitelistElements(Lists.newArrayList(elementKeys));
  }

  /**
   * Whitelists a set of child elements for this element metadata.  This will
   * hide all declared child elements on the metadata instance that will be
   * created from this builder.
   */
  private ElementCreatorImpl whitelistElements(
      Collection<ElementKey<?, ?>> elementKeys) {
    synchronized (registry) {
      if (elementWhitelist == null) {
        elementWhitelist = Sets.newHashSet();
      }
      elementWhitelist.addAll(elementKeys);
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
  private ElementCreatorImpl addElement(
      ElementKey<?, ?> childKey, Action action) {
    synchronized (registry) {
      QName id = childKey.getId();
      Preconditions.checkNotNull(id);
      if (action == Action.ADD) {
        elements.remove(id);
      }
      elements.put(id, new ElementInfo(childKey, action));
      return (ElementCreatorImpl) registry.build(
          elementKey, childKey, transformKey.getContext());
    }
  }

  @Override
  public ElementCreatorImpl setName(QName name) {
    return (ElementCreatorImpl) super.setName(name);
  }

  @Override
  public ElementCreatorImpl setRequired(boolean required) {
    return (ElementCreatorImpl) super.setRequired(required);
  }

  @Override
  public ElementCreatorImpl setVisible(boolean visible) {
    return (ElementCreatorImpl) super.setVisible(visible);
  }

  @Override
  public ElementCreatorImpl setVirtualValue(VirtualValue virtualValue) {
    return (ElementCreatorImpl) super.setVirtualValue(virtualValue);
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

  VirtualElementHolder getVirtualElementHolder() {
    return virtualElementHolder;
  }

  boolean isFlattened() {
    return flattened;
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
    check();
    return ElementTransform.create(this);
  }

  /**
   * Makes sure the state is consistent, throws {@link IllegalStateException} if
   * it notices something wrong.
   */
  private void check() {
    if (virtualElementHolder != null && cardinality != null) {
      if (cardinality == Cardinality.SINGLE) {
        if (virtualElementHolder.getSingleVirtualElement() == null) {
          throw new IllegalStateException(
              "Invalid element transform. "
              + "MultipleVirtualElement set on an element "
              + "with single cardinality for key " + elementKey);
        }
      } else {
        if (virtualElementHolder.getMultipleVirtualElement() == null) {
          throw new IllegalStateException(
              "Invalid element transform. "
              + "SingleVirtualElement set on an element "
              + "with multiple cardinality for key " + elementKey);
        }
      }
    }
  }

  /**
   * Holder for attribute information.  Stores the key + any additional
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
   * Holder for element information.  Stores the key + any additional
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
