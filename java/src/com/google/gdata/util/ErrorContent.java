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


package com.google.gdata.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Structured error information.
 * 
 * 
 */
public interface ErrorContent {

  /**
   * Enumerated constants for error location types.
   */
  public static enum LocationType {
    /**
     * The location is an xpath expression to the location of the error in the
     * request.
     */
    XPATH,
    
    /** The error occurred in a header, and the location describes which one. */
    HEADER,

    /** The error occurred somewhere else, or the location is unknown. */
    OTHER;

    private static final Map<String, LocationType> VALUE_MAP;
    static {
      Map<String, LocationType> map = new HashMap<String, LocationType>();
      for (LocationType locationType : LocationType.values()) {
        map.put(locationType.toString(), locationType);
      }
      VALUE_MAP = Collections.unmodifiableMap(map);
    }
    
    /**
     * Return the location type matching the given string value.  Unlike
     * the {@link #valueOf(String)} method, this will return {@code null} if
     * the value was not found, rather than throwing an exception.  The values
     * are based on the {@link #toString()} method for each location type.
     */
    public static LocationType fromString(String value) {
      return VALUE_MAP.get(value);
    }
    
    /**
     * Return the string value of the location type.  This is the value that
     * will appear in the XML output.
     */
    @Override
    public String toString() {
      return name().toLowerCase();
    }
  }
  
  /**
   * Return the error domain.
   */
  String getDomainName();

  /**
   * Return the name of this error, which will be unique within its domain.
   * The value will appear as the content of the {@code code} element in the XML
   * error format.
   */
  String getCodeName();
  
  /**
   * Return the location of the error.
   */
  String getLocation();

  /**
   * Return the type of error location.  See {@link LocationType} for the
   * available values.
   */
  LocationType getLocationType();
  
  /**
   * Gets the internal reason (unlocalized explanation) associated with this
   * ErrorInfo.  The value will appear as the content of the
   * {@code internalReason} element in the XML error format.
   */
  String getInternalReason();
  
  /**
   * Gets the extended help URI.  This can be used to retrieve a
   * detailed explanation of the error code.  The value will appear
   * as the content of the {@code extendedHelp} element in the XML
   * error format.
   */
  String getExtendedHelp();

  /**
   * Gets the URI to which a report should be sent when this error
   * is received.  The value will appear as the content of the 
   * {@code sendReport} element in the XML error format.
   */
  String getSendReport();

  /**
   * Returns debugging information.
   */
  String getDebugInfo();
}
