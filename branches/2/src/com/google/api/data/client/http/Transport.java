// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import com.google.api.data.client.auth.Authorizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Transport {

  static final Logger LOGGER = Logger.getLogger(Transport.class.getName());

  /**
   * If this system property is set to {@code true}, the GData HTTP client
   * library will use POST to send data to the associated GData service and will
   * specify the actual method using the method override HTTP header. This can
   * be used as a workaround for HTTP proxies or gateways that do not handle
   * PUT, PATCH, or DELETE HTTP methods properly. If the system property is
   * {@code false}, the regular verbs will be used.
   */
  public static final boolean ENABLE_METHOD_OVERRIDE =
      Boolean.getBoolean(Transport.class.getName() + ".UseMethodOverride");

  public static final boolean DISABLE_GZIP =
      Boolean.getBoolean(Transport.class.getName() + ".DisableGZip");

  // TODO: specify this in the metadata of the packaged jar?
  public static HttpTransport httpTransport;

  final ArrayList<String> defaultHeaderNames = new ArrayList<String>();
  final ArrayList<String> defaultHeaderValues = new ArrayList<String>();

  /** Authorizer or {@code null} for none. */
  final Authorizer authorizer;

  public Transport(String applicationName, Authorizer authorizer) {
    this.authorizer = authorizer;
    // user agent and encoding
    StringBuilder userAgent =
        new StringBuilder().append(applicationName).append(
            " Google-API-Java/2.0.0-alpha");
    if (!DISABLE_GZIP) {
      userAgent.append("(gzip)");
      addDefaultHeader("Accept-Encoding", "gzip");
    }
    addDefaultHeader("User-Agent", userAgent.toString());
  }

  public void addDefaultHeader(String name, String value) {
    this.defaultHeaderNames.add(name);
    this.defaultHeaderValues.add(value);
  }

  public Request buildDeleteRequest(String uri) throws IOException {
    return new Request(this, "DELETE", uri);
  }

  public Request buildGetRequest(String uri) throws IOException {
    return new Request(this, "GET", uri);
  }

  public Request buildPatchRequest(String uri) throws IOException {
    return new Request(this, "PATCH", uri);
  }

  public Request buildPostRequest(String uri) throws IOException {
    return new Request(this, "POST", uri);
  }

  public Request buildPutRequest(String uri) throws IOException {
    return new Request(this, "PUT", uri);
  }
}
