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

import com.google.gdata.data.Extension;

/**
 * Interface for specifying a box that describes a rectangular geographic
 * location.  This is a bounding box described by two {@link Point} objects,
 * the lower left coordinate and the upper right coordinate.
 *
 * 
 */
public interface Box extends Extension {

  /**
   * @return the point that represents the lower-left coordinate of the box.
   */
  public Point getLowerLeft();

  /**
   * @return the point that represents the upper-right coordinate of the box.
   */
  public Point getUpperRight();

  /**
   * Sets the coordinates of this box.  Both lowerLeft and upperRight must be
   * non-null or both must be null.  Implementations will throw an
   * IllegalArgumentException if one is null and the other non-null.
   *
   * @param lowerLeft the lower left coordinate.  The latitude of this point
   *        must be below the latitude of the upper right coordinate.
   * @param upperRight the upper right coordinate.  The latitude of this point
   *        must be above the latitude of the lower left coordinate.
   * @throws IllegalArgumentException if only one of the points is non-null, or
   *         if an invalid pair of points is given.
   */
  public void setGeoLocation(Point lowerLeft, Point upperRight);
}
