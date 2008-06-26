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


package sample.authsub.src;

import com.google.gdata.util.common.util.Base64;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Generates and verifies tokens to protect URLs.
 *
 * The URL command attack is a cross site scripting vulnerability.  It
 * is applicable when cookies are used for authentication and user commands
 * are done through HTTP requests (GET or POST).
 * This attack is illustrated through the following
 * example. Assume goodsite.com and evilsite.com. Assume goodsite.com offers a
 * URL of the following form
 * http://goodsite.com/addtophonebook?email=hello@gmail.com that adds
 * hello@gmail.com to the current logged in user's phonebook.
 * An attack may look like this:
 * <p>
 * 1. User logs into goodsite.com and gets an login cookie
 * 2. User goes to evilsite.com
 * 3. On evilsite.com there is an HTML element with a URL source to some
 *    command for goodsite.com (Perhaps through an invisible image).
 * 4. The browser will fetch the URL and the login cookie will go along with
 *    it
 * 5. The user's account is now modified by an evil site
 * <p>
 * This can happen when URL's aren't protected.
 * eg. http://goodsite.com/add=temp@gmail.com
 * <p>
 * Thus to protect the GData URL proxy, we require requests to the GData proxy
 * servlet to contain a secure token that is a hash of the user's login cookie
 * issued by the web service and the URL to be accessed.  Since evilsite.com
 * does not have access to the login cookie, it will not be able to generate a
 * valid secure token for this user.
 * <p>
 * The token used to secure the URL has the following format:
 * token = HMAC<sub>SHA1(login-cookie)</sub>({data})
 * <p>
 * where <code>{data}</code> is a string formed in the following manner:
 *    data = http-url SP http-method SP timestamp
 *           <code>SP</code> is a single ASCII space character.
 *           <code>http-url</code> is the GData feed URl being requested.
 *           <code>timestamp</code> is an integer representing the time
 *              the request was sent, in seconds since Jan 1, 1970 UTC,
 *              formatted as an ASCII string (in decimal).
 * <p>
 * The token used in the URL will be Base64 encoded.
 *
 * 
 */
public class SecureUrl {

  private static final int TOKEN_LIFE_SECONDS = 30 * 60;

  /**
   * Generates a secure token to be used to protect a URL.
   *
   * @param cookie the authentication cookie of the user
   * @param url the URL to protect
   * @param method the HTTP method used against the URL
   * @param currentTimeSecs the current time in seconds
   * @return the secure token
   */
  public static String generateToken(String cookie,
                                     String url,
                                     String method,
                                     long currentTimeSecs) {
    // Form the data to be HMAC'ed
    String data = url + " " + method + " " + currentTimeSecs;

    // Compute SHA1-HMAC
    byte[] hmac;
    try {
      hmac = computeSHA1HMac(data, cookie);
    } catch (GeneralSecurityException e) {
      throw new RuntimeException("Security exception - " + e.getMessage());
    }
    return Base64.encodeWebSafe(hmac, true);
  }

  /**
   * Checks the validity of the secure token to ensure that it was not created
   * by a malicious third party.
   *
   * @param token the token to verify
   * @param cookie the cookie of the user
   * @param url the URL requested
   * @param method the HTTP method used to request the URL
   * @param timestamp the timestamp of the token
   */
  public static boolean isTokenValid(String token,
                                     String cookie,
                                     String url,
                                     String method,
                                     String timestamp) {
    long createTime = Long.parseLong(timestamp);
    long currentTime = (new Date()).getTime() / 1000;
    if ((currentTime - createTime) > TOKEN_LIFE_SECONDS) {
      return false;
    }

    String data = url + " " + method + " " + createTime;
    byte[] hmac;
    try{
      hmac = computeSHA1HMac(data, cookie);
    } catch (GeneralSecurityException e) {
      throw new RuntimeException("Security exception - " + e.getMessage());
    }
    String hmacEnc = Base64.encodeWebSafe(hmac, true);
    return hmacEnc.equals(token);
  }

  /**
   * Computes a SHA1-HMAC. A SHA1 hash of the cookie is used as the key to
   * MAC the data.
   *
   * @param data the data to MAC
   * @param cookie the authentication cookie of the user
   * @return the SHA1-HMAC of the data given the cookie
   */
  public static byte[] computeSHA1HMac(String data,
                                       String cookie)
      throws GeneralSecurityException {

    // Compute SHA-1 hash of the cookie
    byte[] hash;
    MessageDigest digest = MessageDigest.getInstance("SHA-1");
    hash = digest.digest(cookie.getBytes());

    // Compute the HMAC of the data using hash(cookie) as the key
    byte[] hmacResult;
    Mac hm;
    hm = Mac.getInstance("HMacSHA1");
    Key k1 = new SecretKeySpec(hash, 0, hash.length, "HMacSHA1");
    hm.init(k1);
    return hm.doFinal(data.getBytes());
  }
}
