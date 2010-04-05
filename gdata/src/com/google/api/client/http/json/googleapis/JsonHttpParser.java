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

package com.google.api.client.http.json.googleapis;

import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;

import java.io.IOException;

public final class JsonHttpParser implements HttpParser {

  private static final JsonHttpParser INSTANCE = new JsonHttpParser();

  public static void setAsParserOf(HttpTransport transport) {
    transport.setParser(INSTANCE);
  }

  public String getContentType() {
    return Json.CONTENT_TYPE;
  }

  public <T> T parse(HttpResponse response, Class<T> entityClass)
      throws IOException {
    return JsonHttp.parse(response, entityClass);
  }

  private JsonHttpParser() {
  }
}
