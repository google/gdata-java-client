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

import com.google.gdata.client.Query;

import java.util.List;
import java.net.URL;

/**
 * Adds support for the "bq" and "max-values" parameters to a gdata query.
 * {@link com.google.gdata.client.Query}.
 */
public class GoogleBaseQuery extends Query {
  private static final String BQ_PARAMETER = "bq";
  private static final String MAX_VALUES_PARAMETER = "max-values";
  private static final String ORDER_BY = "orderby";
  private static final String CROWD_BY = "crowdby";
  private static final String SORT_ORDER = "sortorder";
  private static final String CONTENT = "content";
  private static final String REFINE = "refine";

  /**
   * Creates a Google Base query.
   *
   * @param feedUrl the URL to connect to
   */
  public GoogleBaseQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Sets the Google Base query to execute.
   *
   * When running queries on Google Base, you have a choice
   * between a Google Base query, which will be interpreted
   * and executed strictly following the Google Base query
   * language, and a Fulltext query (see {@link #setFullTextQuery(String)})
   * which will be interpreted just like a query typed in the
   * search box on the Google Base website - including some
   * user-friendly preprocessing that might be useful or confusing
   * depending on what you're trying to do.
   *
   * You cannot specify both a Google Base query and a Fulltext query.
   *
   * @param query google base query
   * @exception IllegalStateException if a fulltext query
   *   is already set.
   */
  public void setGoogleBaseQuery(String query) {
    overwriteCustomParameter(BQ_PARAMETER, query);
  }

  /**
   * Gets the Google Base query that will be executed.
   *
   * @return the query or null if none was set
   */
  public String getGoogleBaseQuery() {
    String parameterName = BQ_PARAMETER;
    return getCustomParameterValue(parameterName);
  }

  private String getCustomParameterValue(String parameterName) {
    List<CustomParameter> customParams = getCustomParameters(parameterName);
    if (customParams.isEmpty()) {
      return null;
    }
    return customParams.get(0).getValue();
  }

  private void overwriteCustomParameter(String name, String value) {
    List<CustomParameter> customParams = getCustomParameters();

    // Remove any existing value.
    for (CustomParameter existingValue : getCustomParameters(name)) {
      customParams.remove(existingValue);
    }

    // Add the specified value.
    if (value != null) {
      customParams.add(new CustomParameter(name, value));
    }
  }

  /**
   * Sets the <code>max-values</code> parameter used by histogram feeds
   * to limit the number of unique values to return for one attribute.
   *
   * @param maxValues
   */
  public void setMaxValues(Integer maxValues) {
    String stringValue = maxValues == null ? null:maxValues.toString();
    overwriteCustomParameter(MAX_VALUES_PARAMETER, stringValue);
  }

  /**
   * Gets the current value for the <code>max-values</code> parameter.
   *
   * @return current value or null
   */
  public Integer getMaxValues() {
    String valueAsString = getCustomParameterValue(MAX_VALUES_PARAMETER);
    if (valueAsString == null) {
      return null;
    } else {
      return new Integer(valueAsString);
    }
  }

  /**
   * Enables query refinement.
   *
   * By default results are not refined.
   *
   * @param refine true to enable query refinment
   */
  public void setRefine(boolean refine) {
    overwriteCustomParameter(REFINE, refine ? "true" : "false");
  }

  /**
   * Checks whether the query should be refined.
   *
   * @return true if the query should be refined
   */
  public boolean getRefine() {
    return "true".equals(getCustomParameterValue(REFINE));
  }

  /**
   * Selects what to return, on the customer feed.
   *
   * @param content what should be returned (see documentation)
   */
  public void setContent(String content) {
    overwriteCustomParameter(CONTENT, content);
  }

  /**
   * Gets what will be returned, on the customer feed.
   *
   * @return what will be returned
   */
  public String getContent() {
    return getCustomParameterValue(CONTENT);
  }


  /**
   * Sets the criteria used to sort the results.
   *
   * By default results are ordered by relevancy.
   *
   * @param orderBy sorting criteria (see documentation)
   */
  public void setOrderBy(String orderBy) {
    overwriteCustomParameter(ORDER_BY, orderBy);
  }

  /**
   * Sets the criteria used for crowding the results in groups.
   *
   * @param crowdBy crowding criteria (see documentation)
   */
  public void setCrowdingBy(String crowdBy) {
    overwriteCustomParameter(CROWD_BY, crowdBy);
  }
  
  /**
   * Gets the criteria that will be used to sort the results.
   *
   * @return criteria or null if no order has been set
   */
  public String getOrderBy() {
    return getCustomParameterValue(ORDER_BY);
  }

  /**
   * Gets the criteria that will be used to crowd the results.
   *
   * @return criteria or null if no crowding has been set
   */
  public String getCrowdBy() {
    return getCustomParameterValue(CROWD_BY);
  }
  
  /**
   * Changes the order in which results are returned (ascending
   * or descending).
   *
   * Descending order (from the largest value
   * to the smallest value) is the default.
   *
   * @param ascending if true, return the result in ascending
   *   order
   */
  public void setAscendingOrder(boolean ascending) {
    overwriteCustomParameter(SORT_ORDER,
        ascending ? "ascending" : "descending");
  }

  /**
   * Checks whether the results will be returned in ascending
   * order.
   *
   * @return if true, the results will be returned in ascending order,
   *   otherwise they will be returned in descending order
   */
  public boolean isAscendingOrder() {
    return "ascending".equals(getCustomParameterValue(SORT_ORDER));
  }
}
