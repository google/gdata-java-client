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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializes HTTP request content from an input stream into an output stream.
 * <p>
 * The {@code contentType} is required. The {@link #inputStream} and
 * {@link #length} are also required to specify the input stream to use and its
 * content length. For a file input stream, they should be specified using
 * {@link #setFileInput(File)}.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public final class InputStreamContent implements HttpContent {

  private final static int BUFFER_SIZE = 2048;

  /** Required content type. */
  public String type;

  /** Content length or less than zero if not known. Defaults to {@code -1}. */
  public long length = -1;

  /** Required input stream to read from. */
  public InputStream inputStream;

  /**
   * Content encoding (for example {@code "gzip"}) or {@code null} for none.
   */
  public String encoding;

  /**
   * Sets the {@link #inputStream} from a file input stream based on the given
   * file, and the {@link #length} based on the file's length.
   */
  public void setFileInput(File file) throws FileNotFoundException {
    this.inputStream = new FileInputStream(file);
    this.length = file.length();
  }

  public void writeTo(OutputStream out) throws IOException {
    InputStream inputStream = this.inputStream;
    long contentLength = this.length;
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

  public String getEncoding() {
    return this.encoding;
  }

  public long getLength() {
    return this.length;
  }

  public String getType() {
    return this.type;
  }
}
