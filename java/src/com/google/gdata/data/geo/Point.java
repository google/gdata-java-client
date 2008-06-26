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
 * Interface for specifying a point that describes some geographic location.
 * Each point should have a latitude and longitude coordinate
 *
 * 
 */
public interface Point extends Extension {

  /**
   * @return the value of latitude coordinate of this Point.
   */
  public Double getLatitude();

  /**
   * @return the value of the longitude coordinate of this Point.
   */
  public Double getLongitude();

  /**
   * Sets the latitude and longitude coordinates of this Point. Either both lat
   * and lon must be non-null, or both must be null. Implementations will throw
   * an IllegalArgumentException if only one is null and one is not. They will
   * also throw an IllegalArgumentException if either of the lat values is
   * outside the valid range.
   *
   * @param lat The latitude in degrees, from -90 to 90.
   * @param lon The longitude in degrees, from -180 to 180.
   * @throws IllegalArgumentException if either lat or lon values are invalid,
   *         or if one of them is null and the other non-null.
   */
  public void setGeoLocation(Double lat, Double lon);
}
