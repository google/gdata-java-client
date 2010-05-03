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

package com.google.api.client.javanet;

import com.google.api.client.http.LowLevelHttpTransport;

import java.io.IOException;

final class NetHttpTransport extends LowLevelHttpTransport {

  NetHttpTransport() {
  }

  @Override
  public boolean supportsPatch() {
    return false;
  }

  @Override
  public NetHttpRequest buildDeleteRequest(String uri) throws IOException {
    return new NetHttpRequest("DELETE", uri);
  }

  @Override
  public NetHttpRequest buildGetRequest(String uri) throws IOException {
    return new NetHttpRequest("GET", uri);
  }

  @Override
  public NetHttpRequest buildPatchRequest(String uri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public NetHttpRequest buildPostRequest(String uri) throws IOException {
    return new NetHttpRequest("POST", uri);
  }

  @Override
  public NetHttpRequest buildPutRequest(String uri) throws IOException {
    return new NetHttpRequest("PUT", uri);
  }
}
