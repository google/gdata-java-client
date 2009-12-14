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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Container for OAuth-related parameters.  The parameters are divided into
 * two categories: those used to generate the signature base string, and other
 * parameters.  For each parameter, there are 4 different types of methods: a
 * getter, a setter, a check if the parameter exists (that returns a boolean)
 * and a check if the parameter exists (that throws an exception).  Refer to the
 * getters/setters for each parameter below to learn more about the parameter.
 *
 * 
 */
public class OAuthParameters {

  /**
   * Type of OAuth for this parameter set (i.e., two-legged or three-legged
   * OAuth (see {@link "https://sites.google.com/a/google.com/oauth/"}). 
   */
  public static enum OAuthType {
    TWO_LEGGED_OAUTH,
    THREE_LEGGED_OAUTH
  }

  // OAuth related parameters, as defined in http://oauth.net/core/1.0/#anchor3
  public static final String OAUTH_CALLBACK_KEY = "oauth_callback";
  public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
  public static final String OAUTH_CONSUMER_SECRET = "oauth_consumer_secret";
  public static final String OAUTH_NONCE_KEY = "oauth_nonce";
  public static final String OAUTH_KEY = "OAuth";
  public static final String OAUTH_SIGNATURE_KEY = "oauth_signature";
  public static final String OAUTH_SIGNATURE_METHOD_KEY =
    "oauth_signature_method";
  public static final String OAUTH_TIMESTAMP_KEY = "oauth_timestamp";
  public static final String OAUTH_TOKEN_KEY = "oauth_token";
  public static final String OAUTH_TOKEN_SECRET_KEY = "oauth_token_secret";
  public static final String OAUTH_VERIFIER_KEY = "oauth_verifier";
  public static final String REALM_KEY = "realm";
  public static final String XOAUTH_REQUESTOR_ID_KEY = "xoauth_requestor_id";

  protected Map<String, String> baseParameters;
  protected Map<String, String> extraParameters;
  private OAuthType oauthType = OAuthType.THREE_LEGGED_OAUTH;

  /**
   * Creates a new {@link OAuthParameters} object.  Initializes parameters
   * containers.
   */
  public OAuthParameters() {
    baseParameters = new HashMap<String, String>();
    extraParameters = new HashMap<String, String>();
  }

  /**
   * Return the parameters used to calculate the OAuth signature.
   *
   * @return a map of key/value pairs to use in the signature base string
   */
  public Map<String, String> getBaseParameters() {
    return Collections.<String, String>unmodifiableMap(baseParameters);
  }

  /**
   * Returns any other parameters used in the OAuth process, such as the
   * OAuth callback url.
   *
   * @return a map of key/value pairs for the extra parameters
   */
  public Map<String, String> getExtraParameters() {
    return  Collections.<String, String>unmodifiableMap(extraParameters);
  }

  /**
   * Adds a parameter to be used when generating the OAuth signature.
   *
   * @param key The key used to reference this parameter.  This key will also be
   *        used to reference the value in the request url and in the http
   *        authorization header.
   * @param value the value of the parameter
   */
  public void addCustomBaseParameter(String key, String value) {
    put(key, value, baseParameters);
  }

  /**
   * Removes a parameter from the OAuth signature.
   *
   * @param key The key used to reference this parameter.
   */
  public void removeCustomBaseParameter(String key) {
    remove(key, baseParameters);
  }

  /**
    * Sets a parameter related to OAuth (but not used when generating the
    * signature).
    *
    * @param key the key used to reference this parameter
    * @param value the value of the parameter
    */
  public void addExtraParameter(String key, String value) {
    put(key, value, extraParameters);
  }

  /**
   * Resets all transient parameters related to a single request, so that these
   * parameters do not interfere with multiple requests.
   */
  public void reset() {
    remove(OAUTH_NONCE_KEY, baseParameters);
    remove(OAUTH_TIMESTAMP_KEY, baseParameters);
    remove(OAUTH_VERIFIER_KEY, baseParameters);
    remove(OAUTH_SIGNATURE_KEY, extraParameters);
  }

  /**
   * Returns the OAuth Consumer Key.  The OAuth Consumer Key is a value used by
   * the Consumer to identify itself to the Service Provider.  This parameter is
   * required for all OAuth requests.  This parameter is included in the OAuth
   * signature base string.
   */
  public String getOAuthConsumerKey() {
    return get(OAUTH_CONSUMER_KEY, baseParameters);
  }

  /**
   * Sets the OAuth Consumer Key.  See {@link #getOAuthConsumerKey()} to learn
   * more about this parameter.
   */
  public void setOAuthConsumerKey(String consumerKey) {
    put(OAUTH_CONSUMER_KEY, consumerKey, baseParameters);
  }

  /**
   * Checks to see if the OAuth Consumer Key exists.  See
   * {@link #getOAuthConsumerKey()} to learn more about this parameter.
   *
   * @return true if the OAuth Consumer Key exists, false otherwise
   */
  public boolean checkOAuthConsumerKeyExists() {
    return checkExists(OAUTH_CONSUMER_KEY, baseParameters);
  }

  /**
   * Checks to see if the OAuth Consumer Key exists.  Throws an exception if
   * it does not.  See {@link #getOAuthConsumerKey()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth Consumer Key does not exist
   */
  public void assertOAuthConsumerKeyExists() throws OAuthException {
    assertExists(OAUTH_CONSUMER_KEY, baseParameters);
  }

  public String getOAuthConsumerSecret() {
    return get(OAUTH_CONSUMER_SECRET, extraParameters);
  }

  public void setOAuthConsumerSecret(String consumerSecret) {
    put(OAUTH_CONSUMER_SECRET, consumerSecret, extraParameters);
  }

  public boolean checkOAuthConsumerSecretExists() {
    return checkExists(OAUTH_CONSUMER_SECRET, extraParameters);
  }

  public void assertOAuthConsumerSecretExists() throws OAuthException {
    assertExists(OAUTH_CONSUMER_SECRET, extraParameters);
  }

  /**
   * Returns the OAuth nonce.  OAuth defines the nonce as "a random string,
   * uniquely generated for each request. The nonce allows the Service Provider
   * to verify that a request has never been made before and helps prevent
   * replay attacks when requests are made over a non-secure channel (such as
   * HTTP)."  This parameter is optional, and it will be supplied by
   * {@link OAuthUtil#getNonce()} if it is not provided.  This parameter is
   * included in the OAuth signature base string.
   */
  public String getOAuthNonce() {
    return get(OAUTH_NONCE_KEY, baseParameters);
  }

  /**
   * Sets the OAuth nonce.  See {@link #getOAuthNonce()} to learn more about
   * this parameter.
   */
  public void setOAuthNonce(String oauthNonce) {
    put(OAUTH_NONCE_KEY, oauthNonce, baseParameters);
  }

  /**
   * Checks to see if the OAuth nonce exists.  See {@link #getOAuthNonce()} to
   * learn more about this parameter.
   *
   * @return true if the OAuth nonce exists, false otherwise
   */
  public boolean checkOAuthNonceExists() {
    return checkExists(OAUTH_NONCE_KEY, baseParameters);
  }

  /**
   * Checks to see if the OAuth nonce exists.  Throws an exception if
   * it does not.  See {@link #getOAuthNonce()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth nonce does not exist
   */
  public void assertOAuthNonceExists() throws OAuthException {
    assertExists(OAUTH_NONCE_KEY, baseParameters);
  }

  /**
   * Returns the OAuth signature used to sign the current request.  This
   * parameter is optional, and it will be set by {@link OAuthHelper} if it is
   * not provided.
   */
  public String getOAuthSignature() {
    return get(OAUTH_SIGNATURE_KEY, extraParameters);
  }

  /**
   * Sets the OAuth signature used to sign the current request.  See
   * {@link #getOAuthSignature()} to learn more about this parameter.
   */
  public void setOAuthSignature(String signature) {
    put(OAUTH_SIGNATURE_KEY, signature, extraParameters);
  }

  /**
   * Checks to see if the OAuth signature exists.  See
   * {@link #getOAuthSignature()} to learn more about this parameter.
   *
   * @return true if the OAuth signature exists, false otherwise
   */
  public boolean checkOAuthSignatureExists() {
    return checkExists(OAUTH_SIGNATURE_KEY, extraParameters);
  }

  /**
   * Checks to see if the OAuth signature exists.  Throws an exception if
   * it does not.  See {@link #getOAuthSignature()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth signature does not exist
   */
  public void assertOAuthSignatureExists() throws OAuthException {
    assertExists(OAUTH_SIGNATURE_KEY, extraParameters);
  }

  /**
   * Returns the OAuth Signature Method.  Valid values are "RSA-SHA1",
   * "HMAC-SHA1" and "PLAINTEXT".  This parameter is optional, and will be
   * supplied by {@link OAuthSigner} if it is not provided by the user.  This
   * parameter is included in the OAuth signature base string.
   */
  public String getOAuthSignatureMethod() {
    return get(OAUTH_SIGNATURE_METHOD_KEY, baseParameters);
  }

  /**
   * Sets the OAuth Signature Method.  See {@link #getOAuthSignatureMethod()} to
   * learn more about this parameter.
   */
  public void setOAuthSignatureMethod(String signatureMethod) {
    put(OAUTH_SIGNATURE_METHOD_KEY, signatureMethod, baseParameters);
  }

  /**
   * Checks to see if the OAuth signature method exists.  See
   * {@link #getOAuthSignatureMethod()} to learn more about this parameter.
   *
   * @return true if the OAuth signature method exists, false otherwise
   */
  public boolean checkOAuthSignatureMethodExists() {
    return checkExists(OAUTH_SIGNATURE_METHOD_KEY, baseParameters);
  }

  /**
   * Checks to see if the OAuth signature method exists.  Throws an exception if
   * it does not.  See {@link #getOAuthSignatureMethod()} to learn more about
   * this parameter.
   *
   * @throws OAuthException if the OAuth signature method does not exist
   */
  public void assertOAuthSignatureMethodExists() throws OAuthException {
    assertExists(OAUTH_SIGNATURE_METHOD_KEY, baseParameters);
  }

  /**
   * Returns the OAuth timestamp.  OAuth defines the timestamp as "the number of
   * seconds since January 1, 1970 00:00:00 GMT. The timestamp value MUST be a
   * positive integer and MUST be equal or greater than the
   * timestamp used in previous requests."  This parameter is optional, and will
   * be supplied by {@link OAuthUtil#getTimestamp()} if it is not provided by
   * the user.  This parameter is included in the OAuth signature base string.
   */
  public String getOAuthTimestamp() {
    return get(OAUTH_TIMESTAMP_KEY, baseParameters);
  }

  /**
   * Sets the OAuth timestamp.  See {@link #getOAuthTimestamp()} to learn more
   * about this parameter.
   */
  public void setOAuthTimestamp(String timestamp) {
    put(OAUTH_TIMESTAMP_KEY, timestamp, baseParameters);
  }

  /**
   * Checks to see if the OAuth timestamp exists.  See
   * {@link #getOAuthTimestamp()} to learn more about this parameter.
   *
   * @return true if the OAuth timestamp exists, false otherwise
   */
  public boolean checkOAuthTimestampExists() {
    return checkExists(OAUTH_TIMESTAMP_KEY, baseParameters);
  }

  /**
   * Checks to see if the OAuth timestamp exists.  Throws an exception if
   * it does not.  See {@link #getOAuthTimestamp()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth timestamp does not exist
   */
  public void assertOAuthTimestampExists() throws OAuthException {
    assertExists(OAUTH_TIMESTAMP_KEY, baseParameters);
  }

  /**
   * Returns the OAuth token.  This token may either be the unauthorized
   * request token, the user-authorized request token, or the access token.
   * This parameter is optional, and will be modified by the methods in
   * {@link OAuthHelper}.  This parameter is included in the OAuth signature
   * base string.
   */
  public String getOAuthToken() {
    return get(OAUTH_TOKEN_KEY, baseParameters);
  }

  /**
   * Sets the OAuth token.  See {@link #getOAuthToken()} to learn more about
   * this parameter.
   */
  public void setOAuthToken(String token) {
    put(OAUTH_TOKEN_KEY, token, baseParameters);
  }

  /**
   * Checks to see if the OAuth token exists.  See {@link #getOAuthToken()} to
   * learn more about this parameter.
   *
   * @return true if the OAuth token exists, false otherwise
   */
  public boolean checkOAuthTokenExists() {
    return checkExists(OAUTH_TOKEN_KEY, baseParameters);
  }

  /**
   * Checks to see if the OAuth token exists.  Throws an exception if
   * it does not.  See {@link #getOAuthToken()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth token does not exist
   */
  public void assertOAuthTokenExists() throws OAuthException {
    assertExists(OAUTH_TOKEN_KEY, baseParameters);
  }

  /**
   * Returns the OAuth Token Secret.  The OAuth Token Secret is a secret used
   * by the Consumer to establish ownership of a given Token.  This parameter
   * is optional.
   */
  public String getOAuthTokenSecret() {
    return get(OAUTH_TOKEN_SECRET_KEY, extraParameters);
  }

  /**
   * Returns the OAuth Token Secret.  See {@link #getOAuthTokenSecret()} to
   * learn more about this parameter.
   */
  public void setOAuthTokenSecret(String tokenSecret) {
    put(OAUTH_TOKEN_SECRET_KEY, tokenSecret, extraParameters);
  }

  /**
   * Checks to see if the OAuth token secret exists.  See
   * {@link #getOAuthTokenSecret()} to learn more about this parameter.
   *
   * @return true if the OAuth token secret exists, false otherwise
   */
  public boolean checkOAuthTokenSecretExists() {
    return checkExists(OAUTH_TOKEN_SECRET_KEY, extraParameters);
  }

  /**
   * Checks to see if the OAuth token secret exists.  Throws an exception if
   * it does not.  See {@link #getOAuthTokenSecret()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth token secret does not exist
   */
  public void assertOAuthTokenSecretExists() throws OAuthException {
    assertExists(OAUTH_TOKEN_SECRET_KEY, extraParameters);
  }

  /**
   * Retrieves the OAuth type requested. 
   */
  public OAuthType getOAuthType() {
    return oauthType;
  }
  
  /**
   * Sets the value of the OAuth type. 
   */
  public void setOAuthType(OAuthType type) {
    this.oauthType = type;
  }

  /**
   * Returns the OAuth Verifier.
   */
  public String getOAuthVerifier() {
    return get(OAUTH_VERIFIER_KEY, baseParameters);
  }

  /**
   * Returns the OAuth Verifier.  See {@link #getOAuthVerifier()} to
   * learn more about this parameter.
   */
  public void setOAuthVerifier(String verifier) {
    put(OAUTH_VERIFIER_KEY, verifier, baseParameters);
  }

  /**
   * Checks to see if the OAuth Verifier exists.  See
   * {@link #getOAuthTokenSecret()} to learn more about this parameter.
   *
   * @return true if the OAuth Verifier exists, false otherwise
   */
  public boolean checkOAuthVerifierExists() {
    return checkExists(OAUTH_VERIFIER_KEY, baseParameters);
  }

  /**
   * Checks to see if the OAuth Verifier exists.  Throws an exception if
   * it does not.  See {@link #getOAuthVerifier()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth token secret does not exist
   */
  public void assertOAuthVerifierExists() throws OAuthException {
    assertExists(OAUTH_VERIFIER_KEY, baseParameters);
  }

  /**
   * Returns the OAuth callback url.  The OAuth callback url is a url the
   * Consumer provides to the Service Provider in the user authorization url.
   * Once the user has authorized, the Service Provider will redirect the user
   * back to the callback url with the user-authorized request token in the
   * response.  This parameter is optional.
   */
  public String getOAuthCallback() {
    return get(OAUTH_CALLBACK_KEY, extraParameters);
  }

  /**
   * Sets the OAuth callback url.  See {@link #getOAuthCallback()} to learn more
   * about this parameter.
   */
  public void setOAuthCallback(String oauthCallback) {
    put(OAUTH_CALLBACK_KEY, oauthCallback, extraParameters);
  }

  /**
   * Checks to see if the OAuth callback exists.  See
   * {@link #getOAuthCallback()} to learn more about this parameter.
   *
   * @return true if the OAuth callback exists, false otherwise
   */
  public boolean checkOAuthCallbackExists() {
    return checkExists(OAUTH_CALLBACK_KEY, extraParameters);
  }

  /**
   * Checks to see if the OAuth callback exists.  Throws an exception if
   * it does not.  See {@link #getOAuthCallback()} to learn more about this
   * parameter.
   *
   * @throws OAuthException if the OAuth callback does not exist
   */
  public void assertOAuthCallbackExists() throws OAuthException {
    assertExists(OAUTH_CALLBACK_KEY, extraParameters);
  }

  /**
   * Returns the Realm parameter to be used in the authorization header, as
   * defined by <a href="http://tools.ietf.org/html/rfc2617#section-1.2>Section
   * 1.2</a> of RFC 2617.  This parameter is optional.
   */
  public String getRealm() {
    return get(REALM_KEY, extraParameters);
  }

  /**
   * Sets the Realm parameter.  See {@link #getRealm()} to learn more about this
   * parameter.
   */
  public void setRealm(String realm) {
    put(REALM_KEY, realm, extraParameters);
  }

  /**
   * Checks to see if the realm exists.  See {@link #getRealm()} to learn more
   * about this parameter.
   *
   * @return true if the realm exists, false otherwise
   */
  public boolean checkRealmExists() {
    return checkExists(REALM_KEY, extraParameters);
  }

  /**
   * Checks to see if the realm exists.  Throws an exception if it does not.
   * See {@link #getRealm()} to learn more about this parameter.
   *
   * @throws OAuthException if the realm does not exist
   */
  public void assertRealmExists() throws OAuthException {
    assertExists(REALM_KEY, extraParameters);
  }

  /**
   * Retrieves the value with the given key from the input map.  A null value
   * is returned as an empty string.
   *
   * @param key the key whose value to retrieve
   * @param params the map from which to retrieve the value from
   * @return the value associated with the given key
   */
  protected String get(String key, Map<String, String> params) {
    String s = params.get(key);
    return s == null ? "" : s;
  }

  /**
   * Adds the key/value pair to the input map.
   *
   * @param key the key to add to the map
   * @param value the value to add to the map
   * @param params the map to add the values to
   */
  protected void put(String key, String value, Map<String, String> params) {
    params.put(key, value);
  }

  /**
   * Removes a key/value pair from the input map.
   *
   * @param key the key to remove
   * @param params the map to remove the key from
   */
  protected void remove(String key, Map<String, String> params) {
    params.remove(key);
  }

  /**
   * Checks the given key to see if it exists.  In order to "exist", the value
   * can't be null, and it can't be an empty string.
   *
   * @param key the key to check for existence
   * @param params the map to check for the key
   * @return true if the value is a string that is not empty, false otherwise
   */
  protected boolean checkExists(String key, Map<String, String> params) {
    return (get(key, params).length() > 0);
  }

  /**
   * Checks the given key to see if it exists, and throws an exception if it
   * does not.  See {@link #checkExists} for more information.
   *
   * @param key the key to check for existence
   * @param params the map to check for the key
   * @throws OAuthException if the value for the given key doesn't exist
   */
  protected void assertExists(String key, Map<String, String> params)
      throws OAuthException {
    if (!checkExists(key, params)) {
      throw new OAuthException(key + " does not exist.");
    }
  }
}
