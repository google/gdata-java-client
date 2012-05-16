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

import com.google.gdata.util.common.base.PercentEscaper;
import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.GDataProtocol;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.media.IMediaContent;
import com.google.gdata.data.media.IMediaEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.MediaMultipart;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.RedirectRequiredException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.AltRegistry;
import com.google.gdata.wireformats.input.media.MediaMultipartParser;
import com.google.gdata.wireformats.input.media.MediaParser;
import com.google.gdata.wireformats.output.media.MediaGenerator;
import com.google.gdata.wireformats.output.media.MediaMultipartGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Nullable;
import javax.mail.MessagingException;

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
   * Returns an {@link AltRegistry} instance that is configured with the
   * default parser/generator configuration for a media service. 
   */
  public static AltRegistry getDefaultAltRegistry() {
    return MEDIA_REGISTRY;
  }
  
  /**
   * The DEFAULT_REGISTRY contains the default set of representations and
   * associated parser/generator configurations for media services.  It will be
   * used as the default configuration for all MediaService instances unless
   * {@link #setAltRegistry(AltRegistry)} is called.
   */
  private static final AltRegistry MEDIA_REGISTRY;
  
  static {
    // Start with the contents of the base default registry
    MEDIA_REGISTRY = new AltRegistry(Service.getDefaultAltRegistry());
    
    // Register media formats
    MEDIA_REGISTRY.register(AltFormat.MEDIA, 
        new MediaParser(), new MediaGenerator());
    MEDIA_REGISTRY.register(AltFormat.MEDIA_MULTIPART, 
        new MediaMultipartParser(), new MediaMultipartGenerator()); 
    
    // protect against subsequent changes
    MEDIA_REGISTRY.lock();
  }
  
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
    setAltRegistry(MEDIA_REGISTRY);
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
    setAltRegistry(MEDIA_REGISTRY);
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
    setAltRegistry(MEDIA_REGISTRY);
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

    MediaStreamSource mediaSource;
    try {
      startVersionScope();
      GDataRequest request =
          createRequest(GDataRequest.RequestType.QUERY,
              mediaUrl, contentType);
      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      InputStream resultStream = request.getResponseStream();

      mediaSource = new MediaStreamSource(resultStream, 
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
    } finally {
      endVersionScope();
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
  public MediaSource getMedia(IMediaContent mediaContent,
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
  public MediaSource getMedia(IMediaContent mediaContent)
      throws IOException, ServiceException {
    return getMedia(mediaContent, null);
  }

  /**
   * Initializes the attributes of a media request.
   */
  private void initMediaRequest(GDataRequest request, String title) {
    if (title != null) {
      request.setHeader("Slug", escapeSlug(title));
    }
    if (chunkedBufferSize != NO_CHUNKED_MEDIA_REQUEST
        && request instanceof HttpGDataRequest) {
      HttpGDataRequest httpRequest = (HttpGDataRequest) request;
      httpRequest.getConnection().setChunkedStreamingMode(chunkedBufferSize);
    }
  }

  /**
   * Initializes the attributes of a media request.
   */
  private void initMediaRequest(GDataRequest request, MediaSource media) {
    initMediaRequest(request, media.getName());
  }

  /**
   * An escaper for slug header values.  From the atom spec, the range
   * %20-24 and %26-7E are unescaped.  The {@link PercentEscaper} always
   * includes [0-9a-zA-Z] as safe characters, so we add the rest of the
   * unescaped characters: " !\"#$&'()*+,-./:;<=>?@[\\]^_`{|}~"
   */
  private static final PercentEscaper SLUG_ESCAPER =
      new PercentEscaper(" !\"#$&'()*+,-./:;<=>?@[\\]^_`{|}~", false);

  /**
   * Escape the slug header by escaping anything outside the range %20-24,
   * %26-7E using percent encoding.
   */
  static String escapeSlug(String slug) {
    return SLUG_ESCAPER.escape(slug);
  }

  /**
   * Inserts a new {@link com.google.gdata.data.Entry} into a feed associated
   * with the target service.  It will return the inserted Entry, including
   * any additional attributes or extensions set by the GData server.
   *
   * If the Entry has been associated with a {@link MediaSource} through the
   * {@link IMediaEntry#setMediaSource(MediaSource)} method then both the entry
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
   * @see com.google.gdata.data.IFeed#getEntryPostLink()
   */
  @Override
  @SuppressWarnings({"unchecked"})
  public <E extends IEntry> E insert(URL feedUrl, E entry)
      throws IOException, ServiceException {

    Preconditions.checkNotNull(entry, "entry");

    // Delegate non-media handling to base class
    MediaSource media = (entry instanceof IMediaEntry) ?
        ((IMediaEntry) entry).getMediaSource() : null;
    if (media == null) {
      return super.insert(feedUrl, entry);
    }

    GDataRequest request = null;
    try {
      startVersionScope();

      // Write as MIME multipart containing the entry and media.  Use the
      // content type from the multipart since this contains auto-generated
      // boundary attributes.
      MediaMultipart mediaMultipart = new MediaMultipart(entry, media);
      request =
          createRequest(GDataRequest.RequestType.INSERT, feedUrl,
              new ContentType(mediaMultipart.getContentType()));

      initMediaRequest(request, media);

      writeRequestData(request,
          new ClientOutputProperties(request, entry), mediaMultipart);
      request.execute();
      return parseResponseData(request, classOf(entry));

    } catch (MessagingException e) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantWriteMimeMultipart, e);
    } finally {
      endVersionScope();
      if (request != null) {
        request.end();
      }
    }
  }

  /**
   * Inserts a new media resource read from {@link MediaSource} into a
   * media feed associated with the target service.  It will return the
   * resulting entry that describes the inserted media, including
   * any additional attributes or extensions set by the GData server.
   * To insert both the entry and the media content in a single request, use
   * {@link #insert(URL, IEntry)}.
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
   * @see com.google.gdata.data.IFeed#getEntryPostLink()
   * @see com.google.gdata.data.media.MediaFeed#insert(MediaSource)
   */
  @SuppressWarnings({"unchecked"})
  public <E extends IEntry> E insert(URL feedUrl, Class<E> entryClass,
                                     MediaSource media)
      throws IOException, ServiceException {

    Preconditions.checkNotNull(media, "media");

    // Write media content only.
    GDataRequest request = 
      createRequest(GDataRequest.RequestType.INSERT, feedUrl,
          new ContentType(media.getContentType()));
    try {
      startVersionScope();

      initMediaRequest(request, media);
      writeRequestData(request, media);
      request.execute();
      return parseResponseData(request, entryClass);

    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Updates an existing entry metadata by writing it to the specified edit
   * URL. The resulting entry (after update) will be returned.
   * If the entry has media resource, the media part will not be updated.
   * To update both metadata and media, use {@link #updateMedia(URL, IEntry)}.
   * To update media only, use {@link #updateMedia(URL, Class, MediaSource)}.
   *
   * @param url the media edit URL associated with the resource.
   * @param entry the updated entry to be written to the server.
   * @return the updated entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the updated
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see IEntry#getMediaEditLink()
   */
  @Override
  public <E extends IEntry> E update(URL url, E entry)
      throws IOException, ServiceException {
    return super.update(url, entry);
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
   * @see IEntry#getMediaEditLink()
   */
  @SuppressWarnings({"unchecked"})
  public <E extends IEntry> E updateMedia(URL mediaUrl, E entry)
      throws IOException, ServiceException {

    Preconditions.checkNotNull(entry, "entry");

    // Since the input parameter is a media-edit URL, this method should
    // not be used to post Atom-only entries.  These entries should be
    // sent to the edit URL.
    MediaSource media = (entry instanceof IMediaEntry) ?
        ((IMediaEntry) entry).getMediaSource() : null;
    if (media == null) {
      throw new IllegalArgumentException(
          "Must supply media entry with a media source");
    }

    GDataRequest request = null;
    try {
      startVersionScope();

      // Write as MIME multipart containing the entry and media.  Use the
      // content type from the multipart since this contains auto-generated
      // boundary attributes.
      MediaMultipart mediaMultipart = new MediaMultipart(entry, media);
      request =  createRequest(GDataRequest.RequestType.UPDATE, mediaUrl,
          new ContentType(mediaMultipart.getContentType()));

      writeRequestData(request,
          new ClientOutputProperties(request, entry), mediaMultipart);
      request.execute();
      return parseResponseData(request, classOf(entry));

    } catch (MessagingException e) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantWriteMimeMultipart, e);
    } finally {
      endVersionScope();
      if (request != null) {
        request.end();
      }
    }
  }

  /**
   * Updates an existing media resource with data read from the
   * {@link MediaSource} by writing it it to the specified media edit URL.
   * The resulting entry (after update) will be returned.  To update both
   * the entry and the media content in a single request, use
   * {@link #updateMedia(URL, IEntry)}.
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
   * @see IEntry#getMediaEditLink()
   */
  @SuppressWarnings({"unchecked"})
  public <E extends IEntry> E updateMedia(URL mediaUrl,
                                          Class<E> entryClass,
                                          MediaSource media)
      throws IOException, ServiceException {

    // Since the input parameter is a media-edit URL, this method should
    // not be used to post Atom-only entries.  These entries should be
    // sent to the edit URL.
    Preconditions.checkNotNull(media, "media");

    ContentType mediaContentType = new ContentType(media.getContentType());
    GDataRequest request =
        createRequest(GDataRequest.RequestType.UPDATE, mediaUrl,
            mediaContentType);
    try {
      startVersionScope();

      writeRequestData(request, media);
      request.execute();
      return parseResponseData(request, entryClass);

    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Initialize a resumable media upload request.
   *
   * @param request {@link GDataRequest} to initialize.
   * @param file    media file that needs to be upload.
   * @param title   title of uploaded media or {@code null} if no title.
   */
  private void initResumableMediaRequest(
      GDataRequest request, MediaFileSource file, String title) {
    initMediaRequest(request, title);
    request.setHeader(
        GDataProtocol.Header.X_UPLOAD_CONTENT_TYPE, file.getContentType());
    request.setHeader(GDataProtocol.Header.X_UPLOAD_CONTENT_LENGTH,
        new Long(file.getContentLength()).toString());
  }

  /**
   * Creates a resumable upload session for a new media.
   *
   * @param createMediaUrl resumable put/post url.
   * @param title media title for new upload or {@code null} for updating
   *              media part of existing media resource.
   * @param file  new media file to upload.
   * @return resumable upload url to upload the media to.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException insert request failed due to system error.
   */
  URL createResumableUploadSession(
      URL createMediaUrl, String title, MediaFileSource file)
      throws IOException, ServiceException {

    String mimeType = file.getContentType();
    GDataRequest request = createRequest(GDataRequest.RequestType.INSERT,
        createMediaUrl, new ContentType(mimeType));
    initResumableMediaRequest(request, file, title);
    try {
      startVersionScope();
      request.execute();
      return new URL(request.getResponseHeader("Location"));
    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Creates a resumable upload session for a new media with specified metadata.
   *
   * @param createMediaUrl resumable put/post url.
   * @param entry metadata for new media.
   * @param file new media file to upload.
   * @return resumable upload url to upload the media to.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException insert request failed due to system error.
   */
  URL createResumableUploadSession(
      URL createMediaUrl, IEntry entry, MediaFileSource file)
      throws IOException, ServiceException {

    GDataRequest request = createInsertRequest(createMediaUrl);
    initResumableMediaRequest(request, file, file.getName());
    try {
      startVersionScope();

      writeRequestData(request, entry);
      request.execute();
      return new URL(request.getResponseHeader("Location"));
    } finally {
      endVersionScope();
      request.end();
    }

  }

  /**
   * Creates a resumable upload session to update existing media.
   *
   * @param editMediaUrl resumable put/post url.
   * @param entry media entry to update.
   * @param file updated media file to upload.
   * @param isMediaOnly whether to update media only or both media and metadata.
   *                    {@code true} if media-only or {@code false} for both.
   * @return resumable upload url to upload the media to.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException insert request failed due to system error.
   */
  URL createResumableUpdateSession(
      URL editMediaUrl, IEntry entry, MediaFileSource file, boolean isMediaOnly)
      throws IOException, ServiceException {

    GDataRequest request;
    if (isMediaOnly) {
      request = createRequest(GDataRequest.RequestType.UPDATE, editMediaUrl,
          new ContentType(file.getContentType()));
    } else {
      request = createUpdateRequest(editMediaUrl);
    }
    initResumableMediaRequest(request, file, null);
    if (entry.getEtag() != null) {
      request.setEtag(entry.getEtag());
    }
    try {
      startVersionScope();
      if (!isMediaOnly) {
        writeRequestData(request, entry);
      }
      request.execute();
      return new URL(request.getResponseHeader("Location"));
    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Parses Resumable Upload response from RUPIO response stream.
   *
   * @param source response stream to parse.
   * @param responseType response stream content type.
   * @param resultType expected result type, not {@code null}.
   * @return an instance of the expected result type resulting from the parse.
   * @throws IOException read/write error
   * @throws ServiceException server error
   */
  <E> E parseResumableUploadResponse(InputStream source,
      ContentType responseType, Class<E> resultType)
      throws IOException, ServiceException {
    try {
      startVersionScope();
      return parseResponseData(
          new ParseSource(source), responseType, resultType);
    } finally {
      endVersionScope();
    }
  }

}
