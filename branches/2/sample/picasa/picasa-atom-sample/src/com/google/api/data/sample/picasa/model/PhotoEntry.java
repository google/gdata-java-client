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

package com.google.api.data.sample.picasa.model;

import com.google.api.client.Name;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.InputStreamHttpSerializer;
import com.google.api.client.http.googleapis.GoogleHttp;
import com.google.api.client.http.googleapis.GoogleTransport;

import java.io.IOException;
import java.net.URL;

public class PhotoEntry extends Entry {

  public Category category = Category.newKind("photo");

  @Name("media:group")
  public MediaGroup mediaGroup;

  public static PhotoEntry executeInsert(GoogleTransport transport,
      String feedLink, URL photoUrl, String fileName) throws IOException {
    HttpRequest request = transport.buildPostRequest(feedLink);
    GoogleHttp.setSlugHeader(request, fileName);
    request.setContent(new InputStreamHttpSerializer(photoUrl.openStream(), -1,
        "image/jpeg", null));
    return request.execute().parseAs(PhotoEntry.class);
  }
}
