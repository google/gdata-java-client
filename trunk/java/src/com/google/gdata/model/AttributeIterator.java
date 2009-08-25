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

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;

/**
 * An iterator over the attributes in an element using the metadata to set the
 * iteration order.  If no metadata is provided the attributes will be treated
 * as if they were all undeclared, and the iteration order will be the order
 * they were added to the element.
 *
 * 
 */
class AttributeIterator implements Iterator<Attribute> {

  private enum Mode {DECLARED, UNDECLARED, DONE}

  private final Element element;
  private final ElementMetadata<?, ?> metadata;

  private Iterator<AttributeKey<?>> metadataIterator;
  private Iterator<Map.Entry<QName, Attribute>> attributeIterator;
  private Attribute nextAttribute;
  private Mode mode = Mode.DECLARED;

  AttributeIterator(Element element, ElementMetadata<?, ?> metadata,
      Map<QName, Attribute> attributes) {
    this.element = element;
    this.metadata = metadata;
    this.metadataIterator = (metadata == null) ? null
        : metadata.getAttributes().iterator();
    this.attributeIterator = (attributes == null) ? null
        : attributes.entrySet().iterator();

    nextAttribute = findNextAttribute();
  }

  public boolean hasNext() {
    return nextAttribute != null;
  }

  public Attribute next() {
    if (nextAttribute == null) {
      throw new NoSuchElementException("No remaining attributes");
    }
    Attribute retVal = nextAttribute;
    nextAttribute = findNextAttribute();
    return retVal;
  }

  public void remove() {
    throw new UnsupportedOperationException(
        "Removal not supported on attribute iterator");
  }

  /**
   * Returns the next attribute in the iteration order or {@code null} if there
   * are no remaining attributes.
   */
  private Attribute findNextAttribute() {
    Attribute next = null;
    while (next == null && mode != Mode.DONE) {
      switch(mode) {
        case DECLARED:
          next = findNextDeclaredAttribute();
          break;
        case UNDECLARED:
          next = findNextUndeclaredAttribute();
          break;
        default:
          break;
      }
    }
    return next;
  }

  /**
   * Finds the next declared attribute, or null if no more exist.
   */
  private Attribute findNextDeclaredAttribute() {
    if (metadataIterator != null) {
      while (metadataIterator.hasNext()) {
        AttributeKey<?> nextKey = metadataIterator.next();
        if (ElementCreatorImpl.ATTRIBUTE_MARKER == nextKey) {
          mode = Mode.UNDECLARED;
          return null;
        }

        AttributeMetadata<?> attMeta = metadata.bindAttribute(nextKey);
        if (!attMeta.isVisible()) {
          continue;
        }

        Object value = attMeta.generateValue(element, metadata);
        if (value != null) {
          return new Attribute(nextKey, value);
        }
      }

      // No more declared attributes, turn the iterator off.
      metadataIterator = null;
    }

    // Check undeclared next.
    mode = Mode.UNDECLARED;
    return null;
  }

  /**
   * Finds the next valid undeclared attribute in the map.
   */
  private Attribute findNextUndeclaredAttribute() {
    if (attributeIterator != null) {
      while (attributeIterator.hasNext()) {
        Entry<QName, Attribute> entry = attributeIterator.next();
        Attribute attribute = entry.getValue();
        if (isUndeclared(attribute.getAttributeKey())) {
          return entry.getValue();
        }
      }

      // No more attributes, turn the iterator off.
      attributeIterator = null;
    }

    // Go back and check for any remaining declared attributes if needed.
    mode = metadataIterator != null && metadataIterator.hasNext()
        ? Mode.DECLARED : Mode.DONE;
    return null;
  }

  /**
   * Returns true if the given attribute was not declared.
   */
  private boolean isUndeclared(AttributeKey<?> key) {
    return (metadata == null) || !metadata.isDeclared(key);
  }
}
