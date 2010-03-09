// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import com.google.api.data.client.auth.AuthorizedRequest;
import com.google.api.data.client.auth.Authorizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Request {

  private final ArrayList<String> headerNames = new ArrayList<String>();
  private final ArrayList<String> headerValues = new ArrayList<String>();
  private final HttpRequest httpRequest;
  private HttpSerializer content;
  private final String requestedMethod;
  private final String actualMethod;
  private final String uri;
  private final Transport transport;

  Request(Transport transport, String method, String uri) throws IOException {
    this.requestedMethod = method;
    this.transport = transport;
    this.uri = uri;
    HttpTransport httpTransport = Transport.httpTransport;
    // method override?
    boolean isPatch = method == "PATCH";
    String actualMethod;
    if (Transport.ENABLE_METHOD_OVERRIDE
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
    headerNames.add(name);
    headerValues.add(value);
  }

  public void setContent(HttpSerializer serializer) {
    this.content = new GZipHttpSerializer(new LogHttpSerializer(serializer));
  }

  public Response execute() throws IOException {
    HttpRequest httpRequest = this.httpRequest;
    Transport transport = this.transport;
    Logger logger = Transport.LOGGER;
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
      String contentEncoding = content.getContentEncoding();
      String contentType = content.getContentType();
      long contentLength = content.getContentLength();
      Response.debugContentMetadata(logger, contentType, contentEncoding,
          contentLength);
      httpRequest.setContent(content);
    }
    // execute
    return new Response(httpRequest.execute());
  }

  private void addHeaders(ArrayList<String> headerNames,
      ArrayList<String> headerValues) {
    Logger logger = Transport.LOGGER;
    boolean loggable = logger.isLoggable(Level.CONFIG);
    HttpRequest httpRequest = this.httpRequest;
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
