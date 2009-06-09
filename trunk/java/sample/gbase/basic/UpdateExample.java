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
 * Update a Google Base data item using the Google Base data API server.
 */
public class UpdateExample {
  /**
   * URL of the authenticated customer feed.
   */
  private static final String ITEMS_FEED = "http://base.google.com/base/feeds/items";

  /**
   * The data item we are going to insert, in XML/Atom format.
   */
  private static final String DATA_ITEM = 
        "<?xml version='1.0'?>\n" +
        "<entry xmlns='http://www.w3.org/2005/Atom'\n" +
        "    xmlns:g='http://base.google.com/ns/1.0'>\n" +
        "  <g:item_type type='text'>testrecipes</g:item_type>\n" +
        "  <title type='text'>Fabulous cheese cake</title>\n" +
        "  <content type='xhtml'>All that you need is lots of patience.</content>\n" +
        "</entry>";

  /**
   * The updated data item, in XML/Atom format. It adds an additional label to
   * <code>DATA_ITEM</code>.
   */
  private static final String NEW_DATA_ITEM = 
        "<?xml version='1.0'?>\n" +
        "<entry xmlns='http://www.w3.org/2005/Atom'\n" +
        "    xmlns:g='http://base.google.com/ns/1.0'>\n" +
        "  <g:item_type type='text'>testrecipes</g:item_type>\n" +
        "  <title type='text'>Yummy cheese cake</title>\n" +
        "  <content type='xhtml'>All that you need is lots of patience.</content>\n" +
        "  <g:main_ingredient type='text'>cheese</g:main_ingredient>\n" +
        "</entry>";

  /**
   * URL used for authenticating and obtaining an authentication token. 
   * More details about how it works:
   * <code>http://code.google.com/apis/accounts/AuthForInstalledApps.html<code>
   */
  private static final String AUTHENTICATION_URL = 
      "https://www.google.com/accounts/ClientLogin";
  
  /**
   * Fill in your Google Account email here.
   */
  private static final String EMAIL = "";
  
  /**
   * Fill in your Google Account password here.
   */
  private static final String PASSWORD = "";
  
  /**
   * The main method constructs an <code>UpdateExample</code> instance,
   * obtains an authorization token, posts a new item to Google Base, extracts
   * the new item's id from the reponse and uses it to update the item.
   */
  public static void main(String[] args) 
      throws MalformedURLException, IOException {
    UpdateExample updateExample = new UpdateExample();
    String token = updateExample.authenticate();
    System.out.println("Obtained authorization token: " + token);
  
    System.out.println("Posting item:\n" + DATA_ITEM);
    String itemUrl = updateExample.extractItemUrlFromResponse(
        updateExample.postItem(token));
    
    System.out.println("Updating item: " + itemUrl);
    String updateResponse = updateExample.updateItem(token, itemUrl);
    System.out.println(updateResponse);
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
   * Parse the POST XML response returned by <code>insertResponse</code> and
   * extract the Google Base item id of the newly created item. To keep parsing
   * simple, we assume that the first "id" tag contains the Atom item id; this
   * is true for the Google Base data API servers, but it's not enforced by Atom -
   * see how the title is parsed in {@link QueryExample2}
   * @param insertResponse the response sent by the Google Base data API server
   *        after the insert operation
   * @return the Url that identifies the item, for example: <code>
   *         http://base.google.com/base/feeds/items/18020038538902937385</code>
   */
  public String extractItemUrlFromResponse(String insertResponse) {
    int startIndex = insertResponse.indexOf("<id>") + 4;
    int endIndex = insertResponse.indexOf("</id>");
    String itemUrl = insertResponse.substring(startIndex, endIndex);
    return itemUrl;
  }

  /**
   * Inserts <code>DATA_ITEM</code> by making a POST request to
   * <code>ITEMS_FEED<code>.
   * @param token authentication token obtained using <code>authenticate</code>
   * @return the Google Base data API server's insert response
   * @throws IOException if an I/O exception occurs while creating/writing/
   *                     reading the request
   */
  public String postItem(String token) throws IOException {
    return makeHttpRequest(token, ITEMS_FEED, DATA_ITEM, "POST", 
        HttpURLConnection.HTTP_CREATED);
  }
  
  /**
   * Updates <code>NEW_DATA_ITEM</code> by making a PUT request to
   * <code>itemUrl</code>.
   * 
   * @param token authentication token obtained using <code>authenticate</code>
   * @param itemUrl the identifier of the item to update, which has the form
   *          <code>ITEMS_URL + "/itemId"</code>
   * @return the Google Base data API server's update response
   * @throws IOException if an I/O exception occurs while creating/writing/
   *           reading the request
   */
  public String updateItem(String token, String itemUrl) 
      throws MalformedURLException, IOException {
    return makeHttpRequest(token, itemUrl, NEW_DATA_ITEM, "PUT", 
        HttpURLConnection.HTTP_OK);
  }
  
  /**
   * Make an Http request to <code>url</code>, using <code>httpMethod</code>,
   * post <code>item</code> and return the response.
   * 
   * @param token authentication token obtained using <code>authenticate</code>
   * @param url the identifier of the item to update, which has the form
   *          <code>ITEMS_URL + "/itemId"</code>
   * @param item the item to be posted via the request
   * @param httpMethod the httpMethod to use for posting (should be PUT or POST)
   * @param expectedResponseCode the response code returned in case of a
   *          successful operation
   * @return the Google Base data API update response
   * @throws IOException if an I/O exception occurs while
   *           creating/writing/reading the request
   */  
  private String makeHttpRequest(String token, String url, String item, 
      String httpMethod, int expectedResponseCode) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)(new URL(url)).openConnection() ;
    
    connection.setDoInput(true);
    connection.setDoOutput(true);
    
    // Set the properties of the connection: the http request method, the 
    // content type and the authorization header
    connection.setRequestMethod(httpMethod);
    connection.setRequestProperty("Content-Type", "application/atom+xml");
    connection.setRequestProperty("Authorization", "GoogleLogin auth=" + token);

    // Post the data item
    OutputStream outputStream = connection.getOutputStream();
    outputStream.write(item.getBytes());
    outputStream.close();

    // Retrieve the output
    int responseCode = connection.getResponseCode();
    if (responseCode == expectedResponseCode) {
      return toString(connection.getInputStream());
    } else {
      throw new RuntimeException(toString(connection.getErrorStream()));
    }
  }


  /**
   * Makes a HTTP POST request to the provided {@code url} given the provided
   * {@code parameters}. It returns the output from the POST handler as a
   * String object.
   * 
   * @param url the URL to post the request
   * @return the output from the server
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
    content.append("&source=").append(URLEncoder.encode("Google Base data API example", "UTF-8"));
    content.append("&service=").append(URLEncoder.encode("gbase", "UTF-8"));
  
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
    String lineToRead;
    StringBuilder outputBuilder = new StringBuilder();
    if (inputStream != null) {
      BufferedReader reader =
        new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
      while (null != (lineToRead = reader.readLine())) {
        outputBuilder.append(lineToRead).append('\n');
      }
    }
    return outputBuilder.toString(); 
  }
}
