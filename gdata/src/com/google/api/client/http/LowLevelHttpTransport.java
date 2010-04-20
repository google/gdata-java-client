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
 * Low-level HTTP tranport implementation. This allows providing a different
 * implementation of the HTTP transport is that compatible with the Java
 * environment used.
 */
public interface LowLevelHttpTransport {

  /**
   * Returns whether this HTTP transport implementation supports the {@code
   * PATCH} request method.
   */
  boolean supportsPatch();

  /** Builds a {@code DELETE} request. */
  LowLevelHttpRequest buildDeleteRequest(String uri) throws IOException;

  /** Builds a {@code GET} request. */
  LowLevelHttpRequest buildGetRequest(String uri) throws IOException;

  /**
   * Builds a {@code PATCH} request. Won't be called if {@link #supportsPatch()}
   * returns {@code false}.
   */
  LowLevelHttpRequest buildPatchRequest(String uri) throws IOException;

  /** Builds a {@code POST} request. */
  LowLevelHttpRequest buildPostRequest(String uri) throws IOException;

  /** Builds a {@code PUT} request. */
  LowLevelHttpRequest buildPutRequest(String uri) throws IOException;
}
