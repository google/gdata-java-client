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
 * Data interface for all geo-tagged extension points that represent a bounding
 * box around a geographical area.  This allows various feeds and entries to
 * easily include a Box extension without dealing with the underlying Box
 * implementation.
 *
 * 
 */
public interface BoxData {

  /**
   * Sets the bounding box based on two {@link Point} objects.  If there is an
   * existing Box on this object, the new points will be copied into the
   * existing box rather than creating a new box.
   *
   * @param lowerLeft the lower left coordinate of the box.
   * @param upperRight the upper right coordinate of the box.
   */
  public void setGeoBoundingBox(Point lowerLeft, Point upperRight);

  /**
   * Sets the bounding box for this entity based on a {@link Box} extension.  If
   * there is an existing Box on this object, the new box will have its points
   * copied into the existing box rather than using the passed-in box.
   *
   * @param boundingBox the box that bounds this entity.
   */
  public void setGeoBoundingBox(Box boundingBox);

  /**
   * Gets the bounding box of this entity.
   *
   * @return a Box that contains the boundary for the content of this entity.
   */
  public Box getGeoBoundingBox();
  
  /**
   * Clears the bounding box and removes the extension point.
   */
  public void clearGeoBoundingBox();
}
