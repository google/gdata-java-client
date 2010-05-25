package com.google.api.client.auth.oauth;

import com.google.api.client.http.HttpExecuteIntercepter;
import com.google.api.client.http.HttpRequest;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author Yaniv Inbar
 */
final class OAuthAuthorizationHeaderIntercepter implements
    HttpExecuteIntercepter {

  OAuthParameters oauthParameters;

  public void intercept(HttpRequest request) throws IOException {
    oauthParameters.computeNonce();
    oauthParameters.computeTimestamp();
    try {
      oauthParameters.computeSignature(request.method, request.url);
    } catch (GeneralSecurityException e) {
      IOException io = new IOException();
      io.initCause(e);
      throw io;
    }
    request.headers.authorization =
        oauthParameters.getAuthorizationHeader();
  }
}
