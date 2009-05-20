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

import com.google.gdata.client.batch.BatchInterruptedException;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.Feed;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.IBatchInterrupted;
import com.google.gdata.data.batch.IBatchStatus;
import com.google.gdata.model.Element;
import com.google.gdata.model.atom.Entry;

/**
 * New data model version of batch utils.  These utilities can operate on both
 * the old and the new data model, so should be used during the transition.
 *
 * 
 */
public class BatchUtils {

  /**
   * Gets the value of the tag {@code <batch:id>}.
   *
   * @param entry the entry to get the id from
   * @return the batch id or null if it is not set
   */
  public static String getBatchId(IEntry entry) {
    if (entry instanceof Entry) {
      return BatchId.getIdFrom((Entry) entry);
    } else {
      return com.google.gdata.data.batch.BatchUtils.getBatchId(
          (BaseEntry<?>) entry);
    }
  }

  /**
   * Sets the value of the tag {@code <batch:id>}.
   *
   * @param entry entry to get the id from
   * @param id the batch id or null to remove it
   */
  public static void setBatchId(IEntry entry, String id) {
    if (entry instanceof Element) {
      ((Element) entry).setElement(BatchId.KEY,
          (id == null) ? null : new BatchId(id));
    } else {
      com.google.gdata.data.batch.BatchUtils.setBatchId(
          (ExtensionPoint) entry, id);
    }
  }

  /**
   * Gets the batch operation type from the tag {@code <batch:operation>}
   * in a {@link IEntry}.
   *
   * @param entry the entry to get the operation type from
   * @return the operation to execute or null if it's not set
   */
  public static BatchOperationType getBatchOperationType(IEntry entry) {
    if (entry instanceof Element) {
      return getBatchOperationType((Element) entry);
    } else {
      return com.google.gdata.data.batch.BatchUtils.getBatchOperationType(
          (ExtensionPoint) entry);
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
    if (feed instanceof Element) {
      return getBatchOperationType((Element) feed);
    } else {
      return com.google.gdata.data.batch.BatchUtils.getBatchOperationType(
          (ExtensionPoint) feed);
    }
  }

  /**
   * Gets the batch operation type from an element.
   */
  private static BatchOperationType getBatchOperationType(
      Element element) {
    BatchOperation op = element.getElement(BatchOperation.KEY);
    return op == null ? null : op.getType();
  }

  /**
   * Sets the batch operation to execute in a {@link IEntry}.
   *
   * @param entry the entry to set the operation type on
   * @param op batch operation type or null to remove it
   */
  public static void setBatchOperationType(IEntry entry,
      BatchOperationType op) {
    if (entry instanceof Element) {
      setBatchOperationType((Element) entry, op);
    } else {
      com.google.gdata.data.batch.BatchUtils.setBatchOperationType(
          (ExtensionPoint) entry, op);
    }
  }

  /**
   * Sets the batch operation to execute in a {@link BaseFeed}.
   *
   * @param feed
   * @param op batch operation type or null to remove it.
   */
  public static void setBatchOperationType(IFeed feed, BatchOperationType op) {
    if (feed instanceof Element) {
      setBatchOperationType((Element) feed, op);
    } else {
      com.google.gdata.data.batch.BatchUtils.setBatchOperationType(
          (ExtensionPoint) feed, op);
    }
  }

  /**
   * Sets the batch operation type on an element.
   */
  private static void setBatchOperationType(Element entry,
      BatchOperationType op) {
    entry.setElement(BatchOperation.KEY,
        (op == null) ? null : new BatchOperation(op));
  }

  /**
   * Gets the value of the tag {@code <batch:interrupted>}.
   *
   * @return the object corresponding to the tag or null
   * @param entry
   */
  public static IBatchInterrupted getInterrupted(IEntry entry) {
    if (entry instanceof Element) {
      return ((Element) entry).getElement(
          com.google.gdata.model.batch.BatchInterrupted.KEY);
    } else {
      return com.google.gdata.data.batch.BatchUtils.getBatchInterrupted(
          (ExtensionPoint) entry);
    }
  }

  /**
   * Gets the value of the tag {@code <batch:status>}.
   *
   * @return the object corresponding to the tag or null
   * @param entry
   */
  public static IBatchStatus getStatus(IEntry entry) {
    if (entry instanceof Element) {
      return ((Element) entry).getElement(BatchStatus.KEY);
    } else {
      return com.google.gdata.data.batch.BatchUtils.getBatchStatus(
          (ExtensionPoint) entry);
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
   * You'll want to call {@link #getStatus(IEntry)}
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
    IBatchStatus batchStatus = getStatus(entry);
    if (batchStatus == null) {
      throw new IllegalArgumentException("Not a batch response entry; " +
           "Missing BatchStatus extension.");
    }
    return batchStatus.getCode();
  }

  /**
   * Throws a {@link BatchInterrupted} exception if any entry within the feed
   * has a batch interrupted child element.
   *
   * @param ifeed batch response feed to check
   * @throws BatchInterruptedException if batch interrupted entry is found.
   */
  public static void throwIfInterrupted(IFeed ifeed)
      throws BatchInterruptedException {

    int count = ifeed.getEntries().size();
    if (count > 0) {
      IEntry ientry = ifeed.getEntries().get(count - 1);

      IBatchInterrupted interrupted;
      if (ientry instanceof Entry) {
        Entry entry = (Entry) ientry;
        Feed feed = (Feed) ifeed;
        interrupted = entry.getElement(BatchInterrupted.KEY);
      } else if (ientry instanceof BaseEntry) {
        BaseEntry<?> baseEntry = (BaseEntry<?>) ientry;
        BaseFeed<?, ?> baseFeed = (BaseFeed<?, ?>) ifeed;
        interrupted =
          com.google.gdata.data.batch.BatchUtils.getBatchInterrupted(baseEntry);
      } else {
        throw new IllegalStateException("Unrecognized entry type:" +
            ientry.getClass());
      }
      if (interrupted != null) {
        throw new BatchInterruptedException(ifeed, interrupted);
      }
    }
}


  private BatchUtils() {}
}
