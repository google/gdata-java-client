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


package com.google.gdata.data.spreadsheet;

/**
 * Extends the base Link class with Google Spreadsheets extensions.
 *
 * 
 */
public class SpreadsheetLink {

  /** Link relation type. */
  public static final class Rel {

    /** Link for the feed of a worksheet's cells. */
    public static final String CELLS_FEED = Namespaces.gSpreadPrefix +
        "cellsfeed";

    /** Link for the Gviz resource of a worksheet. */
    public static final String GVIZ = Namespaces.GVIZ_PREFIX +
        "visualizationApi";

    /** Link for the feed of a worksheet's rows. */
    public static final String LIST_FEED = Namespaces.gSpreadPrefix +
        "listfeed";

    /** Link for the feed of a table's records. */
    public static final String RECORDS_FEED = Namespaces.gSpreadPrefix +
        "recordsfeed";

    /** Link for the feed of a spreadsheet's worksheets. */
    public static final String WORKSHEETS_FEED = Namespaces.gSpreadPrefix +
        "worksheetsfeed";

  }

  /** Private constructor to ensure class is not instantiated. */
  private SpreadsheetLink() {}

}
