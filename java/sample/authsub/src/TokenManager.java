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

import java.util.Hashtable;

/**
 * Manages and stores AuthSub tokens.
 *
 * The TokenManager currently just uses a Hashtable to store the tokens
 * in-memory.  This is just for the purposes of the sample. Ideally, the
 * token should be stored in a database with the same security restrictions
 * as other sensitive material like user passwords.  Google limits the
 * number of AuthSub tokens generated per user per target and thus the tokens
 * have to be stored permanently and reused.
 *
 * 
 */
public class TokenManager {

  private static Hashtable<String, String> tokenMap;

  static {
    tokenMap = new Hashtable<String, String>();
  }

  public static synchronized void storeToken(String principal, String token) {
    tokenMap.put(principal, token);
  }

  public static synchronized String retrieveToken(String principal) {
    return tokenMap.get(principal);
  }

  public static synchronized void removeToken(String principal) {
    tokenMap.remove(principal);
  }
}
