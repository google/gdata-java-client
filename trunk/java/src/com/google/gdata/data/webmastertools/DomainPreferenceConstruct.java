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

import com.google.gdata.data.EnumConstruct;
import java.util.HashSet;

/**
 * GData schema extension describing a node with a domain preference.
 * The domain preference is a value in {@code preferwww, prefernowww, none}. 
 * This class is abstract, subclasses must define a default constructor 
 * which has the node name hardcoded. See {@link EnumConstruct}.
 *
 * 
 */
public abstract class DomainPreferenceConstruct extends EnumConstruct {
  
  /**
   * Constructs {@link EnumConstruct} to represent the preference setting.
   */
  public DomainPreferenceConstruct(String nodeName) {
    super(Namespaces.WT_NAMESPACE, nodeName, null, getValues());
    setPreference(DomainPreference.getDefault());
  }

  /**
   * Creates the set of accepted values {@code none, preferwww, prefernowww}.
   * 
   * @return A HashSet with the preferred values.
   */
  private static HashSet<String> getValues() {
    HashSet<String> values = new HashSet<String>();
    for (DomainPreference preference : DomainPreference.values()) {
      values.add(preference.toString());
    }
    return values;
  }
  
  /**
   * Compares {@link DomainPreferenceConstruct} objects based on the 
   * domain preference that they they hold.
   */
  @Override
  public boolean equals(Object rhs) {
    if (!super.equals(rhs)) {
      return false;
    }

    return getValue().equals(((DomainPreferenceConstruct) rhs).getValue());
  }

  /** 
   * Returns a hash code which is based on the preference string.
   */
  @Override
  public int hashCode() {
    return getValue().hashCode();
  }

  /**
   * Override {@link EnumConstruct#setValue(String)} to validate that
   * the supplied value is a one of the accepted domain preferences.
   * See {@link DomainPreference}.
   * 
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if value is not a valid domain 
   * preference {@code none, preferwww, prefernowww}.
   */
  @Override
  public void setValue(String value) 
      throws NullPointerException, IllegalArgumentException{
    if (value == null) {
      throw new NullPointerException("value cannot be null");
    }
    
    if (!values.contains(value)) {
      throw new IllegalArgumentException(
          "The argument is not a valid preference");
    }
    
    super.setValue(value);
  }

  public void setPreference(DomainPreference value) {
    super.setValue(value.toString());
  }

  public DomainPreference getPreference() {
    return DomainPreference.fromString(getValue());
  } 
}
