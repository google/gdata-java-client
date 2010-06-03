/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.picasa.model;

import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.util.Key;
import com.google.api.data.picasa.v2.PicasaWebAlbums;

/**
 * @author Yaniv Inbar
 */
public class PicasaUrl extends GoogleUrl {

  @Key("max-results")
  public Integer maxResults;

  @Key
  public String kinds;

  public PicasaUrl(String url) {
    super(url);
    if (Debug.ENABLED) {
      this.prettyprint = true;
    }
  }

  /**
   * Constructs a new Picasa Web Albums URL based on the given relative path.
   * 
   * @param relativePath unencoded path relative to the
   *        {@link PicasaWebAlbums#ROOT_URL} , but not containing any query
   *        parameters
   * @return new Picasa URL
   */
  public static PicasaUrl fromRelativePath(String relativePath) {
    PicasaUrl result = new PicasaUrl(PicasaWebAlbums.ROOT_URL);
    result.appendPath(relativePath);
    return result;
  }
}
