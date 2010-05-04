// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.sample.youtube.model;

import com.google.api.client.googleapis.GoogleUriEntity;
import com.google.api.client.util.Name;

public class YouTubeUri extends GoogleUriEntity {

  public static boolean DEBUG = false;
  
  public String author;

  @Name("max-results")
  public Integer maxResults;

  public YouTubeUri(String uri) {
    super(uri);
    this.alt = "jsonc";
    if (DEBUG) {
      this.prettyprint = true;
    }
  }
}