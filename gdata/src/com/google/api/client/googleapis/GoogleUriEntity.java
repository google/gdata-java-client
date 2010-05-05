package com.google.api.client.googleapis;

import com.google.api.client.http.UriEntity;

/**
 * Google URI entity provides for some common query parameters used in Google
 * API's such as the {@link #alt} and {@link #fields} parameters.
 */
public class GoogleUriEntity extends UriEntity {

  /** Whether to pretty print the output. */
  public volatile Boolean prettyprint;

  /** Alternate wire format. */
  public volatile String alt;

  /** Partial fields mask. */
  public volatile String fields;

  /** Constructs from the given encoded URI. */
  public GoogleUriEntity(String uri) {
    super(uri);
  }
}
