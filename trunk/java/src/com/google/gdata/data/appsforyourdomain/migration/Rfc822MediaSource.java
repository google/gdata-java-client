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


package com.google.gdata.data.appsforyourdomain.migration;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ContentType;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * {@link MediaSource} that wraps an RFC822 message in UTF8 format.
 * 
 * 
 */
public class Rfc822MediaSource implements MediaSource {

  private final String rfc822Msg;

  /**
   * Constructs a new {@code Rfc822MediaSource} from the RFC822 message in UTF-8
   * format.
   * 
   * @param rfc822Msg the text of the RFC822 message
   */
  public Rfc822MediaSource(String rfc822Msg) {
    if (rfc822Msg == null || "".equals(rfc822Msg)) {
      throw new IllegalArgumentException("Empty or null message");
    }
    this.rfc822Msg = rfc822Msg;
  }

  public long getContentLength() {
    return rfc822Msg.length();
  }

  public String getEtag() {
    return null;
  }

  public DateTime getLastModified() {
    return null;
  }

  public String getContentType() {
    return ContentType.MESSAGE_RFC822.toString();
  }

  public InputStream getInputStream() {
    return new ByteArrayInputStream(rfc822Msg.getBytes());
  }

  public String getName() {
    return "rfc822";
  }

  public OutputStream getOutputStream() {
    throw new UnsupportedOperationException();
  }

}
