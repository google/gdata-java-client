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

import com.google.gdata.util.ContentType;

/**
 * Shared BatchInterrupted interface between old and new data models.
 * 
 * 
 */
public interface IBatchInterrupted {

  /**
   * Returns the content of the interrupted element.
   * 
   * @return the content of the element.
   */
  String getContent();
  
  /**
   * Returns the content type of the interrupted element.
   * 
   * @return the content type.
   */
  ContentType getContentType();
  
  /**
   * Returns the number of entries which had errors.
   * 
   * @return the number of entries which failed.
   */
  int getErrorCount();
  
  /**
   * Returns the reason this batch was interrupted.
   * 
   * @return the reason for the interruption.
   */
  String getReason();
  
  /**
   * Returns the number of entries which were parsed but not processed.
   * 
   * @return the number of skipped entries.
   */
  int getSkippedCount();
  
  /**
   * Returns the number of entries which processed successfully.
   * 
   * @return number of successfully processed entries.
   */
  int getSuccessCount();
  
  /**
   * Returns the number of entries which were parsed.
   * 
   * @return total number of parsed entries.
   */
  int getTotalCount();
}
