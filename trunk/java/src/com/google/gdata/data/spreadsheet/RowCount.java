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


package com.google.gdata.data.spreadsheet;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;


/**
 * GData schema extension describing a spreadsheet's row count.
 *
 * 
 */
public class RowCount implements Extension {

  /** The name of the XML tag. */
  public static final String TAG_NAME = "rowCount";
  
  /** The count. */
  private int count = -1;
  
  /**
   * Initializes to blank for XML parsing.
   */
  public RowCount() {
  }
  
  /**
   * Initializes to a particular count.
   */
  public RowCount(int count) {
    this.count = count;
  }
  
  /**
   * Gets the total count.
   */
  public int getCount() {
    return count;
  }
  
  /**
   * Returns the suggested extension description.
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(RowCount.class);
    desc.setNamespace(Namespaces.gSpreadNs);
    desc.setLocalName(TAG_NAME);
    return desc;
  }
  
  /**
   * Writes this cell as XML, omitting any unspecified fields.
   */
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    w.simpleElement(Namespaces.gSpreadNs, TAG_NAME, null,
        String.valueOf(count));
  }
  
  /**
   * Yields an XML handler for parsing a Cell element.
   */
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs)
      throws ParseException, IOException {
    return new XmlParser.ElementHandler() {
      public void processEndElement() throws ParseException {
        if (value != null) {
          try {
            count = Integer.parseInt(value);
          } catch (NumberFormatException nfe) {
            // count remains negative
          }
        }
        if (count <= 0) {
          throw new ParseException("The count must be specified.");
        }
      }
    };
  }
}
