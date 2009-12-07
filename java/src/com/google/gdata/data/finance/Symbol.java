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


package com.google.gdata.data.finance;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Stock symbol for the company.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = FinanceNamespace.GF_ALIAS,
    nsUri = FinanceNamespace.GF,
    localName = Symbol.XML_NAME)
public class Symbol extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "symbol";

  /** XML "exchange" attribute name */
  private static final String EXCHANGE = "exchange";

  /** XML "fullName" attribute name */
  private static final String FULLNAME = "fullName";

  /** XML "symbol" attribute name */
  private static final String SYMBOL = "symbol";

  /** Exchange symbol for the company or mutual fund */
  private String exchange = null;

  /** Full name for the company or mutual fund */
  private String fullName = null;

  /** Stock symbol for the company or mutual fund */
  private String symbol = null;

  /**
   * Default mutable constructor.
   */
  public Symbol() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param exchange exchange symbol for the company or mutual fund.
   * @param fullName full name for the company or mutual fund.
   * @param symbol stock symbol for the company or mutual fund.
   */
  public Symbol(String exchange, String fullName, String symbol) {
    super();
    setExchange(exchange);
    setFullName(fullName);
    setSymbol(symbol);
    setImmutable(true);
  }

  /**
   * Returns the exchange symbol for the company or mutual fund.
   *
   * @return exchange symbol for the company or mutual fund
   */
  public String getExchange() {
    return exchange;
  }

  /**
   * Sets the exchange symbol for the company or mutual fund.
   *
   * @param exchange exchange symbol for the company or mutual fund or
   *     <code>null</code> to reset
   */
  public void setExchange(String exchange) {
    throwExceptionIfImmutable();
    this.exchange = exchange;
  }

  /**
   * Returns whether it has the exchange symbol for the company or mutual fund.
   *
   * @return whether it has the exchange symbol for the company or mutual fund
   */
  public boolean hasExchange() {
    return getExchange() != null;
  }

  /**
   * Returns the full name for the company or mutual fund.
   *
   * @return full name for the company or mutual fund
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Sets the full name for the company or mutual fund.
   *
   * @param fullName full name for the company or mutual fund or
   *     <code>null</code> to reset
   */
  public void setFullName(String fullName) {
    throwExceptionIfImmutable();
    this.fullName = fullName;
  }

  /**
   * Returns whether it has the full name for the company or mutual fund.
   *
   * @return whether it has the full name for the company or mutual fund
   */
  public boolean hasFullName() {
    return getFullName() != null;
  }

  /**
   * Returns the stock symbol for the company or mutual fund.
   *
   * @return stock symbol for the company or mutual fund
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Sets the stock symbol for the company or mutual fund.
   *
   * @param symbol stock symbol for the company or mutual fund or
   *     <code>null</code> to reset
   */
  public void setSymbol(String symbol) {
    throwExceptionIfImmutable();
    this.symbol = symbol;
  }

  /**
   * Returns whether it has the stock symbol for the company or mutual fund.
   *
   * @return whether it has the stock symbol for the company or mutual fund
   */
  public boolean hasSymbol() {
    return getSymbol() != null;
  }

  @Override
  protected void validate() {
    if (exchange == null) {
      throwExceptionForMissingAttribute(EXCHANGE);
    }
    if (fullName == null) {
      throwExceptionForMissingAttribute(FULLNAME);
    }
    if (symbol == null) {
      throwExceptionForMissingAttribute(SYMBOL);
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
        ExtensionDescription.getDefaultDescription(Symbol.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(EXCHANGE, exchange);
    generator.put(FULLNAME, fullName);
    generator.put(SYMBOL, symbol);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    exchange = helper.consume(EXCHANGE, true);
    fullName = helper.consume(FULLNAME, true);
    symbol = helper.consume(SYMBOL, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Symbol other = (Symbol) obj;
    return eq(exchange, other.exchange)
        && eq(fullName, other.fullName)
        && eq(symbol, other.symbol);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (exchange != null) {
      result = 37 * result + exchange.hashCode();
    }
    if (fullName != null) {
      result = 37 * result + fullName.hashCode();
    }
    if (symbol != null) {
      result = 37 * result + symbol.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Symbol exchange=" + exchange + " fullName=" + fullName + " symbol="
        + symbol + "}";
  }

}

