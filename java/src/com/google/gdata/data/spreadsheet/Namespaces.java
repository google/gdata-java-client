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

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * GData namespace definitions related to Google Spreadsheets.
 *
 * 
 */
public class Namespaces {

  private Namespaces() {}

  /** Google Spreadsheets (GS) namespace */
  public static final String gSpread =
      "http://schemas.google.com/spreadsheets/2006";

  /** Google Spreadsheets (GS) namespace prefix */
  public static final String gSpreadPrefix = gSpread + "#";

  /** Google Spreadsheets (GS) namespace alias */
  public static final String gSpreadAlias = "gs";

  /** XML writer namespace for Google Spreadsheets (GS) */
  public static final XmlNamespace gSpreadNs = new XmlNamespace(gSpreadAlias,
      gSpread);

  /** Google Spreadsheets custom tag (GSX) namespace */
  public static final String gSpreadCustom =
      "http://schemas.google.com/spreadsheets/2006/extended";

  /** Google Spreadsheets custom tag (GSX) namespace prefix */
  public static final String gSpreadCustomPrefix = gSpreadCustom + "#";

  /** Google Spreadsheets custom tag (GSX) namespace alias */
  public static final String gSpreadCustomAlias = "gsx";

  /** XML writer namespace for Google Spreadsheets custom tag (GSX) */
  public static final XmlNamespace gSpreadCustomNs = new
      XmlNamespace(gSpreadCustomAlias, gSpreadCustom);

  /** Gviz data source for the worksheet (GVIZ) namespace */
  public static final String GVIZ =
      "http://schemas.google.com/visualization/2008";

  /** Gviz data source for the worksheet (GVIZ) namespace prefix */
  public static final String GVIZ_PREFIX = GVIZ + "#";


  /** Link "rel" for tables feed. */
  public static final String TABLES_LINK_REL =
      gSpreadPrefix + "tablesfeed";

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

 /** Link "rel" for GViz. */
  public static final String GVIZ_LINK_REL =
      GVIZ_PREFIX + "visualizationApi";
}
