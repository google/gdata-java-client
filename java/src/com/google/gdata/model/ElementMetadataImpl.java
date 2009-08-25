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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.model.ElementCreatorImpl.AttributeInfo;
import com.google.gdata.model.ElementCreatorImpl.ElementInfo;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ContentCreationException;
import com.google.gdata.wireformats.ObjectConverter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Immutable implementation of the element metadata.  This class delegates to
 * the schema for binding to other contexts and for retrieving children, and
 * uses an {@link AdaptationRegistry} for dealing with adaptations.
 *
 * <p>Each instance of this class is bound to a specific schema, parent key,
 * element key, and metadata context, see {@link MetadataImpl}.
 *
 * 
 */
final class ElementMetadataImpl<D, E extends Element> extends MetadataImpl<D>
    implements ElementMetadata<D, E> {

  private static final ElementValidator DEFAULT_VALIDATOR =
      new MetadataValidator();

  // Element metadata properties.
  private final ElementKey<D, E> elemKey;
  private final ElementKey<D, E> sourceKey;
  private final Cardinality cardinality;
  private final boolean isContentRequired;
  private final ElementValidator validator;
  private final Object properties;
  private final VirtualElementHolder virtualElementHolder;
  private final boolean isFlattened;

  /** Metadata for element's attributes and child elements. */
  private final ImmutableMap<QName, AttributeKey<?>> attributes;
  private final ImmutableMap<QName, AttributeKey<?>> renamedAttributes;
  private final ImmutableMap<QName, ElementKey<?, ?>> elements;
  private final ImmutableMap<QName, ElementKey<?, ?>> renamedElements;

  /** Adaptation helper for dealing with adaptors on this element. */
  private final AdaptationRegistry adaptations;

  /**
   * Constructs a new immutable element metadata instance from the given
   * declared metadata.
   */
  ElementMetadataImpl(Schema schema, ElementTransform transform,
      ElementKey<?, ?> parent, ElementKey<D, E> key, MetadataContext context) {
    super(schema, transform, parent, key, context);

    this.elemKey = key;
    TransformKey transformSource = transform.getSource();
    if (transformSource != null) {
      // Use the ID of the transform source for the source key.
      ElementKey<D, E> transformSourceKey = ElementKey.of(
          transformSource.getKey().getId(), key.getDatatype(),
          key.getElementType());

      if (transformSourceKey.equals(elemKey)) {
        this.sourceKey = elemKey;
      } else {
        this.sourceKey = transformSourceKey;
      }
    } else {
      this.sourceKey = elemKey;
    }
    
    transform = ElementTransform.mergeSource(schema, key, transform, context);
    
    this.cardinality = firstNonNull(
        transform.getCardinality(), Cardinality.SINGLE);
    this.isContentRequired = firstNonNull(transform.getContentRequired(), true);
    this.validator = firstNonNull(transform.getValidator(), DEFAULT_VALIDATOR);
    this.properties = transform.getProperties();
    this.virtualElementHolder = transform.getVirtualElementHolder();
    this.isFlattened = transform.isFlattened();

    this.attributes = getAttributes(transform.getAttributes().values());
    this.renamedAttributes = getRenamedAttributes();
    this.elements = getElements(transform.getElements().values());
    this.renamedElements = getRenamedElements();

    if (transform.getAdaptations().isEmpty()) {
      this.adaptations = null;
    } else {
      this.adaptations = AdaptationRegistryFactory.create(schema, transform);
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
   * Creates an immutable map of renamed attributes from the collection of
   * attribute keys. This binds the attributes and checks if they have a
   * different name under the context of this metadata, and returns any
   * attributes with an alternate name in the map.
   */
  private ImmutableMap<QName, AttributeKey<?>> getRenamedAttributes() {
    Builder<QName, AttributeKey<?>> builder = ImmutableMap.builder();
    for (AttributeKey<?> key : attributes.values()) {
      AttributeMetadata<?> bound = bindAttribute(key);
      QName boundName = bound.getName();
      if (!boundName.equals(key.getId())) {
        builder.put(boundName, key);
      }
    }
    return builder.build();
  }

  /**
   * Creates an immutable map of child elements from the given collection of
   * element info objects. The info objects are used in transforms to allow
   * bumping elements to the end of the list, to change their order, but once we
   * are creating the element metadata the order is set and we just need the
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
   * Creates an immutable map of renamed child elements from the collection of
   * element keys. This gets the transform for each child and checks if there
   * is a new, different name, and if so creates a map of those new names back
   * to the appropriate key, for use during parsing.
   */
  private ImmutableMap<QName, ElementKey<?, ?>> getRenamedElements() {

    Map<QName, ElementKey<?, ?>> renamed = Maps.newLinkedHashMap();

    for (ElementKey<?, ?> key : elements.values()) {
      ElementTransform transform = schema.getTransform(sourceKey, key, context);
      QName childName = transform.getName();
      if (childName != null && !childName.equals(key.getId())) {

        // We only use the first renamed element if multiple elements are named
        // the same.
        if (!renamed.containsKey(childName)) {
          renamed.put(childName, key);
        }
      }
    }
    return ImmutableMap.copyOf(renamed);
  }

  /**
   * Adapts this element metadata to a different kind, using the provided key.
   * Will return {@code null} if no adaptation on the given kind exists. If an
   * adaptation does exist it will bind the adaptation under the same parent and
   * context as this element.
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
    // First check any renamed attributes, as those take precedence.
    if (!renamedAttributes.isEmpty()) {
      AttributeKey<?> attKey = renamedAttributes.get(id);
      if (attKey != null) {
        return attKey;
      }
    }
    if (!attributes.isEmpty()) {
      AttributeKey<?> attKey = attributes.get(id);
      if (attKey != null) {
        return attKey;
      }

      // For wildcarded ids, iterate and return the first matching attribute
      if (id.matchesAnyNamespace()) {
        for (Map.Entry<QName, AttributeKey<?>> attrEntry : 
             attributes.entrySet()) {
          if (id.matches(attrEntry.getKey())) {
            return attrEntry.getValue();
          }  
        }
      } else if (!id.matchesAnyLocalName()) {
        // See if there is a foo:* match for the given id.
        attKey = attributes.get(toWildcardLocalName(id));
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
    // First check any renamed elements, as those take precedence.
    if (!renamedElements.isEmpty()) {
      ElementKey<?, ?> childKey = renamedElements.get(id);
      if (childKey != null) {
        return childKey;
      }
    }
    if (!elements.isEmpty()) {
      ElementKey<?, ?> childKey = elements.get(id);
      if (childKey != null) {
        return childKey;
      }
      
      // For wildcarded ids, iterate and return the first matching element
      if (id.matchesAnyNamespace()) {
        for (Map.Entry<QName, ElementKey<?, ?>> elemEntry : 
             elements.entrySet()) {
          if (id.matches(elemEntry.getKey())) {
            return elemEntry.getValue();
          }  
        }
      } else if (!id.matchesAnyLocalName()) {
        // See if there is a foo:* match for the provided namespace.
        childKey = elements.get(toWildcardLocalName(id));
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
    return schema.bind(parent, elemKey, context);
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

  public boolean isSelected(Element e) {
    return isVisible();
  }
  
  public boolean isFlattened() {
    return isFlattened;
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
    return schema.bind(sourceKey, key, context);
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
    return schema.bind(sourceKey, key, context);
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
    if (!super.parse(element, metadata, value)) {
      element.setTextValue(
          ObjectConverter.getValue(value, elemKey.getDatatype()));
    }
  }

  public SingleVirtualElement getSingleVirtualElement() {
    if (cardinality != Cardinality.SINGLE) {
      return null;
    }
    if (virtualElementHolder != null) {
      return virtualElementHolder.getSingleVirtualElement();
    }
    return null;
  }

  public MultipleVirtualElement getMultipleVirtualElement() {
    if (cardinality == Cardinality.SINGLE) {
      return null;
    }
    if (virtualElementHolder != null) {
      return virtualElementHolder.getMultipleVirtualElement();
    }
    return null;
  }

  public E createElement() throws ContentCreationException {
    return Element.createElement(elemKey);
  }

  public XmlNamespace getDefaultNamespace() {
    // The default implementation uses the default namespace for the in-use name
    return getName().getNs();
  }

  /**
   * Set of namespaces referenced by this element's metadata and all child
   * attributes and elements.   This field is lazily initialized by the first
   * call to getReferencedNamespaces().
   */
  private Collection<XmlNamespace> referencedNamespaces = null;

  public Collection<XmlNamespace> getReferencedNamespaces() {
    // The referencedNamespaces field is lazily initialized because it is
    // only required for top-level types.   A race condition is
    // harmless and will just result in multiple computations.
    if (referencedNamespaces == null) {
      ImmutableSet.Builder<XmlNamespace> builder = ImmutableSet.builder();
      Set<ElementKey<?, ?>> added = Sets.newHashSet();
      addReferencedNamespaces(this, builder, added);
      referencedNamespaces = builder.build();
    }
    return referencedNamespaces;
  }

  private static void addReferencedNamespaces(ElementMetadata<?, ?> metadata,
      ImmutableSet.Builder<XmlNamespace> builder, Set<ElementKey<?, ?>> added) {

    // Avoid recursive looping
    if (added.contains(metadata.getKey())) {
      return;
    }
    added.add(metadata.getKey());

    // Add namespace for this element (if any)
    XmlNamespace elemNs = metadata.getName().getNs();
    if (elemNs != null) {
      builder.add(elemNs);
    }

    // Add namespace for all attributes (if any)
    for (AttributeKey<?> attrKey : metadata.getAttributes()) {
      AttributeMetadata<?> attrMetadata = metadata.bindAttribute(attrKey);
      XmlNamespace attrNs = attrMetadata.getName().getNs();
      if (attrNs != null) {
        builder.add(attrNs);
      }
    }

    // Add namespace for all child elements (recursively)
    for (ElementKey<?, ?> elemKey : metadata.getElements()) {
      ElementMetadata<?, ?> childMetadata = metadata.bindElement(elemKey);
      addReferencedNamespaces(childMetadata, builder, added);
    }
  }

  /**
   * Returns an id of the form ns:*, if the given id does not already represent
   * the * localname.
   */
  private QName toWildcardLocalName(QName id) {
    return new QName(id.getNs(), QName.ANY_LOCALNAME);
  }
}
