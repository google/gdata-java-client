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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;


/**
 * The MediaSource interface provides a model for accessing media content
 * sent by the GData client or returned by a GDataFeed service implementation.
 * It extends the JavaBeans Activation Framework {@link DataSource} interface,
 * making it possible to use the MediaSource as a DataSource within the
 * framework for media handling.
 * <p>
 * When creating new GData entries using a {@link MediaSource}, the return
 * value of the {@link DataSource#getName()} method will be used to create a
 * Slug header for the media source.  Some concrete implementations of the
 * MediaSource interface will provide the ability to set the name value and/or
 * provide a default value based upon source attributes.
 *
 * 
 */
public interface MediaSource extends DataSource {

  /**
   * Returns the length (in bytes) of the media source.  A value of -1
   * indicates the length is unknown.
   */
  public long getContentLength();

  /**
   * Returns the last modification time of the media content or {@code null}
   * if unknown.
   */
  public DateTime getLastModified();
  
  /**
   * Returns the entity tag (etag) value associated with the media content
   * or {@code null} if unknown.
   */
  public String getEtag();

  /**
   * The Output class provides helper methods for MediaSource handling.
   */
  public class Output {

    /**
     * Helper method that writes the contents of a media source to an output
     * stream.
     *
     * @param source the source media stream containing the media data.
     * @param outputStream the target output stream.
     * @throws IOException on any error writing the data to the stream.
     */
    public static void writeTo(MediaSource source, OutputStream outputStream)
        throws IOException {

      InputStream sourceStream = source.getInputStream();
      BufferedOutputStream bos = new BufferedOutputStream(outputStream);
      BufferedInputStream bis = new BufferedInputStream(sourceStream);

      try {
        byte [] buf = new byte[2048]; // Transfer in 2k chunks
        int bytesRead = 0;
        while ((bytesRead = bis.read(buf, 0, buf.length)) >= 0) {
          bos.write(buf, 0, bytesRead);
        }
        bos.flush();
      } finally {
        bis.close();
      }
    }
  }
}
