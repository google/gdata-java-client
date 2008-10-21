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


package com.google.gdata.client.media;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.GDataProtocol;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.data.media.MediaMultipart;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.RedirectRequiredException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

/**
 * The MediaService class extends the base {@link GoogleService} class to add
 * support for media content handling.   GData services that support posting of
 * MIME content in addition to Atom metadata will be derived from this base
 * class.
 *
 * 
 */
public class MediaService extends GoogleService {
  
  /**
   * Used to set the default buffer size when using Transfer-Encoding: chunked.
   * Setting this to 0 uses the default which is 4MB.
   */
  public static final int DEFAULT_CHUNKED_BUFFER_SIZE = 0;

  /**
   * Used to specify that the media write requests will not be chunked, but 
   * sent in one piece. 
   * 
   * @see MediaService#setChunkedMediaUpload(int)
   */
  public static final int NO_CHUNKED_MEDIA_REQUEST = -1;
  
  /**
   * The size of the buffer to send media write requests, when using
   * Transfer-Encoding: chunked. If the value is equal to 
   * {@link #NO_CHUNKED_MEDIA_REQUEST}, no chunking will be performed. 
   */
  private int chunkedBufferSize = DEFAULT_CHUNKED_BUFFER_SIZE;
  
  /**
   * Constructs a MediaService instance connecting to the service with name
   * {@code serviceName} for an application with the name
   * {@code applicationName}. The default domain (www.google.com) will be
   * used to authenticate.
   *
   * @param serviceName     the name of the Google service to which we are
   *                        connecting. Sample names of services might include
   *                        "cl" (Calendar), "mail" (GMail), or
   *                        "blogger" (Blogger)
   * @param applicationName the name of the client application accessing the
   *                        service.  Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */
  public MediaService(String serviceName,
                      String applicationName) {

    super(serviceName, applicationName);
  }
  
  /**
   * Constructs an instance connecting to the service for an application 
   * with the name {@code applicationName} and the given 
   * {@code GDataRequestFactory} and {@code AuthTokenFactory}. Use
   * this constructor to override the default factories.
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   * @param requestFactory the request factory that generates gdata request
   *                       objects
   * @param authTokenFactory the factory that creates auth tokens
   */
  public MediaService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    
    super(applicationName, requestFactory, authTokenFactory);
  }

  /**
   * Constructs a MediaService instance connecting to the service with name
   * {@code serviceName} for an application with the name
   * {@code applicationName}.  The service will authenticate at the provided
   * {@code domainName}.
   *
   * @param serviceName     the name of the Google service to which we are
   *                        connecting. Sample names of services might include
   *                        "cl" (Calendar), "mail" (GMail), or
   *                        "blogger" (Blogger)
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   * @param protocol        name of protocol to use for authentication
   *                        ("http"/"https")
   * @param domainName      the name of the domain hosting the login handler
   */
  public MediaService(String serviceName,
                      String applicationName,
                      String protocol,
                      String domainName) {

    super(serviceName, applicationName, protocol, domainName);
  }

  /**
   * Configures the service to use chunked streaming mode for media write
   * requests. 
   * <p>
   * By default, the service is configured to use Transfer-Encoding: chunked 
   * using the {@link #DEFAULT_CHUNKED_BUFFER_SIZE}. Use this method to change
   * the size buffer size, or to disable the chunked mode entirely.  
   * 
   * @param chunkSizeInBytes specifies the buffer size (in bytes) to be used 
   *     when sending a media write request. 
   *     Use {@link #DEFAULT_CHUNKED_BUFFER_SIZE} for the default value. 
   *     Use {@link #NO_CHUNKED_MEDIA_REQUEST} for not using chunked requests.
   *     Use a positive number to specify the size of each buffer.
   * 
   * @see HttpURLConnection#setChunkedStreamingMode(int)
   */
  public void setChunkedMediaUpload(int chunkSizeInBytes) { 
    this.chunkedBufferSize = chunkSizeInBytes;
  }
  
  /**
   * Returns a {@link MediaSource} that can be used to read the media pointed
   * to by the media url.
   *
   * @param mediaUrl the media content describing the media
   * @param contentType media content type
   * @param ifModifiedSince used to set a precondition date that indicates the
   *          media should be returned only if it has been modified after the
   *          specified date. A value of {@code null} indicates no precondition.
   * @return media source that can be used to access the media content.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException entry request creation failed.
   */
  private MediaSource getMediaResource(URL mediaUrl, ContentType contentType,
      DateTime ifModifiedSince)
      throws IOException, ServiceException {

    GDataRequest request =
        createRequest(GDataRequest.RequestType.QUERY,
            mediaUrl, contentType);
    request.setIfModifiedSince(ifModifiedSince);
    request.execute();
    InputStream resultStream = request.getResponseStream();

    MediaStreamSource mediaSource =
        new MediaStreamSource(resultStream, 
            request.getResponseContentType().toString());

    DateTime lastModified = 
        request.getResponseDateHeader(GDataProtocol.Header.LAST_MODIFIED);
    if (lastModified != null) {
      mediaSource.setLastModified(lastModified);
    }
    String etag = request.getResponseHeader(GDataProtocol.Header.ETAG);
    if (etag != null) {
      mediaSource.setEtag(etag);
    }

    return mediaSource;
  }


  /**
   * Returns a {@link MediaSource} that can be used to read the external
   * media content of an entry.
   *
   * @param mediaContent the media content describing the media
   * @param ifModifiedSince used to set a precondition date that indicates the
   *          media should be returned only if it has been modified after the
   *          specified date. A value of {@code null} indicates no precondition.
   * @return media source that can be used to access the media content.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException entry request creation failed.
   */
  public MediaSource getMedia(MediaContent mediaContent,
                              DateTime ifModifiedSince)
      throws IOException, ServiceException {

    URL mediaUrl = null;
    try {
      mediaUrl = new URL(mediaContent.getUri());
      return getMediaResource(mediaUrl,
          mediaContent.getMimeType(), ifModifiedSince);
    } catch (MalformedURLException mue) {
      throw new ServiceException(
          CoreErrorDomain.ERR.invalidMediaSourceUri, mue);
    } catch (RedirectRequiredException e) {
      mediaUrl = handleRedirectException(e);
    } catch (SessionExpiredException e) {
      handleSessionExpiredException(e);
    }
    return getMediaResource(mediaUrl,
        mediaContent.getMimeType(), ifModifiedSince);
  }


  /**
   * Returns a {@link MediaSource} that can be used to read the external
   * media content of an entry.
   *
   * @param mediaContent the media content describing the media
   * @return GData request instance that can be used to read the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException entry request creation failed.
   */
  public MediaSource getMedia(MediaContent mediaContent)
      throws IOException, ServiceException {
    return getMedia(mediaContent, null);
  }


  /**
   * Initializes the attributes of a media request.
   */
  private void initMediaRequest(MediaSource media, GDataRequest request)
      throws IOException {
    String name = media.getName();
    if (name != null) {
      request.setHeader("Slug", MimeUtility.encodeText(name, "utf-8", null));
    }
    if (chunkedBufferSize != NO_CHUNKED_MEDIA_REQUEST
        && request instanceof HttpGDataRequest) {
      HttpGDataRequest httpRequest = (HttpGDataRequest) request;
      httpRequest.getConnection().setChunkedStreamingMode(chunkedBufferSize);
    }
  }


  /**
   * Inserts a new {@link com.google.gdata.data.Entry} into a feed associated
   * with the target service.  It will return the inserted Entry, including
   * any additional attributes or extensions set by the GData server.
   *
   * If the Entry has been associated with a {@link MediaSource} through the
   * {@link MediaEntry#setMediaSource(MediaSource)} method then both the entry
   * and the media resource will be inserted into the media feed associated
   * with the target service.
   * If the media source has a name ({@link MediaSource#getName()} that is
   * non-null), the name will be provided as a Slug header that is sent
   * along with request and <i>may</i> be used as a hint when determining
   * the ID, url, and/or title of the inserted resource.
   * <p>
   * To insert only media content, use
   * {@link #insert(URL, Class, MediaSource)}.
   *
   * @param feedUrl the POST URI associated with the target feed.
   * @param entry the new entry to insert into the feed.
   * @return the newly inserted Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the return 
   *         entry data.
   * @throws ServiceException insert request failed due to system error.
   *
   * @see com.google.gdata.data.BaseFeed#getEntryPostLink()
   * @see com.google.gdata.data.BaseFeed#insert(BaseEntry)
   */
  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends BaseEntry<?>> E insert(URL feedUrl, E entry)
      throws IOException, ServiceException {

    if (entry == null) {
      throw new NullPointerException("Must supply entry");
    }

    // Delegate non-media handling to base class
    MediaSource media = (entry instanceof MediaEntry) ?
        ((MediaEntry) entry).getMediaSource() : null;
    if (media == null) {
      return super.insert(feedUrl, entry);
    }

    ParseSource resultEntrySource = null;
    try {
      // Write as MIME multipart containing the entry and media
      MediaMultipart mediaMultipart = new MediaMultipart(entry, media);
      GDataRequest request =
          createRequest(GDataRequest.RequestType.INSERT, feedUrl,
              new ContentType(mediaMultipart.getContentType()));
      initMediaRequest(media, request);
      OutputStream outputStream = request.getRequestStream();
      mediaMultipart.writeTo(outputStream);

      request.execute();

      resultEntrySource = request.getParseSource();
      return (E) parseEntry(entry.getClass(), resultEntrySource);

    } catch (MessagingException e) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantWriteMimeMultipart, e);
    } finally {
      closeSource(resultEntrySource);
    }
  }

  /**
   * Inserts a new media resource read from {@link MediaSource} into a
   * media feed associated with the target service.  It will return the
   * resulting entry that describes the inserted media, including
   * any additional attributes or extensions set by the GData server.
   * To insert both the entry and the media content in a single request, use
   * {@link #insert(URL, BaseEntry)}.
   * <p>
   * If the media source has a name ({@link MediaSource#getName()} that is
   * non-null), the name will be provided as a Slug header that is sent
   * along with request and <i>may</i> be used as a hint when determining
   * the ID, url, and/or title of the inserted resource.
   *
   * @param feedUrl the POST URI associated with the target feed.
   * @param entryClass the class used to parse the returned entry.
   * @param media the media source that contains the media content to insert.
   * @return the newly inserted entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the returned 
   *         entry data.
   * @throws ServiceException insert request failed due to system error.
   *
   * @see com.google.gdata.data.BaseFeed#getEntryPostLink()
   * @see com.google.gdata.data.media.MediaFeed#insert(MediaSource)
   */
  @SuppressWarnings({"unchecked"})
  public <E extends BaseEntry> E insert(URL feedUrl, Class<E> entryClass,
                                        MediaSource media)
      throws IOException, ServiceException {

    if (media == null) {
      throw new NullPointerException("Must supply media source");
    }

    ParseSource resultEntrySource = null;
    try {
      // Write media content only.
      GDataRequest request =
          createRequest(GDataRequest.RequestType.INSERT, feedUrl,
              new ContentType(media.getContentType()));
      initMediaRequest(media, request);

      // Write the media data
      MediaSource.Output.writeTo(media, request.getRequestStream());
      request.execute();
      resultEntrySource = request.getParseSource();
      return parseEntry(entryClass, resultEntrySource);

    } finally {
      closeSource(resultEntrySource);
    }
  }

  /**
   * Updates an existing entry and associated media resource by writing it
   * to the specified media edit URL.  The resulting entry (after update) will
   * be returned.  To update only the media content, use
   * {@link #updateMedia(URL, Class, MediaSource)}.
   *
   * @param mediaUrl the media edit URL associated with the resource.
   * @param entry the updated entry to be written to the server.
   * @return the updated entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the updated 
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see BaseEntry#getMediaEditLink()
   * @see MediaEntry#updateMedia(boolean)
   */
  @SuppressWarnings({"unchecked"})
  public <E extends BaseEntry> E updateMedia(URL mediaUrl, E entry)
      throws IOException, ServiceException {

    if (entry == null) {
      throw new NullPointerException("Must supply entry");
    }

    // Since the input parameter is a media-edit URL, this method should
    // not be used to post Atom-only entries.  These entries should be
    // sent to the edit URL.
    MediaSource media = (entry instanceof MediaEntry) ?
        ((MediaEntry) entry).getMediaSource() : null;
    if (media == null) {
      throw new NullPointerException("Must supply media source");
    }

    ParseSource resultEntrySource = null;
    try {
      // Write as MIME multipart containing the entry and media
      MediaMultipart mediaMultipart = new MediaMultipart(entry, media);
      GDataRequest request =
          createRequest(GDataRequest.RequestType.UPDATE, mediaUrl,
              new ContentType(mediaMultipart.getContentType()));
      OutputStream outputStream = request.getRequestStream();
      mediaMultipart.writeTo(outputStream);
      request.execute();

      resultEntrySource = request.getParseSource();
      return (E) parseEntry(entry.getClass(), resultEntrySource);

    } catch (MessagingException e) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantWriteMimeMultipart, e);
    } finally {
      closeSource(resultEntrySource);
    }
  }


  /**
   * Updates an existing media resource with data read from the
   * {@link MediaSource} by writing it it to the specified media edit URL.
   * The resulting entry (after update) will be returned.  To update both
   * the entry and the media content in a single request, use
   * {@link #updateMedia(URL, BaseEntry)}.
   *
   * @param mediaUrl the media edit URL associated with the resource.
   * @param entryClass the class that will be used to represent the
   *        resulting entry.
   * @param media the media source data to be written to the server.
   * @return the updated Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the updated 
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see BaseEntry#getMediaEditLink()
   * @see MediaEntry#updateMedia(boolean)
   */
  @SuppressWarnings({"unchecked"})
  public <E extends BaseEntry> E updateMedia(URL mediaUrl,
                                        Class<E> entryClass,
                                        MediaSource media)
      throws IOException, ServiceException {

    // Since the input parameter is a media-edit URL, this method should
    // not be used to post Atom-only entries.  These entries should be
    // sent to the edit URL.
    if (media == null) {
      throw new NullPointerException("Must supply media source");
    }

    ParseSource resultEntrySource = null;
    try {
      GDataRequest request =
          createRequest(GDataRequest.RequestType.UPDATE, mediaUrl,
              new ContentType(media.getContentType()));
      MediaSource.Output.writeTo(media, request.getRequestStream());
      request.execute();

      resultEntrySource = request.getParseSource();
      return parseEntry(entryClass, resultEntrySource);

    } finally {
      closeSource(resultEntrySource);
    }
  }
}
