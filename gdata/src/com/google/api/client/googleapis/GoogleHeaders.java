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

package com.google.api.client.googleapis;

import com.google.api.client.escape.PercentEscaper;
import com.google.api.client.http.HttpHeaders;

/**
 * HTTP headers for Google API's.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class GoogleHeaders {

  private static final PercentEscaper SLUG_ESCAPER =
      new PercentEscaper(" !\"#$&'()*+,-./:;<=>?@[\\]^_`{|}~", false);

  /**
   * Sets the {@code Slug} header for given file name into the given HTTP
   * headers, properly escaping the header value.
   */
  public static void setSlug(HttpHeaders headers, String fileName) {
    headers.set("Slug", SLUG_ESCAPER.escape(fileName));
  }
}
