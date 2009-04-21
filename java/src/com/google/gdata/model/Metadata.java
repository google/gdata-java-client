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

/**
 * The Metadata interface defines operations that are common to all content
 * model properties (elements or attributes).  This interface describes the id,
 * name and datatype of a property, as well as including flags for whether the
 * property is required, undeclared, or visible.  Creating new instances of
 * {@link Metadata} should be done using {@link MetadataRegistry}.
 *
 * @param <D> data type of the metadata.  This defines the type of value that
 * the property can contain, which is available through {@link #getKey()}.
 *
 * 
 */
public interface Metadata<D> {

  /**
   * Returns the attribute key for this metadata.
   */
  MetadataKey<D> getKey();

  /**
   * Returns the parent of this metadata instance, or {@code null} if this
   * metadata does not take the parent key into account.
   */
  ElementKey<?, ?> getParent();

  /**
   * Returns the metadata context that this metadata was created for, or
   * {@code null} if this metadata is not part of a context.
   */
  MetadataContext getContext();

  /**
   * Returns the qualified name of the property, used during parsing and
   * generation. For atom this will usually be the same as the id, but for json
   * or other alt types it may be different.
   *
   * @return the qualified name of the property.
   */
  QName getName();

  /**
   * Returns true if this property is required.  If a property is required it
   * must appear in its parent element for that parent element to be valid.
   *
   * @return true if the property is required in its parent element.
   */
  boolean isRequired();

  /**
   * Returns true if this property was not declared (foreign).  This is used
   * for parsing and generation of foreign attributes and foreign xml elements.
   *
   * @return true if the property was not declared.
   */
  boolean isUndeclared();

  /**
   * Returns true if this property is visible.  If a property is visible it will
   * be included in the output generation for its parent element, if it is
   * hidden (not visible) it will not be included.
   *
   * @return true if the property is visible in its parent element.
   */
  boolean isVisible();
}
