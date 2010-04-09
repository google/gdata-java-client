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
import com.google.api.client.http.googleapis.GoogleTransport;
import com.google.api.client.http.xml.atom.AtomSerializer;
import com.google.api.client.xml.atom.googleapis.GData;
import com.google.api.data.picasa.v2.atom.PicasaAtom;

import java.io.IOException;
import java.util.List;

public class Feed {

  public Author author;

  @Name("openSearch:totalResults")
  public int totalResults;

  @Name("link")
  public List<Link> links;


  static Feed executeGet(GoogleTransport transport, PicasaUri uri,
      Class<? extends Feed> feedClass) throws IOException {
    uri.fields = GData.getFieldsFor(feedClass);
    HttpRequest request = transport.buildGetRequest(uri.build());
    return request.execute().parseAs(feedClass);
  }

  Entry executeInsert(GoogleTransport transport, Entry entry)
      throws IOException {
    String postLink = Link.find(links, "http://schemas.google.com/g/2005#post");
    HttpRequest request = transport.buildPostRequest(postLink);
    AtomSerializer serializer = new AtomSerializer();
    serializer.namespaceDictionary = PicasaAtom.NAMESPACE_DICTIONARY;
    serializer.entry = entry;
    request.setContent(serializer);
    return request.execute().parseAs(entry.getClass());
  }
}
