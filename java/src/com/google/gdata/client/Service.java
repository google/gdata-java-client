/* Copyright (c) 2006 Google Inc.
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


package com.google.gdata.client;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.batch.BatchInterruptedException;
import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Feed;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.batch.BatchInterrupted;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.introspection.ServiceDocument;
import com.google.gdata.data.media.MediaMultipart;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.ServiceForbiddenException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

/**
 * The Service class represents a client connection to a GData service.
 * It encapsulates all protocol-level interactions with the GData server
 * and acts as a helper class for higher level entities (feeds, entries,
 * etc) that invoke operations on the server and process their results.
 * <p>
 * This class provides the base level common functionality required to
 * access any GData service.  It is also designed to act as a base class
 * that can be customized for specific types of GData services.  Examples
 * of supported customizations include:
 * <ul>
 * <li><b>Authentication</b> - implementating a custom authentication
 * mechanism for services that require authentication and use something
 * other than HTTP basic or digest authentication.
 * <li><b>Extensions</b> - define expected ExtensionPoints and Extensions
 * with the {@link ExtensionProfile} associated with the service to allow
 * Atom/RSS extension elements to be automatically converted to/from the
 * {@link Feed}/{@link com.google.gdata.data.Entry} object model.
 * </ul>
 *
 * 
 */
public class Service {


  private static final String SERVICE_VERSION =
    "GData-Java/" + Service.class.getPackage().getImplementationVersion();

  /**
   * The GDataRequest interface represents a streaming connection to a
   * GData service that can be used either to send request data to the
   * service using an OutputStream or to receive response data from the
   * service as an InputStream.   The calling client then has full control
   * of the request data generation and response data parsing.  This can
   * be used to integrate GData services with an external Atom or RSS
   * parsing library, such as Rome.
   * <p>
   * A GDataRequest instance will be returned by the streaming client
   * APIs of the Service class.  The basic usage pattern is:
   * <p>
   * <pre>
   * GDataRequest request = ...     // createXXXRequest API call
   * try {
   *    OutputStream requestStream = request.getRequestStream();
   *    // stream request data, if any
   *
   *    request.execute()                // execute the request
   *
   *    InputStream responseStream = request.getResponseStream();
   *    // process the response data, if any
   * }
   * catch (IOException ioe) {
   *    // handle errors writing to / reading from server
   * } catch (ServiceException se) {
   *    // handle service invocation errors
   * }
   * </pre>
   *
   * @see Service#createEntryRequest(URL)
   * @see Service#createFeedRequest(URL)
   * @see Service#createInsertRequest(URL)
   * @see Service#createUpdateRequest(URL)
   * @see Service#createDeleteRequest(URL)
   */
  public interface GDataRequest {


    /**
     * The RequestType enumeration defines the set of expected GData
     * request types.  These correspond to the four operations of the
     * GData protocol:
     * <ul>
     * <li><b>QUERY</b> - query a feed, entry, or description document.</li>
     * <li><b>INSERT</b> - insert a new entry into a feed.</li>
     * <li><b>UPDATE</b> - update an existing entry within a feed.</li>
     * <li><b>DELETE</b> - delete an existing entry within a feed.</li>
     * <li><b>BATCH</b> - execute several insert/update/delete operations</li>
     * </ul>
     */
    public enum RequestType {
      QUERY, INSERT, UPDATE, DELETE, BATCH
    }


    /**
     * Sets the number of milliseconds to wait for a connection to the
     * remote GData service before timing out.
     *
     * @param timeout the read timeout.  A value of zero indicates an
     *        infinite timeout.
     * @throws IllegalArgumentException if the timeout value is negative.
     *
     * @see java.net.URLConnection#setConnectTimeout(int)
     */
    public void setConnectTimeout(int timeout);


    /**
     * Sets the number of milliseconds to wait for a response from the
     * remote GData service before timing out.
     *
     * @param timeout the read timeout.  A value of zero indicates an
     *        infinite timeout.
      @throws IllegalArgumentException if the timeout value is negative.
     *
     * @see java.net.URLConnection#setReadTimeout(int)
     */
    public void setReadTimeout(int timeout);

    /**
     * Sets the If-Modified-Since date precondition to be applied to the
     * request.  If this precondition is set, then the request will be
     * performed only if the target resource has been modified since the
     * specified date; otherwise, a {@code NotModifiedException} will be
     * thrown.   The default value is {@code null}, indicating no
     * precondition.
     *
     * @param conditionDate the date that should be used to limit the
     *          operation on the target resource.  The operation will only
     *          be performed if the resource has been modified later than
     *          the specified date.
     */
    public void setIfModifiedSince(DateTime conditionDate);

    /**
     * Sets a request header (and logs it, if logging is enabled)
     *
     * @param name the header name
     * @param value the header value
     */
    public void setHeader(String name, String value);

    /**
     * Sets request header (and log just the name but not the value, if
     * logging is enabled)
     *
     * @param name the header name
     * @param value the header value
     */
    public void setPrivateHeader(String name, String value);

    /**
     * Returns a stream that can be used to write request data to the
     * GData service.
     *
     * @return OutputStream that can be used to write GData request data.
     * @throws IOException error obtaining the request output stream.
     */
    public OutputStream getRequestStream() throws IOException;

    /**
     * Executes the GData service request.
     *
     * @throws IOException error writing to or reading from GData service.
     * @throws ResourceNotFoundException invalid request target resource.
     * @throws ServiceException system error executing request.
     */
    public void execute() throws IOException, ServiceException;

    /**
     * Returns the content type of the GData response.
     * <p>
     *
     * @return ContentType the GData response content type or {@code null}
     *                               if no response content.
     * @throws IllegalStateException attempt to read content type without
     *                               first calling {@link #execute()}.
     * @throws IOException error obtaining the response content type.
     */
    public ContentType getResponseContentType() throws IOException;

    /**
     * Returns a stream that can be used to read response data from the
     * GData service.
     * <p>
     * <b>The caller is responsible for ensuring that the response stream is
     * properly closed after the response has been read.</b>
     *
     * @return InputStream providing access to GData response data.
     * @throws IllegalStateException attempt to read response without
     *                               first calling {@link #execute()}.
     * @throws IOException error obtaining the response input stream.
     */
    public InputStream getResponseStream() throws IOException;
  }


  /**
   * The GDataRequestFactory interface defines a basic factory interface
   * for constructing a new GDataRequest interface.
   */
  public interface GDataRequestFactory {

    /**
     * Set a header that will be included in all requests. If header of
     * the same name was previously set, then replace the previous header
     * value.
     *
     * @param header the name of the header
     * @param value the value of the header, if null, then unset that header.
     */
    public void setHeader(String header, String value);

    /**
     * Set a header that will be included in all requests and do
     * not log the value.  Useful for values that are sensitive or
     * related to security. If header of the same name was previously set,
     * then replace the previous header value.
     *
     * @param header the name of the header
     * @param value the value of the header.  If null, then unset that header.
     */
    public void setPrivateHeader(String header, String value);
    /**
     * Creates a new GDataRequest instance of the specified RequestType.
     */
    public GDataRequest getRequest(GDataRequest.RequestType type,
                                   URL requestUrl,
                                   ContentType contentType)
      throws IOException, ServiceException;
  }


  /**
   * Constructs a new Service instance that is configured to accept arbitrary
   * extension data within feed or entry elements.
   */
  public Service() {

    // Set the default User-Agent value for requests
    requestFactory.setHeader("User-Agent", getServiceVersion());

    // The default extension profile is configured to accept arbitrary XML
    // at the feed or entry level.   A client never wants to lose any
    // foreign markup, so capture everything even if not explicitly
    // understood.
    new Feed().declareExtensions(extProfile);
  }


  /**
   * Returns information about the service version.
   */
  public String getServiceVersion() {  return SERVICE_VERSION; }



  protected ExtensionProfile extProfile = new ExtensionProfile();

  /**
   * Returns the {@link ExtensionProfile} that defines any expected extensions
   * to the base RSS/Atom content model.
   */
  public ExtensionProfile getExtensionProfile() {
    return extProfile;
  }

  /**
   * Sets the {@link ExtensionProfile} that defines any expected extensions
   * to the base RSS/Atom content model.
   */
  public void setExtensionProfile(ExtensionProfile extProfile) {
    this.extProfile = extProfile;
  }

  /**
   * The GDataRequestFactory associated with this service.  The default is
   * the base HttpGDataRequest Factory class.
   */
  protected GDataRequestFactory requestFactory = new HttpGDataRequest.Factory();


  /**
   * Returns the GDataRequestFactory currently associated with the service.
   */
  public GDataRequestFactory getRequestFactory() {
    return requestFactory;
  }


  /**
   * Sets the GDataRequestFactory currently associated with the service.
   */
  public void setRequestFactory(GDataRequestFactory requestFactory) {
    this.requestFactory = requestFactory;
  }


  /**
   * Creates a new GDataRequest for use by the service.
   */
  public GDataRequest createRequest(GDataRequest.RequestType type,
                                       URL requestUrl,
                                       ContentType contentType)
      throws IOException, ServiceException {

    GDataRequest request =
      requestFactory.getRequest(type, requestUrl, contentType);
    if (connectTimeout >= 0) {
      request.setConnectTimeout(connectTimeout);
    }
    if (readTimeout >= 0) {
      request.setReadTimeout(readTimeout);
    }
    return request;
  }


  /**
   * Content type of data posted to the GData service.
   * Defaults to Atom using UTF-8 character set.
   */
  private ContentType contentType = ContentType.ATOM;

  /**
   * Returns the default ContentType for data associated with this GData
   * service.
   */
  public ContentType getContentType() { return contentType; }


  /**
   * Sets the default ContentType for writing data to the GData service.
   */
  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }


  /**
   * Client-configured connection timeout value.  A value of -1 indicates
   * the client has not set any timeout.
   */
  protected int connectTimeout = -1;


  /**
   * Sets the default wait timeout (in milliseconds) for a connection to the
   * remote GData service.
   *
   * @param timeout the read timeout.  A value of zero indicates an
   *        infinite timeout.
   * @throws IllegalArgumentException if the timeout value is negative.
   *
   * @see java.net.URLConnection#setConnectTimeout(int)
   */
  public void setConnectTimeout(int timeout) {
    if (timeout < 0) {
      throw new IllegalArgumentException("Timeout value cannot be negative");
    }
    connectTimeout = timeout;
  }


  /**
   * Client configured read timeout value.  A value of -1 indicates
   * the client has not set any timeout.
   */
  int readTimeout = -1;


  /**
   * Sets the default wait timeout (in milliseconds) for a response from the
   * remote GData service.
   *
   * @param timeout the read timeout.  A value of zero indicates an
   *        infinite timeout.
    @throws IllegalArgumentException if the timeout value is negative.
   *
   * @see java.net.URLConnection#setReadTimeout(int)
   */
  public void setReadTimeout(int timeout) {
    if (timeout < 0) {
      throw new IllegalArgumentException("Timeout value cannot be negative");
    }
    readTimeout = timeout;
  }

  /**
   * Parse an entry of the specified class from an input stream.
   */
  private <E extends BaseEntry> E parseEntry(Class<E> entryClass,
                                             InputStream entryStream)
      throws IOException, ServiceException {

    E entry;
    try {
      entry = entryClass.newInstance();
    } catch (InstantiationException e) {
      throw new ServiceException("Unable to create entry instance", e);
    } catch (IllegalAccessException e) {
      throw new ServiceException("Unable to create entry instance", e);
    }
    entry.setService(this);
    entry.parseAtom(extProfile, entryStream);
    return entry;
  }

  /**
   * Returns the Atom introspection Service Document associated with a
   * particular feed URL.  This document provides service metadata about
   * the set of Atom services associatd with the target feed URL.
   *
   * @param feedUrl the URL associated with a feed.   This URL can not include
   *        any query parameters.
   * @param serviceClass the class used to represent a service document.
   *
   * @return ServiceDocument resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws ParseException error parsing the returned service data.
   * @throws ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving service document.
   */
  public <S extends ServiceDocument> S introspect(URL feedUrl,
                                                  Class<S> serviceClass)
      throws IOException, ServiceException {

    String feedQuery = feedUrl.getQuery();
    if (feedQuery == null || feedQuery.indexOf("alt=atom-service") == -1) {
      char appendChar = (feedQuery == null) ? '?' : '&';
      feedUrl = new URL(feedUrl.toString() + appendChar + "alt=atom-service");
    }

    InputStream responseStream = null;
    GDataRequest request = createFeedRequest(feedUrl);
    try {
      request.execute();
      responseStream = request.getResponseStream();

      S serviceDoc = serviceClass.newInstance();
      serviceDoc.parse(extProfile, responseStream);

      return serviceDoc ;

    } catch (InstantiationException e) {
      throw new ServiceException("Unable to create service document instance",
                                 e);
    } catch (IllegalAccessException e) {
      throw new ServiceException("Unable to create service document instance",
                                 e);
    }
    finally {
      if (responseStream != null) {
        responseStream.close();
      }
    }
  }


  /**
   * Returns the Feed associated with a particular feed URL, if it's
   * been modified since the specified date.
   *
   * @param feedUrl the URL associated with a feed.   This URL can include
   *                GData query parameters.
   * @param feedClass the class used to represent a service Feed.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *          feed should be returned only if it has been modified after the
   *          specified date. A value of {@code null} indicates no precondition.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws NotModifiedException if the feed resource has not been modified
   *          since the specified precondition date.
   * @throws ParseException error parsing the returned feed data.
   * @throws ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  public <F extends BaseFeed> F getFeed(URL feedUrl,
                                        Class<F> feedClass,
                                        DateTime ifModifiedSince)
      throws IOException, ServiceException {

    InputStream feedStream = null;
    GDataRequest request = createFeedRequest(feedUrl);
    try {
      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      feedStream = request.getResponseStream();

      BaseFeed<?,?> feed;
      try {
        feed = feedClass.newInstance();
        feed.setService(this);
      } catch (InstantiationException e) {
        throw new ServiceException("Unable to create Feed instance", e);
      } catch (IllegalAccessException e) {
        throw new ServiceException("Unable to create Feed instance", e);
      }
      feed.parseAtom(extProfile, feedStream);

      return (F) feed;
    }
    finally {
      if (feedStream != null) {
        feedStream.close();
      }
    }
  }


  /**
   * Returns the Feed associated with a particular feed URL.
   *
   * @param feedUrl the URL associated with a feed.   This URL can include
   *                GData query parameters.
   * @param feedClass the class used to represent a service Feed.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws ParseException error parsing the returned feed data.
   * @throws ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  public <F extends BaseFeed> F getFeed(URL feedUrl, Class<F> feedClass)
      throws IOException, ServiceException {
    return getFeed(feedUrl, feedClass, null);
  }


  /**
   * Executes a GData query against the target service and returns the
   * resulting feed results via an input stream.
   *
   * @param queryUrl URL that defines target feed and any query parameters.
   * @return GData request instance that can be used to read the feed data.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of query feed request failed.
   *
   * @see Query#getUrl()
   */
  public GDataRequest createFeedRequest(URL queryUrl)
      throws IOException, ServiceException {
    return createRequest(GDataRequest.RequestType.QUERY, queryUrl, contentType);
  }


  /**
   * Returns an Atom entry instance, given the URL of the entry and an
   * if-modified-since date.
   *
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *          entry should be returned only if it has been modified after the
   *          specified date. A value of {@code null} indicates no precondition.
   * @return the entry referenced by the URL parameter.
   * @throws IOException error communicating with the GData service.
   * @throws NotModifiedException if the entry resource has not been modified
   *          after the specified precondition date.
   * @throws ParseException error parsing the returned entry.
   * @throws ResourceNotFoundException if the entry URL is not valid.
   * @throws ServiceForbiddenException if the GData service cannot
   *          get the entry resource due to access constraints.
   * @throws ServiceException if a system error occurred when retrieving
   *          the entry.
   */
  public <E extends BaseEntry> E getEntry(URL entryUrl,
                                          Class<E> entryClass,
                                          DateTime ifModifiedSince)
      throws IOException, ServiceException {

    InputStream entryStream = null;
    GDataRequest request = createEntryRequest(entryUrl);
    try {

      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      entryStream = request.getResponseStream();
      return parseEntry(entryClass, entryStream);

    } finally {
      if (entryStream != null) {
        entryStream.close();
      }
    }
  }


  /**
   * Returns an Atom entry instance, given the URL of the entry.
   *
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries.
   * @return the entry referenced by the URL parameter.
   * @throws IOException error communicating with the GData service.
   * @throws ParseException error parsing the returned entry.
   * @throws ResourceNotFoundException if the entry URL is not valid.
   * @throws ServiceForbiddenException if the GData service cannot
   *          get the entry resource due to access constraints.
   * @throws ServiceException if a system error occurred when retrieving
   *          the entry.
   */
  public <E extends BaseEntry> E getEntry(URL entryUrl, Class<E> entryClass)
      throws IOException, ServiceException {
    return getEntry(entryUrl, entryClass, null);
  }


  /**
   * Returns a GDataRequest instance that can be used to access an
   * entry's contents as a stream, given the URL of the entry.
   *
   * @param entryUrl resource URL for the entry.
   * @return GData request instance that can be used to read the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException entry request creation failed.
   */
  public GDataRequest createEntryRequest(URL entryUrl)
      throws IOException, ServiceException {
    return createRequest(GDataRequest.RequestType.QUERY, entryUrl, contentType);
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

    try {
      GDataRequest request =
          createRequest(GDataRequest.RequestType.QUERY,
              new URL(mediaContent.getUri()),
              mediaContent.getMimeType());
      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      InputStream resultStream = request.getResponseStream();
      MediaSource mediaSource =
          new MediaStreamSource(resultStream,
              request.getResponseContentType().toString());
      return mediaSource;
    } catch (MalformedURLException mue) {
      throw new ServiceException("Invalid media source URI", mue);
    }
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
   * Executes a GData query against the target service and returns the
   * {@link Feed} containing entries that match the query result, if
   * it's been modified since the specified date.
   *
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *          query result feed should be returned only if contains entries
   *          that have been modified after the specified date.  A value of
   *          {@code null} indicates no precondition.
   * @throws IOException error communicating with the GData service.
   * @throws NotModifiedException if the query resource does not contain
   *          entries modified since the specified precondition date.
   * @throws ServiceForbiddenException feed does not support the query.
   * @throws ParseException error parsing the returned feed data.
   * @throws ServiceException query request failed.
   */
  public <F extends BaseFeed> F query(Query query,
                                      Class<F> feedClass,
                                      DateTime ifModifiedSince)
      throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return getFeed(query.getUrl(), feedClass, ifModifiedSince);
  }


  /**
   * Executes a GData query against the target service and returns the
   * {@link Feed} containing entries that match the query result.
   *
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceForbiddenException feed does not support the query.
   * @throws ParseException error parsing the returned feed data.
   * @throws ServiceException query request failed.
   */
  public <F extends BaseFeed> F query(Query query, Class<F> feedClass)
      throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return query(query, feedClass, null);
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
  }

  /**
   * Inserts a new {@link com.google.gdata.data.Entry} into a feed associated
   * with the target service.  It will return the inserted Entry, including
   * any additional attributes or extensions set by the GData server.
   *
   * If the Entry has been associated with a {@link MediaSource} through the
   * {@link BaseEntry#setMediaSource(MediaSource)} method then both the entry
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
   * @throws ParseException error parsing the return entry data.
   * @throws ServiceException insert request failed due to system error.
   *
   * @see BaseFeed#getEntryPostLink()
   * @see BaseFeed#insert(BaseEntry)
   */
  public <E extends BaseEntry> E insert(URL feedUrl, E entry)
      throws IOException, ServiceException {

    if (entry == null) {
      throw new NullPointerException("Must supply entry");
    }

    InputStream resultStream = null;
    try {
      GDataRequest request;
      MediaSource media = entry.getMediaSource();
      if (media == null) {
        request = createInsertRequest(feedUrl);
        OutputStream entryStream = request.getRequestStream();
        Writer entryWriter = new OutputStreamWriter(entryStream, "utf-8");
        XmlWriter xw = new XmlWriter(entryWriter);
        entry.generateAtom(xw, extProfile);
        entryWriter.flush();
      } else {
        // Write as MIME multipart containing the entry and media
        MediaMultipart mediaMultipart = new MediaMultipart(entry, media);
        request =
            createRequest(GDataRequest.RequestType.INSERT, feedUrl,
                new ContentType(mediaMultipart.getContentType()));
        initMediaRequest(media, request);
        OutputStream outputStream = request.getRequestStream();
        mediaMultipart.writeTo(outputStream);
      }

      request.execute();

      resultStream = request.getResponseStream();
      return (E)parseEntry(entry.getClass(), resultStream);

    } catch (MessagingException e) {
        throw new ServiceException("Unable to write MIME multipart message", e);
    } finally {
      if (resultStream != null) {
        resultStream.close();
      }
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
   * @throws ParseException error parsing the returned entry data.
   * @throws ServiceException insert request failed due to system error.
   *
   * @see BaseFeed#getEntryPostLink()
   * @see BaseFeed#insert(MediaSource)
   */
  public <E extends BaseEntry> E insert(URL feedUrl, Class<E> entryClass,
                                        MediaSource media)
      throws IOException, ServiceException {

    if (media == null) {
      throw new NullPointerException("Must supply media source");
    }

    InputStream resultStream = null;
    GDataRequest request;
    try {
      // Write media content only.
      request =
          createRequest(GDataRequest.RequestType.INSERT, feedUrl,
              new ContentType(media.getContentType()));
      initMediaRequest(media, request);

      // Write the media data
      MediaSource.Output.writeTo(media, request.getRequestStream());
      request.execute();
      resultStream = request.getResponseStream();
      return (E)parseEntry(entryClass, resultStream);

    } finally {
      if (resultStream != null) {
        resultStream.close();
      }
    }
  }

  /**
   * Executes several operations (insert, update or delete) on the entries
   * that are part of the input {@link Feed}. It will return another feed that
   * describes what was done while executing these operations.
   *
   * It is possible for one batch operation to fail even though other
   * operations have worked, so this method won't throw a ServiceException
   * unless something really wrong is going on. You need to check the
   * entries in the returned feed to know which operations succeeded
   * and which operations failed (see {@link BatchStatus}
   * and {@link com.google.gdata.data.batch.BatchInterrupted} extensions.)
   *
   * @param feedUrl the POST URI associated with the target feed.
   * @param inputFeed a description of the operations to execute, described
   *   using tags in the batch: namespace
   * @return a feed with the result of each operation in a separate
   *   entry
   * @throws IOException error communicating with the GData service.
   * @throws ParseException error parsing the return entry data.
   * @throws ServiceException insert request failed due to system error.
   * @throws BatchInterruptedException if something really wrong was detected
   *   by the server while parsing the request, like invalid XML data. Some
   *   operations might have succeeded when this exception is thrown. Check
   *   {@link BatchInterruptedException#getFeed()}.
   *
   * @see BaseFeed#getEntryPostLink()
   * @see BaseFeed#insert(BaseEntry)
   */
  public <F extends BaseFeed> F batch(URL feedUrl, F inputFeed)
      throws IOException, ServiceException, BatchInterruptedException {
    InputStream resultStream = null;
    GDataRequest request = createInsertRequest(feedUrl);
    try {
      OutputStream entryStream = request.getRequestStream();
      Writer entryWriter = new OutputStreamWriter(entryStream, "utf-8");
      XmlWriter xw = new XmlWriter(entryWriter);
      inputFeed.generateAtom(xw, extProfile);
      entryWriter.flush();

      request.execute();

      resultStream = request.getResponseStream();
      F resultFeed;
      try {
        // Cast should be type safe, since inserted entry will match original
        resultFeed = (F) inputFeed.getClass().newInstance();
      } catch (InstantiationException e) {
        throw new ServiceException("Unable to create feed instance", e);
      } catch (IllegalAccessException e) {
        throw new ServiceException("Unable to create feed instance", e);
      }
      resultFeed.setService(this);
      resultFeed.parseAtom(extProfile, resultStream);

      // Detect BatchInterrupted
      int count = resultFeed.getEntries().size();
      if (count > 0) {
        BaseEntry entry = (BaseEntry)resultFeed.getEntries().get(count - 1);
        BatchInterrupted interrupted = BatchUtils.getBatchInterrupted(entry);
        if (interrupted != null) {
          throw new BatchInterruptedException(resultFeed, interrupted);
        }
      }

      return resultFeed;

    } finally {
      if (resultStream != null) {
        resultStream.close();
      }
    }
  }

  /**
   * Creates a new GDataRequest that can be used to insert a new entry into
   * a feed using the request stream and to read the resulting entry
   * content from the response stream.
   *
   * @param feedUrl the POST URI associated with the target feed.
   * @return GDataRequest to interact with remote GData service.
   * @throws IOException error reading from or writing to the GData service.
   * @throws ServiceException insert request failed.
   */
  public GDataRequest createInsertRequest(URL feedUrl)
      throws IOException, ServiceException {
    return createRequest(GDataRequest.RequestType.INSERT, feedUrl, contentType);
  }

  /**
   * Creates a new GDataRequest that can be used to execute several
   * insert/update/delete operations in one request by writing a
   * feed into the request stream to read a feed containing the
   * result of the batch operations from the response stream.
   *
   * @param feedUrl the POST URI associated with the target feed.
   * @return GDataRequest to interact with remote GData service.
   * @throws IOException error reading from or writing to the GData service.
   * @throws ServiceException insert request failed.
   */
  public GDataRequest createBatchRequest(URL feedUrl)
      throws IOException, ServiceException {
    return createRequest(GDataRequest.RequestType.BATCH, feedUrl, contentType);
  }


  /**
   * Updates an existing {@link com.google.gdata.data.Entry} by writing
   * it to the specified entry edit URL.  The resulting Entry (after update)
   * will be returned.
   *
   * @param entryUrl the edit URL associated with the entry.
   * @param entry the modified Entry to be written to the server.
   * @return the updated Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws ParseException error parsing the updated entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see BaseEntry#getEditLink()
   * @see BaseEntry#update()
   */
  public <E extends BaseEntry> E update(URL entryUrl, E entry)
      throws IOException, ServiceException {

    InputStream resultStream = null;
    GDataRequest request = createUpdateRequest(entryUrl);
    try {
      // Send the entry
      OutputStream entryStream = request.getRequestStream();
      Writer entryWriter = new OutputStreamWriter(entryStream, "utf-8");
      XmlWriter xw = new XmlWriter(entryWriter);
      entry.generateAtom(xw, extProfile);
      entryWriter.flush();

      // Execute the request
      request.execute();

      // Handle the update
      resultStream = request.getResponseStream();
      return (E)parseEntry(entry.getClass(), resultStream);

    } finally {
      if (resultStream != null) {
        resultStream.close();
      }
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
   * @throws ParseException error parsing the updated entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see BaseEntry#getMediaEditLink()
   * @see BaseEntry#updateMedia(boolean)
   */
  public <E extends BaseEntry> E updateMedia(URL mediaUrl, E entry)
      throws IOException, ServiceException {

    if (entry == null) {
      throw new NullPointerException("Must supply entry");
    }

    // Since the input parameter is a media-edit URL, this method should
    // not be used to post Atom-only entries.  These entries should be
    // sent to the edit URL.
    MediaSource media = entry.getMediaSource();
    if (media == null) {
      throw new NullPointerException("Must supply media source");
    }

    InputStream resultStream = null;
    GDataRequest request;
    try {
      // Write as MIME multipart containing the entry and media
      MediaMultipart mediaMultipart = new MediaMultipart(entry, media);
      request =
          createRequest(GDataRequest.RequestType.UPDATE, mediaUrl,
              new ContentType(mediaMultipart.getContentType()));
      OutputStream outputStream = request.getRequestStream();
      mediaMultipart.writeTo(outputStream);
      request.execute();

      resultStream = request.getResponseStream();
      return (E)parseEntry(entry.getClass(), resultStream);

    } catch (MessagingException e) {
      throw new ServiceException("Unable to write MIME multipart message", e);
    } finally {
      if (resultStream != null) {
        resultStream.close();
      }
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
   * @throws ParseException error parsing the updated entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see BaseEntry#getMediaEditLink()
   * @see BaseEntry#updateMedia(boolean)
   */
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

    InputStream resultStream = null;
    GDataRequest request;
    try {
      request =
          createRequest(GDataRequest.RequestType.UPDATE, mediaUrl,
              new ContentType(media.getContentType()));
      MediaSource.Output.writeTo(media, request.getRequestStream());
      request.execute();

      resultStream = request.getResponseStream();
      return (E)parseEntry(entryClass, resultStream);

    } finally {
      if (resultStream != null) {
        resultStream.close();
      }
    }
  }

  /**
   * Creates a new GDataRequest that can be used to update an existing
   * Atom entry.   The updated entry content can be written to the
   * GDataRequest request stream and the resulting updated entry can
   * be obtained from the GDataRequest response stream.
   *
   * @param entryUrl the edit URL associated with the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of update request failed.
   */
  public GDataRequest createUpdateRequest(URL entryUrl)
      throws IOException, ServiceException {

    return createRequest(GDataRequest.RequestType.UPDATE, entryUrl, contentType);
  }


  /**
   * Deletes an existing entry (and associated media content, if any) using the
   * specified edit URL.
   *
   * @param resourceUrl the edit or medit edit url associated with the
   *        resource.
   * @throws IOException error communicating with the GData service.
   * @throws ResourceNotFoundException invalid entry URL.
   * @throws ServiceException delete request failed due to system error.
   */
  public void delete(URL resourceUrl) throws IOException, ServiceException {

    GDataRequest request = createDeleteRequest(resourceUrl);
    request.execute();
  }


  /**
   * Creates a new GDataRequest that can be used to delete an Atom
   * entry.  For delete requests, no input is expected from the request
   * stream nor will any response data be returned.
   *
   * @param entryUrl the edit URL associated with the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of delete request failed.
   */
  public GDataRequest createDeleteRequest(URL entryUrl)
      throws IOException, ServiceException {

    return createRequest(GDataRequest.RequestType.DELETE, entryUrl, contentType);
  }
}
