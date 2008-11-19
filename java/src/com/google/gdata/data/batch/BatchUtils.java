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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;

import com.google.gdata.util.Namespaces;

/**
 * Utility methods for setting up and using batch feeds and entries.
 *
 * 
 */
public class BatchUtils {

  /**
   * Declares batch feed and entry extensions as well as the 
   * batch namespace in an extension profile.
   * 
   * @param extProfile extensionProfile
   */
  public static void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declareAdditionalNamespace(Namespaces.batchNs);
    declareEntryExtensions(extProfile);
    declareFeedExtensions(extProfile);
  }

  /**
   * Declares only the feed extensions in an extension profile.
   * 
   * @param extProfile
   */
  public static void declareFeedExtensions(ExtensionProfile extProfile) {
    // Since batch support is a mix-in, declare at the base feed level
    // so it can be used with any feed type
    extProfile.declare(BaseFeed.class, BatchOperation.getDefaultDescription());
  }

  /**
   * Declares only the entry extensions in an extension profile.
   * 
   * @param extProfile
   */
  public static void declareEntryExtensions(ExtensionProfile extProfile) {
    // Since batch support is a mix-in, declare at the base entry level
    // so it can be used with any entry type
    extProfile.declare(BaseEntry.class, BatchId.getDefaultDescription());
    extProfile.declare(BaseEntry.class, BatchOperation.getDefaultDescription());
    extProfile.declare(BaseEntry.class,
        BatchInterrupted.getDefaultDescription());
    extProfile.declare(BaseEntry.class, BatchStatus.getDefaultDescription());
  }

  /**
   * Gets the value of the tag {@code <batch:id>}.
   *
   * @return the batch id or null if it is not set
   * @param entry
   */
  public static String getBatchId(BaseEntry<?> entry) {
    return BatchId.getIdFrom(entry);
  }

  /**
   * Sets the value of the tag {@code <batch:id>}.
   *
   * @param extPoint extension point to put the id on
   * @param id the batch id or null to remove it
   */
  public static void setBatchId(ExtensionPoint extPoint, String id) {
    if (id == null) {
      extPoint.removeExtension(BatchId.class);
    } else {
      extPoint.setExtension(new BatchId(id));
    }
  }

  /**
   * Gets the batch operation type from the tag {@code <batch:operation>}
   * in a {@link ExtensionPoint}.
   *
   * @param extPoint extension point to get the operation type from
   * @return the operation to execute or null if it's not set
   */
  public static BatchOperationType getBatchOperationType(
      ExtensionPoint extPoint) {
    BatchOperation op = extPoint.getExtension(BatchOperation.class);
    return op == null ? null : op.getType();
  }

  /**
   * Sets the batch operation to execute in a {@link BaseEntry}.
   *
   * @param extPoint extension point to set the operation type on
   * @param op batch operation type or null to remove it
   */
  public static void setBatchOperationType(ExtensionPoint extPoint,
      BatchOperationType op) {
    if (op == null) {
      extPoint.removeExtension(BatchOperation.class);
    } else {
      extPoint.setExtension(new BatchOperation(op));
    }
  }

  /**
   * Gets the value of the tag {@code <batch:interrupted>}.
   *
   * @param extPoint the extension point to get the interrupted tag from
   * @return the object corresponding to the tag or null
   */
  public static BatchInterrupted getBatchInterrupted(ExtensionPoint extPoint) {
      return extPoint.getExtension(BatchInterrupted.class);
  }

  /**
   * Gets the value of the tag {@code <batch:status>}.
   *
   * @param extPoint the extension point to get the status from
   * @return the object corresponding to the tag or null
   */
  public static BatchStatus getBatchStatus(ExtensionPoint extPoint) {
    return extPoint.getExtension(BatchStatus.class);
  }

  /**
   * Checks whether a batch entry is a success report.
   *
   * This method is a shortcut for checking the code of
   * the entry's {@link BatchStatus} object.
   *
   * @param extPoint the extension point to check the status on.
   * @return true if the entry is a success report.
   * @exception IllegalArgumentException if the entry does not contain
   *   a BatchStatus object.
   */
  public static boolean isSuccess(ExtensionPoint extPoint) {
    int code = getRequiredBatchStatusCode(extPoint);
    return code >= 200 && code < 300;
  }
  
  /**
   * Checks whether a batch entry is an error report.
   *
   * This method is a shortcut for checking the code of
   * the entry's {@link BatchStatus} object.
   *
   * You'll want to call {@link #getBatchStatus(ExtensionPoint)}
   * to get the error description and message when this
   * method returns true.
   *
   * @param extPoint the extension point to check the status of.
   * @return true if the entry is an error report.
   * @exception IllegalArgumentException if the entry does not contain
   *   a BatchStatus object.
   */
  public static boolean isFailure(ExtensionPoint extPoint) {
    return !isSuccess(extPoint);
  }

  /**
   * Get the batch status or throw an illegal argument exception if it doesn't
   * have a status code.
   */
  private static int getRequiredBatchStatusCode(ExtensionPoint extPoint) {
    BatchStatus batchStatus = getBatchStatus(extPoint);
    if (batchStatus == null) {
      throw new IllegalArgumentException("Not a batch response entry; " +
           "Missing BatchStatus extension.");
    }
    return batchStatus.getCode();
  }
}

