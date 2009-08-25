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

import com.google.gdata.model.Metadata.VirtualValue;

/**
 * Shared interface for metadata creators.  Extended by {@link AttributeCreator}
 * and {@link ElementCreator}, this is used to create both elements and
 * attributes, which we refer to as properties.
 *
 * 
 */
public interface MetadataCreator {

  /**
   * Sets the name of the property.  This is used on parsing to decide which
   * field to place a property in, and during generation to choose the display
   * name of the property.  This should only be set to override the default
   * name, which is the ID specified in the {@link MetadataKey}.
   *
   * @param name the new name to use for the property.
   * @return this metadata creator for chaining.
   */
  MetadataCreator setName(QName name);

  /**
   * Sets the requiredness of this property.  If set to true, this property
   * must appear in both the input and output or a validation error will occur.
   * If set to false, this property is optional.
   *
   * @param required true to set the property to required, false to set it
   *     to optional (the default).
   * @return this metadata creator for chaining.
   */
  MetadataCreator setRequired(boolean required);

  /**
   * Sets whether this property is visible.  If the property is not visible
   * then it will not be included in the output.  This can be used to hide an
   * property in particular contexts (such as RSS or JSON output).  It can
   * also be used to explicitly set a property to visible that may be hidden
   * by other metadata rules.
   *
   * @param visible true to make the property visible (the default), false to
   *     hide it from the output.
   * @return this metadata creator for chaining.
   */
  MetadataCreator setVisible(boolean visible);

  /**
   * Sets the virtual value for the property.  This is used as the value
   * of the property during parsing and generation.
   */
  MetadataCreator setVirtualValue(VirtualValue virtualValue);
}
