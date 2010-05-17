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
import com.google.api.client.googleapis.xml.atom.PatchRelativeToOriginalContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.DataUtil;
import com.google.api.client.util.Key;
import com.google.api.client.xml.atom.AtomContent;
import com.google.api.data.picasa.v2.atom.PicasaWebAlbumsAtom;

import java.io.IOException;
import java.util.List;

/**
 * @author Yaniv Inbar
 */
public class Entry implements Cloneable {

  @Key("@gd:etag")
  public String etag;

  @Key("link")
  public List<Link> links;

  @Key
  public String title;

  @Override
  protected Entry clone() {
    return DataUtil.clone(this);
  }

  public void executeDelete(GoogleTransport transport) throws IOException {
    HttpRequest request = transport.buildDeleteRequest();
    request.setUrl(getEditLink());
    request.headers.ifMatch = this.etag;
    request.execute().ignore();
  }

  Entry executePatchRelativeToOriginal(GoogleTransport transport, Entry original)
      throws IOException {
    HttpRequest request = transport.buildPatchRequest();
    request.setUrl(getEditLink());
    request.headers.ifMatch = this.etag;
    PatchRelativeToOriginalContent serializer =
        new PatchRelativeToOriginalContent();
    serializer.namespaceDictionary = PicasaWebAlbumsAtom.NAMESPACE_DICTIONARY;
    serializer.originalEntry = original;
    serializer.patchedEntry = this;
    request.content = serializer;
    return request.execute().parseAs(getClass());
  }

  static Entry executeInsert(GoogleTransport transport, Entry entry,
      String postLink) throws IOException {
    HttpRequest request = transport.buildPostRequest();
    request.setUrl(postLink);
    AtomContent content = new AtomContent();
    content.namespaceDictionary = PicasaWebAlbumsAtom.NAMESPACE_DICTIONARY;
    content.entry = entry;
    request.content = content;
    return request.execute().parseAs(entry.getClass());
  }

  private String getEditLink() {
    return Link.find(links, "edit");
  }
}
