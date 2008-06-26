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


package com.google.gdata.data.appsforyourdomain.generic;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.appsforyourdomain.Namespaces;
import com.google.gdata.data.appsforyourdomain.Property;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * GData entry class that models a generic feed entry which contains only
 * name-value pairs of data.
 * 
 * @see GenericFeed
 * 
 * 
 */
public class GenericEntry extends BaseEntry<GenericEntry> {

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(GenericEntry.class, Property.getDefaultDescription());
    
    // Declare our "apps" namespace
    extProfile.declareAdditionalNamespace(Namespaces.APPS_NAMESPACE);
  }

  /**
   * Add a new name-value pair to this entry.
   */
  public void addProperty(String name, String value) {
    Property prop = new Property();
    prop.setName(name);
    prop.setValue(value);
    getRepeatingExtension(Property.class).add(prop);
  }

  /**
   * Convenience method to add multiple name-value pairs to this entry.
   * 
   * @param properties the {@link Map} of name-value pairs to add
   */
  public void addProperties(Map<String, String> properties) {
    for (Map.Entry<String, String> entry : properties.entrySet()) {
      addProperty(entry.getKey(), entry.getValue());
    }
  }
  
  /**
   * Retrieve the value for a name.
   * <p>
   * Note: If you want all the properties in this entry, use
   * {@link #getAllProperties()}
   * 
   * @return null if entry does not contain any value for the given name.
   */
  public String getProperty(String name) {
    List<Property> properties =
        getRepeatingExtension(Property.class);
    for (Property prop : properties) {
      if (prop.getName().equals(name)) {
        return prop.getValue();
      }
    }
    return null;
  }

  /**
   * Remove the property with the given {@code name} (if it exists).
   * 
   * @return true if a property with this name was found and removed, false
   *         otherwise
   */
  public boolean removeProperty(String name) {
    List<Property> properties =
        getRepeatingExtension(Property.class);
    Property found = null;
    
    for (Property prop : properties) {
      if (prop.getName().equals(name)) {
        found = prop;
        break;
      }
    }
    
    if (found != null) {
      removeRepeatingExtension(found);
      return true;
    } else {
      return false;
    }
  }

  /**
   * @return Map containing all the name-value pairs in this entry.
   */
  public Map<String, String> getAllProperties() {
    Map<String, String> map = new HashMap<String, String>();
    List<Property> properties =
        getRepeatingExtension(Property.class);
    for (Property prop : properties) {
      map.put(prop.getName(), prop.getValue());
    }
    return map;
  }

  /**
   * A GenericEntry is valid if it satisfies the following conditions:
   * <ul>
   * <li>All Properties have a non-null names (values can be null)</li>
   * <li>No two properties have the same name</li>
   * </ul>
   * This method is called by the GData framework on the server side.
   * 
   * @throws IllegalStateException If the current state of this entry is
   *         invalid.
   */
  @Override
  public void validate() throws IllegalStateException {
    Set<String> names = new HashSet<String>();
    List<Property> properties 
            = getRepeatingExtension(Property.class);
    for (Property prop : properties) {
      String name = prop.getName();
      if (name == null) {
        throw new IllegalStateException("Found property with null name");
      }
      if (names.contains(name)) {
        throw new IllegalStateException("Duplicate property: " + name);
      }
      names.add(name);
    }
  }
}
