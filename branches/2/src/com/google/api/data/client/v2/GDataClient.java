// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import com.google.api.data.client.v2.escape.CharEscapers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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

  public static final String IMAGE_BMP = "image/bmp";
  public static final String IMAGE_GIF = "image/gif";
  public static final String IMAGE_JPEG = "image/jpeg";
  public static final String IMAGE_PNG = "image/png";

  public static final String VIDEO_3GPP = "video/3gpp";
  public static final String VIDEO_AVI = "video/avi";
  public static final String VIDEO_QUICKTIME = "video/quicktime";
  public static final String VIDEO_MP4 = "video/mp4";
  public static final String VIDEO_MPEG = "video/mpeg";
  public static final String VIDEO_MPEG4 = "video/mpeg4";
  public static final String VIDEO_MSVIDEO = "video/msvideo";
  public static final String VIDEO_X_MS_ASF = "video/x-ms-asf";
  public static final String VIDEO_X_MS_WMV = "video/x-ms-wmv";
  public static final String VIDEO_X_MSVIDEO = "video/x-msvideo";

  /**
   * If this system property is set to {@code true}, the GData HTTP client
   * library will use POST to send data to the associated GData service and will
   * specify the actual method using the method override HTTP header. This can
   * be used as a workaround for HTTP proxies or gateways that do not handle
   * PUT, PATCH, or DELETE HTTP methods properly. If the system property is
   * {@code false}, the regular verbs will be used.
   */
  public static final boolean ENABLE_METHOD_OVERRIDE =
      Boolean.getBoolean("com.google.gdata.UseMethodOverride");

  private static final boolean ENABLE_GZIP = true;
  private final String contentType;
  private final HttpTransport httpTransport;
  private final ArrayList<String> defaultHeaderNames = new ArrayList<String>();
  private final ArrayList<String> defaultHeaderValues = new ArrayList<String>();

  public GDataClient(HttpTransport httpTransport, String contentType,
      String applicationName, String authToken, String version) {
    this.httpTransport = httpTransport;
    this.contentType = contentType;
    addDefaultHeader("GData-Version", version);
    if (authToken != null) {
      addDefaultHeader("Authorization", "GoogleLogin auth=" + authToken);
    }
    StringBuilder buf =
        new StringBuilder().append(applicationName).append(
            " GData-Java/2.0.0-alpha");
    if (ENABLE_GZIP) {
      buf.append("(gzip)");
      addDefaultHeader("Accept-Encoding", "gzip");
    }
    addDefaultHeader("User-Agent", buf.toString());
  }

  public GDataResponse executeGet(String uri) throws IOException {
    return execute(getRequest(uri));
  }

  public GDataResponse executeGetIfModified(String uri, String etag)
      throws IOException {
    HttpRequest request = getRequest(uri);
    request.addHeader("If-None-Match", etag);
    return execute(request);
  }

  public GDataResponse executePutIfNotModified(String uri, String etag,
      GDataSerializer content) throws IOException {
    HttpRequest request = putRequest(uri);
    addIfMatchHeader(request, etag);
    setContent(request, content, this.contentType);
    return execute(request);
  }

  public GDataResponse executePatchIfNotModified(String uri, String etag,
      GDataSerializer content, String patchContentType) throws IOException {
    HttpRequest request = patchRequest(uri);
    setContent(request, content, patchContentType);
    addIfMatchHeader(request, etag);
    if (ENABLE_METHOD_OVERRIDE || !httpTransport.supportsPatch()) {
      addMethodOverrideHeader(request, "PATCH");
    }
    return execute(request);
  }

  public GDataResponse executePost(String uri, GDataSerializer content)
      throws IOException {
    HttpRequest request = postRequest(uri);
    setContent(request, content, this.contentType);
    return execute(request);
  }

  public GDataResponse executePostMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException {
    return executeMediaWithMetadata(postRequest(uri), metadata, mediaType,
        mediaContent, mediaContentLength);
  }

  /**
   * For example, to post media from a file, use {@code executePostMedia(uri,
   * file.getName(), mediaType, new FileInputStream(file), file.length()}.
   */
  public GDataResponse executePostMedia(String uri, String fileName,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException {
    return executeMedia(postRequest(uri), fileName, mediaType, mediaContent,
        mediaContentLength);
  }

  public GDataResponse executePutMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException {
    return executeMediaWithMetadata(putRequest(uri), null, mediaType,
        mediaContent, mediaContentLength);
  }

  public GDataResponse executePutMediaIfNotModified(String uri, String etag,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException {
    HttpRequest request = putRequest(uri);
    addIfMatchHeader(request, etag);
    return executeMedia(request, null, mediaType, mediaContent,
        mediaContentLength);
  }

  public GDataResponse executeDelete(String uri) throws IOException {
    return executeDelete(uri, null);
  }

  public GDataResponse executeDeleteIfNotModified(String uri, String etag)
      throws IOException {
    return executeDelete(uri, etag);
  }

  private GDataResponse execute(HttpRequest request) throws IOException {
    // default headers
    ArrayList<String> defaultHeaderNames = this.defaultHeaderNames;
    ArrayList<String> defaultHeaderValues = this.defaultHeaderValues;
    int size = defaultHeaderNames.size();
    for (int i = 0; i < size; i++) {
      request.addHeader(defaultHeaderNames.get(i), defaultHeaderValues.get(i));
    }
    // execute
    return new GDataResponse(request.execute());
  }

  private GDataResponse executeDelete(String uri, String etag)
      throws IOException {
    HttpRequest request = deleteRequest(uri);
    if (etag != null) {
      addIfMatchHeader(request, etag);
    }
    if (ENABLE_METHOD_OVERRIDE) {
      addMethodOverrideHeader(request, "DELETE");
    }
    return execute(request);
  }

  private GDataResponse executeMediaWithMetadata(HttpRequest request,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException {
    GDataMultipartHttpSerializer httpSerializer =
        GDataMultipartHttpSerializer.create(metadata, this.contentType,
            mediaType, mediaContent, mediaContentLength);
    long length = httpSerializer.length;
    request.setContent(length, "multipart/related; boundary=\"END_OF_PART\"",
        null, httpSerializer);
    Logger logger = GData.LOGGER;
    if (logger.isLoggable(Level.CONFIG)) {
      GDataResponse.debugContentMetadata(logger, contentType, null, length);
    }
    addHeader(request, "MIME-version", "1.0");
    return execute(request);
  }

  private GDataResponse executeMedia(HttpRequest request, String fileName,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException {
    setContent(request, mediaContent, mediaType, mediaContentLength);
    return executeMedia(request, fileName);
  }

  private GDataResponse executeMedia(HttpRequest request, String fileName)
      throws IOException {
    if (fileName != null) {
      addHeader(request, "Slug", CharEscapers.escapeSlug(fileName));
    }
    return execute(request);
  }

  private void addIfMatchHeader(HttpRequest request, String etag) {
    // TODO: shouldn't set if etag.startsWith("W/")?
    addHeader(request, "If-Match", etag);
  }

  private void addMethodOverrideHeader(HttpRequest request, String method) {
    addHeader(request, "X-HTTP-Method-Override", method);
  }

  private void addHeader(HttpRequest request, String name, String value) {
    Logger logger = GData.LOGGER;
    if (logger.isLoggable(Level.CONFIG) && !name.equals("Authorization")) {
      logger.config(name + ": " + value);
    }
    request.addHeader(name, value);
  }

  private void setContent(HttpRequest request, InputStream inputStream,
      String contentType, long contentLength) {
    request.setContent(contentLength, contentType, inputStream);
    Logger logger = GData.LOGGER;
    if (logger.isLoggable(Level.CONFIG)) {
      GDataResponse.debugContentMetadata(logger, contentType, null,
          contentLength);
    }
  }

  private void setContent(HttpRequest request, GDataSerializer content,
      String contentType) {
    GDataHttpSerializer httpSerializer =
        GDataHttpSerializer.create(content, contentType);
    String contentEncoding = httpSerializer.contentEncoding;
    long contentLength = httpSerializer.contentLength;
    request.setContent(contentLength, contentType, contentEncoding,
        httpSerializer);
    Logger logger = GData.LOGGER;
    if (logger.isLoggable(Level.CONFIG)) {
      GDataResponse.debugContentMetadata(logger, contentType, contentEncoding,
          contentLength);
    }
  }

  private void logVerb(String verb, String uri) {
    Logger logger = GData.LOGGER;
    if (logger.isLoggable(Level.CONFIG)) {
      logger.config(verb + " " + uri);
    }
  }

  private HttpRequest getRequest(String uri) throws IOException {
    logVerb("GET", uri);
    return this.httpTransport.getRequest(uri);
  }

  private HttpRequest deleteRequest(String uri) throws IOException {
    logVerb("DELETE", uri);
    return this.httpTransport.deleteRequest(uri);
  }

  private HttpRequest putRequest(String uri) throws IOException {
    logVerb("PUT", uri);
    return this.httpTransport.putRequest(uri);
  }

  private HttpRequest postRequest(String uri) throws IOException {
    logVerb("POST", uri);
    return this.httpTransport.postRequest(uri);
  }

  private HttpRequest patchRequest(String uri) throws IOException {
    logVerb("PATCH", uri);
    return this.httpTransport.patchRequest(uri);
  }

  private void addDefaultHeader(String name, String value) {
    this.defaultHeaderNames.add(name);
    this.defaultHeaderValues.add(value);
  }

  // TODO: support If-Modified-Since or does ETag deprecate it?
  // TODO: consider a request-style model
  // TODO: support running operations async with a callback?
  // TODO: ability to set additional arbitrary headers per request?
}
