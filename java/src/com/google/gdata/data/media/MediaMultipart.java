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

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.data.IEntry;
import com.google.gdata.util.ContentType;

import java.io.InputStream;
import java.util.logging.Logger;

import javax.activation.CommandInfo;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * The MediaMultipart class provides helper code for parsing and generating MIME
 * multipart/related content used to transport GData media resources.  These
 * messages will always contains two parts: one with the Atom metadata about
 * the media and the other with the actual media content in any MIME format.
 *
 * 
 */
public class MediaMultipart extends MimeMultipart {

  private static final Logger LOGGER
      = Logger.getLogger(MediaMultipart.class.getName());

  static {
    loadMimeMappings();
  }

  protected MediaBodyPart atomPart;
  protected MediaBodyPart mediaPart;

  /**
   * Loads the default set of Java activation MIME mappings required by
   * the GData library.  Extends the basic set configured by the JavaMail
   * library to add mappings for Atom, RSS, and JSON application types.
   */
  public static void loadMimeMappings() {
    final String[] CONTENT_TYPES = new String[] {
        "application/atom+xml", "application/rss+xml", "application/json"
    };

    final String CONTENT_HANDLER = ";; x-java-content-handler"
        + "=com.google.gdata.data.media.GDataContentHandler";

    CommandMap commandMap = CommandMap.getDefaultCommandMap();
    if (commandMap instanceof MailcapCommandMap) {
      MailcapCommandMap mailcapMap = (MailcapCommandMap) commandMap;
      for (int i = 0; i < CONTENT_TYPES.length; i++) {
        CommandInfo[] comm = mailcapMap.getAllCommands(CONTENT_TYPES[i]);
        if (comm == null || comm.length == 0) {
          mailcapMap.addMailcap(CONTENT_TYPES[i] + CONTENT_HANDLER);
        }
      }
    } else {
      LOGGER.warning(
          "Unable to find MailcapCommandMap, skipping dynamic mailcap config.");
    }
  }

  /**
   * Constructor for subclasses.
   */
  protected MediaMultipart(String subType) {
    super(subType);
  }
  
  /**
   * Constructs a new MediaMultipart instance by parsing MIME content from
   * the provided input stream.
   */
  public MediaMultipart(String contentType, InputStream inputStream)
      throws MessagingException {

    // Construct a DataSource and pass it to the parent constructor.
    super(new MediaStreamSource(inputStream, contentType));

    // Validate message content.
    if (getCount() != 2) {
      throw new MessagingException("Multipart must have Atom and media part");
    }

    boolean atomFirst =
      getBodyPart(0).isMimeType(ContentType.ATOM.getMediaType());
    if (!atomFirst &&
        !getBodyPart(1).isMimeType(ContentType.ATOM.getMediaType())) {
      throw new MessagingException("No Atom MIME body part found");
    }
    atomPart = (MediaBodyPart)getBodyPart(atomFirst ? 0 : 1);
    mediaPart = (MediaBodyPart)getBodyPart(atomFirst ? 1 : 0);
  }
  
  /**
   * Constructs a new MediaMultipart instance from an Atom entry instance
   * and a media source.
   */
  public MediaMultipart(IEntry entry, MediaSource media)
      throws MessagingException {

    super("related");

    Preconditions.checkNotNull(entry, "entry");
    Preconditions.checkNotNull(media, "media");
    
    atomPart = new MediaBodyPart(entry);
    addBodyPart(atomPart);
    mediaPart = new MediaBodyPart(media);
    addBodyPart(mediaPart);
  }

  @Override
  public MimeBodyPart createMimeBodyPart(InputStream is)
      throws MessagingException {
    return new MediaBodyPart(is);
  }

  @Override
  public MimeBodyPart createMimeBodyPart(InternetHeaders headers,
                                         byte [] content)
      throws MessagingException {
    return new MediaBodyPart(headers, content);
  }

  /**
   * Returns the body part containing atom content.
   */
  public MediaBodyPart getAtomPart() { return atomPart; }

  /**
   * Returns the body part containing media content.
   */
  public MediaBodyPart getMediaPart() { return mediaPart; }
}
