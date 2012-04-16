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


package com.google.gdata.client.http;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GDataProtocol;
import com.google.gdata.client.Query;
import com.google.gdata.client.GDataProtocol.Header;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequestFactory;
import com.google.gdata.client.authn.oauthproxy.OAuthProxyProtocol;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ParseSource;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.EntityTooLargeException;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.LoggableInputStream;
import com.google.gdata.util.LoggableOutputStream;
import com.google.gdata.util.NoLongerAvailableException;
import com.google.gdata.util.NotAcceptableException;
import com.google.gdata.util.NotImplementedException;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.OAuthProxyException;
import com.google.gdata.util.PreconditionFailedException;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.ServiceForbiddenException;
import com.google.gdata.util.VersionConflictException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

/**
 * The HttpGDataRequest class provides a basic implementation of the
 * <code>GDataRequest</code> interface over HTTP.
 *
 * @see GDataRequest
 */
public class HttpGDataRequest implements GDataRequest {

  static final Logger logger =
      Logger.getLogger(HttpGDataRequest.class.getName());

  /**
   * If this system property is set to <code>true</code>, the GData HTTP
   * client library will use POST to send data to the associated GData service
   * and will specify the actual method using the {@code METHOD_OVERRIDE_HEADER}
   * HTTP header. This can be used as a workaround for HTTP proxies or gateways
   * that do not handle PUT or DELETE HTTP methods properly. If the system
   * property is <code>false</code>, the regular PUT and DELETE HTTP verbs
   * will be used.
   */
  public static final String METHOD_OVERRIDE_PROPERTY =
      "com.google.gdata.UseMethodOverride";


  /**
   * Name of HTTP header containing the method name that overrides the normal
   * HTTP method.
   *
   * @deprecated Use {@link GDataProtocol.Header#METHOD_OVERRIDE} instead
   */
  @Deprecated
  public static final String METHOD_OVERRIDE_HEADER =
      GDataProtocol.Header.METHOD_OVERRIDE;


  /**
   * The HttpGDataRequest.Factory class is a factory class for constructing
   * new HttpGDataRequest instances.
   */
  public static class Factory implements GDataRequestFactory {

    protected HttpAuthToken authToken;
    protected Map<String, String> headerMap
        = new LinkedHashMap<String, String>();
    protected Map<String, String> privateHeaderMap
        = new LinkedHashMap<String, String>();
    protected boolean useSsl = false;
    protected HttpUrlConnectionSource connectionSource =
        JdkHttpUrlConnectionSource.INSTANCE;

    public void setAuthToken(AuthTokenFactory.AuthToken authToken) {
      if (authToken != null && !(authToken instanceof HttpAuthToken)) {
        throw new IllegalArgumentException("Invalid token type");
      }
      setAuthToken((HttpAuthToken) authToken);
    }

    public void setAuthToken(HttpAuthToken authToken) {
      this.authToken = authToken;
    }

    public void useSsl() {
      this.useSsl = true;
    }

    private void extendHeaderMap(Map<String, String> headerMap,
                                 String header, String value) {
      if (value == null) {
        headerMap.remove(header);
      } else {
        headerMap.put(header, value);
      }
    }

    public void setHeader(String header, String value) {
      extendHeaderMap(this.headerMap, header, value);
    }

    public void setPrivateHeader(String header, String value) {
      extendHeaderMap(this.privateHeaderMap, header, value);
    }

    /**
     * Sets a specific {@link HttpUrlConnectionSource} instance to create
     * backing {@link URLConnection} instance.
     */
    public void setConnectionSource(HttpUrlConnectionSource connectionSource) {
      if (connectionSource == null) {
        throw new NullPointerException("connectionSource");
      }
      this.connectionSource = connectionSource;
    }

    @SuppressWarnings("unused")
    public GDataRequest getRequest(RequestType type,
                                   URL requestUrl,
                                   ContentType contentType)
        throws IOException, ServiceException {
      if (this.useSsl && !requestUrl.getProtocol().startsWith("https")) {
        requestUrl = new URL(
            requestUrl.toString().replaceFirst("http", "https"));
      }
      return createRequest(type, requestUrl, contentType);
    }

    @SuppressWarnings("unused")
    public GDataRequest getRequest(Query query, ContentType contentType)
        throws IOException, ServiceException {
      return getRequest(RequestType.QUERY, query.getUrl(), contentType);
    }

    /**
     * Creates a {@link GDataRequest} instance.
     *
     * <p>This method is called from {@link #getRequest} after any changes to
     * the parameters have been applied.
     *
     * <p>Subclasses should overwrite this method and not {@link #getRequest}
     */
    protected GDataRequest createRequest(RequestType type,
        URL requestUrl, ContentType contentType)
        throws IOException, ServiceException {
      return new HttpGDataRequest(type, requestUrl, contentType,
          authToken, headerMap, privateHeaderMap, connectionSource);
    }
  }

  /**
   * Source of {@link HttpURLConnection} instances.
   */
  protected final HttpUrlConnectionSource connectionSource;

  /**
   * Underlying HTTP connection to the GData service.
   */
  protected HttpURLConnection httpConn;

  /**
   * The request URL provided by the client.
   */
  protected URL requestUrl;

  /**
   * The GData request type.
   */
  protected RequestType type;


  /**
   * Indicates whether request execution has taken place. Set to
   * <code>true</code> if executed, <code>false</code> otherwise.
   */
  protected boolean executed = false;


  /**
   * True if the request type expects input from the client.
   */
  protected boolean expectsInput;

  /**
   * Contains the content type of the request data
   */
  protected ContentType inputType;

  /**
   * True if the request type returns output to the client.
   */
  protected boolean hasOutput;


  /**
   * The connection timeout for this request. A value of -1 means no value has
   * been configured (use JDK default timeout behavior).
   */
  protected int connectTimeout = -1;


  /**
   * The read timeout for this request. A value of -1 means no value has been
   * configured (use JDK default timeout behavior).
   */
  protected int readTimeout = -1;

  /**
   * The auth token used for this request, may be {@code null}.
   */
  protected final HttpAuthToken authToken;

  /**
   * The input stream from which HTTP response data may be read or {@code null}
   * if no response stream or not opened yet via {@link #getResponseStream()}.
   */
  private InputStream inputStream = null;

  /**
   * Constructs a new HttpGDataRequest instance of the specified RequestType,
   * targeting the specified URL.
   *
   * @param type type of GDataRequest.
   * @param requestUrl request target URL.
   * @param inputType the content type of request data (or {@code null}).
   * @param authToken the auth token used for this request (or {@code null}).
   * @param headerMap a set of headers to be included in each request
   * @param privateHeaderMap a set of headers to be included in each request
   * @param connectionSource source of {@link HttpURLConnection}s
   * @throws IOException on error initializating service connection.
   */
  protected HttpGDataRequest(RequestType type, URL requestUrl,
      ContentType inputType, HttpAuthToken authToken,
      Map<String, String> headerMap, Map<String, String> privateHeaderMap,
      HttpUrlConnectionSource connectionSource)
      throws IOException {

    this.connectionSource = connectionSource;
    this.type = type;
    this.inputType = inputType;
    this.requestUrl = requestUrl;
    this.httpConn = getRequestConnection(requestUrl);
    this.authToken = authToken;

    switch (type) {

      case QUERY:
        hasOutput = true;
        break;

      case INSERT:
      case BATCH:
        expectsInput = true;
        hasOutput = true;
        setMethod("POST");
        setHeader("Content-Type", inputType.toString());
        break;

      case UPDATE:
        expectsInput = true;
        hasOutput = true;
        if (Boolean.getBoolean(METHOD_OVERRIDE_PROPERTY)) {
          setMethod("POST");
          setHeader(Header.METHOD_OVERRIDE, "PUT");
        } else {
          setMethod("PUT");
        }
        setHeader("Content-Type", inputType.toString());
        break;

      case PATCH:
        expectsInput = true;
        hasOutput = true;

        // HTTPUrlConnection does not accept unrecognized methods, so always use
        // the POST override model for PATCH requests
        setMethod("POST");
        setHeader(Header.METHOD_OVERRIDE, "PATCH");
        setHeader("Content-Type", inputType.toString());
        break;

      case DELETE:
        if (Boolean.getBoolean(METHOD_OVERRIDE_PROPERTY)) {
          setMethod("POST");
          setHeader(Header.METHOD_OVERRIDE, "DELETE");
        } else {
          setMethod("DELETE");
        }
        setHeader("Content-Length", "0"); // no data to POST
        break;

      default:
        throw new UnsupportedOperationException("Unknown request type:" + type);
    }

    if (authToken != null) {
      // NOTE: Do not use setHeader() here, authorization should never be
      // logged.
      String authHeader =
          authToken.getAuthorizationHeader(requestUrl, httpConn
              .getRequestMethod());
      setPrivateHeader("Authorization", authHeader);
    }

    if (headerMap != null) {
      for (Map.Entry<String, String> e : headerMap.entrySet()) {
        setHeader(e.getKey(), e.getValue());
      }
    }

    if (privateHeaderMap != null) {
      for (Map.Entry<String, String> e : privateHeaderMap.entrySet()) {
        setPrivateHeader(e.getKey(), e.getValue());
      }
    }

    // Request compressed response format
    setHeader("Accept-Encoding", "gzip");

    httpConn.setDoOutput(expectsInput);
  }

  /**
   * Protected default constructor for testing.
   */
  protected HttpGDataRequest() {
    connectionSource = JdkHttpUrlConnectionSource.INSTANCE;
    this.authToken = null;
  }

  public URL getRequestUrl() {
    return requestUrl;
  }

  public ContentType getRequestContentType() {
    return inputType;
  }

  /**
   * Obtains a connection to the GData service.
   */
  protected HttpURLConnection getRequestConnection(URL requestUrl)
      throws IOException {

    HttpURLConnection uc;
    try {
      uc = connectionSource.openConnection(requestUrl);
    } catch (IllegalArgumentException e) {
      throw new UnsupportedOperationException("Unsupported scheme:"
          + requestUrl.getProtocol());
    }

    // Should never cache GData requests/responses
    uc.setUseCaches(false);

    // Always follow redirects
    uc.setInstanceFollowRedirects(true);

    return uc;
  }

  public void setConnectTimeout(int timeout) {
    if (timeout < 0) {
      throw new IllegalArgumentException("Timeout cannot be negative");
    }
    connectTimeout = timeout;
  }

  public void setReadTimeout(int timeout) {
    if (timeout < 0) {
      throw new IllegalArgumentException("Timeout cannot be negative");
    }
    readTimeout = timeout;
  }

  public void setIfModifiedSince(DateTime conditionDate) {
    if (conditionDate == null) {
      return;
    }

    if (type == RequestType.QUERY) {
      setHeader(GDataProtocol.Header.IF_MODIFIED_SINCE,
          conditionDate.toStringRfc822());
    } else {
      throw new IllegalStateException(
          "Date conditions not supported for this request type");
    }
  }

  public void setEtag(String etag) {

    if (etag == null) {
      return;
    }

    switch (type) {
      case QUERY:
        if (etag != null) {
          setHeader(GDataProtocol.Header.IF_NONE_MATCH, etag);
        }
        break;
      case PATCH:
      case UPDATE:
      case DELETE:
        if (etag != null) {
          setHeader(GDataProtocol.Header.IF_MATCH, etag);
        }
        break;
      default:
        throw new IllegalStateException(
            "Etag conditions not supported for this request type");
    }
  }

  public OutputStream getRequestStream() throws IOException {

    if (!expectsInput) {
      throw new IllegalStateException("Request doesn't accept input");
    }
    if (logger.isLoggable(Level.FINEST)){
      return new LoggableOutputStream(logger, httpConn.getOutputStream());
    }
    return httpConn.getOutputStream();
  }


  public XmlWriter getRequestWriter() throws IOException {
    OutputStream requestStream = getRequestStream();
    Writer writer = new OutputStreamWriter(requestStream, "utf-8");
    return new XmlWriter(writer);
  }

  public void setMethod(String method) throws ProtocolException {
    httpConn.setRequestMethod(method);
    if (logger.isLoggable(Level.FINE)) {
      logger.fine(method + " " + httpConn.getURL().toExternalForm());
    }
  }

  public void setHeader(String name, String value) {
    httpConn.setRequestProperty(name, value);
    logger.finer(name + ": " + value);
  }


  public void setPrivateHeader(String name, String value) {
    httpConn.setRequestProperty(name, value);
    logger.finer(name + ": <Not Logged>");
  }

  public void execute() throws IOException, ServiceException {

    if (connectTimeout >= 0) {
      httpConn.setConnectTimeout(connectTimeout);
    }

    if (readTimeout >= 0) {
      httpConn.setReadTimeout(readTimeout);
    }

    // Set the http.strictPostRedirect property to prevent redirected
    // POST/PUT/DELETE from being mapped to a GET. This
    // system property was a hack to fix a jdk bug w/out changing back
    // compat behavior. It's bogus that this is a system (and not a
    // per-connection) property, so we just change it for the duration
    // of the connection.
    // See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4023866
    String httpStrictPostRedirect =
        System.getProperty("http.strictPostRedirect");
    try {
      System.setProperty("http.strictPostRedirect", "true");
      httpConn.connect();

      if (logger.isLoggable(Level.FINE)) {

        // Avoid calling URL.equals() unless an object equivalence test fails,
        // because URL.equals() requires DNS resolution. This test will
        // fail on the first check for any URLConnection implementation
        // that derives from java.net.URLConnection. The 2nd check would
        // work on an alternate impl that clones the URL.
        if (httpConn.getURL() != requestUrl
            && !httpConn.getURL().toExternalForm().equals(
                requestUrl.toExternalForm())) {
          logger.fine("Redirected to:" + httpConn.getURL().toExternalForm());
        }

        // Log response information here, if enabled
        logger.fine(httpConn.getResponseCode() + " "
            + httpConn.getResponseMessage());
        if (logger.isLoggable(Level.FINER)) {
          for (Map.Entry<String, List<String>> headerField : httpConn
              .getHeaderFields().entrySet()) {
            for (String value : headerField.getValue()) {
              logger.finer(headerField.getKey() + ": " + value);
            }
          }
        }
      }
      checkResponse(); // will flush any request data

    } finally {
      if (httpStrictPostRedirect == null) {
        System.clearProperty("http.strictPostRedirect");
      } else {
        System.setProperty("http.strictPostRedirect", httpStrictPostRedirect);
      }
    }

    executed = true;
  }

  /**
   * Called after a request is executed to process the response and generate an
   * appropriate exception (on failure).
   */
  protected void checkResponse() throws IOException, ServiceException {

    if (isOAuthProxyErrorResponse()) {
      handleOAuthProxyErrorResponse();
    } else if (httpConn.getResponseCode() >= 300) {
      handleErrorResponse();
    }
  }

  /** Whether or not the http response comes from the OAuth Proxy. */
  private boolean isOAuthProxyErrorResponse() throws IOException {
    Set<String> headers = httpConn.getHeaderFields().keySet();
    boolean isOAuthRedirectToApproval =
        headers.contains(OAuthProxyProtocol.Header.X_OAUTH_APPROVAL_URL);
    boolean isOtherOAuthError =
        httpConn.getResponseCode() == HttpURLConnection.HTTP_OK
        && (headers.contains(OAuthProxyProtocol.Header.X_OAUTH_ERROR)
        || headers.contains(OAuthProxyProtocol.Header.X_OAUTH_ERROR_TEXT));
    return isOAuthRedirectToApproval || isOtherOAuthError;
  }

  /**
   * Sets the appropriate parameters used by the OAuth Proxy and throws an
   * {@link OAuthProxyException}.
   */
  private void handleOAuthProxyErrorResponse() throws IOException,
      ServiceException {
    throw new OAuthProxyException(httpConn);
  }

  /**
   * Handles an error response received while executing a GData service request.
   * Throws a {@link ServiceException} or one of its subclasses, depending on
   * the failure conditions.
   *
   * @throws ServiceException exception describing the failure.
   * @throws IOException error reading the error response from the GData
   *         service.
   */
  protected void handleErrorResponse() throws ServiceException, IOException {

    switch (httpConn.getResponseCode()) {

      case HttpURLConnection.HTTP_NOT_FOUND:
        throw new ResourceNotFoundException(httpConn);

      case HttpURLConnection.HTTP_BAD_REQUEST:
        throw new InvalidEntryException(httpConn);

      case HttpURLConnection.HTTP_FORBIDDEN:
        throw new ServiceForbiddenException(httpConn);

      case HttpURLConnection.HTTP_UNAUTHORIZED:
        throw new AuthenticationException(httpConn);

      case HttpURLConnection.HTTP_NOT_MODIFIED:
        throw new NotModifiedException(httpConn);

      case HttpURLConnection.HTTP_PRECON_FAILED:
        throw new PreconditionFailedException(httpConn);

      case HttpURLConnection.HTTP_NOT_IMPLEMENTED:
        throw new NotImplementedException(httpConn);

      case HttpURLConnection.HTTP_CONFLICT:
        throw new VersionConflictException(httpConn);

      case HttpURLConnection.HTTP_ENTITY_TOO_LARGE:
        throw new EntityTooLargeException(httpConn);

      case HttpURLConnection.HTTP_NOT_ACCEPTABLE:
        throw new NotAcceptableException(httpConn);

      case HttpURLConnection.HTTP_GONE:
        throw new NoLongerAvailableException(httpConn);

      default:
        throw new ServiceException(httpConn);
    }
  }

  public ContentType getResponseContentType() {

    if (!executed) {
      throw new IllegalStateException(
          "Must call execute() before attempting to read response");
    }
    String value = httpConn.getHeaderField("Content-Type");
    if (value == null) {
      return null;
    }
    return new ContentType(value);
  }

  public String getResponseHeader(String headerName) {
    return httpConn.getHeaderField(headerName);
  }

  public DateTime getResponseDateHeader(String headerName) {
    long dateValue = httpConn.getHeaderFieldDate(headerName, -1);
    return (dateValue >= 0) ? new DateTime(dateValue) : null;
  }

  public InputStream getResponseStream() throws IOException {

    if (!executed) {
      throw new IllegalStateException(
          "Must call execute() before attempting to read response");
    }

    if (!hasOutput) {
      throw new IllegalStateException("Request doesn't have response data");
    }

    if (inputStream != null) {
      return inputStream;
    }

    inputStream = httpConn.getInputStream();
    if ("gzip".equalsIgnoreCase(httpConn.getContentEncoding())) {
      inputStream = new GZIPInputStream(inputStream);
    }
    if (logger.isLoggable(Level.FINEST)){
      return new LoggableInputStream(logger, inputStream);
    }
    return inputStream;
  }

  public ParseSource getParseSource() throws IOException {
    return new ParseSource(getResponseStream());
  }

  /**
   * Returns the URLConnection instance that represents the underlying
   * connection to the GData service that will be used by this request.
   *
   * @return connection to GData service.
   */
  public HttpURLConnection getConnection() {
    return httpConn;
  }

  public void end() {
    try {
      if (inputStream != null) {
        inputStream.close();
      }
    } catch (IOException ioe) {
      logger.log(Level.WARNING, "Error closing response stream", ioe);
    }
  }
}
