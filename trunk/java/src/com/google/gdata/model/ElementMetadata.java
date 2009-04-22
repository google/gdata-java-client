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

import com.google.gdata.model.ContentModel.Cardinality;
import com.google.gdata.wireformats.ContentCreationException;

import java.util.Collection;
import java.util.Iterator;

/**
 * Element metadata.  This describes the metadata for an individual element,
 * which extends the {@link Metadata} interface with an element type, element
 * cardinality, validator, content requiredness, and attributes and child
 * elements.  To create new instances of {@link ElementMetadata} you should use
 * {@link DefaultRegistry#build(ElementKey)}.
 *
 * @param <D> the datatype of the text node of the element.  If the element has
 *     no text node this will be {@link Void}.  See {@link #getKey()}.
 * @param <E> the element type to use for this metadata.  This is the type
 *     of the key and is used during parsing.  See {@link #getKey()}.
 */
public interface ElementMetadata<D, E extends Element> extends Metadata<D> {

  /**
   * A virtual element is one which exists only as metadata, and handles both
   * parsing and generation between the real element and the virtual element.
   */
  public interface VirtualElement {

    /**
     * Creates a virtual element from the parent element.
     */
    Element generate(Element parent, ElementMetadata<?, ?> metadata);

    /**
     * Parses the element, possibly creating additional elements or attributes.
     */
    void parse(Element parent, Element element, ElementMetadata<?, ?> metadata);
  }

  /**
   * Adapts this metadata instance to another type, based on the {@code kind}
   * parameter.
   */
  ElementMetadata<?, ?> adapt(String kind);

  /**
   * Binds this element metadata to the given context.
   */
  ElementMetadata<D, E> bind(MetadataContext context);

  /**
   * Returns the element key that this element is bound to.
   */
  ElementKey<D, E> getKey();

  /**
   * Returns the cardinality of this element.
   */
  Cardinality getCardinality();

 /**
  * Returns true if the element type is referenced to generate the the output
  * representation of the element type.  This generally means that the element
  * may be visible or is required to evaluate a selection condition.
  */
  boolean isReferenced();

  /**
   * Returns true if the text content of this element is required.
   */
  boolean isContentRequired();

  /**
   * Returns an element validator that can be used to validate this content.
   */
  ElementValidator getValidator();

  /**
   * Validates an element using the specified validation context and the
   * element validator associated with this element metadata.
   *
   * @param vc validation context.
   * @param e element to be validate.
   */
  void validate(ValidationContext vc, Element e);

  /**
   * Returns the object properties associated with this metadata.
   */
  Object getProperties();

  /**
   * Returns an iterator over the attributes of the element with a well-defined
   * iteration order based on this metadata.  All declared attributes are
   * returned first, in the order of declaration, followed by undeclared
   * attributes in the order in which they were added to the element.  If this
   * metadata declares virtual attributes those attributes will be included in
   * the iterator, likewise any attributes which are hidden will be excluded.
   *
   * @param element the element whose attributes we are iterating over.
   * @return an iterator over the attributes of the element.
   */
  Iterator<Attribute> getAttributeIterator(Element element);

  /**
   * Returns an immutable collection of attribute keys in declaration order.
   */
  Collection<AttributeKey<?>> getAttributes();

  /**
   * Binds the attribute metadata associated with a particular key.  Will return
   * {@code null} if no such attribute is declared.
   */
  <K> AttributeMetadata<K> bindAttribute(AttributeKey<K> key);

  /**
   * Returns true if this metadata declares the given attribute.
   */
  boolean isDeclared(AttributeKey<?> key);

  /**
   * Finds an attribute of the given id on this element or any adaptations.
   * This can be used to find appropriate attribute metadata to parse into while
   * parsing an element.  This will include attributes that are not declared as
   * part of this metadata, but are declared on adaptations.
   */
  AttributeMetadata<?> findAttribute(QName id);

  /**
   * Returns an iterator over all child elements with a well-defined iteration
   * order based on this metadata.  All declared elements are returned first, in
   * the order of declaration, followed by undeclared elements in the order in
   * which they were added to the element.  If this metadata declares virtual
   * elements, those elements will be included in the iterator, likewise any
   * elements which are hidden will be excluded.
   *
   * @param element the element whose child elements we are iterating over.
   * @return iterator over the child elements of the element.
   */
  Iterator<Element> getElementIterator(Element element);

  /**
   * Returns an immutable set of child keys in declaration order.
   */
  Collection<ElementKey<?, ?>> getElements();

  /**
   * Binds the child element metadata associated with a particular key. Will
   * return {@code null} if no such element is declared.
   */
  <K, L extends Element> ElementMetadata<K, L> bindElement(
      ElementKey<K, L> key);

  /**
   * Returns true if this metadata declareds the given child element.
   */
  boolean isDeclared(ElementKey<?, ?> element);

  /**
   * Finds an element of the given id on this element or any adaptations.  This
   * can be used to find appropriate element metadata to parse into while
   * parsing an element.
   */
  ElementMetadata<?, ?> findElement(QName id);

  /**
   * Returns the virtual element associated with this metadata.  A virtual
   * element represents a completely virtual DOM element that can have its own
   * attributes and child elements, all of which are pulled from elsewhere in
   * the DOM.
   */
  VirtualElement getVirtualElement();

  /**
   * Create an element with this metadata as the metadata for the element.
   *
   * @throws ContentCreationException if the element could not be created.
   */
  E createElement() throws ContentCreationException;
}
