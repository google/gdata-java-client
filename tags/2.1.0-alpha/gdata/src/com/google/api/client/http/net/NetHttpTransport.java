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

package com.google.api.client.http.net;

import com.google.api.client.http.LowLevelHttpTransport;

import java.io.IOException;

final class NetHttpTransport implements LowLevelHttpTransport {

  NetHttpTransport() {
  }

  public boolean supportsPatch() {
    return false;
  }

  public NetHttpRequest buildDeleteRequest(String uri) throws IOException {
    return new NetHttpRequest("DELETE", uri);
  }

  public NetHttpRequest buildGetRequest(String uri) throws IOException {
    return new NetHttpRequest("GET", uri);
  }

  public NetHttpRequest buildPatchRequest(String uri) throws IOException {
    return new NetHttpRequest("PATCH", uri);
  }

  public NetHttpRequest buildPostRequest(String uri) throws IOException {
    return new NetHttpRequest("POST", uri);
  }

  public NetHttpRequest buildPutRequest(String uri) throws IOException {
    return new NetHttpRequest("PUT", uri);
  }
}
