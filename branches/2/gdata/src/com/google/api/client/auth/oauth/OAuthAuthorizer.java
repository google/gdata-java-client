/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.auth.oauth;

import com.google.api.client.auth.Authorizer;
import com.google.api.client.http.UriEntity;
import com.google.api.client.util.ArrayMap;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

/**
 * OAuth 1.0a authorizer based on the generic OAuth parameters.
 * <p>
 * There are a few features not supported by this implementation:
 * <ul>
 * <li>{@code PLAINTEXT} signature algorithm</li>
 * <li>{@code "application/x-www-form-urlencoded"} HTTP request body</li>
 * <li>{@code "oauth_*"} parameters specified in the HTTP request URL (instead
 * assumes they are specified in the {@code Authorization} header</li>
 * </ul>
 */
public final class OAuthAuthorizer implements Authorizer {

  /** Secure random number generator to sign requests. */
  private static final SecureRandom RANDOM = new SecureRandom();

  /** Required OAuth signature algorithm. */
  public volatile OAuthSigner signer;

  /**
   * Absolute URI back to which the server will redirect the resource owner when
   * the Resource Owner Authorization step is completed.
   */
  public volatile String callback;

  /**
   * Required identifier portion of the client credentials (equivalent to a
   * username).
   */
  public volatile String consumerKey;

  /** Required nonce value. Should be computed using {@link #computeNonce()}. */
  public volatile String nonce;

  /** Realm. */
  public volatile String realm;

  /** Required signature. Should be computed using {@link #computeSignature}. */
  public volatile String signature;

  /**
   * Required name of the signature method used by the client to sign the
   * request. Should be computed using {@link #computeSignature}.
   */
  public volatile String signatureMethod;

  /**
   * Required timestamp value. Should be computed using
   * {@link #computeTimestamp()}.
   */
  public volatile String timestamp;

  /**
   * Token value used to associate the request with the resource owner or
   * {@code null} if the request is not associated with a resource owner.
   */
  public volatile String token;

  /** The verification code received from the server. */
  public volatile String verifier;

  /**
   * Must either be "1.0" or {@code null} to skip. Provides the version of the
   * authentication process as defined in this specification.
   */
  public volatile String version;

  /**
   * Constructs the OAuth parameters using the typical parameters used to
   * authenticate requests to access protected resources using the stored token
   * credentials.
   * 
   * @param signer required OAuth signature algorithm
   * @param consumerKey required identifier portion of the client credentials
   *        (equivalent to a username)
   * @param token optional token value used to associate the request with the
   *        resource owner or {@code null} if the request is not associated with
   *        a resource owner
   * @param realm optional realm or {@code null} for none
   */
  public static OAuthAuthorizer createFromTokenCredentials(OAuthSigner signer,
      String consumerKey, String token, String realm) {
    OAuthAuthorizer result = new OAuthAuthorizer();
    result.signer = signer;
    result.consumerKey = consumerKey;
    result.token = token;
    result.realm = realm;
    return result;
  }

  /**
   * Computes a nonce, setting the value of the {@link #nonce} field.
   * <p>
   * The default implementation sets the nonce based on the hex string of a
   * random non-negative long. Subclasses may override.
   */
  public void computeNonce() {
    this.nonce = Long.toHexString(Math.abs(RANDOM.nextLong()));
  }

  /**
   * Computes a timestamp, setting the value of the {@link #timestamp} field.
   * <p>
   * The default implementation sets the timestamp based on the current system
   * time. Subclasses may override.
   */
  public void computeTimestamp() {
    this.timestamp = Long.toString(System.currentTimeMillis() / 1000);
  }

  /**
   * Computes a new signature based on the fields and the given request method
   * and URL, setting the values of the {@link #signature} and
   * {@link #signatureMethod} fields.
   * 
   * @throws GeneralSecurityException general security exception
   */
  public void computeSignature(String requestMethod, String requestUrl)
      throws GeneralSecurityException {
    OAuthSigner signer = this.signer;
    String signatureMethod = this.signatureMethod = signer.getSignatureMethod();
    // oauth_* parameters (except oauth_signature)
    TreeMap<String, String> parameters = new TreeMap<String, String>();
    putParameterIfValueNotNull(parameters, "oauth_callback", this.callback);
    putParameterIfValueNotNull(parameters, "oauth_consumer_key",
        this.consumerKey);
    putParameterIfValueNotNull(parameters, "oauth_nonce", this.nonce);
    putParameterIfValueNotNull(parameters, "oauth_signature_method",
        signatureMethod);
    putParameterIfValueNotNull(parameters, "oauth_timestamp", this.timestamp);
    putParameterIfValueNotNull(parameters, "oauth_token", this.token);
    putParameterIfValueNotNull(parameters, "oauth_verifier", this.verifier);
    putParameterIfValueNotNull(parameters, "oauth_version", this.version);
    // parse request URL for query parameters
    UriEntity requestUri = new UriEntity(requestUrl);
    ArrayMap<String, Object> queryParams = requestUri.getUnknownFields();
    int size = queryParams.size();
    for (int i = 0; i < size; i++) {
      putParameter(parameters, queryParams.getKey(i), (String) queryParams
          .getValue(i));
    }
    // normalize parameters
    StringBuilder parametersBuf = new StringBuilder();
    boolean first = true;
    for (Map.Entry<String, String> entry : parameters.entrySet()) {
      if (first) {
        first = false;
      } else {
        parametersBuf.append('&');
      }
      parametersBuf.append(entry.getKey());
      String value = entry.getValue();
      if (value != null) {
        parametersBuf.append('=').append(value);
      }
    }
    String normalizedParameters = parametersBuf.toString();
    // normalize path
    URI uri;
    try {
      uri = new URI(requestUrl);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
    String authority = uri.getAuthority().toLowerCase();
    String scheme = uri.getScheme().toLowerCase();
    // remove default port if specified
    int port = uri.getPort();
    if (scheme.equals("http") && port == 80 || scheme.equals("https")
        && port == 443) {
      int index = authority.lastIndexOf(":");
      if (index != -1) {
        authority = authority.substring(0, index);
      }
    }
    String normalizedPath =
        new StringBuilder().append(scheme).append("://").append(authority)
            .append(uri.getRawPath()).toString();
    // signature base string
    StringBuilder buf = new StringBuilder();
    buf.append(OAuth.escape(requestMethod)).append('&');
    buf.append(OAuth.escape(normalizedPath)).append('&');
    buf.append(OAuth.escape(normalizedParameters));
    String signatureBaseString = buf.toString();
    this.signature = signer.getSignature(signatureBaseString);
  }

  /**
   * Returns the {@code Authorization} header value to use with the OAuth
   * parameter values found in the fields. Subclasses may override by calling
   * {@code super.getAuthorizationHeader()}.
   */
  public String getAuthorizationHeader() {
    StringBuilder buf = new StringBuilder("OAuth");
    appendParameter(buf, "realm", this.realm);
    appendParameter(buf, "oauth_callback", this.callback);
    appendParameter(buf, "oauth_consumer_key", this.consumerKey);
    appendParameter(buf, "oauth_nonce", this.nonce);
    appendParameter(buf, "oauth_signature", this.signature);
    appendParameter(buf, "oauth_signature_method", this.signatureMethod);
    appendParameter(buf, "oauth_timestamp", this.timestamp);
    appendParameter(buf, "oauth_token", this.token);
    appendParameter(buf, "oauth_verifier", this.verifier);
    appendParameter(buf, "oauth_version", this.version);
    // hack: we have to remove the extra ',' at the end
    return buf.substring(0, buf.length() - 1);
  }

  private void appendParameter(StringBuilder buf, String name, String value) {
    if (value != null) {
      buf.append(' ').append(name).append("=\"").append(value).append("\",");
    }
  }

  private void putParameterIfValueNotNull(TreeMap<String, String> parameters,
      String key, String value) {
    if (value != null) {
      putParameter(parameters, key, value);
    }
  }

  private void putParameter(TreeMap<String, String> parameters, String key,
      String value) {
    parameters.put(OAuth.escape(key), value == null ? null : OAuth
        .escape(value));
  }

  public String getAuthorizationHeader(String requestMethod, String requestUrl)
      throws IOException {
    computeNonce();
    computeTimestamp();
    try {
      computeSignature(requestMethod, requestUrl);
    } catch (GeneralSecurityException e) {
      IOException io = new IOException();
      io.initCause(e);
      throw io;
    }
    return getAuthorizationHeader();
  }
}
