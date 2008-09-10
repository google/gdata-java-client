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
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ExtensionVisitor;
import com.google.gdata.data.ExtensionVisitor.StoppedException;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GData schema extension describing a place (not necessarily a specific
 * geographical location).
 *
 * 
 */
public class Where extends ExtensionPoint implements Extension {

  /** Relation type. Describes the meaning of this location. */
  public static final class Rel {
    /** Place where the enclosing event occurs. */
    public static final String EVENT = null;
    /** Secondary location. */
    public static final String EVENT_ALTERNATE = Namespaces.gPrefix + "event.alternate";
    /** Nearby parking lot. */
    public static final String EVENT_PARKING = Namespaces.gPrefix + "event.parking";
  }

  /** Constructs an empty Where instance. */
  public Where() {}

  /** Constructs a new Where instance using the specified parameters. */
  public Where(String rel, String label, String valueString) {
    this.rel = rel;
    this.label = label;
    this.valueString = valueString;
  }

  /** Describes the meaning of this location. */
  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }

  /**
   * User-readable label that identifies this location in case multiple
   * locations may be present.
   */
  protected String label;
  public String getLabel() { return label; }
  public void setLabel(String v) { label = v; }

  /** Text description of the place. */
  protected String valueString;
  public String getValueString() { return valueString; }
  public void setValueString(String v) { valueString = v; }

  /** Nested person or venue (Contact) entry. */
  protected EntryLink<?> entryLink;
  public EntryLink<?> getEntryLink() { return entryLink; }
  public void setEntryLink(EntryLink<?> v) { entryLink = v; }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Where.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("where");
    desc.setRepeatable(true);
    return desc;
  }

  @Override
  protected void visitChildren(ExtensionVisitor ev) throws StoppedException {
    if (entryLink != null) {
      visitChild(ev, entryLink);
    }
    super.visitChildren(ev);
  }
  
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    if (rel != null) {
      attrs.add(new XmlWriter.Attribute("rel", rel));
    }

    if (label != null) {
      attrs.add(new XmlWriter.Attribute("label", label));
    }

    if (valueString != null) {
      attrs.add(new XmlWriter.Attribute("valueString", valueString));
    }

    generateStartElement(w, Namespaces.gNs, "where", attrs, null);

    if (entryLink != null) {
      entryLink.generate(w, extProfile);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "where");
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <g:where> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile) {
      super(extProfile, Where.class);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) {
      if (namespace.equals("")) {
        if (localName.equals("rel")) {
          rel = value;
        } else if (localName.equals("label")) {
          label = value;
        } else if (localName.equals("valueString")) {
          valueString = value;
        }
      }
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {
      if (namespace.equals(Namespaces.g)) {
        if (localName.equals("entryLink")) {
          entryLink = new EntryLink<BaseEntry<?>>();
          return entryLink.getHandler(extProfile, namespace, localName, attrs);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}
