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


package com.google.gdata.client.youtube;

import com.google.gdata.client.Query;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A helper class that helps building queries for the
 * YouTube feeds.
 *
 * Not all feeds implement all parameters defined on
 * this class. See the documentation to get the list
 * of parameters each feed supports.
 *
 * 
 */
public class YouTubeQuery extends Query {

  private static final String VQ = "vq";
  private static final String TIME = "time";
  private static final String FORMAT = "format";
  private static final String ORDERBY = "orderby";
  private static final String RACY = "racy";
  private static final String RACY_INCLUDE = "include";
  private static final String RACY_EXCLUDE = "exclude";

  /**
   * Standard values for the {@code time} parameter.
   */
  public static enum Time {
    TODAY("today"),
    THIS_WEEK("this_week"),
    THIS_MONTH("this_month"),
    ALL_TIME("all_time");

    private final String value;

    private Time(String value) {
      this.value = value;
    }

    /** Returns the corresponding parameter value. */
    public String toParameterValue() {
      return value;
    }

    public static Time fromParameterValue(String value) {
      if (value == null) {
        return null;
      }
      Time time = PARAMETER_TO_TIME.get(value);
      if (time == null) {
        throw new IllegalStateException("Cannot convert time value: " + value);
      }
      return time;
    }

    private static Map<String, Time> PARAMETER_TO_TIME;
    static {
      Map<String, Time> map = new HashMap<String, Time>();
      for (Time time : Time.values()) {
        map.put(time.toParameterValue(), time);
      }
      PARAMETER_TO_TIME = Collections.unmodifiableMap(map);
    }
  }

  /**
   * Standard values for the {@code orderby} parameter.
   */
  public static enum OrderBy {
    RELEVANCE("relevance"),
    UPDATED("updated"),
    VIEW_COUNT("viewCount"),
    RATING("rating");

    private final String value;

    private OrderBy(String value) {
      this.value = value;
    }

    /** Returns the corresponding parameter value. */
    public String toParameterValue() {
      return value;
    }

    public static OrderBy fromParameterValue(String value) {
      if (value == null) {
        return null;
      }
      OrderBy orderBy = PARAMETER_TO_ORDERBY.get(value);
      if (orderBy == null) {
        throw new IllegalStateException("Cannot convert orderBy value: "
            + value);
      }
      return orderBy;
    }

    private static Map<String, OrderBy> PARAMETER_TO_ORDERBY;
    static {
      Map<String, OrderBy> map = new HashMap<String, OrderBy>();
      for (OrderBy orderBy : OrderBy.values()) {
        map.put(orderBy.toParameterValue(), orderBy);
      }
      PARAMETER_TO_ORDERBY = Collections.unmodifiableMap(map);
    }
  }


  /**
   * Constructs a new YouTubeQuery object that targets a feed.  The initial
   * state of the query contains no parameters, meaning all entries
   * in the feed would be returned if the query was executed immediately
   * after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be
   *   executed.
   */
  public YouTubeQuery(URL feedUrl) {
    super(feedUrl);
  }

  /**
   * Gets the value of the {@code vq} parameter.
   *
   * @return current query string
   */
  public String getVideoQuery() {
    return getCustomParameterValue(VQ);
  }

  /**
   * Sets the value of the {@code vq} parameter.
   *
   * The {@code vq} parameter supports the same query language as
   * the one used on the youtube website. It is a superset of
   * the language supported by the {@code q} parameter.
   *
   * @param query query string, {@code null} to remove the parameter
   */
  public void setVideoQuery(String query) {
    overwriteCustomParameter(VQ, query);
  }

  /**
   * Gets the value of the {@code time} parameter.
   *
   * @return value of the {@code time} parameter
   * @exception IllegalStateException if a time value was found in the
   *   query that cannot be transformed into {@link YouTubeQuery.Time}
   */
  public Time getTime() {
    return Time.fromParameterValue(getCustomParameterValue(TIME));
  }

  /**
   * Sets the value of the {@code time} parameter.
   *
   * @param time time value, {@code null} to remove the parameter
   */
  public void setTime(Time time) {
    overwriteCustomParameter(TIME,
        time == null ? null : time.toParameterValue());
  }

  /**
   * Gets the value of the {@code format} parameter.
   *
   * @return all defined formats, might be empty but not null
   * @throws NumberFormatException if the current value is
   *   invalid.
   */
  public Set<Integer> getFormats() {
    String value = getCustomParameterValue(FORMAT);
    if (value == null) {
      return Collections.emptySet();
    }

    Set<Integer> retval = new LinkedHashSet<Integer>();

    String[] formats = value.trim().split(" *, *");
    for (String format : formats) {
      retval.add(new Integer(format));
    }
    return retval;
  }

  /**
   * Sets the value of the {@code format} parameter.
   *
   * See the documentation for a description of the
   * different formats that are be available.
   *
   * @param formats integer id of all the formats you are
   *   interested in. Videos will be returned if and only
   *   if they have downloadable content for at least one
   *   of these formats. No formats removes the parameter.
   */
  public void setFormats(int... formats) {
    Set<Integer> formatSet = new LinkedHashSet<Integer>();
    for (int format : formats) {
      formatSet.add(format);
    }
    setFormats(formatSet);
  }

  /**
   * Sets the value of the {@code format} parameter.
   *
   * See the documentation for a description of the
   * different formats that are be available.
   *
   * @param formats integer id of all the formats you are interested
   *   in. Videos will be returned if and only if they have
   *   downloadable content for at least one of these formats. {@code
   *   null} or an empty set removes the parameter
   */
  public void setFormats(Set<Integer> formats) {
    if (formats == null || formats.isEmpty()) {
      overwriteCustomParameter(FORMAT, null);
      return;
    }

    StringBuilder stringValue = new StringBuilder();
    boolean isFirst = true;
    for (int format : formats) {
      if (isFirst) {
        isFirst = false;
      } else {
        stringValue.append(',');
      }
      stringValue.append(format);
    }
    overwriteCustomParameter(FORMAT, stringValue.toString());
  }

  /**
   * Gets the value of the {@code orderby} parameter.
   *
   * @return value of the {@code orderby} parameter.
   * @exception IllegalStateException if a time value was found in the
   *   query that cannot be transformed into {@link YouTubeQuery.OrderBy}
   */
  public OrderBy getOrderby() {
    return OrderBy.fromParameterValue(getCustomParameterValue(ORDERBY));
  }

  /**
   * Sets the value of the {@code orderby} parameter.
   *
   * @param orderBy value of the {@code orderby} parameter, 
   *   {@code null} to remove the parameter
   */
  public void setOrderBy(OrderBy orderBy) {
    overwriteCustomParameter(ORDERBY,
        orderBy == null ? null : orderBy.toParameterValue());
  }

  /**
   * Gest the value of the {@code racy} parameter.
   *
   * @return true if the {@code racy=include}
   */
  public boolean getIncludeRacy() {
    return RACY_INCLUDE.equals(getCustomParameterValue(RACY));
  }

  /**
   * Sets the value of the {@code racy} parameter.
   *
   * @param includeRacy {@code true} to include racy content, false
   *   to exclude it, {@code null} to remove the parameter
   */
  public void setIncludeRacy(Boolean includeRacy) {
    String stringValue;
    if (includeRacy == null) {
      stringValue = null;
    } else {
      stringValue = includeRacy ? RACY_INCLUDE : RACY_EXCLUDE;
    }

    overwriteCustomParameter(RACY, stringValue);
  }

  void overwriteCustomParameter(String name, String value) {
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

  String getCustomParameterValue(String parameterName) {
    List<CustomParameter> customParams = getCustomParameters(parameterName);
    if (customParams.isEmpty()) {
      return null;
    }
    return customParams.get(0).getValue();
  }
}
