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

import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
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

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.replaceElement(Category.KEY).setRequired(true);
    builder.replaceElement(EventEntry.KEY);
    builder.addUndeclaredElementMarker();
    builder.addElement(EventEntry.KEY);
    DefaultRegistry.adapt(Feed.KEY, KIND, KEY);
  }

  /**
   * Default mutable constructor.
   */
  public EventFeed() {
    this(KEY);
  }

  /**
   * Create an instance using a different key.
   */
  public EventFeed(ElementKey<Void, ? extends EventFeed> key) {
    super(key);
    addCategory(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Feed} instance. Will use the given {@link ElementKey} as the key for
   * the element.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  public EventFeed(ElementKey<Void, ? extends EventFeed> key, Feed source) {
    super(key, source);
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

   @Override
   public EventFeed lock() {
     return (EventFeed) super.lock();
   }

  @Override
  public List<? extends EventEntry> getEntries() {
    return getEntries(EventEntry.KEY);
  }

  @Override
  public String toString() {
    return "{EventFeed " + super.toString() + "}";
  }

}
