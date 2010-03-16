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


package com.google.gdata.client.authn.oauthproxy;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Stores the variables related to an OAuth Proxy response.
 *
 * 
 */
public class OAuthProxyResponse extends HashMap<String, String> {

  public OAuthProxyResponse() {
    super();
  }

  /**
   * Creates an {@link OAuthProxyResponse} object from a map of http headers,
   * such as one obtained from {@link HttpURLConnection#getHeaderFields()}.
   *
   * @param headers A map of http headers.
   */
  public OAuthProxyResponse(Map<String, List<String>> headers) {
    List<String> responseHeaders = Lists.newArrayList(
        OAuthProxyProtocol.Header.X_OAUTH_APPROVAL_URL,
        OAuthProxyProtocol.Header.X_OAUTH_ERROR,
        OAuthProxyProtocol.Header.X_OAUTH_ERROR_TEXT,
        OAuthProxyProtocol.Header.X_OAUTH_STATE);
    for (Entry<String, List<String>> entry : headers.entrySet()) {
      if (responseHeaders.contains(entry.getKey())) {
        put(entry.getKey(), entry.getValue().get(0));
      }
    }
  }

  /**
   * Retrieves the approval url.  If this url is present in a request, the
   * consumer should redirect the user to this url to reauthorize.
   */
  public String getApprovalUrl() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_APPROVAL_URL);
  }

  /** Sets the approval url. */
  public void setApprovalUrl(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_APPROVAL_URL, value);
  }

  /** Retrieves the OAuth error. */
  public String getError() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_ERROR);
  }

  /** Sets the OAuth error. */
  public void setError(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_ERROR, value);
  }

  /** Retrieves the OAuth error text. */
  public String getErrorText() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_ERROR_TEXT);
  }

  /** Sets the OAuth error text. */
  public void setErrorText(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_ERROR_TEXT, value);
  }

  /** Retrieves the OAuth state. */
  public String getState() {
    return get(OAuthProxyProtocol.Header.X_OAUTH_STATE);
  }

  /** Sets the OAuth state. */
  public void setState(String value) {
    put(OAuthProxyProtocol.Header.X_OAUTH_STATE, value);
  }
}
