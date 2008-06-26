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
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;


/**
 * GData schema extension describing a comments feed.
 *
 * 
 */
public class Comments extends ExtensionPoint implements Extension {


  /** Comments feed link. */
  protected FeedLink feedLink;
  public FeedLink getFeedLink() { return feedLink; }
  public void setFeedLink(FeedLink v) { feedLink = v; }


  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Comments.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("comments");
    desc.setRepeatable(false);
    return desc;
  }


  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    generateStartElement(w, Namespaces.gNs, "comments", null, null);

    if (feedLink != null) {
      feedLink.generate(w, extProfile);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "comments");
  }


  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs)
      throws ParseException, IOException {

    return new Handler(extProfile);
  }


  /** <g:comments> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile)
        throws ParseException, IOException {

      super(extProfile, Comments.class);
    }


    public void processEndElement() throws ParseException {

      if (feedLink == null) {
        throw new ParseException("g:comments/g:feedLink is required.");
      }

      super.processEndElement();
    }


    public ElementHandler getChildHandler(String namespace, String localName,
                                          Attributes attrs) 
      throws ParseException, IOException {

      if (namespace.equals(Namespaces.g)) {
        if (localName.equals("feedLink")) {
          feedLink = new FeedLink();
          return (ElementHandler)feedLink.getHandler(extProfile, namespace, 
                                                     localName, attrs);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}

