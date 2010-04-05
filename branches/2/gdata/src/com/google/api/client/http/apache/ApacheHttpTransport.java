/* Copyright (c) 2010 Google Inc.
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

package com.google.api.client.http.apache;

import com.google.api.client.http.LowLevelHttpTransport;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

final class ApacheHttpTransport implements LowLevelHttpTransport {

  private final HttpClient httpClient;

  ApacheHttpTransport() {
    // Turn off stale checking. Our connections break all the time anyway,
    // and it's not worth it to pay the penalty of checking every time.
    HttpParams params = new BasicHttpParams();
    HttpConnectionParams.setStaleCheckingEnabled(params, false);
    // Default connection and socket timeout of 20 seconds. Tweak to taste.
    HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
    HttpConnectionParams.setSoTimeout(params, 20 * 1000);
    HttpConnectionParams.setSocketBufferSize(params, 8192);
    this.httpClient = new DefaultHttpClient(params);
  }

  public boolean supportsPatch() {
    return true;
  }

  public ApacheHttpRequest buildDeleteRequest(String uri) {
    return new ApacheHttpRequest(this.httpClient, new HttpDelete(uri));
  }

  public ApacheHttpRequest buildGetRequest(String uri) {
    return new ApacheHttpRequest(this.httpClient, new HttpGet(uri));
  }

  public ApacheHttpRequest buildPatchRequest(String uri) {
    return new ApacheHttpRequest(this.httpClient, new HttpPatch(uri));
  }

  public ApacheHttpRequest buildPostRequest(String uri) {
    return new ApacheHttpRequest(this.httpClient, new HttpPost(uri));
  }

  public ApacheHttpRequest buildPutRequest(String uri) {
    return new ApacheHttpRequest(this.httpClient, new HttpPut(uri));
  }
}
