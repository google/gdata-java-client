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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The MediaFileSource class provides a basic implementation of the
 * {@link MediaSource} interface that reads media data from a {@link File}.
 * The content length will be initialized to the file length, the last 
 * modified time to the file modification time, and the name of the source
 * will be the name (last path element) of the file.
 *
 * 
 * @see MediaSource
 */
public class MediaFileSource extends BaseMediaSource {

  private File mediaFile;

  public MediaFileSource(File mediaFile, String mediaType) {
    super(mediaType);
    
    this.mediaFile = mediaFile;
    this.contentLength = mediaFile.length();
    this.lastModified = new DateTime(mediaFile.lastModified());
    this.name = mediaFile.getName();
  }

  public InputStream getInputStream() throws IOException {
    return new FileInputStream(mediaFile);
  }

  public OutputStream getOutputStream() {
    throw new UnsupportedOperationException("Cannot write to MediaFileSource");
  }
  
  public File getMediaFile() {
    return mediaFile;
  }
}
