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
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseEntry.AtomHandler;
import com.google.gdata.data.Entry;
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
 * GData schema extension describing a nested entry link.
 *
 * 
 */
public class EntryLink extends ExtensionPoint implements Extension {


  /**
   * Rel value that describes the type of entry link.
   */
  protected String rel;
  public String getRel() { return rel; }
  public void setRel(String v) { rel = v; }

  /**
   * Entry URI.
   */
  protected String href;
  public String getHref() { return href; }
  public void setHref(String v) { href = v; }


  /** Read only flag. */
  protected boolean readOnly;
  public boolean getReadOnly() { return readOnly; }
  public void setReadOnly(boolean v) { readOnly = v; }


  /** Nested entry (optional). */
  protected BaseEntry entry;
  public BaseEntry getEntry() { return entry; }
  public void setEntry(BaseEntry v) { entry = v; }


  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(EntryLink.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("entryLink");
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

    generateStartElement(w, Namespaces.gNs, "entryLink", attrs, null);

    if (entry != null) {
      ExtensionProfile nestedExtProfile = extProfile.getEntryLinkProfile();
      if (nestedExtProfile == null) {
        nestedExtProfile = extProfile;
      }
      entry.generateAtom(w, nestedExtProfile);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "entryLink");
  }


  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs)
      throws ParseException, IOException {

    return new Handler(extProfile);
  }


  /** <gd:entryLink> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile)
        throws ParseException, IOException {
      super(extProfile, EntryLink.class);
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
        }
      }
    }


    public ElementHandler getChildHandler(String namespace,
                                          String localName,
                                          Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.atom)) {
        if (localName.equals("entry")) {
          ExtensionProfile nestedExtProfile = extProfile.getEntryLinkProfile();
          if (nestedExtProfile == null) {
            nestedExtProfile = extProfile;
          }
          entry = new Entry();
          return (ElementHandler)entry.new AtomHandler(nestedExtProfile);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}
