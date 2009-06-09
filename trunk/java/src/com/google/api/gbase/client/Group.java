/* Copyright (c) 2009 Google Inc.
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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Collections;

/**
 * Group type holds multiple sub-attributes.
 * Sub-attributes can be any type except GROUP and its derived types 
 * (e.g. TAX and SHIPPING). 
 * 
 * 
 */
public class Group {
  
  private Multimap<String, GoogleBaseAttribute> subAttributes;
  
  public Group(Collection<GoogleBaseAttribute> subAttributes) {
    this.subAttributes = HashMultimap.<String, GoogleBaseAttribute>create();
    for (GoogleBaseAttribute attr : subAttributes) {
      this.subAttributes.put(attr.getName(), attr);
    }
  }
  
  /**
   * Gets one sub-attribute with the name name, if there are any.
   * @param name 
   * @return one of the sub-attributes with the specified name.
   */
  public GoogleBaseAttribute getSubAttribute(String name) {
    if (subAttributes.containsKey(name)) {
      Collection<GoogleBaseAttribute> attrs = subAttributes.get(name);
      if (!attrs.isEmpty()) {
        return subAttributes.get(name).iterator().next();
      }
    }
    return null;
  }
  
  /**
   * Gets all sub-attributes corresponding to the name.  
   * 
   * @param name name
   * @return A collection of GoogleBaseAttribute corresponds to the name
   */
  public Collection<? extends GoogleBaseAttribute> getSubAttributes(String name) {
    if (subAttributes.containsKey(name)) {
      return subAttributes.get(name);
    } else {
      return Collections.<GoogleBaseAttribute>emptySet();
    }
  }
  
  /**
   * Gets all sub-attributes of the group.
   */
  public Multimap<String, GoogleBaseAttribute> getAllSubAttributes() {
    return HashMultimap.<String, GoogleBaseAttribute>create(subAttributes);
  }
  
  public int subAttributesSize() {
    return subAttributes.size();
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("[");
    for (String key : subAttributes.keySet()) {
      Collection<GoogleBaseAttribute> attrs = subAttributes.get(key);
      for (GoogleBaseAttribute attr : attrs) {
        builder.append(key).append("(").append(attr.getType()).append("): ")
            .append(attr.getValueAsString()).append(", ");
      }
    }
    builder.delete(builder.length() - 2, builder.length() - 1); // delete the last comma.
    builder.append("]");
    return builder.toString();
  }
}
