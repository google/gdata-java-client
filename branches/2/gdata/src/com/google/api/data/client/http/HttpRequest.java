// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import com.google.api.data.client.auth.AuthorizedRequest;
import com.google.api.data.client.auth.Authorizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HttpRequest {

  private final ArrayList<String> headerNames = new ArrayList<String>();
  private final ArrayList<String> headerValues = new ArrayList<String>();
  private final LowLevelHttpRequestInterface httpRequest;
  private HttpSerializer content;
  private final String requestedMethod;
  private final String actualMethod;
  private final String uri;
  private final HttpTransport transport;

  HttpRequest(HttpTransport transport, String method, String uri)
      throws IOException {
    this.requestedMethod = method;
    this.transport = transport;
    this.uri = uri;
    LowLevelHttpTransportInterface httpTransport =
        transport.getLowLevelHttpTransportInterface();
    // method override?
    boolean isPatch = method == "PATCH";
    String actualMethod;
    if (HttpTransport.ENABLE_METHOD_OVERRIDE
        && (isPatch || method == "PUT" || method == "DELETE") || isPatch
        && !httpTransport.supportsPatch()) {
      actualMethod = "POST";
    } else {
      actualMethod = method;
    }
    this.actualMethod = actualMethod;
    // build request
    if (actualMethod == "GET") {
      this.httpRequest = httpTransport.buildGetRequest(uri);
    } else if (actualMethod == "POST") {
      this.httpRequest = httpTransport.buildPostRequest(uri);
    } else if (actualMethod == "PUT") {
      this.httpRequest = httpTransport.buildPutRequest(uri);
    } else if (actualMethod == "DELETE") {
      this.httpRequest = httpTransport.buildDeleteRequest(uri);
    } else if (actualMethod == "PATCH") {
      this.httpRequest = httpTransport.buildPatchRequest(uri);
    } else {
      throw new IllegalArgumentException(actualMethod);
    }
  }

  public void addHeader(String name, String value) {
    HttpTransport.setHeader(name, value, this.headerNames, this.headerValues);
  }

  public void setContent(HttpSerializer serializer) {
    setContentNoLogging(new LogHttpSerializer(serializer));
  }

  public void setContentNoLogging(HttpSerializer serializer) {
    this.content = new GZipHttpSerializer(serializer);
  }

  public HttpResponse execute() throws IOException {
    LowLevelHttpRequestInterface httpRequest = this.httpRequest;
    HttpTransport transport = this.transport;
    Logger logger = HttpTransport.LOGGER;
    boolean loggable = logger.isLoggable(Level.CONFIG);
    String requestedMethod = this.requestedMethod;
    String actualMethod = this.actualMethod;
    String uri = this.uri;
    // authorizer
    Authorizer authorizer = transport.authorizer;
    AuthorizedRequest authorizedRequest = null;
    if (authorizer != null) {
      authorizedRequest = authorizer.getAuthorizedRequest(actualMethod, uri);
      uri = authorizedRequest.getUri();
    }
    // log method and uri
    if (loggable) {
      logger.config(actualMethod + " " + uri);
    }
    if (authorizedRequest != null) {
      int size = authorizedRequest.getHeaderCount();
      for (int i = 0; i < size; i++) {
        String name = authorizedRequest.getHeaderName(i);
        String value = authorizedRequest.getHeaderValue(i);
        if (loggable) {
          logger.config(name + ": <Not Logged>");
        }
        httpRequest.addHeader(name, value);
      }
    }
    // method override
    if (actualMethod != requestedMethod) {
      addHeader("X-HTTP-Method-Override", requestedMethod);
    }
    // headers
    addHeaders(transport.defaultHeaderNames, transport.defaultHeaderValues);
    addHeaders(this.headerNames, this.headerValues);
    // content
    HttpSerializer content = this.content;
    if (content != null) {
      if (loggable) {
        logger.config("Content-Type: " + content.getContentType());
        String contentEncoding = content.getContentEncoding();
        if (contentEncoding != null) {
          logger.config("Content-Encoding: " + contentEncoding);
        }
        long contentLength = content.getContentLength();
        if (contentLength >= 0) {
          logger.config("Content-Length: " + contentLength);
        }
      }
      httpRequest.setContent(content);
    }
    // execute
    return new HttpResponse(transport, httpRequest.execute());
  }

  private void addHeaders(ArrayList<String> headerNames,
      ArrayList<String> headerValues) {
    Logger logger = HttpTransport.LOGGER;
    boolean loggable = logger.isLoggable(Level.CONFIG);
    LowLevelHttpRequestInterface httpRequest = this.httpRequest;
    int size = headerNames.size();
    for (int i = 0; i < size; i++) {
      String name = headerNames.get(i);
      String value = headerValues.get(i);
      if (loggable) {
        logger.config(name + ": " + value);
      }
      httpRequest.addHeader(name, value);
    }
  }
}
