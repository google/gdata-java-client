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
import java.util.ArrayList;
import java.util.List;

/**
 * Entry extension for the element {@code <batch:interrupted>}, which marks 
 * the batch feed as having been aborted.
 *
 * 
 */
public class BatchInterrupted extends ExtensionPoint
    implements Extension, IBatchInterrupted {

  private String reason;
  private int totalCount;
  private int successCount;
  private int errorCount;
  private String content;
  private ContentType contentType; 

  /**
   * Creates and initializes a BatchInterrupted object.
   * 
   * @param reason exception that caused batch processing to stop
   * @param totalCount number of entries parsed so far, note that
   *   it is to be expected that {@code totalCount >= successCount + errorCount}
   * @param successCount number of entries processed successfully so far
   * @param errorCount number of entries rejected so far
   */
  public BatchInterrupted(String reason,
                          int totalCount,
                          int successCount,
                          int errorCount) {
    this.reason = reason;
    this.totalCount = totalCount;
    this.successCount = successCount;
    this.errorCount = errorCount;
    if (totalCount < (successCount - errorCount)) {
      throw new IllegalArgumentException("total < success + error. total = " +
          totalCount + " success=" + successCount + " error=" + errorCount);
    }
  }

  /**
   * Creates an empty object.
   * 
   * Usually used in conjuction with 
   * {@link #getHandler(ExtensionProfile,String,String,Attributes)}. 
   */
  public BatchInterrupted() {
    
  }

  /**
   * Creates and initializes a BatchInterrupted object.
   * 
   * @param cause exception that caused batch processing to stop
   * @param totalCount number of entries parsed so far, note that
   *   it is to be expected that {@code totalCount >= successCount + errorCount}
   * @param successCount number of entries processed successfully so far
   * @param errorCount number of entries rejected so far
   */
  public BatchInterrupted(Throwable cause, int totalCount, int successCount,
                          int errorCount) {
    this(getReasonFromException(cause), totalCount, successCount, errorCount);
    if (cause instanceof ServiceException) {
      ServiceException se = (ServiceException)cause;
      content = se.getResponseBody();
      contentType = se.getResponseContentType();
    }
  }

  /** Returns the suggested extension description. */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(BatchInterrupted.class);
    desc.setNamespace(Namespaces.batchNs);
    desc.setLocalName("interrupted");
    desc.setRepeatable(false);
    return desc;
  }

  private static String getReasonFromException(Throwable cause) {
    String message = cause.getMessage();
    if (message == null) {
      return "Unexpected error";
    } else {
      return message;
    }
  }

  /** Gets a short message describing what happened. */
  public String getReason() {
    return reason;
  }

  /** Gets the total number of entries read. */
  public int getTotalCount() {
    return totalCount;
  }

  /** Gets the number of entries that were processed successfully. */
  public int getSuccessCount() {
    return successCount;
  }

  /** Gets the number of entries that were rejected. */
  public int getErrorCount() {
    return errorCount;
  }
  
  /** Gets the number of entries that were skipped. */
  public int getSkippedCount() {
    return (totalCount - ( successCount + errorCount));
  }

  /** Describe the content of this tag. */
  public ContentType getContentType() {
    return contentType;
  }

  /** Sets the content type for this tag. */
  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }

  /** Gets this tag content. See also {@link #getContentType()}. */
  public String getContent() {
    return content;
  }

  /** Sets this tag content. The type must correspond {@code contentType}. */
  public void setContent(String content) {
    this.content = content;
  }


  /**
   * Generates an XML representation for batch:interrupted.
   */
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    List<XmlWriter.Attribute> attrs =
        new ArrayList<XmlWriter.Attribute>(6);
    if (reason != null) {
      attrs.add(new XmlWriter.Attribute("reason", reason));
    }
    attrs.add(new XmlWriter.Attribute("parsed",
                                      Integer.toString(totalCount)));
    attrs.add(new XmlWriter.Attribute("success",
                                      Integer.toString(successCount)));
    attrs.add(new XmlWriter.Attribute("error",
                                      Integer.toString(errorCount)));
    int skippedCount = totalCount - ( successCount + errorCount);
    attrs.add(new XmlWriter.Attribute("unprocessed",
                                      Integer.toString(skippedCount)));
    
    if (contentType != null) {
      // Charset makes no sense in this context
      contentType.getAttributes().remove(ContentType.ATTR_CHARSET);

      attrs.add(new XmlWriter.Attribute("content-type", 
                                        contentType.toString()));
    }
    generateStartElement(w, Namespaces.batchNs,  "interrupted", attrs, null);
    
    // Invoke ExtensionPoint
    generateExtensions(w, extProfile);
    
    if (content != null) {
      w.characters(content);
    }
    w.endElement(Namespaces.batchNs, "interrupted");
  }

  /**
   * Creates an XML ElementHandler that will initialize the object based 
   * on a tag batch:interrupted parsed by the XML parser.
   * 
   * @param extProfile
   * @param namespace
   * @param localName
   * @param attrs attributes of batch:interrupted
   * @return a child handler linked to the current object
   * @throws ParseException 
   */
  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace, String localName,
                                             Attributes attrs)
      throws ParseException {
    return new BatchInterruptedElementHandler(extProfile, attrs);
  }
  
  private static int getIntegerAttribute(Attributes attrs, 
                                         String name, 
                                         int defValue) throws ParseException {
    String stringValue = attrs.getValue(name);
    if (stringValue == null) {
      return defValue;
    }
    try {
      return Integer.parseInt(stringValue);
    } catch (NumberFormatException e) {
      ParseException pe = new ParseException(
          CoreErrorDomain.ERR.invalidIntegerAttribute, e);
      pe.setInternalReason("Invalid integer in value of attribute "
          + name + ": '" + stringValue + "'");
      throw pe;
    }
  }

  /**
   * Parses a batch:interrupted element and initializes the 
   * current object with what is found.
   */
  private class BatchInterruptedElementHandler
      extends ExtensionPoint.ExtensionHandler {

    public BatchInterruptedElementHandler(ExtensionProfile extProfile,
                                          Attributes attrs)
        throws ParseException {
      super(extProfile, BatchInterrupted.class);

      totalCount = getIntegerAttribute(attrs, "parsed", 0);
      successCount = getIntegerAttribute(attrs, "success", 0);
      errorCount = getIntegerAttribute(attrs, "error", 0);
      reason = attrs.getValue("reason");

      String contentTypeString = attrs.getValue("content-type");
      if (contentTypeString != null) {
        try {
          contentType = new ContentType(contentTypeString);
        } catch (IllegalArgumentException e) {
          ParseException pe = new ParseException(
              CoreErrorDomain.ERR.invalidContentType, e);
          pe.setInternalReason("Invalid content type: '" +
              contentTypeString + "'");
          throw pe;
        }
      }
    }

    @Override
    public void processEndElement() {
      content = value;
    }
  }
}
