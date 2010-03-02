// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.apache;

import com.google.api.data.client.v2.GDataClient;
import com.google.api.data.client.v2.GDataResponse;
import com.google.api.data.client.v2.GDataSerializer;
import com.google.api.data.client.v2.escape.CharEscapers;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpMessage;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

final class ApacheGDataClient implements GDataClient {

  // TODO: constructor parameter?
  private static final boolean ENABLE_GZIP = true;
  private static final Header MIME_VERSION_HEADER =
      new BasicHeader("MIME-version", "1.0");
  private static final Header GZIP_HEADER =
      new BasicHeader("Accept-Encoding", "gzip");

  private final Header authHeader;
  private final HttpClient httpClient;
  private final String contentType;
  private final String version;

  ApacheGDataClient(String contentType, String applicationName,
      String authToken, String version) {
    this.contentType = contentType;
    this.version = version;
    this.authHeader =
        authToken == null ? null : new BasicHeader("Authorization",
            "GoogleLogin auth=" + authToken);
    // Turn off stale checking. Our connections break all the time anyway,
    // and it's not worth it to pay the penalty of checking every time.
    HttpParams params = new BasicHttpParams();
    HttpConnectionParams.setStaleCheckingEnabled(params, false);
    // Default connection and socket timeout of 20 seconds. Tweak to taste.
    HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
    HttpConnectionParams.setSoTimeout(params, 20 * 1000);
    HttpConnectionParams.setSocketBufferSize(params, 8192);
    StringBuilder userAgentBuf =
        new StringBuilder().append(applicationName).append(" GData-Android/2.0.0");
    if (ENABLE_GZIP) {
      userAgentBuf.append("(gzip)");
    }
    HttpProtocolParams.setUserAgent(params, userAgentBuf.toString());
    this.httpClient = new DefaultHttpClient(params);
  }

  public GDataResponse executeGet(String uri) throws IOException {
    HttpGet request = new HttpGet(uri);
    return execute(request);
  }

  public GDataResponse executeGetIfModified(String uri, String etag)
      throws IOException {
    HttpGet request = new HttpGet(uri);
    if (etag == null) {
      throw new NullPointerException("etag");
    }
    request.addHeader("If-None-Match", etag);
    return execute(request);
  }

  public GDataResponse executePutIfNotModified(String uri, String etag,
      GDataSerializer content) throws IOException {
    HttpPut request = new HttpPut(uri);
    setIfMatchHeader(request, etag);
    request.setEntity(GDataSerializerEntity.create(content, this.contentType));
    return execute(request);
  }

  public GDataResponse executePatchIfNotModified(String uri, String etag,
      GDataSerializer content, String patchContentType) throws IOException {
    HttpPatch request = new HttpPatch(uri);
    // TODO: etag for PATCH?
    setIfMatchHeader(request, etag);
    request.setEntity(GDataSerializerEntity.create(content, patchContentType));
    return execute(request);
  }

  public GDataResponse executePost(String uri, GDataSerializer content)
      throws IOException {
    HttpPost request = new HttpPost(uri);
    request.setEntity(GDataSerializerEntity.create(content, this.contentType));
    return execute(request);
  }

  public GDataResponse executePostMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException {
    return executeMediaWithMetadata(new HttpPost(uri), metadata, mediaType,
        mediaContent, mediaContentLength);
  }

  public GDataResponse executePostMedia(String uri, String fileName,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException {
    return executeMedia(new HttpPost(uri), fileName, mediaType, mediaContent,
        mediaContentLength);
  }

  public GDataResponse executePutMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException {
    return executeMediaWithMetadata(new HttpPut(uri), null, mediaType,
        mediaContent, mediaContentLength);
  }

  public GDataResponse executePutMediaIfNotModified(String uri, String etag,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException {
    HttpPut request = new HttpPut(uri);
    setIfMatchHeader(request, etag);
    return executeMedia(request, null, mediaType, mediaContent,
        mediaContentLength);
  }

  public GDataResponse executeDelete(String uri) throws IOException {
    return execute(new HttpDelete(uri));
  }

  public GDataResponse executeDeleteIfNotModified(String uri, String etag)
      throws IOException {
    // TODO: use POST with method override?
    HttpDelete request = new HttpDelete(uri);
    setIfMatchHeader(request, etag);
    return execute(request);
  }

  private GDataResponse execute(HttpUriRequest request) throws IOException {
    request.addHeader("GData-Version", version);
    Header authHeader = this.authHeader;
    if (authHeader != null) {
      request.addHeader(authHeader);
    }
    if (ENABLE_GZIP) {
      request.addHeader(GZIP_HEADER);
    }
    debug(request);
    return new ApacheGDataResponse(this.httpClient.execute(request));
  }

  private GDataResponse executeMediaWithMetadata(
      HttpEntityEnclosingRequestBase request, GDataSerializer metadata,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException {
    HttpEntity entity =
        GDataMultipartEntity.create(metadata, this.contentType, mediaType,
            mediaContent, mediaContentLength);
    request.setEntity(entity);
    request.setHeader(MIME_VERSION_HEADER);
    return execute(request);
  }

  private GDataResponse executeMedia(HttpEntityEnclosingRequestBase request,
      String fileName, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException {
    InputStreamEntity entity =
        new InputStreamEntity(mediaContent, mediaContentLength);
    entity.setContentType(mediaType);
    return executeMedia(request, entity, fileName);
  }

  private GDataResponse executeMedia(HttpEntityEnclosingRequestBase request,
      HttpEntity entity, String fileName) throws IOException {
    request.setEntity(entity);
    if (fileName != null) {
      request.setHeader("Slug", CharEscapers.escapeSlug(fileName));
    }
    return execute(request);
  }

  private void setIfMatchHeader(HttpMessage request, String etag) {
    // TODO: shouldn't set if etag.startsWith("W/")?
    request.addHeader("If-Match", etag);
  }

  private static void debug(HttpUriRequest request) {
    Logger logger = ApacheGData.LOGGER; 
    if (logger.isLoggable(Level.CONFIG)) {
      logger.config(request.getRequestLine().toString());
      for (Header header : request.getAllHeaders()) {
        logger.config(header.toString());
      }
      if (request instanceof HttpEntityEnclosingRequest) {
        HttpEntityEnclosingRequest entityRequest =
            (HttpEntityEnclosingRequest) request;
        HttpEntity entity = entityRequest.getEntity();
        if (entity.getContentType() != null) {
          logger.config(entity.getContentType().toString());
        }
        if (entity.getContentEncoding() != null) {
          logger.config(entity.getContentEncoding().toString());
        }
        if (entity.getContentLength() >= 0) {
          logger.config("Content-Length: " + entity.getContentLength());
        }
      }
    }
  }
}
