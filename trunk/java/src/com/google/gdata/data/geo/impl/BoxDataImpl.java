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

  /*
   * If there is an existing box this will set the new values on it.
   * Otherwise it will create a new GeoRssWhere element to contain them.
   * If just one argument is null it will throw an exception.
   */
  public void setGeoBoundingBox(Point lowerLeft, Point upperRight) {
    // Check if there is an existing box.  If so we just set the points
    // on it directly.  Otherwise we create a new extension to hold the data.
    Box existing = getGeoBoundingBox();
    if (existing != null) {
      existing.setGeoLocation(lowerLeft, upperRight);
    } else {
      extPoint.setExtension(new GeoRssWhere(lowerLeft, upperRight));
    }
  }

  /*
   * Sets the geo bounding box to the given box.  If there is an existing box
   * it will copy the values from the given box, otherwise it will use the new
   * box as the extension.
   */
  public void setGeoBoundingBox(Box box) {
    Box existing = getGeoBoundingBox();
    if (existing != null) {
      // If we have a box already, we copy the new points onto it if we can.
      Point lower = box == null ? null : box.getLowerLeft();
      Point upper = box == null ? null : box.getUpperRight();
      existing.setGeoLocation(lower, upper);

    } else if (box != null) {

      // We don't have a box yet, so we set it directly here.  We actually use
      // the passed-in extension in this case.
      extPoint.setExtension(box);
    }
  }

  /*
   * Gets the geo bounding box for this extension point.
   */
  public Box getGeoBoundingBox() {
    for (Extension ext : extPoint.getExtensions()) {
      if (ext instanceof Box) {
        return (Box) ext;
      }
    }
    return null;
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
