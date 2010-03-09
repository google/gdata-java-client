// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import com.google.api.data.client.http.Response;

/** GData error response to a GData request. */
public class GDataException extends Exception {

  static final long serialVersionUID = 1;

  /** Error status code of the response. */
  public final int errorCode;

  /** Error message of the response. */
  public final String errorMessage;

  public String content;

  /**
   * @param response GData response
   */
  public GDataException(Response response) {
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
