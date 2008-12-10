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
 * GData schema extension describing a node with a boolean value. The class is
 * abstract, derive from this class and define default constructor which
 * hardcodes the node name.
 *
 * 
 */
public abstract class BoolValueConstruct extends ValueConstruct {

  /** Variable to cache the boolean value. */
  private boolean boolValue;

  /**
   * Constructs {@link ValueConstruct} to represent boolean value.
   */
  public BoolValueConstruct(String nodeName) {
    super(Namespaces.WT_NAMESPACE, nodeName, null);
    setBooleanValue(false);
  }

  /**
   * Compares {@link BoolValueConstruct} objects based on the boolean value
   * that they hold.
   */
  @Override
  public boolean equals(Object rhs) {
    if (!super.equals(rhs)) {
      return false;
    }

    BoolValueConstruct r = (BoolValueConstruct) rhs;
    return boolValue == r.boolValue;
  }

  /** 
   * Returns hash code which is based on the boolean string representation.
   */
  @Override
  public int hashCode() {
    return getValue().hashCode();
  }

  /**
   * Override {@link ValueConstruct#setValue(String)} to validate that
   * supplied value is xsd:boolean which is a standard way to represent boolean
   * in XML. xsd:boolean treats "true" and "1" as true and "false" and "0" as
   * false.
   * 
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if value is not valid xsd:boolean value.
   */
  @Override
  public void setValue(String value) {
    if (value == null) {
      throw new NullPointerException("value can not be null");
    }

    if ("true".equals(value) || "1".equals(value)) {
      setBooleanValue(true);
    } else if ("false".equals(value) || "0".equals(value)) {
      setBooleanValue(false);
    } else {
      throw new IllegalArgumentException("Invalid boolean value: " + value);
    }
  }

  /** Set boolean value. */
  public void setBooleanValue(boolean value) {
    boolValue = value;
    super.setValue(String.valueOf(value));
  }

  /** Get boolean value. */
  public boolean getBooleanValue() {
    return boolValue;
  }
}
