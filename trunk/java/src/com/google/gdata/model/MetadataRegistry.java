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

import com.google.gdata.util.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gdata.model.Schema.RootKey;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

/**
 * A mutable, thread-safe registry for metadata.  This should be used
 * to construct the default metadata for elements and attributes, the transforms
 * on those default metadata instances, and any adaptations on the element
 * types.  The {@link #createSchema} method can be called to construct an
 * immutable {@link Schema} from this builder.
 *
 * <p>Thread safety is guaranteed by always guarding access to the element and
 * attribute maps by synchronizing on the builder itself.  The individual
 * element and attribute registries are also thread safe.
 *
 * 
 */
public final class MetadataRegistry {

  // The attribute registries that store the attribute data, indexed by ID.
  private final Map<RootKey, AttributeMetadataRegistryBuilder> attributes;

  // The element registries that store the element data, indexed by ID and
  // base element type.
  private final Map<RootKey, ElementMetadataRegistryBuilder> elements;

  // The cached schema built from this builder.  Will be set to null if the
  // registry has been modified since last being built.
  private volatile Schema cachedSchema;

  /**
   * Constructs a new empty metadata registry.
   */
  public MetadataRegistry() {
    this.attributes = Maps.newHashMap();
    this.elements = Maps.newHashMap();
  }

  /**
   * Copy constructor, creates a copy of the given metadata registry. This is
   * equivalent to {@code new MetadataRegistry().merge(source)}.
   */
  public MetadataRegistry(MetadataRegistry source) {
    this();
    merge(source);
  }

  /**
   * Merges another metadata registry into this metadata registry.  Both
   * registries are locked during this time, first this registry and then the
   * other registry.  Do not attempt to do a.merge(b) and b.merge(a) in separate
   * threads at the same time or a deadlock may occur.
   */
  public synchronized MetadataRegistry merge(MetadataRegistry other) {
    synchronized (other) {
      for (Map.Entry<RootKey, AttributeMetadataRegistryBuilder> entry
          : other.attributes.entrySet()) {
        RootKey key = entry.getKey();
        AttributeMetadataRegistryBuilder builder = attributes.get(key);
        if (builder == null) {
          builder = new AttributeMetadataRegistryBuilder(this);
          attributes.put(key, builder);
        }
        builder.merge(entry.getValue());
      }
      for (Map.Entry<RootKey, ElementMetadataRegistryBuilder> entry
          : other.elements.entrySet()) {
        RootKey key = entry.getKey();
        ElementMetadataRegistryBuilder builder = elements.get(key);
        if (builder == null) {
          builder = new ElementMetadataRegistryBuilder(this);
          elements.put(key, builder);
        }
        builder.merge(entry.getValue());
      }
    }
    return this;
  }

  /**
   * Registers the metadata for an element key.  This will call the
   * "registerMetadata" method on the {@link Element} subclass that the key
   * refers to, if it refers to a subclass.  If it refers to Element directly
   * it will just build the key.
   *
   * @throws IllegalArgumentException if the element type does not support a
   * registration method.
   */
  public MetadataRegistry register(ElementKey<?, ?> key) {
    if (key != null) {
      Class<? extends Element> elementType = key.getElementType();
      if (Element.class == elementType) {
        build(key);
      } else {
        registerClass(elementType);
      }
    }
    return this;
  }

  /**
   * Registers the metadata from an element subclass using its
   * "registerMetadata" method through reflection.
   */
  public synchronized MetadataRegistry registerClass(
      Class<? extends Element> clazz) {

    // Element itself does not require registration, so skip it.
    if (Element.class == clazz) {
      return this;
    }
    try {
      Method registerMethod = clazz.getDeclaredMethod(
          "registerMetadata", MetadataRegistry.class);

      if (!Modifier.isStatic(registerMethod.getModifiers())) {
        throw new IllegalArgumentException("Class " + clazz
            + " had a non-static registerMetadata(MetadataRegistry) method.");
      }
      registerMethod.invoke(null, this);
      return this;
    } catch (SecurityException e) {
      // Something fishy is going on, rethrow a runtime exception.
      throw new IllegalArgumentException(e);
    } catch (NoSuchMethodException e) {
      // No "registerMetadata" method found, throw an exception.
      throw new IllegalArgumentException(
          "Class " + clazz + " doesn't support metadata registration.", e);
    } catch (IllegalAccessException e) {
      // Method wasn't public, throw an illegal argument exception.
      throw new IllegalArgumentException(e);
    } catch (InvocationTargetException e) {
      // Our registration method threw an exception!  Rethrown the nested
      // exception in an illegal argument exception.
      throw new IllegalArgumentException(e.getCause());
    }
  }

  /**
   * Creates a {@link Schema} out of this registry. Will return a cached
   * instance if the registry has not changed since last time create() was
   * called.
   */
  public Schema createSchema() {
    Schema instance = cachedSchema;
    return (instance != null) ? instance : buildSchema();
  }

  /**
   * Synchronized to ensure memory visibility into the attribute and element
   * maps and also to guarantee that no changes to the registry can occur while
   * the immutable schema is being built.
   */
  private synchronized Schema buildSchema() {
    Schema instance = cachedSchema;
    if (instance == null) {
      whitelistAttributes();
      whitelistElements();

      cachedSchema = instance = new Schema(this);
    }
    return instance;
  }

  /**
   * Dirty this registry.  Whoever is modifying this builder must also hold
   * onto this builder's lock while modifying it, by using a
   * {@code sychronized(registry) ...} block, or this method will throw
   * an {@link IllegalStateException}.
   */
  void dirty() {
    Preconditions.checkState(Thread.holdsLock(this),
        "Cannot call dirty() without holding the lock on the registry.");
    cachedSchema = null;
  }

  /**
   * Here is what we do to deal with whitelisted attributes:
   * 1) Find each transform(parent, child, context)-> whitelisted attributes
   * 2) Find composite(parent, child, context)-> all attributes.
   * 3) For each attribute not in the whitelist -> hide the attribute.
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
          if (!whitelistNames.contains(attribute.getId())) {
            ElementKey<?, ?> parent = (ElementKey<?, ?>) key.getKey();
            build(parent, attribute, key.getContext()).setVisible(false);
          }
        }
      }
    }
  }

  /**
   * Here is what we do to deal with whitelisted children:
   * 1) Find each transform(parent, child, context)-> whitelisted children
   * 2) Find composite(parent, child, context)-> all children.
   * 3) For each child element not in the whitelist -> hide the child element.
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
          if (!whitelistNames.contains(child.getId())) {
            ElementKey<?, ?> parent = (ElementKey<?, ?>) key.getKey();
            build(parent, child, key.getContext()).setVisible(false);
          }
        }
      }
    }
  }

  /**
   * Returns {@code true} if the given key has already been registered.  This
   * is used to prevent reentrant metadata registration (and cycles).
   */
  public boolean isRegistered(ElementKey<?, ?> key) {
    RootKey rootKey = Schema.getRootKey(key);
    ElementMetadataRegistryBuilder elementRegistry = elements.get(rootKey);
    if (elementRegistry != null) {
      return elementRegistry.isRegistered(null, key, null);
    }
    return false;
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
   *
   * <p>This will also guarantee that any element types that are referenced
   * have been registered in this registry.
   */
  public ElementCreator build(ElementKey<?, ?> parent,
      ElementKey<?, ?> element, MetadataContext context) {
    ElementCreatorImpl creator = getOrCreateElement(element).build(
        parent, element, context);

    // If there is a parent or context this isn't a basic registration, so force
    // basic registration to take place.
    if (parent != null || context != null) {
      register(element);
    }
    return creator;
  }

  /**
   * Returns the existing element registry for the given key, or creates it if
   * it does not already exist.
   */
  private synchronized ElementMetadataRegistryBuilder getOrCreateElement(
          ElementKey<?, ?> key) {
    RootKey rootKey = Schema.getRootKey(key);
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
    RootKey rootKey = Schema.getRootKey(key);
    AttributeMetadataRegistryBuilder attRegistry = attributes.get(rootKey);
    if (attRegistry == null) {
      attRegistry = new AttributeMetadataRegistryBuilder(this);
      attributes.put(rootKey, attRegistry);
    }
    dirty();
    return attRegistry;
  }

  /**
   * Adapts from the source type to the adaptation type on the given kind.
   */
  public <D, E extends Element> void adapt(ElementKey<D, E> source,
      String kind, ElementKey<? extends D, ? extends E> adaptation) {
    build(source).adapt(kind, adaptation);
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
