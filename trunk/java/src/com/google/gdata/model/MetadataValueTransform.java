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


package com.google.gdata.model;

import com.google.common.collect.Lists;
import com.google.gdata.model.ElementCreator.ValueTransform;

import java.util.List;

/**
 * This class represents a transformation of a value based on a list of keys
 * to use as the value for the element.  This will check each of the keys
 * for a value, using the first value found.
 *
 * 
 */
public class MetadataValueTransform implements ValueTransform {

  // A list of metadata sources for the value.
  private final List<MetadataKey<?>> sources;

  /**
   * Constructs a simple value transform using the given sources.
   *
   * @param inputSources the source keys to pull the values from.
   */
  public MetadataValueTransform(MetadataKey<?>... inputSources) {
    this.sources = Lists.newArrayList(inputSources);
  }

  /**
   * Runs this transformation on the given element.
   *
   * @param e the element to transform.
   * @return the String representation of the element value.
   */
  public String transform(Element e) {
    for (MetadataKey<?> source : sources) {
      if (source instanceof ElementKey<?, ?>) {
        Element child = e.getElement((ElementKey<?, ?>) source);
        if (child != null) {
          String childValue = child.getMetadata().getValueAsString(child);
          if (childValue != null) {
            return childValue;
          }
        }
      } else {
        Object attValue = e.getAttributeValue((AttributeKey<?>) source);
        if (attValue != null) {
          return attValue.toString();
        }
      }
    }
    return null;
  }
}