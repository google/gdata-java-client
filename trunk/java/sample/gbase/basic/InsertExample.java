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
 *  Add a new item to Google Base using the Google Base data API server.
 */
public class InsertExample {
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
   * The main method constructs a <code>InsertExample</code> instance, obtains an 
   * authorization token and posts a new item to Google Base.
   */
  public static void main(String[] args) throws MalformedURLException, IOException {
    InsertExample insertExample = new InsertExample();
    String token = insertExample.authenticate();
    System.out.println("Obtained authorization token: " + token);
    insertExample.postItem(token);
  }

  /**
   * Inserts <code>DATA_ITEM</code> by making a POST request to
   * <code>ITEMS_URL<code>.
   * @param token authentication token obtained using <code>authenticate</code>
   * @throws IOException if an I/O exception occurs while creating/writing/
   *         reading the request
   */
  public void postItem(String token) throws IOException {
    HttpURLConnection connection = (HttpURLConnection)(new URL(ITEMS_FEED)).openConnection();
    
    connection.setDoInput(true);
    connection.setDoOutput(true);
    
    // Set the properties of the connection: the Http method, the content type
    // of the POST request and the authorization header 
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/atom+xml");
    connection.setRequestProperty("Authorization", "GoogleLogin auth=" + token);

    // Post the data item
    OutputStream outputStream = connection.getOutputStream();
    outputStream.write(DATA_ITEM.getBytes());
    outputStream.close();
  
    // Retrieve the output
    int responseCode = connection.getResponseCode();
    InputStream inputStream;
    if (responseCode == HttpURLConnection.HTTP_CREATED) {
      inputStream = connection.getInputStream();
    } else {
      inputStream = connection.getErrorStream();
    }
    
    // write the output to the console
    System.out.println(toString(inputStream));
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
   * Makes a HTTP POST request to the provided {@code url} given the provided
   * {@code parameters}. It returns the output from the POST handler as a
   * String object.
   * 
   * @param url the URL to post the request
   * @return the output from the handler
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
    String string;
    StringBuilder outputBuilder = new StringBuilder();
    if (inputStream != null) {
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
      while (null != (string = reader.readLine())) {
        outputBuilder.append(string).append('\n');
      }
    }
    return outputBuilder.toString();
  }
}
