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

package com.google.api.client.auth.googleapis.authsub;

import com.google.api.client.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;

public class AuthSub {

  /** Secure random number generator to sign requests. */
  private static final SecureRandom RANDOM = new SecureRandom();

  /**
   * Retrieves the private key from the specified keystore.
   * 
   * @param keyStream input stream to the keystore file
   * @param storePass password protecting the keystore file
   * @param alias alias under which the private key is stored
   * @param keyPass password protecting the private key
   * @return the private key from the specified keystore
   * @throws GeneralSecurityException if the keystore cannot be loaded
   * @throws IOException if the file cannot be accessed
   */
  public static PrivateKey getPrivateKeyFromKeystore(InputStream keyStream,
      String storePass, String alias, String keyPass) throws IOException,
      GeneralSecurityException {
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    try {
      keyStore.load(keyStream, storePass.toCharArray());
      return (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
    } finally {
      keyStream.close();
    }
  }

  /**
   * Returns {@code AuthSub} authorization header value based on the given
   * authentication token.
   */
  public static String getAuthorizationHeaderValue(String token) {
    return getAuthSubTokenPrefix(token).toString();
  }

  /**
   * Returns {@code AuthSub} authorization header value based on the given
   * authentication token, private key, request method, and request URL.
   * 
   * @throws GeneralSecurityException
   */
  public static String getAuthorizationHeaderValue(String token,
      PrivateKey privateKey, String requestMethod, String requestUrl)
      throws GeneralSecurityException {
    StringBuilder buf = getAuthSubTokenPrefix(token);
    if (privateKey != null) {
      String algorithm = privateKey.getAlgorithm();
      if (!"RSA".equals(algorithm)) {
        throw new IllegalArgumentException(
            "Only supported algorithm for private key is rsa: " + algorithm);
      }
      long timestamp = System.currentTimeMillis() / 1000;
      StringBuilder dataBuf =
          new StringBuilder().append(requestMethod).append(' ').append(
              requestUrl).append(' ').append(timestamp).append(' ');
      appendNonce(dataBuf);
      Signature signature = Signature.getInstance("SHA1withRSA");
      signature.initSign(privateKey);
      String data = dataBuf.toString();
      signature.update(data.getBytes());
      String encodedSig = Base64.encode(signature.sign());
      buf.append(" sigalg=\"rsa-sha1\" data=\"").append(data)
          .append("\" sig=\"").append(encodedSig).append('"');
    }
    return buf.toString();
  }

  private static StringBuilder getAuthSubTokenPrefix(String token) {
    return new StringBuilder("AuthSub token=\"").append(token).append('"');
  }

  private static void appendNonce(StringBuilder buf) {
    long value = RANDOM.nextLong();
    if (value >= 0) {
      buf.append(value);
    } else {
      // Split into two unsigned halves. As digits are printed out from
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
        cbuf[--dst] = Character.forDigit((int) (bot % radix), radix);
        bot = (bot / radix) + ((top % radix) << 32);
        top /= radix;
      }
      buf.append(cbuf, dst, max_dig - dst);
    }
  }

  private AuthSub() {
  }
}
