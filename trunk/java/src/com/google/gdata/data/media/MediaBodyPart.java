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

import com.google.gdata.client.GDataProtocol;
import com.google.gdata.data.IEntry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;

/**
 * The MediaBodyPart class extends the base MimeBodyPart implementation
 * to provide streaming media support for GData MIME multipart content.
 * When the body part contains media content (anything other than
 * "application/atom+xml"), the body part will use a custom
 * {@link DataHandler} implementation that keeps the data in raw stream format.
 *
 * 
 * @see GDataContentHandler
 * @see MediaSource
 * @see MediaMultipart
 */
public class MediaBodyPart extends MimeBodyPart {

  private MediaSource mediaSource;

  /**
   * The MediaDataHandler class customizes the {@link DataHandler} class
   * of the Java Activation Framework.  It simply wraps the original
   * {@link MediaSource} instance associated with the media, and returns it
   * as the content representation for the data.
   */
  private static class MediaSourceDataHandler extends DataHandler {

    MediaSource source;

    private MediaSourceDataHandler(MediaSource ds) {
      super(ds);
      source = ds;
    }

    @Override
    public Object getContent() {
      // In the case of the MediaSource handler, the content representation
      // is just a reference to the underlying media source.
      return source;
    }

    @Override
    public void writeTo(OutputStream os)
        throws IOException {

      MediaSource.Output.writeTo(source, os);
    }
  }

  public MediaBodyPart() {
    super();
  }

  public MediaBodyPart(InputStream is) throws MessagingException {

    // Process the headers from the input stream
    super(new InternetHeaders(is), null);

    // Create a media stream source to read the remaining data, using the
    // content type information from the headers.
    mediaSource = new MediaStreamSource(is, getContentType());
    initMediaDataHandler();
  }

  public MediaBodyPart(IEntry entry) throws MessagingException {
    super();
    setContent(entry, "application/atom+xml");
    setHeader("Content-Type", "application/atom+xml");
  }

  public MediaBodyPart(MediaSource mediaSource) throws MessagingException {
    super();
    this.mediaSource = mediaSource;
    //headers.setHeader("Content-Type", mediaSource.getContentType());
    setHeader("Content-Type", mediaSource.getContentType());
    String etag = mediaSource.getEtag();
    if (etag != null) {
      setHeader(GDataProtocol.Header.ETAG, etag);
    }
    initMediaDataHandler();
  }

  public MediaBodyPart(InternetHeaders headers,  byte [] content)
      throws MessagingException {
    super(headers, content);
    // javax.mail.util.SharedByteArrayInputStream here.
    String contentType = getContentType();
    MediaStreamSource mediaStreamSource =
        new MediaStreamSource(new ByteArrayInputStream(content), contentType);
    String etag = getHeader(GDataProtocol.Header.ETAG, null);
    if (etag != null) {
      mediaStreamSource.setEtag(etag);
    }
    mediaSource = mediaStreamSource;
    initMediaDataHandler();
  }

  /**
   * Initializes the {@link DataHandler} associated with the body part
   * when it contains media content.
   */
  private void initMediaDataHandler() throws MessagingException {

    // Cannot call setDataHandler() because it invalidates header data
    // Fortunately, 'dh' is a protected field on the base class.
    if (!isAtomPart()) {
      // For media content, simply wrap the media source.
      dh = new MediaSourceDataHandler(mediaSource);
    } else {
      // For Atom content, use standard Java activation handling.
      dh = new DataHandler(mediaSource);
    }
  }

  /**
   * Returns {@code true} if the body part contains Atom data.
   */
  public boolean isAtomPart() throws MessagingException {
    return isMimeType("application/atom+xml");
  }

  /**
   * Returns the media source associated with the body part.
   */
  public MediaSource getMediaSource() {
    return mediaSource;
  }
}
