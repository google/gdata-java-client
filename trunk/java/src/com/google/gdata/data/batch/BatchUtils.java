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
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
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
  public static String getBatchId(IEntry entry) {
    return BatchId.getIdFrom((BaseEntry<?>) entry);
  }

  /**
   * Sets the value of the tag {@code <batch:id>}.
   *
   * @param entry
   * @param id the batch id or null to remove it
   */
  public static void setBatchId(IEntry entry, String id) {
    if (entry instanceof ExtensionPoint) {
      if (id == null) {
        ((ExtensionPoint) entry).removeExtension(BatchId.class);
      } else {
        ((ExtensionPoint) entry).setExtension(new BatchId(id));
      }
    } else {
    }
  }

  /**
   * Gets the batch operation type from the tag {@code <batch:operation>}
   * in a {@link BaseEntry}.
   *
   * @return the operation to execute or null if it's not set
   * @param entry
   */
  public static BatchOperationType getBatchOperationType(IEntry entry) {
    if (entry instanceof ExtensionPoint) {
      return getBatchOperationType((ExtensionPoint)entry);
    } else {
      return null;
    }
  }

  /**
   * Sets the batch operation to execute in a {@link BaseEntry}.
   *
   * @param entry
   * @param op batch operation type or null to remove it.
   */
  public static void setBatchOperationType(IEntry entry,
      BatchOperationType op) {
    if (entry instanceof ExtensionPoint) {
      setBatchOperationType((ExtensionPoint)entry, op);
    } else {
    }
  }

  /**
   * Gets the batch operation type from the tag {@code <batch:operation>}
   * in a {@link BaseFeed}.
   *
   * @return the operation to execute or null if it's not set
   * @param feed
   */
  public static BatchOperationType getBatchOperationType(IFeed feed) {
    if (feed instanceof ExtensionPoint) {
      return getBatchOperationType((ExtensionPoint) feed);
    } else {
      return null;
    }
  }

  /**
   * Sets the batch operation to execute in a {@link BaseFeed}.
   *
   * @param feed
   * @param op batch operation type or null to remove it.
   */
  public static void setBatchOperationType(IFeed feed, BatchOperationType op) {
    if (feed instanceof ExtensionPoint) {
      setBatchOperationType((ExtensionPoint)feed, op);
    } else {
    }
  }


  private static BatchOperationType getBatchOperationType(
      ExtensionPoint entry) {
    BatchOperation op = entry.getExtension(BatchOperation.class);
    return op == null ? null : op.getType();
  }

  private static void setBatchOperationType(ExtensionPoint entry,
      BatchOperationType op) {
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
  public static BatchInterrupted getBatchInterrupted(IEntry entry) {
    if (entry instanceof ExtensionPoint) {
      return ((ExtensionPoint) entry).getExtension(BatchInterrupted.class);
    } else {
      return null;
    }
  }

  /**
   * Gets the value of the tag {@code <batch:status>}.
   *
   * @return the object corresponding to the tag or null
   * @param entry
   */
  public static BatchStatus getBatchStatus(IEntry entry) {
    if (entry instanceof ExtensionPoint) {
      return ((ExtensionPoint) entry).getExtension(BatchStatus.class);
    } else {
      return null;
    }
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
  public static boolean isSuccess(IEntry entry) {
    int code = getRequiredBatchStatusCode(entry);
    return code >= 200 && code < 300;
  }
  
  /**
   * Checks whether a batch entry is an error report.
   *
   * This method is a shortcut for checking the code of
   * the entry's {@link BatchStatus} object.
   *
   * You'll want to call {@link #getBatchStatus(IEntry)}
   * to get the error description and message when this
   * method returns true.
   *
   * @param entry
   * @return true if the entry is an error report.
   * @exception IllegalArgumentException if the entry does not contain
   *   a BatchStatus object.
   */
  public static boolean isFailure(IEntry entry) {
    return !isSuccess(entry);
  }

  private static int getRequiredBatchStatusCode(IEntry entry) {
    BatchStatus batchStatus = getBatchStatus(entry);
    if (batchStatus == null) {
      throw new IllegalArgumentException("Not a batch response entry; " +
           "Missing BatchStatus extension.");
    }
    return batchStatus.getCode();
  }
}
