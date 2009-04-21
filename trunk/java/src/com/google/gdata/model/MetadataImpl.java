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
 * Immutable base implementation of {@link Metadata}.
 *
 * 
 */
abstract class MetadataImpl<D> implements Metadata<D> {

  final MetadataRegistry registry;
  final MetadataKey<D> key;
  final ElementKey<?, ?> parent;
  final MetadataContext context;
  final QName name;
  final boolean isRequired;
  final boolean isUndeclared;
  final boolean isVisible;

  /**
   * Constructs a new immutable metadata instance with the given declared data.
   * Any information not contained in the builder will use the default values.
   */
  MetadataImpl(MetadataRegistry registry, Transform transform,
      ElementKey<?, ?> parent, MetadataKey<D> key, MetadataContext context) {
    this.registry = registry;
    this.key = key;
    this.parent = parent;
    this.context = context;
    this.name = nullToDefault(transform.name, key.getId());
    this.isRequired = nullToDefault(transform.required, false);
    this.isVisible = nullToDefault(transform.visible, true);
    this.isUndeclared = false;
  }

  /**
   * Constructs a new immutable undeclared metadata instance.
   */
  MetadataImpl(MetadataKey<D> key) {
    this.registry = null;
    this.parent = null;
    this.key = key;
    this.context = null;
    this.name = key.getId();
    this.isRequired = false;
    this.isVisible = true;
    this.isUndeclared = true;
  }

  /**
   * Provides a default value if the given value is null, allowing us to set
   * the boolean fields to their defaults if the transform does not contain a
   * value.
   */
  static <T> T nullToDefault(T value, T defaultValue) {
    return (value != null) ? value : defaultValue;
  }

  public MetadataKey<D> getKey() {
    return key;
  }

  public ElementKey<?, ?> getParent() {
    return parent;
  }

  public MetadataContext getContext() {
    return context;
  }

  public QName getName() {
    return name;
  }

  public boolean isRequired() {
    return isRequired;
  }

  public boolean isUndeclared() {
    return isUndeclared;
  }

  public boolean isVisible() {
    return isVisible;
  }
}
