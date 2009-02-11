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


package com.google.gdata.data.introspection;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.Service;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.Reference;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.Version;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The Collection class defines the basic Java object model
 * representation and XML parsing/generation support for an
 * APP collection.
 *
 * The implementation is versioned to support the AtomPub draft version 9
 * introspection format (used for the GData v1 implementation) as well
 * as the final RFC5023 format (used for all other versions).  The key
 * difference between the two is that draft used an attribute for the
 * collection title and a comma-delimited list for accepted MIME types,
 * where the final version uses atom:title and repeating app:accept
 * elements.
 *
 * 
 */
public class Collection extends ExtensionPoint implements Reference,
    ICollection {

  private Version coreVersion = Service.getVersion();
  private XmlNamespace atomPubNs = Namespaces.getAtomPubNs();

  public Collection() {
  }

  public Collection(String href) {
    this(href, null);
  }

  public Collection(String href, TextConstruct title) {
    this.href = href;
    this.title = title;
  }

  public Collection(String href, TextConstruct title, String ... accepts) {
    this(href, title);
    this.accepts = Arrays.asList(accepts);
  }

  /**
   * Returns the accept type used in Atom service document to represent
   * the fact that the service accepts Atom entry posting.
   */
  public static String getAtomEntryAcceptType() {

    // Earlier versions of the AtomPub spec (upon which GData v1 was
    // based) used a hardcoded constant, later versions use the Atom
    // entry MIME type.
    if (Service.getVersion().isCompatible(Service.Versions.V1)) {
      return "entry";
    }
    // We don't use the return value of ContentType.getAtomEntry because it
    // contains charset encoding information that is misleading in this
    // context, as we don't require utf-8 encoding of POSTed entries, we'll
    // just always return them with this encoding.
    return "application/atom+xml;type=entry";
  }

  /** The title of the collection */
  private TextConstruct title;
  public TextConstruct getTitle() { return title; }
  public void setTitle(TextConstruct title) { this.title = title; }


  /** The href of the collection */
  private String href;
  public String getHref() { return href; }
  public void setHref(String href) { this.href = href; }
  
  /** The mime type of the collection */
  public String getType() {
    return ContentType.getAtomFeed().toString();
  }

  /**
   * The media types accepted by the collection.  If null, then only
   * Atom entries are supported.
   */
  private List<String> accepts = new ArrayList<String>();
  public List<String> getAcceptList() { return accepts; }
  public void addAccept(String accept) {
    accepts.add(accept);
  }

  private List<Categories> categoriesList = new ArrayList<Categories>();
  public List<Categories> getCategoriesList() { return categoriesList; }
  public void addCategories(Categories c) {
    categoriesList.add(c);
  }

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
    if (coreVersion.isCompatible(Service.Versions.V1)) {
      attrs.add(new Attribute("title", title.getPlainText()));
    }
    attrs.add(new XmlWriter.Attribute("href", href));
    w.startElement(atomPubNs, "collection", attrs, null);

    if (coreVersion.isCompatible(Service.Versions.V1)) {
      if (accepts != null) {
        StringBuffer acceptBuf = new StringBuffer();
        for (String accept : accepts) {
          if (acceptBuf.length() != 0) {
            acceptBuf.append(',');
          }
          acceptBuf.append(accept);
        }
        w.simpleElement(atomPubNs, "accept", null, acceptBuf.toString());
      }
    } else {
      if (title != null) {
        title.generateAtom(w, "title");
      }
      for (String accept : accepts) {
        if (accepts != null) {
          w.simpleElement(atomPubNs, "accept", null, accept);
        }
      }
      for (Categories categories : getCategoriesList()) {
        categories.generate(w, extProfile);
      }
    }
    generateExtensions(w, extProfile);

    w.endElement(atomPubNs, "collection");
  }

  @Override
  public void consumeAttributes(AttributeHelper attrHelper)
      throws ParseException {
    href = attrHelper.consume("href", true);
    if (coreVersion.isCompatible(Service.Versions.V1)) {
      String titleAttr = attrHelper.consume("title", true);
      title = new PlainTextConstruct(titleAttr);
    }
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile p,
      String namespace, String localName, Attributes attrs) {
    return new Handler(p, attrs);
  }

  /**
   * XmlParser ElementHandler for {@code app:workspace}
   */
  public class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile, Attributes attrs) {
      super(extProfile, Collection.class, attrs);
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.atom)) {

        if (localName.equals("title")  &&
            !coreVersion.isCompatible(Service.Versions.V1)) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (title != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateTitle);
          }

          title = chi.textConstruct;
          return chi.handler;

        }
      } else if (namespace.equals(atomPubNs.getUri())) {

        if (localName.equals("accept")) {
          return new AcceptHandler();
        }
        if (localName.equals("categories")) {
          Categories newCategories = new Categories();
          addCategories(newCategories);
          return newCategories.new Handler(extProfile, attrs);
        }
      }
      return super.getChildHandler(namespace, localName, attrs);
    }


    @Override
    public void processEndElement() throws ParseException {

      super.processEndElement();
      if (title == null) {
        throw new ParseException(
          CoreErrorDomain.ERR.collectionTitleRequired);
      }
    }

    /** app:accept element parser. */
    class AcceptHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() {

        if (value == null) {
          value = "";
        }
        if (coreVersion.isCompatible(Service.Versions.V1)) {
          accepts = Arrays.asList(value.split(","));
        } else {
          addAccept(value);
        }
      }
    }
  }
}
