// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.apache;

import com.google.api.data.client.v2.HttpRequest;
import com.google.api.data.client.v2.HttpResponse;
import com.google.api.data.client.v2.HttpSerializer;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.InputStreamEntity;

import java.io.IOException;
import java.io.InputStream;

final class ApacheHttpRequest implements HttpRequest {
  private final HttpClient httpClient;

  private final HttpRequestBase request;

  ApacheHttpRequest(HttpClient httpClient, HttpRequestBase request) {
    this.httpClient = httpClient;
    this.request = request;
  }

  public void addHeader(String name, String value) {
    request.addHeader(name, value);
  }

  public HttpResponse execute() throws IOException {
    return new ApacheHttpResponse(this.httpClient.execute(request));
  }

  public void setContent(long contentLength, String contentType,
      String contentEncoding, HttpSerializer serializer) {
    GDataEntity entity = new GDataEntity(contentLength, serializer);
    entity.setContentEncoding(contentEncoding);
    setEntity(entity, contentType);
  }

  public void setContent(long contentLength, String contentType,
      InputStream inputStream) {
    InputStreamEntity entity =
        new InputStreamEntity(inputStream, contentLength);
    setEntity(entity, contentType);
  }

  private void setEntity(AbstractHttpEntity entity, String contentType) {
    entity.setContentType(contentType);
    ((HttpEntityEnclosingRequest) this.request).setEntity(entity);
  }
}
