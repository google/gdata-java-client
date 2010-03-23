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


package com.google.gdata.data.docs;

/**
 * Describes a document list link.
 *
 * 
 */
public class DocumentListLink {

  /** Link relation type. */
  public static final class Rel {

    /** Link for the zip file contains all exported documents. */
    public static final String EXPORT = DocsNamespace.DOCS_PREFIX + "export";

    /** Link for the parent folder entry. */
    public static final String PARENT = DocsNamespace.DOCS_PREFIX + "parent";

    /** Link for the feed of a spreadsheet's tables. */
    public static final String TABLES_FEED =
        "http://schemas.google.com/spreadsheets/2006#tablesfeed";

    /** Link for the feed of a spreadsheet's worksheets. */
    public static final String WORKSHEETS_FEED =
        "http://schemas.google.com/spreadsheets/2006#worksheetsfeed";

  }

  /** MIME type of link target. */
  public static final class Type {

    /** Type for the zip file. */
    public static final String APPLICATION_ZIP = "application/zip";

  }

  /** Private constructor to ensure class is not instantiated. */
  private DocumentListLink() {}

}

