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


package com.google.gdata.client.calendar;


import com.google.gdata.client.Query;
import com.google.gdata.data.DateTime;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The CalendarQuery class extends the base GData Query class to
 * define convenience APIs for Calendar custom query parameters.
 *
 * 
 * 
 */
public class CalendarQuery extends Query {


  /**
   * The name of the custom query parameter that specifies that all
   * events returned must be greater than or equal to the specified
   * time.
   */
  public static final String MINIMUM_START_TIME = "start-min";


  /**
   * The name of the custom query parameter that specifies that all
   * events returned must be less than the specified time.
   */
  public static final String MAXIMUM_START_TIME = "start-max";

  /**
   * The name of the extended property query parameter that specifies that all
   * events' extended properties must have matching values.
   */
  public static final String EXT_PROP_QUERY = "extq";

  /**
   * An empty array of ExtendedPropertyMatch which will be returned
   * from {@link #getExtendedPropertyQuery()} should the current
   * extended property query be empty.
   */
  private static final ExtendedPropertyMatch[] EMPTY_EXT_PROP_MATCH =
      new ExtendedPropertyMatch[0];

  /**
   * The ExtendedPropertyMatch class corresponds to a single
   * extended property match.
   *
   * @see CalendarQuery#setExtendedPropertyQuery(
   *          CalendarQuery.ExtendedPropertyMatch...)
   * @see CalendarQuery#getExtendedPropertyQuery()
   */
  public static class ExtendedPropertyMatch {

    /** The maximum length of an extended property name. */
    public static final int MAX_EXTENDED_PROPERTY_NAME_LENGTH = 44;

    /**
     * The maximum length of an extended property value (after escaped
     * characters have been converted into plain characters, e.g. \" -> ").
     */
    public static final int MAX_EXTENDED_PROPERTY_VALUE_LENGTH = 1024;

    /**
     * A regex describing the format of extended property names.
     * The name can be a plain string without :'s and ='s.
     */
    public static final String PROPERTY_NAME_REGEX = "[^:=]+";

    /**
     * A regex describing the format of extended property values.
     * The value can be:
     * <ul>
     *   <li>a (possibly empty) plain string without ]'s (e.g. Foo Property), or
     *   <li>a (possibly empty) quoted string with back slashes and quotes
     *   escaped by back slashes (e.g. "\"[Property name]\"").
     * </ul>
     */
    public static final String PROPERTY_VALUE_REGEX =
        "\\\"(?:[^\\\"\\\\]|(?:\\\\\\\\)|(?:\\\\\\\"))*\\\"|[^\\]]*";

    /**
     * Group 1: property name (without :'s and ='s).
     * Group 2: property value (perhaps with surrounding quotes which should be
     * stripped off).
     */
    public static final String SINGLE_EXT_PROP_QUERY_REGEX =
        "\\[(" + PROPERTY_NAME_REGEX + "):" +
              "(" + PROPERTY_VALUE_REGEX + ")\\]";

    /**
     * A pattern that matches exactly one extended property query within
     * a compound extended property query. E.g. when applied to
     * {@code "[foo:bar][baz:"bin"]"},  it will consecutively match:
     * <ol>
     *   <li>[foo:bar] (group1: foo, group2: bar).
     *   <li>[baz:"bin"] (group1: baz, group2: "bin". Please note that the
     *   quotes surrounding 'bin' also belong in the group text and
     *   <em>should be stripped</em> prior to further processing
     *   of the property value).
     * </ol>
     */
    public static final Pattern EXT_PROP_QUERY_PATTERN = Pattern.compile(
        SINGLE_EXT_PROP_QUERY_REGEX);

    private String name;
    private String expr;

    /**
     * @param name extended property name. May contain up to 44 characters and
     *        may not contain ':' or '=' characters.
     * @param value to match against the {@code name} extended
     *        property. May contain up to 1024 characters.
     */
    public ExtendedPropertyMatch(String name, String value) {
      if (name == null) {
        throw new NullPointerException("Property name is null");
      }
      if (value == null) {
        throw new NullPointerException("Property value is null");
      }
      if (name.length() > MAX_EXTENDED_PROPERTY_NAME_LENGTH) {
        throw new IllegalArgumentException(
            "Property name length in characters must not be more than " +
            MAX_EXTENDED_PROPERTY_NAME_LENGTH);
      }
      if (value.length() > MAX_EXTENDED_PROPERTY_VALUE_LENGTH) {
        throw new IllegalArgumentException(
            "Property value length in characters must not be more than " +
            MAX_EXTENDED_PROPERTY_VALUE_LENGTH);
      }
      this.name = name;
      this.expr = value;
    }

    public String getName() {
      return name;
    }

    public String getExpression() {
      return expr;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append('[').append(name).append(':');
      appendExpr(sb);
      sb.append(']');
      return sb.toString();
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof ExtendedPropertyMatch)) {
        return false;
      }
      ExtendedPropertyMatch that = (ExtendedPropertyMatch) other;
      return safeEquals(this.name, that.name) &&
          safeEquals(this.expr, that.expr);
    }

    @Override
    public int hashCode() {
      return name.hashCode() * 0x101 +
          expr.hashCode() * 0x11;
    }

    private static boolean safeEquals(Object left, Object right) {
      return left == right || (left != null && left.equals(right));
    }

    private void appendExpr(StringBuilder sb) {
      if (expr.contains("]") || expr.contains("\"")) {
        sb.append('\"').
            append(expr.replaceAll("\\\\", "\\\\\\\\").
                replaceAll("\\\"", "\\\\\"")).
            append('\"');
      } else {
        sb.append(expr);
      }
    }

    /**
     * Converts strings of the form "[foo:bar][baz:bin]" to a map of
     * "foo"->"bar" and "baz"->"bin".
     * Ignores quotation marks around values but not around keys.
     * Unescapes backslash-escaped characters.
     *
     * @param extqQuery a non-null query string.
     * @return array of {@link ExtendedPropertyMatch}.
     * @throws IllegalArgumentException whenever the extended property query
     * syntax passed in is invalid.
     */
    public static ExtendedPropertyMatch[]
        arrayFromExtendedPropertyQueryString(String extqQuery) {
      List<ExtendedPropertyMatch> result =
          new LinkedList<ExtendedPropertyMatch>();
      int startPos = 0;
      Matcher m = EXT_PROP_QUERY_PATTERN.matcher(extqQuery);
      while (startPos < extqQuery.length()) {
        if (!m.find(startPos) || m.start() != startPos) {
          throw new IllegalArgumentException(
              "Invalid extended property query:" + extqQuery);
        }
        startPos = m.end();
        String propName = m.group(1);
        String propValue = m.group(2);
        if (propValue.startsWith("\"") && propValue.endsWith("\"")) {
          propValue = propValue.substring(1, propValue.length() - 1).
              replaceAll("\\\\(.)", "$1");
        }
        result.add(new ExtendedPropertyMatch(propName, propValue));
      }
      return result.toArray(EMPTY_EXT_PROP_MATCH);
    }

  }

  /**
   * Constructs a new CalendarQuery object that targets a feed.  The initial
   * state of the query contains no parameters, meaning all entries
   * in the feed would be returned if the query was executed immediately
   * after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be
   *                executed.
   */
  public CalendarQuery(URL feedUrl) {
    super(feedUrl);
  }


  /**
   * Sets the minimum start time for events returned by the query.  Together
   * with {@link #setMaximumStartTime} creates a timespan such that only events
   * that are within the timespan are returned.
   *
   * @param minStart the minimum start time.   A value of {@code null}
   *        disables querying by minimum start time.
   */
  public void setMinimumStartTime(DateTime minStart) {

    List<CustomParameter> customParams = getCustomParameters();

    // Remove any existing minimum start value.
    for (CustomParameter existingValue :
         getCustomParameters(MINIMUM_START_TIME)) {
      customParams.remove(existingValue);
    }

    // Add the specified value.
    if (minStart != null) {
      customParams.add(new CustomParameter(MINIMUM_START_TIME,
                                           minStart.toString()));
    }
  }

  /**
   * Returns the minimum start time for events returned by the query.  Only
   * events that start on or after this time will be returned.
   *
   * @return the minimum start time.  A value of {@code null} indicates
   *         that minimum start-time based querying is disabled.
   */
  public DateTime getMinimumStartTime() {

    List<CustomParameter> minParams = getCustomParameters(MINIMUM_START_TIME);
    if (minParams.size() == 0)
      return null;

    return DateTime.parseDateTime(minParams.get(0).getValue());
  }


  /**
   * Sets the maximum start time for events returned by the query.  Together
   * with {@link #setMinimumStartTime} creates a timespan such that only events
   * that are within the timespan are returned.
   *
   * @param maxStart the maximum start time.   A value of {@code null}
   *        disables querying by maximum start time.
   */
  public void setMaximumStartTime(DateTime maxStart) {

    List<CustomParameter> customParams = getCustomParameters();

    // Remove any existing maximum start value.
    for (CustomParameter existingValue :
         getCustomParameters(MAXIMUM_START_TIME)) {
      customParams.remove(existingValue);
    }

    // Add the specified value.
    if (maxStart != null) {
      customParams.add(new CustomParameter(MAXIMUM_START_TIME,
                                           maxStart.toString()));
    }
  }

  /**
   * Returns the maximum start time for events returned by the query.  Only
   * events that start before this time will be returned.
   *
   * @return the maximum start time.  A value of {@code null} indicates
   *         that maximum start-time based querying is disabled.
   */
  public DateTime getMaximumStartTime() {

    List<CustomParameter> maxParams = getCustomParameters(MAXIMUM_START_TIME);
    if (maxParams.size() == 0)
      return null;

    return DateTime.parseDateTime(maxParams.get(0).getValue());
  }


  /**
   * Sets up the extended property matching for events returned by the query
   * by setting the {@code extq} custom parameter value.
   *
   * @param matches extended property matches.
   *        Only events that satisfy all of these will be returned.
   *        A value of {@code null} or an empty array of matches disables
   *        extended property matching for this CalendarQuery.
   */
  public void setExtendedPropertyQuery(ExtendedPropertyMatch... matches) {
    if (matches == null || matches.length == 0) {
      setStringCustomParameter(EXT_PROP_QUERY, null);
      return;
    }
    StringBuilder query = new StringBuilder();
    for (ExtendedPropertyMatch m : matches) {
      query.append(m.toString());
    }
    setStringCustomParameter(EXT_PROP_QUERY, query.toString());
  }

  /**
   * Returns an array of extended property matches parsed from
   * the current value of {@code extq} custom parameter.
   *
   * @return the extended property query text. An empty array
   *         shall be returned when extended property matching
   *         is disabled for this CalendarQuery.
   */
  public ExtendedPropertyMatch[] getExtendedPropertyQuery() {
    String query = getStringCustomParameter(EXT_PROP_QUERY);
    if (query == null) {
      return EMPTY_EXT_PROP_MATCH;
    }
    return ExtendedPropertyMatch.arrayFromExtendedPropertyQueryString(query);
  }
}
