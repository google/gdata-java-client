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

import com.google.gdata.client.authn.oauth.OAuthHelper.HeaderKeyValuePair;
import com.google.gdata.client.authn.oauth.OAuthHelper.KeyValuePair;

import java.util.Map;

/**
 * Provides common OAuth message signing and Authorization header
 * functionality.
 *
 * 
 * 
 */
public class TwoLeggedOAuthHelper {

  private final OAuthSigner signer;
  private final OAuthParameters parameters;
  
  public TwoLeggedOAuthHelper(OAuthSigner signer, OAuthParameters parameters) {
    this.signer = signer;
    this.parameters = parameters;
  }

  void validateInputParameters() throws OAuthException {
    parameters.assertOAuthConsumerKeyExists();
    if (signer instanceof OAuthHmacSha1Signer) {
      parameters.assertOAuthConsumerSecretExists();
    }
  }

  /**
   * Generate and add request-specific parameters that are common to all OAuth
   * requests (if the user did not already specify them in the oauthParameters
   * object). The following parameters are added to the oauthParameter set:
   * <ul>
   * <li>oauth_signature
   * <li>oauth_signature_method
   * <li>oauth_timestamp
   * <li>oauth_nonce
   *
   * @param baseUrl the url to make the request to
   * @param httpMethod the http method of this request (for example, "GET")
   * @throws OAuthException if there is an error with the OAuth request
   */
  void addCommonRequestParameters(String baseUrl, String httpMethod)
      throws OAuthException {
    // add the signature method if it doesn't already exist.
    if (parameters.getOAuthSignatureMethod().length() == 0) {
      parameters.setOAuthSignatureMethod(signer.getSignatureMethod());
    }
    // add the nonce if it doesn't already exist.
    if (parameters.getOAuthTimestamp().length() == 0) {
      parameters.setOAuthTimestamp(OAuthUtil.getTimestamp());
    }
    // add the timestamp if it doesn't already exist.
    if (parameters.getOAuthNonce().length() == 0) {
      parameters.setOAuthNonce(OAuthUtil.getNonce());
    }
    // add the signature if it doesn't already exist.
    // The signature is calculated by the {@link OAuthSigner}.
    if (parameters.getOAuthSignature().length() == 0) {
      String baseString = OAuthUtil.getSignatureBaseString(baseUrl, httpMethod,
          parameters.getBaseParameters());
      parameters.setOAuthSignature(signer.getSignature(baseString, parameters));
    }
  }
  
  /**
   * Generates the string to be used as the HTTP authorization header.  A
   * typical authorization header will look something like this:
   * <p>
   * OAuth realm="", oauth_signature="SOME_LONG_STRING", oauth_nonce="123456",
   * oauth_signature_method="RSA-SHA1", oauth_consumer_key="www.example.com",
   * oauth_token="abc123", oauth_timestamp="123456"
   * <p>
   * The following parameters are required in {@link OAuthParameters}:
   * <ul>
   * <li>oauth_consumer_key
   * <li>oauth_token
   * <li>oauth_token_secret (if signing with HMAC)
   * </ul>
   * @see <a href="http://oauth.net/core/1.0/#auth_header_authorization">OAuth
   *      Authorization Header</a>
   *
   * @param requestUrl the url of the request
   * @param httpMethod the http method of the request (for example GET)
   * @return the full authorization header
   * @throws OAuthException if there is an error with the OAuth request
   */
  public String getAuthorizationHeader(String requestUrl, String httpMethod)
      throws OAuthException {

    validateInputParameters();
    // add request-specific parameters
    return addParametersAndRetrieveHeader(requestUrl, httpMethod);
  }

  /**
   * Adds standard parameters, produces a signature and retrieves the
   * relevant "Authorization" header. 
   */
  String addParametersAndRetrieveHeader(String requestUrl,
      String httpMethod) throws OAuthException {
    addCommonRequestParameters(requestUrl, httpMethod);

    // Add the "realm" item to the header
    KeyValuePair headerParams = new HeaderKeyValuePair();
    headerParams.add(OAuthParameters.REALM_KEY, parameters.getRealm());

    // Add the signature to the header
    headerParams.add(OAuthParameters.OAUTH_SIGNATURE_KEY,
        parameters.getOAuthSignature());

    // Add all other base parameters to the header
    for (Map.Entry<String, String> e :
        parameters.getBaseParameters().entrySet()) {
      if (e.getValue().length() > 0) {
        headerParams.add(e.getKey(), e.getValue());
      }
    }

    // clear the request-specific parameters set in
    // addCommonRequestParameters(), such as nonce, timestamp and signature,
    // which are only needed for a single request.
    parameters.reset();

    return (new StringBuilder()).append(OAuthParameters.OAUTH_KEY).append(" ")
        .append(headerParams.toString()).toString();
  }
}
