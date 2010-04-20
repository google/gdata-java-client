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

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.LowLevelHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class NetHttpResponse implements LowLevelHttpResponse {

  private final HttpURLConnection connection;
  private final int responseCode;
  private final String responseMessage;
  private final List<String> headerNames = new ArrayList<String>();
  private final List<String> headerValues = new ArrayList<String>();

  NetHttpResponse(HttpURLConnection connection) throws IOException {
    this.connection = connection;
    this.responseCode = connection.getResponseCode();
    this.responseMessage = connection.getResponseMessage();
    List<String> headerNames = this.headerNames;
    List<String> headerValues = this.headerValues;
    for (Map.Entry<String, List<String>> entry : connection.getHeaderFields()
        .entrySet()) {
      String key = entry.getKey();
      if (key != null) {
        headerNames.add(key);
        List<String> values = entry.getValue();
        headerValues.add(values.get(values.size() - 1));
      }
    }
  }

  public int getStatusCode() {
    return responseCode;
  }

  public InputStream getContent() throws IOException {
    HttpURLConnection connection = this.connection;
    return HttpResponse.isSuccessStatusCode(responseCode) ? connection
        .getInputStream() : connection.getErrorStream();
  }

  public String getContentEncoding() {
    return this.connection.getContentEncoding();
  }

  public long getContentLength() {
    String string = this.connection.getHeaderField("Content-Length");
    return string == null ? -1 : Long.parseLong(string);
  }

  public String getContentType() {
    return this.connection.getHeaderField("Content-Type");
  }

  public String getReasonPhrase() {
    return this.responseMessage;
  }

  public String getStatusLine() {
    String result = this.connection.getHeaderField(0);
    return result != null && result.startsWith("HTTP/1.") ? result : null;
  }

  public int getHeaderCount() {
    return this.headerNames.size();
  }

  public String getHeaderName(int index) {
    return this.headerNames.get(index);
  }

  public String getHeaderValue(int index) {
    return this.headerValues.get(index);
  }

}
