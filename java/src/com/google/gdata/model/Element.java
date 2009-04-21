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
import com.google.gdata.util.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gdata.model.ContentModel.Cardinality;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ContentCreationException;
import com.google.gdata.wireformats.ContentValidationException;
import com.google.gdata.wireformats.ObjectConverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data element in an instance document. Contains attributes,
 * child elements, and a text node.
 *
 * <p>The various setter methods provided by this class return {@code this}
 * so setter invocations can be chained, as in the following example:
 *
 * <p><pre>
 * Element who = new Element(METADATA)
 *     .addAttribute(ATTR_METADATA, "value")
 *     .addElement(
 *         new Element(EXT_METADATA_NOEXT)
 *             .setValue("yolk"));
 * </pre>
 *
 * <p>Subclasses are expected to follow the same model for any setter
 * methods they provide.
 *
 * @see #setTextValue(Object)
 * @see #addAttribute(Attribute)
 * @see #addAttribute(AttributeKey, Attribute)
 * @see #addAttribute(AttributeKey, Object)
 * @see #addElement(Element)
 * @see #addElement(ElementKey, Element)
 */
public class Element {

  private static final Logger LOGGER =
      Logger.getLogger(Element.class.getName());

  /**
   * Indicates that the element is constant after construction ({@code false}
   * by default).
   */
  protected boolean immutable = false;

  /**
   * Element metadata, used for looking up attributes and child elements.
   */
  protected ElementMetadata<?, ?> metadata;

  /**
   * Metadata context this element is operating in.
   */
  protected MetadataContext context = null;

  /**
   * Map of all attributes that were added to this element, in the order they
   * were added to the element.
   */
  private Map<QName, Attribute> attributes = null;

  /**
   * Map of child elements, keyed by their ID.  The value of a map
   * entry is either an instance of the class (for {@link Cardinality#SINGLE}
   * elements) or a list of instances (for {@link Cardinality#MULTIPLE}), or
   * a set of instances (for {@link Cardinality#SET}).  This map is maintained
   * in the order the child elements were added to this element.
   */
  protected Map<QName, Object> elements = null;

  /**
   * Construct element and associate with metadata.
   *
   * @param metadata element metadata
   */
  public Element(ElementMetadata<?, ?> metadata) {
    setMetadata(metadata);
  }

  /**
   * Copy constructor that initializes a new Element instance to have identical
   * contents to another instance, using a shared reference to the same child
   * element instances. Metadata is given by caller.
   *
   * @param metadata element metadata to associate with copy
   * @param element element to copy data from
   */
  public Element(ElementMetadata<?, ?> metadata, Element element) {
    this.immutable = element.immutable;
    this.value = element.value;
    if (element.attributes != null) {
      attributes = Maps.newLinkedHashMap(element.attributes);
    }
    if (element.elements != null) {
      elements = Maps.newLinkedHashMap(element.elements);
    }

    // Set the metadata and context for this element.
    setMetadata(metadata);
  }

  /**
   * Construct a generic undeclared element with the specified qualified name.
   *
   * @param qName qualified name
   */
  public Element(QName qName) {
    this(createUndeclaredElementMetadata(ElementKey.of(qName)));
  }

  /**
   * Creates undeclared metadata for use in a simple element.
   */
  private static <D, T extends Element> ElementMetadata<D, T>
      createUndeclaredElementMetadata(ElementKey<D, T> key) {
    return new ElementMetadataImpl<D, T>(key);
  }

  /**
   * @return true if element is immutable
   */
  public final boolean isImmutable() {
    return immutable;
  }

  /**
   * Sets element to be immutable or not.
   * as immutable you can't make it mutable.
   * and attributes and affect not just the value but also the container.
   *
   * @param isImmutable true if element is immutable
   */
  public final void setImmutable(boolean isImmutable) {
    this.immutable = isImmutable;
  }

  /**
  * Throws an {@link IllegalStateException} if this instance is immutable.
  * Should only be used in a value-setter method.
  */
  protected final void throwExceptionIfImmutable() {
    if (immutable) {
      throw new IllegalStateException(getElementId()
          + " instance is read only");
    }
  }

  /**
   * Returns the key to this element.
   */
  public ElementKey<?, ?> getElementKey() {
    return metadata.getKey();
  }

  /**
   * Get the id of this element.
   */
  public QName getElementId() {
    return getElementKey().getId();
  }

  /**
   * Return metadata for the current context.
   */
  public ElementMetadata<?, ?> getMetadata() {
    return metadata;
  }

  /**
   * Changes the registry this element is a part of.  This will regenerate the
   * metadata for the new registry instead of the old registry.
   */
  public void setRegistry(MetadataRegistry registry) {
    setMetadata(registry.bind(
        metadata.getParent(), metadata.getKey(), metadata.getContext()));
  }

  /**
   * Changes the metadata of this element to a different set of metadata.
   */
  void setMetadata(ElementMetadata<?, ?> metadata) {
    Preconditions.checkNotNull(metadata, "Metadata cannot be null.");

    // Ignore if we already have this metadata.
    if (this.metadata == metadata) {
      return;
    }

    this.metadata = metadata;
    this.context = metadata.getContext();

    resolveMetadata();
  }

  /**
   * Synchronize the state of declared elements and attributes based on changes
   * to the element's metadata.
   */
  private void resolveMetadata() {
    resolveAttributeMetadata();
    resolveElementMetadata();
  }

  /**
   * Resolve changes to attribute metadata.  This will loop through the
   * attributes, binding them to the new metadata.
   */
  private void resolveAttributeMetadata() {
    if (attributes == null) {
      return;
    }
    for (Attribute attribute : attributes.values()) {
      AttributeMetadata<?> newMeta = metadata.bindAttribute(
          attribute.getAttributeKey());
      if (newMeta != null) {
        attribute.setMetadata(newMeta);
      }
    }
  }

  /**
   * Resolve changes to child metadata.  This will loop through the child
   * elements, binding their metadata to the new metadata. This will also
   * handle a change in cardinality from multiple to single, and throw an
   * {@link IllegalArgumentException} if the new metadata would set a child
   * to single that has multiple elements.
   */
  private void resolveElementMetadata() {
    if (elements == null) {
      return;
    }
    Iterator<Map.Entry<QName, Object>> iter = elements.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<QName, Object> entry = iter.next();
      Object obj = entry.getValue();
      ElementMetadata<?, ?> newMeta;
      if (obj instanceof Collection<?>) {
        Collection<Element> elementCollect = castElementCollection(obj);

        switch(elementCollect.size()) {

          // Empty list, shouldn't actually be possible, but if it occurs
          // we remove the entry from the element map.
          case 0:
            iter.remove();
            break;

          // Single element, this occurs if we had multiple then removed
          // them until only one was left.  In this case we replace the
          // value with the single value.
          case 1:
            Element single = elementCollect.iterator().next();
            newMeta = metadata.bindElement(single.getElementKey());
            if (newMeta != null) {
              single.setMetadata(newMeta);
            }
            entry.setValue(single);
            break;

          // Greater than 1 element in the collectiuon.  We create metadata
          // for each element in the collection, and if any of them are of
          // {@code SINGLE} cardinality it is an error.
          default:
            for (Element element : elementCollect) {
              newMeta = metadata.bindElement(element.getElementKey());
              if (newMeta != null) {
                if (newMeta.getCardinality() == Cardinality.SINGLE) {
                  throw new IllegalArgumentException(
                      "Element metadata states that " + element.getElementId()
                      + " is not repeating, but " + elementCollect.size()
                      + " elements were found.");
                }
                element.setMetadata(newMeta);
              }
            }
            break;
        }
      } else {
        Element single = (Element) obj;
        newMeta = metadata.bindElement(single.getElementKey());
        if (newMeta != null) {
          single.setMetadata(newMeta);
        }
      }
    }
  }

  /**
   * Returns the current metadata context of this element.
   */
  public MetadataContext getContext() {
    return context;
  }

  /**
   * Binds a new context for this element.  This sets the metadata to the
   * transformed metadata based on the new context, and then propogates the
   * context change down to any attributes or elements.
   */
  public void bind(MetadataContext newContext) {
    if (context != null && context.equals(newContext)) {
      return;
    }
    setMetadata(metadata.bind(newContext));
  }

  /**
   * Returns an iterator over all element attributes with a well-defined order
   * of iteration.  All declared attributes are returned first (by order of
   * declaration), followed by undeclared attributes in the order in which they
   * were added to the element.
   *
   * @return attribute iterator
   */
  public Iterator<Attribute> getAttributeIterator() {
    return new AttributeIterator();
  }

  private enum Mode {DECLARED, UNDECLARED, DONE}

  /**
   * The AttributeIterator class provider the ability to iterate over all
   * attributes of an element with a well-defined order.   Declared attributes
   * will be returned first (in order by declaration) followed by undeclared
   * attributes in the order that they were added to the element.
   */
  private class AttributeIterator implements Iterator<Attribute> {

    private Attribute nextAttribute;
    Iterator<AttributeKey<?>> metadataIterator;
    private Iterator<Attribute> attributeIterator;
    private Mode mode = Mode.DECLARED;

    private AttributeIterator() {
      metadataIterator = metadata.getAttributes().iterator();
      attributeIterator = (attributes == null) ? null
          : attributes.values().iterator();
      nextAttribute = findNextAttribute();
    }

    public boolean hasNext() {
      return nextAttribute != null;
    }

    public Attribute next() {
      if (nextAttribute == null) {
        throw new NoSuchElementException("No remaining attributes");
      }
      Attribute retval = nextAttribute;
      nextAttribute = findNextAttribute();
      return retval;
    }

    public void remove() {
      throw new UnsupportedOperationException(
          "Removal not supported on attribute iterator");
    }

    /**
     * Find the next attribute in the correct order.
     */
    private Attribute findNextAttribute() {

      while (mode != Mode.DONE) {
        Attribute next = null;
        switch(mode) {
          case DECLARED:
            next = findNextDeclaredAttribute();
            break;
          case UNDECLARED:
            next = findNextUndeclaredAttribute();
            break;
          default:
            break;
        }
        if (next != null) {
          return next;
        }
      }

      // Nothing remains.
      return null;
    }

    /**
     * Finds the next declared attribute, or null if no more exist.
     */
    private Attribute findNextDeclaredAttribute() {
      if (metadataIterator != null) {
        while (metadataIterator.hasNext()) {
          AttributeKey<?> nextKey = metadataIterator.next();
          if (ElementCreatorImpl.ATTRIBUTE_MARKER == nextKey) {
            mode = Mode.UNDECLARED;
            return null;
          }
          Attribute attribute = getAttribute(nextKey.getId());
          if (attribute != null) {
            return attribute;
          }
        }

        // No more declared attributes, turn the iterator off.
        metadataIterator = null;
      }

      // Check undeclared next.
      mode = Mode.UNDECLARED;
      return null;
    }

    /**
     * Finds the next valid undeclared attribute in the map.
     */
    private Attribute findNextUndeclaredAttribute() {
      if (attributeIterator != null) {
        while (attributeIterator.hasNext()) {
          Attribute attribute = attributeIterator.next();
          if (!metadata.isDeclared(attribute.getAttributeKey())) {
            return attribute;
          }
        }

        // No more attributes, turn the iterator off.
        attributeIterator = null;
      }

      // Go back and check for any remaining declared attributes if needed.
      mode = metadataIterator != null && metadataIterator.hasNext()
          ? Mode.DECLARED : Mode.DONE;
      return null;
    }
  }

  /**
   * Returns the number of attributes present on this element.
   *
   * @return count of attributes
   */
  public int getAttributeCount() {
    return (attributes != null) ? attributes.size() : 0;
  }

  /**
   * Returns true if the element has an attribute with the given id.
   */
  public boolean hasAttribute(QName id) {
    return (attributes == null) ? false : attributes.containsKey(id);
  }

  /**
   * Returns true if the element has an attribute with the given key.
   */
  public boolean hasAttribute(AttributeKey<?> childKey) {
    return hasAttribute(childKey.getId());
  }

  /**
   * Retrieve an attribute by id.
   */
  public Attribute getAttribute(QName id) {
    return (attributes == null) ? null : attributes.get(id);
  }

  /**
   * Retrieve an attribute by key.
   */
  public Attribute getAttribute(AttributeKey<?> key) {
    return getAttribute(key.getId());
  }

  /**
   * Get the value of an attribute by id.
   */
  public Object getAttributeValue(QName id) {
    Attribute attribute = getAttribute(id);
    return (attribute == null) ? null : attribute.getValue();
  }

  /**
   * Get the value of an attribute by key.
   */
  public <T> T getAttributeValue(AttributeKey<T> key) {
    Attribute attribute = getAttribute(key);
    return (attribute == null) ? null : attribute.getValue(key);
  }

  /**
   * Add attribute by id and value.  If the value is {@code null} this is
   * equivalent to removing the attribute with the given id.
   */
  public Element addAttribute(QName id, Object attrValue) {
    return addAttribute(AttributeKey.of(id), attrValue);
  }

  /**
   * Add attribute by value. If the value is {@code null} the value will be
   * removed.
   *
   * @param key attribute key that is being added.
   * @param attrValue attribute value or {@code null} to remove.
   */
  public Element addAttribute(AttributeKey<?> key, Object attrValue) {
    Attribute att = null;
    if (attrValue instanceof Attribute) {
      att = (Attribute) attrValue;
      key = att.getAttributeKey();
    } else if (attrValue != null) {

      AttributeMetadata<?> attMeta;
      if (metadata.isDeclared(key)) {
        attMeta = metadata.bindAttribute(key);
      } else {
        attMeta = createUndeclaredAttributeMetadata(key);
      }
      att = new Attribute(attMeta, attrValue);
    }

    // This will be a remove if attrValue was null.
    return addAttribute(key, att);
  }

  /**
   * Creates an undeclared attribute metadata for the given key.
   */
  private static <T> AttributeMetadata<T> createUndeclaredAttributeMetadata(
      AttributeKey<T> key) {
    return new AttributeMetadataImpl<T>(key);
  }

  /**
   * Add an attribute object to this element.
   */
  public Element addAttribute(Attribute attribute) {
    addAttribute(attribute.getAttributeKey(), attribute);
    return this;
  }

  /**
   * Add an attribute by id.
   */
  public Element addAttribute(QName id, Attribute attribute) {
    if (attribute == null) {
      removeAttribute(id);
      return this;
    }

    return addAttribute(
        AttributeKey.of(id, attribute.getAttributeKey().getDatatype()),
        attribute);
  }

  /**
   * Add an attribute by qname.  If attribute is {@code null} the attribute will
   * be removed.
   *
   * @param attKey attribute key.
   * @param attribute attribute or {@code null} to remove.
   * @return this to enable chaining.
   */
  public Element addAttribute(AttributeKey<?> attKey, Attribute attribute) {

    // This is a remove, not an add, if attribute is null.
    if (attribute == null) {
      removeAttribute(attKey);
      return this;
    }

    if (attributes == null) {
      attributes = new LinkedHashMap<QName, Attribute>();
    }
    if (metadata.isDeclared(attKey)) {
      attribute.setMetadata(metadata.bindAttribute(attKey));
    }
    attributes.put(attKey.getId(), attribute);
    return this;
  }

  /**
   * Remove attribute (if present).
   *
   * @param id the qualified name of the attribute.
   * @return this element
   */
  public Attribute removeAttribute(QName id) {
    return (attributes == null) ? null : attributes.remove(id);
  }

  /**
   * Remove attribute (if present).
   *
   * @param key the key of the attribute.
   * @return this element
   */
  public Attribute removeAttribute(AttributeKey<?> key) {
    return removeAttribute(key.getId());
  }

  /**
   * Returns an iterator over all child elements. The order of iteration is
   * predictable, in that elements that have been declared in
   * {@link ElementMetadata} will appear in the order of declaration, with
   * repeating elements of a given type appearing in the order they have been
   * added. Elements of any undeclared types (if any) will appear last in the
   * iteration, in the order they were added.
   *
   * @return iterator over child elements ordered by declaration and addition.
   */
  public Iterator<Element> getElementIterator() {
    return new ElementIterator();
  }

  /**
   * The ElementIterator class implements the {@link Iterator} interface over
   * the declared and undeclared child elements of an Element instance. The
   * iteration order is predictable, with declared elements being returned in
   * the order that the child ElementMetadata was declared and with repeating
   * elements (for a given child type) returned in the order in which they were
   * added. After all declared elements have been returned then undeclared
   * element types will be returned, in the order that they were added to the
   * parent element.
   */
  private class ElementIterator implements Iterator<Element> {

    private Iterator<ElementKey<?, ?>> metadataIterator;
    private Iterator<Element> sublistIterator;
    private Iterator<Object> elementIterator;
    private Element nextElement = null;
    private Mode mode = Mode.DECLARED;

    private ElementIterator() {
      metadataIterator = metadata.getElements().iterator();
      elementIterator = (elements == null) ? null
          : elements.values().iterator();
      nextElement = findNextElement();
    }

    public boolean hasNext() {
      return nextElement != null;
    }

    public Element next() {
      if (nextElement == null) {
        throw new NoSuchElementException("No remaining elements");
      }
      Element retval = nextElement;
      nextElement = findNextElement();
      return retval;
    }

    public void remove() {
      throw new UnsupportedOperationException(
          "Removal not supported on element iterator");
    }

    /**
     * Returns the next sequential element in the iteration order or
     * {@code null} if there are no remaining elements.
     *
     * @return next element or {@code null}
     */
    private Element findNextElement() {

      // If we are iterating a sublist and elements remain, return the element
      // in the sublist.
      if (sublistIterator != null) {
        if (sublistIterator.hasNext()) {
          return sublistIterator.next();
        }
        // Done with the sublist iterator.
        sublistIterator = null;
      }

      Element next = null;
      while (next == null && mode != Mode.DONE) {
        switch(mode) {
          case DECLARED:
            next = findNextDeclaredElement();
            break;
          case UNDECLARED:
            next = findNextUndeclaredElement();
            break;
          case DONE:
            break;
        }
      }

      return next;
    }

    /**
     * Finds the next declared element, if one exists, otherwise returns null.
     * Will set the mode to undeclared if nothing was found.
     */
    private Element findNextDeclaredElement() {
      if (metadataIterator != null) {
        while (metadataIterator.hasNext()) {
          ElementKey<?, ?> nextKey = metadataIterator.next();
          if (ElementCreatorImpl.ELEMENT_MARKER == nextKey) {
            mode = Mode.UNDECLARED;
            return null;
          }
          Object obj = getElementObject(nextKey);
          Element first = firstElement(obj);
          if (first != null) {
            return first;
          }
        }

        // No more declared elements, turn the iterator off.
        metadataIterator = null;
      }

      // Check undeclared next.
      mode = Mode.UNDECLARED;
      return null;
    }

    /**
     * Find the next undeclared element, or null if no more undeclared elements
     * exist.
     */
    private Element findNextUndeclaredElement() {
      if (elementIterator != null) {
        while (elementIterator.hasNext()) {
          Object next = elementIterator.next();
          Element first = firstElement(next);
          if (first != null && !metadata.isDeclared(first.getElementKey())) {
            return first;
          } else {

            // Clear the sublist iterator if the first element wasn't valid.
            sublistIterator = null;
          }
        }

        // No more undeclared elements, turn the iterators off.
        sublistIterator = null;
        elementIterator = null;
      }

      // Go back and check for any remaining declared metadata.
      mode = metadataIterator != null && metadataIterator.hasNext()
          ? Mode.DECLARED : Mode.DONE;
      return null;
    }

    /**
     * Get the first element from either a singleton or a collection of
     * elements.  This method will also set the sublistIterator as a side effect
     * of retrieving the first element in a collection.
     */
    private Element firstElement(Object obj) {
      if (obj == null) {
        return null;
      }
      // If the next declared element is a single instance, then just
      // return it.  On the next call we will move to the next declared
      // type.
      if (obj instanceof Element) {
        return (Element) obj;
      } else {
        // If the next declared element is a collection of elements, set the
        // sublist iterator to the content of the collection and return the
        // first element.  The next call will continue iteration on this
        // collection.
        Collection<Element> elementCollection = castElementCollection(obj);
        if (!elementCollection.isEmpty()) {
          sublistIterator = elementCollection.iterator();
          return sublistIterator.next();
        }
      }
      return null;
    }
  }

  /**
   * Returns the number of child elements present on this element.
   * @return number of elements.
   */
  public int getElementCount() {
    int elementCount = 0;
    if (elements != null) {
      for (Object elementValue : elements.values()) {
        if (elementValue instanceof Collection) {
          elementCount += (castElementCollection(elementValue)).size();
        } else {
          elementCount++;
        }
      }
    }

    return elementCount;
  }

  /**
   * Get a child element matching the specified qualified name.
   *
   * @param id the qualified name of the child to retrieve.
   * @return the matching child element, or {@code null} if none was found.
   * @throws IllegalArgumentException if the id referenced a repeating element.
   */
  public Element getElement(QName id) {
    Object mapValue = getElementObject(id);
    throwIfNotSingle(mapValue);
    if (mapValue instanceof Element) {
      return (Element) mapValue;
    }
    return null;
  }

  /**
   * Get child element matching the specified key.  Will try to adapt the
   * element to the given key if it is not already an instance of the requested
   * class.  This will fail with an exception if the adaptation was not valid.
   *
   * @param <T> the type of element to return.
   * @param childKey the metadata key for the child element to retrieve.
   * @return child element, or {@code null} if none was found.
   * @throws IllegalArgumentException if the key referenced a repeating element.
   */
  public <D, T extends Element> T getElement(ElementKey<D, T> childKey) {
    Element element = getElement(childKey.getId());
    if (element == null) {
      return null;
    }
    ElementMetadata<D, T> childMeta = metadata.bindElement(childKey);
    if (childMeta == null) {
      childMeta = createUndeclaredElementMetadata(childKey);
    }
    try {
      return adapt(element, childMeta);
    } catch (ContentCreationException e) {
      throw new IllegalArgumentException("Unable to adapt to "
          + childKey.getElementType(), e);
    }
  }

  /**
   * Throws an illegal argument exception if the given value is a collection
   * rather than a single value.
   */
  private void throwIfNotSingle(Object mapValue) {
    if (mapValue instanceof Collection<?>) {
      throw new IllegalArgumentException(
          "The getElement(*) method was called for a repeating element.  " +
          "Use getElements(*) instead.");
    }
  }

  /**
   * This method just returns the bare object stored in the map, or null if
   * either the map didn't contain the object or the map is null.
   */
  private Object getElementObject(QName id) {
    return (elements != null) ? elements.get(id) : null;
  }

  /**
   * This method just returns the bare object stored in the map, or null if
   * either the map didn't contain the object or the map is null.
   */
  private Object getElementObject(ElementKey<?, ?> childKey) {
    return (elements != null) ? elements.get(childKey.getId()) : null;
  }

  /**
   * Convenience method to return child element's text node as an object.
   * Returns {@code null} if child doesn't exist or child does not have a text
   * node.
   */
  public Object getElementValue(QName id) {
    Element e = getElement(id);
    return (e == null) ? null : e.getTextValue();
  }

  /**
   * Convenience method to return child element's text node cast to
   * the specified type. Returns {@code null} if child element does
   * not exist or has no text node.
   *
   * @param <V> child element's text node type
   * @param key identifying the child element.
   * @return child element's text node, cast to type {@code V},
   * or {@code null} if child element does not exist or has no
   * text node
   */
  public <V> V getElementValue(ElementKey<V, ? extends Element> key) {
    Element e = getElement(key);
    return e == null ? null : e.getTextValue(key);
  }

  /**
   * Returns true if the element has child element(s) with the given id.
   */
  public boolean hasElement(QName id) {
    return (elements == null) ? false : elements.containsKey(id);
  }

  /**
   * Returns true if the element has child element(s) with the given key.
   */
  public boolean hasElement(ElementKey<?, ?> childKey) {
    return hasElement(childKey.getId());
  }

  /**
   * Returns an immutable list of elements matching the given id.
   */
  public List<Element> getElements(QName id) {

    // children, and if so add all of the wrapper junk we deal with in Multimap.
    ImmutableList.Builder<Element> builder = ImmutableList.builder();
    Object obj = getElementObject(id);
    if (obj != null) {
      if (obj instanceof Element) {
        builder.add((Element) obj);
      } else {
        for (Object o : (Collection<?>) obj) {
          builder.add((Element) o);
        }
      }
    }
    return builder.build();
  }

  /**
   * Get child elements matching the specified metadata.  This list cannot be
   * used to add new child elements, instead the {@link #addElement(Element)}
   * method should be used.
   *
   * @param key child metadata to lookup child elements based on.
   * @return element's children, or an empty list if there are no children with
   *     the given metadata's id.
   */
  public <T extends Element> List<T> getElements(ElementKey<?, T> key) {

    // children, and if so add all of the wrapper junk we deal with in Multimap.
    ImmutableList.Builder<T> builder = ImmutableList.builder();
    Object obj = getElementObject(key);
    if (obj != null) {
      ElementMetadata<?, T> childMeta = metadata.bindElement(key);
      if (childMeta == null) {
        childMeta = createUndeclaredElementMetadata(key);
      }

      try {
        if (obj instanceof Element) {
          T adapted = adapt((Element) obj, childMeta);
          builder.add(adapted);
        } else {

          // Adapt the list elements on the way out if necessary.  We return a
          // new list currently, if we change to return mutable lists we'll need
          // to adapt in-place.  To enforce clients not adding to the list we
          // return an immutable list.

          for (Object o : (Collection<?>) obj) {
            Element e = (Element) o;
            builder.add(adapt((Element) o, childMeta));
          }
        }
      } catch (ContentCreationException e) {
        throw new IllegalArgumentException("Unable to adapt to "
            + key.getElementType(), e);
      }
    }
    return builder.build();
  }

  /**
   * Get child elements matching the specified id. This set cannot be used
   * to add new child elements, instead the {@link #addElement(Element)} method
   * should be used.
   */
  public Set<Element> getElementSet(QName id) {

    // children, and if so add all of the wrapper junk we deal with in Multimap.
    ImmutableSet.Builder<Element> builder = ImmutableSet.builder();
    Object obj = getElementObject(id);
    if (obj != null) {
      if (obj instanceof Element) {
        builder.add((Element) obj);
      } else {
        for (Object o : (Collection<?>) obj) {
          builder.add((Element) o);
        }
      }
    }
    return builder.build();
  }

  /**
   * Get child elements matching the specified metadata.  This set cannot be
   * used to add new child elements, instead the {@link #addElement(Element)}
   * method should be used.
   *
   * @param key the child key to lookup child elements based on.
   * @return elements children, or an empty set if there are no children with
   *     the given metadata's id.
   */
  public <T extends Element> Set<T> getElementSet(ElementKey<?, T> key) {

    // children, and if so add all of the wrapper junk we deal with in Multimap.
    ImmutableSet.Builder<T> builder = ImmutableSet.builder();
    Object obj = getElementObject(key);
    if (obj != null) {
      ElementMetadata<?, T> childMeta = metadata.bindElement(key);
      if (childMeta == null) {
        childMeta = createUndeclaredElementMetadata(key);
      }

      try {
        if (obj instanceof Element) {
          T adapted = adapt((Element) obj, childMeta);
          builder.add(adapted);
        } else {

          // Adapt the list elements on the way out if necessary.  We return a
          // new list currently, if we change to return mutable lists we'll need
          // to adapt in-place.  To enforce clients not adding to the list we
          // return an immutable list.

          for (Object o : (Collection<?>) obj) {
            Element e = (Element) o;
            builder.add(adapt((Element) o, childMeta));
          }
        }
      } catch (ContentCreationException e) {
        throw new IllegalArgumentException("Unable to adapt to "
            + key.getElementType(), e);
      }
    }
    return builder.build();
  }

  /**
   * Add a child element. Get parent-child linkage from parent's
   * metadata map. Element will be added using the ID of the metadata on the
   * child element.
   *
   * @param childElement child element
   * @return this element for chaining
   */
  public Element addElement(Element childElement) {
    return addElement(childElement.getElementId(), childElement);
  }

  /**
   * Sets the value of the child element(s) with the given id. The given element
   * will replace all existing elements at the given id. If the given element
   * is {@code null}, this is equivalent to {@link #removeElement(QName)}.
   */
  public Element setElement(QName id, Element element) {
    removeElement(id);
    if (element != null) {
      addElement(id, element);
    }
    return this;
  }

  /**
   * Sets the value of the child element(s) with the given key.  The given
   * element will replace all existing elements at the given key.  If the given
   * element is null, this is equivalent to {@link #removeElement(ElementKey)}.
   *
   * @param childKey key for the child element
   * @param element child element
   * @return this element for chaining
   */
  public Element setElement(ElementKey<?, ?> childKey, Element element) {
    removeElement(childKey);
    if (element != null) {
      addElement(childKey, element);
    }
    return this;
  }

  /**
   * Add a child element with the given ID.  The metadata will be retrieved by
   * looking it up on the metadata of the parent element.
   * <p>
   * If the metadata declares element to be of cardinality
   * {@link Cardinality#SINGLE}, a simple {@link Element} is added. If element
   * already exists in the map, the old value is replaced. If the metadata
   * declares the element to be of cardinality {@link Cardinality#MULTIPLE},
   * an {@link Element} is added to a list and stored in the map.
   *
   * @param childKey key for the child element
   * @param element child element
   * @return this element for chaining
   */
  public Element addElement(ElementKey<?, ?> childKey, Element element) {
    return addElement(childKey.getId(), element);
  }

  /**
   * Add a child element with the given Key.  The metadata will be retrieved by
   * looking it up on the metadata of the parent element.
   * <p>
   * If the metadata declares element to be of cardinality
   * {@link Cardinality#SINGLE}, a simple {@link Element} is added. If element
   * already exists in the map, the old value is replaced. If the metadata
   * declares the element to be of cardinality {@link Cardinality#MULTIPLE},
   * an {@link Element} is added to a list and stored in the map.
   *
   * @param id the qualified name to use for the child
   * @param element child element
   * @return this element for chaining
   */
  public Element addElement(QName id, Element element) {

    if (elements == null) {
      elements = new LinkedHashMap<QName, Object>();
    }

    ElementKey<?, ?> childKey = ElementKey.of(
        id, element.getElementKey().getDatatype(), element.getClass());

    Cardinality cardinality = Cardinality.MULTIPLE;
    if (metadata.isDeclared(childKey)) {
      ElementMetadata<?, ?> childMeta = metadata.bindElement(childKey);
      element.setMetadata(childMeta);
      cardinality = childMeta.getCardinality();
    }

    if (cardinality == Cardinality.SINGLE) {
      elements.put(id, element);
    } else {
      Object obj = elements.get(id);
      if (obj == null) {
        elements.put(id, element);
      } else if (obj instanceof Collection<?>) {
        Collection<Element> collect = castElementCollection(obj);
        collect.add(element);
      } else {
        Collection<Element> collect = createCollection(cardinality);
        collect.add((Element) obj);
        collect.add(element);
        elements.put(id, collect);
      }
    }
    return this;
  }

  /**
   * Remove child element(s) of a given name. All elements with the given name
   * will be removed.
   *
   * @param id the id of the child element(s) to remove.
   * @return this element for chaining.
   */
  public Element removeElement(QName id) {
    if (elements != null) {
      elements.remove(id);
    }
    return this;
  }

  /**
   * Remove child element(s) of a given name.  All elements with the given name
   * will be removed.
   *
   * @param childKey key of the element to remove.
   * @return this element for chaining.
   */
  public Element removeElement(ElementKey<?, ?> childKey) {
    return removeElement(childKey.getId());
  }

  /**
   * Remove a single child element from this element.  This method returns true
   * if the element was found and removed, or false if it was not.  It uses
   * identity and not equality to find a match.
   *
   * @param element the child element to remove.
   * @return true if the child element was removed from this element.
   */
  public boolean removeElement(Element element) {
    return removeElement(element.getElementKey(), element);
  }

  /**
   * Remove a single child element from this element.  This method returns true
   * if the element was found and removed, or false if it was not.  It uses
   * identity and not equality to find a match.
   *
   * @param childKey the key for the child element to remove.
   * @param element the child element to remove.
   * @return true if the child element was removed from this element.
   */
  public boolean removeElement(ElementKey<?, ?> childKey, Element element) {
    boolean removed = false;
    if (elements != null) {
      Object obj = getElementObject(childKey);
      if (obj instanceof Collection<?>) {
        Collection<Element> collect = castElementCollection(obj);
        Iterator<Element> iter = collect.iterator();
        while (iter.hasNext()) {
          if (iter.next() == element) {
            iter.remove();
            removed = true;
            break;
          }
        }
        if (collect.isEmpty()) {
          removeElement(childKey);
        }
      } else if (obj == element) {
        removeElement(childKey);
        removed = true;
      }
    }
    return removed;
  }

  /**
   * Replace one element with another.  If the element to add has the
   * same id as the one that is being replaced, it will be switch in place,
   * maintaining order in repeating or undeclared elements.
   *
   * @param toRemove element to remove.
   * @param toAdd element to add.
   * @return true if the replacement succeeded.
   */
  public boolean replaceElement(Element toRemove, Element toAdd) {

    // If toAdd is null, this is just a remove.
    if (toAdd == null) {
      return removeElement(toRemove);
    }

    // If the IDs do not match, we do a remove and then an add.
    QName id = toRemove.getElementId();
    if (!id.equals(toAdd.getElementId())) {
      boolean removed = removeElement(toRemove);
      if (removed) {
        addElement(toAdd);
      }
      return removed;
    }

    // Matched IDs, try to find the removed element and replace it.
    if (elements != null) {
      Object obj = elements.get(id);
      if (obj instanceof List<?>) {
        List<Element> list = castElementList(obj);
        for (int i = 0; i < list.size(); i++) {
          if (list.get(i) == toRemove) {
            list.set(i, toAdd);
            return true;
          }
        }
      } else if (obj instanceof Set<?>) {
        Set<Element> set = castElementSet(obj);
        if (set.remove(toRemove)) {
          set.add(toAdd);
        }
      } else if (obj == toRemove) {
        elements.put(id, toAdd);
        return true;
      }
    }
    return false;
  }


  /**
   * Suppress the warnings around casting the object in the map into a list
   * of elements.
   */
  @SuppressWarnings("unchecked")
  private <T extends Element> List<T> castElementList(Object obj) {
    return (List<T>) obj;
  }

  /**
   * Suppress the warnings around casting the object in the map into a set of
   * elements.
   */
  @SuppressWarnings("unchecked")
  private <T extends Element> Set<T> castElementSet(Object obj) {
    return (Set<T>) obj;
  }

  /**
   * Suppress the warnings around casting the object in the map into a
   * collection of elements.
   */
  @SuppressWarnings("unchecked")
  private <T extends Element> Collection<T> castElementCollection(Object obj) {
    return (Collection<T>) obj;
  }

  /**
   * Creates a collection based on the cardinality.
   */
  private <T extends Element> Collection<T> createCollection(Cardinality card) {
    switch(card) {
      case MULTIPLE: return new ArrayList<T>();
      case SET: return new HashSet<T>();
      default: throw new IllegalArgumentException(
          "Cannot create collection for " + card);
    }
  }

  /**
   * Clears internal state of all attributes, child elements, declared
   * namespaces and any element value.
   */
  public void clear() {
    value = null;
    attributes = null;
    elements = null;
  }

  /**
   * Element's text node value.
   */
  protected Object value;

  /**
   * Returns the untyped element value or null if it has no value.
   *
   * @return untyped element value
   */
  public Object getTextValue() {
    return value;
  }

  /**
   * Returns the element value cast to the metadata's datatype.
   * not compatible.
   *
   * @param <V> data type of the metadata.
   * @param key element key used to cast the value.
   * @return typed element value.
   */
  public <V> V getTextValue(ElementKey<V, ?> key) {
    if (value != null) {
      try {
        return ObjectConverter.getValue(value, key.getDatatype());
      } catch (ParseException e) {
        throw new IllegalArgumentException("Unable to convert value " + e
            + " to datatype " + key.getDatatype());
      }
    }
    return null;
  }

  /**
   * Sets the value of the element and returns the element to allow chaining.
   *
   * @param newValue element's value
   * @return this element
   * @throws IllegalStateException if the element is immutable
   * @throws IllegalArgumentException if the object is of an invalid type or
   *     if this element does not allow a value
   */
  public Element setTextValue(Object newValue) {
    throwExceptionIfImmutable();

    // Some elements e.g. text constructs aren't associated
    // with metadata until they're added to a parent element.
    // Their content is validated later, in {@link validate()}.
    if (metadata != null) {
      Class<?> datatype = getElementKey().getDatatype();
      if (datatype == null) {
        throw new IllegalArgumentException(
            "Element must not contain a text node");
      }
      if (newValue != null && !datatype.isInstance(newValue)) {
        throw new IllegalArgumentException(
            "Invalid class: " + newValue.getClass().getCanonicalName());
      }
    }
    this.value = newValue;
    return this;
  }

  /**
   * @return true if element has a text node value
   */
  public boolean hasTextValue() {
    return value != null;
  }

  /**
   * Resolve the state of all elements in the tree, rooted at this
   * element, against their metadata. Throws an exception if the tree
   * cannot be resolved.
   *
   * @return the narrowed element if narrowing took place
   * @throws ContentValidationException if tree cannot be resolved
   */
  public Element resolve() throws ContentValidationException {

    ValidationContext vc = new ValidationContext();
    Element narrowed = resolve(vc);
    if (!vc.isValid()) {
      throw new ContentValidationException("Invalid data", vc);
    }
    return narrowed;
  }

  /**
   * Resolve this element's state against its metadata. Accumulates
   * errors in caller's validation context.
   *
   * @param vc validation context
   * @return the narrowed element if narrowing took place.
   */
  public Element resolve(ValidationContext vc) {

    Element narrowed = narrow(vc);
    narrowed.validate(vc);

    // Resolve all child elements.
    List<Pair<Element, Element>> replacements = Lists.newArrayList();
    Iterator<Element> childIterator = narrowed.getElementIterator();
    while (childIterator.hasNext()) {
      Element child = childIterator.next();
      Element resolved = child.resolve(vc);
      if (resolved != child) {
        replacements.add(Pair.of(child, resolved));
      }
    }

    // Replace any resolved child elements with their replacement.
    for (Pair<Element, Element> pair : replacements) {
      narrowed.replaceElement(pair.getFirst(), pair.getSecond());
    }
    return narrowed;
  }

  /**
   * Narrow down element's type to the most specific one possible.
   * <p>
   * Any validation errors discovered during narrowing are accumulated
   * in the validation context.
   * <p>
   * Default action is to not do anything with current element.
   * Subclasses may override this function to narrow the type
   * in some custom fashion.
   *
   * @param vc validation context
   * @return element narrowed down to the most specific type
   */
  protected Element narrow(ValidationContext vc) {
    Class<?> narrowedType = metadata.getKey().getElementType();
    if (!narrowedType.isInstance(this)) {

      // Make sure the narrowing is valid.
      if (!getClass().isAssignableFrom(narrowedType)) {
        LOGGER.severe("Element of type " + getClass()
            + " cannot be narrowed to type " + narrowedType);
      }

      // Adapt to the more narrow type.
      try {
        return adapt(this, metadata);
      } catch (ContentCreationException e) {
        LOGGER.log(Level.SEVERE, "Unable to adapt " +
            getClass() + " to " + narrowedType, e);
      }
    }
    return this;
  }

  /**
   * Adapts an element based on a key.  This will find an adaptation in the
   * metadata and adapt to that metadata type (and element type).  If no
   * adaptation is found this will return the source element.
   *
   * @param source the element we are narrowing from.
   * @param kind the kind name to lookup the adaptation for.
   * @return the adapted element if one was found.
   */
  protected Element adapt(Element source, String kind) {
    ElementMetadata<?, ?> meta = metadata.adapt(kind);
    if (meta != null) {
      try {
        return adapt(source, meta);
      } catch (ContentCreationException e) {
        // Not usable as a adaptable kind, skip.
        LOGGER.log(Level.SEVERE, "Unable to adapt "
            + source.getClass() + " to " + meta.getKey().getElementType(), e);
      }
    }
    return source;
  }

  /**
   * Adapts an element based on some new metadata.  If the metadata represents
   * a more narrow type than {@code source}, an instance of the more narrow
   * type will be returned, containing the same information as the source.  If
   * the source is {@code null}, a null instance of {@code T} will be returned.
   *
   * @param <T> the type of element to adapt to.
   * @param source the element we are narrowing from.
   * @param meta metadata to adapt based on.
   * @return the adapted element if one was found.
   * @throws ContentCreationException if the metadata cannot be used to adapt.
   * @throws NullPointerException if meta is null.
   */
  protected <T extends Element> T adapt(
      Element source, ElementMetadata<?, T> meta)
      throws ContentCreationException {
    if (meta == null) {
      throw new NullPointerException("Metadata cannot be null.");
    }
    Class<? extends T> adaptingTo = meta.getKey().getElementType();
    if (source == null || adaptingTo.isInstance(source)) {
      return adaptingTo.cast(source);
    }
    Class<? extends Element> adaptingFrom = source.getClass();
    if (!adaptingFrom.isAssignableFrom(adaptingTo)) {
      throw new IllegalArgumentException("Cannot adapt from element of type "
          + adaptingFrom + " to an element of type " + adaptingTo);
    }
    return createElement(meta, source);
  }

  /**
   * Validate element content against element metadata. Validates
   * element's attributes and child elements, but does not recurse
   * into child elements. Accumulates validation errors in caller's
   * validation context.
   *
   * @param vc validation context
   */
  public void validate(ValidationContext vc) {
    metadata.validate(vc, this);
  }

  /**
   * Called to visit all children of this element.
   *
   * @param ev the element visitor.
   * @throws ElementVisitor.StoppedException if traversal must be stopped
   */
  protected void visitChildren(ElementVisitor ev)
      throws ElementVisitor.StoppedException {

    // Visit children
    Iterator<Element> childIterator = getElementIterator();
    while (childIterator.hasNext()) {
      childIterator.next().visit(ev, this);
    }
  }

  /**
   * Visits the tree of element data using the specified {@link ElementVisitor}.
   *
   * @param ev the element visitor instance to use.
   * @param parent the parent of this element (or {@code null} if no
   *        parent or unspecified.
   * @throws ElementVisitor.StoppedException if traversal must be stopped
   */
  public void visit(ElementVisitor ev, Element parent)
      throws ElementVisitor.StoppedException {

    // Visit the current extension point
    boolean visitChildren = ev.visit(parent, this);
    if (visitChildren) {
      visitChildren(ev);
    }
    ev.visitComplete(this);
  }

  /**
   * @return true if element contains a simple value, meaning it has
   *     a text node but no attributes or child elements, or it has one
   *     attribute but no text node or child elements.
   */
  public boolean containsSimpleValue() {
    Class<?> datatype = metadata.getKey().getDatatype();
    return metadata.getElements().isEmpty() &&
        ((datatype != Void.class &&
            metadata.getAttributes().isEmpty()) ||
         (datatype == Void.class &&
            metadata.getAttributes().size() == 1));
  }

  /**
   * @param o given object
   * @return true if the given object is not null and is the same concrete class
   *         as this one
   */
  protected boolean sameClassAs(Object o) {
    return o != null && getClass().equals(o.getClass());
  }

  /**
   * Helper method to check for equality between two object, including null
   * checks.
   *
   * @param o1 object 1 or <code>null</code>
   * @param o2 object 2 or <code>null</code>
   * @return true if the specified arguments are equal, or both null
   */
  protected static boolean eq(Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals(o2);
  }

  /**
   * Helper method that constructs a new {@link Element} instance of the type
   * defined by the type parameter {@code E}.
   *
   * @param metadata metadata to initialize the element with
   * @return element that was created
   * @throws ContentCreationException if content cannot be created
   */
  public static <E extends Element> E createElement(
      ElementMetadata<?, E> metadata) throws ContentCreationException {
    return createElement(metadata, null);
  }

  /**
   * Helper method that constructs a new {@link Element} instance of the type
   * defined by the type parameter {@code E}.
   *
   * @param metadata metadata to initialize the element with
   * @param source the source element to use, or null if a fresh instance should
   *     be created.
   * @return element that was created
   * @throws ContentCreationException if content cannot be created
   */
  public static <E extends Element> E createElement(
      ElementMetadata<?, E> metadata, Element source)
      throws ContentCreationException {
    Class<?>[] argTypes;
    Object[] args;
    Class<? extends E> elementClass = metadata.getKey().getElementType();
    try {
      try {
        // First try the constructor that takes in {@link ElementMetadata}.
        if (source != null) {
          argTypes = new Class<?>[] {ElementMetadata.class, source.getClass()};
          args = new Object[] {metadata, source};
        } else {
          argTypes = new Class<?>[] {ElementMetadata.class};
          args = new Object[] {metadata};
        }
        return construct(elementClass, argTypes, args);
      } catch (NoSuchMethodException e) {

        // Now try the null-arg or 1-arg constructor.
        if (source != null) {
          argTypes = new Class<?>[] {source.getClass()};
          args = new Object[] {source};
        } else {
          argTypes = new Class<?>[] {};
          args = new Object[] {};
        }
        E result = construct(elementClass, argTypes, args);
        result.setMetadata(metadata);
        return result;
      }
    } catch (NoSuchMethodException e) {
      throw new ContentCreationException(
          "Constructor not found: " + elementClass);
    } catch (IllegalAccessException e) {
      throw new ContentCreationException(
          "Constructor not found: " + elementClass);
    } catch (InstantiationException e) {
      throw new ContentCreationException(
          "Constructor not found: " + elementClass);
    } catch (InvocationTargetException e) {
      throw new ContentCreationException(
          "Constructor not found: " + elementClass, e.getCause());
    }
  }

  /**
   * Attempt to construct an instance of the given class with the given args
   * and arg types.  Will set the constructot to accessible, allowing access
   * to non-public constructors, so use with caution.
   */
  private static <T> T construct(Class<? extends T> clazz, Class<?>[] argTypes,
      Object[] args) throws SecurityException, NoSuchMethodException,
      InstantiationException, IllegalAccessException,
      InvocationTargetException {
    Constructor<? extends T> ctc = clazz.getDeclaredConstructor(argTypes);
    ctc.setAccessible(true);
    return ctc.newInstance(args);
  }

  @Override
  public String toString() {
    return "Element{" + getElementId() + "}@" +
        Integer.toHexString(hashCode());
  }
}
