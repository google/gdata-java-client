/* Copyright (c) 2006 Google Inc.
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

package com.google.api.gbase.client;

/**
 * Shipping information, defined using destination country,
 * a price (in the item's currency), and optionally a
 * shipping service and comments (notes).
 */
public class Shipping {
  /** Destination country, never null. */
  private final String country;
  /** Shipping service, may be null. */
  private final String service;
  /** Price, always set. */
  private final float price;
  /** Currency, not always set. */
  private final String currency;

  /**
   * Creates a shipping object.
   *
   * @param country destination country (ISO 3312 2-letter code)
   * @param service shipping method
   * @param price price
   * @param currency price currency
   */
  public Shipping(String country, String service, float price,
      String currency) {
    this.country = country;
    this.price = price;
    this.service = service;
    this.currency = currency;
  }

  public String toString() {
    return "Shipping(country=" + country +", service=" + service + ", price= " +
        price + ", currency=" + currency + ")";
  }

  /** Gets the price. */
  public float getPrice() {
    return price;
  }

  /** Gets the price currency, or null. */
  public String getCurrency() {
    return currency;
  }

  /**
   * Gets the destination country, or null.
   *
   * This should be a
   * <a
   * href="http://www.iso.org/iso/en/prods-services/iso3166ma/02iso-3166-code-lists/list-en1.html">
   * ISO 3312 2-letter code.</a>
   */
  public String getCountry() {
    return country;
  }

  /** Gets the shipping service, or null. */
  public String getService() {
    return service;
  }

}
