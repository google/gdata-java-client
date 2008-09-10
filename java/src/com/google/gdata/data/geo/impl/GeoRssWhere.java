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
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.Namespaces;
import com.google.gdata.data.geo.Point;

import java.io.IOException;

/**
 * The georss:where element.  This can contain other elements, so it is
 * extensible, but it also acts as a Point object.  It can also act as a Box as
 * well, and can be extended to support other GML object types.
 *
 * 
 */
public class GeoRssWhere extends ExtensionPoint implements Point, Box {

  static final String NAME = "where";

  /**
   * Constructs an empty georss:where element.
   */
  public GeoRssWhere() {}

  /**
   * Constructs a georss:where element containing the given lat/lon point.
   * If the given lat and lon are null, will construct an empty gml:Point
   * element.
   */
  public GeoRssWhere(Double lat, Double lon) {
    this(new GmlPoint(lat, lon));
  }

  /**
   * Constructs a georss:where element containing the given point.  If the point
   * is already a gml:Point object, it will be used as the extension directly,
   * otherwise a copy will be created.  If the point is null no extensions will
   * be added.
   */
  public GeoRssWhere(Point point) {
    if (point != null) {
      if (!(point instanceof GmlPoint)) {
        point = new GmlPoint(point);
      }
      setExtension(point);
    }
  }

  /**
   * Constructs a georss:where element containing the four coordinates that
   * define a box.  If all of the points are null then an empty gml:envelope
   * will be created.
   */
  public GeoRssWhere(Double lowerLat, Double lowerLon,
      Double upperLat, Double upperLon) {
    this(new GmlEnvelope(lowerLat, lowerLon, upperLat, upperLon));
  }

  /**
   * Constructs a georss:where element containing the given gml:lowerCorner
   * and gml:upperCorner elements.  If both lower and upper are null then an
   * empty gml:envelope will be created.
   */
  public GeoRssWhere(Point lower, Point upper) {
    this(new GmlEnvelope(lower, upper));
  }

  /**
   * Constructs a georss:where element containing the given box.  If the box
   * is null no extension will be set.
   */
  public GeoRssWhere(Box box) {
    if (box != null) {
      if (!(box instanceof GmlEnvelope)) {
        box = new GmlEnvelope(box);
      }
      setExtension(box);
    }
  }

  /**
   * Returns the default extension description for this element.
   */
  public static ExtensionDescription getDefaultDescription(boolean repeatable) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(GeoRssWhere.class);
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

  /*
   * Declare the extensions for georss:where.  This contains elements for
   * points, boxes, polygons, and lines.  This can be extended to include other
   * types of information as well.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Declare the gml extensions we support.
    extProfile.declare(GeoRssWhere.class,
        GmlPoint.getDefaultDescription(false));
    new GmlPoint().declareExtensions(extProfile);

    extProfile.declare(GeoRssWhere.class,
        GmlEnvelope.getDefaultDescription(false));
    new GmlEnvelope().declareExtensions(extProfile);

    super.declareExtensions(extProfile);
  }

  /*
   * Get the latitude by pulling it from the gml:Point element (if it exists).
   */
  public Double getLatitude() {
    GmlPoint point = getExtension(GmlPoint.class);
    return point != null ? point.getLatitude() : null;
  }

  /*
   * Get the longitude by pulling it from the gml:Point element (if it exists).
   */
  public Double getLongitude() {
    GmlPoint point = getExtension(GmlPoint.class);
    return point != null ? point.getLongitude() : null;
  }

  /*
   * Set the latitude and longitude by creating or removing the gml:Point
   * element as needed. If the existing wrapped point is non-null but the given
   * lat and lon are both null, it is removed. Otherwise its new latitude and
   * longitude are set. If the gml:Point element doesn't exist, it is only
   * created if the given coordinates are non-null.  We also rely on the
   * gml:Point object to do checking of valid values.
   */
  public void setGeoLocation(Double lat, Double lon) {
    GmlPoint point = getExtension(GmlPoint.class);
    if (point != null) {
      point.setGeoLocation(lat, lon);
    } else if (lat != null || lon != null) {
      point = new GmlPoint();
      setExtension(point);
      point.setGeoLocation(lat, lon);
    }
  }

  /*
   * Pull out the envelope and use its lower left point.
   */
  public GmlLowerCorner getLowerLeft() {
    GmlEnvelope envelope = getExtension(GmlEnvelope.class);
    return envelope != null ? envelope.getLowerLeft() : null;
  }

  /*
   * Pull out the envelope and use its upper right point.
   */
  public GmlUpperCorner getUpperRight() {
    GmlEnvelope envelope = getExtension(GmlEnvelope.class);
    return envelope != null ? envelope.getUpperRight() : null;
  }

  /*
   * Set the bounding box points.  This correctly handles all cases where the
   * existing gml:envelope is null or has null values.  In particular:
   *
   * 1)  Remove the lower and upper corner of the GmlEnvelope
   *     if lowerLeft and upperRight are null.
   * 2)  Create a new extension if it doesn't exist and either lowerLeft or
   *     upperRight are non-null.
   * 3)  Otherwise just set the lowerLeft and upperRight on the existing point.
   */
  public void setGeoLocation(Point lowerLeft, Point upperRight) {
    GmlEnvelope envelope = getExtension(GmlEnvelope.class);
    if (envelope != null) {
      envelope.setGeoLocation(lowerLeft, upperRight);
    } else if (lowerLeft != null || upperRight != null) {
      envelope = new GmlEnvelope();
      setExtension(envelope);
      envelope.setGeoLocation(lowerLeft, upperRight);
    }
  }
  
  /**
   * @return true if this GeoRssWhere element contains a point element.
   */
  public boolean hasPoint() {
    return getExtension(GmlPoint.class) != null;
  }
  
  /**
   * @return true if this GeoRssWhere element contains an envelope element.
   */
  public boolean hasBox() {
    return getExtension(GmlEnvelope.class) != null;
  }
  
  /**
   * Removes the GmlPoint element inside this GeoRssWhere.
   */
  public void clearPoint() {
    removeExtension(GmlPoint.class);
  }
  
  /**
   * Removes the GmlEnvelope element inside this GeoRssWhere.
   */
  public void clearBox() {
    removeExtension(GmlEnvelope.class);
  }

  /*
   * Just write out the start tag, any extensions, and the end tag.
   * This will automatically handle any extensions we add, so shouldn't need
   * to be updated as we add more gml types.
   */
  @Override
  public void generate(XmlWriter w, ExtensionProfile p) throws IOException {

    generateStartElement(w, Namespaces.GEO_RSS_NAMESPACE, NAME, null, null);

    generateExtensions(w, p);

    w.endElement(Namespaces.GEO_RSS_NAMESPACE, NAME);
  }
}
