// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.auth.clientlogin;

import com.google.api.data.client.auth.Authorizer;
import com.google.api.data.client.http.HttpRequest;
import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.http.Response;
import com.google.api.data.client.http.Transport;
import com.google.api.data.client.http.UrlEncodedFormHttpSerializer;

import java.io.IOException;

public final class ClientLoginAuthenticator {

  private static final String AUTH_KEY = "Auth=";

  public String authTokenType;
  public String username;
  public String password;
  public String applicationName;
  public String captchaToken;
  public String captchaAnswer;

  public Authorizer authenticate() throws IOException {
    HttpTransport httpTransport = Transport.httpTransport;
    HttpRequest request =
        httpTransport
            .buildPostRequest("https://www.google.com/accounts/ClientLogin");
    // build request content
    UrlEncodedFormHttpSerializer.Builder builder =
        new UrlEncodedFormHttpSerializer.Builder();
    builder.add("Email", this.username);
    builder.add("Passwd", this.password);
    builder.add("service", this.authTokenType);
    builder.add("source", this.applicationName);
    builder.add("accountType", "HOSTED_OR_GOOGLE");
    String captchaToken = this.captchaToken;
    if (captchaToken != null) {
      builder.add("logintoken", captchaToken);
    }
    String captchaAnswer = this.captchaAnswer;
    if (captchaAnswer != null) {
      builder.add("logincaptcha", captchaAnswer);
    }
    request.setContent(builder.build());
    // execute and parse auth key
    HttpResponse response = request.execute();
    String string =
        Response.parseContentAsString(response.getContent(), response
            .getContentLength());
    int auth = string.indexOf(AUTH_KEY);
    if (auth == -1) {
      throw new IllegalStateException("no auth key found");
    }
    String authToken =
        string.substring(auth + AUTH_KEY.length(), string.length() - 1);
    return new ClientLoginAuthorizer(authToken);
  }
}
