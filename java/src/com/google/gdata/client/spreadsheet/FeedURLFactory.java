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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Provides feed URLs that can be used with a Spreadsheets server.
 *
 * 
 * 
 */
public class FeedURLFactory {

  /**
   * URL of the server to connect to by default.
   * Currently {@value #DEFAULT_SPREADSHEETS_URL}.
   */
  public static final String DEFAULT_SPREADSHEETS_URL =
      "https://spreadsheets.google.com";

  private static final String SPREADSHEETS_PATH = 
      "feeds/spreadsheets/private/full";
  private static final String WORKSHEETS_PATH = "feeds/worksheets/";
  private static final String LIST_PATH = "feeds/list/";
  private static final String CELLS_PATH = "feeds/cells/";
  private static final String TABLE_PATH = "/tables/";
  private static final String RECORD_PATH = "/records/";
  private static final String BASE_PATH = "feeds/";
      

  /** The url used as a base when creating urls. */
  private URL baseUrl;

  private URL feedSpreadsheets;

  private URL feedWorksheets;

  private URL feedList;

  private URL feedCells;

  /**
   * The default FeedURLFactory instance targeted
   * to the default URL.
   */
  private static final FeedURLFactory instance = new FeedURLFactory();


  /**
   * Gets the default instance of this factory, targeted
   * to {@value #DEFAULT_SPREADSHEETS_URL}.
   *
   * @return the default FeedURLFactory
   */
  public static FeedURLFactory getDefault() {
    return instance;
  }


  /**
   * Creates an URL factory targeted to {@value #DEFAULT_SPREADSHEETS_URL}.
   *
   * Access it using {@link #getDefault()}.
   */
  private FeedURLFactory() {
    try {
      init(DEFAULT_SPREADSHEETS_URL);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Unexpected malformed URL", e);
    }
  }

  /**
   * Creates an URL factory targeted to a server.
   *
   * As long as you don't need to connect to a nonstandard
   * URL, different from {@value #DEFAULT_SPREADSHEETS_URL}, you should
   * consider calling {@link #getDefault()} instead.
   *
   * @param url an URL used as a base for the generated URLs
   * @throws MalformedURLException
   */
  public FeedURLFactory(String url) throws MalformedURLException {
    init(url);
  }
  
  private void init(String url) throws MalformedURLException {
    if (!url.endsWith("/")) {
      url += "/";
    }

    baseUrl = new URL(url);
    feedSpreadsheets = new URL(baseUrl, SPREADSHEETS_PATH);
    feedWorksheets = new URL(baseUrl, WORKSHEETS_PATH);
    feedList = new URL(baseUrl, LIST_PATH);
    feedCells = new URL(baseUrl, CELLS_PATH);
  }

  /** Returns the URL used as a base for the generated URLs. */
  public URL getBaseUrl() {
    return baseUrl;
  }
  
  /** 
   * Encodes a string using UTF-8. 
   * 
   * @param s string to be encoded
   * @throws RuntimeException when the JVM does not support UTF-8
   */
  private String encode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UTF-8 is not supported by the JVM", e);
    }
  }

  /**
   * Gets a URL you can use to get a SpreadsheetFeed of all your
   * spreadsheets.
   */
  public URL getSpreadsheetsFeedUrl() {
    return feedSpreadsheets;
  }

  /**
   * Creates a URL you can use to get a WorksheetFeed of all the
   * worksheets within a spreadsheet.
   *
   * This requires the spreadsheet key, which can be obtained from the
   * URL of the AJAX page through {@link #getSpreadsheetKeyFromUrl(String)},
   * or via using the My Spreadsheets feed.
   *
   * @param spreadsheetKey a spreadsheet key, like o1123123.12312312
   */
  public URL getWorksheetFeedUrl(String spreadsheetKey, 
      String visibility, String projection) throws MalformedURLException {
    if (spreadsheetKey == null) {
      throw new NullPointerException("spreadsheetKey is null");
    }
    return makeUrl(feedWorksheets, encode(spreadsheetKey), 
        visibility, projection);
  }

  /**
   * Creates a url that you can use to get a Table Feed of all the tables 
   * within a spreadsheet.  
   * @param spreadsheetKey key of the workbook to get the table feed from. 
   * @throws MalformedURLException
   */
  public URL getTableFeedUrl(String spreadsheetKey) 
      throws MalformedURLException {
    if (spreadsheetKey == null) {
      throw new NullPointerException("spreadsheetKey is null");
    }
    return new URL(baseUrl, BASE_PATH + encode(spreadsheetKey) + TABLE_PATH);
  }
  
  /**
   * Creates a url that you can use to get a feed of records from a table. 
   * @param spreadsheetKey key of the workbook to get the table feed from.
   * @param tableId id of the table to get a record feed from.
   * @throws MalformedURLException
   */
  public URL getRecordFeedUrl(String spreadsheetKey, String tableId) 
      throws MalformedURLException {
    if (spreadsheetKey == null) {
      throw new NullPointerException("spreadsheetKey is null");
    }
    return new URL(baseUrl, BASE_PATH + encode(spreadsheetKey) + RECORD_PATH
        + tableId);
  }
  
  /**
   * Creates a URL you can use to get a ListFeed, which treats
   * the spreadsheet as a list of rows.
   *
   * Like {@link #getWorksheetFeedUrl(String, String, String)}, this requires 
   * the spreadsheet key.
   *
   * This also requires the worksheet identifier.  See the
   * documentation on how worksheets can be identified.
   *
   * @param spreadsheetKey a spreadsheet key, like 01123123.12312312
   * @param worksheetId a worksheet identifier or a 1-based positional indicator
   */
  public URL getListFeedUrl(String spreadsheetKey, String worksheetId, 
      String visibility, String projection) throws MalformedURLException {
    return makeUrl(feedList, spreadsheetKey, worksheetId,
        visibility, projection);
  }

  /**
   * Creates a URL you can use to get a CellFeed, which treats
   * the spreadsheet like spatially oriented cells.
   *
   * Like {@link #getWorksheetFeedUrl(String, String, String)}, this requires 
   * the spreadsheet key.
   *
   * This also requires the worksheet identifier.  See the
   * documentation on how worksheets can be identified.
   *
   * @param spreadsheetKey a spreadsheet key, like 01123123.12312312
   * @param worksheetId a worksheet identifier or a 1-based positional indicator
   */
  public URL getCellFeedUrl(String spreadsheetKey, String worksheetId,
      String visibility, String projection) throws MalformedURLException {
    return makeUrl(feedCells, spreadsheetKey, worksheetId,
        visibility, projection);
  }


  /**
   * Creates a new URL by urlencoding the parameters and adding 
   * "spreadsheetKey/parentResourceId/visibility/projection" 
   * to the provided url.
   */
  private URL makeUrl(URL url, String spreadsheetKey, String parentResourceId, 
      String visibility, String projection) throws MalformedURLException {
    if (spreadsheetKey == null) {
      throw new NullPointerException("spreadsheetKey is null");
    }
    if (parentResourceId == null) {
      throw new NullPointerException("worksheetId is null");
    }
    String path = encode(spreadsheetKey) + "/" + encode(parentResourceId);
    return makeUrl(url, path, visibility, projection);
  }

  /**
   * Creates a new URL by urlencoding visibility, projection and adding 
   * "path/visibility/projection" to the provided url.
   */
  private URL makeUrl(URL url, String path, 
      String visibility, String projection) throws MalformedURLException {
    if (visibility == null) {
      throw new NullPointerException("visibility is null");
    }
    if (projection == null) {
      throw new NullPointerException("projection is null");
    }
    path = path + "/" + encode(visibility) + "/" + encode(projection);
    return new URL(url, path);
  }

  /**
   * Turns a Google Spreadsheets URL directly into the spreadsheet key.
   * 
   * @param url a URL like
   *        http://abcd.spreadsheets.google.com/ccc?id=o1231.1231.1231.1231;
   *        if just the ID is specified, this will also work
   * @return the spreadsheet key (o1231.1231)
   * @throws IllegalArgumentException if the URL is not formatted correctly
   */
  public static String getSpreadsheetKeyFromUrl(String url)
       throws IllegalArgumentException {

    // Make it a URL
    URL urlAsUrl;
    try {
      urlAsUrl = new URL(url);
      String query = urlAsUrl.getQuery();
      if ( query != null ) {
     
        String[] parts = query.split("&");
      
        int offset = -1;
        int numParts = 0;
        String keyOrId = "";

        for (String part : parts) {
          if (part.startsWith("id=")) {
            offset = ("id=").length();
            keyOrId = part.substring(offset);
            numParts = 4;
            break;
          } else if (part.startsWith("key=")) {
            offset = ("key=").length();
            keyOrId = part.substring(offset);
            if (keyOrId.startsWith("p")) {
              return keyOrId;
            }
            numParts = 2;
            break;
          }
        }

        if (offset > -1) {
          String[] dottedParts = keyOrId.split("\\.");
          if (dottedParts.length == numParts) {
            return dottedParts[0] + "." + dottedParts[1];
          }
        }
      }
      
    } catch ( MalformedURLException e ) {
      // This is not a URL, maybe it is just an id
      String[] dottedParts = url.split("\\.");
      
      if (dottedParts.length == 4 || dottedParts.length == 2) {
        return dottedParts[0] + "." + dottedParts[1];
      }    
    }
    
    throw new IllegalArgumentException("Uknown URL format.");
  }


}
