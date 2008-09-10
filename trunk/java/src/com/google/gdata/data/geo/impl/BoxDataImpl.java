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

import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.BoxData;
import com.google.gdata.data.geo.Point;

/**
 * Implementation of the BoxData interface.  Currently only supports a
 * non-repeating Box extension.  This class uses an {@link ExtensionPoint} that
 * is passed in to store the Box extension.
 *
 * 
 */
public class BoxDataImpl implements BoxData {

  private final ExtensionPoint extPoint;

  /**
   * Construct a new BoxData with the given extension point as the backing
   * storage of the data.
   */
  public BoxDataImpl(ExtensionPoint extensionPoint) {
    this.extPoint = extensionPoint;
  }

  /**
   * If there is an existing box this will set the new values on it.
   * Otherwise it will create a new GeoRssWhere element to contain them.
   * If just one argument is null it will throw an exception.
   */
  public void setGeoBoundingBox(Point lowerLeft, Point upperRight) { 
    setGeoBoundingBox(new GeoRssWhere(lowerLeft, upperRight));
  }

  /**
   * Sets the geo bounding box to the given box.  If there is an existing box
   * it will copy the values from the given box, otherwise it will use the new
   * box as the extension.
   */
  public void setGeoBoundingBox(Box box) {
    setBox(extPoint, box);
  }

  /**
   * Gets the geo bounding box for this extension point.
   */
  public Box getGeoBoundingBox() {
    return getBox(extPoint);
  }

  public void clearGeoBoundingBox() {
    clearBox(extPoint);
  }
  
  /**
   * Sets the geo bounding box of the extension passed in. This will first 
   * try to replace any existing bounding box information. If there is no 
   * existing bounding box, then it'll simply add the box extension.
   * 
   * @param ext The extension point to add the Box to.
   * @param box The new box information.
   */
  public static void setBox(ExtensionPoint ext, Box box) {
    Box existing = getBoxExtension(ext);
    if (existing != null) {
      Point lowerLeft = box != null ? box.getLowerLeft() : null;
      Point upperRight = box != null ? box.getUpperRight() : null;
      // If we have a box already, we copy the new points onto it if we can.
      existing.setGeoLocation(lowerLeft, upperRight);
    } else if (box != null) {
      // We don't have a box yet, so we set it directly here.  We actually use
      // the passed-in extension in this case.
      ext.setExtension(box);
    }
  }
  
  /**
   * Helper method to retrieve the Box extension point. Note this will
   * return empty boxes but will only return an GeoRssWhere extension point
   * if it contains a GmlEnvelope.
   * 
   * @param ext The containing extension point.
   * @return An extension point that implements the Box interface and contain
   *    box information.
   */
  public static Box getBox(ExtensionPoint ext) {
    Box b = getBoxExtension(ext);
    if (b != null) {
      if (b instanceof GeoRssWhere) {
        GeoRssWhere geoWhere = (GeoRssWhere)b;
        if (geoWhere.hasBox()) {
          return geoWhere;
        }
      } else {
        return b;
      }
    }
    return null;
  }
  
  /**
   * Iterates through all the extension points and finds the first matching
   * Box extension (any box extension).
   * 
   * NOTE(pingc): Package private for testing. DO NOT USE.
   * 
   * @param ext The extension point to search through.
   * @return A box extension point. This includes GeoRssWhere that may only
   *    contain a point.
   */
  static Box getBoxExtension(ExtensionPoint ext) {
    for (Extension e : ext.getExtensions()) {
      if (e instanceof Box) {
        return (Box) e;
      }
    }
    return null;
  }
  
  /**
   * Removes the first Box extension found on the extension point. If the
   * box extension is a GeoRssWhere, it will only remove the GeoRssWhere
   * extension if it does not contain a Point extension.
   * 
   * @param ext The extension point from which to clear the Box extension.
   */
  public static void clearBox(ExtensionPoint ext) {
    Box b = getBoxExtension(ext);
    if (b != null) {
      if (b instanceof GeoRssWhere) {
        GeoRssWhere where = (GeoRssWhere)b;
        if (where.hasPoint()) {
          // If the GeoRssWhere has a point, just clear the box. Otherwise
          // remove the whole thing.
          where.clearBox();
          return;
        }
      }
      ext.removeExtension(b);
    }
  }
  
  /*
   * Declare the extensions that are used for storing Box information.
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    Class<? extends ExtensionPoint> extClass = extPoint.getClass();

    // Declare all all Box implementations here so they are parsable
    // in the context of extClass.
    extProfile.declare(extClass, GeoRssBox.getDefaultDescription(false));
    extProfile.declare(extClass, GeoRssWhere.getDefaultDescription(false));
    new GeoRssWhere().declareExtensions(extProfile);

    // Also declare GmlEnvelope as an allowed box, even though it should
    // be embedded in a georss:where.  This is just to be more forgiving
    // to clients of our apis.
    extProfile.declare(extClass, GmlEnvelope.getDefaultDescription(false));
    new GmlEnvelope().declareExtensions(extProfile);
  }
}
