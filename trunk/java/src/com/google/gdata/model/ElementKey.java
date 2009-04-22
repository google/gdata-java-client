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
 * A key referring to a particular element.  Holds the ID of the element and the
 * expected datatype and element type. Element keys support value-based
 * equality, natural ordering, and matching.
 * <ul>
 * <li>An element key is {@link #equals(Object)} to another element key if their
 * IDs are equivalent or both {@code null}, and their datatypes, and element
 * types are the same.<li>
 * <li>An element key's natural ordering is based first on the name, then on
 * the element type, and finally on the data type.</li>
 * <li>An element key {@link #matches(MetadataKey)} another element key if the
 * ID is a match for the ID of the other key, and the datatype and element type
 * are both assignable from the other key's datatype and element type.</li>
 * </ul>
 *
 * @param <D> the datatype of the element
 * @param <E> the element type of the element
 * 
 */
public final class ElementKey<D, E extends Element> extends MetadataKey<D> {

  /**
   * Return a default element key using a string datatype and
   * {@link Element} as the element type. The id must not be {@code null}.
   */
  public static ElementKey<String, Element> of(QName id) {
    return of(id, String.class, Element.class);
  }

  /**
   * Construct an element key with the given id and element type, but with
   * a {@link Void} datatype.  This is used for elements without text content.
   *
   * <p>The {@code elementType} must not be {@code null}.  A null id is only
   * valid for element types that are a subclass of {@link Element}, and are
   * used as a key referring to all instances of that element type.
   */
  public static <V extends Element> ElementKey<Void, V> of(
      QName id, Class<? extends V> elementType) {
    return of(id, Void.class, elementType);
  }

  /**
   * Construct an element key with the given id, datatype, and element type.
   * This is used for elements that contain text content.
   *
   * <p>The {@code elementType} must not be {@code null}.  A null id is only
   * valid for element types that are a subclass of {@link Element}, and are
   * used as a key referring to all instances of that element type.
   */
  public static <T, V extends Element> ElementKey<T, V> of(QName id,
      Class<? extends T> datatype, Class<? extends V> elementType) {
    return new ElementKey<T, V>(id, datatype, elementType);
  }

  // The element type for this key.
  final Class<? extends E> elementType;

  /**
   * Construct a new element key. Both datatype and element type must not be
   * {@code null}.  The id may be null only if the key represents a construct;
   * the element type must be a subclass of {@link Element}.
   */
  private ElementKey(QName id, Class<? extends D> datatype,
      Class<? extends E> elementType) {
    super(id, datatype);
    Preconditions.checkNotNull(elementType, "elementType");
    if (Element.class == elementType) {
      Preconditions.checkNotNull(id, "id");
    }
    this.elementType = elementType;
  }

  /**
   * Returns the element type of the element.
   */
  public Class<? extends E> getElementType() {
    return elementType;
  }

  /**
   * Returns {@code true} if this key is a match for the given key. This key is
   * a match for the other key if the other key is also an element key and if
   * the ID, datatype, and element types all match.
   */
  @Override
  public boolean matches(MetadataKey<?> other) {
    if (other == null) {
      return false;
    }

    if (!(other instanceof ElementKey<?, ?>)) {
      return false;
    }

    if (!matchIdAndDatatype(other)) {
      return false;
    }

    return elementType.isAssignableFrom(((ElementKey<?, ?>) other).elementType);
  }

  /**
   * Compares first on ID, then on element type, then on datatype.
   */
  public int compareTo(MetadataKey<?> other) {
    if (other == this) {
      return 0;
    }

    // If they aren't the same type, put element keys at the end.
    if (!(other instanceof ElementKey<?, ?>)) {
      return 1;
    }

    int compare = compareQName(id, other.id);
    if (compare != 0) {
      return compare;
    }

    compare = compareClass(elementType, ((ElementKey<?, ?>) other).elementType);
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
    hashCode *= 17;
    return hashCode + elementType.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != ElementKey.class) {
      return false;
    }
    ElementKey<?, ?> o = (ElementKey<?, ?>) obj;

    if (id == null) {
      if (o.id != null) {
        return false;
      }
    } else if (!id.equals(o.id)) {
      return false;
    }

    return elementType == o.elementType
        && datatype == o.datatype;
  }

  @Override
  public String toString() {
    return "{ElementKey " + id
        + ", D:" + datatype
        + ", E:" + elementType + "}";
  }
}
