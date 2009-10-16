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

import com.google.gdata.util.common.base.CharEscapers;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * A general container for methods related to OAuth.
 *
 * 
 */
public class OAuthUtil {

  private OAuthUtil() {
  }

  /**
   * Get a nonce for an OAuth request.  OAuth defines the nonce as "a random
   * string, uniquely generated for each request. The nonce allows the Service
   * Provider to verify that a request has never been made before and helps
   * prevent replay attacks when requests are made over a non-secure channel
   * (such as HTTP)."
   *
   * @return the nonce string to use in an OAuth request
   */
  public static String getNonce() {
    return Long.toString(System.nanoTime());
  }

  /**
   * Get a timestamp for an OAuth request.  OAuth defines the timestamp as
   * "the number of seconds since January 1, 1970 00:00:00 GMT. The timestamp
   * value MUST be a positive integer and MUST be equal or greater than the
   * timestamp used in previous requests."
   *
   * @return the timestamp string to use in an OAuth request.
   */
  public static String getTimestamp() {
    return Long.toString(System.currentTimeMillis() / 1000);
  }

  /**
   * Calculates the signature base url as per section 9.1 of the OAuth Spec.
   * This is a concatenation of http method, request url, and other request
   * parameters.
   *
   * @see <a href="http://oauth.net/core/1.0/#anchor14">9.1 Signature Base
   *      String</a>
   *
   * @param requestUrl the url of the request
   * @param httpMethod the http method, for example "GET" or "PUT"
   * @param baseParameters the request parameters (see section 9.1.3)
   * @return the base string to be used in the OAuth signature
   * @throws OAuthException if the input url is not formatted properly
   */
  public static String getSignatureBaseString(String requestUrl,
      String httpMethod, Map<String, String> baseParameters)
      throws OAuthException {
    return encode(httpMethod.toUpperCase()) + '&'
        + encode(normalizeUrl(requestUrl)) + '&'
        + encode(normalizeParameters(requestUrl, baseParameters));
  }

  /**
   * Calculates the normalized request url, as per section 9.1.2 of the OAuth
   * Spec.  This removes the querystring from the url and the port (if it is
   * the standard http or https port).
   *
   * @see <a href="http://oauth.net/core/1.0/#rfc.section.9.1.2">9.1.2
   *      Construct Request URL</a>
   *
   * @param requestUrl the request url to normalize (not <code>null</code>)
   * @return the normalized request url, as per the rules in the link above
   * @throws OAuthException if the input url is not formatted properly
   */
  public static String normalizeUrl(String requestUrl)
      throws OAuthException {

    // validate the request url
    if ((requestUrl == null) || (requestUrl.length() == 0)) {
      throw new OAuthException("Request Url cannot be empty");
    }

    // parse the url into its constituent parts.
    URI uri;
    try {
      uri = new URI(requestUrl);
    } catch (URISyntaxException e) {
      throw new OAuthException(e);
    }

    String authority = uri.getAuthority();
    String scheme = uri.getScheme();
    if (authority == null || scheme == null) {
      throw new OAuthException("Invalid Request Url");
    }
    authority = authority.toLowerCase();
    scheme = scheme.toLowerCase();

    // if this url contains the standard port, remove it
    if ((scheme.equals("http") && uri.getPort() == 80)
        || (scheme.equals("https") && uri.getPort() == 443)) {
      int index = authority.lastIndexOf(":");
      if (index >= 0) {
          authority = authority.substring(0, index);
      }
    }

    // piece together the url without the querystring
    return scheme + "://" + authority + uri.getRawPath();
  }

  /**
   * Calculates the normalized request parameters string to use in the base
   * string, as per section 9.1.1 of the OAuth Spec.
   *
   * @see <a href="http://oauth.net/core/1.0/#rfc.section.9.1.1">9.1.1
   *      Normalize Request Parameters</a>
   *
   * @param requestUrl the request url to normalize (not <code>null</code>)
   * @param requestParameters key/value pairs of parameters in the request
   * @return the parameters normalized to a string
   */
  public static String normalizeParameters(
      String requestUrl, Map<String, String> requestParameters) {

    // use a TreeMap to alphabetize the parameters by key
    TreeMap<String, String> alphaParams =
        new TreeMap<String, String>(requestParameters);

    // add the querystring to the base string (if one exists)
    if (requestUrl.indexOf('?') > 0) {
      Map<String, String> queryParameters =
          parseQuerystring(requestUrl.substring(requestUrl.indexOf('?')+1));
      alphaParams.putAll(queryParameters);
    }

    // piece together the base string, encoding each key and value
    StringBuilder paramString = new StringBuilder();
    for (Map.Entry<String, String> e : alphaParams.entrySet()) {
      if (e.getValue().length() == 0) {
        continue;
      }
      if (paramString.length() > 0) {
        paramString.append("&");
      }
      paramString.append(encode(e.getKey())).append("=")
          .append(encode(e.getValue()));
    }
    return paramString.toString();
  }

  /**
   * Parse a querystring into a map of key/value pairs.
   *
   * @param queryString the string to parse (without the '?')
   * @return key/value pairs mapping to the items in the querystring
   */
  public static Map<String, String> parseQuerystring(String queryString) {
    Map<String, String> map = new HashMap<String, String>();
    if ((queryString == null) || (queryString.equals(""))) {
      return map;
    }
    String[] params = queryString.split("&");
    for (String param : params)
    {
      try {
        String[] keyValuePair = param.split("=", 2);
        String name = URLDecoder.decode(keyValuePair[0], "UTF-8");
        if (name == "") {
          continue;
        }
        String value = keyValuePair.length > 1 ?
            URLDecoder.decode(keyValuePair[1], "UTF-8") : "";
        map.put(name, value);
      } catch (UnsupportedEncodingException e) {
        // ignore this parameter if it can't be decoded
      }
    }
    return map;
  }

  /**
   * Formats the input string for inclusion in a url.  Account for the
   * differences in how OAuth encodes certain characters (such as the
   * space character).
   *
   * @param stringToEncode the string to encode
   * @return the url-encoded string
   */
  public static String encode(String stringToEncode) {
    return CharEscapers.uriEscaper().escape(stringToEncode).replace("+", "%20")
    .replace("*", "%2A").replace("%7E", "~");
  }
}
