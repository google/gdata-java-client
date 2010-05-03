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

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.util.common.net.UriParameterMap;
import com.google.gdata.client.AuthTokenFactory.AuthToken;
import com.google.gdata.client.batch.BatchInterruptedException;
import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.IAtom;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.ILink;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.introspection.IServiceDocument;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataContext;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.Schema;
import com.google.gdata.model.atom.Feed;
import com.google.gdata.model.batch.BatchUtils;
import com.google.gdata.model.transforms.atom.AtomVersionTransforms;
import com.google.gdata.model.transforms.atompub.AtompubVersionTransforms;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.PreconditionFailedException;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.AltRegistry;
import com.google.gdata.wireformats.StreamProperties;
import com.google.gdata.wireformats.input.AtomDualParser;
import com.google.gdata.wireformats.input.AtomServiceDualParser;
import com.google.gdata.wireformats.input.InputParser;
import com.google.gdata.wireformats.input.InputProperties;
import com.google.gdata.wireformats.output.AtomDualGenerator;
import com.google.gdata.wireformats.output.AtomServiceDualGenerator;
import com.google.gdata.wireformats.output.OutputGenerator;
import com.google.gdata.wireformats.output.OutputProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

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
 * <li><b>Extensions</b> - define expected extensions for feed, entry, and
 * other types associated with a the service.
 * <li><b>Formats</b> - define additional custom resource representations that
 * might be consumed or produced by the service and client side parsers and
 * generators to handle them.
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
     * Version 1.  GData core protocol released in May 2006 and is still in use
     * by version 1 of some GData services.
     */
    public static final Version V1 = new Version(Service.class, 1, 0);

    /**
     * Version 2.  GData core protocol release that brings full alignment with
     * the now standard Atom Publishing Protocol specification and migrates to
     * OpenSearch 1.1.
     */
    public static final Version V2 = new Version(Service.class, 2, 0);

    /**
     * Version {@code 2.1}.  Support new gd:kind attribute on feeds and
     * entries.
     */
    public static final Version V2_1 = new Version(Service.class, 2, 1);

    /**
     * Version {@code 2.2}.  Unreleased next minor version of the GData
     * protocol.
     */
    public static final Version V2_2 = new Version(Service.class, 2, 2);

    /**
     * Version 3.  Unreleased next major version of the GData protocol that will
     * default to structured error messages.
     */
    public static final Version V3 = new Version(Service.class, 3, 0);

    private Versions() {}
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
   * } finally {
   *   request.end();
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
     * <li><b>PATCH</b> - patch an existing entry within a feed.</li>
     * <li><b>DELETE</b> - delete an existing entry within a feed.</li>
     * <li><b>BATCH</b> - execute several insert/update/delete operations</li>
     * </ul>
     */
    public enum RequestType {
      QUERY, INSERT, UPDATE, PATCH, DELETE, BATCH
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
     * Returns the {@link URL} that is the target of the GData request
     */
    public URL getRequestUrl();

    /**
     * Returns a stream that can be used to write request data to the GData
     * service.
     *
     * @return OutputStream that can be used to write GData request data.
     * @throws IOException error obtaining the request output stream.
     */
    public OutputStream getRequestStream() throws IOException;

    /**
     * Returns the {@link ContentType} of the data that will be written to the
     * service by this request or {@code null} if no data is written to the
     * server by the request.
     */
    public ContentType getRequestContentType();

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

    /**
     * Ends all processing associated with this request and releases any
     * transient resources (such as open data streams) required for execution.
     */
    public void end();
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
     * <p>
     * Clients should be sure to call {@link GDataRequest#end()} on the returned
     * request once they have finished using it.
     * 
     * @param type the request type
     * @param requestUrl the target URL for the request
     * @param contentType the contentType of the data being provided in the
     *        request body. May be {@code null} if no data is provided.
     */
    public GDataRequest getRequest(GDataRequest.RequestType type,
        URL requestUrl, ContentType contentType) throws IOException,
        ServiceException;

    /**
     * Creates a new GDataRequest instance for querying a service. This method
     * pushes the query parameters down to the factory method instead of
     * serializing them as a URL. Some factory implementations prefer to get
     * access to query parameters in their original form, not as a URL.
     * <p>
     * Clients should be sure to call {@link GDataRequest#end()} on the returned
     * request once they have finished using it.
     * 
     * @param query the query associated with the request
     * @param contentType this parameter is unused but remains for backwards
     *        compatibility.
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
    new com.google.gdata.data.Feed().declareExtensions(extProfile);

    // The default metadata registry contains the basic feed plus the
    // version transforms for atom and atompub.
    this.metadataRegistry = new MetadataRegistry();
    Feed.registerMetadata(metadataRegistry);
    AtomVersionTransforms.addTransforms(metadataRegistry);
    AtompubVersionTransforms.addTransforms(metadataRegistry);
  }

  /**
   * Returns an {@link AltRegistry} instance that is configured with the
   * default parser/generator configuration for a media service.
   */
  public static AltRegistry getDefaultAltRegistry() {
    return BASE_REGISTRY;
  }

  /**
   * The DEFAULT_REGISTRY contains the default set of representations and
   * associated parser/generator configurations for all services.  It will be
   * used as the default configuration for all Service instances unless
   * {@link #setAltRegistry(AltRegistry)} is called.
   */
  private static final AltRegistry BASE_REGISTRY = new AltRegistry();

  static {
    BASE_REGISTRY.register(AltFormat.ATOM,
        new AtomDualParser(),
        new AtomDualGenerator());

    BASE_REGISTRY.register(AltFormat.ATOM_SERVICE,
        new AtomServiceDualParser(),
        new AtomServiceDualGenerator());

    BASE_REGISTRY.register(AltFormat.APPLICATION_XML,
        null,
        new AtomDualGenerator(AltFormat.APPLICATION_XML));

    // protect against subsequent changes
    BASE_REGISTRY.lock();
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

  protected final MetadataRegistry metadataRegistry;

  /**
   * Returns the {@link MetadataRegistry} that defines the expected metadata.
   */
  public MetadataRegistry getMetadataRegistry() {
    return metadataRegistry;
  }

  /**
   * Returns the {@link Schema} that contains the metadata about
   * element types parsed or generated by this service.
   */
  public Schema getSchema() {
    return metadataRegistry.createSchema();
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
   * Set a header that will be included in all requests. If header of the same
   * name was previously set, then replace the previous header value.
   *
   * @param header the name of the header
   * @param value the value of the header, if null, then unset that header.
   */
  public void setHeader(String header, String value) {
    getRequestFactory().setHeader(header, value);
  }

  /**
   * Set a header that will be included in all requests and do not log the
   * value. Useful for values that are sensitive or related to security. If
   * header of the same name was previously set, then replace the previous
   * header value.
   *
   * @param header the name of the header
   * @param value the value of the header. If null, then unset that header.
   */
  public void setPrivateHeader(String header, String value) {
    getRequestFactory().setPrivateHeader(header, value);
  }

  /**
   * Adds OAuth Proxy-related headers to the request.  The OAuth Proxy
   * simplifies the OAuth dance on when running in App Engine.
   * @see <a href="http://sites.google.com/site/oauthgoog/Home/gaeoauthproxy">
   * http://sites.google.com/site/oauthgoog/Home/gaeoauthproxy</a>
   */
  public void setOAuthProxyHeaders(Map<String, String> headers) {
    for (String key : headers.keySet()) {
      setHeader(key, headers.get(key));
    }
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
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * The alternate representation registry that describes formats supported by
   * the remote GData service.
   */
  private AltRegistry altRegistry = BASE_REGISTRY;

  /**
   * Returns the alternate registration registry that describes representations
   * that may be parsed from or generated to the remote GData service.
   */
  public AltRegistry getAltRegistry() {
    return altRegistry;
  }

  public void setAltRegistry(AltRegistry altRegistry) {
    this.altRegistry = altRegistry;
  }

  private boolean strictValidation = true;

  /**
   * Returns {@code true} if strict validation is enabled for
   * this service.
   */
  public boolean getStrictValidation() {
    return strictValidation;
  }

  /**
   * Enables or disables strict validation. It is enabled by
   * default. When this flag is enabled, the client library rejects
   * unknown attributes and validates both input and output data.
   */
  public void setStrictValidation(boolean strictValidation) {
    this.strictValidation = strictValidation;
  }

  // Helper method that narrows the scope of unchecked (but safe) class casting.
  @SuppressWarnings("unchecked")
  protected <T> Class<T> classOf(T object) {
    return (Class<T>) object.getClass();
  }

  /**
   * Returns the Atom introspection Service Document associated with a
   * particular feed URL. This document provides service metadata about the set
   * of Atom services associated with the target feed URL.
   *
   * @param feedUrl the URL associated with a feed. This URL can not include any
   *        query parameters.
   * @param serviceClass the class used to represent a service document,
   *        not {@code null}.
   * @return ServiceDocument resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         service data.
   * @throws com.google.gdata.util.ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving service document.
   */
  public <S extends IServiceDocument> S introspect(URL feedUrl,
      Class<S> serviceClass) throws IOException, ServiceException {

    String feedQuery = feedUrl.getQuery();
    String altParam =
        GDataProtocol.Parameter.ALT + "=" + AltFormat.ATOM_SERVICE.getName();
    if (feedQuery == null || feedQuery.indexOf(altParam) == -1) {
      char appendChar = (feedQuery == null) ? '?' : '&';
      feedUrl = new URL(feedUrl.toString() + appendChar + altParam);
    }

    GDataRequest request = createFeedRequest(feedUrl);
    try {
      startVersionScope();
      request.execute();
      return parseResponseData(request, serviceClass);
    } finally {
      endVersionScope();
      request.end();
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
  public <F extends IFeed> F getFeed(URL feedUrl, Class<F> feedClass,
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
  public <F extends IFeed> F getFeed(URL feedUrl, Class<F> feedClass,
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
  public <F extends IFeed> F getFeed(URL feedUrl, Class<F> feedClass)
      throws IOException, ServiceException {
    return getFeed(feedUrl, feedClass, (String) null);
  }


  /**
   * Returns the feed resulting from execution of a query.
   *
   * @param query feed query.
   * @param feedClass the class used to represent query results.
   * @return feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws ParseException error parsing the returned feed data.
   * @throws ResourceNotFoundException invalid feed URL.
   * @throws ServiceException system error retrieving feed.
   */
  public <F extends IFeed> F getFeed(Query query, Class<F> feedClass)
      throws IOException, ServiceException {
    return getFeed(query, feedClass, (String) null);
  }


  /**
   * Returns the Feed resulting from executing a query, if it's been modified
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
  public <F extends IFeed> F getFeed(Query query, Class<F> feedClass,
      DateTime ifModifiedSince) throws IOException, ServiceException {
    GDataRequest request = createFeedRequest(query);
    return getFeed(request, feedClass, ifModifiedSince);
  }

  /**
   * Returns the Feed resulting from query execution, if  if the entity tag
   * associated with it has changed.
   *
   * @param query feed query.
   * @param feedClass the class used to represent query results.
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
  public <F extends IFeed> F getFeed(Query query, Class<F> feedClass,
      String etag) throws IOException, ServiceException {
    GDataRequest request = createFeedRequest(query);
    return getFeed(request, feedClass, etag);
  }

  /**
   * Returns the Feed associated with a particular feed URL, if it's been
   * modified since the specified date.
   *
   * @param request the GData request.
   * @param feedClass the class used to represent a service Feed,
   *        not {@code null}.
   * @param ifModifiedSince used to set a precondition date that indicates the
   *        feed should be returned only if it has been modified after the
   *        specified date. A value of {@code null} indicates no precondition.
   * @return Feed resource referenced by the input URL.
   * @throws IOException error sending request or reading the feed.
   * @throws ServiceException system error retrieving feed.
   */
  private <F extends IFeed> F getFeed(GDataRequest request,
      Class<F> feedClass, DateTime ifModifiedSince) throws IOException,
      ServiceException {

    try {
      startVersionScope();
      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      return parseResponseData(request, feedClass);
    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Returns the feed associated with a particular feed URL if its entity tag
   * is different than the provided value.
   *
   * @param request the GData request.
   * @param feedClass the class used to represent the resulting feed,
   *        not {@code null}.
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
  private <F extends IFeed> F getFeed(GDataRequest request,
      Class<F> feedClass, String etag) throws IOException, ServiceException {

    try {
      startVersionScope();
      request.setEtag(etag);
      request.execute();
      return parseResponseData(request, feedClass);
    } finally {
      endVersionScope();
      request.end();
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
   * Executes a GData query against the target service and returns the
   * {@link IFeed} containing entries that match the query result.
   *
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed,
   *        not {@code null}.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ServiceForbiddenException feed does not
   *         support the query.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         feed data.
   * @throws ServiceException query request failed.
   */
  public <F extends IFeed> F query(Query query, Class<F> feedClass)
      throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return query(query, feedClass, (String) null);
  }


  /**
   * Executes a GData query against the target service and returns the
   * {@link IFeed} containing entries that match the query result, if it's been
   * modified since the specified date.
   *
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed,
   *        not {@code null}.
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
  public <F extends IFeed> F query(Query query, Class<F> feedClass,
      DateTime ifModifiedSince) throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return getFeed(query, feedClass, ifModifiedSince);
  }


  /**
   * Executes a GData query against the target service and returns the
   * {@link IFeed} containing entries that match the query result if the etag
   * for the target feed does not match the provided value.
   *
   * @param query Query instance defining target feed and query parameters.
   * @param feedClass the Class used to represent a service Feed,
   *        not {@code null}.
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
  public <F extends IFeed> F query(Query query, Class<F> feedClass,
      String etag) throws IOException, ServiceException {

    // A query is really same as getFeed against the combined feed + query URL
    return getFeed(query, feedClass, etag);
  }

  /**
   * Executes a GData query request against the target service and returns the
   * resulting feed results via an input stream.
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * @param entryClass class used to represent service entries,
   *        not {@code null}.
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
  public <E extends IEntry> E getEntry(URL entryUrl, Class<E> entryClass)
      throws IOException, ServiceException {
    return getEntry(entryUrl, entryClass, (String) null);
  }

  /**
   * Returns an Atom entry instance, given the URL of the entry and an
   * if-modified-since date.
   *
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries,
   *        not {@code null}.
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
  public <E extends IEntry> E getEntry(URL entryUrl, Class<E> entryClass,
      DateTime ifModifiedSince) throws IOException, ServiceException {

    ParseSource entrySource = null;
    GDataRequest request = createEntryRequest(entryUrl);
    try {
      startVersionScope();
      request.setIfModifiedSince(ifModifiedSince);
      request.execute();
      return parseResponseData(request, entryClass);

    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Returns an Atom entry instance given the URL of the entry, if its current
   * entity tag is different than the provided value.
   *
   * @param entryUrl resource URL for the entry.
   * @param entryClass class used to represent service entries,
   *        not {@code null}.
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
  public <E extends IEntry> E getEntry(URL entryUrl, Class<E> entryClass,
      String etag) throws IOException, ServiceException {

    ParseSource entrySource = null;
    GDataRequest request = createEntryRequest(entryUrl);
    try {
      startVersionScope();
      request.setEtag(etag);
      request.execute();
      return parseResponseData(request, entryClass);

    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Returns a GDataRequest instance that can be used to access an entry's
   * contents as a stream, given the URL of the entry.
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * Inserts a new {@link IEntry} into a feed associated
   * with the target service. It will return the inserted entry, including any
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
   * @see IFeed#getEntryPostLink()
   */
  @SuppressWarnings("unchecked")
  public <E extends IEntry> E insert(URL feedUrl, E entry)
      throws IOException, ServiceException {

    if (entry == null) {
      throw new NullPointerException("Must supply entry");
    }

    GDataRequest request = createInsertRequest(feedUrl);
    try {
      startVersionScope();

      writeRequestData(request, entry);
      request.execute();
      return parseResponseData(request, classOf(entry));

    } finally {
      endVersionScope();
      request.end();
    }
  }


  /**
   * Executes several operations (insert, update or delete) on the entries that
   * are part of the input {@link IFeed}. It will return another feed that
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
   *         Check {@link BatchInterruptedException#getIFeed()}.
   *
   * @see IFeed#getEntryPostLink()
   */
  @SuppressWarnings("unchecked")
  public <F extends IFeed> F batch(URL feedUrl, F inputFeed)
      throws IOException, ServiceException, BatchInterruptedException {

    GDataRequest request = createInsertRequest(feedUrl);
    try {
      startVersionScope();
      writeRequestData(request, inputFeed);
      request.execute();
      F resultFeed = parseResponseData(request, classOf(inputFeed));

      // Detect BatchInterrupted
      int count = resultFeed.getEntries().size();
      BatchUtils.throwIfInterrupted(resultFeed);
      return resultFeed;

    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Creates a new GDataRequest that can be used to insert a new entry into a
   * feed using the request stream and to read the resulting entry content from
   * the response stream.
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * Updates an existing {@link IEntry} by writing it to
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
   * @see IEntry#getEditLink()
   */
  @SuppressWarnings("unchecked")
  public <E extends IEntry> E update(URL entryUrl, E entry)
      throws IOException, ServiceException {

    // If the entry has a strong etag, use it as a precondition.
    String etag = entry.getEtag();
    if (GDataProtocol.isWeakEtag(etag)) {
      etag = null;
    }
    return update(entryUrl, entry, etag);
  }

  /**
   * Updates an existing {@link IEntry} by writing it to the specified entry
   * edit URL. The resulting entry (after update) will be returned. This update
   * is conditional upon the provided tag matching the current entity tag for
   * the entry. If (and only if) they match, the update will be performed.
   *
   * @param entryUrl the edit URL associated with the entry.
   * @param entry the modified entry to be written to the server.
   * @param etag the entity tag value that is the expected value for the target
   *        resource. A value of {@code null} will not set an etag precondition
   *        and a value of <code>"*"</code> will perform an unconditional
   *        update.
   * @return the updated Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws PreconditionFailedException if the resource entity tag does not
   *         match the provided value.
   * @throws com.google.gdata.util.ParseException error parsing the updated
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see IEntry#getEditLink()
   */
  public <E extends IEntry> E update(URL entryUrl, E entry, String etag)
      throws IOException, ServiceException {

    GDataRequest request = createUpdateRequest(entryUrl);
    try {
      startVersionScope();
      request.setEtag(etag);
      writeRequestData(request, entry);
      request.execute();
      return parseResponseData(request, classOf(entry));
    } finally {
      endVersionScope();
      request.end();
    }
  }


  /**
   * Creates a new GDataRequest that can be used to update an existing Atom
   * entry. The updated entry content can be written to the GDataRequest request
   * stream and the resulting updated entry can be obtained from the
   * GDataRequest response stream.
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * Patches an existing {@link IEntry} by removing a set of selected fields and
   * then merging a partial entry representation into the resource at the
   * specified entry edit URL. The resulting entry (after update) will be
   * returned.
   *
   * @param entryUrl the edit URL associated with the entry.
   * @param fields selection representing the set of fields to be patched from
   *        the resource.
   * @param entry the partial entry to be merged with current resource.
   * @return the patched Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws com.google.gdata.util.ParseException error parsing the returned
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see IEntry#getEditLink()
   */
  @SuppressWarnings("unchecked")
  public <E extends IEntry> E patch(URL entryUrl, String fields, E entry)
      throws IOException, ServiceException {

    // If the entry has a strong etag, use it as a precondition.
    String etag = null;
    if (entry != null) {
      etag = entry.getEtag();
      if (GDataProtocol.isWeakEtag(etag)) {
        etag = null;
      }
    }
    return patch(entryUrl, fields, entry, etag);
  }


  /**
   * Patches an existing {@link IEntry} by removing a set of selected fields and
   * then merging a partial entry representation into the resource at the
   * specified entry edit URL. The resulting entry (after update) will be
   * returned. This update is conditional upon the provided tag matching the
   * current entity tag for the entry. If (and only if) they match, the patch
   * will be performed.
   *
   * @param entryUrl the edit URL associated with the entry.
   * @param fields selection representing the set of fields to be removed from
   *        the resource.
   * @param entry the partial entry to be merged with current resource.
   * @param etag the entity tag value that is the expected value for the target
   *        resource. A value of {@code null} will not set an etag precondition
   *        and a value of <code>"*"</code> will perform an unconditional
   *        update.
   * @return the patched Entry returned by the service.
   * @throws IOException error communicating with the GData service.
   * @throws PreconditionFailedException if the resource entity tag does not
   *         match the provided value.
   * @throws com.google.gdata.util.ParseException error parsing the patched
   *         entry data.
   * @throws ServiceException update request failed due to system error.
   *
   * @see IEntry#getEditLink()
   */
  public <E extends IEntry> E patch(URL entryUrl, String fields, E entry,
      String etag) throws IOException, ServiceException {

    GDataRequest request = createPatchRequest(entryUrl);
    try {
      startVersionScope();
      request.setEtag(etag);
      entry.setSelectedFields(fields);
      writeRequestData(request, entry);
      request.execute();
      return parseResponseData(request, classOf(entry));
    } finally {
      endVersionScope();
      request.end();
    }
  }

  /**
   * Creates a new GDataRequest that can be used to update an existing Atom
   * entry. The updated entry content can be written to the GDataRequest request
   * stream and the resulting updated entry can be obtained from the
   * GDataRequest response stream.
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
   *
   * @param entryUrl the edit URL associated with the entry.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of update request failed.
   */
  public GDataRequest createPatchRequest(URL entryUrl) throws IOException,
      ServiceException {

    return createRequest(GDataRequest.RequestType.PATCH, entryUrl,
        ContentType.APPLICATION_XML);
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

    GDataRequest request = createDeleteRequest(resourceUrl);
    try {
      startVersionScope();
      request.setEtag(etag);
      request.execute();
    } finally {
      request.end();
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
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
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
   * Returns an InputStream that contains the content referenced by a link.
   *
   * @param link link that references the target resource.
   * @return input stream that can be used to access the resource content.
   * @throws IOException error communication with the remote service.
   * @throws ServiceException resource access failed due to system error.
   *
   * @deprecated Use {@link #createLinkQueryRequest(ILink)} instead.
   */
  @Deprecated
  public InputStream getStreamFromLink(ILink link)
      throws IOException, ServiceException {
    GDataRequest request = createLinkQueryRequest(link);

    request.execute();
    InputStream resultStream = request.getResponseStream();

    return resultStream;
  }

  /**
   * Returns a query (GET) request that targets the provided link.   This
   * can be used to execute the request and access the link's content via
   * the response stream of the request (if successful).
   * <p>
   * Clients should be sure to call {@link GDataRequest#end()} on the
   * returned request once they have finished using it.
   *
   * @param link link to target resource for created request
   * @return query request to retrieve linked content.
   * @throws IOException error communicating with the GData service.
   * @throws ServiceException creation of query request failed.
   */
  public GDataRequest createLinkQueryRequest(ILink link)
  throws IOException, ServiceException {
    return createRequest(GDataRequest.RequestType.QUERY,
        new URL(link.getHref()), null);
  }

  /**
   * The ClientStreamProperties class is an abstract adaptor class that
   * implements the {@link StreamProperties} interface for content to be written
   * to or read from the target service based upon its attributes and a
   * {@link GDataRequest}.
   * <p>
   * Subclasses must implement the {@link StreamProperties#getContentType()}
   * method since the expected content type depends on the direction of data
   * transfer for the request.
   */
  protected abstract class ClientStreamProperties implements StreamProperties {

    protected final GDataRequest req;
    protected final UriParameterMap queryMap;

    protected ClientStreamProperties(GDataRequest req) {
      this.queryMap = computeQueryMap(req);
      this.req = req;
    }

    protected ClientStreamProperties() {
      this.queryMap = UriParameterMap.EMPTY_MAP;
      this.req = null;
    }

    public GDataRequest getGDataRequest() {
      return req;
    }

    public Version getRequestVersion() {
      return getProtocolVersion();
    }

    public AltRegistry getAltRegistry() {
      return Service.this.getAltRegistry();
    }

    public boolean isPartial() {
      return false;
    }

    public ExtensionProfile getExtensionProfile() {
      return Service.this.getExtensionProfile();
    }

    /**
     * Returns a {@link MetadataContext} based upon the alt format and version
     * of the request associated with these properties.  This can be used by
     * subclasses to bind root element metadata for request or response types.
     */
    protected MetadataContext getMetadataContext() {

      // There's no way to identify the current request projection in
      // the client. This is acceptable because we can still use the full model
      // to parse since the client code doesn't validate server responses.
      // Similarly for data sent by the client, the caller will generally only
      // populate the fields used in the projection so it's OK too, but if they
      // did populate extraneous fields they will be sent to the server but they
      // should be ignored there as well.   In both cases, there's an implicit
      // assumption that a projection is a structurally equivalent subset of the
      // full representation, which seems reasonable.
      return MetadataContext.forContext(getAltFormat(), null,
          getProtocolVersion());
    }

    public Collection<String> getQueryParameterNames() {
      return queryMap.keySet();
    }

    public String getQueryParameter(String name) {
      return queryMap.getFirst(name);
    }

    /**
     * Returns the {@link AltFormat} describing the representation used for
     * the current request.
     *
     * @return alternate representation format for current request
     */
    protected AltFormat getAltFormat() {
      String altName = queryMap.getFirst(GDataProtocol.Parameter.ALT);
      AltFormat altFormat = getAltRegistry().lookupName(altName);
      return altFormat != null ? altFormat : AltFormat.ATOM;
    }

    /**
     * Returns the {@link UriParameterMap} containing the decoded query
     * parameters for the current request.
     *
     * @return query parameter map containing decoded query parameters
     */
    protected UriParameterMap getParameterMap() {
      return queryMap;
    }
  }

  /**
   * The ClientInputProperties class is an adaptor class that implements the
   * {@link InputProperties} interface for content to be read from the target
   * service based upon its attributes and a {@link GDataRequest}.
   */
  protected class ClientInputProperties extends ClientStreamProperties
      implements InputProperties {

    private final Class<?> expectType;
    protected final ContentType inputType;
    private ElementMetadata<?, ?> elementMetadata;

    protected ClientInputProperties(GDataRequest req, Class<?> expectType)
        throws IOException, ServiceException {
      super(req);
      this.expectType = expectType;
      this.inputType = req.getResponseContentType();
      init();
    }

    protected ClientInputProperties(ContentType inputType, Class<?> expectType)
        throws IOException, ServiceException {
      this.expectType = expectType;
      this.inputType = inputType;
      init();
    }

    private void init() {
      if (Element.class.isAssignableFrom(expectType)) {
        ElementKey<?, ?> key =
            Element.getDefaultKey(expectType.asSubclass(Element.class));
        elementMetadata = getSchema().bind(key, getMetadataContext());
      } else {
        elementMetadata = null;
      }
    }

    public ContentType getContentType() {
      return inputType;
    }

    public Class<?> getRootType() {
      return expectType;
    }

    public ElementMetadata<?, ?> getRootMetadata() {
      return elementMetadata;
    }

    @Override
    public boolean isPartial() {
      return getQueryParameter(GDataProtocol.Query.FIELDS) != null;
    }
  }

  /**
   * The ClientOutputProperties class is an adaptor class that implements
   * the {@link OutputProperties} interface for content to be written to the
   * target service based upon its attributes and a {@link GDataRequest}.
   */
  public class ClientOutputProperties extends ClientStreamProperties
      implements OutputProperties {

    protected final ContentType requestType;
    private ElementMetadata<?, ?> elementMetadata;

    public ClientOutputProperties(GDataRequest req, Object source) {
      super(req);
      this.requestType = req.getRequestContentType();
      init(source);
    }

    public ClientOutputProperties(ContentType requestType, Object source) {
      this.requestType = requestType;
      init(source);
    }

    private void init(Object source) {
      if (source instanceof Element) {
        Element element = (Element) source;
        ElementKey<?, ?> key = element.getElementKey();
        elementMetadata = getSchema().bind(key, getMetadataContext());
      } else {
        elementMetadata = null;
      }
    }

    public ContentType getContentType() {
      return requestType;
    }

    public ElementMetadata<?, ?> getRootMetadata() {
      return elementMetadata;
    }

    public String getCallback() {
      return null;
    }
  }

  /**
   * Writes the request body to the target service based upon the attributes of
   * the request and the source object.
   *
   * @param req currently executing request
   * @param source source object to be written
   * @throws IOException
   */
  public void writeRequestData(GDataRequest req, Object source)
      throws IOException {
    writeRequestData(req, new ClientOutputProperties(req, source), source);
  }

  /**
   * Writes the request body to the target service based upon requested
   * output properties and the source object.
   *
   * @param outProps client output properties
   * @param source source object to be written
   * @throws IOException
   */
  protected void writeRequestData(GDataRequest req,
      ClientOutputProperties outProps, Object source) throws IOException {

    AltFormat outputFormat = altRegistry.lookupType(outProps.getContentType());
    if (outputFormat == null) {
      // If no registered type, see if the target service supports media
      outputFormat = altRegistry.lookupName(AltFormat.MEDIA.getName());
    }
    if (outputFormat == null) {
      throw new IllegalStateException("Unsupported request type: " +
          outProps.getContentType());
    }
    OutputGenerator<?> generator = altRegistry.getGenerator(outputFormat);
    if (!generator.getSourceType().isAssignableFrom(source.getClass())) {
      throw new IllegalArgumentException("Invalid source type: " +
          "expected: " + generator.getSourceType() + " but got: " +
          source.getClass() + " for output format " + outputFormat);
    }

    // The cast here is safe because of the runtime check above
    @SuppressWarnings("unchecked")
    OutputGenerator<Object> typedGenerator =
        (OutputGenerator<Object>) generator;

    // If request type is partial, disable strict validation
    boolean disableValidation = !strictValidation
        || outputFormat.equals(AltFormat.APPLICATION_XML);
    if (disableValidation) {
      AbstractExtension.disableStrictValidation();
    }
    try {
      typedGenerator.generate(req.getRequestStream(), outProps, source);
    } finally {
      // If request type is partial, renable strict validation
      if (disableValidation) {
        AbstractExtension.enableStrictValidation();
      }
    }

  }

  /**
   * Parses the response stream for a request based upon request properties and
   * an expected result type.   The parser will be selected based upon the
   * request alt type or response content type and used to parse the response
   * content into the result object.
   *
   * @param <E> expected result type
   * @param req request that has been executed but not yet read from.
   * @param resultType expected result type, not {@code null}.
   * @return an instance of the expected result type resulting from the parse.
   * @throws IOException
   * @throws ServiceException
   */
  public <E> E parseResponseData(GDataRequest req, Class<E> resultType)
      throws IOException, ServiceException {
    InputProperties inputProperties =
        new ClientInputProperties(req, resultType);
    return parseResponseData(
        req.getParseSource(), inputProperties, resultType);
  }

  /**
   * Parses the response stream for a request based upon response content type
   * and an expected result type.   The parser will be selected based upon the
   * request alt type or response content type and used to parse the response
   * content into the result object.
   *
   * @param <E> expected result type
   * @param responseType content type of the response to parse.
   * @param resultType expected result type, not {@code null}.
   * @return an instance of the expected result type resulting from the parse.
   * @throws IOException
   * @throws ServiceException
   */
  protected <E> E parseResponseData(
      ParseSource source, ContentType responseType, Class<E> resultType)
      throws IOException, ServiceException {
    InputProperties inputProperties =
        new ClientInputProperties(responseType, resultType);
    return parseResponseData(source, inputProperties, resultType);
  }

  private <E> E parseResponseData(
      ParseSource source, InputProperties inputProperties, Class<E> resultType)
      throws IOException, ServiceException {
    Preconditions.checkNotNull("resultType", resultType);

    AltFormat inputFormat = null;
    String alt = inputProperties.getQueryParameter(GDataProtocol.Parameter.ALT);
    if (alt != null) {
      inputFormat = altRegistry.lookupName(alt);
    }
    if (inputFormat == null) {
      inputFormat = altRegistry.lookupType(inputProperties.getContentType());
      if (inputFormat == null) {
        throw new ParseException("Unrecognized content type:" +
            inputProperties.getContentType());
      }
    }
    InputParser<?> inputParser = altRegistry.getParser(inputFormat);
    if (inputParser == null) {
      throw new ParseException("No parser for content type:" + inputFormat);
    }

    if (!inputParser.getResultType().isAssignableFrom(resultType)) {
      throw new IllegalStateException("Input parser (" + inputParser +
          ") does not produce expected result type: " + resultType);
    }

    // The cast here is safe because of the runtime check above
    @SuppressWarnings("unchecked")
    InputParser<E> typedParser = (InputParser<E>) inputParser;

    // Disable validation for partial request in old data model.
    String fields =
        inputProperties.getQueryParameter(GDataProtocol.Parameter.FIELDS);
    boolean disableValidation = !strictValidation
        || (fields != null && !Element.class.isAssignableFrom(resultType));
    if (disableValidation) {
      AbstractExtension.disableStrictValidation();
    }

    E result;
    try {
      result = typedParser.parse(source, inputProperties, resultType);
    } finally {
      // Re-enable validation.
      if (disableValidation) {
        AbstractExtension.enableStrictValidation();
      }
    }

    // Associate service with the result if atom content
    if (result instanceof IAtom) {
      ((IAtom) result).setService(this);
    }
    return result;
  }

  /**
   * Computes a {@link UriParameterMap} containing all query parameters passed
   * in a request.
   *
   * @param req request to parse
   * @return parameter map containing parsed and decoded query parameters
   */
  private static UriParameterMap computeQueryMap(GDataRequest req) {
    String query = req.getRequestUrl().getQuery();
    if (query == null) {
      return UriParameterMap.EMPTY_MAP;
    }
    return UriParameterMap.parse(query);
  }
}
