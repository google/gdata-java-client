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

/**
 * Data interface for all geo-tagged extension points.  This allows various
 * entries and feeds to easily include a Point extension without dealing with
 * the underlying implementation of the Point.
 *
 * 
 */
public interface PointData {

  /**
   * Sets the geo-location of the entity based on the lat and long coordinates
   * passed in.  This will create a new Point object if none exists, otherwise
   * it will copy the new coordinates into the existing object.
   *
   * @param lat The latitude coordinate, between -90 and 90 degrees.
   * @param lon The longitude coordinate, between -180 and 180 degrees.
   * @throws IllegalArgumentException if the latitude and longitude coordinates
   *        are invalid.
   */
  public void setGeoLocation(Double lat, Double lon)
      throws IllegalArgumentException;

  /**
   * Sets the geo-location of the entity based on the Point extension.  This
   * will use the passed in extension as the geo location if none already
   * exists, otherwise it will copy the given point's data into the existing
   * point object.
   *
   * @param point A point containing the latitude and longitude coordinates.
   */
  public void setGeoLocation(Point point);

  /**
   * Gets the geo-location of the entity.
   * 
   * @return a Point that contains the geo-coordinates (latitude and longitude).
   */
  public Point getGeoLocation();
  
  /**
   * Clears the point data and removes the extension point.
   */
  public void clearPoint();
}
