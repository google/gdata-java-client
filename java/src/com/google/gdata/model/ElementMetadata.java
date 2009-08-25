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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.ParseException;
import com.google.gdata.wireformats.ContentCreationException;

import java.util.Collection;

/**
 * Element metadata.  This describes the metadata for an individual element,
 * which extends the {@link Metadata} interface with an element type, element
 * cardinality, validator, content requiredness, and attributes and child
 * elements.  To create new instances of {@link ElementMetadata} you should use
 * {@link MetadataRegistry#build(ElementKey)}.
 *
 * @param <D> the datatype of the text node of the element.  If the element has
 *     no text node this will be {@link Void}.  See {@link #getKey()}.
 * @param <E> the element type to use for this metadata.  This is the type
 *     of the key and is used during parsing.  See {@link #getKey()}.
 */
public interface ElementMetadata<D, E extends Element> extends Metadata<D> {

  /**
   * Cardinality of an element.
   */
  public enum Cardinality { SINGLE, MULTIPLE, SET }

  /**
   * Defines a virtual element with multiple cardinality.  This can be used to
   * represent a collection of elements in the output that do not map directly
   * to a collection of elements in the physical DOM.
   */
  public interface MultipleVirtualElement {

    /**
     * Creates a collection of virtual elements from the parent element.
     *
     * @return collection generated virtual elements or {@code null}
     */
    Collection<? extends Element> generateMultiple(Element parent,
        ElementMetadata<?, ?> parentMetadata, ElementMetadata<?, ?> metadata);

    /**
     * Parses the elements, possibly creating additional elements or attributes.
     */
    void parse(Element parent, ElementMetadata<?, ?> parentMetadata,
        Collection<Element> elements, ElementMetadata<?, ?> metadata)
        throws ParseException;
  }

  /**
   * Defines a virtual element with single cardinality.  This can be used to
   * represent a single element that does not map directly to another element
   * in the physical DOM.
   */
  public interface SingleVirtualElement {

    /**
     * Creates a single virtual element from the parent element.
     *
     * @return generated virtual element or {@code null}
     */
    Element generateSingle(Element parent, ElementMetadata<?, ?> parentMetadata,
        ElementMetadata<?, ?> metadata);

    /**
     * Parses the element, possibly creating additional elements or attributes.
     */
    void parse(Element parent, ElementMetadata<?, ?> parentMetadata,
        Element element, ElementMetadata<?, ?> metadata) throws ParseException;
  }

  /**
   * Adapts this metadata to another type, based on the given kind.  Will return
   * the element key that the kind represents, or {@code null} if no adaptation
   * was found.
   */
  ElementKey<?, ?> adapt(String kind);

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
   * Returns true if the element instance is selected for use in the current
   * operation.
   */
  boolean isSelected(Element e);

  /**
   * Returns true if the text content of this element is required.
   */
  boolean isContentRequired();

  /**
   * Returns {@code true} if the element has been flattened, which means its
   * value should be output directly in its parent.
   */
  boolean isFlattened();
  
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
  AttributeKey<?> findAttribute(QName id);

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
   * Returns true if this metadata declares the given child element.
   */
  boolean isDeclared(ElementKey<?, ?> element);

  /**
   * Finds an element of the given id on this element or any adaptations.  This
   * can be used to find appropriate element metadata to parse into while
   * parsing an element.
   */
  ElementKey<?, ?> findElement(QName id);

  /**
   * Returns the virtual element associated with this metadata with single
   * cardinality.  A virtual element represents a completely virtual DOM element
   * that can have its own attributes and child elements, all of which are
   * pulled from elsewhere in the DOM.
   */
  SingleVirtualElement getSingleVirtualElement();

  /**
   * Returns the virtual element associated with this metadata with repeated
   * cardinality.  A virtual element represents a completely virtual DOM element
   * that can have its own attributes and child elements, all of which are
   * pulled from elsewhere in the DOM.
   */
  MultipleVirtualElement getMultipleVirtualElement();
  
  /**
   * Create an element with this metadata as the metadata for the element.
   *
   * @throws ContentCreationException if the element could not be created.
   */
  E createElement() throws ContentCreationException;
  
  /**
   * Returns the best namespace to use as the default in documents if this
   * element is the root type.
   */
  public XmlNamespace getDefaultNamespace();
  
  /**
   * Returns an immutable collection of the namespaces that are referenced by
   * this element, its attributes, and recursively within any declared children.
   */
  public Collection<XmlNamespace> getReferencedNamespaces();
}
