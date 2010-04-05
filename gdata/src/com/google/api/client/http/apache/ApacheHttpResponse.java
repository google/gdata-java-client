// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http.apache;

import com.google.api.data.client.http.LowLevelHttpResponseInterface;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;

import java.io.IOException;
import java.io.InputStream;

final class ApacheHttpResponse implements LowLevelHttpResponseInterface {

  private final org.apache.http.HttpResponse response;
  private final Header[] allHeaders;

  ApacheHttpResponse(org.apache.http.HttpResponse response) {
    this.response = response;
    this.allHeaders = response.getAllHeaders();
  }

  public int getStatusCode() {
    StatusLine statusLine = this.response.getStatusLine();
    return statusLine == null ? 0 : statusLine.getStatusCode();
  }

  public InputStream getContent() throws IOException {
    HttpEntity entity = this.response.getEntity();
    return entity == null ? null : entity.getContent();
  }

  public String getContentEncoding() {
    HttpEntity entity = this.response.getEntity();
    if (entity != null) {
      Header contentEncodingHeader = entity.getContentEncoding();
      if (contentEncodingHeader != null) {
        return contentEncodingHeader.getValue();
      }
    }
    return null;
  }

  public long getContentLength() {
    HttpEntity entity = this.response.getEntity();
    return entity == null ? -1 : entity.getContentLength();
  }

  public String getContentType() {
    HttpEntity entity = this.response.getEntity();
    if (entity != null) {
      Header contentTypeHeader = entity.getContentType();
      if (contentTypeHeader != null) {
        return contentTypeHeader.getValue();
      }
    }
    return null;
  }

  public String getReasonPhrase() {
    StatusLine statusLine = this.response.getStatusLine();
    return statusLine == null ? null : statusLine.getReasonPhrase();
  }

  public String getStatusLine() {
    StatusLine statusLine = this.response.getStatusLine();
    return statusLine == null ? null : statusLine.toString();
  }

  public String getHeaderValue(String name) {
    return this.response.getLastHeader(name).getValue();
  }

  public int getHeaderCount() {
    return this.allHeaders.length;
  }

  public String getHeaderName(int index) {
    return this.allHeaders[index].getName();
  }

  public String getHeaderValue(int index) {
    return this.allHeaders[index].getValue();
  }
}
