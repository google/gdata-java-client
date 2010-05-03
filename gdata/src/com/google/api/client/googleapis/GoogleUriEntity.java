package com.google.api.client.googleapis;

import com.google.api.client.http.UriEntity;

/**
 * Google URI entity provides for some common query parameters used in Google
 * API's such as the {@code "alt"} and {@code "fields"} parameters.
 */
public class GoogleUriEntity extends UriEntity {
  public String alt;
  public String fields;

  /** Constructs from the given encoded URI. */
  public GoogleUriEntity(String uri) {
    super(uri);
  }
}
