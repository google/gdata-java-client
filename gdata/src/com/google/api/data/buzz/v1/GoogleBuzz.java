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

package com.google.api.data.buzz.v1;

/**
 * Constants for the Google Buzz API.
 *
 * @since 2.3
 */
public final class GoogleBuzz {

  /** Root URL. */
  public static final String ROOT_URL = "https://www.googleapis.com/buzz/v1/";

  /** OAuth authorization service endpoint. */
  public static final String OAUTH_AUTHORIZATION_URL =
      "https://www.google.com/buzz/api/auth/OAuthAuthorizeToken";

  public static final String OAUTH_SCOPE =
      "https://www.googleapis.com/auth/buzz";

  public static final String OAUTH_SCOPE_READ_ONLY =
      "https://www.googleapis.com/auth/buzz.readonly";

  private GoogleBuzz() {
  }
}
