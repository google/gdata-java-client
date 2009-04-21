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

/**
 * Shared implementation of the basic metadata fields for
 * {@link AttributeCreator} and {@link ElementCreator}.  Holds the state of
 * the shared fields, which are the name and whether the property is required
 * or visible.  This class is thread safe for writing.
 *
 * 
 */
abstract class MetadataCreatorImpl {

  // Root registry builder, used to set the dirty bit and enforce locking
  // across all metadata creators.
  final MetadataRegistryBuilder registry;

  // Modifiable fields, can be changed via the set* methods.
  private QName name;
  private Boolean required;
  private Boolean visible;

  /**
   * Construct a new empty metadata creator, with all fields defaulted to null.
   */
  MetadataCreatorImpl(MetadataRegistryBuilder root) {
    this.registry = root;
  }

  /**
   * Copy another metadata creator into this metadata creator.
   */
  MetadataCreatorImpl(MetadataRegistryBuilder root,
      MetadataCreatorImpl source) {
    this(root);
    Preconditions.checkNotNull(source, "source");
    this.name = source.name;
    this.required = source.required;
    this.visible = source.visible;
  }

  /**
   * Set the name of the metadata.
   */
  MetadataCreatorImpl setName(QName name) {
    synchronized (registry) {
      this.name = name;
      registry.dirty();
    }
    return this;
  }

  /**
   * Set the metadata to required or optional.
   */
  MetadataCreatorImpl setRequired(boolean required) {
    synchronized (registry) {
      this.required = required;
      registry.dirty();
    }
    return this;
  }

  /**
   * Set the metadata to visible or hidden.
   */
  MetadataCreatorImpl setVisible(boolean visible) {
    synchronized (registry) {
      this.visible = visible;
      registry.dirty();
    }
    return this;
  }

  // Package-level read-only access.

  QName getName() {
    return name;
  }

  Boolean getRequired() {
    return required;
  }

  Boolean getVisible() {
    return visible;
  }
}
