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


package com.google.api.gbase.client;

/**
 * Class representing parsed information for one thumbnail: the size (in pixels)
 * and the Url for accessing the image. This class is mutable through its 
 * {@link #setUrl(String)} and {@link #setSize(int, int)} methods. 
 * 
 * 
 */
public final class Thumbnail {
  
  /** Url pointing to the thumbnail. */
  private String url;

  /** Thumbnail width, expressed in pixels. Null if not set. */
  private Integer width;
  
  /** Thumbnail height, expressed in pixels. Null if not set. */
  private Integer height;
  
  /** 
   * Returns the Url pointing to the thumbnail image.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the Url that should point to the thumbnail image.
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Returns the image width (pixels). Use {@link #isSizeSpecified()} to check
   * if this object contains valid size information. 
   * 
   * @return the width of the thumbnail, expressed in pixels
   * @throws IllegalStateException if the thumbnail size information is not set
   */
  public int getWidth() {
    if (!isSizeSpecified()) {
      throw new IllegalStateException("Size is not specified.");
    }
    return width;
  }

  /**
   * Returns the image height (pixels). Use {@link #isSizeSpecified()} to check
   * if this object contains valid size information. 
   * 
   * @return the height of the thumbnail, expressed in pixels
   * @throws IllegalStateException if the thumbnail size information is not set
   */
  public int getHeight() {
    if (!isSizeSpecified()) {
      throw new IllegalStateException("Size is not specified.");
    }
    return height;
  }

  /**
   * Sets the thumbnail size.
   * 
   * @param width the width of the thumbnail, expressed in pixels
   * @param height the height of the thumbnail, expressed in pixels
   */
  public void setSize(int width, int height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Returns {@code true} if the size was explicitly set, {@code false}
   * otherwise.
   */
  public boolean isSizeSpecified() {
    return (width != null) && (height != null);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    
    if (obj.getClass() != Thumbnail.class ) {
      return false;
    }
    
    Thumbnail that = (Thumbnail) obj;
    return equalsPossibleNulls(this.width, that.width) 
        && equalsPossibleNulls(this.height, that.height)
        && equalsPossibleNulls(this.url, that.url); 
  }
    
  @Override
  public int hashCode() {
    return hashCodePossibleNulls(width) * hashCodePossibleNulls(height) * 37 
        + hashCodePossibleNulls(url)* 31;
  }
  
  @Override
  public String toString() {
    return new StringBuilder("Thumbnail: ")
        .append(width).append("x").append(height)
        .append(" ").append(url).toString();
  }
  
  private static final <T> boolean equalsPossibleNulls(T a, T b) { 
    return a == null ? b == null : a.equals(b); 
  }

  private static final <T> int hashCodePossibleNulls(T a) {
    return a == null ? 41 : a.hashCode();
  }
}
