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

import com.google.gdata.util.common.xml.XmlWriter;

/**
 * Relevant namespaces for Google Spreadsheets.
 * 
 * 
 */
public final class Namespaces {

  /** Namespace for standard Google Spreadsheet tags. */
  public static final String gSpread =
      "http://schemas.google.com/spreadsheets/2006";

  /** Prefix for individual kinds. */
  public static final String gSpreadPrefix = gSpread + "#";

  /** Google data XML writer namespace. */
  public static final XmlWriter.Namespace gSpreadNs =
      new XmlWriter.Namespace("gs", gSpread);

  /** URI of the schema for custom spreadsheet tags, for list feed. */
  public static final String gSpreadCustom =
      "http://schemas.google.com/spreadsheets/2006/extended";

  /** Namespace for custom spreadsheet tags. */
  public static final XmlWriter.Namespace gSpreadCustomNs =
      new XmlWriter.Namespace("gsx", gSpreadCustom);

  /** Link "rel" for worksheets feed. */
  public static final String WORKSHEETS_LINK_REL =
      gSpreadPrefix + "worksheetsfeed";
  
  /** Link "rel" for list feeds. */
  public static final String LIST_LINK_REL =
      gSpreadPrefix + "listfeed";
  
  /** Link "rel" for cells feeds. */
  public static final String CELLS_LINK_REL =
      gSpreadPrefix + "cellsfeed";

  /** Link "rel" for a url that is a source for a particular cell. */
  public static final String SOURCE_LINK_REL =
    gSpreadPrefix + "source";

  /** Disallows construction for this utility class. */
  private Namespaces() {
  }
  
}
