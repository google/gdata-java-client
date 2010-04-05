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

package com.google.api.client.http.apache;

import com.google.api.client.http.HttpSerializer;

import org.apache.http.entity.AbstractHttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class GDataEntity extends AbstractHttpEntity {

  private final long contentLength;
  private final HttpSerializer serializer;

  GDataEntity(long contentLength, HttpSerializer serializer) {
    this.contentLength = contentLength;
    this.serializer = serializer;
  }

  public InputStream getContent() {
    throw new UnsupportedOperationException();
  }

  public long getContentLength() {
    return this.contentLength;
  }

  public boolean isRepeatable() {
    return false;
  }

  public boolean isStreaming() {
    return true;
  }

  public void writeTo(OutputStream out) throws IOException {
    this.serializer.writeTo(out);
  }
}
