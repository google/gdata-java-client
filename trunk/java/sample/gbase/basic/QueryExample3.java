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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 * Display all items of a specific customer.
 */
public class QueryExample3 {
  /**
   * URL of the authenticated customer feed.
   */
  private static final String ITEMS_FEED = "http://base.google.com/base/feeds/items";

  /**
   * URL used for authenticating and obtaining an authentication token. 
   * More details about how it works:
   * <code>http://code.google.com/apis/accounts/AuthForInstalledApps.html<code>
   */
  private static final String AUTHENTICATION_URL = "https://www.google.com/accounts/ClientLogin";

  /**
   * Fill in your Google Account email here.
   */
  private static final String EMAIL = "";
  
  /**
   * Fill in your Google Account password here.
   */
  private static final String PASSWORD = "";
  
  /**
   * Create a <code>QueryExample3</code> instance and call
   * <code>displayMyItems</code>, which displays all items that belong to the
   * currently authenticated user.
   */
  public static void main(String[] args) throws IOException {
    QueryExample3 queryExample = new QueryExample3();
    String token = queryExample.authenticate();
    queryExample.displayMyItems(token);
  }

  /**
   * Retrieves the authentication token for the provided set of credentials.
   * @return the authorization token that can be used to access authenticated
   *         Google Base data API feeds
   */
  public String authenticate() {
    // create the login request
    String postOutput = null;
    try {
      URL url = new URL(AUTHENTICATION_URL);
      postOutput = makeLoginRequest(url);
    } catch (IOException e) {
      System.out.println("Could not connect to authentication server: " 
          + e.toString());
      System.exit(1);
    }
  
    // Parse the result of the login request. If everything went fine, the 
    // response will look like
    //      HTTP/1.0 200 OK
    //      Server: GFE/1.3
    //      Content-Type: text/plain 
    //      SID=DQAAAGgA...7Zg8CTN
    //      LSID=DQAAAGsA...lk8BBbG
    //      Auth=DQAAAGgA...dk3fA5N
    // so all we need to do is look for "Auth" and get the token that comes after it
  
    StringTokenizer tokenizer = new StringTokenizer(postOutput, "=\n ");
    String token = null;
    
    while (tokenizer.hasMoreElements()) {
      if (tokenizer.nextToken().equals("Auth")) {
        if (tokenizer.hasMoreElements()) {
          token = tokenizer.nextToken(); 
        }
        break;
      }
    }
    if (token == null) {
      System.out.println("Authentication error. Response from server:\n" + postOutput);
      System.exit(1);
    }
    return token;
  }

  /**
   * Displays the "items" feed, that is the feed that contains the items that
   * belong to the currently authenticated user.
   * 
   * @param token the authorization token, as returned by
   *        <code>authenticate<code>
   * @throws IOException if an IOException occurs while creating/reading the 
   *         request
   */
  public void displayMyItems(String token) throws MalformedURLException, IOException {
    HttpURLConnection connection = (HttpURLConnection)(new URL(ITEMS_FEED)).openConnection() ;
    // Set properties of the connection
    connection.setRequestMethod("GET");
    connection.setRequestProperty("Authorization", "GoogleLogin auth=" + token);

    int responseCode = connection.getResponseCode();
    InputStream inputStream;
    if (responseCode == HttpURLConnection.HTTP_OK) {
      inputStream = connection.getInputStream();
    } else {
      inputStream = connection.getErrorStream();
    }
    
    System.out.println(toString(inputStream));
   }
  
  /**
   * Makes a HTTP POST request to the provided {@code url} given the provided
   * {@code parameters}. It returns the output from the POST handler as a
   * String object.
   * 
   * @param url the URL to post the request
   * @return the output from the Google Accounts server, as string
   * @throws IOException if an I/O exception occurs while
   *           creating/writing/reading the request
   */
  private String makeLoginRequest(URL url)
      throws IOException {
    // Create a login request. A login request is a POST request that looks like
    // POST /accounts/ClientLogin HTTP/1.0
    // Content-type: application/x-www-form-urlencoded
    // Email=johndoe@gmail.com&Passwd=north23AZ&service=gbase&source=Insert Example

    // Open connection
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
  
    
    // Set properties of the connection
    urlConnection.setRequestMethod("POST");
    urlConnection.setDoInput(true);
    urlConnection.setDoOutput(true);
    urlConnection.setUseCaches(false);
    urlConnection.setRequestProperty("Content-Type",
                                     "application/x-www-form-urlencoded");
  
    // Form the POST parameters
    StringBuilder content = new StringBuilder();
    content.append("Email=").append(URLEncoder.encode(EMAIL, "UTF-8"));
    content.append("&Passwd=").append(URLEncoder.encode(PASSWORD, "UTF-8"));
    content.append("&service=").append(URLEncoder.encode("gbase", "UTF-8"));
    content.append("&source=").append(URLEncoder.encode("Google Base data API example", "UTF-8"));

    OutputStream outputStream = urlConnection.getOutputStream();
    outputStream.write(content.toString().getBytes("UTF-8"));
    outputStream.close();
  
    // Retrieve the output
    int responseCode = urlConnection.getResponseCode();
    InputStream inputStream;
    if (responseCode == HttpURLConnection.HTTP_OK) {
      inputStream = urlConnection.getInputStream();
    } else {
      inputStream = urlConnection.getErrorStream();
    }
  
    return toString(inputStream);
  }

  /**
   * Writes the content of the input stream to a <code>String<code>.
   */
  private String toString(InputStream inputStream) throws IOException {
    String string;
    StringBuilder outputBuilder = new StringBuilder();
    if (inputStream != null) {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(inputStream));
      while (null != (string = reader.readLine())) {
        outputBuilder.append(string).append('\n');
      }
    }
    return outputBuilder.toString();
  }
}
