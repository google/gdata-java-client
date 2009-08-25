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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.model.ElementMetadata.MultipleVirtualElement;
import com.google.gdata.model.ElementMetadata.SingleVirtualElement;
import com.google.gdata.model.Metadata.VirtualValue;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ContentCreationException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

/**
 * A static factory for adapting from {@link Path} instances to instances of
 * {@link VirtualValue}, {@link SingleVirtualElement} or
 * {@link MultipleVirtualElement}.  We adapt to both single and multiple at the
 * same time because we don't know if the path is singular or multiple until it
 * is bound to metadata, and these paths are used during the creation of
 * metadata.
 *
 * they would be useful for other path contexts.  Perhaps "Paths"?
 *
 * 
 */
class PathAdapter {

  /**
   * Creates an adapter that implements both {@link SingleVirtualElement} and
   * {@link MultipleVirtualElement} based on the path.
   */
  static ElementAdapter elementAdapter(Path path) {
    return new ElementAdapter(path);
  }

  /**
   * Creates an adapter that implements {@link VirtualValue} based on the path.
   */
  static ValueAdapter valueAdapter(Path path) {
    return new ValueAdapter(path);
  }

  /**
   * Returns element at the second-to-last position along the path, starting
   * with the given root element.  If at any point there is no valid element
   * this method will return {@code null}.
   *
   * @throws NullPointerException if the root or path is null.
   * @throws IllegalArgumentException if there are any repeating elements along
   *     the path.
   */
  static Element getParentElement(Path path, Element rootElement) {
    Preconditions.checkNotNull(path, "path");
    Preconditions.checkNotNull(rootElement, "rootElement");
    List<MetadataKey<?>> steps = path.getSteps();
    if (steps.isEmpty()) {
      return null;
    }
    Element parent = rootElement;
    for (int i = 0; i < steps.size() - 1; i++) {
      ElementKey<?, ?> childKey = (ElementKey<?, ?>) steps.get(i);
      parent = parent.getElement(childKey);
      if (parent == null) {
        return null;
      }
    }
    return parent;
  }

  /**
   * Returns the last element along the path, starting with the given root
   * element.  If at any point there is no valid element this method will return
   * {@code null}.  If the path ends in an attribute key this will return the
   * parent of that attribute, if it ends in an element key it will return that
   * element.
   *
   * @throws NullPointerException if the root or path is null.
   * @throws IllegalArgumentException if there are any repeating elements along
   *     the path.
   */
  static Element getFinalElement(Path path, Element rootElement) {
    Element parent = getParentElement(path, rootElement);
    if (path.selectsAttribute() || parent == null) {
      return parent;
    }
    return parent.getElement(path.getSelectedElementKey());
  }

  /**
   * Returns a collection of parent elements along the path.  This will throw
   * an exception if the path had multiple steps where the child was multiple
   * cardinality.
   */
  static Collection<? extends Element> getFinalElements(Path path,
      Element rootElement) {
    List<? extends Element> parents = ImmutableList.of(rootElement);

    for (MetadataKey<?> part : path.getSteps()) {
      if (part instanceof AttributeKey<?>) {
        break;
      }
      ElementKey<?, ?> childKey = (ElementKey<?, ?>) part;

      // If we already have multiple parents, all remaining elements must be
      // singular.
      if (parents.size() > 1) {
        List<Element> next = Lists.newArrayListWithCapacity(parents.size());
        for (Element e : parents) {
          next.add(e.getElement(childKey));
        }
        parents = next;
      } else {

        // If we only have a single parent, we get all children.
        Element parent = parents.get(0);
        parents = parents.get(0).getElements(childKey);
      }
      if (parents.isEmpty()) {
        return parents;
      }
    }
    return parents;
  }

  /**
   * Travels along the path, creating any elements that don't exist until it
   * gets to the second-to-last key along the path.
   */
  static Element createParentElement(Path path, Element rootElement)
      throws ParseException {
    Preconditions.checkNotNull(path, "path");
    Preconditions.checkNotNull(rootElement, "rootElement");
    List<MetadataKey<?>> steps = path.getSteps();
    if (steps.isEmpty()) {
      return null;
    }
    Element parent = rootElement;
    for (int i = 0; i < steps.size() - 1; i++) {
      ElementKey<?, ?> childKey = (ElementKey<?, ?>) steps.get(i);
      parent = getOrCreateChild(parent, childKey);
    }
    return parent;
  }

  /**
   * Creates an array of parent elements for the parent element.  This method
   * requires the metadata in hand because we need to know where any multiple
   * cardinality elements show up in the path.
   */
  static Collection<Element> createParentElements(Path path,
      Element rootElement, ElementMetadata<?, ?> rootMetadata,
      int elementCount) throws ParseException {
    Preconditions.checkNotNull(path, "path");
    Preconditions.checkNotNull(rootElement, "rootElement");

    List<MetadataKey<?>> steps = path.getSteps();
    if (steps.isEmpty()) {
      return ImmutableList.of();
    }
    Element parent = rootElement;
    List<Element> parents = null;
    ElementMetadata<?, ?> parentMetadata = rootMetadata;
    for (int i = 0; i < steps.size() - 1; i++) {
      ElementKey<?, ?> childKey = (ElementKey<?, ?>) steps.get(i);
      ElementMetadata<?, ?> childMetadata = (parentMetadata == null)
          ? null : parentMetadata.bindElement(childKey);

      // If parents is non-null, we already found a multiple-cardinality
      // element, so we create a single child element for each parent.
      if (parents != null) {
        checkNotMultiple(childMetadata);

        List<Element> children = Lists.newArrayListWithCapacity(parents.size());
        for (Element p : parents) {
          children.add(getOrCreateChild(p, childKey));
        }
        parents = children;
        parentMetadata = childMetadata;
        continue;
      }

      // If we haven't found our multiple cardinality element, and this element
      // is singular, we create a single child.
      if (childMetadata == null
          || childMetadata.getCardinality() == Cardinality.SINGLE) {
        Element child = getOrCreateChild(parent, childKey);
        parent = child;
        parentMetadata = childMetadata;
        continue;
      }

      // This is our first multiple cardinality element, so we create the
      // appropriate number of child elements and add them to the parent.
      List<Element> children = Lists.newArrayListWithCapacity(elementCount);
      for (int j = 0; j < elementCount; j++) {
        try {
          Element child = Element.createElement(childKey);
          parent.addElement(child);
          children.add(child);
        } catch (ContentCreationException e) {
          throw new ParseException(e);
        }
      }
      parents = children;
      parentMetadata = childMetadata;
    }

    if (parents != null) {
      return parents;
    }
    return ImmutableList.of(parent);
  }

  /**
   * Travels along the path, creating any elements that don't exist until it
   * gets to the final key.  If the final key is an element, it creates that
   * element if necessary and returns it.  If the final key is an attribute, it
   * returns the parent of that attribute.
   */
  static Element createFinalElement(Path path, Element rootElement)
      throws ParseException {
    Element parent = createParentElement(path, rootElement);
    if (path.selectsAttribute() || parent == null) {
      return parent;
    }
    ElementKey<?, ?> childKey = path.getSelectedElementKey();
    Element child = parent.getElement(childKey);
    if (child == null) {
      try {
        child = Element.createElement(childKey);
        parent.addElement(child);
      } catch (ContentCreationException e) {
        throw new ParseException(e);
      }
    }
    return child;
  }

  /**
   * Gets an existing child from the parent or creates it if needed.
   */
  private static Element getOrCreateChild(
      Element parent, ElementKey<?, ?> childKey) throws ParseException {
    Element child = parent.getElement(childKey);
    if (child != null) {
      return child;
    }
    try {
      child = Element.createElement(childKey);
      parent.addElement(child);
      return child;
    } catch (ContentCreationException e) {
      throw new ParseException(e);
    }
  }

  /**
   * Checks that a particular piece of metadata is not multiple cardinality.
   *
   * @throws IllegalStateException if the metadata is multiple cardinality
   */
  private static void checkNotMultiple(ElementMetadata<?, ?> meta) {
    if (meta != null && meta.getCardinality() != Cardinality.SINGLE) {
      throw new IllegalStateException("Metadata for key " + meta.getKey()
          + " represents a multiple-cardinality element."
          + " The path cannot contain more than one multiple-cardinality"
          + " element.");
    }
  }

  /**
   * Generates the value for a given attribute key and metadata.  If the
   * attribute metadata is null this will just return the attribute value, but
   * if the metadata is not null it will have the metadata generate the value.
   */
  static Object generateAttributeValue(
      Element element, ElementMetadata<?, ?> metadata,
      AttributeKey<?> attKey, AttributeMetadata<?> attMeta) {
    if (metadata != null && attMeta != null) {
      return attMeta.generateValue(element, metadata);
    }
    return element.getAttributeValue(attKey);
  }

  /**
   * Parses the value for an attribute.
   */
  static void parseAttributeValue(
      Element element, ElementMetadata<?, ?> metadata,
      AttributeKey<?> attKey, AttributeMetadata<?> attMeta, Object value)
      throws ParseException {
    if (attMeta == null) {
      element.setAttributeValue(attKey, value);
    } else {
      attMeta.parseValue(element, metadata, value);
    }
  }

  /**
   * Generates the text value for a given key and metadata.  If the metadata is
   * null this will just return the text value, but if the metadata is not null
   * it will have the metadata generate the value.
   */
  static Object generateTextValue(
      Element element, ElementMetadata<?, ?> metadata) {
    if (metadata != null) {
      return metadata.generateValue(element, metadata);
    }
    return element.getTextValue();
  }

  /**
   * Parses the text content for an element.
   */
  static void parseTextValue(
      Element element, ElementMetadata<?, ?> metadata, Object value)
      throws ParseException {
    if (metadata != null) {
      metadata.parseValue(element, metadata, value);
    } else {
      element.setTextValue(value);
    }
  }

  /**
   * An adapter to {@link SingleVirtualElement} and
   * {@link MultipleVirtualElement}.  Allows a path to be used as a virtual
   * element for parsing and generation.
   */
  static class ElementAdapter implements SingleVirtualElement,
      MultipleVirtualElement {

    // The underlying path.
    private final Path path;

    /**
     * Constructs an element adapter with a particular path.
     */
    ElementAdapter(Path path) {
      this.path = path;
    }

    /**
     * Generates a single element on the parent by using this path.  This
     * will follow the path to its end, and then use that same element content
     * as the returned element, after wrapping it with the correct key (id).
     *
     * elements along it.
     */
    public Element generateSingle(Element parent,
        ElementMetadata<?, ?> parentMetadata, ElementMetadata<?, ?> metadata) {
      Preconditions.checkState(path.selectsElement(),
          "An attribute path cannot be used to generate elements.");

      Element element = getFinalElement(path, parent);
      if (element == null) {
        return null;
      }

      try {
        ElementKey<?, ?> realKey = mergeKeys(
            element.getElementKey(), metadata.getKey());
        return Element.createElement(realKey, element);
      } catch (ContentCreationException e) {
        throw new IllegalArgumentException("Invalid metadata", e);
      }
    }

    /**
     * Parses a single element using this path.  This will follow the path until
     * the second to last element on the path, and then parse into the final
     * element using the real parent instead of the path's parent.
     *
     * elements along it.
     */
    public void parse(Element parent,  ElementMetadata<?, ?> parentMetadata,
        Element element, ElementMetadata<?, ?> metadata) throws ParseException {
      Preconditions.checkState(path.selectsElement(),
          "An attribute path cannot be used to parse elements.");

      parent = createParentElement(path, parent);
      ElementKey<?, ?> lastKey = path.getSelectedElementKey();
      try {
        Element child = Element.createElement(lastKey, element);
        parent.addElement(child);
      } catch (ContentCreationException e) {
        throw new ParseException(e);
      }
    }

    /**
     * Generate multiple elements based on this path.  This only allows a single
     * multiple cardinality element in the path, but that multiple cardinality
     * element can be anywhere in the path.  Once found we only follow single
     * cardinality children after that point.
     *
     * cardinality along the path.
     */
    public Collection<? extends Element> generateMultiple(Element parent,
        ElementMetadata<?, ?> parentMetadata, ElementMetadata<?, ?> metadata) {
      Preconditions.checkState(path.selectsElement(),
          "An attribute path cannot be used to generate elements.");

      Collection<? extends Element> elements = getFinalElements(path, parent);
      if (elements.isEmpty()) {
        return elements;
      }

      List<Element> result = Lists.newArrayListWithCapacity(elements.size());
      for (Element e : elements) {
        try {
          ElementKey<?, ?> realKey = mergeKeys(
              e.getElementKey(), metadata.getKey());
          result.add(Element.createElement(realKey, e));
        } catch (ContentCreationException ex) {
          throw new IllegalArgumentException("Invalid metadata", ex);
        }
      }
      return result;
    }

    /**
     * Parse multiple elements using the path.  Because the path only allows a
     * single multiple cardinality element, we can correctly parse into the
     * appropriate child locations by matching up our elements with the parents,
     * and creating one element per passed in element once we hit a multiple
     * cardinality path element.
     */
    public void parse(Element parent,  ElementMetadata<?, ?> parentMetadata,
        Collection<Element> elements, ElementMetadata<?, ?> metadata)
        throws ParseException {
      Preconditions.checkState(path.selectsElement(),
          "An attribute path cannot be used to parse elements.");

      Path bound = path.toAbsolute(parentMetadata);

      Collection<Element> parents = createParentElements(
          bound, parent, parentMetadata, elements.size());

      ElementKey<?, ?> childKey = bound.getSelectedElementKey();
      ElementMetadata<?, ?> childMetadata = bound.getSelectedElement();

      Iterator<Element> pIter = parents.iterator();
      Iterator<Element> eIter = elements.iterator();

      // If we have multiple parents, the child metadata must not be multiple.
      if (parents.size() > 1) {
        checkNotMultiple(childMetadata);

        while (pIter.hasNext() && eIter.hasNext()) {
          Element p = pIter.next();
          p.addElement(eIter.next());
        }
        return;
      }

      // If we only have a single parent and more than one child, the child
      // metadata must be multiple cardinality.
      if (elements.size() > 1 && childMetadata != null
          && childMetadata.getCardinality() == Cardinality.SINGLE) {
        throw new IllegalStateException("Metadata for key " + childKey
            + " represents a single-cardinality element."
            + " The path must contain at least one multiple-cardinality"
            + " element in order to parse multiple elements.");
      }

      // Add all elements to the single parent.
      parent = pIter.next();
      while (eIter.hasNext()) {
        parent.addElement(eIter.next());
      }
    }

    /**
     * Merges the key from the metadata with the source key to get the "real"
     * key for a moved element.
     */
    private static ElementKey<?, ?> mergeKeys(ElementKey<?, ?> sourceKey,
        ElementKey<?, ?> metadataKey) {
      if (!metadataKey.getId().equals(sourceKey.getId())) {
        return ElementKey.of(metadataKey.getId(), sourceKey.getDatatype(),
            sourceKey.getElementType());
      }
      return sourceKey;
    }
  }

  /**
   * An adapter from a path to {VirtualValue}, this allows a path to represent
   * the value of an attribute or element.
   */
  static class ValueAdapter implements VirtualValue {

    // The path being adapted.
    private final Path path;

    /**
     * Constructs an adapter with a particular path.
     */
    ValueAdapter(Path path) {
      this.path = path;
    }

    /**
     * Generate a text value through the path.  If the path ends in an element,
     * the value will be the text content of the final element.  If the path
     * ends in an attribute, the value will be the value of that attribute.
     */
    public Object generate(Element element, ElementMetadata<?, ?> metadata) {
      Path bound = path.toAbsolute(metadata);

      element = getFinalElement(bound, element);
      if (element == null) {
        return null;
      }

      // Generate the value at the end of the path.
      if (bound.selectsAttribute()) {
        return generateAttributeValue(element, bound.getSelectedElement(),
            bound.getSelectedAttributeKey(), bound.getSelectedAttribute());
      } else {
        return generateTextValue(element, bound.getSelectedElement());
      }
    }

    /**
     * Parses a value through a path.  This will find the element or attribute
     * that is at the end of the path, and parse the value into the element's
     * text content or the attribute's value as appropriate.
     */
    public void parse(Element element, ElementMetadata<?, ?> metadata,
        Object value) throws ParseException {

      Path bound = path.toAbsolute(metadata);
      element = createFinalElement(path, element);

      if (bound.selectsAttribute()) {
        parseAttributeValue(element, bound.getSelectedElement(),
            bound.getSelectedAttributeKey(), bound.getSelectedAttribute(),
            value);
      } else {
        parseTextValue(element, bound.getSelectedElement(), value);
      }
    }
  }

  private PathAdapter() {}
}
