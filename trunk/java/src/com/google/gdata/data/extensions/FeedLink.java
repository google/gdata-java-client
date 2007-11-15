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


package com.google.gdata.data.extensions;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.BaseFeed.FeedHandler;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Feed;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;


/**
 * GData schema extension describing a nested feed link.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = "feedLink",
    isRepeatable = true)
public class FeedLink<F extends BaseFeed>
    extends ExtensionPoint implements Extension {

  /**
   * Rel value that describes the type of feed link.
   */
  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }

  /**
   * Feed URI.
   */
  protected String href;
  public String getHref() { return href; }
  public void setHref(String v) { href = v; }


  /** Read only flag. */
  protected boolean readOnly = false;
  public boolean getReadOnly() { return readOnly; }
  public void setReadOnly(boolean v) { readOnly = v; }


  /** Count hint. */
  protected Integer countHint;
  public Integer getCountHint() { return countHint; }
  public void setCountHint(Integer v) { countHint = v; }


  /** Nested feed (optional). */
  protected BaseFeed feed;
  public BaseFeed getFeed() { return feed; }
  public void setFeed(BaseFeed v) { feed = v; }


  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(FeedLink.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("feedLink");
    return desc;
  }


  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (href != null) {
      attrs.add(new XmlWriter.Attribute("href", href));
    }

    if (readOnly) {
      attrs.add(new XmlWriter.Attribute("readOnly", "true"));
    }

    if (countHint != null) {
      attrs.add(new XmlWriter.Attribute("countHint", countHint.toString()));
    }

    generateStartElement(w, Namespaces.gNs, "feedLink", attrs, null);

    if (feed != null) {
      ExtensionProfile nestedExtProfile = extProfile.getFeedLinkProfile();
      if (nestedExtProfile == null) {
        nestedExtProfile = extProfile;
      }
      feed.generateAtom(w, nestedExtProfile);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "feedLink");
  }


  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs)
      throws ParseException, IOException {

    return new Handler(extProfile);
  }


  /** <gd:feedLink> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile)
        throws ParseException, IOException {

      super(extProfile, FeedLink.class);
    }


    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {

      if (namespace.equals("")) {

        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("href")) {
          href = value;
        } else if (localName.equals("readOnly")) {
          readOnly = value.equals("true");
        } else if (localName.equals("countHint")) {
          try {
            countHint = Integer.valueOf(value);
          } catch (NumberFormatException e) {
            throw new ParseException("Invalid gd:feedLink/@countHint.", e);
          }
        }
      }
    }


    public ElementHandler getChildHandler(String namespace,
                                          String localName,
                                          Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.atom)) {
        if (localName.equals("feed")) {
          ExtensionProfile nestedExtProfile = extProfile.getFeedLinkProfile();
          if (nestedExtProfile == null) {
            nestedExtProfile = extProfile;
          }
          feed = new Feed();
          return (ElementHandler)feed.new FeedHandler(nestedExtProfile);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}
