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

package com.google.api.client.http;

import java.io.IOException;

/**
 * Low-level HTTP transport.
 * <p>
 * This allows providing a different implementation of the HTTP transport that
 * is more compatible with the Java environment used.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public abstract class LowLevelHttpTransport {

  /**
   * Returns whether this HTTP transport implementation supports the {@code
   * PATCH} request method.
   */
  public abstract boolean supportsPatch();

  /** Builds a {@code DELETE} request. */
  public abstract LowLevelHttpRequest buildDeleteRequest(String url)
      throws IOException;

  /** Builds a {@code GET} request. */
  public abstract LowLevelHttpRequest buildGetRequest(String url)
      throws IOException;

  /**
   * Builds a {@code PATCH} request. Won't be called if {@link #supportsPatch()}
   * returns {@code false}.
   */
  public abstract LowLevelHttpRequest buildPatchRequest(String url)
      throws IOException;

  /** Builds a {@code POST} request. */
  public abstract LowLevelHttpRequest buildPostRequest(String url)
      throws IOException;

  /** Builds a {@code PUT} request. */
  public abstract LowLevelHttpRequest buildPutRequest(String url)
      throws IOException;
}
