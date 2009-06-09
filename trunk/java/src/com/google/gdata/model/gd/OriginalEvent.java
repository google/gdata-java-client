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
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes a link to a recurring event.
 *
 * 
 */
public class OriginalEvent extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      OriginalEvent> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "originalEvent"), Void.class, OriginalEvent.class);

  /**
   * URL of the original recurring event entry.
   */
  public static final AttributeKey<String> HREF = AttributeKey.of(new
      QName(null, "href"), String.class);

  /**
   * Event ID of the original recurring event entry.
   */
  public static final AttributeKey<String> ORIGINAL_ID = AttributeKey.of(new
      QName(null, "id"), String.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.addAttribute(HREF).setRequired(true);
    builder.addAttribute(ORIGINAL_ID).setRequired(true);
    builder.addElement(When.KEY).setRequired(true);
  }

  /**
   * Default mutable constructor.
   */
  public OriginalEvent() {
    this(KEY);
  }

  /**
   * Create an instance using a different key.
   */
  public OriginalEvent(ElementKey<Void, ? extends OriginalEvent> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  public OriginalEvent(ElementKey<Void, ? extends OriginalEvent> key,
      Element source) {
    super(key, source);
  }

   @Override
   public OriginalEvent lock() {
     return (OriginalEvent) super.lock();
   }

  /**
   * Returns the URL of the original recurring event entry.
   *
   * @return URL of the original recurring event entry
   */
  public String getHref() {
    return super.getAttributeValue(HREF);
  }

  /**
   * Sets the URL of the original recurring event entry.
   *
   * @param href URL of the original recurring event entry or <code>null</code>
   *     to reset
   */
  public OriginalEvent setHref(String href) {
    super.setAttributeValue(HREF, href);
    return this;
  }

  /**
   * Returns whether it has the URL of the original recurring event entry.
   *
   * @return whether it has the URL of the original recurring event entry
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the event ID of the original recurring event entry.
   *
   * @return event ID of the original recurring event entry
   */
  public String getOriginalId() {
    return super.getAttributeValue(ORIGINAL_ID);
  }

  /**
   * Sets the event ID of the original recurring event entry.
   *
   * @param originalId event ID of the original recurring event entry or
   *     <code>null</code> to reset
   */
  public OriginalEvent setOriginalId(String originalId) {
    super.setAttributeValue(ORIGINAL_ID, originalId);
    return this;
  }

  /**
   * Returns whether it has the event ID of the original recurring event entry.
   *
   * @return whether it has the event ID of the original recurring event entry
   */
  public boolean hasOriginalId() {
    return getOriginalId() != null;
  }

  /**
   * Returns the original start time.
   *
   * @return original start time
   */
  public When getOriginalStartTime() {
    return super.getElement(When.KEY);
  }

  /**
   * Sets the original start time.
   *
   * @param originalStartTime original start time or <code>null</code> to reset
   */
  public OriginalEvent setOriginalStartTime(When originalStartTime) {
    super.setElement(When.KEY, originalStartTime);
    return this;
  }

  /**
   * Returns whether it has the original start time.
   *
   * @return whether it has the original start time
   */
  public boolean hasOriginalStartTime() {
    return super.hasElement(When.KEY);
  }

  @Override
  public String toString() {
    return "{OriginalEvent href=" + getAttributeValue(HREF) + " originalId=" +
        getAttributeValue(ORIGINAL_ID) + "}";
  }

}
