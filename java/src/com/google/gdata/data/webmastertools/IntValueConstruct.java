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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.ValueConstruct;

/**
 * GData schema extension describing a node with an integer value. The class is
 * abstract, derive from this class and define default constructor which
 * hardcodes the node name.
 *
 * 
 */
public abstract class IntValueConstruct extends ValueConstruct {

  /** Variable to cache the integer value. */
  private int intValue;
  
  /** Constructs {@link ValueConstruct} to represent integer value. */
  public IntValueConstruct(String nodeName) {
    super(Namespaces.WT_NAMESPACE, nodeName, null);
    setIntValue(0);
  }

  /**
   * Compares {@link IntValueConstruct} objects based on the integer value
   * that they hold.
   */  
  @Override
  public boolean equals(Object rhs) {
    if (!super.equals(rhs)) {
      return false;
    }

    IntValueConstruct r = (IntValueConstruct) rhs;
    return intValue == r.intValue;
  }

  /** 
   * Returns hash code which is the integer value that the object holds.
   */  
  @Override
  public int hashCode() {
    return intValue;
  }

  /**
   * Override {@link ValueConstruct#setValue(String)} to validate that
   * supplied value is integer.
   *
   * @throws NullPointerException if argument is null.
   * @throws IllegalArgumentException if argument is not a valid integer.
   */
  @Override
  public void setValue(String value) {
    if (value == null) {
      throw new NullPointerException("value can not be null");
    }

    try {
      Integer i = Integer.parseInt(value);
      setIntValue(i);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /** Set {@link Integer} value. */
  public void setIntValue(int value) {
    intValue = value;
    super.setValue(String.valueOf(value));
  }

  /** Get {@link Integer} value. */
  public int getIntValue() {
    return intValue;
  }
}
