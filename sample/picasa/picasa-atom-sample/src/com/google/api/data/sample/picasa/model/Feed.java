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

import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.xml.atom.GData;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.Key;
import com.google.api.client.xml.atom.AtomContent;
import com.google.api.data.picasa.v2.atom.PicasaWebAlbumsAtom;

import java.io.IOException;
import java.util.List;

/**
 * @author Yaniv Inbar
 */
public class Feed {

  @Key
  public Author author;

  @Key("openSearch:totalResults")
  public int totalResults;

  @Key("link")
  public List<Link> links;

  static Feed executeGet(GoogleTransport transport, PicasaUrl url,
      Class<? extends Feed> feedClass) throws IOException {
    url.fields = GData.getFieldsFor(feedClass);
    HttpRequest request = transport.buildGetRequest();
    request.url = url;
    return request.execute().parseAs(feedClass);
  }

  Entry executeInsert(GoogleTransport transport, Entry entry)
      throws IOException {
    HttpRequest request = transport.buildPostRequest();
    request.setUrl(Link.find(links, "http://schemas.google.com/g/2005#post"));
    AtomContent content = new AtomContent();
    content.namespaceDictionary = PicasaWebAlbumsAtom.NAMESPACE_DICTIONARY;
    content.entry = entry;
    request.content = content;
    return request.execute().parseAs(entry.getClass());
  }
}
