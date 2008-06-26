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


package com.google.gdata.data.geo;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;

import java.text.NumberFormat;

/**
 * Extension for a W3C geo:lat element.  It contains the getter/setter for
 * specifying a latitude of a geo-coordinate.  The coordinate is contained
 * within the lat element as: {@literal <geo:lat>coordinate</geo:lat> }.
 * Note that the latitude element does not need to be a child of the geo:Point
 * element. Please see the W3C document
 * <a href="http://www.w3.org/2003/01/geo">http://www.w3.org/2003/01/geo</a> for
 * more information.
 *
 * @see com.google.gdata.data.geo.impl.W3CPoint
 * 
 */
@ExtensionDescription.Default(
    nsAlias=Namespaces.W3C_GEO_ALIAS,
    nsUri=Namespaces.W3C_GEO,
    localName="lat")
public class GeoLat extends ValueConstruct {

  /**
   * The maximmum acceptable value of a latitude coordinate in degrees.
   */
  public static final double MAX_LAT = 90.0d;

  /**
   * The minimum acceptable value of a latitude coordinate in degrees.
   */
  public static final double MIN_LAT = -90.0d;

  /**
   * This denotes the number of significant digits after the decimal point
   * for a coordinate when represented by a string.
   */
  public static final int COORDINATE_PRECISION = 6;

  private static final NumberFormat NUM_FORMAT = NumberFormat.getInstance();

  static {
    NUM_FORMAT.setMaximumFractionDigits(COORDINATE_PRECISION);
    NUM_FORMAT.setMinimumFractionDigits(COORDINATE_PRECISION);
  }

  private Double lat = null;

  /**
   * Creates an instance of the GeoLat extension without a latitude value set.
   */
  public GeoLat() {
    this(null);
  }

  /**
   * Creates an immutable instance of GeoLat extension with the coordinate
   * set to the value passed in.
   *
   * @param lat The latitude coordinate reprensented by this element.
   * @throws IllegalArgumentException if the the latitude is not between
   *        -90 and 90.
   */
  public GeoLat(Double lat) throws IllegalArgumentException {
    super(Namespaces.W3C_GEO_NAMESPACE, "lat", null, null);
    setRequired(true);
    if (lat != null) {
      setLatitude(lat);
      setImmutable(true);
    }
  }

  /**
   * Returns the actual double coordinate for latitude.  Note this value
   * is not rounded unlike the value returned by getValue().
   *
   * @return the latitude represented by this element.
   */
  public Double getLatitude() {
    return lat;
  }

  /**
   * Sets the latitude represented by this element.
   *
   * @param latitude the latitude of this element.
   */
  public void setLatitude(Double latitude) {
    // Note latitude might be null.
    lat = latitude;
    String value = null;

    if (latitude != null) {
      if (latitude.compareTo(MIN_LAT) < 0 || latitude.compareTo(MAX_LAT) > 0) {
        throw new IllegalArgumentException("Latitude must be between " +
            "-90 and 90 degrees.");
      }

      // Format the string so that it has a consistent number of digits
      // after the decimal place.
      value = NUM_FORMAT.format(latitude);
    }

    super.setValue(value);
  }

  /**
   * Overrides base implementation by validating that the string represents
   * a latitude coordinate between -90 and 90 degrees.  Also formats the
   * the string so that it has a consistent number of significant digits
   * after the decimal point.
   */
  @Override
  public void setValue(String value) {
    Double d = null;
    if (value != null) {
      // First ensure that the string is actually a double.
      try {
        d = Double.parseDouble(value);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("'value' must be a double.");
      }
    }

    setLatitude(d);
  }
}
