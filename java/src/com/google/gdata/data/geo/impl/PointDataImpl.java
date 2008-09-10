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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.geo.Point;
import com.google.gdata.data.geo.PointData;

/**
 * Implementation of the PointData interface.  Currently only supports
 * a non-repeating Point extension.  This class uses an {@link ExtensionPoint}
 * that is passed in to store the Point extension.
 *
 * 
 */
public class PointDataImpl implements PointData {

  private ExtensionPoint extPoint;

  /**
   * Construct a new implementation of PointData with the given
   * extension point as the backing storage for data.
   */
  public PointDataImpl(ExtensionPoint extensionPoint) {
    this.extPoint = extensionPoint;
  }

  /**
   * Sets the geo-location of the entity based on the lat and long coordinates
   * passed in.
   *
   * @param lat The latitude coordinate, between -90 and 90 degrees.
   * @param lon The longitude coordinate, between -180 and 180 degrees.
   * @throws IllegalArgumentException if the latitude and longitude coordinates
   *        are invalid.
   */
  public void setGeoLocation(Double lat, Double lon)
      throws IllegalArgumentException {
    setGeoLocation(new GeoRssWhere(lat, lon));
  }

  /**
   * Sets the geo-location of the entity based on the Point extension.
   *
   * @param point A point containing the latitude and longitude coordinates.
   */
  public void setGeoLocation(Point point) {
    setPoint(extPoint, point);
  }

  /**
   * Gets the geo-location of the entity.
   * @return a Point that contains the geo-coordinates (latitude and longitude).
   */
  public Point getGeoLocation() {
    return getPoint(extPoint);
  }

  public void clearPoint() {
    clearPoint(extPoint);
  }
  
  /*
   * Declare the extensions that are used for storing Point information.
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    Class<? extends ExtensionPoint> extClass = extPoint.getClass();

    // Declare all all Point implementations here so they are parsable
    // in the context of extClass.

    // Declare our various point extensions, none are repeatable.
    declare(extProfile, extClass, W3CPoint.getDefaultDescription(false));
    declare(extProfile, extClass, GeoRssPoint.getDefaultDescription(false));
    declare(extProfile, extClass, GeoRssWhere.getDefaultDescription(false));
    
    new W3CPoint().declareExtensions(extProfile);
    new GeoRssWhere().declareExtensions(extProfile);
  }

  /**
   * Helper method to add the description to the BaseEntry as well as to the
   * extension class, so auto extension works properly.
   */
  private void declare(ExtensionProfile extProfile,
      Class<? extends ExtensionPoint> extClass, ExtensionDescription desc) {
    extProfile.declare(extClass, desc);
    
    if (BaseEntry.class.isAssignableFrom(extClass)) {
      extProfile.declare(BaseEntry.class, desc);
    }
  }
  
  /**
   * Sets the geo point of the extension passed in. This will first try to
   * replace any existing point information. If there is no existing point, then
   * it will simply add the point extension.
   * 
   * @param ext The extension point to add the Point to.
   * @param point The new point information.
   */
  public static void setPoint(ExtensionPoint ext, Point point) {
    Point existing = getPointExtension(ext);
    if (existing != null) {
      Double lat = point != null ? point.getLatitude() : null;
      Double lon = point != null ? point.getLongitude() : null;
      existing.setGeoLocation(lat, lon);
    } else if (point != null) {
      ext.setExtension(point);
    }
  }
  
  /**
   * Iterates through all the extension points and finds the first matching
   * Point extension.
   * 
   * NOTE(pingc): Made package private for testing. DO NOT USE.
   * 
   * @param ext The extension point to search through.
   * @return A point extension point. This includes GeoRssWhere that may only
   *    contain a box.
   */
  static Point getPointExtension(ExtensionPoint ext) {
    for (Extension e : ext.getExtensions()) {
      if (e instanceof Point) {
        return (Point) e;
      }
    }
    return null;
  }
  
  /**
   * Helper method to retrieve the Box extension point.
   * @param ext The containing extension point.
   * @return An extension point that implements the Box interface and contain
   *    box information.
   */
  public static Point getPoint(ExtensionPoint ext) {
    Point p = getPointExtension(ext);
    if (p != null) {
      if (p instanceof GeoRssWhere) {
        GeoRssWhere geoWhere = (GeoRssWhere)p;
        if (geoWhere.hasPoint()) {
          return geoWhere;
        }
      } else {
        return p;
      }
    }
    return null;
  }
  
  /**
   * Removes the first Point extension found on the extension point. If the
   * point extension is a GeoRssWhere, it will only remove the GeoRssWhere
   * extension if it does not contain a Box/Envelope extension.
   * 
   * @param ext The extension point from which to clear the Box extension.
   */
  public static void clearPoint(ExtensionPoint ext) {
    Point p = getPointExtension(ext);
    if (p != null) {
      if (p instanceof GeoRssWhere) {
        GeoRssWhere where = (GeoRssWhere)p;
        if (where.hasBox()) {
          // If the GeoRssWhere has a box, just clear the point, otherwise
          // remove the whole thing.
          where.clearPoint();
          return;
        }
      }
      ext.removeExtension(p);
    }
  }
}
