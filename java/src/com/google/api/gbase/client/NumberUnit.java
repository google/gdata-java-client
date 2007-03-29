/* Copyright (c) 2006 Google Inc.
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

package com.google.api.gbase.client;

/**
 * A number with a unit.
 *
 * @param <T> concrete type for the value, either Integer or Float
 */
public class NumberUnit<T extends Number>  {
  private final T value;
  private final String unit;

  /** Creates a new value with a unit. */
  public NumberUnit(T value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  /** Gets the value. */
  public T getValue() {
    return value;
  }

  /** Gets the unit. */
  public String getUnit() {
    return unit;
  }

  /**
   * Gets the standard string representation for such
   * an object: <code>value " " unit</code>
   */
  @Override
  public String toString() {
    return value + " " + unit;
  }

  @Override
  public int hashCode() {
    return 47 * value.hashCode() + unit.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof NumberUnit)) {
      return false;
    }

    NumberUnit<? extends Number> other = (NumberUnit<? extends Number>)o;
    return value.equals(other.value) && unit.equals(other.unit);
  }
}
