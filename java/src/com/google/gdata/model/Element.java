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

import com.google.gdata.util.common.base.Objects;
import com.google.gdata.util.common.base.Objects.ToStringHelper;
import com.google.gdata.util.common.base.Pair;
import com.google.gdata.util.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.model.atom.Category;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ContentCreationException;
import com.google.gdata.wireformats.ContentValidationException;
import com.google.gdata.wireformats.ObjectConverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

/**
 * Data element in an instance document. Contains attributes,
 * child elements, and a text node.
 *
 * <p>The various setter methods provided by this class return {@code this}
 * so setter invocations can be chained, as in the following example:
 *
 * <p><pre>
 * Element who = new Element(KEY)
 *     .setAttributeValue(ATTR_KEY, "value")
 *     .addElement(
 *         new Element(EXT_KEY_NOEXT)
 *             .setTextValue("yolk"));
 * </pre>
 *
 * <p>Subclasses are expected to follow the same model for any setter
 * methods they provide.
 *
 * @see #setTextValue(Object)
 * @see #setAttributeValue(AttributeKey, Object)
 * @see #addElement(Element)
 * @see #addElement(ElementKey, Element)
 */
public class Element {

  // Logger for logging warnings and errors.
  private static final Logger LOGGER =
      Logger.getLogger(Element.class.getName());

  /**
   * Returns the default {@link ElementKey} for an {@link Element} type.
   *
   * @param type element type
   * @return default element key for type
   */
  public static ElementKey<?, ?> getDefaultKey(Class<? extends Element> type) {

    Preconditions.checkNotNull(type, "type");

    // The current approach used reflection based upon the implementation
    // pattern that every Element type will expose a static ElementKey field
    // named "KEY".
    ElementKey<?, ?> key = null;
    try {
      Field keyField = type.getField("KEY");
      key = ElementKey.class.cast(keyField.get(null));
    } catch (NoSuchFieldException nsfe) {
      throw new IllegalArgumentException("Unable to access KEY field:" + type,
          nsfe);
    } catch (IllegalArgumentException iae) {
      throw new IllegalArgumentException("Unable to access KEY field:" + type,
          iae);
    } catch (IllegalAccessException iae) {
      throw new IllegalArgumentException("Unable to access KEY field:" + type,
          iae);
    } catch (NullPointerException npe) {
      throw new IllegalArgumentException("Unable to access KEY field:" + type,
          npe);
    }
    return key;
  }

  /**
   * This class contains the element state, which is the attributes, elements,
   * and text content of this element.
   */
  private static class ElementState {

    /**
     * Map of all attributes that were added to this element, in the order they
     * were added to the element.
     */
    private Map<QName, Attribute> attributes;

    /**
     * Map of child elements, keyed by their ID.  The value of a map
     * entry is either an instance of the class (for {@link Cardinality#SINGLE}
     * elements) or a list of instances (for {@link Cardinality#MULTIPLE}), or
     * a set of instances (for {@link Cardinality#SET}).  This map is maintained
     * in the order the child elements were added to this element.
     */
    private Map<QName, Object> elements;

    /**
     * Element's text node value.
     */
    private Object value;

    /**
     * Indicates that the element has been locked.
     */
    private volatile boolean locked;

    @Override
    public String toString() {
      ToStringHelper helper = Objects.toStringHelper(this);
      if (attributes != null) {
        helper.add("attributes", attributes.values());
      }
      if (elements != null) {
        helper.add("elements", elements.values());
      }
      if (value != null) {
        helper.add("value", value);
      }
      
      return helper.toString();
    }
  }

  /**
   * The element key associated with this element.
   */
  private final ElementKey<?, ?> key;

  /**
   * The state of this element, contains all actual data.  This allows shallow
   * copies to be very efficient.
   */
  private final ElementState state;

  /**
   * Construct element and associate with a key.
   *
   * @param elementKey the key to this element, contains the ID and datatype.
   */
  public Element(ElementKey<?, ?> elementKey) {
    Preconditions.checkNotNull(elementKey, "elementKey");
    this.key = bindKey(elementKey, getClass());
    this.state = new ElementState();
  }

  /**
   * Construct a generic undeclared element with the specified qualified name.
   *
   * @param qName qualified name
   */
  public Element(QName qName) {
    this.key = ElementKey.of(qName, String.class, getClass());
    this.state = new ElementState();
  }

  /**
   * Copy constructor that initializes a new Element instance to be a wrapper
   * around another element instance.  The element will use the given element
   * as its source for any content.
   *
   * @param elementKey the element key to associate with the copy.
   * @param source the element to copy data from.
   */
  public Element(ElementKey<?, ?> elementKey, Element source) {
    this.key = bindKey(elementKey, getClass());
    this.state = source.state;
  }

  /**
   * Binds an element key to a specific element subclass.  This guarantees that
   * the key on an element will always have exactly that element's type as its
   * element type, and not some other element type.  This makes it possible to
   * believe an element's key without needing to check the element type when
   * looking up metadata.
   */
  private static ElementKey<?, ?> bindKey(ElementKey<?, ?> key,
      Class<? extends Element> type) {
    Class<?> keyType = key.getElementType();
    if (keyType == type) {
      return key;
    }
    return ElementKey.of(key.getId(), key.getDatatype(), type);
  }

  /**
   * Returns true if this element has been locked using {@link #lock}. Once an
   * element has been locked it cannot be unlocked.
   */
  public final boolean isLocked() {
    return state.locked;
  }

  /**
   * Locks this element.  A locked element cannot have any changes made to its
   * content or its attributes or child elements.  This will also lock all
   * attributes and child elements as well.  Once this method has been called,
   * this element can be safely published to other threads.
   */
  public Element lock() {
    state.locked = true;
    if (state.attributes != null) {
      for (Attribute att : state.attributes.values()) {
        att.lock();
      }
    }
    if (state.elements != null) {
      for (Object childObj : state.elements.values()) {
        if (childObj instanceof Element) {
          ((Element) childObj).lock();
        } else {
          for (Element child : castElementCollection(childObj)) {
            child.lock();
          }
        }
      }
    }
    return this;
  }

  /**
  * Throws an {@link IllegalStateException} if this instance is locked.
  */
  private void throwExceptionIfLocked() {
    Preconditions.checkState(!state.locked,
        "%s instance is read only", getElementId());
  }

  /**
   * Returns the key to this element.
   */
  public ElementKey<?, ?> getElementKey() {
    return key;
  }

  /**
   * Get the id of this element.
   */
  public QName getElementId() {
    return key.getId();
  }

  /**
   * Returns an iterator over all attributes on this element.
   */
  public Iterator<Attribute> getAttributeIterator() {
    return getAttributeIterator(null);
  }

  /**
   * Returns an iterator over the attributes of this element with a well-defined
   * iteration order based on the metadata. All declared attributes are
   * returned first, in the order of declaration, followed by undeclared
   * attributes in the order in which they were added to this element.  If the
   * metadata declares virtual attributes, those attributes will be included in
   * the iterator, likewise any attributes which are hidden will be excluded.
   *
   * @param metadata the element metadata to use for iteration
   * @return an iterator over the attributes of this element
   */
  public Iterator<Attribute> getAttributeIterator(
      ElementMetadata<?, ?> metadata) {
    return new AttributeIterator(this, metadata, state.attributes);
  }

  /**
   * Returns the number of attributes present on this element.
   *
   * @return count of attributes
   */
  public int getAttributeCount() {
    return (state.attributes != null) ? state.attributes.size() : 0;
  }

  /**
   * Returns true if the element has an attribute with the given id.
   */
  public boolean hasAttribute(QName id) {
    return (state.attributes == null)
        ? false : state.attributes.containsKey(id);
  }

  /**
   * Returns true if the element has an attribute with the given key.
   */
  public boolean hasAttribute(AttributeKey<?> childKey) {
    return hasAttribute(childKey.getId());
  }

  /**
   * Get the value of an attribute by id.
   */
  public Object getAttributeValue(QName id) {
    if (state.attributes == null) {
      return null;
    }
    Attribute attribute = state.attributes.get(id);
    return (attribute == null) ? null : attribute.getValue();
  }

  /**
   * Returns the attribute value cast to the appropriate type, based on the
   * given key.
   *
   * @param <T> return type
   * @param key the attribute key to use to cast the attribute value
   * @return typed attribute value
   * @throws IllegalArgumentException if the value cannot be converted to the
   *     key type
   */
  public <T> T getAttributeValue(AttributeKey<T> key) {
    Attribute attribute = (state.attributes == null) ? null
        : state.attributes.get(key.getId());
    Object value = (attribute == null) ? null : attribute.getValue();
    if (value == null) {
      return null;
    }
    try {
      return ObjectConverter.getValue(value, key.getDatatype());
    } catch (ParseException e) {
      throw new IllegalArgumentException("Unable to convert value " + e
          + " to datatype " + key.getDatatype());
    }
  }

  /**
   * Add attribute by id and value.  If the value is {@code null} this is
   * equivalent to removing the attribute with the given id.
   */
  public Element setAttributeValue(QName id, Object attrValue) {
    return setAttributeValue(AttributeKey.of(id), attrValue);
  }

  /**
   * Add attribute by value. If the value is {@code null} the value will be
   * removed.
   *
   * @param key attribute key that is being added
   * @param attrValue attribute value or {@code null} to remove
   */
  public Element setAttributeValue(AttributeKey<?> key, Object attrValue) {
    if (attrValue == null) {
      removeAttributeValue(key);
    } else {
      setAttribute(key, new Attribute(key, attrValue));
    }
    return this;
  }

  /**
   * Puts an attribute into the attribute map, creating the map if needed.
   */
  private void setAttribute(AttributeKey<?> attKey, Attribute attribute) {
    throwExceptionIfLocked();
    if (state.attributes == null) {
      state.attributes = new LinkedHashMap<QName, Attribute>();
    }
    state.attributes.put(attKey.getId(), attribute);
  }

  /**
   * 
   * @deprecated use removeAttributeValue instead.
   */
  @Deprecated
  public Object removeAttribute(QName id) {
    return removeAttributeValue(id);
  }
  
  /**
   * Remove attribute (if present).
   *
   * @param id the qualified name of the attribute.
   * @return this element
   */
  public Object removeAttributeValue(QName id) {
    throwExceptionIfLocked();
    Attribute removed = (state.attributes == null) ? null
        : state.attributes.remove(id);
    return (removed == null) ? null : removed.getValue();
  }

  /**
   * 
   * @deprecated use removeAttributeValue instead.
   */
  @Deprecated
  public Object removeAttribute(AttributeKey<?> key) {
    return removeAttributeValue(key);
  }
  
  /**
   * Remove attribute (if present).
   *
   * @param key the key of the attribute.
   * @return this element
   */
  public Object removeAttributeValue(AttributeKey<?> key) {
    return removeAttributeValue(key.getId());
  }

  /**
   * Returns an iterator over all child elements of this element.
   */
  public Iterator<Element> getElementIterator() {
    return getElementIterator(null);
  }

  /**
   * Returns an iterator over all child elements with a well-defined iteration
   * order based on this metadata.  All declared elements are returned first, in
   * the order of declaration, followed by undeclared elements in the order in
   * which they were added to this element.  If the metadata declares virtual
   * elements, those elements will be included in the iterator, likewise any
   * elements which are hidden will be excluded.
   *
   * @param metadata the metadata to use for iteration
   * @return iterator over the child elements of the element
   */
  public Iterator<Element> getElementIterator(ElementMetadata<?, ?> metadata) {
    return new ElementIterator(this, metadata, state.elements);
  }

  /**
   * Returns the number of child elements present on this element.
   * @return number of elements.
   */
  public int getElementCount() {
    int elementCount = 0;
    if (state.elements != null) {
      for (Object elementValue : state.elements.values()) {
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
   * @param id the qualified name of the child to retrieve
   * @return the matching child element, or {@code null} if none was found
   * @throws IllegalArgumentException if the id referenced a repeating element
   */
  public Element getElement(QName id) {
    Object mapValue = getElementObject(id);
    if (mapValue instanceof Element) {
      return (Element) mapValue;
    }
    Preconditions.checkArgument(!(mapValue instanceof Collection<?>),
        "The getElement(*) method was called for a repeating element.  " +
        "Use getElements(*) instead.");
    return null;
  }

  /**
   * Get child element matching the specified key.  Will try to adapt the
   * element to the given key if it is not already an instance of the requested
   * class.  This will fail with an exception if the adaptation was not valid.
   *
   * @param <T> the type of element to return
   * @param childKey the metadata key for the child element to retrieve
   * @return child element, or {@code null} if none was found
   * @throws IllegalArgumentException if the key referenced a repeating element
   */
  public <D, T extends Element> T getElement(ElementKey<D, T> childKey) {
    Element child = getElement(childKey.getId());
    if (child == null) {
      return null;
    }
    try {
      return adapt(childKey, child);
    } catch (ContentCreationException e) {
      throw new IllegalArgumentException("Unable to adapt to "
          + childKey.getElementType(), e);
    }
  }

  /**
   * This method just returns the bare object stored in the map, or null if
   * either the map didn't contain the object or the map is null.
   */
  private Object getElementObject(QName id) {
    if (state.elements == null) {
      return null;
    }
    if ("*".equals(id.getLocalName())) {
      XmlNamespace ns = id.getNs();
      if (ns != null) {
        String uri = ns.getUri();
        ImmutableList.Builder<Element> builder = ImmutableList.builder();
        for (Map.Entry<QName, Object> entry : state.elements.entrySet()) {
          QName key = entry.getKey();
          XmlNamespace keyNs = key.getNs();
          if (keyNs != null && uri.equals(keyNs.getUri())) {
            Object value = entry.getValue();
            if (value instanceof Element) {
              builder.add((Element) value);
            } else {
              builder.addAll(castElementCollection(value));
            }
          }
        }
        return builder.build();
      }
    }
    return state.elements.get(id);
  }

  /**
   * This method just returns the bare object stored in the map, or null if
   * either the map didn't contain the object or the map is null.
   */
  private Object getElementObject(ElementKey<?, ?> childKey) {
    return getElementObject(childKey.getId());
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
    return (state.elements == null) ? false : state.elements.containsKey(id);
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
        for (Element e : castElementCollection(obj)) {
          builder.add(e);
        }
      }
    }
    return builder.build();
  }

  /**
   * Get child elements matching the specified key.  This list cannot be
   * used to add new child elements, instead the {@link #addElement(Element)}
   * method should be used. If the elements at the given key are not of the
   * correct type an {@link IllegalArgumentException} will be thrown.
   *
   * @param key child key to lookup child elements based on.
   * @return element's children, or an empty list if there are no children with
   *     the given key's id.
   */
  public <T extends Element> List<T> getElements(ElementKey<?, T> key) {

    // children, and if so add all of the wrapper junk we deal with in Multimap.
    ImmutableList.Builder<T> builder = ImmutableList.builder();
    Object obj = getElementObject(key);
    if (obj != null) {
      Class<? extends T> childType = key.getElementType();
      if (obj instanceof Element) {
        if (childType.isInstance(obj)) {
          builder.add(childType.cast(obj));
        }
      } else {

        // Returns a list of all children that matched the given key.
        // If we change to returning mutable lists this will need to be a
        // view of the underlying data instead.
        for (Element e : castElementCollection(obj)) {
          if (childType.isInstance(e)) {
            builder.add(childType.cast(e));
          }
        }
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
        for (Element e : castElementCollection(obj)) {
          builder.add(e);
        }
      }
    }
    return builder.build();
  }

  /**
   * Get child elements matching the specified key.  This set cannot be
   * used to add new child elements, instead the {@link #addElement(Element)}
   * method should be used. If the elements at the given key are not of the
   * correct type an {@link IllegalArgumentException} will be thrown.
   *
   * @param key the child key to lookup child elements based on.
   * @return elements children, or an empty set if there are no children with
   *     the given key's id.
   */
  public <T extends Element> Set<T> getElementSet(ElementKey<?, T> key) {

    // children, and if so add all of the wrapper junk we deal with in Multimap.
    ImmutableSet.Builder<T> builder = ImmutableSet.builder();
    Object obj = getElementObject(key);
    if (obj != null) {
      Class<? extends T> childType = key.getElementType();
      if (obj instanceof Element) {
        if (childType.isInstance(obj)) {
          builder.add(childType.cast(obj));
        }
      } else {

        // Returns a set of all children that matched the given key.
        // If we change to returning mutable lists this will need to be a
        // view of the underlying data instead.
        for (Element e : castElementCollection(obj)) {
          if (childType.isInstance(e)) {
            builder.add(childType.cast(e));
          }
        }
      }
    }
    return builder.build();
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
   * Sets a child element to the given value.  Uses the element key of the
   * element as the key.  This is equivalent to calling
   * {@code setElement(element.getElementKey(), element);}.
   * 
   * @throws NullPointerException if element is null.
   */
  public Element setElement(Element element) {
    Preconditions.checkNotNull(element);
    setElement(element.getElementKey(), element);
    return this;
  }

  /**
   * Sets the value of the child element(s) with the {@code key}.  The
   * {@code element} will replace all existing elements with the same key.  If
   * element is null, this is equivalent to {@link #removeElement(ElementKey)}.
   *
   * @param key the key for the child element
   * @param element child element
   * @return this element for chaining
   */
  public Element setElement(ElementKey<?, ?> key, Element element) {
    removeElement(key);
    if (element != null) {
      addElement(key, element);
    }
    return this;
  }

  /**
   * Add a child element, using the key of the child element as the key into
   * this element's children.
   *
   * @param element child element
   * @return this element for chaining
   * @throws NullPointerException if element is null.
   */
  public Element addElement(Element element) {
    Preconditions.checkNotNull(element);
    addElement(element.getElementKey(), element);
    return this;
  }

  /**
   * Add a child element with the given ID.  This will add the given element to
   * the end of the collection of elements with the same ID.  If you want to
   * replace any existing elements use {@link #setElement(QName, Element)}
   * instead.
   *
   * @param id the qualified name to use for the child
   * @param element child element
   * @return this element for chaining
   * @throws NullPointerException if element is null.
   */
  public Element addElement(QName id, Element element) {
    Preconditions.checkNotNull(element);
    addElement(ElementKey.of(id, element.getElementKey().getDatatype(),
        element.getClass()), element);
    return this;
  }

  /**
   * Add a child element with the given key.  This will add the given element to
   * the end of the collection of elements with the same ID.  If you want to
   * replace any existing elements use {@link #setElement(ElementKey, Element)}
   * instead.
   *
   * @param key the key of the child.
   * @param element child element
   * @return this element for chaining
   */
  public Element addElement(ElementKey<?, ?> key, Element element) {
    throwExceptionIfLocked();
    if (state.elements == null) {
      state.elements = new LinkedHashMap<QName, Object>();
    }

    ElementKey<?, ?> elementKey = element.getElementKey();
    key = calculateKey(key, elementKey);
    if (!key.equals(elementKey)) {
      try {
        element = createElement(key, element);
      } catch (ContentCreationException e) {
        throw new IllegalArgumentException("Key " + key + " cannot be applied"
            + " to element with key " + elementKey);
      }
    }

    QName id = key.getId();
    Object obj = state.elements.get(id);
    if (obj == null) {
      state.elements.put(id, element);
    } else if (obj instanceof Collection<?>) {
      Collection<Element> collect = castElementCollection(obj);
      collect.add(element);
    } else {
      Collection<Element> collect = createCollection(key);
      collect.add((Element) obj);
      collect.add(element);
      state.elements.put(id, collect);
    }
    return this;
  }

  /**
   * Calculates the actual key that should be used for adding an element.  This
   * uses the ID and datatype of the key, but if the element types are in the
   * same type hierarchy the narrowest element type is used.
   */
  private ElementKey<?, ?> calculateKey(ElementKey<?, ?> key,
      ElementKey<?, ?> sourceKey) {
    Class<?> keyType = key.getElementType();
    Class<? extends Element> sourceType = sourceKey.getElementType();

    // If the sourceType is a subtype of the key type, we want to use it
    // as the type of element that we create, because it is more specific but
    // still compatible.
    if (keyType != sourceType && keyType.isAssignableFrom(sourceType)) {
      key = ElementKey.of(key.getId(), key.getDatatype(), sourceType);
    }
    return key;
  }

  /**
   * Remove child element(s) of a given name. All elements with the given id
   * will be removed.
   *
   * @param id the id of the child element(s) to remove.
   * @return this element for chaining.
   */
  public Element removeElement(QName id) {
    throwExceptionIfLocked();
    if (state.elements != null) {
      state.elements.remove(id);
    }
    return this;
  }

  /**
   * Remove child element(s) of a given name.  All elements with the same ID as
   * the given key will be removed.
   *
   * @param childKey key of the element(s) to remove.
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
    throwExceptionIfLocked();
    boolean removed = false;
    if (state.elements != null) {
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
    throwExceptionIfLocked();

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
    if (state.elements != null) {
      Object obj = state.elements.get(id);
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
        state.elements.put(id, toAdd);
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
   * Creates a collection based on the given key.
   */
  private <T extends Element> Collection<T> createCollection(
      ElementKey<?, ?> key) {

    // is part of resolve?
    Class<?> elementType = key.getElementType();
    if (Category.class.isAssignableFrom(elementType)) {
      return Sets.newLinkedHashSet();
    } else {
      return Lists.newArrayList();
    }
  }

  /**
   * Clears internal state of all attributes, child elements, and text content.
   */
  public void clear() {
    throwExceptionIfLocked();
    state.value = null;
    state.attributes = null;
    state.elements = null;
  }

  /**
   * Returns the untyped element value or null if it has no value.
   *
   * @return untyped element value
   */
  public Object getTextValue() {
    return state.value;
  }

  /**
   * Returns the element value adapted to the key's datatype.
   *
   * @param <V> data type of the key.
   * @param key the element key used to convert the value.
   * @return typed element value.
   */
  public <V> V getTextValue(ElementKey<V, ?> key) {
    if (state.value != null) {
      try {
        return ObjectConverter.getValue(state.value, key.getDatatype());
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
    throwExceptionIfLocked();
    state.value = checkValue(key, newValue);
    return this;
  }

  /**
   * Checks that the datatype of this element allows setting the value to the
   * given object.  Throws an {@link IllegalArgumentException} if the value
   * is not valid for this element.
   */
  Object checkValue(ElementKey<?, ?> elementKey, Object newValue) {
    if (newValue != null) {
      Class<?> datatype = elementKey.getDatatype();
      Preconditions.checkArgument(datatype != Void.class,
          "Element must not contain a text node");
      Preconditions.checkArgument(datatype.isInstance(newValue),
          "Invalid class: %s", newValue.getClass().getCanonicalName());
    }
    return newValue;
  }

  /**
   * @return true if element has a text node value
   */
  public boolean hasTextValue() {
    return state.value != null;
  }

  /**
   * Resolve the state of all elements in the tree, rooted at this
   * element, against the metadata. Throws an exception if the tree
   * cannot be resolved.
   *
   * @param metadata the metadata to resolve against.
   * @return the narrowed element if narrowing took place
   * @throws ContentValidationException if tree cannot be resolved
   */
  public Element resolve(ElementMetadata<?, ?> metadata)
      throws ContentValidationException {

    ValidationContext vc = new ValidationContext();
    Element narrowed = resolve(metadata, vc);
    if (!vc.isValid()) {
      throw new ContentValidationException("Invalid data", vc);
    }
    return narrowed;
  }

  /**
   * Resolve this element's state against the metadata. Accumulates
   * errors in caller's validation context.
   *
   * @param vc validation context
   * @return the narrowed element if narrowing took place.
   */
  public Element resolve(ElementMetadata<?, ?> metadata, ValidationContext vc) {

    // Return immediately if the metadata is null, no resolve necessary.
    if (metadata == null) {
      return this;
    }

    Element narrowed = narrow(metadata, vc);
    narrowed.validate(metadata, vc);

    // Resolve all child elements.
    Iterator<Element> childIterator = narrowed.getElementIterator();
    if (childIterator.hasNext()) {
      List<Pair<Element, Element>> replacements = Lists.newArrayList();

      while (childIterator.hasNext()) {
        Element child = childIterator.next();
        ElementMetadata<?, ?> childMeta = metadata.bindElement(
            child.getElementKey());
        Element resolved = child.resolve(childMeta, vc);
        if (resolved != child) {
          replacements.add(Pair.of(child, resolved));
        }
      }

      // Replace any resolved child elements with their replacement.
      for (Pair<Element, Element> pair : replacements) {
        narrowed.replaceElement(pair.getFirst(), pair.getSecond());
      }
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
   * @param metadata the element metadata to narrow to.
   * @param vc validation context
   * @return element narrowed down to the most specific type
   */
  protected Element narrow(ElementMetadata<?, ?> metadata,
      ValidationContext vc) {
    ElementKey<?, ?> narrowedKey = metadata.getKey();
    Class<?> narrowedType = narrowedKey.getElementType();
    if (!narrowedType.isInstance(this)) {

      // Make sure the narrowing is valid.
      if (!getClass().isAssignableFrom(narrowedType)) {
        LOGGER.severe("Element of type " + getClass()
            + " cannot be narrowed to type " + narrowedType);
      }

      // Adapt to the more narrow type.
      try {
        return adapt(narrowedKey, this);
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
   * @param sourceMeta the source metadata to adapt from.
   * @param kind the kind name to lookup the adaptation for.
   * @return the adapted element if one was found.
   */
  protected Element adapt(Element source,
      ElementMetadata<?, ?> sourceMeta, String kind) {
    ElementKey<?, ?> adaptorKey = sourceMeta.adapt(kind);
    if (adaptorKey != null) {
      try {
        return adapt(adaptorKey, source);
      } catch (ContentCreationException e) {
        // Not usable as a adaptable kind, skip.
        LOGGER.log(Level.SEVERE, "Unable to adapt "
            + source.getClass() + " to " + adaptorKey.getElementType(), e);
      }
    }
    return source;
  }

  /**
   * Adapts an element based on a different key.  If the key represents
   * a more narrow type than {@code source}, an instance of the more narrow
   * type will be returned, containing the same information as the source.  If
   * the source is {@code null}, a null instance of {@code T} will be returned.
   *
   * @param <T> the type of element to adapt to.
   * @param key the element key to adapt to.
   * @param source the element we are adapting from.
   * @return the adapted element if one was found.
   * @throws ContentCreationException if the metadata cannot be used to adapt.
   * @throws NullPointerException if meta is null.
   */
  protected <T extends Element> T adapt(ElementKey<?, T> key, Element source)
      throws ContentCreationException {
    Preconditions.checkNotNull(key);
    Class<? extends T> adaptingTo = key.getElementType();
    if (source == null || adaptingTo.isInstance(source)) {
      return adaptingTo.cast(source);
    }
    Class<? extends Element> adaptingFrom = source.getClass();
    Preconditions.checkArgument(adaptingFrom.isAssignableFrom(adaptingTo),
        "Cannot adapt from element of type %s to an element of type %s",
        adaptingFrom, adaptingTo);
    return createElement(key, source);
  }

  /**
   * Validate the element using the given metadata, and placing any errors into
   * the validation context.  The default behavior is to use the metadata
   * validation, subclasses may override this to add their own validation. If
   * the metadata is null (undeclared), no validation will be performed.
   */
  protected void validate(ElementMetadata<?, ?> metadata,
      ValidationContext vc) {
    if (metadata != null) {
      metadata.validate(vc, this);
    }
  }

  /**
   * Visits the element using the specified {@link ElementVisitor} and metadata.
   * A {@code null} metadata indicates that the element is undeclared, and
   * child elements will be visited in the order they were added to the element.
   *
   * @param ev the element visitor instance to use.
   * @param meta the metadata for the element, or {@code null} for undeclared
   *     metadata.
   * @throws ElementVisitor.StoppedException if traversal must be stopped
   */
  public void visit(ElementVisitor ev, ElementMetadata<?, ?> meta) {
    visit(ev, null, meta);
  }

  /**
   * Visit implementation, recursively visits this element and all of its
   * children.
   */
  private void visit(ElementVisitor ev, Element parent,
      ElementMetadata<?, ?> meta) throws ElementVisitor.StoppedException {

    // Visit the current element.
    boolean visitChildren = ev.visit(parent, this, meta);
    if (visitChildren) {
      visitChildren(ev, meta);
    }
    ev.visitComplete(parent, this, meta);
  }

  /**
   * Visit all of the children of this element, calling the element visitor
   * with the child element and child metadata for each child.
   */
  private void visitChildren(ElementVisitor ev, ElementMetadata<?, ?> meta)
      throws ElementVisitor.StoppedException {

    // Visit children
    Iterator<Element> childIterator = getElementIterator(meta);
    while (childIterator.hasNext()) {
      Element child = childIterator.next();
      ElementMetadata<?, ?> childMeta = (meta == null) ? null
          : meta.bindElement(child.getElementKey());
      child.visit(ev, this, childMeta);
    }
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
   * @param key the element key to create the element from
   * @return element that was created
   * @throws ContentCreationException if content cannot be created
   */
  public static <E extends Element> E createElement(
      ElementKey<?, E> key) throws ContentCreationException {
    return createElement(key, null);
  }

  /**
   * Helper method that constructs a new {@link Element} instance of the type
   * defined by the type parameter {@code E}.
   *
   * @param key the element key to create the element for.
   * @param source the source element to use, or null if a fresh instance should
   *     be created.
   * @return element that was created
   * @throws ContentCreationException if content cannot be created
   */
  public static <E extends Element> E createElement(
      ElementKey<?, E> key, Element source)
      throws ContentCreationException {
    
    if (source != null
        && key.equals(source.getElementKey())
        && key.getElementType().isInstance(source)) {
      return key.getElementType().cast(source);
    }
    
    Class<?>[] argTypes;
    Object[] args;
    Class<? extends E> elementClass = key.getElementType();
    try {
      try {
        // First try the constructor that takes in {@link ElementKey}.
        if (source != null) {
          argTypes = new Class<?>[] {ElementKey.class, source.getClass()};
          args = new Object[] {key, source};
        } else {
          argTypes = new Class<?>[] {ElementKey.class};
          args = new Object[] {key};
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
        return construct(elementClass, argTypes, args);
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
   * and arg types.  Will set the constructor to accessible, allowing access
   * to non-public constructors, so use with caution.
   */
  private static <T> T construct(Class<? extends T> clazz, Class<?>[] argTypes,
      Object[] args) throws SecurityException, NoSuchMethodException,
      InstantiationException, IllegalAccessException,
      InvocationTargetException {
    @SuppressWarnings("unchecked")
    Constructor<T>[] ctcs = (Constructor<T>[]) clazz.getDeclaredConstructors();
    for (Constructor<T> ctc : ctcs) {
      Class<?>[] paramTypes = ctc.getParameterTypes();
      if (paramsValid(paramTypes, argTypes)) {
        ctc.setAccessible(true);
        return ctc.newInstance(args);
      }
    }

    // We didn't find a constructor, this will report an error consistent
    // with not finding a valid public constructor.
    return clazz.getConstructor(argTypes).newInstance(args);
  }

  private static boolean paramsValid(Class<?>[] paramTypes,
      Class<?>[] argTypes) {
    if (paramTypes.length != argTypes.length) {
      return false;
    }
    for (int i = 0; i < paramTypes.length; i++) {
      if (!paramTypes[i].isAssignableFrom(argTypes[i])) {
        return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    return state.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Element)) {
      return false;
    }
    return state.equals(((Element) obj).state);
  }

  @Override
  public String toString() {
    ToStringHelper helper = Objects.toStringHelper(this);
    helper.addValue(getElementId() + "@" + Integer.toHexString(hashCode()));
    Iterator<Attribute> aIter = getAttributeIterator();
    while (aIter.hasNext()) {
      Attribute att = aIter.next();
      helper.add(att.getAttributeKey().getId().toString(), att.getValue());
    }
    if (hasTextValue()) {
      helper.addValue(getTextValue());
    }
    return helper.toString();
  }
}
