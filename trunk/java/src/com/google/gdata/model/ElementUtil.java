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

import java.util.Map;

/**
 * Some package-private utility methods for dealing with elements.
 * 
 * 
 */
class ElementUtil {

  /**
   * Interface for callbacks to bind a metadata key.  Needed for adaptations to
   * work properly.
   */
  interface KeyBinder {
    
    /**
     * Binds the key to metadata.
     */
    Metadata<?> bind(MetadataKey<?> key);
  }

  /**
   * A simple implementation of {@link KeyBinder} for use with regular metadata.
   */
  private static class MetadataBinder implements KeyBinder {
    private final ElementMetadata<?, ?> metadata;

    MetadataBinder(ElementMetadata<?, ?> metadata) {
      this.metadata = metadata;
    }
    
    public Metadata<?> bind(MetadataKey<?> key) {
      if (key instanceof AttributeKey<?>) {
        return metadata.bindAttribute((AttributeKey<?>) key);
      } else {
        return metadata.bindElement((ElementKey<?, ?>) key);
      }
    }
  }
  
  /**
   * Searches a map of of metadata keys indexed by id to find the best match for
   * the provided id.   The algorithm supports both direct lookup or a linear
   * search for input ids that have a namespace wildcard.
   * <p>
   * If the input id has a namespace wildcard, the method will return the first
   * visible metadata type that has a matching local name or that contains a
   * wildcard local name.
   * <p>
   * If the input id does not have a namespace wildcard and also does not
   * directly match any id in the map, an attempt will be made to look for a
   * map entry that contains a localname wildcard for the same namespace and
   * that id.
   * <p>
   * Any time a match is made to any map key that has a wildcard local name, a
   * key in the same namespace as the matched id but with the local name of the
   * input id will be returned.
   * 
   * @param id qualified name to match
   * @param keyMap map to search
   * @return matching metadata key or {@code null}
   */
  static <K extends MetadataKey<?>> K findKey(ElementMetadata<?, ?> meta,
      QName id, Map<QName, K> keyMap) {
    return findKey(new MetadataBinder(meta), id, keyMap);
  }

  /**
   * Searches a map of of metadata keys indexed by id to find the best match for
   * the provided id.   The algorithm supports both direct lookup or a linear
   * search for input ids that have a namespace wildcard.
   * <p>
   * If the input id has a namespace wildcard, the method will return the first
   * visible metadata type that has a matching local name or that contains a
   * wildcard local name.
   * <p>
   * If the input id does not have a namespace wildcard and also does not
   * directly match any id in the map, an attempt will be made to look for a
   * map entry that contains a localname wildcard for the same namespace and
   * that id.
   * <p>
   * Any time a match is made to any map key that has a wildcard local name, a
   * key in the same namespace as the matched id but with the local name of the
   * input id will be returned.
   * 
   * @param id qualified name to match
   * @param keyMap map to search
   * @return matching metadata key or {@code null}
   */
  static <K extends MetadataKey<?>> K findKey(KeyBinder binder, QName id,
      Map<QName, K> keyMap) {
    if (keyMap.isEmpty()) {
      return null;
    }

    K key = keyMap.get(id);
    if (key != null) {
       return key;
     }

    // For wildcarded ids, iterate and return the first matching key
    if (id.matchesAnyNamespace()) {
      for (Map.Entry<QName, K> mapEntry :  keyMap.entrySet()) {

        QName keyId = mapEntry.getKey();
        if (id.matches(keyId) || keyId.matchesAnyLocalName()) {
          key = mapEntry.getValue();

          // Only visible types are valid for wildcard matching
          Metadata<?> keyMetadata = binder.bind(key);
          if (keyMetadata.isVisible()) {

            // For name wildcards, return a key bound to the namespace of the
            // wildcard and the id of the input key
            if (key.getId().matchesAnyLocalName()) {
              return makeKey(
                  new QName(key.getId().getNs(), id.getLocalName()), key);
            }

            // Only return if the matched key hasn't been renamed
            if (id.matches(keyMetadata.getName())) {
              return key;
            }
          }
        }
      }
    } else if (!id.matchesAnyLocalName()) {
      // See if there is a foo:* match for the given id.
      key = keyMap.get(toWildcardLocalName(id));
      if (key != null) {
        return makeKey(id, key);
      }
    }

    return null;
  }

  /**
   * Returns an id of the form ns:*, if the given id does not already represent
   * the localname.
   */
  private static QName toWildcardLocalName(QName id) {
    return new QName(id.getNs(), QName.ANY_LOCALNAME);
  }
  
  /**
   * Creates a new metadata key of the same type as the input key with the
   * provided id and the same value types as the input key.
   */
  @SuppressWarnings("unchecked")
  private static <K extends MetadataKey<?>> K makeKey(QName id, K key) {
    if (key instanceof AttributeKey<?>) {
      return (K) AttributeKey.of(id, key.getDatatype());
    } else {
      return (K) ElementKey.of(id, key.getDatatype(),
          ((ElementKey<?, ?>) key).getElementType());
    }
  }
}
