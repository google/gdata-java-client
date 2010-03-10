// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import com.google.api.data.client.auth.Authorizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class HttpTransport {

  static final Logger LOGGER = Logger.getLogger(HttpTransport.class.getName());

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

  public static final boolean DISABLE_GZIP =
      Boolean.getBoolean(HttpTransport.class.getName() + ".DisableGZip");

  // TODO: specify this in the metadata of the packaged jar?
  public static LowLevelHttpTransportInterface lowLevelHttpTransportInterface;

  final ArrayList<String> defaultHeaderNames = new ArrayList<String>();
  final ArrayList<String> defaultHeaderValues = new ArrayList<String>();

  /** Authorizer or {@code null} for none. */
  public Authorizer authorizer;

  public HttpTransport(String applicationName) {
    StringBuilder userAgent =
        new StringBuilder().append(applicationName).append(
            " Google-API-Java/2.0.0-alpha");
    if (!DISABLE_GZIP) {
      userAgent.append("(gzip)");
      setDefaultHeader("Accept-Encoding", "gzip");
    }
    setDefaultHeader("User-Agent", userAgent.toString());
  }

  public void setDefaultHeader(String name, String value) {
    setHeader(name, value, this.defaultHeaderNames, this.defaultHeaderValues);
  }

  public HttpRequest buildDeleteRequest(String uri) throws IOException {
    return new HttpRequest(this, "DELETE", uri);
  }

  public HttpRequest buildGetRequest(String uri) throws IOException {
    return new HttpRequest(this, "GET", uri);
  }

  public HttpRequest buildPatchRequest(String uri) throws IOException {
    return new HttpRequest(this, "PATCH", uri);
  }

  public HttpRequest buildPostRequest(String uri) throws IOException {
    return new HttpRequest(this, "POST", uri);
  }

  public HttpRequest buildPutRequest(String uri) throws IOException {
    return new HttpRequest(this, "PUT", uri);
  }

  static void setHeader(String name, String value,
      ArrayList<String> headerNames, ArrayList<String> headerValues) {
    if (name == null) {
      throw new NullPointerException();
    }
    int index = headerNames.indexOf(name);
    if (index == -1) {
      if (value != null) {
        headerNames.add(name);
        headerValues.add(value);
      }
    } else if (value != null) {
      headerValues.set(index, value);
    } else {
      headerNames.remove(index);
      headerValues.remove(index);
    }
  }
}
