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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.EnumConstruct;
import java.util.HashSet;

/**
 * GData schema extension describing a node with a crawl rate. The crawl rate
 * is a value in {@code slowest, slower, normal, faster, fastest}. 
 * This class is abstract, subclasses must define a default constructor 
 * which has the node name hardcoded. See {@link EnumConstruct}.
 *
 * 
 */
public abstract class CrawlRateConstruct extends EnumConstruct {
  
  /**
   * Constructs {@link EnumConstruct} to represent the crawl rate setting.
   */
  public CrawlRateConstruct(String nodeName) {
    super(Namespaces.WT_NAMESPACE, nodeName, null, getValues());
    setCrawlRate(CrawlRate.getDefault());
  }

  /**
   * Creates the set of accepted strings.
   * 
   * @return A HashSet with the preferred strings.
   */
  private static HashSet<String> getValues() {
    HashSet<String> values = new HashSet<String>();
    for (CrawlRate rate : CrawlRate.values()) {
      values.add(rate.toString());
    }
    return values;
  }
  
  /**
   * Compares {@link DomainPreferenceConstruct} objects based on the 
   * domain preference that they they hold.
   */
  @Override
  public boolean equals(Object rhs) {
    if (!super.equals(rhs)) {
      return false;
    }

    return getValue().equals(((CrawlRateConstruct) rhs).getValue());
  }

  /** 
   * Returns a hash code which is based on the crawl rate string.
   */
  @Override
  public int hashCode() {
    return getValue().hashCode();
  }

  /**
   * Override {@link EnumConstruct#setValue(String)} to validate that
   * the supplied value is a one of the accepted crawl rates.
   * See {@link CrawlRate}
   * 
   * @throws NullPointerException if the value is null.
   * @throws IllegalArgumentException if value is not a valid crawl rate.
   */
  @Override
  public void setValue(String value) 
      throws NullPointerException, IllegalArgumentException{
    if (value == null) {
      throw new NullPointerException("value cannot be null");
    }
    
    if (!values.contains(value)) {
      throw new IllegalArgumentException(
          "The argument is not a valid Crawl Rate");
    }

    super.setValue(value);
  }

  public void setCrawlRate(CrawlRate value) {
    super.setValue(value.toString());
  }

  public CrawlRate getCrawlRate() {
    return CrawlRate.fromString(getValue());
  } 
}
