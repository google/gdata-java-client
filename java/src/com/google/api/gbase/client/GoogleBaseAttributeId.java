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
 * The definition (name and type) of a Google Base attribute.
 */
public class GoogleBaseAttributeId {
  private final String name;
  private final GoogleBaseAttributeType type;

  /**
   * Creates a public attribute id.
   *
   * @param name attribute name
   * @param type attribute type, can be null if it is not known
   */
  public GoogleBaseAttributeId(String name, GoogleBaseAttributeType type) {
    if (name == null) {
      throw new NullPointerException("attribute 'name' is required");
    }
    this.name = name;
    this.type = type;
  }

  /**
   * Gets the attribute name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the attribute type, or null if it is not known.
   */
  public GoogleBaseAttributeType getType() {
    return type;
  }
  
  @Override
  public String toString() {
    if (type == null) {
      return name;
    } else {
      return name + "(" + type + ")";
    }
  }
  
  @Override
  public int hashCode() {
    int retval = 19 + name.hashCode();
    if (type != null) { 
      retval = retval * 37 + type.hashCode();
    }
    return retval;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof GoogleBaseAttributeId)) {
      return false;
    }
    
    GoogleBaseAttributeId other = (GoogleBaseAttributeId)o;
    if (!name.equals(other.name)) {
      return false;
    }
    
    if (type == null) {
      return other.type == null;
    } else {
      return type.equals(other.type);
    }
  }
}
