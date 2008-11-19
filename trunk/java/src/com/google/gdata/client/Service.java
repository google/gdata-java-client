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


package com.google.gdata.client;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.AuthTokenFactory.AuthToken;
import com.google.gdata.client.batch.BatchInterruptedException;
import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Feed;
import com.google.gdata.data.Link;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.batch.BatchInterrupted;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.introspection.ServiceDocument;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.PreconditionFailedException;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

/**
 * The Service class represents a client connection to a GData service. It
 * encapsulates all protocol-level interactions with the GData server and acts
 * as a helper class for higher level entities (feeds, entries, etc) that invoke
 * operations on the server and process their results.
 * <p>
 * This class provides the base level common functionality required to access
 * any GData service. It is also designed to act as a base class that can be
 * customized for specific types of GData services. Examples of supported
 * customizations include:
 * <ul>
 * <li><b>Authentication</b> - implementing a custom authentication
 * mechanism for services that require authentication and use something other
 * than HTTP basic or digest authentication.
 * <li><b>Extensions</b> - define expected ExtensionPoints and Extensions with
 * the {@link ExtensionProfile} associated with the service to allow Atom/RSS
 * extension elements to be automatically converted to/from the {@link Feed} /
 * {@link com.google.gdata.data.Entry} object model.
 * </ul>
 * 
 * 
 */
public class Service {


  private static final String SERVICE_VERSION =
      "GData-Java/" + Service.class.getPackage().getImplementationVersion()
          + "(gzip)"; // Necessary to get GZIP encoded responses

  /**
   * The Versions class defines {@link Version} constants representing the set
   * of active versions of the GData core protocol and common data model
   * classes.
   */
  public static class Versions {

    /**
     * The V1 version of the GData core protocol that was released in May 2006
     * and is in use for all current GData services.
     */
    public static final Version V1 = new Version(Service.class, 1, 0);
    /**
     * The upcoming V2 release of the GData core protocol that will bring full
     * alignment with the now standard Atom Publishing Protocol specification,
     * migration to OpenSearch 1.1, and other (TBD) features.
     */
    public static final Version V2 = new Version(Service.class, 2, 0);
    /**
     * The eventual future V3 release (not yet supported) of the GData
     * core protocol that will default to structured error messages.
     */
    public static final Version V3 = new Version(Service.class, 3, 0);
  }

  /**
   * Initializes the default client version for the GData core protocol.
   */
  @SuppressWarnings("unused")
  private static final Version CORE_VERSION =
      initServiceVersion(Service.class, Versions.V1);

  /**
   * The GDataRequest interface represents a streaming connection to a GData
   * service that can be used either to send request data to the service using
   * an OutputStream (or XmlWriter for XML content) or to receive response data
   * from the service as an InputStream (or ParseSource for XML data). The
   * calling client then has full control of the request data generation and
   * response data parsing. This can be used to integrate GData services with an
   * external Atom or RSS parsing library, such as Rome.
   * <p>
   * A GDataRequest instance will be returned by the streaming client APIs of
   * the Service class. The basic usage pattern is:
   * <p>
   * 
   * <pre>
   * GDataRequest request = ...     // createXXXRequest API call
   * try {
   *    OutputStream requestStream = request.getRequestStream();
   *    // stream request data, if any
   *    request.execute()                // execute the request
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
     * The RequestType enumeration defines the set of expected GData request
     * types. These correspond to the four operations of the GData protocol:
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
     * Sets the number of milliseconds to wait for a connection to the remote
     * GData service before timing out.
     * 
     * @param timeout the read timeout. A value of zero indicates an infinite
     *        timeout.
     * @throws IllegalArgumentException if the timeout value is negative.
     * 
     * @see java.net.URLConnection#setConnectTimeout(int)
     */
    public void setConnectTimeout(int timeout);


    /**
     * Sets the number of milliseconds to wait for a response from the remote
     * GData service before timing out.
     * 
     * @param timeout the read timeout. A value of zero indicates an infinite
     *        timeout.
     * @throws IllegalArgumentException if the timeout value is negative.
     * 
     * @see java.net.URLConnection#setReadTimeout(int)
     */
    public void setReadTimeout(int timeout);

    /**
     * Sets the entity tag value that will be used to conditionalize the request
     * if not {@code null}. For a query requests, the tag will cause the target
     * resource to be returned if the resource entity tag <b>does not match</b>
     * the specified value (i.e. if the resource has not changed). For update or
     * delete request types, the entity tag value is used to indicate that the
     * requested operation should occur only if the specified etag value <b>does
     * match</b> the specified value (i.e. if the resource has changed). A
     * request entity tag value may not be associated with other request types.
     * 
     * @param etag
     */
    public void setEtag(String etag);

    /**
     * Sets the If-Modified-Since date precondition to be applied to the
     * request. If this precondition is set, then the request will be performed
     * only if the target resource has been modified since the specified date;
     * otherwise, a {@code NotModifiedException} will be thrown. The default
     * value is {@code null}, indicating no precondition.
     * 
     * @param conditionDate the date that should be used to limit the operation
     *        on the target resource. The operation will only be performed if
     *        the resource has been modified later than the specified date.
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
     * Sets request header (and log just the name but not the value, if logging
     * is enabled)
     * 
     * @param name the header name
     * @param value the header value
     */
    public void setPrivateHeader(String name, String value);

    /**
     * Returns a stream that can be used to write request data to the GData
     * service.
     * 
     * @return OutputStream that can be used to write GData request data.
     * @throws IOException error obtaining the request output stream.
     */
    public OutputStream getRequestStream() throws IOException;

    /**
     * Returns an XML writer that can be used to write XML request data to the
     * GData service.
     * 
     * @return XmlWriter that can be used to write GData XML request data.
     * @throws IOException error obtaining the request writer.
     * @throws ServiceException error obtaining the request writer.
     */
    public XmlWriter getRequestWriter() throws IOException, ServiceException;

    /**
     * Executes the GData service request.
     * 
     * @throws IOException error writing to or reading from GData service.
     * @throws com.google.gdata.util.ResourceNotFoundException invalid request
     *         target resource.
     * @throws ServiceException system error executing request.
     */
    public void execute() throws IOException, ServiceException;

    /**
     * Returns the content type of the GData response.
     * 
     * @return ContentType the GData response content type or {@code null} if no
     *         response content.
     * @throws IllegalStateException attempt to read content type without first
     *         calling {@link #execute()}.
     * @throws IOException error obtaining the response content type.
     * @throws ServiceException error obtaining the response content type.
     */
    public ContentType getResponseContentType() throws IOException,
        ServiceException;

    /**
     * Returns an input stream that can be used to read response data from the
     * GData service. Returns null if response data cannot be read as an input
     * stream. Use {@link #getParseSource()} instead.
     * <p>
     * <b>The caller is responsible for ensuring that the input stream is
     * properly closed after the response has been read.</b>
     * 
     * @return InputStream providing access to GData response input stream.
     * @throws IllegalStateException attempt to read response without first
     *         calling {@link #execute()}.
     * @throws IOException error obtaining the response input stream.
     */
    public InputStream getResponseStream() throws IOException;

    /**
     * Returns the value of the specified response header name or {@code null}
     * if no response header of this type exists.
     * 
     * @param headerName name of header
     * @return header value.
     */
    public String getResponseHeader(String headerName);

    /**
     * Returns the value of a header containing a header or {@code null} if no
     * response header of this type exists or it could not be parsed as a valid
     * date.
     * 
     * @param headerName name of header
     * @return header value.
     */
    public DateTime getResponseDateHeader(String headerName);

    /**
     * Returns a parse source that can be used to read response data from the
     * GData service. Parse source is an abstraction over input streams,
     * readers, and other forms of input.
     * <p>
     * <b>The caller is responsible for ensuring that input streams and readers
     * contained in the parse source are properly closed after the response has
     * been read.</b>
     * 
     * @return ParseSource providing access to GData response data.
     * @throws IllegalStateException attempt to read response without first
     *         calling {@link #execute()}.
     * @throws IOException error obtaining the response data.
     * @throws ServiceException error obtaining the response data.
     */
    public ParseSource getParseSource() throws IOException, ServiceException;
  }


  /**
   * The GDataRequestFactory interface defines a basic factory interface for
   * constructing a new GDataRequest interface.
   */
  public interface GDataRequestFactory {

    /**
     * Set a header that will be included in all requests. If header of the same
     * name was previously set, then replace the previous header value.
     * 
     * @param header the name of the header
     * @param value the value of the header, if null, then unset that header.
     */
    public void setHeader(String header, String value);

    /**
     * Set a header that will be included in all requests and do not log the
     * value. Useful for values that are sensitive or related to security. If
     * header of the same name was previously set, then replace the previous
     * header value.
     * 
     * @param header the name of the header
     * @param value the value of the header. If null, then unset that header.
     */
    public void setPrivateHeader(String header, String value);

    /**
     * Set authentication token to be used on subsequent requests created via
     * {@link #getRequest(
     * com.google.gdata.client.Service.GDataRequest.RequestType, URL,
     * ContentType)}.
     * 
     * An {@link IllegalArgumentException} is thrown if an auth token of the
     * wrong type is passed, or if authentication is not supported.
     * 
     * @param authToken Authentication token.
     */
    public void setAuthToken(AuthToken authToken);

    /**
     * Creates a new GDataRequest instance of the specified RequestType.
     */
    public GDataRequest getRequest(GDataRequest.RequestType type,
        URL requestUrl, ContentType contentType) throws IOException,
        ServiceException;

    /**
     * Creates a new GDataRequest instance for querying a service. This method
     * pushes the query parameters down to the factory method instead of
     * serializing them as a URL. Some factory implementations prefer to get
     * access to query parameters in their original form, not as a URL.
     */
    public GDataRequest getRequest(Query query, ContentType contentType)
        throws IOException, ServiceException;
  }

  /**
   * Initializes the version information for a specific service type. Subclasses
   * of {@link Service} will generally call this method from within their static
   * initializers to bind version information for the associated service.
   * 
   * @param serviceClass the service type being initialized.
   * @param defaultVersion the service version expected by this client library.
   */
  protected static Version initServiceVersion(
      Class<? extends Service> serviceClass, Version defaultVersion) {

    VersionRegistry versionRegistry = VersionRegistry.ensureRegistry();
    Version v = null;
    try {
      // Check to see if default has already been defined
      v = versionRegistry.getVersion(serviceClass);
    } catch (IllegalStateException ise) { 
      // If not, use system property override or provided default version
      try {
        v = VersionRegistry.getVersionFromProperty(serviceClass);
      } catch (SecurityException e) {
        // Ignore exception, and just take default.
      }
      if (v == null) {
        v = defaultVersion;
      }
      // Do not include any implied versions, which are defaulted separately
      // by their own service static initialization.
      versionRegistry.addDefaultVersion(v, false);
    }
    return v;
  }

  /**
   * Returns the current {@link Version} of the GData core protocol.
   * 
   * @return protocol version.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(Service.class);
  }

  /**
   * Constructs a new Service instance that is configured to accept arbitrary
   * extension data within feed or entry elements.
   */
  public Service() {

    // Set the default User-Agent value for requests
    requestFactory.setHeader("User-Agent", getServiceVersion());
    
    // Initialize the protocol version for this Service instance
    protocolVersion = initProtocolVersion(getClass());

    // The default extension profile is configured to accept arbitrary XML
    // at the feed or entry level. A client never wants to lose any
    // foreign markup, so capture everything even if not explicitly
    // understood.
    new Feed().declareExtensions(extProfile);
  }
  
  @SuppressWarnings("unchecked")
  private static Version initProtocolVersion(
      Class<? extends Service> serviceClass) {

    // Find the service type with a declared default version that is
    // closest to the requested service type. Walking up the class hierarchy
    // allows for the possibility of subclassing without defining a different
    // protocol type
    Class<? extends Service> checkClass = serviceClass;
    VersionRegistry registry = VersionRegistry.get();
    while (checkClass != Service.class) {
      try {
        return registry.getVersion(checkClass);
      } catch (IllegalStateException ise) {
        checkClass = (Class<? extends Service>) checkClass.getSuperclass();
      }
    }
    
    // If no matching default, just return the core protocol version
    try {
      return Service.getVersion();
    } catch (IllegalStateException ise) {
      return CORE_VERSION;
    }
  }
  
  /**
   * The version of the service protocol to use for this service instance. It
   * will be initialized to the service default version but can be set
   * explicitly by calling {@link #setProtocolVersion(Version)}.
   */
  private Version protocolVersion; 
  
  /**
   * Returns the service protocol version that will be used for requests
   * generated by this service.
   * 
   * @return service protocol version
   */
  public Version getProtocolVersion() {
    return protocolVersion;
  }
  
  /**
   * Sets the service protocol version that will be used for requests associated
   * with this service.
   * 
   * @param v new service protocol version.
   */
  public void setProtocolVersion(Version v) {
    // Ensure that any set version is appropriate for this service type, based
    // upon the default type that was computed at construction time.
    if (!protocolVersion.getServiceClass().equals(v.getServiceClass())) {
      throw new IllegalArgumentException("Invalid service class, " +
          "was: " + v.getServiceClass() + 
          ", expected: " + protocolVersion.getServiceClass());
    }
    protocolVersion = v;
  }
  
  protected void startVersionScope() {
    VersionRegistry.get().setThreadVersion(protocolVersion);
  }
  
  protected void endVersionScope() {
    VersionRegistry.get().resetThreadVersion();
  }

  /**
   * Returns information about the service version.
   */
  public String getServiceVersion() {
    return SERVICE_VERSION;
  }

  protected ExtensionProfile extProfile = new ExtensionProfile();

  /**
   * Returns the {@link ExtensionProfile} that defines any expected extensions
   * to the base RSS/Atom content model.
   */
  public ExtensionProfile getExtensionProfile() {
    return extProfile;
  }

  /**
   * Sets the {@link ExtensionProfile} that defines any expected extensions to
   * the base RSS/Atom content model.
   */
  public void setExtensionProfile(ExtensionProfile v) {
    this.extProfile = v;
  }

  /**
   * The GDataRequestFactory associated with this service. The default is the
   * base HttpGDataRequest Factory class.
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
   * Sets the HttpGDataRequest.Factory associate with the service
   * to use secure connections.
   */
  public void useSsl() {
    if (!(this.requestFactory instanceof HttpGDataRequest.Factory)) {
      throw new UnsupportedOperationException("Not a http transport");
    }
    ((HttpGDataRequest.Factory) this.requestFactory).useSsl();
  }

  /**
   * Defines the languages accepted by the application.
   *
   * This parameters defines the human language the service should use for
   * generated strings. Different services support different languages, please
   * check the service documentation.
   *
   * If no language on this list is accepted by the service, and if the list
   * does not contain {@code *} to accept all languages, the exception
   * in the exception {@link com.google.gdata.util.NotAcceptableException}.
   *
   * The service will choose the best available language on this list. Check the
   * attribute {@code xml:lang} on the relevant tags, such as atom:content,
   * atom:title and atom:category.
   *
   * @param acceptedLanguages list of accepted languages, as defined
   *        in section 14.4 of RFC 2616
   */
  public void setAcceptLanguage(String acceptedLanguages) {
    this.requestFactory.setHeader(
        GDataProtocol.Header.ACCEPT_LANGUAGE, acceptedLanguages);
  }

  /**
   * Creates a new GDataRequest for use by the service.
   * 
   * For query requests, use {@link #createRequest(Query, ContentType)} instead.
   */
  public GDataRequest createRequest(GDataRequest.RequestType type,
      URL requestUrl, ContentType inputType) throws IOException,
      ServiceException {

    GDataRequest request =
        requestFactory.getRequest(type, requestUrl, inputType);
    setTimeouts(request);
    return request;
  }


  /**
   * Creates a new GDataRequest for querying the service.
   */
  protected GDataRequest createRequest(Query query, ContentType inputType)
      throws IOException, ServiceException {

    GDataRequest request = requestFactory.getRequest(query, inputType);
    setTimeouts(request);
    return request;
  }


  /**
   * Sets timeout value for GDataRequest.
   */
  public void setTimeouts(GDataRequest request) {
    if (connectTimeout >= 0) {
      request.setConnectTimeout(connectTimeout);
    }
    if (readTimeout >= 0) {
      request.setReadTimeout(readTimeout);
    }
  }


  /**
   * Content type of data posted to the GData service. Defaults to Atom using
   * UTF-8 character set.
   */
  private ContentType contentType = ContentType.ATOM;

  /**
   * Returns the default ContentType for data associated with this GData
   * service.
   */
  public ContentType getContentType() {
    return contentType;
  }


  /**
   * Sets the default ContentType for writing data to the GData service.
   */
  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }


  /**
   * Client-configured connection timeout value. A value of -1 indicates the
   * client has not set any timeout.
   */
  protected int connectTimeout = -1;


  /**
   * Sets the default wait timeout (in milliseconds) for a connection to the
   * remote GData service.
   * 
   * @param timeout the read timeout. A value of zero indicates an infinite
   *        timeout.
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
   * Client configured read timeout value. A value of -1 indicates the client
   * has not set any timeout.
   */
  int readTimeout = -1;


  /**
   * Sets the default wait timeout (in milliseconds) for a response from the
   * remote GData service.
   * 
   * @param timeout the read timeout. A value of zero indicates an infinite
   *        timeout.
   * @throws IllegalArgumentException if the timeout value is negative.
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
   * Parse an entry of the specified class from a parse source.
   */
  protected <E extends BaseEntry<?>> E parseEntry(Class<E> entryClass,
      ParseSource entrySource) throws IOException, ServiceException {

    E entry = BaseEntry.readEntry(entrySource, entryClass, extProfile);
    entry.setService(this);
    return entry;
  }

  /**
   * Returns the Atom introspection Service Document associated with a
   * particular feed URL. This document provides service metadata about the set
   * of Atom services associated with the target feed URL.
   * 
   * @param feedUrl the URL associated with a feed. This URL can not include any
   *        query parameters.
   * @param serviceClass the class used to represent a service document.
   * 
   * @return ServiceDocument resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         service data.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving service document.
   */
  public <S extends ServiceDocument> S introspect(URL feedUrl,
      Class<S> serviceClass) throws IOException, ServiceException {

    String feedQuery = feedUrl.getQuery();
    if (feedQuery == null || feedQuery.indexOf("alt=atom-service") == -1) {
      char appendChar = (feedQuery == null) ? '?' : '&';
      feedUrl = new URL(feedUrl.toString() + appendChar + "alt=atom-service");
    }

    InputStream responseStream = null;
    GDataRequest request = createFeedRequest(feedUrl);
    try {
      startVersionScope();
      request.execute();
      responseStream = request.getResponseStream();
      if (responseStream == null) {
        throw new ServiceException("Unable to obtain service document");
      }

      S serviceDoc = serviceClass.newInstance();
      serviceDoc.parse(extProfile, responseStream);

      return serviceDoc;

    } catch (InstantiationException e) {
      throw new ServiceException("Unable to create service document instance",
          e);
    } catch (IllegalAccessException e) {
      throw new ServiceException("Unable to create service document instance",
          e);
    } finally {
      endVersionScope();
      if (responseStream != null) {
        responseStream.close();
      }
    }
  }


  /**
   * Returns the Feed associated with a particular feed URL, if it's been
   * modified since the specified date.
   * 
   * @param feedUrl the URL associated with a feed. This URL can include GData
   *        query parameters.
   * @param feedClass the class used to represent a service Feed.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *        feed should be returned only if it has been modified after the
   *        specified date. A value of {@code null} indicates no precondition.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws com.google.gdata.util.NotModifiedException if the feed resource has
   *         not been modified since the specified precondition date.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         feed data.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  @SuppressWarnings("unchecked")
  public <F extends BaseFeed<?, ?>> F getFeed(URL feedUrl, Class<F> feedClass,
      DateTime ifModifiedSince) throws IOException, ServiceException {
    GDataRequest request = createFeedRequest(feedUrl);
    return getFeed(request, feedClass, ifModifiedSince);
  }
  
  /**
   * Returns the Feed associated with a particular feed URL if the entity tag
   * associated with it has changed.
   * 
   * @param feedUrl the URL associated with a feed. This URL can include GData
   *        query parameters.
   * @param feedClass the class used to represent a service Feed.
   * @param etag used to provide an entity tag that indicates the feed should be
   *        returned only if the entity tag of the current representation is
   *        different from the provided value. A value of {@code null} indicates
   *        unconditional return.
   * @throws IOException error sending request or reading the feed.
   * @throws NotModifiedException if the feed resource entity tag matches the
   *         provided value.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         feed data.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  @SuppressWarnings("unchecked")
  public <F extends BaseFeed<?, ?>> F getFeed(URL feedUrl, Class<F> feedClass,
      String etag) throws IOException, ServiceException {
    GDataRequest request = createFeedRequest(feedUrl);
    return getFeed(request, feedClass, etag);
  }
  

  /**
   * Returns the Feed associated with a particular feed URL.
   * 
   * @param feedUrl the URL associated with a feed. This URL can include GData
   *        query parameters.
   * @param feedClass the class used to represent a service Feed.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         feed data.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  public <F extends BaseFeed<?, ?>> F getFeed(URL feedUrl, Class<F> feedClass)
      throws IOException, ServiceException {
    return getFeed(feedUrl, feedClass, (String) null);
  }
 
  
  /**
   * Returns the Feed associated with a particular query.
   * 
   * @param query feed query.
   * @param feedClass the class used to represent a service Feed.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws ParseException error parsing the returned feed data.
   * @throws ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  public <F extends BaseFeed<?, ?>> F getFeed(Query query, Class<F> feedClass)
      throws IOException, ServiceException {
    return getFeed(query, feedClass, (String) null);
  }
  

  /**
   * Returns the Feed associated with a particular query, if it's been modified
   * since the specified date.
   * 
   * @param query feed query.
   * @param feedClass the class used to represent a service Feed.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *        feed should be returned only if it has been modified after the
   *        specified date. A value of {@code null} indicates no precondition.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws ServiceException system error retrieving feed.
   */
  public <F extends BaseFeed<?, ?>> F getFeed(Query query, Class<F> feedClass,
      DateTime ifModifiedSince) throws IOException, ServiceException {
    GDataRequest request = createFeedRequest(query);
    return getFeed(request, feedClass, ifModifiedSince);
  }

  /**
   * Returns the Feed associated with a particular query, if  if the entity tag
   * associated with it has changed.
   * 
   * @param query feed query.
   * @param feedClass the class used to represent a service Feed.
   * @param etag used to provide an entity tag that indicates the feed should be
   *        returned only if the entity tag of the current representation is
   *        different from the provided value. A value of {@code null} indicates
   *        unconditional return.
   * @throws IOException error sending request or reading the feed.
   * @throws NotModifiedException if the feed resource entity tag matches the
   *         provided value.
   * @throws ParseException error parsing the returned feed data.
   * @throws ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  public <F extends BaseFeed<?, ?>> F getFeed(Query query, Class<F> feedClass,
      String etag) throws IOException, ServiceException {
    GDataRequest request = createFeedRequest(query);
    return getFeed(request, feedClass, etag);
  }

  /**
   * Returns the Feed associated with a particular feed URL, if it's been
   * modified since the specified date.
   *
   * @param request the GData request.
   * @param feedClass the class used to represent a service Feed.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *        feed should be returned only if it has been modified after the
   *        specified date. A value of {@code null} indicates no precondition.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws ServiceException system error retrieving feed.
   */
  @SuppressWarnings("unchecked")
  private <F extends BaseFeed<?, ?>> F getFeed(GDataRequest request,
      Class<F> feedClass, DateTime ifModifiedSince) throws IOException,
      ServiceException {

    ParseSource feedSource = null;
    try {
      startVersionScope();
      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      feedSource = request.getParseSource();

      BaseFeed<?, ?> feed =
          BaseFeed.readFeed(feedSource, feedClass, extProfile);
      feed.setService(this);
      return (F) feed;
    } finally {
      endVersionScope();
      closeSource(feedSource);
    }
  }

  /**
   * Returns the Feed associated with a particular feed URL if its entity tag
   * is different than the provided value.
   * 
   * @param request the GData request.
   * @param feedClass the class used to represent a service Feed.
   * @param etag used to provide an entity tag that indicates the feed should be
   *        returned only if the entity tag of the current representation is
   *        different from the provided value. A value of {@code null} indicates
   *        unconditional return.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws NotModifiedException if the feed resource entity tag matches the
   *         provided value.
   * @throws ParseException error parsing the returned feed data.
   * @throws ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  @SuppressWarnings("unchecked")
  private <F extends BaseFeed<?, ?>> F getFeed(GDataRequest request,
      Class<F> feedClass, String etag) throws IOException, ServiceException {

    ParseSource feedSource = null;
    try {
      startVersionScope();
      request.setEtag(etag);
      request.execute();
      feedSource = request.getParseSource();

      BaseFeed<?, ?> feed =
          BaseFeed.readFeed(feedSource, feedClass, extProfile);
      feed.setService(this);
      return (F) feed;
    } finally {
      endVersionScope();
      closeSource(feedSource);
    }
  }
  
  /**
   * Executes a GData feed request against the target service and returns the
   * resulting feed results via an input stream.
   * 
   * @param feedUrl URL that defines target feed.
   * @return GData request instance that can be used to read the feed data.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of query feed request failed.
   * 
   * @see Query#getUrl()
   */
  public GDataRequest createFeedRequest(URL feedUrl) throws IOException,
      ServiceException {
    return createRequest(GDataRequest.RequestType.QUERY, feedUrl, contentType);
  }


  /**
   * Executes a GData query request against the target service and returns the
   * resulting feed results via an input stream.
   * 
   * @param query feed query.
   * @return GData request instance that can be used to read the feed data.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of query feed request failed.
   * 
   * @see Query#getUrl()
   */
  public GDataRequest createFeedRequest(Query query) throws IOException,
      ServiceException {
    return createRequest(query, contentType);
  }


  /**
   * Returns an Atom entry instance, given the URL of the entry.
   * 
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries.
   * @return the entry referenced by the URL parameter.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         entry.
   * @throws com.google.gdata.util.ResourceNotFoundException if the entry URL is
   *         not valid.
   * @throws com.google.gdata.util.ServiceForbiddenException if the GData
   *         service cannot get the entry resource due to access constraints.
   * @throws ServiceException if a system error occurred when retrieving the
   *         entry.
   */
  public <E extends BaseEntry<?>> E getEntry(URL entryUrl, Class<E> entryClass)
      throws IOException, ServiceException {
    return getEntry(entryUrl, entryClass, (String) null);
  }
  
  
  /**
   * Returns an Atom entry instance, given the URL of the entry and an
   * if-modified-since date.
   * 
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *        entry should be returned only if it has been modified after the
   *        specified date. A value of {@code null} indicates no precondition.
   * @return the entry referenced by the URL parameter.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.NotModifiedException if the entry resource
   *         has not been modified after the specified precondition date.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         entry.
   * @throws com.google.gdata.util.ResourceNotFoundException if the entry URL is
   *         not valid.
   * @throws com.google.gdata.util.ServiceForbiddenException if the GData
   *         service cannot get the entry resource due to access constraints.
   * @throws ServiceException if a system error occurred when retrieving the
   *         entry.
   */
  public <E extends BaseEntry<?>> E getEntry(URL entryUrl, Class<E> entryClass,
      DateTime ifModifiedSince) throws IOException, ServiceException {

    ParseSource entrySource = null;
    GDataRequest request = createEntryRequest(entryUrl);
    try {
      startVersionScope();
      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      entrySource = request.getParseSource();
      return parseEntry(entryClass, entrySource);

    } finally {
      endVersionScope();
      closeSource(entrySource);
    }
  }


  /**
   * Returns an Atom entry instance given the URL of the entry, if its current
   * entity tag is different than the provided value.
   * 
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries.
   * @param etag used to provide an entity tag that indicates the entry should
   *        be returned only if the entity tag of the current representation is
   *        different from the provided value. A value of {@code null} indicates
   *        unconditional return.
   * @throws IOException error communicating with the GData service.
   * @throws NotModifiedException if the entry resource entity tag matches the
   *         provided value.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         entry.
   * @throws com.google.gdata.util.ResourceNotFoundException if the entry URL is
   *         not valid.
   * @throws com.google.gdata.util.ServiceForbiddenException if the GData
   *         service cannot get the entry resource due to access constraints.
   * @throws ServiceException if a system error occurred when retrieving the
   *         entry.
   */
  public <E extends BaseEntry<?>> E getEntry(URL entryUrl, Class<E> entryClass,
      String etag) throws IOException, ServiceException {

    ParseSource entrySource = null;
    GDataRequest request = createEntryRequest(entryUrl);
    try {
      startVersionScope();
      request.setEtag(etag);
      request.execute();
      entrySource = request.getParseSource();
      return parseEntry(entryClass, entrySource);

    } finally {
      endVersionScope();
      closeSource(entrySource);
    }
  }
  
  
  /**
   * Returns a GDataRequest instance that can be used to access an entry's
   * contents as a stream, given the URL of the entry.
   * 
   * @param entryUrl resource URL for the entry.
   * @return GData request instance that can be used to read the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException entry request creation failed.
   */
  public GDataRequest createEntryRequest(URL entryUrl) throws IOException,
      ServiceException {
    return createRequest(GDataRequest.RequestType.QUERY, entryUrl, contentType);
  }
  

  /**
   * Executes a GData query against the target service and returns the
   * {@link Feed} containing entries that match the query result.
   * 
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ServiceForbiddenException feed does not
   *         support the query.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         feed data.
   * @throws ServiceException query request failed.
   */
  public <F extends BaseFeed<?, ?>> F query(Query query, Class<F> feedClass)
      throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return query(query, feedClass, (String) null);
  }
  
  
  /**
   * Executes a GData query against the target service and returns the
   * {@link Feed} containing entries that match the query result, if it's been
   * modified since the specified date.
   * 
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *        query result feed should be returned only if contains entries that
   *        have been modified after the specified date. A value of {@code null}
   *        indicates no precondition.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.NotModifiedException if the query resource
   *         does not contain entries modified since the specified precondition
   *         date.
   * @throws com.google.gdata.util.ServiceForbiddenException feed does not
   *         support the query.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         feed data.
   * @throws ServiceException query request failed.
   */
  public <F extends BaseFeed<?, ?>> F query(Query query, Class<F> feedClass,
      DateTime ifModifiedSince) throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return getFeed(query, feedClass, ifModifiedSince);
  }
  
  
  /**
   * Executes a GData query against the target service and returns the
   * {@link Feed} containing entries that match the query result if the etag
   * for the target feed does not match the provided value.
   * 
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed.
   * @param etag used to provide an entity tag that indicates the query should be
   *        be performed only if the entity tag of the current representation is
   *        different from the provided value. A value of {@code null} indicates
   *        unconditional return.
   * @throws IOException error communicating with the GData service.
   * @throws NotModifiedException if the feed resource entity tag matches the
   *         provided value.
   * @throws com.google.gdata.util.ServiceForbiddenException feed does not
   *         support the query.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         feed data.
   * @throws ServiceException query request failed.
   */
  public <F extends BaseFeed<?, ?>> F query(Query query, Class<F> feedClass,
      String etag) throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return getFeed(query, feedClass, etag);
  }




  /**
   * Inserts a new {@link com.google.gdata.data.Entry} into a feed associated
   * with the target service. It will return the inserted Entry, including any
   * additional attributes or extensions set by the GData server.
   * 
   * @param feedUrl the POST URI associated with the target feed.
   * @param entry the new entry to insert into the feed.
   * @return the newly inserted Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the return entry
   *         data.
   * @throws com.google.gdata.util.ServiceForbiddenException the inserted Entry
   *         has associated media content and can only be inserted using a media
   *         service.
   * @throws ServiceException insert request failed due to system error.
   * 
   * @see BaseFeed#getEntryPostLink()
   * @see BaseFeed#insert(BaseEntry)
   */
  @SuppressWarnings("unchecked")
  public <E extends BaseEntry<?>> E insert(URL feedUrl, E entry)
      throws IOException, ServiceException {

    if (entry == null) {
      throw new NullPointerException("Must supply entry");
    }

    ParseSource resultEntrySource = null;
    try {
      startVersionScope();
      GDataRequest request = createInsertRequest(feedUrl);
      XmlWriter xw = request.getRequestWriter();
      entry.generateAtom(xw, extProfile);
      xw.flush();

      request.execute();

      resultEntrySource = request.getParseSource();
      return (E) parseEntry(entry.getClass(), resultEntrySource);

    } finally {
      endVersionScope();
      closeSource(resultEntrySource);
    }
  }


  /**
   * Executes several operations (insert, update or delete) on the entries that
   * are part of the input {@link Feed}. It will return another feed that
   * describes what was done while executing these operations.
   * 
   * It is possible for one batch operation to fail even though other operations
   * have worked, so this method won't throw a ServiceException unless something
   * really wrong is going on. You need to check the entries in the returned
   * feed to know which operations succeeded and which operations failed (see
   * {@link com.google.gdata.data.batch.BatchStatus} and
   * {@link com.google.gdata.data.batch.BatchInterrupted} extensions.)
   * 
   * @param feedUrl the POST URI associated with the target feed.
   * @param inputFeed a description of the operations to execute, described
   *        using tags in the batch: namespace
   * @return a feed with the result of each operation in a separate entry
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the return entry
   *         data.
   * @throws ServiceException insert request failed due to system error.
   * @throws BatchInterruptedException if something really wrong was detected by
   *         the server while parsing the request, like invalid XML data. Some
   *         operations might have succeeded when this exception is thrown.
   *         Check {@link BatchInterruptedException#getFeed()}.
   * 
   * @see BaseFeed#getEntryPostLink()
   * @see BaseFeed#insert(BaseEntry)
   */
  @SuppressWarnings("unchecked")
  public <F extends BaseFeed<?, ?>> F batch(URL feedUrl, F inputFeed)
      throws IOException, ServiceException, BatchInterruptedException {
    ParseSource resultFeedSource = null;
    GDataRequest request = createInsertRequest(feedUrl);
    try {
      startVersionScope();
      XmlWriter xw = request.getRequestWriter();
      inputFeed.generateAtom(xw, extProfile);
      xw.flush();

      request.execute();

      resultFeedSource = request.getParseSource();
      F resultFeed =
          (F) BaseFeed.readFeed(resultFeedSource, inputFeed.getClass(),
              extProfile);
      resultFeed.setService(this);

      // Detect BatchInterrupted
      int count = resultFeed.getEntries().size();
      if (count > 0) {
        BaseEntry<?> entry = resultFeed.getEntries().get(count - 1);
        BatchInterrupted interrupted = BatchUtils.getBatchInterrupted(entry);
        if (interrupted != null) {
          throw new BatchInterruptedException(resultFeed, interrupted);
        }
      }

      return resultFeed;

    } finally {
      endVersionScope();
      closeSource(resultFeedSource);
    }
  }

  /**
   * Creates a new GDataRequest that can be used to insert a new entry into a
   * feed using the request stream and to read the resulting entry content from
   * the response stream.
   * 
   * @param feedUrl the POST URI associated with the target feed.
   * @return GDataRequest to interact with remote GData service.
   * @throws IOException error reading from or writing to the GData service.
   * @throws ServiceException insert request failed.
   */
  public GDataRequest createInsertRequest(URL feedUrl) throws IOException,
      ServiceException {
    return createRequest(GDataRequest.RequestType.INSERT, feedUrl, contentType);
  }

  /**
   * Creates a new GDataRequest that can be used to execute several
   * insert/update/delete operations in one request by writing a feed into the
   * request stream to read a feed containing the result of the batch operations
   * from the response stream.
   * 
   * @param feedUrl the POST URI associated with the target feed.
   * @return GDataRequest to interact with remote GData service.
   * @throws IOException error reading from or writing to the GData service.
   * @throws ServiceException insert request failed.
   */
  public GDataRequest createBatchRequest(URL feedUrl) throws IOException,
      ServiceException {
    return createRequest(GDataRequest.RequestType.BATCH, feedUrl, contentType);
  }


  /**
   * Updates an existing {@link com.google.gdata.data.Entry} by writing it to
   * the specified entry edit URL. The resulting Entry (after update) will be
   * returned.
   * 
   * @param entryUrl the edit URL associated with the entry.
   * @param entry the modified Entry to be written to the server.
   * @return the updated Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the updated
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   * 
   * @see BaseEntry#getEditLink()
   * @see BaseEntry#update()
   */
  @SuppressWarnings("unchecked")
  public <E extends BaseEntry<?>> E update(URL entryUrl, E entry)
      throws IOException, ServiceException {
    
    // If the entry has a strong etag, use it as a precondition.
    String etag = entry.getEtag();
    if (GDataProtocol.isWeakEtag(etag)) {
      etag = null;
    }
    return update(entryUrl, entry, etag);
  }
  
  
  /**
   * Updates an existing {@link com.google.gdata.data.Entry} by writing it to
   * the specified entry edit URL. The resulting Entry (after update) will be
   * returned. This update is conditional upon the provided tag matching the
   * current entity tag for the entry. If (and only if) they match, the update
   * will be performed.
   * 
   * @param entryUrl the edit URL associated with the entry.
   * @param entry the modified Entry to be written to the server.
   * @param etag the entity tag value that is the expected value for the target
   *        resource.   A value of {@code null} will not set an etag 
   *        precondition and a value of <code>"*"</code> will perform an
   *        unconditional update.
   * @return the updated Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws PreconditionFailedException if the resource entity tag does not
   *         match the provided value.
   * @throws com.google.gdata.util.ParseException error parsing the updated
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   * 
   * @see BaseEntry#getEditLink()
   * @see BaseEntry#update()
   */
  @SuppressWarnings("unchecked")
  public <E extends BaseEntry<?>> E update(URL entryUrl, E entry, String etag)
      throws IOException, ServiceException {

    ParseSource resultEntrySource = null;
    GDataRequest request = createUpdateRequest(entryUrl);

    try {
      startVersionScope();
      request.setEtag(etag);
      
      // Send the entry
      XmlWriter xw = request.getRequestWriter();
      entry.generateAtom(xw, extProfile);
      xw.flush();

      // Execute the request
      request.execute();

      // Handle the update
      resultEntrySource = request.getParseSource();
      return (E) parseEntry(entry.getClass(), resultEntrySource);

    } finally {
      endVersionScope();
      closeSource(resultEntrySource);
    }
  }


  /**
   * Creates a new GDataRequest that can be used to update an existing Atom
   * entry. The updated entry content can be written to the GDataRequest request
   * stream and the resulting updated entry can be obtained from the
   * GDataRequest response stream.
   * 
   * @param entryUrl the edit URL associated with the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of update request failed.
   */
  public GDataRequest createUpdateRequest(URL entryUrl) throws IOException,
      ServiceException {

    return createRequest(GDataRequest.RequestType.UPDATE, entryUrl, 
        contentType);
  }


  /**
   * Deletes an existing entry (and associated media content, if any) using the
   * specified edit URL.
   * 
   * @param resourceUrl the edit or medit edit url associated with the resource.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid entry URL.
   * @throws ServiceException delete request failed due to system error.
   */
  public void delete(URL resourceUrl) throws IOException, ServiceException {
    delete(resourceUrl, null);
  }
  
  /**
   * Deletes an existing entry (and associated media content, if any) using the
   * specified edit URI.
   * 
   * @param resourceUri the edit or medit edit URI associated with the resource.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid entry URI.
   * @throws ServiceException delete request failed due to system error.
   */
  public void delete(URI resourceUri) throws IOException, ServiceException {
    delete(resourceUri.toURL(), null);
  }
  
  /**
   * Deletes an existing entry (and associated media content, if any) using the
   * specified edit URL. This delete is conditional upon the provided tag
   * matching the current entity tag for the entry. If (and only if) they match,
   * the deletion will be performed.
   * 
   * @param resourceUrl the edit or medit edit url associated with the resource.
   * @param etag the entity tag value that is the expected value for the target
   *        resource.   A value of {@code null} will not set an etag 
   *        precondition and a value of <code>"*"</code> will perform an
   *        unconditional delete.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid entry URL.
   * @throws ServiceException delete request failed due to system error.
   */
  public void delete(URL resourceUrl, String etag) 
      throws IOException, ServiceException {

    try {
      startVersionScope();
      GDataRequest request = createDeleteRequest(resourceUrl);
      request.setEtag(etag);
      request.execute();
    } finally {
      endVersionScope();
    }
  }
  
  /**
   * Deletes an existing entry (and associated media content, if any) using the
   * specified edit URI. This delete is conditional upon the provided tag
   * matching the current entity tag for the entry. If (and only if) they match,
   * the deletion will be performed.
   * 
   * @param resourceUri the edit or medit edit URI associated with the resource.
   * @param etag the entity tag value that is the expected value for the target
   *        resource.   A value of {@code null} will not set an etag 
   *        precondition and a value of <code>"*"</code> will perform an
   *        unconditional delete.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid entry URI.
   * @throws ServiceException delete request failed due to system error.
   */
  public void delete(URI resourceUri, String etag)
      throws IOException, ServiceException {
    delete(resourceUri.toURL(), etag);
  }

  /**
   * Creates a new GDataRequest that can be used to delete an Atom entry. For
   * delete requests, no input is expected from the request stream nor will any
   * response data be returned.
   * 
   * @param entryUrl the edit URL associated with the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of delete request failed.
   */
  public GDataRequest createDeleteRequest(URL entryUrl) throws IOException,
      ServiceException {

    return createRequest(GDataRequest.RequestType.DELETE, entryUrl, 
        contentType);
  }


  /**
   * Closes streams and readers associated with a parse source.
   * 
   * @param source Parse source.
   * @throws IOException
   */
  protected void closeSource(ParseSource source) throws IOException {
    if (source != null) {
      if (source.getInputStream() != null) {
        source.getInputStream().close();
      }
      if (source.getReader() != null) {
        source.getReader().close();
      }
    }
  }
  
  public InputStream getStreamFromLink(Link link) throws IOException, ServiceException {
    GDataRequest request = createRequest(GDataRequest.RequestType.QUERY, 
        new URL(link.getHref()), null);

    request.execute();
    InputStream resultStream = request.getResponseStream();

    return resultStream;
  }
}
