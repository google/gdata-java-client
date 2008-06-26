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


package com.google.gdata.data.extensions;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * Describes money.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = Money.XML_NAME)
public class Money extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "money";

  /** XML "amount" attribute name */
  private static final String AMOUNT = "amount";

  /** XML "currencyCode" attribute name */
  private static final String CURRENCYCODE = "currencyCode";

  /** Amount */
  private Double amount = null;

  /** ISO4217 currency code */
  private String currencyCode = null;

  /**
   * Default mutable constructor.
   */
  public Money() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param amount amount.
   * @param currencyCode ISO4217 currency code.
   */
  public Money(Double amount, String currencyCode) {
    super();
    setAmount(amount);
    setCurrencyCode(currencyCode);
    setImmutable(true);
  }

  /**
   * Returns the amount.
   *
   * @return amount
   */
  public Double getAmount() {
    return amount;
  }

  /**
   * Sets the amount.
   *
   * @param amount amount or <code>null</code> to reset
   */
  public void setAmount(Double amount) {
    throwExceptionIfImmutable();
    this.amount = amount;
  }

  /**
   * Returns whether it has the amount.
   *
   * @return whether it has the amount
   */
  public boolean hasAmount() {
    return getAmount() != null;
  }

  /**
   * Returns the ISO4217 currency code.
   *
   * @return ISO4217 currency code
   */
  public String getCurrencyCode() {
    return currencyCode;
  }

  /**
   * Sets the ISO4217 currency code.
   *
   * @param currencyCode ISO4217 currency code or <code>null</code> to reset
   */
  public void setCurrencyCode(String currencyCode) {
    throwExceptionIfImmutable();
    this.currencyCode = currencyCode;
  }

  /**
   * Returns whether it has the ISO4217 currency code.
   *
   * @return whether it has the ISO4217 currency code
   */
  public boolean hasCurrencyCode() {
    return getCurrencyCode() != null;
  }

  @Override
  protected void validate() {
    if (amount == null) {
      throwExceptionForMissingAttribute(AMOUNT);
    }
    if (currencyCode == null) {
      throwExceptionForMissingAttribute(CURRENCYCODE);
    }
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(Money.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(AMOUNT, amount);
    generator.put(CURRENCYCODE, currencyCode);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    amount = helper.consumeDouble(AMOUNT, true);
    currencyCode = helper.consume(CURRENCYCODE, true);
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
    return eq(amount, other.amount)
        && eq(currencyCode, other.currencyCode);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (amount != null) {
      result = 37 * result + amount.hashCode();
    }
    if (currencyCode != null) {
      result = 37 * result + currencyCode.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Money amount=" + amount + " currencyCode=" + currencyCode + "}";
  }

}
