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
 * A key to lookup a particular metadata configuration.  This takes into account
 * static information such as the parent class of the metadata and any metadata
 * subtypes, as well as including the normal {@link MetadataContext} contextual
 * information.
 *
 * 
 */
final class TransformKey implements Comparable<TransformKey> {

  /**
   * Creates a metadata key for the given parent class.  Will return null if
   * {@code parentClass} is null.
   *
   * @param parent the parent to create a transform for.
   * @return a key with just the parent class set, or null if the parent class
   *     was null.
   */
  static TransformKey forParent(ElementKey<?, ?> parent,
      MetadataKey<?> key) {
    return forTransform(parent, key, null);
  }

  /**
   * Creates a metadata key for the given subtype.  Will return null if
   * {@code subType} is null.
   *
   * @param key the metadata key to create a transform for.
   * @return a key with just the subtype set, or null if the subtype was null.
   */
  static TransformKey forKey(MetadataKey<?> key) {
    return forTransform(null, key, null);
  }

  /**
   * Creates a metadata key for the given metadata context.  Will return null if
   * {@code context} is null.
   *
   * @param context the metadata context to create a key for.
   * @return a key with just the metadata context set, or null if the context
   *     was null.
   */
  static TransformKey forContext(MetadataKey<?> key,
      MetadataContext context) {
    return forTransform(null, key, context);
  }

  /**
   * Creates a metadata key for the given parentClass, subType, and context.
   * If all parts of the key are null, this method will return null, which is
   * the default key.
   *
   * @param parent the parent key to create a transform for
   * @param key the key to create a transform for
   * @param context the metadata context to create a transform for
   * @return a transform key with the given parts set, or null if all were null.
   */
  static TransformKey forTransform(ElementKey<?, ?> parent,
      MetadataKey<?> key, MetadataContext context) {
    Preconditions.checkNotNull(key, "key");
    return new TransformKey(parent, key, context);
  }

  private final ElementKey<?, ?> parent;
  private final MetadataKey<?> key;
  private final MetadataContext context;

  /**
   * Private constructor, only factory methods can be used to create keys.  This
   * enforces that {@code null} is the default key, and in the future could be
   * used to cache keys to allow identity semantics for equals.
   */
  private TransformKey(ElementKey<?, ?> parent,
      MetadataKey<?> key, MetadataContext context) {
    this.parent = parent;
    this.key = key;
    this.context = context;
  }

  /**
   * Returns true if this key matches the given key.  A key is considered a
   * match for another key if the parent and subtypes are matches or subtypes of
   * the given key's parent and subtype, and the context is a subset of the
   * matched context.
   */
  boolean matches(TransformKey other) {
    return (parent == null || parent.matches(other.parent))
        && (key.matches(other.key))
        && (context == null || context.matches(other.context));
  }

  /**
   * Merged this key with another key.  This will override local values if the
   * given key has a value for that part of the key.
   */
  TransformKey bind(TransformKey other) {
    if (other == null) {
      return this;
    }

    ElementKey<?, ?> otherParent = other.getParent();
    MetadataKey<?> otherKey = other.getKey();
    MetadataContext otherContext = other.getContext();

    if ((otherParent == parent)
        && (otherKey == key)
        && ((otherContext != null && otherContext.equals(context))
            || (otherContext == null && context == null))) {
      return this;
    }

    return new TransformKey(
        otherParent != null ? otherParent : parent,
        otherKey != null ? otherKey : key,
        otherContext != null ? otherContext : context);
  }

  /**
   * Gets the parent portion of the key.  This represents a parent-child
   * metadata relationship, and is used to store the metadata delta associated
   * with that relationship.
   *
   * @return the parent class or {@code null}.
   */
  ElementKey<?, ?> getParent() {
    return parent;
  }

  /**
   * Gets the subtype portion of the key.  This allows metadata modifications
   * to be associated with a particular subtype of element in the tree.
   *
   * @return the subtype class or {@code null}.
   */
  MetadataKey<?> getKey() {
    return key;
  }

  /**
   * Gets the context portion of the key.  This represents the request-scoped
   * portions of the key, and is used to create the metadata for a particular
   * request.
   *
   * @return the context portion of the key, or {@code null}.
   */
  MetadataContext getContext() {
    return context;
  }

  /**
   * Compares this transform key to the other transform key.  Transform keys are
   * sorted by:
   * <ul>
   * <li>Parent key</li>
   * <li>Key</li>
   * <li>Context</li>
   * </ul>
   *
   * <p>A null value for any part is assumed to be the lowest value.
   */
  public int compareTo(TransformKey other) {
    if (this == other) {
      return 0;
    }
    if (other == null) {
      return 1;
    }
    int compare = compare(parent, other.parent);
    if (compare != 0) {
      return compare;
    }
    compare = compare(key, other.key);
    if (compare != 0) {
      return compare;
    }
    if (context == null) {
      if (other.context != null) {
        compare = -1;
      }
    } else if (other.context == null) {
      compare = 1;
    } else {
     compare = context.compareTo(other.context);
    }
    return compare;
  }

  /**
   * Order two metadata keys, first checking nulls.  Nulls count as lower
   * than any other value, so more general transforms will show up first.
   */
  static int compare(MetadataKey<?> a, MetadataKey<?> b) {
    if (a == b) {
      return 0;
    }
    if (a == null) {
      return -1;
    }
    if (b == null) {
      return 1;
    }
    return a.compareTo(b);
  }

  @Override
  public int hashCode() {
    int hash = key.hashCode();
    hash *= 17;
    if (parent != null) {
      hash += parent.hashCode();
    }
    hash *= 17;
    if (context != null) {
      hash += context.hashCode();
    }
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof TransformKey)) {
      return false;
    }

    TransformKey other = (TransformKey) obj;
    if (parent == null) {
      if (other.parent != null) {
        return false;
      }
    } else if (!parent.equals(other.parent)) {
      return false;
    }
    if (!key.equals(other.key)) {
      return false;
    }
    if (context == null) {
      if (other.context != null) {
        return false;
      }
    } else if (!context.equals(other.context)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{TransformKey(");
    sb.append(parent == null ? "null" : parent);
    sb.append(',');
    sb.append(key == null ? "null" : key);
    sb.append(',');
    sb.append(context);
    sb.append(")}");
    return sb.toString();
  }
}
