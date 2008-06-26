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

// Author: Steven Soneff (ssoneff@google.com)

package com.google.gdata.client.finance;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.extensions.Money;
import com.google.gdata.data.finance.Commission;
import com.google.gdata.data.finance.PortfolioData;
import com.google.gdata.data.finance.PortfolioEntry;
import com.google.gdata.data.finance.Price;
import com.google.gdata.data.finance.TransactionData;
import com.google.gdata.data.finance.TransactionEntry;

/**
 * FinanceUtilities contains useful methods related to the Finance GData API
 */
public class FinanceUtilities {

  /* FinanceUtilities is only used for its static methods */
  private FinanceUtilities() {}

  /**
   * Creates a portfolio entry object.
   *
   * @param portfolioName the name of the new portfolio. e.g. "Oil & Gas".
   *   This is required when creating a portfolio, but optional when updating.
   * @param currencyCode the ISO4217 currency code for the portfolio.
   *   e.g. USD, EUR, JPY, GBP, CAD, HKD, etc.
   *   This is required when creating a portfolio, but optional when updating.
   * @return the newly created portfolio entry
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  public static PortfolioEntry makePortfolioEntry(String portfolioName,
                                                  String currencyCode) {
    PortfolioEntry entry = new PortfolioEntry();
    if (!portfolioName.equals("")) entry.setTitle(new PlainTextConstruct(portfolioName));
    PortfolioData data = new PortfolioData();
    if (!currencyCode.equals("")) data.setCurrencyCode(currencyCode);
    entry.setPortfolioData(data);
    return entry;
  }

  /**
   * Creates a transaction entry object.
   *
   * @param type The type of transaction (one of "Buy", "Sell", "Sell Short",
   *   "Buy to Cover").  This should be specified.  In this method, the type
   *   will be set to "Buy" if left unspecified.
   * @param date The date of the transaction in the format YYYY-MM-DD.  This is
   *   optional ("watchlist" or "holding" items might not have a date).  If
   *   left unspecified, this parameter should be an empty string.
   * @param shares The number of shares/units involved in a transaction.  This
   *   does not have to be a whole number quantity (e.g. 10.1 is valid).  This
   *   is optional ("watchlist" entries need not have a share count)
   * @param price The transaction unit price (e.g. 99.99).  This is optional.
   * @param commission The transaction commission (e.g. 25.0). This is optional.
   * @param currency The ISO4217 currency code for the transaction price and
   *   commission.  e.g. USD, EUR, JPY, GBP, CAD, HKD, etc.  This is required
   *   only if price or commission have been specified.
   * @param notes Any notes related to the transaction.  This is optional.
   * @return the newly created transaction entry
   * @throws IOException If there is a problem communicating with the server.
   * @throws MalformedURLException If the URL is invalid.
   * @throws ServiceException If the service is unable to handle the request.
   */
  public static TransactionEntry makeTransactionEntry(String type,
                                                      String date,
                                                      String shares,
                                                      String price,
                                                      String commission,
                                                      String currency,
                                                      String notes) {
    TransactionEntry entry = new TransactionEntry();
    TransactionData data = new TransactionData();
    if (type.equals("")) type = "Buy";
    data.setType(type);
    if (!date.equals("")) data.setDate(DateTime.parseDateTime(date + "T00:00:00.000Z"));
    if (!shares.equals("")) data.setShares(Double.valueOf(shares).doubleValue());
    if (!price.equals("")) {
      Price p = new Price();
      p.addMoney(new Money(Double.valueOf(price).doubleValue(), currency));
      data.setPrice(p);
    }
    if (!commission.equals("")) {
      Commission c = new Commission();
      c.addMoney(new Money(Double.valueOf(commission).doubleValue(), currency));
      data.setCommission(c);
    }
    if (!notes.equals("")) data.setNotes(notes);
    entry.setTransactionData(data);
    return entry;
  }
}
