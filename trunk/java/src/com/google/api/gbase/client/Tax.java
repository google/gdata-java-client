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

package com.google.api.gbase.client;

import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Collections;

/**
 * Tax information
 *
 * 
 *
 */
public class Tax {
  /** Destination country, optional.*/
  private final String country;
  /** The region, optional and repeatable. */
  private final Collection<String> regions;  
  /** The tax rate in percentage, always set. */
  private final float rate;
  /** A boolean indicating whether the shipping is taxed. Optional. Default is false. */
  private final Boolean taxShip;

  /**
   * Creates a tax object.
   * 
   * @param country destination country (ISO 3312 2-letter code), can be null
   * @param regions A list of shipping regions within the destination country, can be null. 
   * @param rate the tax percentage rate. For example, for 6% tax rate, rate should be set to 6.
   * @param taxShip A boolean indicating whether the shipping is taxed, can be null.
   */
  public Tax(String country, Collection<String> regions, float rate, Boolean taxShip) {
    this.country = country;
    if (regions != null) {
      this.regions = ImmutableList.copyOf(regions);
    } else {
      this.regions = Collections.<String>emptySet();
    }
    this.rate = rate;
    this.taxShip = taxShip;
  }
  
  @Override
  public String toString() {
    return "Tax(country=" + country +", regions=" + regions + ", rate=" + rate 
        + ", taxShip= " + taxShip;
  }
  
  /** Gets the tax rate. */
  public float getRate() {
    return rate;
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

  /** Gets the regions. This method never returns null, but the returned list can be empty. */
  public Collection<String> getRegions() {
    return regions;
  }
  
  /** Gets the taxShip, or null. */
  public Boolean getTaxShip() {
    return taxShip;
  }
}
