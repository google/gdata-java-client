/* Copyright (c) 2006 Google Inc.                                                                                                                                                                                
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

package sample.gbase.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Dumps to the console the response of a Google Base data API query. 
 */
public class QueryExample1 {
  /**
   * Url of the Google Base data API snippet feed.
   */
  private static final String SNIPPETS_FEED = "http://base.google.com/base/feeds/snippets";

  /**
   * The query that is sent over to the Google Base data API server.
   */
  private static final String QUERY = "cars [item type : products]";

  /**
   * Connect to <code>SNIPPETS_FEED</code> and display the response. 
   * @throws IOException if an error occured while connecting or reading from 
   *         the feed
   */
  public void displayItems() throws IOException {
    URL url = new URL(SNIPPETS_FEED + "?bq=" + 
        URLEncoder.encode(QUERY, "UTF-8"));
 
    HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
    InputStream inputStream = httpConnection.getInputStream();
    
    int ch;
    while ((ch=inputStream.read()) > 0) {
      System.out.print((char)ch);
    }
  }

  /**
   * The main method simply creates a <code>QueryExample2</code> instance and
   * calls <code>displayItems</code>. 
   */
  public static void main(String[] args) throws IOException {
    new QueryExample1().displayItems();
  }
}
