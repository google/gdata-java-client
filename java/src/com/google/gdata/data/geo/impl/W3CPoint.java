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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.geo.GeoLat;
import com.google.gdata.data.geo.GeoLong;
import com.google.gdata.data.geo.Namespaces;
import com.google.gdata.data.geo.Point;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Extension for a W3C geo:Point element.  It contains the getter/setter for
 * specifying the longitude and latitude of a geo-coordinate.
 * Please see the W3C document
 * <a href="http://www.w3.org/2003/01/geo">http://www.w3.org/2003/01/geo</a> for
 * more information.
 *
 * 
 */
public class W3CPoint extends ExtensionPoint implements Point {

  static final String NAME = "Point";

  /**
   * Constructs an empty geo:Point element.
   */
  public W3CPoint() {}

  /**
   * Constructs a geo:Point element with child geo:lat and geo:long
   * elements based on the parameters passed in.
   *
   * @param lat The latitude coordinate of this point.
   * @param lon The longitude coordinate of this point.
   */
  public W3CPoint(Double lat, Double lon) {
    setGeoLocation(lat, lon);
  }

  /**
   * Constructs a geo:Point element copying the lat and lon values from the
   * given point.  If the given point is null an empty geo:Point element will
   * be created.
   */
  public W3CPoint(Point copyFrom) {
    this(copyFrom == null ? null : copyFrom.getLatitude(),
        copyFrom == null ? null : copyFrom.getLongitude());
  }

  /**
   * Returns the suggested extension description with configurable
   * repeatability.
   */
  public static ExtensionDescription getDefaultDescription(boolean repeatable) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(W3CPoint.class);
    desc.setNamespace(Namespaces.W3C_GEO_NAMESPACE);
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

  /*
   * Declare the extensions for geo point.  This contains elements for
   * latitude and longitude.  In the future, it will also include other geo
   * information such as altitude.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Declare the latitude and longitude extensions.
    extProfile.declare(W3CPoint.class,
        ExtensionDescription.getDefaultDescription(GeoLat.class));
    extProfile.declare(W3CPoint.class,
        ExtensionDescription.getDefaultDescription(GeoLong.class));
    super.declareExtensions(extProfile);
  }

  /**
   * @return the value of the geo:lat element within this Point.
   */
  public Double getLatitude() {
    GeoLat lat = getExtension(GeoLat.class);
    return lat != null ? lat.getLatitude() : null;
  }

  /**
   * @return the value of the geo:long element within this Point.
   */
  public Double getLongitude() {
    GeoLong lon = getExtension(GeoLong.class);
    return lon != null ? lon.getLongitude() : null;
  }

  /**
   * Sets the geo location based on the passed in lat and lon values.  Both
   * must be null or non null, and the lat must be between -90 and 90, and the
   * lon between -180 and 180.
   */
  public void setGeoLocation(Double lat, Double lon) {
    if (lat != null && lon != null) {
      setExtension(new GeoLat(lat));
      setExtension(new GeoLong(lon));
    } else if (lat != null || lon != null) {
      throw new IllegalArgumentException(
          "'lat' and 'lon' must either both be null or non-null.");
    } else {
      removeExtension(GeoLat.class);
      removeExtension(GeoLong.class);
    }
  }

  /**
   * Generates the XML corresponding to this GeoPoint.
   */
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    generateStartElement(w, Namespaces.W3C_GEO_NAMESPACE, NAME, null, null);

    // Generate the inner extensions (lat, long).
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.W3C_GEO_NAMESPACE, NAME);
  }

  /**
   * @return a handler for processing a W3C geo:Point element.  All
   * points must have a latitude and longitude element.
   */
  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new Handler(extProfile);
  }

  /**
   * Simple delegating handler implementation that uses (only)
   * ExtensionProfile linkages for child element lookup. Ensures that all points
   * have a latitude and longitude element.
   */
  protected class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) {
      super(extProfile, W3CPoint.class);
    }

    /**
     * Overrides the base implementation by adding checks to make sure
     * the point has a latitude and longitude.
     */
    @Override
    public void processEndElement() throws ParseException {
      super.processEndElement();
      Extension lat = getExtension(GeoLat.class);
      Extension lon = getExtension(GeoLong.class);
      if (lat != null || lon != null) {
        // If lat OR long is specified, then both are required.
        if (lat == null) {
          throw new ParseException("All geo:Point elements must have a " +
              "latitude coordinate.");
        }
        if (lon == null) {
          throw new ParseException("All geo:Point elements must have a " +
              "longitude coordinate.");
        }
      }
    }
  }
}
