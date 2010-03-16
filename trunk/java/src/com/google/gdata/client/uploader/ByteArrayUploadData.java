/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.client.uploader;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * An implementation of {@link UploadData} that provides data from a {@code byte[]}.
 * 
 * 
 */
public class ByteArrayUploadData implements UploadData {
  private final byte[] buffer;
  private final ByteArrayInputStream stream;
  
  public ByteArrayUploadData(byte[] buffer) {
    this.buffer = buffer;
    stream = new ByteArrayInputStream(buffer);
  }
  
  public long length() {
    return buffer.length;
  }

  public void read(byte[] destination) throws IOException {
    stream.read(destination);
  }
  
  public void setPosition(long position) {
    stream.reset();
    stream.skip(position);
  }
  
  public int read(byte[] chunk, int i, int length) {
    return stream.read(chunk, i, length);
  }
}
