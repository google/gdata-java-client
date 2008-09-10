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


package com.google.gdata.data.media.mediarss;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * An empty tag with a url, width and height attribute.
 *
 * 
 */
public abstract class AbstractMediaResource
    extends ExtensionPoint implements Extension {
  private String url;
  private int height;
  private int width;

  /** Prevents this class from being extended outside of this package. */
  AbstractMediaResource() {
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Subclasses can overwrite this method to add extra attributes.
   * @param generator used to output attributes.
   */
  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put("url", url);
    if (height > 0) {
      generator.put("height", height);
    }
    if (width > 0) {
      generator.put("width", width);
    }
  }

  /**
   * Subclasses can overwrite this method to parse extra
   * attributes.
   *
   * @param attrsHelper
   */
  @Override
  protected void consumeAttributes(AttributeHelper attrsHelper)
      throws ParseException {
    url = attrsHelper.consume("url", false);
    height = attrsHelper.consumeInteger("height", false);
    width = attrsHelper.consumeInteger("width", false);
  }
}
