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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * GData schema extension describing an RFC 2445 recurrence rule.
 *
 * 
 */
public class Recurrence implements Extension {

  /** Recurrence rule. Contains DTSTART, RRULE, RDATE, EXRULE, EXDATE, etc. */
  protected String value;
  public String getValue() { return value; }
  public void setValue(String v) { value = v; }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Recurrence.class);
    desc.setNamespace(Namespaces.gNs);
    desc.setLocalName("recurrence");
    desc.setRepeatable(false);
    return desc;
  }

  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    w.simpleElement(Namespaces.gNs, "recurrence", null, value);
  }

  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs) {
    return new Handler();
  }

  /** <g:recurrence> parser. */
  private class Handler extends XmlParser.ElementHandler {

    @Override
    public void processEndElement() {

      // Delete leading and trailing linebreaker/whitespace
      // to avoid ical parsing error.
      Recurrence.this.value = Handler.this.value.trim();
    }
  }
}

