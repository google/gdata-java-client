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
 * Shared BatchStatus interface between old and new data models.
 * 
 * 
 */
public interface IBatchStatus {

  /**
   * Returns the status code associated with this batch status element.
   * 
   * @return the status code.
   */
  int getCode();
  
  /**
   * Returns the content of the batch status element.
   * 
   * @return the content of the element.
   */
  String getContent();
  
  /**
   * Returns the content type associated with this status element.
   * 
   * @return the content type of the element.
   */
  ContentType getContentType();
  
  /**
   * Returns the reason for the batch status.
   * 
   * @return the reason for the status.
   */
  String getReason();
}
