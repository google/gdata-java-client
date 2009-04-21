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
import com.google.gdata.data.media.GDataContentHandler;
import com.google.gdata.data.media.MediaMultipart;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.input.AbstractParser;
import com.google.gdata.wireformats.input.InputProperties;

import java.io.InputStream;

import javax.mail.MessagingException;

/**
 * The MediaMultipartParser is an
 * {@link com.google.gdata.wireformats.input.InputParser} implementation that
 * is capable parsing GData media multipart data streams to produce a
 * {@link MediaMultipart} instance.
 * 
 * 
 */
public class MediaMultipartParser extends AbstractParser<MediaMultipart> {
  
  public MediaMultipartParser() {
    super(AltFormat.MEDIA_MULTIPART, MediaMultipart.class);
  }
  
  /**
   * Creates the MediaMultipart instance that does the actual parsing into
   * multipart body parts.
   * 
   * @param parseSource source input stream
   * @param inputProperties input properties
   * @return resulting media multipart
   * @throws MessagingException
   */
  protected MediaMultipart createMultipart(ParseSource parseSource, 
      InputProperties inputProperties) throws MessagingException {
    return new MediaMultipart(inputProperties.getContentType().toString(),
        parseSource.getInputStream());          
  }

  public <R extends MediaMultipart> R parse(ParseSource parseSource,
      InputProperties inputProperties, Class<R> resultClass) 
      throws ServiceException {

    Preconditions.checkArgument(
        MediaMultipart.class.isAssignableFrom(resultClass),
        "Result class must extend " + MediaMultipart.class.getName());
    
    InputStream inputStream = parseSource.getInputStream();
    Preconditions.checkNotNull(inputStream,
        "Parse source must be stream-based");
    
    InputProperties prevProperties = null;
    try {
      prevProperties = 
        GDataContentHandler.setThreadInputProperties(inputProperties);

      MediaMultipart result = createMultipart(parseSource, inputProperties);
      return resultClass.cast(result);

    } catch (MessagingException me) {
      throw new InvalidEntryException(me.getMessage(), me);
    } finally {
      GDataContentHandler.setThreadInputProperties(prevProperties);
    }
  }
}
