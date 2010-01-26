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
 * Control ordering for a set of atom entries.
 *
 * 
 */
public class Ordering extends Element {

  /** Order type. */
  public static final class Rel {

    /** ComesAfter ordering. */
    public static final String COMESAFTER = "comesAfter";

    /** First ordering. */
    public static final String FIRST = "first";

    /** Last ordering. */
    public static final String LAST = "last";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      COMESAFTER,
      FIRST,
      LAST};

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
      Ordering> KEY = ElementKey.of(new QName(Namespaces.gNs, "ordering"),
      Void.class, Ordering.class);

  /**
   * Ordered feed id.
   */
  public static final AttributeKey<String> ORDER_ID = AttributeKey.of(new
      QName(null, "orderId"), String.class);

  /**
   * Order type.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * Other entry id.
   */
  public static final AttributeKey<String> REF = AttributeKey.of(new QName(null,
      "ref"), String.class);

  /**
   * Previous element.
   */
  public static final AttributeKey<String> PREV = AttributeKey.of(new
      QName(null, "prev"), String.class);

  /**
   * Next element.
   */
  public static final AttributeKey<String> NEXT = AttributeKey.of(new
      QName(null, "next"), String.class);

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
    builder.addAttribute(ORDER_ID);
    builder.addAttribute(REL);
    builder.addAttribute(REF);
    builder.addAttribute(PREV);
    builder.addAttribute(NEXT);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Ordering() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Ordering(ElementKey<?, ? extends Ordering> key) {
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
  protected Ordering(ElementKey<?, ? extends Ordering> key, Element source) {
    super(key, source);
  }

  @Override
  public Ordering lock() {
    return (Ordering) super.lock();
  }

  /**
   * Returns the next element.
   *
   * @return next element
   */
  public String getNext() {
    return super.getAttributeValue(NEXT);
  }

  /**
   * Sets the next element.
   *
   * @param next next element or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Ordering setNext(String next) {
    super.setAttributeValue(NEXT, next);
    return this;
  }

  /**
   * Returns whether it has the next element.
   *
   * @return whether it has the next element
   */
  public boolean hasNext() {
    return super.hasAttribute(NEXT);
  }

  /**
   * Returns the ordered feed id.
   *
   * @return ordered feed id
   */
  public String getOrderId() {
    return super.getAttributeValue(ORDER_ID);
  }

  /**
   * Sets the ordered feed id.
   *
   * @param orderId ordered feed id or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Ordering setOrderId(String orderId) {
    super.setAttributeValue(ORDER_ID, orderId);
    return this;
  }

  /**
   * Returns whether it has the ordered feed id.
   *
   * @return whether it has the ordered feed id
   */
  public boolean hasOrderId() {
    return super.hasAttribute(ORDER_ID);
  }

  /**
   * Returns the previous element.
   *
   * @return previous element
   */
  public String getPrev() {
    return super.getAttributeValue(PREV);
  }

  /**
   * Sets the previous element.
   *
   * @param prev previous element or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Ordering setPrev(String prev) {
    super.setAttributeValue(PREV, prev);
    return this;
  }

  /**
   * Returns whether it has the previous element.
   *
   * @return whether it has the previous element
   */
  public boolean hasPrev() {
    return super.hasAttribute(PREV);
  }

  /**
   * Returns the other entry id.
   *
   * @return other entry id
   */
  public String getRef() {
    return super.getAttributeValue(REF);
  }

  /**
   * Sets the other entry id.
   *
   * @param ref other entry id or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Ordering setRef(String ref) {
    super.setAttributeValue(REF, ref);
    return this;
  }

  /**
   * Returns whether it has the other entry id.
   *
   * @return whether it has the other entry id
   */
  public boolean hasRef() {
    return super.hasAttribute(REF);
  }

  /**
   * Returns the order type.
   *
   * @return order type
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the order type.
   *
   * @param rel order type or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Ordering setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the order type.
   *
   * @return whether it has the order type
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Ordering other = (Ordering) obj;
    return eq(getNext(), other.getNext())
        && eq(getOrderId(), other.getOrderId())
        && eq(getPrev(), other.getPrev())
        && eq(getRef(), other.getRef())
        && eq(getRel(), other.getRel());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getNext() != null) {
      result = 37 * result + getNext().hashCode();
    }
    if (getOrderId() != null) {
      result = 37 * result + getOrderId().hashCode();
    }
    if (getPrev() != null) {
      result = 37 * result + getPrev().hashCode();
    }
    if (getRef() != null) {
      result = 37 * result + getRef().hashCode();
    }
    if (getRel() != null) {
      result = 37 * result + getRel().hashCode();
    }
    return result;
  }
}


