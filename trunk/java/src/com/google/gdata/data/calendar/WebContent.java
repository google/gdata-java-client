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


package com.google.gdata.data.calendar;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.calendar.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A "web content" extension -- here are some examples:
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
 * 
 * <atom:link rel="http://schemas.google.com/gCal/2005/webContent"
 *            title="DateTime Gadget (a classic!)"
 *            href="http://www.google.com/favicon.ico"
 *            type="application/x-google-gadgets+xml">
 *   <gCal:webContent
 *         width="300"
 *         height="136"
 *         url="http://google.com/ig/modules/datetime.xml">
 *     <gCal:webContentGadgetPref name="color" value="green" />
 *   </gCal:webContent>
 * </atom:link>
 * </xmp>
 */
public class WebContent implements Extension {

  /** value of the rel attribute for a link containing web content */
  public static final String REL =
      "http://schemas.google.com/gCal/2005/webContent";

  /** the name of the child element of the web content link */
  private static final String TYPE = "webContent";

  /** the name of a gadget pref element */
  private static final String GADGET_TYPE = "webContentGadgetPref";
  
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

  private Map<String,String> gadgetPrefs;
  
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
    Map<String,String> preferences = getGadgetPrefs();
    if (attrs.size() != 0) {
      if (preferences == null || preferences.isEmpty()) {
        writer.simpleElement(Namespaces.gCalNs, TYPE, attrs, null);
      } else {
        writer.startElement(Namespaces.gCalNs, TYPE, attrs, null);
        writer.startRepeatingElement();
        for (Map.Entry<String,String> pref : preferences.entrySet()) {
          List<XmlWriter.Attribute> prefAttrs =
              new ArrayList<XmlWriter.Attribute>();
          prefAttrs.add(new XmlWriter.Attribute("name", pref.getKey()));
          prefAttrs.add(new XmlWriter.Attribute("value", pref.getValue()));
          writer.simpleElement(Namespaces.gCalNs, GADGET_TYPE, prefAttrs, null);
        }
        writer.endRepeatingElement();
        writer.endElement(Namespaces.gCalNs, TYPE);
      }
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
      gadgetPrefs = null;
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) {
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

    @Override
    public ElementHandler getChildHandler(String namespace,
        String localName, Attributes attrs) {
      // handle element: <gCal:webContentGadgetPref name="" value="" />
      if (Namespaces.gCal.equals(namespace) && GADGET_TYPE.equals(localName)) {
        return new GadgetPrefHandler(this);
      } else {
        return null;
      }
    }
    
    void addGadgetPref(String name, String value) {
      if (gadgetPrefs == null) {
        gadgetPrefs = new HashMap<String,String>();
      }
      gadgetPrefs.put(name, value);
    }
    
  }
  
  class GadgetPrefHandler extends XmlParser.ElementHandler {
    
    private Handler parentHandler;
    
    private String name;
    
    private String value;
    
    GadgetPrefHandler(Handler parentHandler) {
      this.parentHandler = parentHandler;
      this.name = null;
      this.value = null;
    }
    
    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {
      if (!namespace.equals("")) {
        return;
      }
      if (localName.equals("name")) {
        this.name = value;
      } else if (localName.equals("value")) {
        this.value = value;
      }
    }    
    
    public void processEndElement() throws ParseException {
      if (this.name != null && this.value != null) {
        this.parentHandler.addGadgetPref(this.name, this.value);
      } else if (this.name != null) {
        throw new ParseException("name attribute defined but not value");
      } else if (this.value != null) {
        throw new ParseException("value attribute defined but not name");
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

  public Map<String,String> getGadgetPrefs() {
    return gadgetPrefs;
  }
  
  public void setGadgetPrefs(Map<String,String> gadgetPrefs) {
    this.gadgetPrefs = gadgetPrefs;
  }
  
  public String toString() {
    return "icon=" + getIcon() + ",title=" + getTitle() + ",type=" +
      getType() + ",width=" + getWidth() + ",height=" + getHeight() +
      ",url=" + getUrl();
  }

}
