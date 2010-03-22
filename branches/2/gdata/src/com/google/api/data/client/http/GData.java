package com.google.api.data.client.http;

import com.google.api.data.client.v2.escape.CharEscapers;

public class GData {

  public static void setVersionHeader(HttpTransport transport, String version) {
    transport.setDefaultHeader("GData-Version", version);
  }

  public static void setSlugHeader(HttpRequest request, String fileName) {
    request.addHeader("Slug", CharEscapers.escapeSlug(fileName));
  }

  public static void setIfNoneMatchHeader(HttpRequest request, String etag) {
    request.addHeader("If-None-Match", etag);
  }
  
  public static void setIfMatchHeader(HttpRequest request, String etag) {
    request.addHeader("If-Match", etag);
  }
  
  private GData() {
  }
}
