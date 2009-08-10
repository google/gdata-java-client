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
import com.google.gdata.model.Metadata.VirtualValue;

/**
 * Shared implementation of the basic metadata fields for
 * {@link AttributeCreator} and {@link ElementCreator}.  Holds the state of
 * the shared fields, which are the name and whether the property is required
 * or visible.  This class is thread safe for writing.
 *
 * 
 */
abstract class MetadataCreatorImpl {

  // Root metadata registry, used to set the dirty bit and enforce locking
  // across all of the metadata creators.
  final MetadataRegistry registry;

  // Modifiable fields, can be changed via the set* methods.
  private QName name;
  private Boolean required;
  private Boolean visible;
  private VirtualValue virtualValue;

  /**
   * Construct a new empty metadata creator, with all fields defaulted to null.
   */
  MetadataCreatorImpl(MetadataRegistry root) {
    this.registry = root;
  }

  /**
   * Merges the values from an existing metadata creator.
   */
  void merge(MetadataCreatorImpl other) {
    Preconditions.checkNotNull(other, "other");
    if (other.name != null) {
      this.name = other.name;
    }
    if (other.required != null) {
      this.required = other.required;
    }
    if (other.visible != null) {
      this.visible = other.visible;
    }
    if (other.virtualValue != null) {
      this.virtualValue = other.virtualValue;
    }
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

  /**
   * Set the metadata to visible or hidden.
   */
  MetadataCreatorImpl setVirtualValue(VirtualValue virtualValue) {
    synchronized (registry) {
      this.virtualValue = virtualValue;
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

  VirtualValue getVirtualValue() {
    return virtualValue;
  }
}
