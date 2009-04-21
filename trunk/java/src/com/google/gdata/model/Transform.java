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
 * An immutable transform on a piece of metadata.  This contains all of the
 * base metadata fields, every value is optional (anything may be null).
 *
 * 
 */
abstract class Transform {

  /**
   * Returns {@code true} if the given transform is empty.
   */
  static boolean isEmptyTransform(Transform transform) {
    return transform.name == null
        && transform.required == null
        && transform.visible == null;
  }

  // Our final, nullable fields.
  final QName name;
  final Boolean required;
  final Boolean visible;

  /**
   * Constructs an empty transform with all null values.  This should only be
   * used by subclasses.  See {@link AttributeTransform#EMPTY} and
   * {@link ElementTransform#EMPTY} for the appropriate empty transforms.
   */
  Transform() {
    this.name = null;
    this.required = null;
    this.visible = null;
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
  }

  /**
   * Construct a composite transform out of the given parts.
   */
  Transform(Iterable<? extends Transform> parts) {
    QName compositeName = null;
    Boolean compositeRequired = null;
    Boolean compositeVisible = null;

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
    }

    this.name = compositeName;
    this.required = compositeRequired;
    this.visible = compositeVisible;
  }
}
