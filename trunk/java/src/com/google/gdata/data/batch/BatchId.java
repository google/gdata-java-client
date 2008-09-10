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


package com.google.gdata.data.batch;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Entry extension for {@code <batch:id>} tags.
 *
 * 
 */
public class BatchId implements Extension {

  private String id;

  /** Creates a BatchId object. */
  public BatchId(String id) {
    this.id = id;
  }
  
  /** Creates a BatchId object without ID. */
  public BatchId() {
  }
  
  /** Gets the id. */
  public String getId() {
    return id;
  }

  /** Sets the id. */
  public void setId(String id) {
    this.id = id;
  }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(BatchId.class);
    desc.setNamespace(Namespaces.batchNs);
    desc.setLocalName("id");
    desc.setRepeatable(false);
    return desc;
  }

  /**
   * Convenience method for getting a batchId from 
   * an entry if it's there.
   * 
   * @param entry
   * @return the id or null if it's not defined
   */
  public static String getIdFrom(BaseEntry<?> entry) {
    BatchId tag = entry.getExtension(BatchId.class);
    return tag == null ? null : tag.getId();
  }
  
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    w.simpleElement(Namespaces.batchNs, "id", null, id);
  }

  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace, String localName,
                                             Attributes attrs) {
    return new XmlParser.ElementHandler() {
      
      @Override
      public void processEndElement() {
        id = value;
      }
    };
  }
}
