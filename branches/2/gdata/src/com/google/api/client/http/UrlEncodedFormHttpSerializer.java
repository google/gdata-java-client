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

package com.google.api.client.http;

import com.google.api.client.escape.CharEscapers;
import com.google.api.client.util.Entities;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Implements support for HTTP form content encoding serialization of type
 * {@code application/x-www-form-urlencoded} as specified in the <a href=
 * "http://www.w3.org/TR/1998/REC-html40-19980424/interact/forms.html#h-17.13.4.1"
 * >HTML 4.0 Specification</a>.
 */
public final class UrlEncodedFormHttpSerializer implements HttpSerializer {

  private final byte[] content;

  public UrlEncodedFormHttpSerializer(Object entity) {
    StringBuilder buf = new StringBuilder();
    boolean first = true;
    for (Map.Entry<String, Object> nameValueEntry : Entities.mapOf(entity)
        .entrySet()) {
      Object value = nameValueEntry.getValue();
      if (value != null) {
        if (first) {
          first = false;
        } else {
          buf.append('&');
        }
        String name = nameValueEntry.getKey();
        buf.append(CharEscapers.escapeUri(name)).append('=').append(
            CharEscapers.escapeUri(value.toString()));
      }
    }
    try {
      this.content = buf.toString().getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

  public String getContentEncoding() {
    return null;
  }

  public long getContentLength() {
    return this.content.length;
  }

  public String getContentType() {
    return "application/x-www-form-urlencoded";
  }

  public void writeTo(OutputStream out) throws IOException {
    out.write(this.content);
  }
}
