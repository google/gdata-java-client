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


package com.google.gdata.client.spreadsheet;


import com.google.gdata.client.DocumentQuery;

import java.net.URL;


/**
 * Simple class for worksheets-feed-specific queries.
 *
 * 
 */
public class WorksheetQuery extends DocumentQuery {

  /**
   * Constructs a query for querying worksheets within a spreadsheet.
   * 
   * @param feedUrl the feed's URI
   */
  public WorksheetQuery(URL feedUrl) {
    super(feedUrl);
  }
}
