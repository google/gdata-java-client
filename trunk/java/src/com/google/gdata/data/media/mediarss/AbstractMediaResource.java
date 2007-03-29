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


package com.google.gdata.data.media.mediarss;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An empty tag with a url, width and height attribute.
 *
 * 
 */
public abstract class AbstractMediaResource 
    extends ExtensionPoint implements Extension {
  private final String tagName;
  private String url;
  private int height;
  private int width;

  AbstractMediaResource(String tagName) {
    this.tagName = tagName;
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

  public final void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();
    ExtensionUtils.addAttribute(attrs, "url", url);
    ExtensionUtils.addAttribute(attrs, "height", height);
    ExtensionUtils.addAttribute(attrs, "width", width);
    addAttributes(attrs);
    w.startElement(MediaRssNamespace.NS, tagName, attrs, null);
    generateExtensions(w, extProfile);
    w.endElement();
  }

  /**
   * Subclasses can overwrite this method to add extra attributes.
   * @param attrs
   */
  protected void addAttributes(List<XmlWriter.Attribute> attrs) {
  }

  /**
   * Subclasses can overwrite this method to parse extra
   * attributes.
   *
   * @param attrsHelper
   */
  protected void consumeAttributes(AttributeHelper attrsHelper)
      throws ParseException {
  }

  final public XmlParser.ElementHandler getHandler(
      final ExtensionProfile extProfile, String namespace, String localName,
      Attributes attrs) throws ParseException, IOException {
    final AttributeHelper attrsHelper = new AttributeHelper(attrs);
    url = attrsHelper.consume("url", false);
    height = attrsHelper.consumeInteger("height", false);
    width = attrsHelper.consumeInteger("width", false);
    consumeAttributes(attrsHelper);

    return new XmlParser.ElementHandler() {
      @Override
      public XmlParser.ElementHandler getChildHandler(String namespace,
          String localName, Attributes attrs)
          throws ParseException, IOException {
        return getExtensionHandler(extProfile, getClass(), namespace, localName,
            attrs);
      }

      @Override
      public void processEndElement() throws ParseException {
        super.processEndElement();
        attrsHelper.assertAllConsumed();
      }
    };
  }

}
