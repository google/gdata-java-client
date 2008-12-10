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
package com.google.gdata.util.common.base;

import java.io.Serializable;

/**
 * Pair is an expedient way to combine two arbitrary objects into one.  <b>Its
 * use is not to be preferred</b> to creating a custom type, but it can be
 * handy in a pinch.  If your {@link #equals}, {@link #hashCode},
 * {@link #toString}, and/or {@link #clone} behavior matter to you at all, you
 * may not want to use this (or you may at least wish to subclass).
 *
 *

 *
 * <p>This class may be used with the Google Web Toolkit (GWT).
 *
 */
public class Pair<A,B> implements Serializable, Cloneable {
  private static final long serialVersionUID = 747826592375603043L;

  /** Creates a new pair containing the given objects in order. */
  public static <A,B> Pair<A,B> of(A first, B second) {
    return new Pair<A,B>(first, second);
  }

  /** The first element of the pair. */
  public final A first;

  /** The second element of the pair. */
  public final B second;

  /** Cumbersome way to create a new pair (see {@link #of}. */
  public Pair(A first, B second) {
    this.first = first;
    this.second = second;
  }

  /** Optional accessor method for {@link #first}. */
  public A getFirst() {
    return first;
  }

  /** Optional accessor method for {@link #second}. */
  public B getSecond() {
    return second;
  }

  @Override public Pair<A,B> clone() {
    try {
      return (Pair<A,B>) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError(e);





    }
  }

  @Override public boolean equals(Object object) {


    if (object instanceof Pair<?,?>) {
      Pair<?,?> other = (Pair<?,?>) object;
      return eq(first, other.first) && eq(second, other.second);
    }
    return false;
  }

  @Override public int hashCode() {
    /*
     * This combination yields entropy comparable to constituent hashes
     * and ensures that hash(a,b) != hash(b,a).
     */
    return (hash(first, 0) + 0xDeadBeef) ^ hash(second, 0);
  }

  /**
   * This implementation returns the String representation of this pair in
   * the form {@code (string1, string2)}, where {@code string1} and
   * {@code string2} are the {@link Object#toString} representations of the
   * elements inside the pair.
   */
  @Override public String toString() {
    return String.format("(%s, %s)", first, second);
  }


  private static boolean eq(Object a, Object b) {
    return a == b || (a != null && a.equals(b));
  }

  private static int hash(Object object, int nullHash) {
    return (object == null) ? nullHash : object.hashCode();
  }
}

