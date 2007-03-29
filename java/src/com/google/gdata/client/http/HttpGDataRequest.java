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


package com.google.gdata.client.http;

import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequestFactory;
import com.google.gdata.data.DateTime;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.NotImplementedException;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.ServiceForbiddenException;
import com.google.gdata.util.VersionConflictException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;


/**
 * The HttpGDataRequest class provides a basic implemention of the
 * <code>GDataRequest</code> interface over HTTP.
 *
 * @see GDataRequest
 */
public class HttpGDataRequest implements GDataRequest {

  private static final Logger logger =
          Logger.getLogger(HttpGDataRequest.class.getName());


  /**
   * If this system property is set to <code>true</code>, the GData HTTP
   * client library will use POST to send data to the associated GData service
   * and will specify the actual method using the {@code METHOD_OVERRIDE_HEADER}
   * HTTP header. This can be used as a workaround for HTTP proxies or gateways
   * that do not handle PUT or DELETE HTTP methods properly.  If the system
   * property is <code>false</code>, the regular PUT and DELETE HTTP verbs will
   * be used.
   */
  public static final String METHOD_OVERRIDE_PROPERTY =
    "com.google.gdata.UseMethodOverride";


  /**
   * Name of HTTP header containing the method name that overrides
   * the normal HTTP method. This is used to allow clients that are
   * unable to issue PUT or DELETE methods to emulate such methods.
   * The client would issue a POST method with this header set to
   * PUT or DELETE, and the service translates the invocation to
   * the corresponding request type.
   */
  public static final String METHOD_OVERRIDE_HEADER =
    "X-HTTP-Method-Override";

  /**
   * The HttpGDataRequest.Factory class is a factory class for constructing
   * new HttpGDataRequest instances.
   */
  public static class Factory implements GDataRequestFactory {

    protected AuthToken authToken;
    protected Map<String, String> headerMap
        = new LinkedHashMap<String, String>();
    protected Map<String, String> privateHeaderMap
        = new LinkedHashMap<String, String>();

    public void setAuthToken(AuthToken authToken) {
      this.authToken = authToken;
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

    public GDataRequest getRequest(RequestType type,
                                   URL requestUrl,
                                   ContentType contentType)
        throws IOException, ServiceException {
      return new HttpGDataRequest(type, requestUrl, contentType,
                                  authToken, headerMap, privateHeaderMap);
    }
  }


  /**
   * The HttpGDataRequest.AuthToken interface represents a token used to
   * authenticate a request.  It encapsulates the functionality to create
   * the "Authorization" header to be appended to a HTTP request.
   */
  public static interface AuthToken {

    /**
     * Returns an authorization header to be used for a HTTP request
     * for the respective authentication token.
     *
     * @param requestUrl the URL being requested
     * @param requestMethod the HTTP method of the request
     * @return the "Authorization" header to be used for the request
     */
    public String getAuthorizationHeader(URL requestUrl,
                                         String requestMethod);
  }


  /**
   * Underlying HTTP connection to the GData service.
   */
  protected HttpURLConnection httpConn;

  /**
   * The request URL provided by the client.
   */
  protected URL requestUrl;

  /**
   * The GData request "verb", one of GET, QUERY, INSERT, UPDATE or DELETE
   */
  protected RequestType type;


  /**
   * Indicates whether request execution has taken place.  Set to
   * <code>true</code> if executed, <code>false</code> otherwise.
   */
  protected boolean executed = false;


  /**
   * True if the request type expects input from the client.
   */
  protected boolean expectsInput;


  /**
   * True if the request type returns output to the client.
   */
  protected boolean hasOutput;


  /**
   * Contains the IfModifiedSince precondition to be applied to the
   * request.
   */
  protected DateTime ifModifiedCondition;


  /**
   * The connection timeout for this request.   A value of -1 means no
   * value has been configured (use JDK default timeout behavior).
   */
  protected int connectTimeout = -1;


  /**
   * The read timeout for this request.   A value of -1 means no
   * value has been configured (use JDK default timeout behavior).
   */
  protected int readTimeout = -1;


  /**
   * Constructs a new HttpGDataRequest instance of the specified RequestType,
   * targeting the specified URL.
   *
   * @param type type of GDataRequest.
   * @param requestUrl request target URL.
   * @param contentType the content type of request/response data.
   * @param headerMap a set of headers to be included in each request
   * @param privateHeaderMap a set of headers to be included in each request
   * @throws IOException on error initializating service connection.
   */
  protected HttpGDataRequest(RequestType type,
                             URL requestUrl,
                             ContentType contentType,
                             AuthToken authToken,
                             Map<String, String> headerMap,
                             Map<String, String> privateHeaderMap)
      throws IOException {

    this.type = type;
    this.requestUrl = requestUrl;
    httpConn = getRequestConnection(requestUrl);

    switch(type) {

      case QUERY:
        hasOutput = true;
        break;

      case INSERT:
      case BATCH:
        expectsInput = true;
        hasOutput = true;
        setMethod("POST");
        setHeader("Content-Type", contentType.toString());
        break;

      case UPDATE:
        expectsInput = true;
        hasOutput = true;
        if (Boolean.getBoolean(METHOD_OVERRIDE_PROPERTY)) {
          setMethod("POST");
          setHeader(METHOD_OVERRIDE_HEADER, "PUT");
        } else {
          setMethod("PUT");
        }
        setHeader("Content-Type", contentType.toString());
        break;

      case DELETE:
        if (Boolean.getBoolean(METHOD_OVERRIDE_PROPERTY)) {
          setMethod("POST");
          setHeader(METHOD_OVERRIDE_HEADER, "DELETE");
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
        authToken.getAuthorizationHeader(requestUrl,
                                         httpConn.getRequestMethod());
      setPrivateHeader("Authorization", authHeader);
    }

    if (headerMap != null) {
      for (String h : headerMap.keySet()) {
        setHeader(h, headerMap.get(h));
      }
    }

    if (privateHeaderMap != null) {
      for (String h : privateHeaderMap.keySet()) {
        setPrivateHeader(h, privateHeaderMap.get(h));
      }
    }

    // Request compressed response format
    setHeader("Accept-Encoding", "gzip");

    httpConn.setDoOutput(expectsInput);
  }


  /**
   * Obtains a connection to the GData service.
   */
  protected HttpURLConnection getRequestConnection(URL requestUrl)
      throws IOException {

    if (!requestUrl.getProtocol().startsWith("http")) {
      throw new UnsupportedOperationException("Unsupported scheme:" +
                                              requestUrl.getProtocol());
    }
    HttpURLConnection uc = (HttpURLConnection)requestUrl.openConnection();

    // Should never cache GData requests/responses
    uc.setUseCaches(false);

    // Always follow redirects
    uc.setInstanceFollowRedirects(true);

    return (HttpURLConnection) uc;
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
  public void setConnectTimeout(int timeout) {
    if (timeout < 0) {
      throw new IllegalArgumentException("Timeout cannot be negative");
    }
    connectTimeout = timeout;
  }


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
  public void setReadTimeout(int timeout) {
    if (timeout < 0) {
      throw new IllegalArgumentException("Timeout cannot be negative");
    }
    readTimeout = timeout;
  }


  /**
   * Sets the If-Modified-Since date precondition to be applied to the
   * request.  If this precondition is set, then the request will be
   * performed only if the target resource has been modified since the
   * specified date; otherwise, a {@code NotModifiedException} will be
   * thrown.
   */
  public void setIfModifiedSince(DateTime conditionDate) {
    this.ifModifiedCondition = conditionDate;
  }


  /**
   * Returns a stream that can be used to write request data to the
   * GData service.
   *
   * @return OutputStream that can be used to write GData request data.
   * @throws IOException error obtaining the request output stream.
   */
  public OutputStream getRequestStream() throws IOException {

    if (!expectsInput) {
      throw new IllegalStateException("Request doesn't accept input");
    }
    return httpConn.getOutputStream();
  }


  /**
   * Sets request method (and logs it, if enabled)
   */
  private void setMethod(String method) throws ProtocolException {
    httpConn.setRequestMethod(method);
    if (logger.isLoggable(Level.FINE)) {
      logger.fine(method + " " + httpConn.getURL().toExternalForm());
    }
  }

  /**
   * Sets request header (and logs it, if enabled)
   */
  public void setHeader(String name, String value) {
    httpConn.setRequestProperty(name, value);
    logger.finer(name + ": " + value);
  }


  /**
   * Sets request header and log just it's name, not it's value
   */
  public void setPrivateHeader(String name, String value) {
    httpConn.setRequestProperty(name, value);
    logger.finer(name + ": <Not Logged>");
  }


  /**
   * Executes the GData service request.
   *
   * @throws IOException error writing to or reading from GData service.
   * @throws ServiceException service invocation error.
   */
  public void execute() throws IOException, ServiceException {

    if (connectTimeout >= 0) {
      httpConn.setConnectTimeout(connectTimeout);
    }

    if (readTimeout >= 0) {
      httpConn.setReadTimeout(readTimeout);
    }

    if (ifModifiedCondition != null) {
      setHeader("If-Modified-Since", ifModifiedCondition.toStringRfc822());
    }

    // Set the http.strictPostRedirect property to prevent redirected
    // POST/PUT/DELETE from being mapped to a GET.  This
    // system property was a hack to fix a jdk bug w/out changing back
    // compat behavior.  It's bogus that this is a system (and not a
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
        // because URL.equals() requires DNS resolution.  This test will
        // fail on the first check for any URLConnection implementation
        // that derives from java.net.URLConnection.  The 2nd check would
        // work on an alternate impl that clones the URL.
        if (httpConn.getURL() != requestUrl &&
            !httpConn.getURL().toExternalForm().equals(
                requestUrl.toExternalForm())) {
          logger.fine("Redirected to:" + httpConn.getURL().toExternalForm());
        }

        // Log response information here, if enabled
        logger.fine(httpConn.getResponseCode() + " " +
                    httpConn.getResponseMessage());
        if (logger.isLoggable(Level.FINER)) {
          for (Map.Entry<String,List<String>> headerField:
               httpConn.getHeaderFields().entrySet()) {
            for (String value: headerField.getValue()) {
              logger.finer(headerField.getKey() + ": " + value);
            }
          }
        }
      }
      checkResponse();  // will flush any request data

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
   * Called after a request is executed to process the response and
   * generate an appropriate exception (on failure).
   */
  protected void checkResponse()
      throws IOException, ServiceException {

    if (httpConn.getResponseCode() >= 300) {
      handleErrorResponse();
    }
  }


  /**
   * Handles an error response received while executing a GData service
   * request.  Throws a {@link ServiceException} or one of its subclasses,
   * depending on the failure conditions.
   * @throws ServiceException exception describing the failure.
   * @throws IOException error reading the error response from the
   *         GData service.
   */
  protected void handleErrorResponse()
      throws ServiceException, IOException {

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

        case HttpURLConnection.HTTP_NOT_IMPLEMENTED:
          throw new NotImplementedException(httpConn);

        case HttpURLConnection.HTTP_CONFLICT:
          throw new VersionConflictException(httpConn);

        default:
          throw new ServiceException(httpConn);
      }
  }

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
  public ContentType getResponseContentType() throws IOException {

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

  /**
   * Returns a stream that can be used to read response data from the
   * GData service.
   *
   * @return InputStream providing access to GData response data.
   * @throws IllegalStateException if attemping to read response without
   *                               first calling {@link #execute()}.
   * @throws IOException error obtaining the response input stream.
   */
  public InputStream getResponseStream() throws IOException {

    if (!executed) {
      throw new IllegalStateException(
        "Must call execute() before attempting to read response");
    }

    if (!hasOutput) {
      throw new IllegalStateException("Request doesn't have response data");
    }

    InputStream responseStream = httpConn.getInputStream();
    if ("gzip".equalsIgnoreCase(httpConn.getContentEncoding())) {
      responseStream = new GZIPInputStream(responseStream);
    }
    return responseStream;
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
}
