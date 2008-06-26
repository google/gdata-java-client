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


package sample.authsub.src;

import com.google.gdata.client.http.AuthSubUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Acts as a proxy and retrieves the requested feed.
 * <p>
 * The identity of the user is determined using the provided authentication
 * cookie.  The authentication cookie will be mapped to the associated Google
 * AuthSub token.  The token will be used to retrieve the feed.
 * <p>
 * Special care must be taken to ensure the following:
 * -  the requested feed belongs to a pre-specified approved list
 * -  since cookies are used and GData URLs aren't protected, the URL being
 *    requested by the client should contain a secure hash.  This is primarily
 *    required for POST/PUT/DELETE but shown in the GET example below as an
 *    example.
 *
 * 
 */
public class RetrieveFeedServlet extends HttpServlet {

  private static String[] acceptedFeedPrefixList = {
    "http://www.google.com/calendar/feeds",
    "https://www.google.com/calendar/feeds"
  };

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // Retrieve the authentication cookie to identify user
    String principal =
      Utility.getCookieValueWithName(req.getCookies(), Utility.LOGIN_COOKIE_NAME);
    if (principal == null) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "Unidentified principal.");
      return;
    }

    // Check that the user has an AuthSub token
    String authSubToken = TokenManager.retrieveToken(principal);
    if (authSubToken == null) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "User isn't authorized through AuthSub.");
      return;
    }

    // If no query string, complain
    if (req.getQueryString() == null) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "Query string is required.");
      return;
    }

    // Parse the query parameters
    String queryString = URLDecoder.decode(req.getQueryString(), "UTF-8");
    Map<String,String> queryParams = Utility.parseQueryString(queryString);

    String queryUri = queryParams.get("href");
    String token = queryParams.get("token");
    String timestamp = queryParams.get("timestamp");
    if ((queryUri == null) || (token == null) || (timestamp == null)) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "Missing a query parameter.");
      return;
    }

    // Verify the feed by checking that it's from a known list of feeds and
    // that the secure token hasn't expired and is valid.
    if (!verifyFeedRequest(principal, queryUri, token, timestamp, "GET")) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "Request failed validation.");
      return;
    }

    // Handle a GET request
    handleGetRequest(req, resp, queryUri, authSubToken);
  }

  /**
   * Handles a GET request by issuing a GET to the requested feed with the
   * AuthSub token attached in a header.  The output from the server will
   * be proxied back to the requestor.
   * POST/PUT/DELETE can be handled in a similar manner except that the XML
   * sent as part of the request should be sent to the server.
   */
  private void handleGetRequest(HttpServletRequest req,
                                HttpServletResponse resp,
                                String queryUri,
                                String authSubToken)
      throws ServletException, IOException {
    HttpURLConnection connection = null;
    try {
      connection = openConnectionFollowRedirects(queryUri, authSubToken);
    } catch (GeneralSecurityException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "Error creating authSub header.");
      return;
    } catch (MalformedURLException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "Malformed URL - " + e.getMessage());
      return;
    } catch (IOException e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "IOException - " + e.getMessage());
      return;
    }

    int respCode = connection.getResponseCode();

    // Handle error from remote server
    if (respCode != HttpServletResponse.SC_OK) {
      Map<String, List<String>> headers = connection.getHeaderFields();
      StringBuffer errorMessage = new StringBuffer(
          "Failed to retrive calendar feed from: ");
      errorMessage.append(queryUri);
      errorMessage.append(".\nServer Error Response:\n");
      errorMessage.append(connection.getResponseMessage());

      for (Iterator<String> iter = headers.keySet().iterator() ;
           iter.hasNext();) {
        String header = iter.next();
        List<String> headerValues = headers.get(header);

        for (Iterator<String> headerIter = headerValues.iterator() ;
             headerIter.hasNext(); ) {
          String headerVal = headerIter.next();
          errorMessage.append(header + ": " + headerVal + ", ");
        }
      }

      resp.sendError(respCode, errorMessage.toString());
      return;
    }

    // Handle success reply from remote server
    try {
      BufferedReader reader =
        new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while((line = reader.readLine()) != null) {
        resp.getWriter().write(line);
      }
    } catch(IOException e) {
      // Ignore
    }
  }

  /**
   * Open a HTTP connection to the provided URL with the AuthSub token specified
   * in the header.  Follow redirects returned by the server - a new AuthSub
   * signature will be computed for each of the redirected-to URLs.
   */
  private HttpURLConnection openConnectionFollowRedirects(String urlStr,
                                                          String authSubToken)
      throws MalformedURLException, GeneralSecurityException, IOException {
    boolean redirectsDone = false;
    HttpURLConnection connection = null;
    while (!redirectsDone) {
      URL url = new URL(urlStr);
      // Open connection to requested feed
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("Content-Type",
                                    "application/x-www-form-urlencoded");

      // Form AuthSub authentication header
      String authHeader = null;
      authHeader = AuthSubUtil.formAuthorizationHeader(authSubToken,
                                                       Utility.getPrivateKey(),
                                                       url,
                                                       "GET");
      connection.setRequestProperty("Authorization", authHeader);
      connection.setInstanceFollowRedirects(false);
      int responseCode = connection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
          responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
        urlStr = connection.getHeaderField("Location");
        // If "Location" is not specified, stop following redirects, and
        // propagate error to the client of the proxy
        if (urlStr == null) {
          redirectsDone = true;
        }
      } else {
        redirectsDone = true;
      }
    }
    return connection;
  }


  /**
   * Verifies the request for a feed by:
   * a. validating that the request belongs to a known list of feeds
   * b. validating the token (to protect against url command attacks)
   *<p>
   * This verification is in order to prevent the proxy from URL command attacks
   * which is a cross site scripting problem.
   */
  private boolean verifyFeedRequest(String cookie,
                                    String feed,
                                    String token,
                                    String timestamp,
                                    String method) {
    // Check the list of accepted feed URLs that we are proxying
    int url_i;
    for (url_i = 0; url_i < acceptedFeedPrefixList.length; url_i++) {
      if(feed.toLowerCase().startsWith(
             acceptedFeedPrefixList[url_i].toLowerCase())) {
        break;
      }
    }
    if (url_i == acceptedFeedPrefixList.length) {
      return false;
    }

    return SecureUrl.isTokenValid(token, cookie, feed, method, timestamp);
  }
}
