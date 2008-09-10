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
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Entry and Feed extension that defines the batch operation
 * to apply, defined by its type ({@link BatchOperationType}).
 *
 * 
 */
public class BatchOperation extends ExtensionPoint implements Extension {

  private BatchOperationType type;

  /** Creates a new BatchOperation object. */
  public BatchOperation(BatchOperationType type) {
    this.type = type;
  }

  /** Creates a BatchOperation without type. */
  public BatchOperation() {
    
  }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(BatchOperation.class);
    desc.setNamespace(Namespaces.batchNs);
    desc.setLocalName("operation");
    desc.setRepeatable(false);
    return desc;
  }
  
  /**
   * Gets the operation type ({@link BatchOperationType#DELETE}, 
   * {@link BatchOperationType#INSERT} or {@link BatchOperationType#UPDATE}.
   */
  public BatchOperationType getType() {
    return type;
  }
  
  /** Sets the operation type. */
  public void setType(BatchOperationType type) {
    this.type = type;
  }
  
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    List<XmlWriter.Attribute> attrs = null;
    if (type != null) {
      attrs = Collections.singletonList(
          new XmlWriter.Attribute("type", this.type.getName()));
    }
    generateStartElement(w, Namespaces.batchNs, "operation", attrs, null);
    
    // Generate extension elements
    generateExtensions(w, extProfile);
    
    w.endElement(Namespaces.batchNs, "operation");
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace, String localName,
                                             Attributes attrs)
      throws ParseException {
    return new BatchOperationHandler(extProfile, attrs);
  }
  
  private class BatchOperationHandler extends ExtensionPoint.ExtensionHandler {

    public BatchOperationHandler(ExtensionProfile profile, Attributes attrs)
        throws ParseException {
      super(profile, BatchOperation.class);
      String operationType = attrs.getValue("type");
      BatchOperationType op = BatchOperationType.forName(operationType);
      if (op == null) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.invalidBatchOperationType);
        pe.setInternalReason("Invalid type for batch:operation: '" +
            operationType + "'");
        throw pe;
      }
      type = op;
    }
  }
}
