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

package com.google.api.client.auth.oauth;

import com.google.api.client.escape.PercentEscaper;

/** Utility methods related to OAuth. */
public class OAuth {

  private static final PercentEscaper ESCAPER =
      new PercentEscaper("-_.~", false);

  /** Returns the escaped form of the given value using OAuth escaping rules. */
  public static String escape(String value) {
    return ESCAPER.escape(value);
  }

  private OAuth() {
  }
}
