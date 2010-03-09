// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.auth;

public interface Authorizer {
  AuthorizedRequest getAuthorizedRequest(String method, String uri);
}
