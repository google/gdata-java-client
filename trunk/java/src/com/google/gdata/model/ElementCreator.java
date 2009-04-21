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

/**
 * An element creator allows setting element information, which includes the
 * following pieces:
 * <ul>
 * <li>name: The qualified name to use for input and output.  Defaults to using
 * the value in the {@link ElementKey} associated with the metadata. Setting the
 * name here will override the default.</li>
 * <li>required: Validates that the element exists in both input and output.
 * Defaults to false.</li>
 * <li>visible: Hides or shows an element in the output. Defaults to true.</li>
 * <li>cardinality: Allows setting this element to allow a single or multiple
 * elements. See {@link Cardinality} for the valid values. Defaults to
 * {@link Cardinality#SINGLE}.</li>
 * <li>contentRequired: Sets the text content for this element to required.
 * Defaults to true if the {@link ElementKey} has a datatype other than
 * {@link Void}.</li>
 * <li>validator: Overrides the validator for this element. By default a
 * {@link MetadataValidator} is used.</li>
 * <li>properties: Additional properties used during parsing and generation.
 * Different wire formats support different properties. Defaults to
 * {@code null}.</li>
 * <li>valueTransform: Allows changing where the value of the element comes
 * from. By default the value comes from the text content field, but this can
 * be overridden by using a {@link ValueTransform}.</li>
 * <li>attributes: The ordered set of attributes expected for this element.
 * Defaults to empty. Adding an attribute can be accomplished by calling either
 * {@link #addAttribute} or {@link #replaceAttribute}, based on the desired
 * order in the output.</li>
 * <li>elements: The ordered set of child elements expected for this element.
 * Defaults to empty. Adding an element can be accomplished by calling either
 * {@link #addElement} or {@link #replaceElement},
 * based on the desired order in the output.</li>
 * <li>adaptations: A map from adaptation key to adaptation. An adaptation
 * allows parsing into a generic base class and adapting the element into an
 * appropriate subclass based on key.</li>
 * </ul>
 *
 * 
 */
public interface ElementCreator {

  /**
   * This interface represents a transformation of a value.  The transform
   * method will be called on output to generate the correct representation.
   */
  public interface ValueTransform {

    /**
     * Runs this transformation on the given element.
     */
    public String transform(Element e);
  }

  /**
   * Sets the name of the property.  This can be used after copying some other
   * metadata to change the name.
   *
   * @param name the new name to use for the property.
   * @return this metadata creator for chaining.
   */
  ElementCreator setName(QName name);

  /**
   * Sets the requiredness of this property.  This means that the property
   * must appear in the parent element for the parent element to be valid.
   *
   * @param required true to set the property to required, false to set it
   *     to optional (the default).
   * @return this metadata creator for chaining.
   */
  ElementCreator setRequired(boolean required);

  /**
   * Sets whether this property is visible.  If the property is not visible
   * then it will not be included in the output.  This can be used to set the
   * state of a property to invisible if it is not part of the default set
   * of metadata.
   *
   * @param visible true to make the property visible (the default), false to
   *     hide it from the output.
   * @return this metadata creator for chaining.
   */
  ElementCreator setVisible(boolean visible);

  /**
   * Adds an adaptation from the element type of this element to the adaptation
   * type. An adaptation is a customization of a base type to a particular
   * variant type. Adaptations are used to allow adaptive parsing; the data is
   * first parsed into the base type and then adapted to a more specific variant
   * during resolution. If you are adding adaptations you should also override
   * {@link Element#narrow(ValidationContext)} and call
   * {@link Element#adapt(Element, String)} with the appropriate key.
   */
  ElementCreator addAdaptation(String kind, ElementKey<?, ?> adaptation);

  /**
   * Sets the cardinality of the element.  The cardinality can be either
   * {@link Cardinality#SINGLE} for only a single element, or
   * {@link Cardinality#MULTIPLE} for repeating elements.
   *
   * @param cardinality the cardinality of the element.
   * @return this element metadata builder for chaining.
   */
  ElementCreator setCardinality(Cardinality cardinality);

  /**
   * Sets whether the element's content is required.  By default the content is
   * required if the datatype is not {@link Void}.
   *
   * @param contentRequired true to set the content to required, false to set
   *     it to optional.
   * @return this element metadata builder for chaining.
   */
  ElementCreator setContentRequired(boolean contentRequired);

  /**
   * Sets the element's validator.  The validator is used to check that the
   * element has all required attributes/elements/values.  By default an
   * instance of {@link MetadataValidator} is used.
   *
   * @param validator element validator to use when validating the element, or
   *     null if no validation is needed (for undeclared metadata).
   * @return this element metadata builder for chaining.
   */
  ElementCreator setValidator(ElementValidator validator);

  /**
   * Sets the element's properties.  This is used to provide additional
   * information during parsing/generation, and is specific to the wire format.
   *
   * @param properties default properties for the element.
   * @return this element metadata builder for chaining.
   */
  ElementCreator setProperties(Object properties);

  /**
   * Sets the value transform for the element.  This is used to retrieve the
   * value of the element if the normal text content is not appropriate.
   */
  ElementCreator setValueTransform(ValueTransform valueTransform);

  /**
   * Sets the location of the undeclared attributes. By default, undeclared
   * attributes appear after all declared attributes, this lets them appear
   * earlier in the list.
   */
  ElementCreator addUndeclaredAttributeMarker();

  /**
   * Add the key for an attribute.  If an attribute with the same ID
   * already exists, the previous attribute will be removed, and the new
   * attribute will be added to the end of the list.  If you want to replace the
   * existing element, use {@link #replaceAttribute(AttributeKey)}.
   *
   * @param key the key to the attribute that is being added.
   * @return an attribute builder that can be used to set the attribute fields.
   */
  AttributeCreator addAttribute(AttributeKey<?> key);

  /**
   * Replaces the existing metadata for an attribute.
   *
   * @param key the key to the attribute that is being replaced.
   * @return an attribute builder that can be used to modify the attribute.
   */
  AttributeCreator replaceAttribute(AttributeKey<?> key);

  /**
   * Whitelists a set of attributes for this element metadata.  This will hide
   * all declared attributes on the metadata instance that will be created from
   * this builder.
   */
  ElementCreator whitelistAttributes(AttributeKey<?>... keys);

  /**
   * Sets the location of the undeclared elements. By default, undeclared
   * elements appear after all declared elements, this lets them appear
   * earlier in the list.
   */
  ElementCreator addUndeclaredElementMarker();

  /**
   * Add the metadata for a child element.  If an element with the same ID
   * already exists, the previous element will be removed, and the new element
   * will be added to the end of the list.  If you want to replace the existing
   * element, use {@link #replaceElement(ElementKey)}.
   *
   * @param element the key we are adding or pushing to the end.
   * @return the builder for the child element.
   */
  ElementCreator addElement(ElementKey<?, ?> element);

  /**
   * Replaces the existing metadata for a child element.
   *
   * @param key the key we are replacing.
   * @return this element metadata builder for chaining.
   * @throws IllegalArgumentException if the child metadata doesn't exist.
   */
  ElementCreator replaceElement(ElementKey<?, ?> key);

  /**
   * Whitelists a set of child elements for this element metadata.  This will
   * hide all declared child elements on the metadata instance that will be
   * created from this builder.
   */
  ElementCreator whitelistElements(ElementKey<?, ?>... keys);

  /**
   * Blacklist a set of keys, these keys will be explicitly hidden from view.
   */
  ElementCreator blacklistElements(ElementKey<?, ?>... keys);
}
