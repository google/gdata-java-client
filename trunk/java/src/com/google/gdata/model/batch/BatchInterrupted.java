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

import com.google.gdata.data.batch.IBatchInterrupted;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ServiceException;

/**
 * Signals that feed processing was interrupted.
 *
 * 
 */
public class BatchInterrupted extends Element implements IBatchInterrupted {

  /**
   * The key for this element.
   */
  public static final ElementKey<String, BatchInterrupted> KEY = ElementKey.of(
      new QName(Namespaces.batchNs, "interrupted"), String.class,
      BatchInterrupted.class);

  /**
   * The MIME type of the content of this element.
   */
  public static final AttributeKey<ContentType> CONTENT_TYPE = AttributeKey.of(
      new QName("content-type"), ContentType.class);

  /**
   * The number of entries for which processing failed.
   */
  public static final AttributeKey<Integer> ERROR_COUNT = AttributeKey.of(
      new QName("error"), Integer.class);

  /**
   * The reason.
   */
  public static final AttributeKey<String> REASON = AttributeKey.of(
      new QName("reason"));

  /**
   * The number of entries parsed but not processed.
   */
  public static final AttributeKey<Integer> SKIPPED_COUNT = AttributeKey.of(
      new QName("unprocessed"), Integer.class);

  /**
   * The number of entries processed successfully.
   */
  public static final AttributeKey<Integer> SUCCESS_COUNT = AttributeKey.of(
      new QName("success"), Integer.class);

  /**
   * The number of entries that were parsed.
   */
  public static final AttributeKey<Integer> TOTAL_COUNT = AttributeKey.of(
      new QName("parsed"), Integer.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY).setContentRequired(false);
    builder.addAttribute(CONTENT_TYPE);
    builder.addAttribute(ERROR_COUNT).setRequired(true);
    builder.addAttribute(TOTAL_COUNT).setRequired(true);
    builder.addAttribute(REASON);
    builder.addAttribute(SUCCESS_COUNT).setRequired(true);
    builder.addAttribute(SKIPPED_COUNT).setRequired(true);
  }

  /**
   * Default mutable constructor.
   */
  public BatchInterrupted() {
    super(KEY);
  }

  /**
   * Creates and initializes a BatchInterrupted object.
   *
   * @param reason exception that caused batch processing to stop
   * @param totalCount number of entries parsed so far, note that
   *   it is to be expected that {@code totalCount >= successCount + errorCount}
   * @param successCount number of entries processed successfully so far
   * @param errorCount number of entries rejected so far
   * @throws IllegalArgumentException if the total count is less than
   *     successCount - errorCount
   */
  public BatchInterrupted(String reason, int totalCount, int successCount,
      int errorCount) {
    this();
    if (totalCount < (successCount - errorCount)) {
      throw new IllegalArgumentException("total < success + error. total = " +
          totalCount + " success=" + successCount + " error=" + errorCount);
    }
    setReason(reason);
    setTotalCount(totalCount);
    setSuccessCount(successCount);
    setErrorCount(errorCount);
    setSkippedCount(totalCount - (successCount + errorCount));
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
      setContent(se.getResponseBody());
      setContentType(se.getResponseContentType());
    }
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
  public BatchInterrupted setContent(String content) {
    setTextValue(content);
    return this;
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
   * Returns the MIME type of the content of this element.
   *
   * @return MIME type of the content of this element
   */
  public ContentType getContentType() {
    return getAttributeValue(CONTENT_TYPE);
  }

  /**
   * Sets the MIME type of the content of this element.
   *
   * @param contentType MIME type of the content of this element or
   *     <code>null</code> to reset
   */
  public BatchInterrupted setContentType(ContentType contentType) {
    setAttributeValue(CONTENT_TYPE, (contentType == null) ? null
        : new ContentType(contentType.getMediaType()));
    return this;
  }

  /**
   * Returns whether it has the MIME type of the content of this element.
   *
   * @return whether it has the MIME type of the content of this element
   */
  public boolean hasContentType() {
    return getContentType() != null;
  }

  /**
   * Returns the number of entries for which processing failed.
   *
   * @return number of entries for which processing failed
   */
  public int getErrorCount() {
    Integer count = getAttributeValue(ERROR_COUNT);
    return count == null ? 0 : count.intValue();
  }

  /**
   * Sets the number of entries for which processing failed.
   *
   * @param errorCount number of entries for which processing failed or
   *     <code>null</code> to reset
   */
  public BatchInterrupted setErrorCount(Integer errorCount) {
    setAttributeValue(ERROR_COUNT, errorCount);
    return this;
  }

  /**
   * Returns true if the error count exists.
   */
  public boolean hasErrorCount() {
    return getErrorCount() != 0;
  }

  /**
   * Returns the reason.
   *
   * @return reason
   */
  public String getReason() {
    return getAttributeValue(REASON);
  }

  /**
   * Sets the reason.
   *
   * @param reason reason or <code>null</code> to reset
   */
  public BatchInterrupted setReason(String reason) {
    setAttributeValue(REASON, reason);
    return this;
  }

  /**
   * Returns whether it has the reason.
   *
   * @return whether it has the reason
   */
  public boolean hasReason() {
    return getReason() != null;
  }

  /**
   * Returns the number of entries parsed but not processed.
   *
   * @return number of entries parsed but not processed
   */
  public int getSkippedCount() {
    Integer count = getAttributeValue(SKIPPED_COUNT);
    return count == null ? 0 : count.intValue();
  }

  /**
   * Sets the number of entries parsed but not processed.
   *
   * @param skippedCount number of entries parsed but not processed or
   *     <code>null</code> to reset
   */
  public BatchInterrupted setSkippedCount(Integer skippedCount) {
    setAttributeValue(SKIPPED_COUNT, skippedCount);
    return this;
  }

  /**
   * Returns whether it has the number of entries parsed but not processed.
   *
   * @return whether it has the number of entries parsed but not processed
   */
  public boolean hasSkippedCount() {
    return getSkippedCount() != 0;
  }

  /**
   * Returns the number of entries processed successfully.
   *
   * @return number of entries processed successfully
   */
  public int getSuccessCount() {
    Integer count = getAttributeValue(SUCCESS_COUNT);
    return count == null ? 0 : count.intValue();
  }

  /**
   * Sets the number of entries processed successfully.
   *
   * @param successCount number of entries processed successfully or
   *     <code>null</code> to reset
   */
  public BatchInterrupted setSuccessCount(Integer successCount) {
    setAttributeValue(SUCCESS_COUNT, successCount);
    return this;
  }

  /**
   * Returns whether it has the number of entries processed successfully.
   *
   * @return whether it has the number of entries processed successfully
   */
  public boolean hasSuccessCount() {
    return getSuccessCount() != 0;
  }

  /**
   * Returns the number of entries that were parsed.
   *
   * @return number of entries that were parsed
   */
  public int getTotalCount() {
    Integer count = getAttributeValue(TOTAL_COUNT);
    return count == null ? 0 : count.intValue();
  }

  /**
   * Sets the number of entries that were parsed.
   *
   * @param totalCount number of entries that were parsed or <code>null</code>
   *     to reset
   */
  public BatchInterrupted setTotalCount(Integer totalCount) {
    setAttributeValue(TOTAL_COUNT, totalCount);
    return this;
  }

  /**
   * Returns whether it has the number of entries that were parsed.
   *
   * @return whether it has the number of entries that were parsed
   */
  public boolean hasTotalCount() {
    return getTotalCount() != 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    BatchInterrupted other = (BatchInterrupted) obj;
    return eq(getContent(), other.getContent())
        && eq(getContentType(), other.getContentType())
        && eq(getErrorCount(), other.getErrorCount())
        && eq(getReason(), other.getReason())
        && eq(getSkippedCount(), other.getSkippedCount())
        && eq(getSuccessCount(), other.getSuccessCount())
        && eq(getTotalCount(), other.getTotalCount());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getContent() != null) {
      result = 37 * result + getContent().hashCode();
    }
    if (getContentType() != null) {
      result = 37 * result + getContentType().hashCode();
    }
    result = 37 * result + getErrorCount();
    if (getReason() != null) {
      result = 37 * result + getReason().hashCode();
    }
    result = 37 * result + getSkippedCount();
    result = 37 * result + getSuccessCount();
    result = 37 * result + getTotalCount();
    return result;
  }

  @Override
  public String toString() {
    return "{BatchInterrupted content=" + getTextValue() + " contentType=" +
        getAttributeValue(CONTENT_TYPE) + " errorCount="
        + getAttributeValue(ERROR_COUNT) + " reason="
        + getAttributeValue(REASON) + " skippedCount="
        + getAttributeValue(SKIPPED_COUNT) + " successCount="
        + getAttributeValue(SUCCESS_COUNT) + " totalCount="
        + getAttributeValue(TOTAL_COUNT) + "}";
  }

  private static String getReasonFromException(Throwable cause) {
    String message = cause.getMessage();
    if (message == null) {
      return "Unexpected error";
    } else {
      return message;
    }
  }
}
