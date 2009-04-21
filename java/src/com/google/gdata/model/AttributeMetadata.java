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
 * Metadata describing an attribute.  Attribute metadata includes the id, name,
 * and datatype of an attribute, as well whether that metadata is required,
 * declared, or visible.  New attribute instances can only be created as part
 * of an element, see {@link ElementCreator#addAttribute(AttributeKey)}.
 *
 * <p>See the {@link Metadata} interface for the properties shared between
 * attribute metadata and {@link ElementMetadata}.
 *
 * 
 */
public interface AttributeMetadata<D> extends Metadata<D> {

  /**
   * Returns the attribute key for this attribute metadata.
   */
  AttributeKey<D> getKey();

  /**
   * Binds this attribute metadata to the given context.
   */
  AttributeMetadata<D> bind(MetadataContext context);
}
