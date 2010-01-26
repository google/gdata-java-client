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
 * Describes money.
 *
 * 
 */
public class Money extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Money> KEY = ElementKey.of(new QName(Namespaces.gNs, "money"), Void.class,
      Money.class);

  /**
   * Amount.
   */
  public static final AttributeKey<Double> AMOUNT = AttributeKey.of(new
      QName(null, "amount"), Double.class);

  /**
   * ISO4217 currency code.
   */
  public static final AttributeKey<String> CURRENCY_CODE = AttributeKey.of(new
      QName(null, "currencyCode"), String.class);

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
    builder.addAttribute(AMOUNT).setRequired(true);
    builder.addAttribute(CURRENCY_CODE).setRequired(true);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Money() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Money(ElementKey<?, ? extends Money> key) {
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
  protected Money(ElementKey<?, ? extends Money> key, Element source) {
    super(key, source);
  }

  @Override
  public Money lock() {
    return (Money) super.lock();
  }

  /**
   * Returns the amount.
   *
   * @return amount
   */
  public Double getAmount() {
    return super.getAttributeValue(AMOUNT);
  }

  /**
   * Sets the amount.
   *
   * @param amount amount or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Money setAmount(Double amount) {
    super.setAttributeValue(AMOUNT, amount);
    return this;
  }

  /**
   * Returns whether it has the amount.
   *
   * @return whether it has the amount
   */
  public boolean hasAmount() {
    return super.hasAttribute(AMOUNT);
  }

  /**
   * Returns the ISO4217 currency code.
   *
   * @return ISO4217 currency code
   */
  public String getCurrencyCode() {
    return super.getAttributeValue(CURRENCY_CODE);
  }

  /**
   * Sets the ISO4217 currency code.
   *
   * @param currencyCode ISO4217 currency code or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Money setCurrencyCode(String currencyCode) {
    super.setAttributeValue(CURRENCY_CODE, currencyCode);
    return this;
  }

  /**
   * Returns whether it has the ISO4217 currency code.
   *
   * @return whether it has the ISO4217 currency code
   */
  public boolean hasCurrencyCode() {
    return super.hasAttribute(CURRENCY_CODE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Money other = (Money) obj;
    return eq(getAmount(), other.getAmount())
        && eq(getCurrencyCode(), other.getCurrencyCode());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getAmount() != null) {
      result = 37 * result + getAmount().hashCode();
    }
    if (getCurrencyCode() != null) {
      result = 37 * result + getCurrencyCode().hashCode();
    }
    return result;
  }
}


