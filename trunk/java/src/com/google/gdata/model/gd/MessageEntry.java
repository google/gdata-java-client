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
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.atom.Category;
import com.google.gdata.model.atom.Entry;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Extension class for manipulating entries of the Message kind.
 *
 * 
 */
public class MessageEntry extends Entry {

  /**
   * Message kind term value.
   */
  public static final String KIND = Namespaces.gPrefix + "message";

  /**
   * Message kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind,
      KIND).lock();

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<Void,
      MessageEntry> KEY = ElementKey.of(Entry.KEY.getId(), Void.class,
      MessageEntry.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // Register superclass metadata.
    Entry.registerMetadata(registry);

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addElement(GeoPt.KEY);
    builder.addElement(Rating.KEY);
    builder.addElement(When.KEY);
    builder.addElement(Who.KEY).setCardinality(
        ElementMetadata.Cardinality.MULTIPLE);

    // Adaptations from the super type
    registry.adapt(Entry.KEY, KIND, KEY);
  }

  /**
   * Constructs an instance using the default key.
   */
  public MessageEntry() {
    super(KEY);
    addCategory(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Entry} instance.
   *
   * @param sourceEntry source entry
   */
  public MessageEntry(Entry sourceEntry) {
    super(KEY, sourceEntry);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected MessageEntry(ElementKey<?, ? extends MessageEntry> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Entry} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected MessageEntry(ElementKey<?, ? extends MessageEntry> key,
      Entry source) {
    super(key, source);
  }

  @Override
  public MessageEntry lock() {
    return (MessageEntry) super.lock();
  }

  /**
   * Returns the geo pt.
   *
   * @return geo pt
   */
  public GeoPt getGeoPt() {
    return super.getElement(GeoPt.KEY);
  }

  /**
   * Sets the geo pt.
   *
   * @param geoPt geo pt or {@code null} to reset
   * @return this to enable chaining setters
   */
  public MessageEntry setGeoPt(GeoPt geoPt) {
    super.setElement(GeoPt.KEY, geoPt);
    return this;
  }

  /**
   * Returns whether it has the geo pt.
   *
   * @return whether it has the geo pt
   */
  public boolean hasGeoPt() {
    return super.hasElement(GeoPt.KEY);
  }

  /**
   * Returns the rating.
   *
   * @return rating
   */
  public Rating getRating() {
    return super.getElement(Rating.KEY);
  }

  /**
   * Sets the rating.
   *
   * @param rating rating or {@code null} to reset
   * @return this to enable chaining setters
   */
  public MessageEntry setRating(Rating rating) {
    super.setElement(Rating.KEY, rating);
    return this;
  }

  /**
   * Returns whether it has the rating.
   *
   * @return whether it has the rating
   */
  public boolean hasRating() {
    return super.hasElement(Rating.KEY);
  }

  /**
   * Returns the time period description.
   *
   * @return time period description
   */
  public When getTime() {
    return super.getElement(When.KEY);
  }

  /**
   * Sets the time period description.
   *
   * @param time time period description or {@code null} to reset
   * @return this to enable chaining setters
   */
  public MessageEntry setTime(When time) {
    super.setElement(When.KEY, time);
    return this;
  }

  /**
   * Returns whether it has the time period description.
   *
   * @return whether it has the time period description
   */
  public boolean hasTime() {
    return super.hasElement(When.KEY);
  }

  /**
   * Returns the person descriptions.
   *
   * @return person descriptions
   */
  public List<Who> getWhoList() {
    return super.getElements(Who.KEY);
  }

  /**
   * Adds a new person description.
   *
   * @param whoList person description
   */
  public MessageEntry addWhoList(Who whoList) {
    super.addElement(Who.KEY, whoList);
    return this;
  }

  /**
   * Removes an existing person description.
   *
   * @param whoList person description
   * @return true if the whoList was removed
   */
  public boolean removeWhoList(Who whoList) {
    return super.removeElement(whoList);
  }

  /**
   * Removes all existing person description instances.
   */
  public void clearWhoList() {
    super.removeElement(Who.KEY);
  }

  /**
   * Returns whether it has the person descriptions.
   *
   * @return whether it has the person descriptions
   */
  public boolean hasWhoList() {
    return super.hasElement(Who.KEY);
  }

}


