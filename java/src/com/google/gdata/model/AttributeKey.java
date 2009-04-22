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
 * A key referring to a particular attribute.  Holds the ID of the attribute
 * and the expected datatype.  Attribute keys support value-based equality,
 * natural ordering, and matching.
 * <ul>
 * <li>An attribute key is {@link #equals(Object)} to another attribute key if
 * their IDs are equal and the datatypes are the same.</li>
 * <li>An attribute key's natural ordering is based first on name and then on
 * datatype.</li>
 * <li>An attribute key {@link #matches(MetadataKey)} another attribute key if
 * the ID is a match for the ID of the other key, and the datatype is assignable
 * from the other key's datatype.</li>
 * </ul>
 *
 * @param <D> the datatype of the attribute
 * 
 */
public final class AttributeKey<D> extends MetadataKey<D> {

  /**
   * Construct and return a new attribute key with the default datatype of
   * String.  The id must not be {@code null}.
   */
  public static AttributeKey<String> of(QName id) {
    return of(id, String.class);
  }

  /**
   * Construct and return a new attribute key with the given id and datatype.
   * Both id and datatype must not be {@code null}, or a
   * {@link NullPointerException} will be thrown.
   */
  public static <T> AttributeKey<T> of(QName id,
      Class<? extends T> datatype) {
    return new AttributeKey<T>(id, datatype);
  }

  /**
   * Construct a new attribute key.  Both id and datatype must not be
   * {@code null}.
   */
  private AttributeKey(QName id, Class<? extends D> datatype) {
    super(Preconditions.checkNotNull(id, "id"), datatype);
  }

  /**
   * Returns {@code true} if this key is a match for the given key. This key
   * is a match for the other key if the other key is also an attribute key and
   * if its ID and datatype match.
   */
  @Override
  public boolean matches(MetadataKey<?> other) {
    if (other == null) {
      return false;
    }

    if (!(other instanceof AttributeKey<?>)) {
      return false;
    }

    return matchIdAndDatatype(other);
  }

  /**
   * Compares first on ID, then on datatype.
   */
  public int compareTo(MetadataKey<?> other) {
    if (other == this) {
      return 0;
    }

    // If they aren't the same type, put attribute keys at the front.
    if (!(other instanceof AttributeKey<?>)) {
      return -1;
    }

    int compare = compareQName(id, other.id);
    if (compare != 0) {
      return compare;
    }
    return compareClass(datatype, other.datatype);
  }

  @Override
  public int hashCode() {
    int hashCode = datatype.hashCode();
    hashCode *= 17;
    if (id != null) {
      hashCode += id.hashCode();
    }
    return hashCode;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != AttributeKey.class) {
      return false;
    }
    AttributeKey<?> o = (AttributeKey<?>) obj;
    return id.equals(o.id) && datatype == o.datatype;
  }

  @Override
  public String toString() {
    return "{AttributeKey " + id + ", D:" + datatype + "}";
  }
}
