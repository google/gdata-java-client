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

package com.google.api.data.sample.buzz.model;

import com.google.api.client.googleapis.GoogleUrl;

/**
 * Buzz URL builder.
 * 
 * @author Yaniv Inbar
 */
public final class BuzzUrl extends GoogleUrl {

  public static final String ROOT_URL = "https://www.googleapis.com/buzz/v1/";

  /** Debug flag. Enabling will show HTTP request/response details. */
  public static final boolean DEBUG = true;

  /** Constructs a new Buzz URL from the given encoded URI. */
  public BuzzUrl(String encodedUrl) {
    super(encodedUrl);
    alt = "json";
    if (DEBUG) {
      prettyprint = true;
    }
  }

  /**
   * Constructs a new Buzz URL based on the given relative path.
   * 
   * @param relativePath encoded path relative to the {@link #ROOT_URL}
   * @return new Buzz URL
   */
  public static BuzzUrl relativeToRoot(String relativePath) {
    return new BuzzUrl(ROOT_URL + relativePath);
  }
}
