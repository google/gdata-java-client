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

import com.google.gdata.client.GDataProtocol;
import com.google.gdata.client.GoogleAuthTokenFactory.OAuth2Token;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.GoogleService.SessionExpiredException;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.RedirectRequiredException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.Version;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


/**
 * The GoogleGDataRequest class provides a basic implementation of an
 * interface to connect with a Google-GData server.
 *
 * 
 */
public class GoogleGDataRequest extends HttpGDataRequest {

  private static final Logger logger =
          Logger.getLogger(GoogleGDataRequest.class.getName());


  /**
   * If set, this System property will globally disable interception and
   * handling of cookies for all GData services.
   */
  public static final String DISABLE_COOKIE_HANDLER_PROPERTY =
    "com.google.gdata.DisableCookieHandler";

  /*
   * Disables cookie handling when run in AppEngine. This is a no-op if run
   * outside of AppEngine.
   */
  static {
    try {
      Class apiProxyClass = Class.forName(
          "com.google.apphosting.api.ApiProxy");
      if (apiProxyClass.getMethod(
          "getCurrentEnvironment").invoke(null) != null) {
        System.setProperty(DISABLE_COOKIE_HANDLER_PROPERTY, "true");
      }
    } catch (ClassNotFoundException e) {
    } catch (IllegalAccessException e) {
    } catch (InvocationTargetException e) {
    } catch (NoSuchMethodException e) {
    }
  }

  /**
   * The GoogleGDataRequest.Factory class is a factory class for
   * constructing new GoogleGDataRequest instances.
   */
  public static class Factory extends HttpGDataRequest.Factory {
    @Override
    protected GDataRequest createRequest(RequestType type,
        URL requestUrl, ContentType contentType)
        throws IOException, ServiceException {
      return new GoogleGDataRequest(type, requestUrl, contentType, authToken,
          headerMap, privateHeaderMap, connectionSource);
    }
  }


  /**
   * Google cookie.
   */
  public static class GoogleCookie {

    // Cookie state.  All fields have public accessors, except for cookie
    // values which are restricted to package-level access for security.
    private String domain;
    public String getDomain() { return domain; }

    private String path;
    public String getPath() { return path; }

    private String name;
    public String getName() { return name; }

    private String value;
    String getValue() { return value; }

    private Date expires;
    public Date getExpires() {
      return (expires != null) ? (Date) expires.clone() : null;
    }

    /**
     * Constructs a new GoogleCookie instance.
     *
     * @param uri the original request URI that returned Set-Cookie header
     *              in the response
     * @param cookieHeader the value of the Set-Cookie header.
     */
    public GoogleCookie(URI uri, String cookieHeader) {

      // Set default values
      String attributes[] = cookieHeader.split(";");
      String nameValue = attributes[0].trim();
      int equals = nameValue.indexOf('=');
      if (equals < 0) {
        throw new IllegalArgumentException("Cookie is not a name/value pair");
      }
      this.name = nameValue.substring(0, equals);
      this.value = nameValue.substring(equals + 1);
      this.path = "/";
      this.domain = uri.getHost();

      // Process optional cookie attributes
      for (int i = 1; i < attributes.length; i++) {
        nameValue = attributes[i].trim();
        equals = nameValue.indexOf('=');
        if (equals == -1) {
          continue;
        }
        String name = nameValue.substring(0, equals);
        String value = nameValue.substring(equals + 1);
        if (name.equalsIgnoreCase("domain")) {
          if (uri.getPort() > 0) {
            // ignore port
            int colon = value.lastIndexOf(':');
            if (colon > 0) {
              value = value.substring(0, colon);
            }
          }
          String uriDomain = uri.getHost();
          if (uriDomain.equals(value)) {
            this.domain = value;
          } else {
            if (!matchDomain(uriDomain, value)) {
              throw new IllegalArgumentException(
                "Trying to set foreign cookie");
            }
          }
          this.domain = value;
        } else if (name.equalsIgnoreCase("path")) {
          this.path = value;
         } else if (name.equalsIgnoreCase("expires")) {
          try {
            this.expires =
                new SimpleDateFormat("E, dd-MMM-yyyy k:m:s 'GMT'", Locale.US)
                    .parse(value);
          } catch (java.text.ParseException e) {
            try {
              this.expires =
                  new SimpleDateFormat("E, dd MMM yyyy k:m:s 'GMT'", Locale.US)
                      .parse(value);
            } catch (java.text.ParseException e2) {
              throw new IllegalArgumentException(
                "Bad date format in header: " + value);
            }
          }
        }
      }
    }

    /**
     * Returns true if the full domain's final segments match
     * the tail domain.
     */
    private boolean matchDomain(String testDomain, String tailDomain) {

      // Simple check
      if (!testDomain.endsWith(tailDomain)) {
        return false;
      }

      // Exact match
      if (testDomain.length() == tailDomain.length()) {
        return true;
      }

      // Verify that a segment match happened, not a partial match
      if (tailDomain.charAt(0) == '.') {
        return true;
      }
      return testDomain.charAt(testDomain.length() -
                               tailDomain.length() - 1) == '.';
    }

    /**
     * Returns {@code true} if the cookie has expired.
     */
    public boolean hasExpired() {
      if (expires == null) {
        return false;
      }
      Date now = new Date();
      return now.after(expires);
    }

    /**
     * Returns {@code true} if the cookie hasn't expired, the
     * URI domain matches, and the URI path starts with the
     * cookie path.
     *
     * @param uri URI to check against
     * @return true if match, false otherwise
     */
    public boolean matches(URI uri) {

      if (hasExpired()) {
        return false;
      }

      String uriDomain = uri.getHost();
      if (!matchDomain(uriDomain, domain)) {
        return false;
      }

      String path = uri.getPath();
      if (path == null) {
        path = "/";
      }

      return path.startsWith(this.path);
    }

    /**
     * Returns the actual name/value pair that should be sent in a
     * Cookie request header.
     */
    String getHeaderValue() {
      StringBuilder result = new StringBuilder(name);
      result.append("=");
      result.append(value);
      return result.toString();
    }

    /**
     * Returns {@code true} if the target object is a GoogleCookie that
     * has the same name as this cookie and that matches the same target
     * domain and path as this cookie.   Cookie expiration and value
     * <b>are not</b> taken into account when considering equivalence.
     */
    @Override
    public boolean equals(Object o) {
      if (o == null || !(o instanceof GoogleCookie)) {
          return false;
      }
      GoogleCookie cookie = (GoogleCookie) o;
      if (!name.equals(cookie.name) || !domain.equals(cookie.domain)) {
        return false;
      }
      if (path == null) {
        if (cookie.path != null) {
          return false;
        }
        return true;
      }
      return path.equals(cookie.path);
    }

    @Override
    public int hashCode() {
      int result = 17;
      result = 37 * result + name.hashCode();
      result = 37 * result + domain.hashCode();
      result = 37 * result + (path != null ? path.hashCode() : 0);
      return result;
    }

    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder("GoogleCookie(");
      buf.append(domain);
      buf.append(path);
      buf.append("[");
      buf.append(name);
      buf.append("]");
      buf.append(")");
      return buf.toString();
    }
  }


  /**
   * Implements a scoped cookie handling mechanism for GData services.  This
   * handler is a singleton class that is registered to globally listen and
   * set cookies using {@link CookieHandler#setDefault(CookieHandler)}.  It
   * will only process HTTP headers and responses associated with GData
   * services, and will delegate the processing of any other cookie headers
   * to the previously registered {@link CookieHandler} (if any).  When
   * a Set-Cookie response header is found, it will save any associated
   * cookie in the cookie cache associated with the {@link GoogleService}
   * issuing the request.   Similarly, when a {@link GoogleService} issues
   * a request, it will check its cookie cache and add any necessary
   * Cookie header.
   */
  private static class GoogleCookieHandler extends CookieHandler {

    private CookieHandler nextHandler;

    // This is a singleton, only constructed once at class load time.
    private GoogleCookieHandler() {

      // Install the global GoogleCookieHandler instance, chaining to any
      // existing CookieHandler
      if (!Boolean.getBoolean(DISABLE_COOKIE_HANDLER_PROPERTY)) {
        logger.fine("Installing GoogleCookieHandler");
        nextHandler = CookieHandler.getDefault();
        CookieHandler.setDefault(this);
      }
    }


    @Override
    public Map<String, List<String>> get(
        URI uri, Map<String, List<String>> requestHeaders)
        throws IOException {

      Map<String, List<String>> cookieHeaders =
        new HashMap<String, List<String>>();

      // Only service requests initiated by GData services with cookie
      // handling enabled.
      GoogleService service = activeService.get();
      if (service != null && service.handlesCookies()) {

        // Get the list of matching cookies and accumulate a buffer
        // containing the cookie name/value pairs.
        Set<GoogleCookie> cookies = service.getCookies();
        StringBuilder cookieBuf = new StringBuilder();
        for (GoogleCookie cookie : cookies) {
          if (cookie.matches(uri)) {
            if (cookieBuf.length() > 0) {
              cookieBuf.append("; ");
            }
            cookieBuf.append(cookie.getHeaderValue());
            logger.fine("Setting cookie: " + cookie);
          }
        }

        // If any matching cookies were found, update the request headers.
        // Note: it's assumed here that nothing else is setting the Cookie
        // header, which seems reasonable; otherwise we'd have to parse the
        // existing value and add/merge managed cookies.
        if (cookieBuf.length() != 0) {
          cookieHeaders.put("Cookie",
                             Collections.singletonList(cookieBuf.toString()));
        }
      } else {
        if (nextHandler != null) {
          return nextHandler.get(uri, requestHeaders);
        }
      }

      return Collections.unmodifiableMap(cookieHeaders);
    }


    @Override
    public void put(URI uri,
                    Map<String, List<String>> responseHeaders)
        throws IOException {

      // Only service requests initiated by GData services with cookie
      // handling enabled.
      GoogleService service = activeService.get();
      if (service != null && service.handlesCookies()) {
        List<String> setCookieList = responseHeaders.get("Set-Cookie");
        if (setCookieList != null && setCookieList.size() > 0) {
          for (String cookieValue : setCookieList) {
            GoogleCookie cookie = new GoogleCookie(uri, cookieValue);
            service.addCookie(cookie);
            logger.fine("Adding cookie:" + cookie);
          }
        }
      } else {
        if (nextHandler != null) {
          nextHandler.get(uri, responseHeaders);
        }
      }
    }
  }


  /**
   * Holds the GoogleService that is executing requests for the current
   * execution thread.
   */
  private static final ThreadLocal<GoogleService> activeService =
    new ThreadLocal<GoogleService>();

  /**
   * The global CookieHandler instance for GData services.
   */
  @SuppressWarnings("unused") // instance init installs global hooks.
  private static final GoogleCookieHandler googleCookieHandler;

  static {
    if (!Boolean.getBoolean(DISABLE_COOKIE_HANDLER_PROPERTY)) {
      googleCookieHandler = new GoogleCookieHandler();
    } else {
      googleCookieHandler = null;
    }
  }

  /**
   * Constructs a new GoogleGDataRequest instance of the specified
   * RequestType, targeting the specified URL with the specified
   * authentication token.
   *
   * @param type type of GDataRequest
   * @param requestUrl request target URL
   * @param authToken token authenticating request to server
   * @param headerMap map containing additional headers to set
   * @param privateHeaderMap map containing additional headers to set
   *    that should not be logged (eg. authentication info)
   * @throws IOException on error initializing service connection
   */
  protected GoogleGDataRequest(RequestType type,
                               URL requestUrl,
                               ContentType contentType,
                               HttpAuthToken authToken,
                               Map<String, String> headerMap,
                               Map<String, String> privateHeaderMap,
                               HttpUrlConnectionSource connectionSource)
      throws IOException {

    super(type, requestUrl, contentType, authToken,
        headerMap, privateHeaderMap, connectionSource);
  }

  /**
   * The GoogleService instance that constructed the request.
   */
  private GoogleService service;

  /**
   * Returns the {@link Version} that will be used to execute the request on the
   * target service or {@code null} if the service is not versioned.
   *
   * @return version sent with the request or {@code null}.
   */
  public Version getRequestVersion() {
    // Always get the request version from the associated service, never from
    // the version registry.   There are aspects of request handling that happen
    // outside the scope of Service.begin/endVersionScope.
    return service.getProtocolVersion();
  }

  /**
   * The version associated with the response.
   */
  private Version responseVersion;

  /**
   * Returns the {@link Version} that was used by the target service to execute
   * the request or {@code null} if the service is not versioned.
   *
   * @return version returned with the response or {@code null}.
   */
  public Version getResponseVersion() {
    if (!executed) {
      throw new IllegalStateException("Request has not been executed");
    }
    return responseVersion;
  }

  /**
   * Sets the GoogleService associated with the request.
   */
  public void setService(GoogleService service) {
    this.service = service;

    // This undocumented system property can be used to disable version headers.
    // It exists only to support some unit test scenarios for query-parameter
    // version configuration and back-compat defaulting when no version
    // information is sent by the client library.
    if (Boolean.getBoolean("GoogleGDataRequest.disableVersionHeader")) {
      return;
    }

    // Look up the active version for the type of service initiating the
    // request, and set the version header if found.
    try {
      Version requestVersion = service.getProtocolVersion();
      if (requestVersion != null) {
        setHeader(GDataProtocol.Header.VERSION,
            requestVersion.getVersionString());
      }
    } catch (IllegalStateException iae) {
      // Service may not be versioned.
    }
  }




  @Override
  public void execute() throws IOException, ServiceException {

    // Set the current active service, so cookie handling will be enabled.
    try {
      activeService.set(service);
      // Propagate redirects to our layer to add URL specific data to the
      // request (like URL dependant authentication headers)
      httpConn.setInstanceFollowRedirects(false);
      super.execute();

      // Capture the version used to process the request
      String versionHeader =
        httpConn.getHeaderField(GDataProtocol.Header.VERSION);
      if (versionHeader != null) {
        GoogleService service = activeService.get();
        if (service != null) {
          responseVersion = new Version(service.getClass(), versionHeader);
        }

      }
    } finally {
      activeService.set(null);
    }
  }


  @Override
  protected void handleErrorResponse()
      throws IOException, ServiceException {

    try {
      switch (httpConn.getResponseCode()) {
        case HttpURLConnection.HTTP_MOVED_PERM:
        case HttpURLConnection.HTTP_MOVED_TEMP:
          throw new RedirectRequiredException(httpConn);
      }
      super.handleErrorResponse();
    } catch (AuthenticationException e) {
      // Throw a more specific exception for session expiration.
      String msg = e.getMessage();
      if ((msg != null && msg.contains("Token expired")) ||
          (this.authToken != null && this.authToken instanceof OAuth2Token)) {
        SessionExpiredException se =
          new SessionExpiredException(e.getMessage());
        se.setResponse(e.getResponseContentType(), e.getResponseBody());
        throw se;
      }
      throw e;
    }
  }
}
