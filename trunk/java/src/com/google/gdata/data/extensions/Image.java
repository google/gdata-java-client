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


package com.google.gdata.data.extensions;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GData schema extension describing an image.
 *
 * 
 */
public class Image extends ExtensionPoint implements Extension {

  /**
   * Image type. Describes the type of image.
   */
  public static final class Rel {
    /** g.full: Full sized image */
    public static final String FULL = null;
    /** g.scalable: Image can be requested in various sizes */
    public static final String SCALABLE = Namespaces.gPrefix + "scalable";
    /** g.resized: Image resized for on-screen viewing */
    public static final String RESIZED = Namespaces.gPrefix + "resized";
    /** g.thumbnail: Small thumbnail */
    public static final String THUMBNAIL = Namespaces.gPrefix + "thumbnail";
    /** g.raw: Raw image */
    public static final String RAW = Namespaces.gPrefix + "raw";
  }

  protected String rel;

  public String getRel() {
    return rel;
  }

  public void setRel(String v) {
    rel = v;
  }

  /**
   * Image width in pixels.
   */
  protected Integer width;

  public int getWidth() {
    return width;
  }

  public void setWidth(int w) {
    width = w;
  }

  /**
   * Image height in pixels.
   */
  protected Integer height;

  public int getHeight() {
    return height;
  }

  public void setHeight(int w) {
    height = w;
  }

  /**
   * URI where the image can be downloaded.
   */
  protected String src;

  public String getSrc() {
    return src;
  }

  public void setSrc(String s) {
    src = s;
  }

  /**
   * Returns the suggested extension description.
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Image.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("image");
    desc.setRepeatable(true);
    return desc;
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (width != null) {
      attrs.add(new XmlWriter.Attribute("width", width.toString()));
    }

    if (height != null) {
      attrs.add(new XmlWriter.Attribute("height", height.toString()));
    }

    if (src != null) {
      attrs.add(new XmlWriter.Attribute("src", src));
    }

    generateStartElement(w, Namespaces.gNs, "image", attrs, null);

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "image");
  }

  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <g:image> parser */
  private class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) {
      super(extProfile, Image.class);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) throws NumberFormatException {

      if (namespace.equals("")) {
        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("width")) {
          width = Integer.parseInt(value);
        } else if (localName.equals("height")) {
          height = Integer.parseInt(value);
        } else if (localName.equals("src")) {
          src = value;
        }
      }
    }

    @Override
    public void processEndElement() throws ParseException {

      if (src == null) {
        throw new ParseException(
            CoreErrorDomain.ERR.missingSrcAttribute);
      }
    }
  }
}
