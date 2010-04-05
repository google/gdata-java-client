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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class InputStreamHttpSerializer implements HttpSerializer {

  private final static int BUFFER_SIZE = 2048;

  private final String contentType;
  private final long contentLength;
  private final InputStream inputStream;

  public InputStreamHttpSerializer(InputStream inputStream, long contentLength,
      String contentType, String contentEncoding) {
    this.inputStream = inputStream;
    this.contentType = contentType;
    this.contentLength = contentLength;
  }

  public final void writeTo(OutputStream out) throws IOException {
    InputStream inputStream = this.inputStream;
    long contentLength = this.contentLength;
    byte[] buffer = new byte[BUFFER_SIZE];
    int read;
    try {
      if (contentLength < 0) {
        // consume until EOF
        while ((read = inputStream.read(buffer)) != -1) {
          out.write(buffer, 0, read);
        }
      } else {
        // consume no more than length
        long remaining = contentLength;
        while (remaining > 0) {
          read =
              inputStream.read(buffer, 0, (int) Math
                  .min(BUFFER_SIZE, remaining));
          if (read == -1) {
            break;
          }
          out.write(buffer, 0, read);
          remaining -= read;
        }
      }
    } finally {
      inputStream.close();
    }
  }

  public final String getContentEncoding() {
    return null;
  }

  public final long getContentLength() {
    return this.contentLength;
  }

  public final String getContentType() {
    return this.contentType;
  }
}
