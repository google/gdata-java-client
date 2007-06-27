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
 * Adjustment information for one attribute ({@link GoogleBaseAttribute}). It
 * maps to {@code gm:adjusted_name} and {@code gm:adjusted_value} Xml elements.
 */
public class Adjustments {
  
  /** The adjusted name. */
  private String name;
  
  /** The adjusted value. */
  private String value;
  
  /** 
   * Gets the adjusted name, or {@code null} if the {@code adjusted_name} 
   * element is not specified. 
   */
  public String getName() {
    return name;
  }
  
  /** 
   * Gets the adjusted value in string format, or {@code null} if the 
   * {@code adjusted_value} element is not specified. 
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns {@code true} if no adjusted attribute is specified, {@code false}
   * otherwise. 
   */
  public boolean isEmpty() {
    return name == null && value == null;
  }

  /**
   * Sets the ajdusted name.
   */
  void setName(String name) {
    this.name = name;
  }
  
  /**
   * Sets the adjusted value.
   */
  void setValue(String value) {
    this.value = value;
  }
}
