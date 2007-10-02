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
import com.google.gdata.util.common.xml.XmlWriter.Namespace;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;


/**
 * The Collection class defines the basic Java object model 
 * representation and XML parsing/generation support for an
 * APP collection.
 *
 * 
 */
public class Collection extends ExtensionPoint {

  private Namespace atomPubNs = Namespaces.getAtomPubNs();

  /**
   * Value of "accept" element used to indicate that a Collection
   * accepts "entry" objects.
   */
  public static final String ATOM_ENTRY_ACCEPT_VALUE = "entry";

  public Collection() {}

  public Collection(String href) {
    this(null, href, null);
  }

  public Collection(TextConstruct title, String href) {
    this(title, href, null);
  }

  public Collection(TextConstruct title, String href, String accept) {
    this.title = title;
    this.href = href;
    this.accept = accept;
  }

  /** The title of the collection */
  private TextConstruct title;
  public TextConstruct getTitle() { return title; }
  public void setTitle(TextConstruct title) { this.title = title; }


  /** The href of the collection */
  private String href;
  public String getHref() { return href; }
  public void setHref(String href) { this.href = href; }

  /** 
   * The media types accepted by the collection.  If null, then only 
   * Atom entries are supported. 
   */
  private String accept;
  public String getAccept() { return accept; }
  public void setAccept(String accept) { this.accept = accept; }


  /**
   * Generates XML.
   *
   * @param   w
   *            output writer
   *
   * @throws  IOException
   */
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(1);
    attrs.add(new XmlWriter.Attribute("href", href));
    w.startElement(atomPubNs, "collection", attrs, null);
    if (title != null) {
      title.generateAtom(w, "title");
    }
    if (accept != null) {
      w.simpleElement(atomPubNs, "accept", null, accept);
    }
    generateExtensions(w, extProfile);

    w.endElement(atomPubNs, "collection");
  }


  /*
   * XmlParser ElementHandler for {@code app:workspace}
   */
  public class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile) throws IOException {
      super(extProfile, Collection.class);
    }


    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.atom)) {

        if (localName.equals("title")) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (title != null) {
            throw new ParseException("Duplicate title.");
          }

          title = chi.textConstruct;
          return chi.handler;

        } else {
          throw new ParseException("Unrecognized element: " +
                                   "namespace: " + namespace + 
                                   ",localName: " + localName);
        }

      } else if (namespace.equals(atomPubNs.getUri())) {

        if (localName.equals("accept")) {
          return new AcceptHandler();
        } else {
          throw new ParseException("Unrecognized element: " +
                                   "namespace: " + namespace + 
                                   ",localName: " + localName);
        }

      } else {

        return super.getChildHandler(namespace, localName, attrs);

      }
    }

    /** app:accept element parser. */
    class AcceptHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (accept != null) {
          throw new ParseException("Duplicate accept elements.");
        }

        if (value == null) {
          throw new ParseException("accept must have a value.");
        }
        accept = value;
      }
    }
  }
}
