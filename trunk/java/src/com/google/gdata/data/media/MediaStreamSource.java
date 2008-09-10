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

import java.io.InputStream;
import java.io.OutputStream;

import javax.mail.internet.SharedInputStream;

/**
 * The MediaStreamSource class provides a basic implementation of the
 * {@link MediaSource} interface that reads media data from an existing
 * {@link InputStream}.
 *
 * 
 * @see MediaSource
 */
public class MediaStreamSource extends BaseMediaSource {

  private InputStream mediaStream;
  private long sharedMediaStreamStart;

  public MediaStreamSource(InputStream mediaStream,
                            String mediaType,
                            DateTime lastModified,
                            long contentLength) {

    super(mediaType);
    this.mediaStream = mediaStream;
    this.lastModified = lastModified;
    this.contentLength = contentLength;

    if (mediaStream instanceof SharedInputStream) {
      sharedMediaStreamStart = ((SharedInputStream)mediaStream).getPosition();
    }
  }

  public MediaStreamSource(InputStream mediaStream,
                            String mediaType) {
    this(mediaStream, mediaType, null, -1);
  }

  public InputStream getInputStream() {

    // If the underlying stream implements SharedInputStream, then get
    // a new stream so the stream source can be read multiple times.
    InputStream returnStream = (mediaStream instanceof SharedInputStream)
        ? ((SharedInputStream)mediaStream).newStream(sharedMediaStreamStart, -1)
        : mediaStream;

    return returnStream;
  }

  public OutputStream getOutputStream() {
    throw new UnsupportedOperationException(
        "Cannot write to MediaStreamSource");
  }
}
