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

/**
 * Interface for signing OAuth requests.
 *
 * 
 */
public interface OAuthSigner {

  /**
   * Signs the input string using the appropriate signature method.
   *
   * @param baseString the string to sign
   * @param oauthParameters the parameters related to the OAuth request or
   *        <code>null</code>
   * @return the signed string
   * @throws OAuthException if signing the request fails
   */
  String getSignature(String baseString, OAuthParameters oauthParameters)
      throws OAuthException;

  /**
   * Gets the signature method for this specific implementation.
   *
   * @return the signature method used to sign the base string
   */
  String getSignatureMethod();
}
