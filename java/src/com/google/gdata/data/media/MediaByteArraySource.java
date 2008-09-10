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


package com.google.gdata.data.media;

import com.google.gdata.data.DateTime;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The MediaByteArraySource class provides a basic implementation of the
 * {@link MediaSource} interface that reads media data from an in-memory
 * byte array.  The content length of the media source defaults to the size
 * of the byte array and the time of modification to the current time.
 *
 * 
 * @see MediaSource
 */
public class MediaByteArraySource extends BaseMediaSource {

  private byte [] mediaBytes;

  /**
   * Constructs a new MediaBteyArraySource using the specified byte data
   * and content type.
   */
  public MediaByteArraySource(byte [] mediaBytes, String mediaType) {
    super(mediaType);

    this.mediaBytes = mediaBytes;
    this.contentLength = mediaBytes.length;
    this.lastModified = DateTime.now();
  }

  public InputStream getInputStream() {
    return new ByteArrayInputStream(mediaBytes);
  }

  public OutputStream getOutputStream() {
    throw new UnsupportedOperationException("Cannot write to MediaSource");
  }
}
