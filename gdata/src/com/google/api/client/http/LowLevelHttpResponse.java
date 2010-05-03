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

package com.google.api.client.http;

import java.io.IOException;
import java.io.InputStream;

public abstract class LowLevelHttpResponse {

  public abstract InputStream getContent() throws IOException;

  public abstract String getContentEncoding();

  public abstract long getContentLength();

  public abstract String getContentType();

  /** Response status line or {@code null} for none. */
  public abstract String getStatusLine();

  public abstract int getStatusCode();

  public abstract String getReasonPhrase();

  public abstract int getHeaderCount();

  public abstract String getHeaderName(int index);

  public abstract String getHeaderValue(int index);
}
