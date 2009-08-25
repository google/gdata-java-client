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
 * A key representing a particular metadata instance. All metadata keys have at
 * least an ID and a datatype. Metadata keys have a natural ordering based first
 * on key type, then on the natural ordering within that key type. Metadata keys
 * also support equivalence and matching, both of which are defined by subtypes.
 * See {@link AttributeKey} and {@link ElementKey} for the details.
 *
 * @param <D> the datatype of this key
 * 
 */
public abstract class MetadataKey<D> implements Comparable<MetadataKey<?>> {

  // The id and datatype for this key.
  final QName id;
  final Class<? extends D> datatype;

  /**
   * Construct a new attribute key.  The datatype must not be {@code null}.  A
   * null {@code id} indicates that this value is a construct and should not be
   * used directly.
   */
  MetadataKey(QName id, Class<? extends D> datatype) {
    Preconditions.checkNotNull(datatype, "datatype");
    this.id = id;
    this.datatype = datatype;
  }

  /**
   * Returns the id of the property.  This is the canonical name of the
   * property, and will stay the same across transforms and contexts.  It is
   * based on the XML schema for the atom representation.
   *
   * @return the unique identifier of the property.
   */
  public QName getId() {
    return id;
  }

  /**
   * Returns the datatype of the property.  This the type of the attribute value
   * or the text content of an element.
   *
   * @return the datatype of the property
   */
  public Class<? extends D> getDatatype() {
    return datatype;
  }

  /**
   * Returns {@code true} if this key matches the given key.
   */
  public abstract boolean matches(MetadataKey<?> other);

  /**
   * Match on id and datatype, used by subclasses to implement matching.
   */
  boolean matchIdAndDatatype(MetadataKey<?> other) {
    if (id != null && !id.matches(other.id)) {
      return false;
    }

    // Datatypes much match if they are non-default types.
    return datatype.isAssignableFrom(other.datatype)
        || other.datatype == String.class;
  }

  /**
   * Order two QNames, where either may be null.  A null value is considered
   * lower than a non-null value, and will come before it in the list.  This
   * forces more-specific qnames to appear later.
   */
  static int compareQName(QName a, QName b) {
    if (a == b) {
      return 0;
    }
    // If a is null (and b non-null), a < b.
    if (a == null) {
      return -1;
    }
    // If b is null (and a non-null), a > b.
    if (b == null) {
      return 1;
    }
    // Else compare using normal QName compare.
    return a.compareTo(b);
  }

  /**
   * Order two class objects. If a class is a supertype of another class, it
   * comes before that class, so more specific classes are later in the list.
   * If neither class is a supertype of the other, the classes are ordered
   * based on the lexical ordering of the point in the class hierarchy where the
   * two classes diverge.
   */
  static int compareClass(Class<?> a, Class<?> b) {
    if (a == b) {
      return 0;
    }
    // If a is a supertype of b, return -1
    if (a.isAssignableFrom(b)) {
      return -1;
    }
    // If b is a supertype of a, return 1.
    if (b.isAssignableFrom(a)) {
      return 1;
    }

    // Compare based on the lexical ordering of the most general superclass
    // of the given classes that don't match.
    a = getFirstNonAssignable(a, b);
    b = getFirstNonAssignable(b, a);

    // Compare them based on lexical ordering of the non-assignable classes.
    return a.getName().compareTo(b.getName());
  }

  /**
   * Find the first class in a's type hierarchy that is not assignable
   * from b.  This is the first class after a and b's common ancestor (which
   * may be Object).
   */
  static Class<?> getFirstNonAssignable(Class<?> a, Class<?> b) {
    Class<?> superA = a.getSuperclass();
    while (!superA.isAssignableFrom(b)) {
      a = superA;
      superA = a.getSuperclass();
    }
    return a;
  }

  @Override
  public String toString() {
    return "{MetadataKey " + id + ", D:" + datatype + "}";
  }
}
