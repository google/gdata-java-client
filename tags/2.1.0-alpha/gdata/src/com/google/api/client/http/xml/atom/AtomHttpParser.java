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

package com.google.api.client.http.xml.atom;

import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.client.xml.atom.Atom;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public final class AtomHttpParser implements HttpParser {

  public XmlNamespaceDictionary namespaceDictionary;

  public String getContentType() {
    return Atom.CONTENT_TYPE;
  }

  public <T> T parse(HttpResponse response, Class<T> entityClass)
      throws IOException {
    try {
      return AtomHttp.parse(response, entityClass, this.namespaceDictionary);
    } catch (XmlPullParserException e) {
      IOException exception = new IOException();
      exception.initCause(e);
      throw exception;
    }
  }
}
