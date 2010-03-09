// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http.apache;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

final class HttpPatch extends HttpEntityEnclosingRequestBase {

  public HttpPatch(String uri) {
    setURI(URI.create(uri));
  }

  @Override
  public String getMethod() {
    return "PATCH";
  }
}
