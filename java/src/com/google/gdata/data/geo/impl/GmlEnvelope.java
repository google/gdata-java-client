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
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.geo.Box;
import com.google.gdata.data.geo.Namespaces;
import com.google.gdata.data.geo.Point;

/**
 * A gml:Envelope element, this is used to describe a box using the gml version
 * of our geographic information language.  An envelope contains an upper and
 * a lower corner.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias=Namespaces.GML_ALIAS,
    nsUri=Namespaces.GML,
    localName=GmlEnvelope.NAME)
public class GmlEnvelope extends ExtensionPoint implements Box {

  static final String NAME = "Envelope";

  /**
   * Constructs an empty gml:Envelope element.
   */
  public GmlEnvelope() {}

  /**
   * Constructs a gml:Envelope with the given coordinates.
   */
  public GmlEnvelope(Double lowerLat, Double lowerLon,
      Double upperLat, Double upperLon) {
    this(new GmlLowerCorner(lowerLat, lowerLon),
        new GmlUpperCorner(upperLat, upperLon));
  }

  /**
   * Constructs a gml:Envelope with the given lower and upper values.  If the
   * given values are already a GmlLowerCorner and a GmlUpperCorner, they will
   * be used direclty as the extensions, otherwise they will be copied.  If both
   * points are null an empty point will be created, otherwise if one of them is
   * null then an IllegalArgumentException will be thrown.
   */
  public GmlEnvelope(Point lower, Point upper) {
    setGeoLocation(lower, upper);
  }

  /**
   * Constructs a gml:Envelope by copying from the given box.  This calls the
   * {@link #GmlEnvelope(Point, Point)} constructor with the points in the box,
   * or with nulls if the box itself is null.
   */
  public GmlEnvelope(Box box) {
    this(box == null ? null : box.getLowerLeft(),
        box == null ? null : box.getUpperRight());
  }

  /**
   * Returns the suggested extension description with configurable
   * repeatability.
   */
  public static ExtensionDescription getDefaultDescription(boolean repeatable) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(GmlEnvelope.class);
    desc.setNamespace(Namespaces.GML_NAMESPACE);
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
   * Declare the extensions for gml envelope.  This contains two elements with
   * the coordinates for the lower and upper corners.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Declare the gml:lowerCorner and gml:upperCorner elements.
    extProfile.declare(GmlEnvelope.class,
        GmlLowerCorner.getDefaultDescription(false));
    extProfile.declare(GmlEnvelope.class,
        GmlUpperCorner.getDefaultDescription(false));
    super.declareExtensions(extProfile);
  }

  /*
   * Get the lower corner extension element.
   */
  public GmlLowerCorner getLowerLeft() {
    return getExtension(GmlLowerCorner.class);
  }

  /*
   * Get the upper corner extension element.
   */
  public GmlUpperCorner getUpperRight() {
    return getExtension(GmlUpperCorner.class);
  }

  /*
   * Sets the geo location of this envelope.  If the passed in points are not
   * GmlLowerCorner and GmlUpperCorner, they will get converted.  Either both
   * must be null or both non null or an IllegalArgumentException is thrown.
   */
  public void setGeoLocation(Point lowerLeft, Point upperRight) {
    if (lowerLeft != null && upperRight != null) {
      if (!(lowerLeft instanceof GmlLowerCorner)) {
        lowerLeft = new GmlLowerCorner(lowerLeft);
      }
      if (!(upperRight instanceof GmlUpperCorner)) {
        upperRight = new GmlUpperCorner(upperRight);
      }
      setExtension(lowerLeft);
      setExtension(upperRight);
    } else if (lowerLeft != null || upperRight != null) {
      throw new IllegalArgumentException(
      "'lower' and 'upper' must either both be null or non-null.");
    } else {
      removeExtension(GmlLowerCorner.class);
      removeExtension(GmlUpperCorner.class);
    }
  }

  /*
   * Set the upper extension element.  If the passed in Point is not a
   * GmlUpperCorner it will get converted to one.
   */
  public void setUpperRight(Point upperRight) {
    if (upperRight == null) {
      removeExtension(GmlUpperCorner.class);
    } else {
      if (!(upperRight instanceof GmlUpperCorner)) {
        upperRight = new GmlUpperCorner(upperRight);
      }

      setExtension(upperRight);
    }
  }

  @Override
  protected void validate() throws IllegalStateException {
    super.validate();

    Point lower = getLowerLeft();
    Point upper = getUpperRight();

    if ((lower != null && upper == null)
        || (lower == null && upper != null)) {
      throw new IllegalStateException(
          "Both upper and lower must be set or neither may be set.");
    }
  }
}
