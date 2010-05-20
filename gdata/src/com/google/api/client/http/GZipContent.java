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
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Serializes another source of HTTP content using GZip compression.
 * 
 * @author Yaniv Inbar
 */
final class GZipContent implements HttpContent {

  private static final long MIN_GZIP_BYTES = 256;

  private final HttpContent httpContent;

  private final boolean isGZiped;

  private final String contentEncoding;

  private final String contentType;

  private final long contentLength;

  /**
   * @param httpContent another source of HTTP content
   */
  public GZipContent(HttpContent httpContent) {
    this.httpContent = httpContent;
    String contentEncoding = httpContent.getEncoding();
    long contentLength = this.contentLength = httpContent.getLength();
    String contentType = this.contentType = httpContent.getType();
    boolean isGZiped =
        this.isGZiped =
            contentLength >= MIN_GZIP_BYTES && contentEncoding == null
                && LogContent.isTextualContentType(contentType);
    if (isGZiped) {
      contentEncoding = "gzip";
    }
    this.contentEncoding = contentEncoding;
  }

  public void writeTo(OutputStream out) throws IOException {
    HttpContent httpSerializer = this.httpContent;
    if (this.isGZiped) {
      GZIPOutputStream zipper = new GZIPOutputStream(out);
      httpSerializer.writeTo(zipper);
      zipper.finish();
    } else {
      httpSerializer.writeTo(out);
    }
  }

  public String getEncoding() {
    return this.contentEncoding;
  }

  public long getLength() {
    return this.contentLength;
  }

  public String getType() {
    return this.contentType;
  }
}
