// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import com.google.api.data.client.http.HttpSerializer;
import com.google.api.data.client.http.MultipartHttpSerializer;
import com.google.api.data.client.http.Request;
import com.google.api.data.client.http.Response;
import com.google.api.data.client.http.Transport;
import com.google.api.data.client.v2.escape.CharEscapers;

import java.io.IOException;

public final class GDataClient {

  /**
   * Constant for a HTTP not modified condition (HTTP status code 304)
   */
  public static final int ERROR_NOT_MODIFIED = 304;

  /**
   * Constant for a HTTP bad request condition (HTTP status code 400)
   */
  public static final int ERROR_BAD_REQUEST = 400;

  /**
   * Constant for a HTTP unauthorized condition (HTTP status code 401)
   */
  public static final int ERROR_UNAUTHORIZED = 401;

  /**
   * Constant for a HTTP forbidden condition (HTTP status code 403)
   */
  public static final int ERROR_FORBIDDEN = 403;

  /**
   * Constant for a HTTP not found condition (HTTP status code 404)
   */
  public static final int ERROR_NOT_FOUND = 404;

  /**
   * Constant for a HTTP conflict condition (HTTP status code 409)
   */
  public static final int ERROR_CONFLICT = 409;

  /**
   * Constant for a HTTP gone condition (HTTP status code 410)
   */
  public static final int ERROR_GONE = 410;

  /**
   * Constant for a HTTP precondition failed condition (HTTP status code 412)
   */
  public static final int ERROR_PRECONDITION_FAILED = 412;

  /**
   * Constant for a HTTP internal server error condition (HTTP status code 500)
   */
  public static final int ERROR_INTERNAL_SERVER_ERROR = 500;

  private final Transport transport;

  public GDataClient(Transport transport, String version) {
    this.transport = transport;
    transport.addDefaultHeader("GData-Version", version);
  }

  public Response executeGet(String uri) throws IOException {
    return execute(buildGetRequest(uri));
  }

  public Response executeGetIfModified(String uri, String etag)
      throws IOException {
    Request request = buildGetRequest(uri);
    request.addHeader("If-None-Match", etag);
    return execute(request);
  }

  public Response executePutIfNotModified(String uri, String etag,
      HttpSerializer content) throws IOException {
    Request request = buildPutRequest(uri);
    addIfMatchHeader(request, etag);
    request.setContent(content);
    return execute(request);
  }

  public Response executePatchIfNotModified(String uri, String etag,
      HttpSerializer content) throws IOException {
    Request request = buildPatchRequest(uri);
    request.setContent(content);
    addIfMatchHeader(request, etag);
    return execute(request);
  }

  public Response executePost(String uri, HttpSerializer content)
      throws IOException {
    Request request = buildPostRequest(uri);
    request.setContent(content);
    return execute(request);
  }

  public Response executePostMediaWithMetadata(String uri,
      HttpSerializer metadata, HttpSerializer mediaContent) throws IOException {
    return executeMediaWithMetadata(buildPostRequest(uri), metadata, mediaContent);
  }

  /**
   * For example, to post media from a file, use {@code executePostMedia(uri,
   * file.getName(), mediaType, new FileHttpSerializer(file))}.
   */
  public Response executePostMedia(String uri, String fileName,
      HttpSerializer mediaContent) throws IOException {
    return executeMedia(buildPostRequest(uri), fileName, mediaContent);
  }

  public Response executePutMediaWithMetadata(String uri,
      HttpSerializer metadata, HttpSerializer content) throws IOException {
    return executeMediaWithMetadata(buildPutRequest(uri), metadata, content);
  }

  public Response executePutMediaIfNotModified(String uri, String etag,
      HttpSerializer mediaContent) throws IOException {
    Request request = buildPutRequest(uri);
    addIfMatchHeader(request, etag);
    return executeMedia(request, null, mediaContent);
  }

  public Response executeDelete(String uri) throws IOException {
    return executeDelete(uri, null);
  }

  public Response executeDeleteIfNotModified(String uri, String etag)
      throws IOException {
    return executeDelete(uri, etag);
  }

  private Response execute(Request request) throws IOException {
    // execute
    return request.execute();
  }

  private Response executeDelete(String uri, String etag) throws IOException {
    Request request = buildDeleteRequest(uri);
    if (etag != null) {
      addIfMatchHeader(request, etag);
    }
    return execute(request);
  }

  private Response executeMediaWithMetadata(Request request,
      HttpSerializer metadata, HttpSerializer content) throws IOException {
    MultipartHttpSerializer serializer =
        new MultipartHttpSerializer(metadata, content);
    serializer.addHeaders(request);
    return execute(request);
  }

  private Response executeMedia(Request request, String fileName,
      HttpSerializer mediaContent) throws IOException {
    request.setContent(mediaContent);
    return executeMedia(request, fileName);
  }

  private Response executeMedia(Request request, String fileName)
      throws IOException {
    if (fileName != null) {
      request.addHeader("Slug", CharEscapers.escapeSlug(fileName));
    }
    return execute(request);
  }

  private void addIfMatchHeader(Request request, String etag) {
    // TODO: shouldn't set if etag.startsWith("W/")?
    request.addHeader("If-Match", etag);
  }

  private Request buildGetRequest(String uri) throws IOException {
    return this.transport.buildGetRequest(uri);
  }

  private Request buildDeleteRequest(String uri) throws IOException {
    return this.transport.buildDeleteRequest(uri);
  }

  private Request buildPutRequest(String uri) throws IOException {
    return this.transport.buildPutRequest(uri);
  }

  private Request buildPostRequest(String uri) throws IOException {
    return this.transport.buildPostRequest(uri);
  }

  private Request buildPatchRequest(String uri) throws IOException {
    return this.transport.buildPatchRequest(uri);
  }

  // TODO: support If-Modified-Since or does ETag deprecate it?
}
