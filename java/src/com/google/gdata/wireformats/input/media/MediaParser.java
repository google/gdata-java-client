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


package com.google.gdata.wireformats.input.media;

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.input.AbstractParser;
import com.google.gdata.wireformats.input.InputParser;
import com.google.gdata.wireformats.input.InputProperties;

import java.io.InputStream;

/**
 * The MediaParser class provides an {@link InputParser} implementation for
 * consuming media data and exposing it as an {@link MediaSource} result.
 *
 * 
 */
public class MediaParser extends AbstractParser<MediaSource> {

  public MediaParser() {
    super(AltFormat.MEDIA, MediaSource.class);
  }

  public <R extends MediaSource> R parse(ParseSource parseSource,
      InputProperties inProps, Class<R> resultClass) {
    
    // Ensure that the expected result type is MediaSource
    Preconditions.checkArgument(resultClass.isAssignableFrom(MediaSource.class),
        "Result class must be " + MediaSource.class.getName());
    
    InputStream inputStream = parseSource.getInputStream();
    Preconditions.checkNotNull(inputStream,
        "Parse source must be stream-based");   
    
    MediaStreamSource mediaSource = 
        new MediaStreamSource(inputStream, inProps.getContentType().toString());
   
    return resultClass.cast(mediaSource);
  }
}
