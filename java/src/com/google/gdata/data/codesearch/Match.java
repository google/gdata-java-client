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


package com.google.gdata.data.codesearch;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.codesearch.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * GData schema extension describing a match in the source.
 * <gcs:match lineNumber="23" type="text/html">
 *   found &lt;b&gt; query &lt;/b&gt;
 * </gcs:match>
 *
 * 
 */
public class Match extends ExtensionPoint implements Extension {
  public static final String EXTENSION_MATCH = "match";
  public static final String ATTRIBUTE_LINENUMBER = "lineNumber";

  /** Property userName. */
  protected String lineNumber;
  public String getLineNumber() {
    return lineNumber;
  }

  /** Embedded line text. */
  protected HtmlTextConstruct lineText;
  public HtmlTextConstruct getLineText() {
    return lineText;
  }

  /**
   * @return Description of this extension
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Match.class);
    desc.setNamespace(Namespaces.gCSNs);
    desc.setLocalName(EXTENSION_MATCH);
    desc.setRepeatable(true);
    return desc;
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    ArrayList<XmlWriter.Attribute> attributes =
      new ArrayList<XmlWriter.Attribute>();
    if (this.lineNumber != null) {
      attributes.add(new XmlWriter.Attribute(ATTRIBUTE_LINENUMBER,
                                             this.lineNumber));
      attributes.add(new XmlWriter.Attribute("type",
                                             "text/html"));
    }
    w.simpleElement(Namespaces.gCSNs, EXTENSION_MATCH,
                    attributes, lineText.getPlainText());
  }

  @Override
  public ElementHandler getHandler(ExtensionProfile extProfile,
                                   String namespace, String localName,
                                   Attributes attrs) {
    return new Handler(extProfile);
  }

  /** <c:match> parser. */
  private class Handler extends ExtensionPoint.ExtensionHandler {
    public Handler(ExtensionProfile extProfile) {

      super(extProfile, Match.class);
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) {

      if ("".equals(namespace)) {
        if (ATTRIBUTE_LINENUMBER.equals(localName)) {
          lineNumber = value;
        }
      }
    }

    @Override
    public void processEndElement() throws ParseException {
      if (lineNumber == null) {
        throw new ParseException(Namespaces.gCS + EXTENSION_MATCH + "/@" +
                                 ATTRIBUTE_LINENUMBER + " is required.");
      }
      lineText = new HtmlTextConstruct(value);
    }
  }
}
