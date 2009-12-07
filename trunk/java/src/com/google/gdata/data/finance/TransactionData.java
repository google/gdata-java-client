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
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;

/**
 * Data for the transction.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = FinanceNamespace.GF_ALIAS,
    nsUri = FinanceNamespace.GF,
    localName = TransactionData.XML_NAME)
public class TransactionData extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "transactionData";

  /** XML "date" attribute name */
  private static final String DATE = "date";

  /** XML "notes" attribute name */
  private static final String NOTES = "notes";

  /** XML "shares" attribute name */
  private static final String SHARES = "shares";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** Date at which the transaction occurred */
  private DateTime date = null;

  /** Notes */
  private String notes = null;

  /** Number of shares involved in the transaction */
  private Double shares = null;

  /** Type for the transaction (can be "Buy", "Sell", "Buy to Cover" or "Sell
   * Short") */
  private String type = null;

  /**
   * Default mutable constructor.
   */
  public TransactionData() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param date date at which the transaction occurred.
   * @param notes notes.
   * @param shares number of shares involved in the transaction.
   * @param type type for the transaction (can be "Buy", "Sell", "Buy to Cover"
   *     or "Sell Short").
   */
  public TransactionData(DateTime date, String notes, Double shares,
      String type) {
    super();
    setDate(date);
    setNotes(notes);
    setShares(shares);
    setType(type);
    setImmutable(true);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(TransactionData.class)) {
      return;
    }
    extProfile.declare(TransactionData.class, Commission.class);
    new Commission().declareExtensions(extProfile);
    extProfile.declare(TransactionData.class, Price.class);
    new Price().declareExtensions(extProfile);
  }

  /**
   * Returns the commission for the transaction.
   *
   * @return commission for the transaction
   */
  public Commission getCommission() {
    return getExtension(Commission.class);
  }

  /**
   * Sets the commission for the transaction.
   *
   * @param commission commission for the transaction or <code>null</code> to
   *     reset
   */
  public void setCommission(Commission commission) {
    if (commission == null) {
      removeExtension(Commission.class);
    } else {
      setExtension(commission);
    }
  }

  /**
   * Returns whether it has the commission for the transaction.
   *
   * @return whether it has the commission for the transaction
   */
  public boolean hasCommission() {
    return hasExtension(Commission.class);
  }

  /**
   * Returns the date at which the transaction occurred.
   *
   * @return date at which the transaction occurred
   */
  public DateTime getDate() {
    return date;
  }

  /**
   * Sets the date at which the transaction occurred.
   *
   * @param date date at which the transaction occurred or <code>null</code> to
   *     reset
   */
  public void setDate(DateTime date) {
    throwExceptionIfImmutable();
    this.date = date;
  }

  /**
   * Returns whether it has the date at which the transaction occurred.
   *
   * @return whether it has the date at which the transaction occurred
   */
  public boolean hasDate() {
    return getDate() != null;
  }

  /**
   * Returns the notes.
   *
   * @return notes
   */
  public String getNotes() {
    return notes;
  }

  /**
   * Sets the notes.
   *
   * @param notes notes or <code>null</code> to reset
   */
  public void setNotes(String notes) {
    throwExceptionIfImmutable();
    this.notes = notes;
  }

  /**
   * Returns whether it has the notes.
   *
   * @return whether it has the notes
   */
  public boolean hasNotes() {
    return getNotes() != null;
  }

  /**
   * Returns the price of the transaction.
   *
   * @return price of the transaction
   */
  public Price getPrice() {
    return getExtension(Price.class);
  }

  /**
   * Sets the price of the transaction.
   *
   * @param price price of the transaction or <code>null</code> to reset
   */
  public void setPrice(Price price) {
    if (price == null) {
      removeExtension(Price.class);
    } else {
      setExtension(price);
    }
  }

  /**
   * Returns whether it has the price of the transaction.
   *
   * @return whether it has the price of the transaction
   */
  public boolean hasPrice() {
    return hasExtension(Price.class);
  }

  /**
   * Returns the number of shares involved in the transaction.
   *
   * @return number of shares involved in the transaction
   */
  public Double getShares() {
    return shares;
  }

  /**
   * Sets the number of shares involved in the transaction.
   *
   * @param shares number of shares involved in the transaction or
   *     <code>null</code> to reset
   */
  public void setShares(Double shares) {
    throwExceptionIfImmutable();
    this.shares = shares;
  }

  /**
   * Returns whether it has the number of shares involved in the transaction.
   *
   * @return whether it has the number of shares involved in the transaction
   */
  public boolean hasShares() {
    return getShares() != null;
  }

  /**
   * Returns the type for the transaction (can be "Buy", "Sell", "Buy to Cover"
   * or "Sell Short").
   *
   * @return type for the transaction (can be "Buy", "Sell", "Buy to Cover" or
   *     "Sell Short")
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type for the transaction (can be "Buy", "Sell", "Buy to Cover" or
   * "Sell Short").
   *
   * @param type type for the transaction (can be "Buy", "Sell", "Buy to Cover"
   *     or "Sell Short") or <code>null</code> to reset
   */
  public void setType(String type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the type for the transaction (can be "Buy", "Sell",
   * "Buy to Cover" or "Sell Short").
   *
   * @return whether it has the type for the transaction (can be "Buy", "Sell",
   *     "Buy to Cover" or "Sell Short")
   */
  public boolean hasType() {
    return getType() != null;
  }

  @Override
  protected void validate() {
    if (type == null) {
      throwExceptionForMissingAttribute(TYPE);
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
        ExtensionDescription.getDefaultDescription(TransactionData.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(DATE, date);
    generator.put(NOTES, notes);
    generator.put(SHARES, shares);
    generator.put(TYPE, type);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    date = helper.consumeDateTime(DATE, false);
    notes = helper.consume(NOTES, false);
    shares = helper.consumeDouble(SHARES, false);
    type = helper.consume(TYPE, true);
  }

  @Override
  public String toString() {
    return "{TransactionData date=" + date + " notes=" + notes + " shares=" +
        shares + " type=" + type + "}";
  }

}

