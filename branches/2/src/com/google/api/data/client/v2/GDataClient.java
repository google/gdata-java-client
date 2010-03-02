// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;
import java.io.InputStream;

public interface GDataClient {
  
  String LINE_SEPARATOR = System.getProperty("line.separator");

  /**
   * Constant for a HTTP not modified condition (HTTP status code 304)
   */
  int ERROR_NOT_MODIFIED = 304;

  /**
   * Constant for a HTTP bad request condition (HTTP status code 400)
   */
  int ERROR_BAD_REQUEST = 400;

  /**
   * Constant for a HTTP unauthorized condition (HTTP status code 401)
   */
  int ERROR_UNAUTHORIZED = 401;

  /**
   * Constant for a HTTP forbidden condition (HTTP status code 403)
   */
  int ERROR_FORBIDDEN = 403;

  /**
   * Constant for a HTTP not found condition (HTTP status code 404)
   */
  int ERROR_NOT_FOUND = 404;

  /**
   * Constant for a HTTP conflict condition (HTTP status code 409)
   */
  int ERROR_CONFLICT = 409;

  /**
   * Constant for a HTTP gone condition (HTTP status code 410)
   */
  int ERROR_GONE = 410;

  /**
   * Constant for a HTTP precondition failed condition (HTTP status code 412)
   */
  int ERROR_PRECONDITION_FAILED = 412;

  /**
   * Constant for a HTTP internal server error condition (HTTP status code 500)
   */
  int ERROR_INTERNAL_SERVER_ERROR = 500;

  String IMAGE_BMP = "image/bmp";
  String IMAGE_GIF = "image/gif";
  String IMAGE_JPEG = "image/jpeg";
  String IMAGE_PNG = "image/png";

  String VIDEO_3GPP = "video/3gpp";
  String VIDEO_AVI = "video/avi";
  String VIDEO_QUICKTIME = "video/quicktime";
  String VIDEO_MP4 = "video/mp4";
  String VIDEO_MPEG = "video/mpeg";
  String VIDEO_MPEG4 = "video/mpeg4";
  String VIDEO_MSVIDEO = "video/msvideo";
  String VIDEO_X_MS_ASF = "video/x-ms-asf";
  String VIDEO_X_MS_WMV = "video/x-ms-wmv";
  String VIDEO_X_MSVIDEO = "video/x-msvideo";

  GDataResponse executeDelete(String uri) throws IOException;

  GDataResponse executeDeleteIfNotModified(String uri, String etag)
      throws IOException;

  GDataResponse executeGet(String uri) throws IOException;

  GDataResponse executeGetIfModified(String uri, String etag)
      throws IOException;

  GDataResponse executePatchIfNotModified(String uri, String etag,
      GDataSerializer content, String patchContentType) throws IOException;

  GDataResponse executePost(String uri, GDataSerializer content)
      throws IOException;

  /**
   * For example, to post media from a file, use {@code executePostMedia(uri,
   * file.getName(), mediaType, new FileInputStream(file), file.length()}.
   */
  GDataResponse executePostMedia(String uri, String fileName, String mediaType,
      InputStream mediaContent, long mediaContentLength) throws IOException;

  GDataResponse executePostMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException;

  GDataResponse executePutIfNotModified(String uri, String etag,
      GDataSerializer content) throws IOException;

  GDataResponse executePutMediaIfNotModified(String uri, String etag,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException;

  GDataResponse executePutMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException;

  // TODO: support If-Modified-Since or does ETag deprecate it?
  // TODO: consider a request-style model
  // TODO: support running operations async with a callback?
  // TODO: ability to set additional arbitrary headers per request?
}
