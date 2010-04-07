package com.google.api.client.http.googleapis;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;

import java.io.IOException;

/** Transport for Google API's. */
public class GoogleTransport extends HttpTransport {

  /**
   * If this system property is set to {@code true}, the GData HTTP client
   * library will use POST to send data to the associated GData service and will
   * specify the actual method using the method override HTTP header. This can
   * be used as a workaround for HTTP proxies or gateways that do not handle
   * PUT, PATCH, or DELETE HTTP methods properly. If the system property is
   * {@code false}, the regular verbs will be used.
   */
  public static final boolean ENABLE_METHOD_OVERRIDE =
      Boolean.getBoolean(HttpTransport.class.getName() + ".UseMethodOverride");

  /**
   * @param applicationName application name of the format {@code
   *        "[company-id]-[app-name]-[app-version]"}.
   */
  public GoogleTransport(String applicationName) {
    super();
    StringBuilder userAgent = new StringBuilder();
    if (applicationName != null) {
      userAgent.append(applicationName).append(' ');
    }
    userAgent.append("Google-API-Java/2.1.0-alpha");
    if (!DISABLE_GZIP) {
      // user agent trick that enables gzip for Google API's
      userAgent.append("(gzip)");
    }
    setUserAgentHeader(userAgent.toString());
  }

  /**
   * Sets the {@code "GData-Version"} header required by Google Data API's.
   * 
   * @param version version of the Google Data API being access, for example
   *        {@code "2"}.
   */
  public void setGDataVersionHeader(String version) {
    this.defaultHeaders.put("GData-Version", version);
  }

  /**
   * Sets the {@code GoogleLogin} authorization header based on the given
   * authentication token. This is primarily intended for use in the Android
   * environment after retrieving the authentication token from the
   * AccountManager.
   */
  public void setGoogleLoginAuthorizationHeader(String authToken) {
    setAuthorizationHeader(getGoogleLoginAuthorizationHeaderValue(authToken));
  }

  /**
   * Returns {@code GoogleLogin} authorization header value based on the given
   * authentication token.
   */
  public static String getGoogleLoginAuthorizationHeaderValue(String authToken) {
    return "GoogleLogin auth=" + authToken;
  }

  @Override
  public HttpRequest buildDeleteRequest(String uri) throws IOException {
    if (!ENABLE_METHOD_OVERRIDE) {
      return super.buildDeleteRequest(uri);
    }
    HttpRequest request = buildPostRequest(uri);
    setMethodOverride(request, "DELETE");
    return request;
  }

  @Override
  public HttpRequest buildPatchRequest(String uri) throws IOException {
    if (!ENABLE_METHOD_OVERRIDE && getLowLevelHttpTransport().supportsPatch()) {
      return super.buildPatchRequest(uri);
    }
    HttpRequest request = buildPostRequest(uri);
    setMethodOverride(request, "PATCH");
    return request;
  }

  @Override
  public HttpRequest buildPutRequest(String uri) throws IOException {
    if (!ENABLE_METHOD_OVERRIDE) {
      return super.buildPutRequest(uri);
    }
    HttpRequest request = buildPostRequest(uri);
    setMethodOverride(request, "PUT");
    return request;
  }

  private void setMethodOverride(HttpRequest request, String method) {
    request.headers.put("X-HTTP-Method-Override", method);
  }
}
