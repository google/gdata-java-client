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
 * An immutable transform on a piece of metadata.  This contains all of the
 * base metadata fields, every value is optional (anything may be null).
 *
 * 
 */
abstract class Transform {

  // Our final, nullable fields.
  private final QName name;
  private final Boolean required;
  private final Boolean visible;
  private final VirtualValue virtualValue;
  private final TransformKey source;
  private final Path path;
  private final boolean isMoved;

  /**
   * Constructs an empty transform with all null values.  This should only be
   * used by subclasses.  See {@link AttributeTransform#EMPTY} and
   * {@link ElementTransform#EMPTY} for the appropriate empty transforms.
   */
  Transform() {
    this.name = null;
    this.required = null;
    this.visible = null;
    this.virtualValue = null;
    this.source = null;
    this.path = null;
    this.isMoved = false;
  }

  /**
   * Constructs a transform for the declared metadata described by the given
   * creator.  This constructor is only for use by subclasses, callers should
   * use either {@link AttributeTransform#create(AttributeCreatorImpl)} or
   * {@link ElementTransform#create(ElementCreatorImpl)} to build transforms
   * from their builders.
   */
  Transform(MetadataCreatorImpl creator) {
    this.name = creator.getName();
    this.required = creator.getRequired();
    this.visible = creator.getVisible();
    this.virtualValue = creator.getVirtualValue();
    this.source = creator.getSource();
    this.path = creator.getPath();
    this.isMoved = creator.isMoved();
  }

  /**
   * Construct a composite transform out of the given parts. Transforms are
   * combined by allowing transforms later in the parts to override values
   * provided earlier in the iterable.
   */
  Transform(Iterable<? extends Transform> parts) {
    QName compositeName = null;
    Boolean compositeRequired = null;
    Boolean compositeVisible = null;
    VirtualValue compositeVirtualValue = null;
    TransformKey compositeSource = null;
    Path compositePath = null;
    boolean compositeMoved = false;

    for (Transform part : parts) {
      if (part.name != null) {
        compositeName = part.name;
      }
      if (part.required != null) {
        compositeRequired = part.required;
      }
      if (part.visible != null) {
        compositeVisible = part.visible;
      }
      if (part.virtualValue != null) {
        compositeVirtualValue = part.virtualValue;
      }
      if (part.source != null) {
        compositeSource = part.source;
      }
      if (part.path != null) {
        compositePath = part.path;
      }
      if (part.isMoved) {
        compositeMoved = true;
      }
    }

    this.name = compositeName;
    this.required = compositeRequired;
    this.visible = compositeVisible;
    this.virtualValue = compositeVirtualValue;
    this.source = compositeSource;
    this.path = compositePath;
    this.isMoved = compositeMoved;
  }

  /**
   * Constructs a transform out of a base transform and a source transform.
   * This differs from the {@link #Transform(Iterable)} constructor by only
   * using certain values from the source.  The differences are: 
   * <ul>
   * <li>Requiredness is based only on the transform, not on the source.</li>
   * <li>The path is pulled only from the transform, not from the source.  If
   * the source also has a path it will be resolved during parsing/generation as
   * appropriate.<li>
   * <li>Ignores if the source is marked as being moved.</li>
   * <li>Explicitly nulls the {@code source} field, as the source has already
   * been taken into account.</li>
   */
  Transform(Transform transform, Transform source) {
    this.name = first(transform.name, source.name);
    this.required = transform.required;
    this.visible = first(transform.visible, source.visible);
    this.virtualValue = first(transform.virtualValue, source.virtualValue);
    this.path = transform.path;
    this.isMoved = transform.isMoved;
    
    // In this special case we've included source information, so we get rid
    // of the source variable so we don't try to include it again.
    this.source = null;
  }

  /**
   * Returns the first non-null value, or null if all were null.
   */
  static <T> T first(T... ts) {
    for (T t : ts) {
      if (t != null) {
        return t;
      }
    }
    return null;
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
  
  Path getPath() {
    return path;
  }

  boolean isMoved() {
    return isMoved;
  }
  
  boolean isEmpty() {
    return name == null
    && required == null
    && visible == null
    && virtualValue == null
    && source == null
    && path == null
    && !isMoved;
  }
}
