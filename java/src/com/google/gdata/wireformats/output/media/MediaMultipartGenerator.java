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

import com.google.gdata.data.media.GDataContentHandler;
import com.google.gdata.data.media.MediaMultipart;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.output.OutputGenerator;
import com.google.gdata.wireformats.output.OutputProperties;

import java.io.IOException;
import java.io.OutputStream;

import javax.mail.MessagingException;

/**
 * The MediaMultipartGenerator class handles output generation for MIME 
 * multipart related documents containing Atom and media content as separate
 * parts.   Support for media-only output generation is provided by the
 * {@link MediaGenerator} class.
 * 
 * 
 */
public class MediaMultipartGenerator  
    implements OutputGenerator<MediaMultipart> {

  public AltFormat getAltFormat() {
    return AltFormat.MEDIA_MULTIPART;
  }

  public Class<MediaMultipart> getSourceType() {
    return MediaMultipart.class;
  }

  /**
   * Generates output for requests that target media resources.
   */
  public void generate(OutputStream contentStream, OutputProperties request, 
        MediaMultipart source) throws IOException {

    OutputProperties prevProperties = null;
    try {
      prevProperties =
          GDataContentHandler.setThreadOutputProperties(request);
      source.writeTo(contentStream);
    } catch (MessagingException me) {
      
      // Unwrap basic I/O errors 
      Throwable t = me.getCause();
      if (t instanceof IOException) {
        throw (IOException) t;
      }
      
      // Wrap others based upon an invalid input entry
      IOException ioe = new IOException("Invalid multipart content");
      ioe.initCause(new InvalidEntryException("Invalid media entry", me));
      throw ioe;
    } finally {
      GDataContentHandler.setThreadOutputProperties(prevProperties);
    }
  }
}
