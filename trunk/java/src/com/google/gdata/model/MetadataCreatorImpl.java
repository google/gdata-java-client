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
abstract class MetadataCreatorImpl implements MetadataCreator {

  // Root metadata registry, used to set the dirty bit and enforce locking
  // across all of the metadata creators.
  final MetadataRegistry registry;

  final TransformKey transformKey;

  // Modifiable fields, can be changed via the set* methods.
  private QName name;
  private Boolean required;
  private Boolean visible;
  private VirtualValue virtualValue;
  private TransformKey source;
  private Path path;
  private boolean isMoved;

  /**
   * Construct a new empty metadata creator, with all fields defaulted to null.
   */
  MetadataCreatorImpl(MetadataRegistry root, TransformKey transformKey) {
    this.registry = root;
    this.transformKey = transformKey;
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
    if (other.source != null) {
      this.source = other.source;
    }
    if (other.path != null) {
      this.path = other.path;
    }
    if (other.isMoved) {
      this.isMoved = true;
    }
  }

  /**
   * Set the name of the metadata.
   */
  public MetadataCreatorImpl setName(QName name) {
    synchronized (registry) {
      this.name = name;
      registry.dirty();
    }
    return this;
  }

  /**
   * Set the metadata to required or optional.
   */
  public MetadataCreatorImpl setRequired(boolean required) {
    synchronized (registry) {
      this.required = required;
      registry.dirty();
    }
    return this;
  }

  /**
   * Set the metadata to visible or hidden.
   */
  public MetadataCreatorImpl setVisible(boolean visible) {
    synchronized (registry) {
      this.visible = visible;
      registry.dirty();
    }
    return this;
  }

  /**
   * Set the metadata to visible or hidden.
   */
  public MetadataCreatorImpl setVirtualValue(VirtualValue virtualValue) {
    synchronized (registry) {
      this.virtualValue = virtualValue;
      registry.dirty();
    }
    return this;
  }

  /**
   * Sets the source of this metadata creator
   */
  void setSource(Path path, TransformKey key) {
    synchronized (registry) {
      this.path = path;
      this.source = key;
      registry.dirty();
    
      // Explicitly set moved elements to optional, only the source should be
      // required.
      if (this.required == null) {
        setRequired(false);
      }
    }
  }
  
  /**
   * Marks this metadata creator as having been moved.  Moved metadata will be
   * hidden on output, but will not hide any attributes or child elements that
   * have been moved elsewhere.
   */
  MetadataCreatorImpl moved() {
    synchronized (registry) {
      this.isMoved = true;
      registry.dirty();
    }
    return this;
  }

  // Package-level read-only access.

  TransformKey getTransformKey() {
    return transformKey;
  }
  
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

  TransformKey getSource() {
    return source;
  }

  public Path getPath() {
    return path;
  }
  
  boolean isMoved() {
    return isMoved;
  }
}
