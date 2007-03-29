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


package com.google.gdata.client.calendar;


import com.google.gdata.client.Query;
import com.google.gdata.data.DateTime;

import java.net.URL;
import java.util.List;


/**
 * The CalendarQuery class extends the base GData Query class to
 * define convenience APIs for Calendar custom query parameters.
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
   * Sets the minimum start time for events returned by the query.  Only
   * events that start on or after this time will be returned.
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
   * Sets the maximum start time for events returned by the query.  Only
   * events that start before this time will be returned.
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
}
