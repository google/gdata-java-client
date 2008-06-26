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


package com.google.gdata.client.batch;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.batch.BatchInterrupted;
import com.google.gdata.util.ServiceException;

/**
 * Exception thrown by the client when an entry with a
 * {@code <batch:interrupted>} tag has been detected.
 *
 * 
 */
public class BatchInterruptedException
    extends ServiceException {
  private final BaseFeed feed;
  private final BatchInterrupted interrupted;

  /**
   * Creates a BatchInterrupted exception.
   *
   * @param feed
   * @param interrupted
   */
  public BatchInterruptedException(BaseFeed feed,
      BatchInterrupted interrupted) {
    super("Batch Interrupted (some operations might have succeeded) : " +
        interrupted.getReason());
    this.feed = feed;
    this.interrupted = interrupted;
  }

  /**
   * Gets the {@code <batch:interrupted>} tag that describes
   * what went wrong.
   *
   * @return BatchInterrupted object
   */
  public BatchInterrupted getBatchInterrupted() {
    return interrupted;
  }

  /**
   * Gets the interrupted feed, which might contain reports of succesful
   * operations.
   *
   * The last entry on the feed is always the entry that contains the
   * {@link BatchInterrupted} object.
   *
   * @return the feed
   */
  public BaseFeed getFeed() {
    return feed;
  }
}
