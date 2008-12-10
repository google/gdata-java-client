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


package com.google.gdata.util;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * Arbitrary self-contained block of XML.
 *
 * 
 */
public class XmlBlob {


  /**
   * Initial {@code xml:lang} value. This value is typically inherited through
   * the XML tree. The blob itself may contain overrides.
   * <p>
   *
   * See http://www.w3.org/TR/REC-xml/#sec-lang-tag for more information.
   */
  protected String lang;
  public String getLang() { return lang; }
  public void setLang(String v) { lang = v; }


  /**
   * Initial {@code xml:base} value. This value is typically inherited through
   * the XML tree. The blob itself may contain overrides.
   * <p>
   *
   * See http://www.cafeconleche.org/books/xmljava/chapters/ch03s03.html for
   * more information.
   */
  protected String base;
  public String getBase() { return base; }
  public void setBase(String v) { base = v; }


  /**
   * Namespace declarations inherited when this blob was parsed but used
   * within it.
   */
  protected LinkedList<XmlNamespace> namespaces =
    new LinkedList<XmlNamespace>();

  public List<XmlNamespace> getNamespaces() {
    return namespaces;
  }

  public boolean addNamespace(XmlNamespace namespace) {
    return namespaces.add(namespace);
  }


  /**
   * Contents of the blob. Depending on how the blob was parsed, it may contain
   * top-level text() nodes mixed together with child elements.
   */
  protected String blob;
  public String getBlob() { return blob; }
  public void setBlob(String v) { blob = v; }


  /**
   * Contains strings from {@code blob} for full-text indexing.
   * Valid only when this blob comes from {@link XmlParser}.
   */
  protected String fullText;
  public String getFullText() { return fullText; }
  public void setFullText(String v) { fullText = v; }


  /** Generates an element opening tag containing an XML blob. */
  public static void startElement(XmlWriter w,
                                  XmlNamespace namespace,
                                  String elementName,
                                  XmlBlob xml,
                                  Collection<XmlWriter.Attribute>
                                    additionalAttrs,
                                  Collection<XmlNamespace> additionalNs)
      throws IOException {

    Collection<XmlWriter.Attribute> attrs;
    Collection<XmlNamespace> namespaces;

    if (xml != null) {

      String lang = xml.getLang();
      String base = xml.getBase();

      if (lang != null || base != null) {

        attrs = new ArrayList<XmlWriter.Attribute>();

        if (additionalAttrs != null) {
          attrs.addAll(additionalAttrs);
        }

        if (lang != null) {
          attrs.add(new XmlWriter.Attribute("xml:lang", lang));
        }

        if (base != null) {
          attrs.add(new XmlWriter.Attribute("xml:base", base));
        }

      } else { assert lang == null && base == null;
        attrs = additionalAttrs;
      }

      List<XmlNamespace> blobNamespaces = xml.getNamespaces();
      int additionalNsSize = (additionalNs == null ? 0 : additionalNs.size());

      if (blobNamespaces.size() == 0 && additionalNsSize == 0) {

        namespaces = null;

      } else {

        namespaces = new ArrayList<XmlNamespace>(additionalNsSize +
                                                 blobNamespaces.size());

        // Blob namespaces have to go first, because we depend on their aliases
        // being preserved. If an additional namespace has a conflicting alias,
        // we want the additional namespace to get an arbitrarily generated
        // alias, not the blob's.

        for (XmlNamespace blobNs : blobNamespaces) {
          namespaces.add(new XmlNamespace(blobNs.getAlias(),
                                          blobNs.getUri()));
        }

        if (additionalNs != null) {
          namespaces.addAll(additionalNs);
        }
      }

    } else { assert xml == null;
      attrs = additionalAttrs;
      namespaces = additionalNs;
    }

    w.startElement(namespace, elementName, attrs, namespaces);
  }


  /** Generates an element closing tag containing an XML blob. */
  public static void endElement(XmlWriter w,
                                XmlNamespace namespace,
                                String elementName,
                                XmlBlob xml) throws IOException {

    if (xml != null && xml.getBlob() != null) {
      w.innerXml(xml.getBlob());
    }

    w.endElement(namespace, elementName);
  }
}
