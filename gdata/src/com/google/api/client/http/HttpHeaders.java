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

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

/**
 * Stores HTTP headers used in an HTTP request. {@code null} is not allowed as a
 * name or value of a header.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class HttpHeaders extends GenericData {

  @Key("Accept-Encoding")
  public String acceptEncoding;

  @Key("User-Agent")
  public String userAgent;

  @Key("Authorization")
  public String authorization;

  @Key("If-Match")
  public String ifMatch;

  @Key("If-None-Match")
  public String ifNoneMatch;

  @Key("MIME-Version")
  public String mimeVersion;

  @Override
  public HttpHeaders clone() {
    return (HttpHeaders) super.clone();
  }
}
