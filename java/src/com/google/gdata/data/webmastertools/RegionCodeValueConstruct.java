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
 * GData schema extension describing a node with a region code value. 
 * This class is abstract, derive from this class and define a default 
 * constructor which has the node name hardcoded, see
 * http://www.unicode.org/cldr/data/diff/supplemental/territory_containment_un_m_49.html
 * for a list of valid region codes.
 *
 * 
 */
public abstract class RegionCodeValueConstruct extends ValueConstruct {

  /**
   * Default value for the region code is US
   */
  public static final String DEFAULT_REGION_CODE = "ZZ";
  
  /**
   * Constructs {@link ValueConstruct} to represent the region code value.
   */
  public RegionCodeValueConstruct(String nodeName) {
    super(Namespaces.WT_NAMESPACE, nodeName, null);
    setValue(DEFAULT_REGION_CODE);
  }

  /**
   * Compares {@link RegionCodeValueConstruct} objects based on the region code
   * value that they hold.
   */
  @Override
  public boolean equals(Object rhs) {
    if (!super.equals(rhs)) {
      return false;
    }

    return getValue().equals(((RegionCodeValueConstruct) rhs).getValue());
  }

  /** 
   * Returns hash code which is based on the Region Code string representation.
   */
  @Override
  public int hashCode() {
    return getValue().hashCode();
  }

  /**
   * Override {@link ValueConstruct#setValue(String)} to validate that
   * the region code is not null.
   * 
   * @throws NullPointerException if the value is null.
   */
  @Override
  public void setValue(String value) 
      throws NullPointerException, IllegalArgumentException{
    if (value == null) {
      throw new NullPointerException("value cannot be null");
    }
    
    super.setValue(value);
  }
}
