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


package com.google.gdata.client;

/**
 * The valid values for the "accountType" parameter in ClientLogin.
 *
 * 
 *
 */
public enum ClientLoginAccountType {
  // Authenticate as a Google account only.
  GOOGLE("GOOGLE"),
  // Authenticate as a hosted account only.
  HOSTED("HOSTED"),
  // Authenticate first as a hosted account; if attempt fails, authenticate as a
  // Google account.  Use HOSTED_OR_GOOGLE if you're not sure which type of
  // account needs authentication. If the user information matches both a hosted
  // and a Google account, only the hosted account is authenticated.
  HOSTED_OR_GOOGLE("HOSTED_OR_GOOGLE");

  private final String accountTypeValue;

  ClientLoginAccountType(String accountTypeValue) {
    this.accountTypeValue = accountTypeValue;
  }

  /** Returns the value of the accountType. */
  public String getValue() {
    return accountTypeValue;
  }
}
