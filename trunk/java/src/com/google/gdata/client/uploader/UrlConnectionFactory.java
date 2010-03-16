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


package com.google.gdata.client.uploader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Factory for creating HTTP connections.
 *
 * 
 */
interface UrlConnectionFactory {

  /**
   * Default URL connection factory.
   */
  public static final UrlConnectionFactory DEFAULT =
      new UrlConnectionFactory() {
    public HttpURLConnection create(URL url) throws IOException {
      return (HttpURLConnection) url.openConnection();
    }
  };

  /**
   * Creates an HTTP connection to <code>url</code>.
   *
   * @param url denoting the location to connect to
   * @return an HTTP connection to <code>url</code>
   */
  public HttpURLConnection create(URL url) throws IOException;
}
