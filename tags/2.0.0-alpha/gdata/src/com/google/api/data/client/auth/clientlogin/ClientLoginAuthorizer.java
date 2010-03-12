// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.auth.clientlogin;

import com.google.api.data.client.auth.AuthorizedRequest;
import com.google.api.data.client.auth.Authorizer;

public final class ClientLoginAuthorizer implements Authorizer {

  final String authorization;

  final class ClientLoginAuthorizedRequest implements AuthorizedRequest {

    private final String uri;

    ClientLoginAuthorizedRequest(String uri) {
      this.uri = uri;
    }

    public String getUri() {
      return this.uri;
    }

    public int getHeaderCount() {
      return 1;
    }

    public String getHeaderName(int index) {
      return "Authorization";
    }

    public String getHeaderValue(int index) {
      return ClientLoginAuthorizer.this.authorization;
    }
  }

  public ClientLoginAuthorizer(String authToken) {
    this.authorization = "GoogleLogin auth=" + authToken;
  }

  public AuthorizedRequest getAuthorizedRequest(String method, String uri) {
    return new ClientLoginAuthorizedRequest(uri);
  }
}
