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
import com.google.gdata.data.ExtensionVisitor;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.Version;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * The Workspace class defines the basic Java object model
 * representation and XML parsing/generation support for an
 * AtomPub workspace.
 *
 * The implementation is versioned to support the AtomPub draft version 9
 * introspection format (used for the GData v1 implementation) as well
 * as the final RFC5023 format (used for all other versions).  The key
 * difference between the two is that draft used an attribute for the
 * workspace title where the final version uses an atom:title element.
 * elements.
 *
 * 
 */
public class Workspace extends ExtensionPoint implements IWorkspace {

  // Locally case version-dependent information.   The assumption here is that
  // a single instance isn't used with multiple version of the protocol.
  private Version coreVersion = Service.getVersion();
  private XmlNamespace atomPubNs = Namespaces.getAtomPubNs();

  public Workspace() {}

  public Workspace(TextConstruct title) {
    this.title = title;
  }

  /** Title of workspace */
  private TextConstruct title;
  public TextConstruct getTitle() { return title; }
  public void setTitle(TextConstruct v) { title = v; }

  /** The list of collections associated with the workspace */
  private List<Collection> collectionList = new ArrayList<Collection>();
  public List<Collection> getCollections() { return collectionList; }
  public void addCollection(Collection coll) { collectionList.add(coll); }

  public Collection addCollection(String collectionUri, String title,
      String... acceptedTypes) {
    Collection collection = new Collection(collectionUri,
        new PlainTextConstruct(title), acceptedTypes);
    addCollection(collection);
    return collection;
  }
  
  @Override
  protected void visitChildren(ExtensionVisitor ev)
      throws ExtensionVisitor.StoppedException {
    
    // Add nested collections to the visitor pattern
    for (Collection collection : collectionList) {
      this.visitChild(ev, collection);
    }
    super.visitChildren(ev);
  } 
  
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile) 
      throws IOException {

    ArrayList<Attribute> attrs = new ArrayList<Attribute>();
    if (coreVersion.isCompatible(Service.Versions.V1)) {
      attrs.add(new Attribute("title", title.getPlainText()));
    }
    w.startElement(atomPubNs, "workspace", attrs, null);

    if (!coreVersion.isCompatible(Service.Versions.V1)) {
      title.generateAtom(w, "title");
    }

    w.startRepeatingElement();
    for (Collection collection : collectionList) {
      collection.generate(w, extProfile);
    }
    w.endRepeatingElement();

    generateExtensions(w, extProfile);

    w.endElement(atomPubNs, "workspace");
  }
  
  @Override
  public void consumeAttributes(AttributeHelper attrHelper) 
      throws ParseException {
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
      super(extProfile, Workspace.class, attrs);
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(atomPubNs.getUri())) {

        if (localName.equals("collection")) {


          Collection collection = new Collection();
          addCollection(collection);
          return collection.new Handler(extProfile, attrs);
        }
      } else if (namespace.equals(Namespaces.atom)) {
        if (localName.equals("title") && 
            !coreVersion.isCompatible(Service.Versions.V1)) {

          if (title != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateTitle);
          }

          TextConstruct.ChildHandlerInfo chi =
              TextConstruct.getChildHandler(attrs);
          title = chi.textConstruct;
          return chi.handler;
        }
      }
      return super.getChildHandler(namespace, localName, attrs);
    }
  }

  public void processEndElement() throws ParseException {

    if (title == null) {
      throw new ParseException(
        CoreErrorDomain.ERR.workspaceTitleRequired);
    }
  }
}
