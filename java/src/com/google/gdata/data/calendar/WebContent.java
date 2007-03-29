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


package com.google.gdata.data.calendar;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A "web content" extension, which looks something like:
 * <xmp>
 * <atom:link rel="http://schemas.google.com/gCal/2005/webContent"
 *            title="World Cup"
 *            href="http://www.google.com/calendar/images/google-holiday.gif"
 *            type="image/gif">
 *   <gCal:webContent
 *         width="276"
 *         height="120"
 *         url="http://www.google.com/logos/worldcup06.gif" />
 * </atom:link>
 * </xmp>
 */
public class WebContent implements Extension {

  /** value of the rel attribute for a link containing web content */
  public static final String REL =
      "http://schemas.google.com/gCal/2005/webContent";

  /** the name of the child element of the web content link */
  private static final String TYPE = "webContent";

  private static final ExtensionDescription EXTENSION_DESCRIPTION;

  static {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(WebContent.class);
    desc.setNamespace(Namespaces.gCalNs);
    desc.setLocalName(TYPE);
    desc.setRepeatable(false);
    EXTENSION_DESCRIPTION = desc;
  }

  private String width;

  private String height;

  private String url;

  // webContentLink should never be null
  private Link webContentLink;

  public WebContent() {
    width = null;
    height = null;
    url = null;
    setLink(new Link(REL, null, null));
  }

  public static ExtensionDescription getDefaultDescription() {
    return EXTENSION_DESCRIPTION;
  }

  /**
   * Done as a post-process on a CalendarEventEntry to see if it contains
   * the appropriate XML to signify that the entry contains web content.
   * If so, the WebContent property of the entry is set.
   */
  public static void updateWebContent(CalendarEventEntry entry)
      throws ParseException {
    Link wcLink = entry.getWebContentLink();
    if (wcLink == null) {
      // there is no web content info, so clear out any old web content info
      entry.setWebContent(null);
    } else {
      WebContent wc = entry.getWebContent();
      wc.setLink(wcLink);
      entry.setWebContent(wc);
    }
  }

  public void generate(XmlWriter writer, ExtensionProfile profile)
      throws IOException {
    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();
    if (getWidth() != null) {
      attrs.add(new XmlWriter.Attribute("width", getWidth()));
    }
    if (getHeight() != null) {
      attrs.add(new XmlWriter.Attribute("height", getHeight()));
    }
    if (getUrl() != null) {
      attrs.add(new XmlWriter.Attribute("url", getUrl()));
    }
    if (attrs.size() != 0) {
      writer.simpleElement(Namespaces.gCalNs, TYPE, attrs, null);
    }
  }

  public XmlParser.ElementHandler getHandler(ExtensionProfile profile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs)
      throws ParseException, IOException {
    return new Handler();
  }

  class Handler extends XmlParser.ElementHandler {

    public Handler() {
      // clear all existing values
      width = null;
      height = null;
      url = null;
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {
      if (!namespace.equals("")) {
        return;
      }
      if (localName.equals("width")) {
        setWidth(value);
      } else if (localName.equals("height")) {
        setHeight(value);
      } else if (localName.equals("url")) {
        setUrl(value);
      }
    }

  }

  // restrict Link getter and setter to default access
  Link getLink() {
    return webContentLink;
  }

  void setLink(Link link) {
    if (webContentLink != null) {
      webContentLink.removeExtension(WebContent.class);
    }
    webContentLink = link;
    webContentLink.setExtension(this);
  }

  ////// standard getters and setters //////

  public String getIcon() {
    return webContentLink.getHref();
  }

  public void setIcon(String icon) {
    webContentLink.setHref(icon);
  }

  public String getTitle() {
    return webContentLink.getTitle();
  }

  public void setTitle(String title) {
    webContentLink.setTitle(title);
  }

  public String getType() {
    return webContentLink.getType();
  }

  public void setType(String type) {
    webContentLink.setType(type);
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String toString() {
    return "icon=" + getIcon() + ",title=" + getTitle() + ",type=" +
      getType() + ",width=" + getWidth() + ",height=" + getHeight() +
      ",url=" + getUrl();
  }

}
