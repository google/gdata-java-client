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

import com.google.gdata.util.common.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Signs strings using HMAC-SHA1.
 *
 * 
 */
public class OAuthHmacSha1Signer implements OAuthSigner {

  public String getSignature(String baseString, OAuthParameters oauthParameters)
      throws OAuthException {
    try {
      if (oauthParameters == null) {
        throw new OAuthException("OAuth parameters cannot be null");
      }
      String keyString = getKey(oauthParameters);
      SecretKey key = new SecretKeySpec(keyString.getBytes("UTF-8"), "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(key);
      return Base64.encode(mac.doFinal(baseString.getBytes("UTF-8")));
    } catch (UnsupportedEncodingException e) {
      throw new OAuthException(e);
    } catch (NoSuchAlgorithmException e) {
      throw new OAuthException(e);
    } catch (InvalidKeyException e) {
      throw new OAuthException(e);
    }
  }

  private String getKey(OAuthParameters oauthParameters) {
    return (new StringBuilder())
        .append(OAuthUtil.encode(oauthParameters.getOAuthConsumerSecret()))
        .append("&")
        .append(OAuthUtil.encode(oauthParameters.getOAuthTokenSecret()))
        .toString();
  }

  public String getSignatureMethod() {
    return "HMAC-SHA1";
  }

}
