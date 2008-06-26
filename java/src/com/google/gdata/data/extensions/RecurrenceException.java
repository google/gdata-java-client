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
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;


/**
 * GData schema extension describing individual exceptions to a
 * recurring event. It contains a collection of {@code
 * <gd:entryLink>} elements containing the exceptions.
 *
 * 
 */
public class RecurrenceException extends ExtensionPoint implements Extension {

  protected boolean isSpecialized;
  public boolean getSpecialized() { return isSpecialized; }
  public void setSpecialized(boolean v) { isSpecialized = v; }

  /** Nested event entry. */
  protected EntryLink entryLink;
  public EntryLink getEntryLink() { return entryLink; }
  public void setEntryLink(EntryLink v) { entryLink = v; }


  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(RecurrenceException.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("recurrenceException");
    desc.setRepeatable(true);
    return desc;
  }


  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    ArrayList<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    attrs.add(new XmlWriter.Attribute("specialized", String.valueOf(isSpecialized)));

    generateStartElement(w, Namespaces.gNs, "recurrenceException", attrs, null);

    if (entryLink != null) {
      entryLink.generate(w, extProfile);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gNs, "recurrenceException");
  }


  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs)
      throws ParseException, IOException {

    return new Handler(extProfile);
  }

  public void processAttribute(String namespace,
                               String localName,
                               String value)
      throws ParseException {
    if (namespace.equals("")) {
      if (localName.equals("specialized")) {
        if ("true".equals(value)) {
          isSpecialized = true;
        } else if ("false".equals(value)) {
          isSpecialized = false;
        } else {
          throw
            new ParseException("Invalid g:recurrenceException/@specialized");
        }
      }
    }
  }

  /** <g:recurrenceException> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {


    public Handler(ExtensionProfile extProfile)
        throws ParseException, IOException {

      super(extProfile, RecurrenceException.class);
    }

    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {


      if (namespace.equals(Namespaces.g)) {
        if (localName.equals("entryLink")) {
          entryLink = new EntryLink();
          return entryLink.getHandler(extProfile, namespace, localName, attrs);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}
