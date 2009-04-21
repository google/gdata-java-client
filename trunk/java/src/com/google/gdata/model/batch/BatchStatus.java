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


package com.google.gdata.model.batch;

import com.google.gdata.data.batch.IBatchStatus;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.QName;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ServiceException;

import java.net.HttpURLConnection;

/**
 * Describes server status information about how an entry was processed in a
 * batch operation.
 *
 * 
 */
public class BatchStatus extends Element implements IBatchStatus {

  /**
   * The key for this element.
   */
  public static final ElementKey<String, BatchStatus> KEY = ElementKey.of(
      new QName(Namespaces.batchNs, "status"), String.class, BatchStatus.class);

  /**
   * The HTTP response code.
   */
  public static final AttributeKey<Integer> CODE = AttributeKey.of(
      new QName("code"), Integer.class);

  /**
   * The MIME type for the content of the error message contained in this
   * element.
   */
  public static final AttributeKey<ContentType> CONTENT_TYPE = AttributeKey.of(
      new QName("content-type"), ContentType.class);

  /**
   * The short message describing this status.
   */
  public static final AttributeKey<String> REASON = AttributeKey.of(
      new QName("reason"));

  /*
   * Generate the default metadata for this element.
   */
  static {
    ElementCreator builder = DefaultRegistry.build(KEY)
        .setContentRequired(false);
    builder.addAttribute(CODE).setRequired(true);
    builder.addAttribute(CONTENT_TYPE);
    builder.addAttribute(REASON).setRequired(true);
  }

  /** Creates a 200 Success status object. */
  public static BatchStatus createSuccessStatus() {
    BatchStatus status = new BatchStatus();
    status.setCode(HttpURLConnection.HTTP_OK);
    status.setReason("Success");
    return status;
  }

  /** Creates a Success status object. */
  public static BatchStatus createCreatedStatus() {
    BatchStatus status = new BatchStatus();
    status.setCode(HttpURLConnection.HTTP_CREATED);
    status.setReason("Created");
    return status;
  }
  
  /**
   * Default mutable constructor.
   */
  public BatchStatus() {
    super(DefaultRegistry.get(KEY));
  }

  /**
   * Creates a BatchStatus and initializes it
   * based on an exception.
   *
   * @param e exception to initialize the status from
   */
  public BatchStatus(ServiceException e) {
    this();
    int code = e.getHttpErrorCodeOverride();
    if (code == -1) {
      code = HttpURLConnection.HTTP_INTERNAL_ERROR;
    }
    setCode(code);
    setReason(e.getMessage());
    setContentType(e.getResponseContentType());
    setContent(e.getResponseBody());
  }

  /**
   * Returns the HTTP response code.
   *
   * @return HTTP response code
   */
  public int getCode() {
    Integer code = getAttributeValue(CODE);
    return code == null ? 0 : code.intValue();
  }
  
  /**
   * Sets the HTTP response code.
   *
   * @param code HTTP response code or <code>null</code> to reset
   */
  public void setCode(Integer code) {
    throwExceptionIfImmutable();
    if (code == null) {
      super.removeAttribute(CODE);
    } else {
      super.addAttribute(CODE, code);
    }
  }

  /**
   * Returns whether it has the HTTP response code.
   *
   * @return whether it has the HTTP response code
   */
  public boolean hasCode() {
    return getCode() != 0;
  }

  /**
   * Returns the error message explaining what went wrong while processing the
   * request.
   *
   * @return error message explaining what went wrong while processing the
   *     request
   */
  public String getContent() {
    return getTextValue(KEY);
  }

  /**
   * Sets the error message explaining what went wrong while processing the
   * request.
   *
   * @param content error message explaining what went wrong while processing
   *     the request or <code>null</code> to reset
   */
  public void setContent(String content) {
    setTextValue(content);
  }

  /**
   * Returns whether it has the error message explaining what went wrong while
   * processing the request.
   *
   * @return whether it has the error message explaining what went wrong while
   *     processing the request
   */
  public boolean hasContent() {
    return hasTextValue();
  }

  /**
   * Returns the MIME type for the content of the error message contained in
   * this element.
   *
   * @return MIME type for the content of the error message contained in this
   *     element
   */
  public ContentType getContentType() {
    return getAttributeValue(CONTENT_TYPE);
  }

  /**
   * Sets the MIME type for the content of the error message contained in this
   * element.
   *
   * @param contentType MIME type for the content of the error message contained
   *     in this element or <code>null</code> to reset
   */
  public void setContentType(ContentType contentType) {
    throwExceptionIfImmutable();
    if (contentType == null) {
      super.removeAttribute(CONTENT_TYPE);
    } else {
      super.addAttribute(CONTENT_TYPE, 
          new ContentType(contentType.getMediaType()));
    }
  }

  /**
   * Returns whether it has the MIME type for the content of the error message
   * contained in this element.
   *
   * @return whether it has the MIME type for the content of the error message
   *     contained in this element
   */
  public boolean hasContentType() {
    return getContentType() != null;
  }

  /**
   * Returns the short message describing this status.
   *
   * @return short message describing this status
   */
  public String getReason() {
    return getAttributeValue(REASON);
  }

  /**
   * Sets the short message describing this status.
   *
   * @param reason short message describing this status or <code>null</code> to
   *     reset
   */
  public void setReason(String reason) {
    throwExceptionIfImmutable();
    if (reason == null) {
      super.removeAttribute(REASON);
    } else {
      super.addAttribute(REASON, reason);
    }
  }

  /**
   * Returns whether it has the short message describing this status.
   *
   * @return whether it has the short message describing this status
   */
  public boolean hasReason() {
    return getReason() != null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    BatchStatus other = (BatchStatus) obj;
    return eq(getCode(), other.getCode())
        && eq(getContent(), other.getContent())
        && eq(getContentType(), other.getContentType())
        && eq(getReason(), other.getReason());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    result = 37 * result + getCode();
    if (getContent() != null) {
      result = 37 * result + getContent().hashCode();
    }
    if (getContentType() != null) {
      result = 37 * result + getContentType().hashCode();
    }
    if (getReason() != null) {
      result = 37 * result + getReason().hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{BatchStatus code=" + getAttributeValue(CODE) + " content="
        + getTextValue() + " contentType=" + getAttributeValue(CONTENT_TYPE)
        + " reason=" + getAttributeValue(REASON) + "}";
  }
}
