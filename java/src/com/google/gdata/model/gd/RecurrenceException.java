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
 * Describes an exception to a recurring event.
 *
 * 
 */
public class RecurrenceException extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      RecurrenceException> KEY = ElementKey.of(new QName(Namespaces.gNs,
      "recurrenceException"), Void.class, RecurrenceException.class);

  /**
   * Whether the exception is specialized.
   */
  public static final AttributeKey<Boolean> SPECIALIZED = AttributeKey.of(new
      QName(null, "specialized"), Boolean.class);

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY);
    builder.addAttribute(SPECIALIZED).setRequired(true);
    builder.addElement(RecurrenceExceptionEntryLink.KEY).setRequired(true);
  }

  /**
   * Default mutable constructor.
   */
  public RecurrenceException() {
    this(KEY);
  }

  /**
   * Create an instance using a different key.
   */
  public RecurrenceException(ElementKey<Void,
      ? extends RecurrenceException> key) {
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
  public RecurrenceException(ElementKey<Void,
      ? extends RecurrenceException> key, Element source) {
    super(key, source);
  }

   @Override
   public RecurrenceException lock() {
     return (RecurrenceException) super.lock();
   }

  /**
   * Returns the nested entry providing the details about the exception.
   *
   * @return nested entry providing the details about the exception
   */
  public RecurrenceExceptionEntryLink getEntryLink() {
    return super.getElement(RecurrenceExceptionEntryLink.KEY);
  }

  /**
   * Sets the nested entry providing the details about the exception.
   *
   * @param entryLink nested entry providing the details about the exception or
   *     <code>null</code> to reset
   */
  public RecurrenceException setEntryLink(RecurrenceExceptionEntryLink
      entryLink) {
    super.setElement(RecurrenceExceptionEntryLink.KEY, entryLink);
    return this;
  }

  /**
   * Returns whether it has the nested entry providing the details about the
   * exception.
   *
   * @return whether it has the nested entry providing the details about the
   *     exception
   */
  public boolean hasEntryLink() {
    return super.hasElement(RecurrenceExceptionEntryLink.KEY);
  }

  /**
   * Returns the whether the exception is specialized.
   *
   * @return whether the exception is specialized
   */
  public Boolean getSpecialized() {
    return super.getAttributeValue(SPECIALIZED);
  }

  /**
   * Sets the whether the exception is specialized.
   *
   * @param specialized whether the exception is specialized or
   *     <code>null</code> to reset
   */
  public RecurrenceException setSpecialized(Boolean specialized) {
    super.setAttributeValue(SPECIALIZED, specialized);
    return this;
  }

  /**
   * Returns whether it has the whether the exception is specialized.
   *
   * @return whether it has the whether the exception is specialized
   */
  public boolean hasSpecialized() {
    return getSpecialized() != null;
  }

  @Override
  public String toString() {
    return "{RecurrenceException specialized=" + getAttributeValue(SPECIALIZED)
        + "}";
  }

}
