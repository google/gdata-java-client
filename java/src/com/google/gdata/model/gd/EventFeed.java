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


package com.google.gdata.model.gd;

import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.atom.Category;
import com.google.gdata.model.atom.Feed;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes an event feed.
 *
 * 
 */
public class EventFeed extends Feed {

  /**
   * Event kind term value.
   */
  public static final String KIND = Namespaces.gPrefix + "event";

  /**
   * Event kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind,
      KIND).lock();

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<Void,
      EventFeed> KEY = ElementKey.of(Feed.KEY.getId(), Void.class,
      EventFeed.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // Register superclass metadata.
    Feed.registerMetadata(registry);

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addUndeclaredElementMarker();
    builder.addElement(EventEntry.KEY);

    // Adaptations from the super type
    registry.adapt(Feed.KEY, KIND, KEY);
  }

  /**
   * Constructs an instance using the default key.
   */
  public EventFeed() {
    super(KEY);
    addCategory(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Feed} instance.
   *
   * @param sourceFeed source feed
   */
  public EventFeed(Feed sourceFeed) {
    super(KEY, sourceFeed);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected EventFeed(ElementKey<?, ? extends EventFeed> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Feed} instance. Will use the given {@link ElementKey} as the key for
   * the element. This constructor is used when adapting from one element key to
   * another. You cannot call this constructor directly, instead use {@link
   * Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected EventFeed(ElementKey<?, ? extends EventFeed> key, Feed source) {
    super(key, source);
  }

  @Override
  public EventFeed lock() {
    return (EventFeed) super.lock();
  }

  @Override
  public List<? extends EventEntry> getEntries() {
    return getEntries(EventEntry.KEY);
  }

}


