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


package com.google.gdata.data.geo.impl;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.geo.GeoLat;
import com.google.gdata.data.geo.GeoLong;
import com.google.gdata.data.geo.Point;

/**
 * A basic point construct consists of a space-separated coordinate in geo
 * space.  The namespace and name of the element are supplied by subclasses.
 *
 * 
 */
public abstract class PointConstruct extends ValueConstruct implements Point {

  private Double lat;
  private Double lon;

  /**
   * Constructor to create an empty point object.
   */
  public PointConstruct(XmlNamespace namespace, String name) {
    super(namespace, name, null);
    setRequired(false);
  }

  /**
   * Constructor to create a point from a given lat/lon pair.  Will create an
   * empy point if both values are null, if only one value is null with throw
   * an illegal argument exception.
   */
  public PointConstruct(XmlNamespace namespace, String name,
      Double lat, Double lon) {
    super(namespace, name, null);

    if (lat == null && lon == null) {
      setRequired(false);
    }
    setGeoLocation(lat, lon);
  }

  /**
   * Copy constructor to create a point from another point.
   */
  public PointConstruct(XmlNamespace namespace, String name, Point copyFrom) {
    this(namespace, name, copyFrom == null ? null : copyFrom.getLatitude(),
        copyFrom == null ? null : copyFrom.getLongitude());
  }

  /*
   * Just returns the stored latitude.
   */
  public Double getLatitude() {
    return lat;
  }

  /*
   * Just returns the stored longitude.
   */
  public Double getLongitude() {
    return lon;
  }

  /*
   * Sets the latitude and longitude values of this Point, and validates that
   * the values are correct.  Throws IllegalArgumentExceptions on invalid
   * values, as well as when one is null and the other is not.
   */
  public void setGeoLocation(Double lat, Double lon) {
    if (lat != null && lon != null) {
      if (lat.compareTo(GeoLat.MIN_LAT) < 0
          || lat.compareTo(GeoLat.MAX_LAT) > 0) {
        throw new IllegalArgumentException("Latitude must be between "
            + "-90 and 90 degrees.");
      }

      if (lon.compareTo(GeoLong.MIN_LONG) < 0
          || lon.compareTo(GeoLong.MAX_LONG) > 0) {
        throw new IllegalArgumentException("Longitude must be between "
            + "-180 and 180 degrees.");
      }
    } else if (lat != null || lon != null) {
      throw new IllegalArgumentException(
          "latitude and longitude must either both be null or non-null.");
    }

    this.lat = lat;
    this.lon = lon;

    super.setValue(toString());
  }

  /*
   * Overriding toString to generate the string representation of the point.
   */
  @Override
  public String toString() {
    if (lat != null && lon != null) {
      return lat + " " + lon;
    }
    return null;
  }

  /*
   * Overriding setValue to parse the string into a lat and long, and then
   * setting those values.  Will throw illegal argument exceptions if the format
   * isn't correct.
   */
  @Override
  public void setValue(String v) {
    Double lat = null;
    Double lon = null;

    if (v != null) {
      v = v.trim();
      int space = v.indexOf(' ');
      if (space == -1) {
        throw createInvalidValueException();
      }

      String latString = v.substring(0, space);
      String lonString = v.substring(space + 1);
      try {
        lat = Double.valueOf(latString);
        lon = Double.valueOf(lonString);
      } catch (NumberFormatException e) {
        throw createInvalidValueException();
      }
    }

    // This will also call super.setValue so we don't call it here.
    setGeoLocation(lat, lon);
  }

  @Override
  protected void validate() throws IllegalStateException {
    super.validate();

    if ((lat == null && lon != null)
        || (lat != null && lon == null)) {
      throw new IllegalStateException (
          "latitude and longitude must either both be null or non-null.");
    }
  }

  /**
   * Helper to generate the nice error message when given illegal values.
   */
  private IllegalArgumentException createInvalidValueException() {
    return new IllegalArgumentException(
        "Format of a coordinate is \"latitude longitude\", where latitude "
        + "and longitude are doubles, separated by a space.");
  }
}
