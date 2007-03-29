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


package com.google.gdata.data.introspection;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The Workspace class defines the basic Java object model 
 * representation and XML parsing/generation support for an
 * APP workspace.
 *
 * 
 */
public class Workspace extends ExtensionPoint {

  public Workspace(String title) {
    this.title = title;
  }

  /** The title of the workspace */
  private String title;
  public String getTitle() { return title; }

  /** The list of collections associated with the workspace */
  List<Collection> collections = new ArrayList<Collection>();
  public List<Collection> getCollections() { return collections; }


  /**
   * Generates XML.
   *
   * @param   w
   *            output writer
   *
   * @throws  IOException
   */
  public void generate(XmlWriter w, ExtensionProfile extProfile) 
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = 
      new ArrayList<XmlWriter.Attribute>(1);
    attrs.add(new XmlWriter.Attribute("title", title));
    w.startElement(Namespaces.atomPubNs, "workspace", attrs, null);

    w.startRepeatingElement();
    for (Collection collection : collections) {
      collection.generate(w, extProfile);
    }
    w.endRepeatingElement();

    generateExtensions(w, extProfile);
    
    w.endElement(Namespaces.atomPubNs, "workspace");
  }


  /*
   * XmlParser ElementHandler for {@code app:workspace}
   */
  public class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile) throws IOException {
      super(extProfile, Workspace.class);
    }


    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.atomPub)) {

        if (localName.equals("collection")) {

          String colHref = attrs.getValue("", "href");
          if (colHref == null) {
            throw new ParseException(
              "href missing for app:collection element");
          }
          Collection collection = new Collection(colHref);
          collections.add(collection);
          return collection.new Handler(extProfile);

        } else {

          throw new ParseException("Unrecognized element: " +
                                   "namespace: " + namespace + 
                                   ",localName: " + localName);
        }

      } else {

        return super.getChildHandler(namespace, localName, attrs);

      }
    }
  }

  public void processEndElement() throws ParseException {
    if (collections.size() == 0) {
      throw new ParseException(
        "Workspace must contain at least one collection");
    }
  }
}
