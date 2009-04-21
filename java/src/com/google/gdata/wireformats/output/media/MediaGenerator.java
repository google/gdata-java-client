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


package com.google.gdata.wireformats.output.media;

import com.google.gdata.data.media.MediaSource;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.output.OutputGenerator;
import com.google.gdata.wireformats.output.OutputProperties;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The MediaGenerator class implements the {@link OutputGenerator} interface
 * for generating media data for a service that supports media content.  Support
 * for media multipart documents is provided by the 
 * {@link MediaMultipartGenerator} class.
 *
 * 
 */

public class MediaGenerator implements OutputGenerator<MediaSource> {

  public AltFormat getAltFormat() {
    return AltFormat.MEDIA;
  }

  public Class<MediaSource> getSourceType() {
    return MediaSource.class;
  }

  /**
   * Generates output for requests that target media resources.
   */
  public void generate(OutputStream contentStream,
                       OutputProperties request,
                       MediaSource source)
      throws IOException {

    MediaSource.Output.writeTo(source, contentStream);
  }
}
