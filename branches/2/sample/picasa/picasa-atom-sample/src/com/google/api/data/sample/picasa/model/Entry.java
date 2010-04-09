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

import com.google.api.client.Entities;
import com.google.api.client.Name;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.googleapis.GoogleTransport;
import com.google.api.client.http.xml.atom.googleapis.PatchRelativeToOriginalSerializer;
import com.google.api.client.xml.atom.googleapis.GData;
import com.google.api.data.picasa.v2.atom.PicasaAtom;

import java.io.IOException;
import java.util.List;

public class Entry implements Cloneable {

  @Name("@gd:etag")
  public String etag;

  @Name("link")
  public List<Link> links;

  public String summary;

  public String title;

  public String updated;

  public String getFeedLink() {
    return Link.find(links, "http://schemas.google.com/g/2005#feed");
  }

  public String getSelfLink() {
    return Link.find(links, "self");
  }

  @Override
  protected Entry clone() {
    return Entities.clone(this);
  }

  public void executeDelete(GoogleTransport transport) throws IOException {
    HttpRequest request = transport.buildDeleteRequest(getEditLink());
    request.setIfMatchHeader(etag);
    request.execute().ignore();
  }

  static Entry executeGet(GoogleTransport transport, PicasaUri uri,
      Class<? extends Entry> entryClass) throws IOException {
    uri.fields = GData.getFieldsFor(entryClass);
    HttpRequest request = transport.buildGetRequest(uri.build());
    return request.execute().parseAs(entryClass);
  }

  Entry executePatchRelativeToOriginal(GoogleTransport transport, Entry original)
      throws IOException {
    HttpRequest request = transport.buildPatchRequest(getEditLink());
    request.setIfMatchHeader(etag);
    PatchRelativeToOriginalSerializer serializer =
        new PatchRelativeToOriginalSerializer();
    serializer.namespaceDictionary = PicasaAtom.NAMESPACE_DICTIONARY;
    serializer.originalEntry = original;
    serializer.patchedEntry = this;
    request.setContent(serializer);
    return request.execute().parseAs(getClass());
  }

  private String getEditLink() {
    return Link.find(links, "edit");
  }
}
