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


package com.google.gdata.client.http;

import com.google.gdata.util.common.base.CharEscapers;
import com.google.gdata.util.common.base.Charsets;
import com.google.gdata.util.common.base.StringUtil;
import com.google.gdata.util.common.io.CharStreams;
import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.AuthenticationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Map;


/**
 * Utility functions to support AuthSub (Account Authentication for Web
 * Applications).
 *
 * 
 */
public class AuthSubUtil {

  // The default protocol to use for AuthSub communication
  private static final String DEFAULT_PROTOCOL = "https";

  // The default domain to use for AuthSub communication
  private static final String DEFAULT_DOMAIN = "www.google.com";

  // Secure random number generator to sign requests
  private static final SecureRandom RANDOM = new SecureRandom();


  /**
   * Signature algorithms supported by AuthSub to sign the requests.
   */
  private enum SignatureAlgorithm {
    DSA_SHA1("dsa-sha1", "SHA1withDSA"),
    RSA_SHA1("rsa-sha1", "SHA1withRSA");

    SignatureAlgorithm(String authSubName, String jcaName) {
      this.authSubName = authSubName;
      this.jcaName = jcaName;
    }

    @Override
    public String toString() { return jcaName; }
    public String getAuthSubName() { return authSubName; }
    public String getJCAName() { return jcaName; }

    private final String authSubName;
    private final String jcaName;
  }


  /**
   * Creates the request URL to be used to retrieve an AuthSub token.
   * <p>
   * On success, the user will be redirected to the next URL with the
   * AuthSub token appended to the URL.  Use {@link #getTokenFromReply(String)}
   * to retrieve the token from the reply.
   *
   * @param nextUrl the URL to redirect to on successful token retrieval
   * @param scope the scope of the requested AuthSub token
   * @param secure <code>true</code> if the token will be used securely
   * @param session <code>true</code> if the token will be exchanged for a
   *                session cookie
   * @return the URL to be used to retrieve the AuthSub token
   */
  public static String getRequestUrl(String nextUrl,
                                     String scope,
                                     boolean secure,
                                     boolean session) {

    return getRequestUrl(DEFAULT_PROTOCOL, DEFAULT_DOMAIN, nextUrl, scope,
                         secure, session);
  }


  /**
   * Creates the request URL to be used to retrieve an AuthSub token.
   * <p>
   * On success, the user will be redirected to the next URL with the
   * AuthSub token appended to the URL.  Use {@link #getTokenFromReply(String)}
   * to retrieve the token from the reply.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @param nextUrl the URL to redirect to on successful token retrieval
   * @param scope the scope of the requested AuthSub token
   * @param secure <code>true</code> if the token will be used securely
   * @param session <code>true</code> if the token will be exchanged for a
   *                session cookie
   * @return the URL to be used to retrieve the AuthSub token
   */
  public static String getRequestUrl(String protocol,
                                     String domain,
                                     String nextUrl,
                                     String scope,
                                     boolean secure,
                                     boolean session) {

    StringBuffer url = new StringBuffer(protocol).append("://");
    url.append(domain).append("/accounts/AuthSubRequest");
    addParameter(url, "next", nextUrl);
    addParameter(url, "scope", scope);
    addParameter(url, "secure", secure ? "1" : "0");
    addParameter(url, "session", session ? "1" : "0");
    return url.toString();
  }

  /**
   * Creates the request URL to be used to retrieve an AuthSub token for
   * hosted applications.
   * <p>
   * On success, the user will be redirected to the next URL with the
   * AuthSub token appended to the URL.  Use {@link #getTokenFromReply(String)}
   * to retrieve the token from the reply.
   *
   * @param hostedDomain hosted domain name, for example 
   *                     <code>mydomain.example.com</code>
   * @param nextUrl the URL to redirect to on successful token retrieval
   * @param scope the scope of the requested AuthSub token
   * @param secure <code>true</code> if the token will be used securely
   * @param session <code>true</code> if the token will be exchanged for a
   *                session cookie
   * @return the URL to be used to retrieve the AuthSub token
   */
  public static String getRequestUrl(String hostedDomain,
                                     String nextUrl,
                                     String scope,
                                     boolean secure,
                                     boolean session) {
    return getRequestUrl(DEFAULT_PROTOCOL, DEFAULT_DOMAIN, hostedDomain,
                         nextUrl, scope, secure, session);
  }

  /**
   * Creates the request URL for to be used to retrieve an AuthSub token for
   * hosted applications.
   * <p>
   * On success, the user will be redirected to the next URL with the
   * AuthSub token appended to the URL.  Use {@link #getTokenFromReply(String)}
   * to retrieve the token from the reply.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @param hostedDomain hosted domain name, for example
   *                     <code>mydomain.example.com</code>
   * @param nextUrl the URL to redirect to on successful token retrieval
   * @param scope the scope of the requested AuthSub token
   * @param secure <code>true</code> if the token will be used securely
   * @param session <code>true</code> if the token will be exchanged for a
   *                session cookie
   * @return the URL to be used to retrieve the AuthSub token
   */
  public static String getRequestUrl(String protocol,
                                     String domain,
                                     String hostedDomain,
                                     String nextUrl,
                                     String scope,
                                     boolean secure,
                                     boolean session) {
    StringBuffer url = new StringBuffer(
        getRequestUrl(protocol, domain, nextUrl, scope, secure, session));
    addParameter(url, "hd", hostedDomain);
    return url.toString();
  }

  /**
   * Parses and returns the AuthSub token returned by Google on a successful
   * AuthSub login request.  The token will be appended as a query parameter
   * to the next URL specified while making the AuthSub request.
   *
   * @param url the redirected-to next URL with the token
   * @return the AuthSub token returned by Google
   */
  public static String getTokenFromReply(URL url) {

    return getTokenFromReply(url.getQuery());
  }


  /**
   * Parses and returns the AuthSub token returned by Google on a successful
   * AuthSub login request.  The token will be appended as a query parameter
   * to the "next" URL specified while making the AuthSub request.
   *
   * @param queryString the query portion of the redirected-to URL containing
   *                    the token (as the server received it; i.e. what
   *                    {@code httpServletRequest.getQueryString()} returns)
   * @return the AuthSub token returned by Google; {@code null} if there is no
   *         token present in {@code queryString}. The token text will have
   *         been run through {@link URLDecoder} already, and will not need any
   *         additional decoding before use; however, the token string will
   *         not contain percent ({@code %}) characters and therefore
   *         additional url-decoding will do no harm.
   */
  public static String getTokenFromReply(String queryString) {

    // Parse the query parameters
    Map<String, String> params =
      StringUtil.string2Map(queryString, "&", "=", true /*stripEntry*/);
    params = StringUtil.lowercaseKeys(params);
    String encoded = params.get("token");
    try {
      return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e); // Can't happen - UTF-8 in all jvms
    }
  }


  /**
   * Retrieves the private key from the specified keystore.
   *
   * @param keystore the path to the keystore file
   * @param keystorePass the password that protects the keystore file
   * @param keyAlias the alias under which the private key is stored
   * @param keyPass the password protecting the private key
   * @return the private key from the specified keystore
   * @throws GeneralSecurityException if the keystore cannot be loaded
   * @throws IOException if the file cannot be accessed
   */
  public static PrivateKey getPrivateKeyFromKeystore(String keystore,
                                                     String keystorePass,
                                                     String keyAlias,
                                                     String keyPass)
      throws IOException, GeneralSecurityException {

    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    FileInputStream keyStream = null;
    try {
      keyStream = new FileInputStream(keystore);
      keyStore.load(keyStream, keystorePass.toCharArray());
      return (PrivateKey) keyStore.getKey(keyAlias, keyPass.toCharArray());
    } finally {
      if (keyStream != null) {
        keyStream.close();
      }
    }
  }


  /**
   * Exchanges the one time use token returned in the URL for a session
   * token.
   * <p>
   * If the <code>key</code> is non-null, the token will be used securely
   * and the request to make the exchange will be signed.
   *
   * @param onetimeUseToken the one time use token sent by Google in the URL
   * @param key the private key to sign the request
   * @return the session token.  <code>null</code> if the request failed
   * @throws IOException if error in writing/reading the request
   * @throws GeneralSecurityException if error in signing the request
   * @throws AuthenticationException if one time use token is rejected
   */
  public static String exchangeForSessionToken(String onetimeUseToken,
                                               PrivateKey key)
      throws IOException, GeneralSecurityException, AuthenticationException {

    return exchangeForSessionToken(DEFAULT_PROTOCOL, DEFAULT_DOMAIN,
                                   onetimeUseToken, key);
  }

  /**
   * Exchanges the one time use token returned in the URL for a session
   * token.
   * <p>
   * If the <code>key</code> is non-null, the token will be used securely
   * and the request to make the exchange will be signed.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @param onetimeUseToken the one time use token sent by Google in the URL
   * @param key the private key to sign the request
   * @return the session token.  <code>null</code> if the request failed
   * @throws IOException if error in writing/reading the request
   * @throws GeneralSecurityException if error in signing the request
   * @throws AuthenticationException if one time use token is rejected
   */
  public static String exchangeForSessionToken(String protocol,
                                               String domain,
                                               String onetimeUseToken,
                                               PrivateKey key)
      throws IOException, GeneralSecurityException, AuthenticationException {

    String sessionUrl = getSessionTokenUrl(protocol, domain);
    URL url = new URL(sessionUrl);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

    String header = formAuthorizationHeader(onetimeUseToken, key, url, "GET");
    httpConn.setRequestProperty("Authorization", header);

    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new AuthenticationException(httpConn.getResponseCode() + ": "
                                        + httpConn.getResponseMessage());
    }

    // Parse the response
    String body =
        CharStreams.toString(
            new InputStreamReader(
                httpConn.getInputStream(), Charsets.ISO_8859_1));
    Map<String, String> parsedTokens =
      StringUtil.string2Map(body, "\n", "=", true /*stripEntry*/);
    parsedTokens = StringUtil.lowercaseKeys(parsedTokens);
    return parsedTokens.get("token");
  }


  /**
   * Retrieves information about the AuthSub token.
   * <p>
   * If the <code>key</code> is non-null, the token will be used securely
   * and the request to revoke the token will be signed.
   *
   * @param token the AuthSub token for which to receive information
   * @param key the private key to sign the request
   * @throws IOException if error in writing/reading the request
   * @throws GeneralSecurityException if error in signing the request
   * @throws AuthenticationException if the token is rejected
   * @return the token information in the form of a Map from the name of the
   *          attribute to the value of the attribute.
   */
  public static Map<String, String> getTokenInfo(String token,
                                                 PrivateKey key)
      throws IOException, GeneralSecurityException, AuthenticationException {

    return getTokenInfo(DEFAULT_PROTOCOL, DEFAULT_DOMAIN, token, key);
  }


  /**
   * Retrieves information about the AuthSub token.
   * <p>
   * If the <code>key</code> is non-null, the token will be used securely
   * and the request to revoke the token will be signed.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @param token the AuthSub token for which to receive information
   * @param key the private key to sign the request
   * @throws IOException if error in writing/reading the request
   * @throws GeneralSecurityException if error in signing the request
   * @throws AuthenticationException if the token is rejected
   * @return the token information in the form of a Map from the name of the
   *          attribute to the value of the attribute.
   */
  public static Map<String, String> getTokenInfo(String protocol,
                                                 String domain,
                                                 String token,
                                                 PrivateKey key)
      throws IOException, GeneralSecurityException, AuthenticationException {

    String tokenInfoUrl = getTokenInfoUrl(protocol, domain);
    URL url = new URL(tokenInfoUrl);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

    String header = formAuthorizationHeader(token, key, url, "GET");
    httpConn.setRequestProperty("Authorization", header);

    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new AuthenticationException(httpConn.getResponseCode() + ": "
                                        + httpConn.getResponseMessage());
    }

    String body =
      CharStreams.toString(
          new InputStreamReader(
              httpConn.getInputStream(), Charsets.ISO_8859_1));
    return StringUtil.string2Map(body.trim(), "\n", "=", true);
  }

  
  /**
   * Revokes the specified token.
   * <p>
   * If the <code>key</code> is non-null, the token will be used securely
   * and the request to revoke the token will be signed.
   *
   * @param token the AuthSub token to revoke
   * @param key the private key to sign the request
   * @throws IOException if error in writing/reading the request
   * @throws GeneralSecurityException if error in signing the request
   * @throws AuthenticationException if the token is rejected
   */
  public static void revokeToken(String token,
                                 PrivateKey key)
      throws IOException, GeneralSecurityException, AuthenticationException {

    revokeToken(DEFAULT_PROTOCOL, DEFAULT_DOMAIN, token, key);
  }

  
  /**
   * Revokes the specified token.
   * <p>
   * If the <code>key</code> is non-null, the token will be used securely
   * and the request to revoke the token will be signed.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @param token the AuthSub token to revoke
   * @param key the private key to sign the request
   * @throws IOException if error in writing/reading the request
   * @throws GeneralSecurityException if error in signing the request
   * @throws AuthenticationException if the token is rejected
   */
  public static void revokeToken(String protocol,
                                 String domain,
                                 String token,
                                 PrivateKey key)
      throws IOException, GeneralSecurityException, AuthenticationException {

    String revokeUrl = getRevokeTokenUrl(protocol, domain);
    URL url = new URL(revokeUrl);
    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

    String header = formAuthorizationHeader(token, key, url, "GET");
    httpConn.setRequestProperty("Authorization", header);

    if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new AuthenticationException(httpConn.getResponseCode() + ": "
                                        + httpConn.getResponseMessage());
    }
  }


  /**
   * Forms the AuthSub authorization header.
   * <p>
   * If the <code>key</code> is null, the token will be used in insecure mode.
   * If the <code>key</code> is non-null, the token will be used securely and
   * the header will contain a signature.
   *
   * @param token the AuthSub token to use in the header
   * @param key the private key used to sign the request
   * @param requestUrl the URL of the request being issued
   * @param requestMethod the HTTP method being used to issue the request
   * @return the authorization header
   * @throws GeneralSecurityException if error occurs while creating signature
   */
  public static String formAuthorizationHeader(String token,
                                               PrivateKey key,
                                               URL requestUrl,
                                               String requestMethod)
      throws GeneralSecurityException {

    if (key == null) {
      return String.format("AuthSub token=\"%s\"", token);
    } else {
      // Form signature for secure mode
      long timestamp = System.currentTimeMillis() / 1000;
      long nonce = RANDOM.nextLong();
      String dataToSign =
        String.format("%s %s %d %s", requestMethod, requestUrl.toExternalForm(),
                      timestamp, unsignedLongToString(nonce));
      SignatureAlgorithm sigAlg = getSigAlg(key);
      byte[] signature = sign(key, dataToSign, sigAlg);
      String encodedSignature = Base64.encode(signature);
      return String.format("AuthSub token=\"%s\" data=\"%s\" sig=\"%s\" " +
                           "sigalg=\"%s\"", token, dataToSign, encodedSignature,
                           sigAlg.getAuthSubName());
    }
  }


  /**
   * Adds the query parameter with the given name and value to the URL.
   */
  private static void addParameter(StringBuffer url,
                                   String name,
                                   String value) {

    name = CharEscapers.uriEscaper().escape(name);
    value = CharEscapers.uriEscaper().escape(value);

    // Make sure the url currently ends with the correct delimiter
    if (url.indexOf("?") == -1) {
      url.append('?');
    } else {
      switch (url.charAt(url.length() - 1)) {
        case '?':
        case '&':
          break;
        default:
          url.append('&');
      }
    }

    // Append the name/value pair
    url.append(name).append('=').append(value);
  }


  /**
   * Signs the data with the given key and the provided algorithm.
   */
  private static byte[] sign(PrivateKey key,
                             String data,
                             SignatureAlgorithm algorithm)
      throws GeneralSecurityException {

    Signature signature = Signature.getInstance(algorithm.getJCAName());
    signature.initSign(key);
    signature.update(data.getBytes());
    return signature.sign();
  }


  /**
   * Returns the signature algorithm to be used for the provided private key.
   */
  private static SignatureAlgorithm getSigAlg(PrivateKey key) {
    String algorithm = key.getAlgorithm();
    if ("dsa".equalsIgnoreCase(algorithm)) {
      return SignatureAlgorithm.DSA_SHA1;
    } else if ("rsa".equalsIgnoreCase(algorithm)) {
      return SignatureAlgorithm.RSA_SHA1;
    } else {
      throw new IllegalArgumentException("Unknown algorithm in private key.");
    }
  }


  /**
   * Returns the URL to use to exchange the one-time-use token for
   * a session token.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @return the URL to exchange for the session token
   */
  private static String getSessionTokenUrl(String protocol,
                                           String domain) {
    return protocol + "://" + domain + "/accounts/AuthSubSessionToken";
  }


  /**
   * Returns the URL that handles token revocation.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @return the URL that handles token revocation.
   */
  private static String getRevokeTokenUrl(String protocol,
                                          String domain) {
    return protocol + "://" + domain + "/accounts/AuthSubRevokeToken";
  }


  /**
   * Returns the URL that handles token revocation.
   *
   * @param protocol the protocol to use to communicate with the server
   * @param domain the domain at which the authentication server exists
   * @return the URL that handles token revocation.
   */
  private static String getTokenInfoUrl(String protocol,
                                        String domain) {
    return protocol + "://" + domain + "/accounts/AuthSubTokenInfo";
  }


  /**
   *  Treats the provided long as unsigned and converts it to a string.
   */
  private static String unsignedLongToString(long value) {
    if (value >= 0) {
      return Long.toString(value);
    } else {
      // Split into two unsigned halves.  As digits are printed out from
      // the bottom half, move data from the top half into the bottom
      // half
      int max_dig = 20;
      char[] cbuf = new char[max_dig];
      int radix = 10;
      int dst = max_dig;
      long top = value >>> 32;
      long bot = value & 0xffffffffl;
      bot += (top % radix) << 32;
      top /= radix;
      while (bot > 0 || top > 0) {
        cbuf[--dst] = Character.forDigit((int)(bot % radix), radix);
        bot = (bot / radix) + ((top % radix) << 32);
        top /= radix;
      }
      return new String(cbuf, dst, max_dig-dst);
    }
  }
}
