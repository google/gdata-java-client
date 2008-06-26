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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.Namespaces;
import com.google.gdata.data.geo.Point;

/**
 * A georss:box that contains 2 points, like:
 * <georss:box>42.943 -71.032 43.039 -69.856</georss:box>
 *
 * 
 */
public class GeoRssBox extends ValueConstruct implements Box {

  static final String NAME = "box";

  private Point lowerLeft;
  private Point upperRight;

  /**
   * Constructs an empty georss:box element.
   */
  public GeoRssBox() {
    this(null, null);
  }

  /**
   * Constructs a georss:box element with the given set of points.
   */
  public GeoRssBox(Double lowerLat, Double lowerLon,
      Double upperLat, Double upperLon) {
    this(new GeoRssPoint(lowerLat, lowerLon),
        new GeoRssPoint(upperLat, upperLon));
  }

  /**
   * Constructs a georss:box element with the given bounding points.
   */
  public GeoRssBox(Point lowerLeft, Point upperRight) {
    super(Namespaces.GEO_RSS_NAMESPACE, NAME, null);

    if (lowerLeft == null && upperRight == null) {
      setRequired(false);
    }

    setGeoLocation(lowerLeft, upperRight);
  }

  /**
   * Constructs a georss:box element by copying the data from the given box.
   * Will call the {@link #GeoRssBox(Point, Point)} constructor with the parts
   * of the passed in box, or with nulls if the box itself is null.
   */
  public GeoRssBox(Box box) {
    this(box == null ? null : box.getLowerLeft(),
        box == null ? null : box.getUpperRight());
  }

  /**
   * Returns the suggested extension description with configurable
   * repeatability.
   */
  public static ExtensionDescription getDefaultDescription(boolean repeatable) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(GeoRssBox.class);
    desc.setNamespace(Namespaces.GEO_RSS_NAMESPACE);
    desc.setLocalName(NAME);
    desc.setRepeatable(repeatable);
    return desc;
  }

  /**
   * Returns the suggested extension description and is repeatable.
   */
  public static ExtensionDescription getDefaultDescription() {
    return getDefaultDescription(true);
  }

  /**
   * Gets the lower left point.
   */
  public Point getLowerLeft() {
    return lowerLeft;
  }

  /**
   * Gets the upper right point.
   */
  public Point getUpperRight() {
    return upperRight;
  }

  /*
   * Sets the geo location based on the given points.
   */
  public void setGeoLocation(Point lowerLeft, Point upperRight) {
    if (lowerLeft != null && upperRight != null) {
      if (lowerLeft.getLatitude() > upperRight.getLatitude()) {
        throw new IllegalArgumentException(
            "'lowerLeft' must be below 'upperRight'.");
      }
    } else if (lowerLeft != null || upperRight != null) {
      throw new IllegalArgumentException(
          "'lowerLeft' and 'upperRight' must either both be null or non-null.");
    }

    this.lowerLeft = lowerLeft;
    this.upperRight = upperRight;

    super.setValue(toString());
  }

  /*
   * Overriding toString to generate the string representation of the point.
   */
  @Override
  public String toString() {
    if (lowerLeft != null && upperRight != null) {
      return lowerLeft.getLatitude() + " " + lowerLeft.getLongitude()
          + " " + upperRight.getLatitude() + " " + upperRight.getLongitude();
    }
    return null;
  }

  /*
   * Overriding setValue to parse the string into two latlong pairs, and then
   * set those values.  Will throw illegal argument exceptions if the format
   * isn't correct.
   */
  @Override
  public void setValue(String v) {
    Point lower = null;
    Point upper = null;

    if (v != null) {
      v = v.trim();
      String[] values = v.split(" ");
      if (values.length != 4) {
        throw createInvalidValueException();
      }
      lower = getPoint(values[0], values[1]);
      upper = getPoint(values[2], values[3]);
    }

    // This will also call super.setValue so we don't call it here.
    setGeoLocation(lower, upper);
  }

  /*
   * Converts a pair of strings into a point object.
   */
  private Point getPoint(String latStr, String lonStr) {
    try {
      Double lat = Double.valueOf(latStr);
      Double lon = Double.valueOf(lonStr);
      return new GeoRssPoint(lat, lon);
    } catch (NumberFormatException e) {
      throw createInvalidValueException();
    }
  }

  /**
   * Helper to generate the nice error message when given illegal values.
   */
  private IllegalArgumentException createInvalidValueException() {
    return new IllegalArgumentException(
        "Format of a georss:box is \"latitude longitude latitude longitutde\","
        + " where the first pair is the lower left point, and the second pair"
        + " is the upper right point.  All values must be doubles, separated"
        + " by spaces.");
  }
}
