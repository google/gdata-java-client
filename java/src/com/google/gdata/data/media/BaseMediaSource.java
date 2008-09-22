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


/**
 * The BaseMediaSource class provides an abstract base class
 * implementation of the {@link MediaSource} interface.
 *
 * 
 * @see MediaSource
 */
abstract public class BaseMediaSource implements MediaSource {

  protected final String mediaType;           // required
  protected long contentLength = -1;          // default: unknown
  protected DateTime lastModified;            // default: unknown
  protected String name;                      // default: none
  protected String etag;                      // default: unknown

  /**
   * Constructs a new BaseMediaSource of the specified content type.
   */
  protected BaseMediaSource(String mediaType) {
    this.mediaType = mediaType;
  }

  public String getContentType() { return mediaType; }

  public void setName(String name) { this.name = name; }
  public String getName() { return name; }

  public void setLastModified(DateTime lastModified) {
    this.lastModified = lastModified;
  }
  public DateTime getLastModified() { return lastModified; }

  public void setContentLength(long contentLength) {
    this.contentLength = contentLength;
  }
  public long getContentLength() { return contentLength; }

  public void setEtag(String etag) {
    this.etag = etag;
  }
  public String getEtag() { return etag; }
}
