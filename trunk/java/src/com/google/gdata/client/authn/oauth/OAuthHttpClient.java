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


package com.google.gdata.client.authn.oauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A simple client for making http requests and returning the response body.
 * Uses {@link java.net.HttpURLConnection} to make http requests.
 *
 * 
 */
public class OAuthHttpClient {

  /**
   * Makes an http request to the input URL, and returns the response body as a
   * string.
   *
   * @param url the url to make the request to
   * @return the response body of the request
   * @throws OAuthException if there was an error making the request
   */
  public String getResponse(URL url) throws OAuthException {
    return getResponse(url, null);
  }

  /**
   * Makes an http request to the input URL, and returns the response body as a
   * string.
   *
   * @param url the url to make the request to
   * @param headers any headers to add to the request
   * @return the response body of the request
   * @throws OAuthException if there was an error making the request
   */
  public String getResponse(URL url, Map<String, String> headers)
      throws OAuthException {
      try {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (headers != null) {
          for (Entry<String, String> entry : headers.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
          }
        }
        connection.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        in.close();
        return response.toString();
      } catch (IOException e) {
        throw new OAuthException("Error getting HTTP response", e);
      }
  }
}
