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
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Entry;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ExtensionVisitor;
import com.google.gdata.data.Link;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The EntryLink class defines the object model for a link entity that refers to
 * a GData entry.   The entry content may be included inline via child elements 
 * of the entry link or only included by reference.
 * 
 * @param <E>   Nested entry type.
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = "entryLink")
public class EntryLink<E extends BaseEntry<?>> extends Link {

  /**
   * Constructs an entry link that points to an {@link Entry}.
   */
  @SuppressWarnings("unchecked")
  public EntryLink() {
    this((Class<E>) Entry.class);
  }

  /**
   * Constructs an entry link that points to the given entry type.
   *
   * @param entryClass  Entry class.
   */
  public EntryLink(Class<E> entryClass) {
    this.entryClass = entryClass;
  }

  /** Read only flag. */
  protected boolean readOnly;
  public boolean getReadOnly() { return readOnly; }
  public void setReadOnly(boolean v) { readOnly = v; }

  /** Nested entry (optional). */
  protected BaseEntry<?> entry;
  @SuppressWarnings("unchecked")
  public E getEntry() { return (E) entry; }
  public void setEntry(E v) { entry = v; }

  /** Nested entry class. */
  protected final Class<E> entryClass;
  public Class<E> getEntryClass() { return entryClass; }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    return ExtensionDescription.getDefaultDescription(EntryLink.class);
  }
  
  @Override
  public String getType() {
    return ContentType.getAtomEntry().toString();
  }

  @Override
  protected void visitChildren(ExtensionVisitor ev) 
    throws ExtensionVisitor.StoppedException {
    
    if (entry != null) {
      this.visitChild(ev, entry);
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

  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace,
                                   String localName,
                                   Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <gd:entryLink> parser. */
  private class Handler extends Link.AtomHandler {

    public Handler(ExtensionProfile extProfile) {
      super(extProfile, EntryLink.class);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) throws ParseException {
      if (namespace.equals("")) {
        if (localName.equals("readOnly")) {
          readOnly = value.equals("true");
        } else {
          super.processAttribute(namespace, localName, value);
        }
      }
    }

    @Override
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
          try {
            entry = entryClass.newInstance();
          } catch (IllegalAccessException iae) {
            throw new ParseException(
                CoreErrorDomain.ERR.cantCreateEntry);
          } catch (InstantiationException ie) {
            throw new ParseException(
                CoreErrorDomain.ERR.cantCreateEntry);
          }
          return entry.new AtomHandler(nestedExtProfile);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}
