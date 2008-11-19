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
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry extension for the element {@code <batch:status>}, which contains
 * status information returned by the server about how an entry was processed
 * in a batch operation.
 *
 * 
 */
public class BatchStatus extends ExtensionPoint
    implements Extension, IBatchStatus {

  private int code;
  private String reason;
  private ContentType contentType;
  private String content;

  /** Creates an empty BatchStatus. */
  public BatchStatus() {

  }

  /**
   * Creates a BatchStatus and initializes it
   * based on an exception.
   *
   * @param e
   */
  public BatchStatus(ServiceException e) {
    code = e.getHttpErrorCodeOverride();
    if (code == -1) {
      code = HttpURLConnection.HTTP_INTERNAL_ERROR;
    }
    reason = e.getMessage();
    contentType = e.getResponseContentType();
    content = e.getResponseBody();
  }

  /** Creates a 200 Success status object. */
  public static BatchStatus createSuccessStatus() {
    BatchStatus retval = new BatchStatus();
    retval.setCode(HttpURLConnection.HTTP_OK);
    retval.setReason("Success");
    return retval;
  }

  /** Creates a Success status object. */
  public static BatchStatus createCreatedStatus() {
    BatchStatus retval = new BatchStatus();
    retval.setCode(HttpURLConnection.HTTP_CREATED);
    retval.setReason("Created");
    return retval;
  }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(BatchStatus.class);
    desc.setNamespace(Namespaces.batchNs);
    desc.setLocalName("status");
    desc.setRepeatable(false);
    return desc;
  }

  /** Returns the HTTP response code for this status. */
  public int getCode() {
    return code;
  }

  /** Sets HTTP response code. */
  public void setCode(int code) {
    this.code = code;
  }

  /** Returns a short message describing this status. */
  public String getReason() {
    return reason;
  }

  /** Sets a short message describing this status. */
  public void setReason(String reason) {
    this.reason = reason;
  }

  /** Gets mime type for the content of this error. */
  public ContentType getContentType() {
    return contentType;
  }

  /** Sets mime type for the content of this error. */
  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }

  /** Gets error message. */
  public String getContent() {
    return content;
  }

  /** Sets error message. Its type must correspond to the content type. */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Generate an Atom XML representation of the current object.
   */
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    List<XmlWriter.Attribute> attributes =
        new ArrayList<XmlWriter.Attribute>(4);

    if (code > 0) {
      attributes.add(new XmlWriter.Attribute("code", Integer.toString(code)));
    }
    if (reason != null) {
      attributes.add(new XmlWriter.Attribute("reason", reason));
    }
    if (contentType != null) {
      // Charset makes no sense in this context
      contentType.getAttributes().remove(ContentType.ATTR_CHARSET);

      attributes.add(new XmlWriter.Attribute("content-type",
                                             contentType.toString()));
    }
    generateStartElement(w, Namespaces.batchNs, "status", attributes, null);

    generateExtensions(w, extProfile);

    if (content != null) {
      w.characters(content);
    }
    w.endElement(Namespaces.batchNs, "status");
  }

  /**
   * Parses XML in the Atom format and uses it to set field values.
   *
   * @param extProfile
   * @param namespace
   * @param localName
   * @param attrs XML attributes
   * @return  a child handler for batch:atom
   * @throws ParseException if the current element is not a valid
   *   batch:atom element
   */
  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace, String localName,
                                             Attributes attrs)
      throws ParseException {
    return new BatchStatusElementHandler(extProfile, attrs);
  }

  /** Element handler for batch:status. */
  private class BatchStatusElementHandler
      extends ExtensionPoint.ExtensionHandler {

    private BatchStatusElementHandler(ExtensionProfile extProfile,
                                      Attributes attrs)
        throws ParseException {
      super(extProfile, BatchStatus.class);
      String code = attrs.getValue("code");
      if (code != null) {
        try {
          setCode(Integer.parseInt(code));
        } catch (NumberFormatException e) {
          ParseException pe = new ParseException(
              CoreErrorDomain.ERR.invalidIntegerAttribute, e);
          pe.setInternalReason("Invalid integer value for code " +
              "attribute : '" + code + "'");
          throw pe;
        }
      }

      String reason = attrs.getValue("reason");
      if (reason != null) {
        setReason(reason);
      }

      String contentType = attrs.getValue("content-type");
      if (contentType != null) {
        try {
          setContentType(new ContentType(contentType));
        } catch (IllegalArgumentException e) {
          ParseException pe = new ParseException(
              CoreErrorDomain.ERR.invalidContentType, e);
          pe.setInternalReason("Invalid content type: " + contentType);
          throw pe;
        }
      }
    }

    @Override
    public void processEndElement() {
      if (value != null) {
        setContent(value);
      }
    }
  }
}
