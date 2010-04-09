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

package com.google.api.data.sample.youtube.model;

import com.google.api.client.DateTime;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.googleapis.GoogleTransport;

import java.io.IOException;

public class Feed {
  public int itemsPerPage;
  public int startIndex;
  public int totalItems;
  public DateTime updated;

  static Feed executeGet(GoogleTransport transport, String uri,
      Class<? extends Feed> feedClass) throws IOException {
    HttpRequest request = transport.buildGetRequest(uri);
    return request.execute().parseAs(feedClass);
  }
}
