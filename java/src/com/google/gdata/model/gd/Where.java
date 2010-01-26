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

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes a place (not necessarily a specific geographical location).
 *
 * 
 */
public class Where extends Element {

  /** Meaning of this location. */
  public static final class Rel {

    /** Place where the enclosing event occurs. */
    public static final String EVENT = Namespaces.gPrefix + "event";

    /** Secondary location. */
    public static final String EVENT_ALTERNATE = Namespaces.gPrefix +
        "event.alternate";

    /** Nearby parking lot. */
    public static final String EVENT_PARKING = Namespaces.gPrefix +
        "event.parking";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      EVENT,
      EVENT_ALTERNATE,
      EVENT_PARKING};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Rel() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Where> KEY = ElementKey.of(new QName(Namespaces.gNs, "where"), Void.class,
      Where.class);

  /**
   * User-readable label that identifies this location in case multiple
   * locations may be present.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * Meaning of this location.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * Text description of the place.
   */
  public static final AttributeKey<String> VALUE_STRING = AttributeKey.of(new
      QName(null, "valueString"), String.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addAttribute(LABEL);
    builder.addAttribute(REL);
    builder.addAttribute(VALUE_STRING);
    builder.addElement(EntryLink.KEY);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Where() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Where(ElementKey<?, ? extends Where> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected Where(ElementKey<?, ? extends Where> key, Element source) {
    super(key, source);
  }

  @Override
  public Where lock() {
    return (Where) super.lock();
  }

  /**
   * Returns the nested person or venue (Contact) entry.
   *
   * @return nested person or venue (Contact) entry
   */
  public EntryLink getEntryLink() {
    return super.getElement(EntryLink.KEY);
  }

  /**
   * Sets the nested person or venue (Contact) entry.
   *
   * @param entryLink nested person or venue (Contact) entry or {@code null} to
   *     reset
   * @return this to enable chaining setters
   */
  public Where setEntryLink(EntryLink entryLink) {
    super.setElement(EntryLink.KEY, entryLink);
    return this;
  }

  /**
   * Returns whether it has the nested person or venue (Contact) entry.
   *
   * @return whether it has the nested person or venue (Contact) entry
   */
  public boolean hasEntryLink() {
    return super.hasElement(EntryLink.KEY);
  }

  /**
   * Returns the user-readable label that identifies this location in case
   * multiple locations may be present.
   *
   * @return user-readable label that identifies this location in case multiple
   *     locations may be present
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the user-readable label that identifies this location in case multiple
   * locations may be present.
   *
   * @param label user-readable label that identifies this location in case
   *     multiple locations may be present or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Where setLabel(String label) {
    super.setAttributeValue(LABEL, label);
    return this;
  }

  /**
   * Returns whether it has the user-readable label that identifies this
   * location in case multiple locations may be present.
   *
   * @return whether it has the user-readable label that identifies this
   *     location in case multiple locations may be present
   */
  public boolean hasLabel() {
    return super.hasAttribute(LABEL);
  }

  /**
   * Returns the meaning of this location.
   *
   * @return meaning of this location
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the meaning of this location.
   *
   * @param rel meaning of this location or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Where setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the meaning of this location.
   *
   * @return whether it has the meaning of this location
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  /**
   * Returns the text description of the place.
   *
   * @return text description of the place
   */
  public String getValueString() {
    return super.getAttributeValue(VALUE_STRING);
  }

  /**
   * Sets the text description of the place.
   *
   * @param valueString text description of the place or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Where setValueString(String valueString) {
    super.setAttributeValue(VALUE_STRING, valueString);
    return this;
  }

  /**
   * Returns whether it has the text description of the place.
   *
   * @return whether it has the text description of the place
   */
  public boolean hasValueString() {
    return super.hasAttribute(VALUE_STRING);
  }

}


