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

package com.google.api.gbase.client;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Object representation of the gm:publishing_priority tag.
 */
public class GmPublishingPriority implements Extension {

  /** GData attribute name under which to expose the publishing priority. */
  private static final String ATTRIBUTE_NAME = "publishing_priority";
  
  /** 
   * Possible values for the indexing element.
   */
  public enum Value {
    /** The item has a high priority for getting published. Using this value
     * should result in the item getting published (available for search) 
     * in a very short time (less than a minute, most of the time).
     */
    HIGH("high"),
    
    /** 
     * The item has a low priority for getting published. Using this value
     * should result in the item getting published (available for search)
     * in less than a day. 
     */
    LOW("low");
    
    private final String value;
    
    private Value(String value) {
      this.value = value;
    }
    
    /** 
     * Returns the text value, as represented in the entry.
     */
    public String getTextValue() {
      return value;
    }
    
    /**
     * Returns the priority value based on the {@code textValue} 
     * parameter, or {@code null} if no such value exists.
     * 
     * @param textValue text representation of the value
     * @return the priority value, or {@code null} if not found
     */
    public static Value getByText(String textValue) {
      for (Value pipeline : Value.values()) {
        if (pipeline.getTextValue().equals(textValue)) {
          return pipeline;
        }
      }
      return null;
    }
  }
  
  /** The publishing priority for the entry. */
  private Value value;
  
  /**
   * Creates a gm:publishing_priority tag.
   */
  public GmPublishingPriority() {

  }
  
  /** 
   * Returns a description for this extension. 
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(GmPublishingPriority.class);
    desc.setNamespace(GoogleBaseNamespaces.GM);
    desc.setLocalName(ATTRIBUTE_NAME);
    desc.setRepeatable(false);
    return desc;
  }
  
  public Value getValue() {
    return value;
  }
  
  public void setValue(Value value) {
    this.value = value;
  }
  
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    w.simpleElement(GoogleBaseNamespaces.GM, ATTRIBUTE_NAME, 
        null, value.getTextValue());
  }
  
  public ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new XmlParser.ElementHandler() { 
      @Override
      public void processEndElement() throws ParseException {
        if (value == null) {
          throw new ParseException("No value specified for " 
              + ATTRIBUTE_NAME + " element.");
        }

        String text = value.trim().toLowerCase();
        Value parsedValue = Value.getByText(text);
        if (parsedValue == null) {
          throw new ParseException("Invalid value specified for "
              + ATTRIBUTE_NAME + " element: '" + text + "'");
        }
            
        GmPublishingPriority.this.value = parsedValue;
      }
    };
  }
}
