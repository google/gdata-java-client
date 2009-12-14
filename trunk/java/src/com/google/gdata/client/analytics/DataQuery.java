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


package com.google.gdata.client.analytics;

import com.google.gdata.client.Query;

import java.net.URL;

/**
 * Describes a query for the arbitrary data from an Analytics profile.
 *
 * 
 */
public class DataQuery extends Query {

  /** Comma separated list of row keys. */
  private String dimensions;

  /** Last day for which to retrieve data in form YYYY-MM-DD. */
  private String endDate;

  /** Dimension and metric filters. */
  private String filters;

  /** Google Analytics profile ID, prefixed by 'ga:'. */
  private String ids;

  /** Comma separated list of numeric value fields. */
  private String metrics;

  /** Segment to be applied. */
  private String segment;

  /** Comma separated list of sort parameters in order of importance. */
  private String sort;

  /** First day for which to retrieve data in form YYYY-MM-DD. */
  private String startDate;

  /**
   * Constructs a new query object that targets a feed.  The initial state of
   * the query contains no parameters, meaning all entries in the feed would be
   * returned if the query was executed immediately after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be executed.
   */
  public DataQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Returns the comma separated list of row keys.
   *
   * @return comma separated list of row keys or <code>null</code> to indicate
   *     that the parameter is not set.
   */
  public String getDimensions() {
    return dimensions;
  }

  /**
   * Sets the comma separated list of row keys.
   *
   * @param dimensions comma separated list of row keys or <code>null</code> to
   *     remove this parameter if set.
   */
  public void setDimensions(String dimensions) {
    // check if setting to existing value
    if (this.dimensions == null ? dimensions != null :
        !this.dimensions.equals(dimensions)) {
      // set to new value for customer parameter
      this.dimensions = dimensions;
      setStringCustomParameter("dimensions", dimensions);
    }
  }

  /**
   * Returns the last day for which to retrieve data in form YYYY-MM-DD.
   *
   * @return last day for which to retrieve data in form YYYY-MM-DD or
   *     <code>null</code> to indicate that the parameter is not set.
   */
  public String getEndDate() {
    return endDate;
  }

  /**
   * Sets the last day for which to retrieve data in form YYYY-MM-DD.
   *
   * @param endDate last day for which to retrieve data in form YYYY-MM-DD or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setEndDate(String endDate) {
    // check if setting to existing value
    if (this.endDate == null ? endDate != null : !this.endDate.equals(endDate))
        {
      // set to new value for customer parameter
      this.endDate = endDate;
      setStringCustomParameter("end-date", endDate);
    }
  }

  /**
   * Returns the dimension and metric filters.
   *
   * @return dimension and metric filters or <code>null</code> to indicate that
   *     the parameter is not set.
   */
  public String getFilters() {
    return filters;
  }

  /**
   * Sets the dimension and metric filters.
   *
   * @param filters dimension and metric filters or <code>null</code> to remove
   *     this parameter if set.
   */
  public void setFilters(String filters) {
    // check if setting to existing value
    if (this.filters == null ? filters != null : !this.filters.equals(filters))
        {
      // set to new value for customer parameter
      this.filters = filters;
      setStringCustomParameter("filters", filters);
    }
  }

  /**
   * Returns the Google Analytics profile ID, prefixed by 'ga:'.
   *
   * @return Google Analytics profile ID, prefixed by 'ga:' or <code>null</code>
   *     to indicate that the parameter is not set.
   */
  public String getIds() {
    return ids;
  }

  /**
   * Sets the Google Analytics profile ID, prefixed by 'ga:'.
   *
   * @param ids Google Analytics profile ID, prefixed by 'ga:' or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setIds(String ids) {
    // check if setting to existing value
    if (this.ids == null ? ids != null : !this.ids.equals(ids)) {
      // set to new value for customer parameter
      this.ids = ids;
      setStringCustomParameter("ids", ids);
    }
  }

  /**
   * Returns the comma separated list of numeric value fields.
   *
   * @return comma separated list of numeric value fields or <code>null</code>
   *     to indicate that the parameter is not set.
   */
  public String getMetrics() {
    return metrics;
  }

  /**
   * Sets the comma separated list of numeric value fields.
   *
   * @param metrics comma separated list of numeric value fields or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setMetrics(String metrics) {
    // check if setting to existing value
    if (this.metrics == null ? metrics != null : !this.metrics.equals(metrics))
        {
      // set to new value for customer parameter
      this.metrics = metrics;
      setStringCustomParameter("metrics", metrics);
    }
  }

  /**
   * Returns the segment to be applied.
   *
   * @return segment to be applied or <code>null</code> to indicate that the
   *     parameter is not set.
   */
  public String getSegment() {
    return segment;
  }

  /**
   * Sets the segment to be applied.
   *
   * @param segment segment to be applied or <code>null</code> to remove this
   *     parameter if set.
   */
  public void setSegment(String segment) {
    // check if setting to existing value
    if (this.segment == null ? segment != null : !this.segment.equals(segment))
        {
      // set to new value for customer parameter
      this.segment = segment;
      setStringCustomParameter("segment", segment);
    }
  }

  /**
   * Returns the comma separated list of sort parameters in order of importance.
   *
   * @return comma separated list of sort parameters in order of importance or
   *     <code>null</code> to indicate that the parameter is not set.
   */
  public String getSort() {
    return sort;
  }

  /**
   * Sets the comma separated list of sort parameters in order of importance.
   *
   * @param sort comma separated list of sort parameters in order of importance
   *     or <code>null</code> to remove this parameter if set.
   */
  public void setSort(String sort) {
    // check if setting to existing value
    if (this.sort == null ? sort != null : !this.sort.equals(sort)) {
      // set to new value for customer parameter
      this.sort = sort;
      setStringCustomParameter("sort", sort);
    }
  }

  /**
   * Returns the first day for which to retrieve data in form YYYY-MM-DD.
   *
   * @return first day for which to retrieve data in form YYYY-MM-DD or
   *     <code>null</code> to indicate that the parameter is not set.
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * Sets the first day for which to retrieve data in form YYYY-MM-DD.
   *
   * @param startDate first day for which to retrieve data in form YYYY-MM-DD or
   *     <code>null</code> to remove this parameter if set.
   */
  public void setStartDate(String startDate) {
    // check if setting to existing value
    if (this.startDate == null ? startDate != null :
        !this.startDate.equals(startDate)) {
      // set to new value for customer parameter
      this.startDate = startDate;
      setStringCustomParameter("start-date", startDate);
    }
  }

}

