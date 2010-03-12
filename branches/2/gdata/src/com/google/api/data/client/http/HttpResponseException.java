// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.IOException;

public class HttpResponseException extends IOException {

  static final long serialVersionUID = 1;

  /** Error status code of the response. */
  public final int errorCode;

  /** Error message of the response. */
  public final String errorMessage;

  public String content;

  /**
   * @param response HTTP response
   */
  public HttpResponseException(HttpResponse response) {
    this.errorCode = response.getStatusCode();
    this.errorMessage = response.getStatusMessage();
  }

  @Override
  public String getMessage() {
    StringBuilder messageBuilder = new StringBuilder().append(this.errorCode);
    String reasonPhrase = this.errorMessage;
    if (reasonPhrase != null) {
      messageBuilder.append(' ').append(reasonPhrase);
    }
    String message = this.content;
    if (message != null) {
      messageBuilder.append(' ').append(message);
    }
    return messageBuilder.toString();
  }

}
