/* Copyright (c) 2006 Google Inc.
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
    extProfile.declareFeedExtension(BatchOperation.getDefaultDescription());
  }

  /**
   * Declares only the entry extensions in an extension profile.
   * 
   * @param extProfile
   */
  public static void declareEntryExtensions(ExtensionProfile extProfile) {
    extProfile.declareEntryExtension(BatchId.getDefaultDescription());
    extProfile.declareEntryExtension(BatchOperation.getDefaultDescription());
    extProfile.declareEntryExtension(BatchInterrupted.getDefaultDescription());
    extProfile.declareEntryExtension(BatchStatus.getDefaultDescription());
  }

  /**
   * Gets the value of the tag {@code <batch:id>}.
   *
   * @return the batch id or null if it is not set
   * @param entry
   */
  public static String getBatchId(BaseEntry entry) {
    return BatchId.getIdFrom(entry);
  }

  /**
   * Sets the value of the tag {@code <batch:id>}.
   *
   * @param entry
   * @param id the batch id or null to remove it
   */
  public static void setBatchId(BaseEntry entry, String id) {
    if (id == null) {
      entry.removeExtension(BatchId.class);
    } else
      entry.setExtension(new BatchId(id));
  }

  /**
   * Gets the batch operation type from the tag {@code <batch:operation>}
   * in a {@link BaseEntry}.
   *
   * @return the operation to execute or null if it's not set
   * @param entry
   */
  public static BatchOperationType getBatchOperationType(BaseEntry entry) {
    return getBatchOperationType((ExtensionPoint)entry);
  }

  /**
   * Sets the batch operation to execute in a {@link BaseEntry}.
   *
   * @param entry
   * @param op batch operation type or null to remove it.
   */
  public static void setBatchOperationType(BaseEntry entry, BatchOperationType op) {
    setBatchOperationType((ExtensionPoint)entry, op);
  }

  /**
   * Gets the batch operation type from the tag {@code <batch:operation>}
   * in a {@link BaseFeed}.
   *
   * @return the operation to execute or null if it's not set
   * @param feed
   */
  public static BatchOperationType getBatchOperationType(BaseFeed feed) {
    return getBatchOperationType((ExtensionPoint)feed);
  }

  /**
   * Sets the batch operation to execute in a {@link BaseFeed}.
   *
   * @param feed
   * @param op batch operation type or null to remove it.
   */
  public static void setBatchOperationType(BaseFeed feed, BatchOperationType op) {
    setBatchOperationType((ExtensionPoint)feed, op);
  }


  private static BatchOperationType getBatchOperationType(ExtensionPoint entry) {
    BatchOperation op = (BatchOperation)entry.getExtension(BatchOperation.class);
    return op == null ? null : op.getType();
  }

  private static void setBatchOperationType(ExtensionPoint entry, BatchOperationType op) {
    if (op == null) {
      entry.removeExtension(BatchOperation.class);
    } else {
      entry.setExtension(new BatchOperation(op));
    }
  }

  /**
   * Gets the value of the tag {@code <batch:interrupted>}.
   *
   * @return the object corresponding to the tag or null
   * @param entry
   */
  public static BatchInterrupted getBatchInterrupted(BaseEntry entry) {
    return (BatchInterrupted)entry.getExtension(BatchInterrupted.class);
  }

  /**
   * Gets the value of the tag {@code <batch:status>}.
   *
   * @return the object corresponding to the tag or null
   * @param entry
   */
  public static BatchStatus getBatchStatus(BaseEntry entry) {
    return (BatchStatus)entry.getExtension(BatchStatus.class);
  }

  /**
   * Checks whether a batch entry is a success report.
   *
   * This method is a shortcut for checking the code of
   * the entry's {@link BatchStatus} object.
   *
   * @param entry
   * @return true if the entry is a success report.
   * @exception IllegalArgumentException if the entry does not contain
   *   a BatchStatus object.
   */
  public static boolean isSuccess(BaseEntry entry) {
    int code = getRequiredBatchStatusCode(entry);
    return code >= 200 && code < 300;
  }
  
  /**
   * Checks whether a batch entry is an error report.
   *
   * This method is a shortcut for checking the code of
   * the entry's {@link BatchStatus} object.
   *
   * You'll want to call {@link #getBatchStatus(BaseEntry)}
   * to get the error description and message when this
   * method returns true.
   *
   * @param entry
   * @return true if the entry is an error report.
   * @exception IllegalArgumentException if the entry does not contain
   *   a BatchStatus object.
   */
  public static boolean isFailure(BaseEntry entry) {
    return !isSuccess(entry);
  }

  private static int getRequiredBatchStatusCode(BaseEntry entry) {
    BatchStatus batchStatus = getBatchStatus(entry);
    if (batchStatus == null) {
      throw new IllegalArgumentException("Not a batch response entry; " +
           "Missing BatchStatus extension.");
    }
    return batchStatus.getCode();
  }
}
