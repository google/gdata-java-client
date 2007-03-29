/* Copyright (c) 2006 Google Inc.
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
  // point.
  
  /**
   * @return the value of latitude coordinate of this Point.
   */
  public abstract Double getLatitude();

  /**
   * Sets the latitude coordinate of this Point.
   * 
   * @param lat The latitude coordinate of this point.
   */
  public abstract void setLatitude(Double lat);

  /**
   * @return the value of the longitude coordinate of this Point.
   */
  public abstract Double getLongitude();

  /**
   * Sets the longitude coordinate of this Point.
   * 
   * @param lon The longitude coordinate of this point.
   */
  public abstract void setLongitude(Double lon);

}