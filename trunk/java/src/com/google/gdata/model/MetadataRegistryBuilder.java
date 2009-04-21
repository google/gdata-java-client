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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gdata.model.MetadataRegistry.RootKey;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Set;

/**
 * A mutable, thread-safe builder for a metadata registry.  This should be used
 * to construct the default metadata for elements and attributes, the transforms
 * on those default metadata instances, and any adaptations on the element
 * types.  The {@link #create} method can be called to construct an immutable
 * {@link MetadataRegistry} from this builder.
 *
 * <p>Thread safety is guaranteed by always guarding access to the element and
 * attribute maps by synchronizing on the builder itself.  The individual
 * element and attribute builders are also thread safe.
 *
 * 
 */
public final class MetadataRegistryBuilder {

  /**
   * Copy static factory to copy an existing metadata registry builder.
   */
  public static MetadataRegistryBuilder copy(MetadataRegistryBuilder source) {
    return new MetadataRegistryBuilder(source);
  }

  // The attribute registries that store the attribute data, indexed by ID.
  private final Map<RootKey, AttributeMetadataRegistryBuilder> attributes =
      Maps.newHashMap();

  // The element registries that store the element data, indexed by ID and
  // base element type.
  private final Map<RootKey, ElementMetadataRegistryBuilder> elements =
      Maps.newHashMap();

  // The cached registry build from this builder.  Will be set to null if the
  // registry has been modified since last being built.
  private volatile MetadataRegistry cachedRegistry;

  /**
   * Construct a new empty metadata registry builder.
   */
  public MetadataRegistryBuilder() {
    // Nothing to see here.
  }

  /**
   * Copy constructor, copies from the given builder into this one.
   */
  MetadataRegistryBuilder(MetadataRegistryBuilder source) {

    // Sychronize on the source builder so no changes may be made to it while
    // we are copying it.
    synchronized (source) {
      for (Map.Entry<RootKey, AttributeMetadataRegistryBuilder> entry
          : source.attributes.entrySet()) {
        attributes.put(entry.getKey(),
            new AttributeMetadataRegistryBuilder(this, entry.getValue()));
      }
      for (Map.Entry<RootKey, ElementMetadataRegistryBuilder> entry
          : source.elements.entrySet()) {
        elements.put(entry.getKey(),
            new ElementMetadataRegistryBuilder(this, entry.getValue()));
      }
    }
  }

  /**
   * Creates a metadata registry out of this builder. Will return a cached
   * instance if the builder has not changed since last time create() was
   * called.
   */
  public MetadataRegistry create() {
    MetadataRegistry instance = cachedRegistry;
    if (instance == null) {
      return buildRegistry();
    }
    return instance;
  }

  /**
   * Synchronized to ensure memory visiblity into the attribute and element
   * maps and also to guarantee that no changes to the registry can occur while
   * the immutable registry is being built.
   */
  private synchronized MetadataRegistry buildRegistry() {
    MetadataRegistry instance = cachedRegistry;
    if (instance == null) {
      whitelistAttributes();
      whitelistElements();

      cachedRegistry = instance = new MetadataRegistry(this);
    }
    return instance;
  }

  /**
   * Dirty this builder.  Whoever is modifying this builder must also hold
   * onto this builder's lock while modifying it, by using a
   * {@code sychronized(registryBuilder) ...} block, or this method will throw
   * an {@link IllegalStateException}.
   */
  void dirty() {
    Preconditions.checkState(Thread.holdsLock(this),
        "Cannot call dirty() without holding the lock on the registry.");
    cachedRegistry = null;
  }

  /**
   * Here is what we do to deal with whitelisted attributes:
   * 1) Find each transform(parent, child, context)-> whitelisted attributes
   * 2) Find composite(parent, child, context)-> all attributes.
   * 3) Add transforms(child, attribute, context) -> hide or unhide.
   */
  private void whitelistAttributes() {

    for (ElementMetadataRegistryBuilder builder : elements.values()) {
      Map<TransformKey, Set<AttributeKey<?>>> whitelistMap =
          Maps.newLinkedHashMap();

      Map<TransformKey, ElementCreatorImpl> creators = builder.getCreators();

      for (Map.Entry<TransformKey, ElementCreatorImpl> entry
          : creators.entrySet()) {
        TransformKey key = entry.getKey();
        ElementCreatorImpl element = entry.getValue();
        if (element.getAttributeWhitelist() != null) {
          whitelistMap.put(key, element.getAttributeWhitelist());
        }
      }

      for (Map.Entry<TransformKey, Set<AttributeKey<?>>> whitelistEntry
          : whitelistMap.entrySet()) {
        TransformKey key = whitelistEntry.getKey();
        Set<AttributeKey<?>> whitelist = whitelistEntry.getValue();
        Set<QName> whitelistNames = Sets.newHashSet();
        for (AttributeKey<?> whitelistKey : whitelist) {
          whitelistNames.add(whitelistKey.getId());
        }

        Set<AttributeKey<?>> allAttributes = Sets.newHashSet();
        for (Map.Entry<TransformKey, ElementCreatorImpl> entry
            : creators.entrySet()) {

          if (entry.getKey().matches(key)) {
            allAttributes.addAll(entry.getValue().getAttributeSet());
          }
        }

        if (!allAttributes.containsAll(whitelist)) {
          Set<AttributeKey<?>> missing = Sets.newHashSet(whitelist);
          missing.removeAll(allAttributes);
          throw new IllegalStateException(
              "Missing attributes!  Whitelist specified " + missing
              + " but did not find those attributes.");
        }

        for (AttributeKey<?> attribute : allAttributes) {
          ElementKey<?, ?> parent = (ElementKey<?, ?>) key.getKey();
          build(parent, attribute, key.getContext())
              .setVisible(whitelistNames.contains(attribute.getId()));
        }
      }
    }
  }

  /**
   * Here is what we do to deal with whitelisted children:
   * 1) Find each transform(parent, child, context)-> whitelisted children
   * 2) Find composite(parent, child, context)-> all children.
   * 3) Add transforms(child, grandchild, context) -> hide or unhide.
   */
  private void whitelistElements() {

    for (Map.Entry<RootKey, ElementMetadataRegistryBuilder> rootEntry
        : elements.entrySet()) {
      ElementMetadataRegistryBuilder builder = rootEntry.getValue();
      Map<TransformKey, Set<ElementKey<?, ?>>> whitelistMap =
          Maps.newLinkedHashMap();

      Map<TransformKey, ElementCreatorImpl> creators = builder.getCreators();

      for (Map.Entry<TransformKey, ElementCreatorImpl> entry
          : creators.entrySet()) {
        TransformKey key = entry.getKey();
        ElementCreatorImpl element = entry.getValue();
        if (element.getElementWhitelist() != null) {
          whitelistMap.put(key, element.getElementWhitelist());
        }
      }

      for (Map.Entry<TransformKey, Set<ElementKey<?, ?>>> whitelistEntry
          : whitelistMap.entrySet()) {
        TransformKey key = whitelistEntry.getKey();
        Set<ElementKey<?, ?>> whitelist = whitelistEntry.getValue();
        Set<QName> whitelistNames = Sets.newHashSet();
        for (ElementKey<?, ?> whitelistKey : whitelist) {
          whitelistNames.add(whitelistKey.getId());
        }

        Set<ElementKey<?, ?>> allChildren = Sets.newHashSet();
        for (Map.Entry<TransformKey, ElementCreatorImpl> entry
            : creators.entrySet()) {

          if (entry.getKey().matches(key)) {
            allChildren.addAll(entry.getValue().getElementSet());
          }
        }

        if (!allChildren.containsAll(whitelist)) {
          Set<ElementKey<?, ?>> missing = Sets.newHashSet(whitelist);
          missing.removeAll(allChildren);
          throw new IllegalStateException(
              "Missing children!  Whitelist specified " + missing
              + " but did not find those child elements.");
        }

        for (ElementKey<?, ?> child : allChildren) {
          ElementKey<?, ?> parent = (ElementKey<?, ?>) key.getKey();
          build(parent, child, key.getContext())
              .setVisible(whitelistNames.contains(child.getId()));
        }
      }
    }
  }

  /**
   * Builds element metadata for the given key.  This will create the default
   * metadata for that key.  If the key's element type has a superclass with
   * existing metadata registered at the same ID, the new key will inherit
   * any existing properties.
   */
  public ElementCreator build(ElementKey<?, ?> element) {
    return build(null, element, null);
  }

  /**
   * Builds metadata for when the {@code key} is inside the {@code parent}.
   * Note that this will not declare the element as part of the parent element,
   * that must be done using {@link ElementCreator#addElement(ElementKey)} or
   * {@link ElementCreator#replaceElement(ElementKey)}.
   */
  public ElementCreator build(ElementKey<?, ?> parent,
      ElementKey<?, ?> element) {
    return build(parent, element, null);
  }

  /**
   * Builds metadata for when the {@code key} is used in a context compatible
   * with {@code context}.
   */
  public ElementCreator build(ElementKey<?, ?> element,
      MetadataContext context) {
    return build(null, element, context);
  }

  /**
   * Builds metadata for when the {@code key} is inside the {@code parent} and
   * used in a context compatible with {@code context}.  Note that this will not
   * declare the element as part of the parent element, that must be done using
   * {@link ElementCreator#addElement(ElementKey)} or
   * {@link ElementCreator#replaceElement(ElementKey)}.
   */
  public ElementCreator build(ElementKey<?, ?> parent,
      ElementKey<?, ?> element, MetadataContext context) {
    return getOrCreateElement(element).build(parent, element, context);
  }

  /**
   * Returns the existing element registry for the given key, or creates it if
   * it does not already exist.
   */
  private synchronized ElementMetadataRegistryBuilder getOrCreateElement(
          ElementKey<?, ?> key) {
    RootKey rootKey = MetadataRegistry.getRootKey(key);
    ElementMetadataRegistryBuilder elementRegistry = elements.get(rootKey);
    if (elementRegistry == null) {
      elementRegistry = new ElementMetadataRegistryBuilder(this);
      elements.put(rootKey, elementRegistry);
    }
    dirty();
    return elementRegistry;
  }

  /**
   * Builds the metadata for the attribute inside the parent.  Note that this
   * will not declare the attribute as part of the parent element, that must be
   * done using {@link ElementCreator#addAttribute(AttributeKey)} or
   * {@link ElementCreator#replaceAttribute(AttributeKey)}.
   */
  public AttributeCreator build(
      ElementKey<?, ?> parent, AttributeKey<?> attribute) {
    return build(parent, attribute, null);
  }

  /**
   * Builds the metadata for the attribute inside the parent, during the
   * context.  Note that this will not declare the attribute as part of the
   * parent element, that must be done using
   * {@link ElementCreator#addAttribute(AttributeKey)} or
   * {@link ElementCreator#replaceAttribute(AttributeKey)}.
   */
  public AttributeCreator build(ElementKey<?, ?> parent,
      AttributeKey<?> attribute, MetadataContext context) {
    return getOrCreateAttribute(attribute).build(parent, attribute, context);
  }

  /**
   * Returns the existing element registry for the given key, or creates it if
   * it does not already exist.
   */
  private synchronized AttributeMetadataRegistryBuilder getOrCreateAttribute(
      AttributeKey<?> key) {
    RootKey rootKey = MetadataRegistry.getRootKey(key);
    AttributeMetadataRegistryBuilder attRegistry = attributes.get(rootKey);
    if (attRegistry == null) {
      attRegistry = new AttributeMetadataRegistryBuilder(this);
      attributes.put(rootKey, attRegistry);
    }
    dirty();
    return attRegistry;
  }

  /**
   * Package level read-only access to the attributes.
   */
  Map<RootKey, AttributeMetadataRegistryBuilder> getAttributes() {
    return ImmutableMap.copyOf(attributes);
  }

  /**
   * Package level read-only access to the elements.
   */
  Map<RootKey, ElementMetadataRegistryBuilder> getElements() {
    return ImmutableMap.copyOf(elements);
  }
}
