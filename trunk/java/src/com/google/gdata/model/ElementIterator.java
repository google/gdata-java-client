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

import com.google.gdata.model.ElementMetadata.SingleVirtualElement;
import com.google.gdata.model.ElementMetadata.MultipleVirtualElement;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * An iterator over child elements of an element using the metadata to set the
 * iteration order.  If no metadata is provided the child elements will be
 * treated as if they were all undeclared, and the iteration order will be the
 * order they were added to the element.
 *
 * 
 */
class ElementIterator implements Iterator<Element> {

  private enum Mode {DECLARED, UNDECLARED, DONE}

  private final Element element;
  private final ElementMetadata<?, ?> metadata;
  private final Map<QName, Object> elements;

  private Iterator<ElementKey<?, ?>> metadataIterator;
  private Iterator<? extends Element> sublistIterator;
  private Iterator<Object> elementIterator;
  private Element nextElement;
  private Mode mode = Mode.DECLARED;

  ElementIterator(Element element, ElementMetadata<?, ?> metadata,
      Map<QName, Object> elements) {
    this.element = element;
    this.metadata = metadata;
    this.elements = elements;
    this.metadataIterator = (metadata == null) ? null
        : metadata.getElements().iterator();
    this.elementIterator = (elements == null) ? null
        : elements.values().iterator();

    nextElement = findNextElement();
  }

  public boolean hasNext() {
    return nextElement != null;
  }

  public Element next() {
    if (nextElement == null) {
      throw new NoSuchElementException("No remaining elements");
    }
    Element retval = nextElement;
    nextElement = findNextElement();
    return retval;
  }

  public void remove() {
    throw new UnsupportedOperationException(
        "Removal not supported on element iterator");
  }

  /**
   * Returns the next sequential element in the iteration order or
   * {@code null} if there are no remaining elements.
   *
   * @return next element or {@code null}
   */
  private Element findNextElement() {

    // If we are iterating a sublist and elements remain, return the element
    // in the sublist.
    if (sublistIterator != null) {
      if (sublistIterator.hasNext()) {
        return sublistIterator.next();
      }
      // Done with the sublist iterator.
      sublistIterator = null;
    }

    Element next = null;
    while (next == null && mode != Mode.DONE) {
      switch(mode) {
        case DECLARED:
          next = findNextDeclaredElement();
          break;
        case UNDECLARED:
          next = findNextUndeclaredElement();
          break;
        default:
          break;
      }
    }

    return next;
  }

  /**
   * Finds the next declared element, if one exists, otherwise returns null.
   * Will set the mode to undeclared if nothing was found.
   */
  private Element findNextDeclaredElement() {
    if (metadataIterator != null) {
      while (metadataIterator.hasNext()) {
        ElementKey<?, ?> nextKey = metadataIterator.next();
        if (ElementCreatorImpl.ELEMENT_MARKER == nextKey) {
          mode = Mode.UNDECLARED;
          return null;
        }

        ElementMetadata<?, ?> childMeta = metadata.bindElement(nextKey);
        if (!childMeta.isVisible()) {
          continue;
        }

        SingleVirtualElement singleVirtual = 
            childMeta.getSingleVirtualElement();
        if (singleVirtual != null) {
          Element generated =
              singleVirtual.generateSingle(element, metadata, childMeta);
          if (generated != null) {
            return generated;
          }
        }

        MultipleVirtualElement multipleVirtual = 
            childMeta.getMultipleVirtualElement();
        if (multipleVirtual != null) {
          Collection<? extends Element> virtualElements =
              multipleVirtual.generateMultiple(element, metadata, childMeta);
          if (virtualElements != null && !virtualElements.isEmpty()) {
            sublistIterator = virtualElements.iterator();
            return sublistIterator.next();
          }
        }

        Object obj = getElementObject(nextKey.getId());
        Element first = firstElement(obj);
        if (first != null) {
          return first;
        }
      }

      // No more declared elements, turn the iterator off.
      metadataIterator = null;
    }

    // Check undeclared next.
    mode = Mode.UNDECLARED;
    return null;
  }

  /**
   * Find the next undeclared element, or null if no more undeclared elements
   * exist.
   */
  private Element findNextUndeclaredElement() {
    if (elementIterator != null) {
      while (elementIterator.hasNext()) {
        Object next = elementIterator.next();
        Element first = firstElement(next);
        if (first != null && isUndeclared(first.getElementKey())) {
          return first;
        } else {

          // Clear the sublist iterator if the first element wasn't valid.
          sublistIterator = null;
        }
      }

      // No more undeclared elements, turn the iterators off.
      sublistIterator = null;
      elementIterator = null;
    }

    // Go back and check for any remaining declared metadata.
    mode = metadataIterator != null && metadataIterator.hasNext()
        ? Mode.DECLARED : Mode.DONE;
    return null;
  }

  /**
   * Get the first element from either a singleton or a collection of
   * elements.  This method will also set the sublistIterator as a side effect
   * of retrieving the first element in a collection.
   */
  private Element firstElement(Object obj) {
    if (obj == null) {
      return null;
    }
    // If the next declared element is a single instance, then just
    // return it.  On the next call we will move to the next declared
    // type.
    if (obj instanceof Element) {
      return (Element) obj;
    } else {
      // If the next declared element is a collection of elements, set the
      // sublist iterator to the content of the collection and return the
      // first element.  The next call will continue iteration on this
      // collection.
      Collection<Element> elementCollection = castElementCollection(obj);
      if (!elementCollection.isEmpty()) {
        sublistIterator = elementCollection.iterator();
        return sublistIterator.next();
      }
    }
    return null;
  }

  /**
   * Suppress the warnings around casting the object in the map into a
   * collection of elements.
   */
  @SuppressWarnings("unchecked")
  private <T extends Element> Collection<T> castElementCollection(Object obj) {
    return (Collection<T>) obj;
  }

  /**
   * This method just returns the bare object stored in the map, or null if
   * either the map didn't contain the object or the map is null.
   */
  private Object getElementObject(QName id) {
    return (elements != null) ? elements.get(id) : null;
  }

  /**
   * Returns true if the given element was not declared.
   */
  private boolean isUndeclared(ElementKey<?, ?> key) {
    return (metadata == null) || !metadata.isDeclared(key);
  }
}
