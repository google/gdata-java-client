// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import com.google.api.data.client.auth.Authorizer;
import com.google.api.data.client.http.HttpSerializer;
import com.google.api.data.client.http.Response;
import com.google.api.data.client.http.Transport;
import com.google.api.data.client.v2.GDataClient;
import com.google.api.data.client.v2.jsonc.JsoncEntity;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public final class JsoncClient {
  static final String CONTENT_TYPE = "application/json";

  public static final class Builder {

    public Authorizer authorizer;
    public String applicationName;
    public String version;

    public JsoncClient build() {
      return new JsoncClient(this.applicationName, this.authorizer,
          this.version);
    }
  }

  private final GDataClient gdataClient;

  JsoncClient(String applicationName, Authorizer authorizer, String version) {
    Transport transport =
        new Transport(applicationName + "(jsonc)", authorizer);
    this.gdataClient = new GDataClient(transport, version);
  }

  // TODO: GDataJsoncRequest with an execute() method?
  public JsoncFeedResponse executeGetFeed(String uri) throws IOException,
      JsoncException {
    return new JsoncFeedResponse(executeGet(uri));
  }

  /**
   * Executes a JSON-C request to retrieve an item at a given URI.
   * 
   * @return GData JSON-C response
   * @throws IOException low-level networking problem
   * @throws JsoncException GData error response
   */
  public JsoncItemResponse executeGetItem(String uri) throws IOException,
      JsoncException {
    return new JsoncItemResponse(executeGet(uri));
  }

  public JsoncFeedResponse executeGetFeedIfModified(String uri, String etag)
      throws IOException, JsoncException {
    return new JsoncFeedResponse(executeGetIfModified(uri, etag));
  }

  /**
   * Executes a JSON-C request to retrieve an item at a given URI, checking if
   * it has been modified using an ETag.
   * 
   * @return JSON parser for the response if it has been modified
   * 
   * @throws IOException low-level networking problem
   * @throws JsoncException GData error response, including a {@code 304
   *         "Not Modified"} if it has not been modified
   */
  public JsoncItemResponse executeGetItemIfModified(String uri, String etag)
      throws IOException, JsoncException {
    return new JsoncItemResponse(executeGetIfModified(uri, etag));
  }

  public InputStream executeGetMedia(String uri) throws IOException,
      JsoncException {
    return processResponse(this.gdataClient.executeGet(uri));
  }

  public JsoncItemResponse executePostItem(String uri, Object item)
      throws IOException, JsoncException {
    Response response =
        this.gdataClient.executePost(uri, new JsoncSerializer(item));
    return new JsoncItemResponse(parseUpToData(response));
  }

  public JsoncItemResponse executePostMedia(String uri, String fileName,
      HttpSerializer mediaContent) throws IOException, JsoncException {
    Response response =
        this.gdataClient.executePostMedia(uri, fileName, mediaContent);
    return new JsoncItemResponse(parseUpToData(response));
  }

  public JsoncItemResponse executePatchItemWithMaskIfNotModified(String uri,
      String etag, Object patchedItem, String partialMask) throws IOException,
      JsoncException {
    Response response =
        this.gdataClient.executePatchIfNotModified(uri, etag,
            new JsoncPatchWithMaskSerializer(patchedItem, partialMask));
    return new JsoncItemResponse(parseUpToData(response));
  }

  public JsoncItemResponse executePatchItemRelativeToOriginalIfNotModified(
      String uri, String etag, Object patchedItem, Object originalItem)
      throws IOException, JsoncException {
    Response response =
        this.gdataClient.executePatchIfNotModified(uri, etag,
            new JsoncPatchRelativeToOriginalSerializer(patchedItem,
                originalItem));
    return new JsoncItemResponse(parseUpToData(response));
  }

  public JsoncItemResponse executePutItemIfNotModified(String uri, String etag,
      Object item) throws IOException, JsoncException {
    if (!(item instanceof JsoncEntity)) {
      throw new IllegalArgumentException("can only post an item that extends "
          + JsoncEntity.class.getName());
      // TODO: check subclasses extend GDataJsoncObject
    }
    Response response =
        this.gdataClient.executePutIfNotModified(uri, etag,
            new JsoncSerializer(item));
    return new JsoncItemResponse(parseUpToData(response));
  }

  public JsoncItemResponse executePutMediaIfNotModified(String uri,
      String etag, HttpSerializer mediaContent) throws IOException,
      JsoncException {
    Response response =
        this.gdataClient.executePutMediaIfNotModified(uri, etag, mediaContent);
    return new JsoncItemResponse(parseUpToData(response));
  }

  public void executeDeleteItem(String uri) throws IOException, JsoncException {
    ignoreSuccessResponse(this.gdataClient.executeDelete(uri));
  }

  public void executeDeleteItemIfNotModified(String uri, String etag)
      throws IOException, JsoncException {
    ignoreSuccessResponse(this.gdataClient
        .executeDeleteIfNotModified(uri, etag));
  }

  private JsonParser executeGetIfModified(String uri, String etag)
      throws IOException, JsoncException {
    Response response = this.gdataClient.executeGetIfModified(uri, etag);
    return parseUpToData(response);
  }

  private JsonParser executeGet(String uri) throws IOException, JsoncException {
    Response response = this.gdataClient.executeGet(uri);
    return parseUpToData(response);
  }

  private static void ignoreSuccessResponse(Response response)
      throws JsoncException, IOException {
    processResponse(response).close();
  }

  private static JsonParser parseUpToData(Response response)
      throws IOException, JsoncException {
    InputStream content = processResponse(response);
    try {
      // check for JSON content type
      String contentType = response.getContentType();
      if (!contentType.startsWith(CONTENT_TYPE)) {
        throw new IllegalArgumentException("Wrong content type: expected <"
            + CONTENT_TYPE + "> but got <" + contentType + ">");
      }
      JsonParser parser = Jackson.JSON_FACTORY.createJsonParser(content);
      content = null;
      parser.nextToken();
      Jackson.skipToKey(parser, "data");
      return parser;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

  private static InputStream processResponse(Response response)
      throws JsoncException, IOException {
    if (response.isSuccessStatusCode()) {
      return response.getContent();
    }
    // TODO: use err=json (or jsonc?) to force error response to json-c?
    JsoncException exception = new JsoncException(response);
    InputStream inputStream = response.getContent();
    if (inputStream != null) {
      try {
        if (response.getContentType().startsWith(CONTENT_TYPE)) {
          JsonParser parser =
              Jackson.JSON_FACTORY.createJsonParser(inputStream);
          exception.parser = parser;
          try {
            parser.nextToken();
            Jackson.skipToKey(parser, "error");
            parser = null;
          } finally {
            if (parser != null) {
              parser.close();
            }
            inputStream = null;
          }
        } else {
          exception.content = response.parseContentAsString();
          inputStream = null;
        }
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    throw exception;
  }
}
