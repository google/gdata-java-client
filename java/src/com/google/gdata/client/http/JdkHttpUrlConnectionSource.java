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


package com.google.gdata.client.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Opens HTTP connection using {@link URL#openConnection()}.
 */
public class JdkHttpUrlConnectionSource implements HttpUrlConnectionSource { 

  /** Pre-built instance. */
  public static final JdkHttpUrlConnectionSource INSTANCE = 
      new JdkHttpUrlConnectionSource();

  public HttpURLConnection openConnection(URL url) throws IOException {
    if (!url.getProtocol().startsWith("http")) {
      throw new IllegalArgumentException("Not an HTTP url: " + url);
    }
    return (HttpURLConnection) url.openConnection();
  }
}
