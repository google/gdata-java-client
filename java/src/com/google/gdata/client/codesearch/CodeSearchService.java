/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.client.codesearch;

import com.google.gdata.client.GoogleService;
import com.google.gdata.data.codesearch.CodeSearchFeed;

/**
* CodeSearchService Extension for Google Data
*
* 
*/

public class CodeSearchService extends GoogleService {
  /**
   * The abbreviated name of the Code Search service recognized by Google.
   * The service name is used while requesting an authentication token.
   */
  public static final String CODESEARCH_SERVICE = "codesearch";

  public static final String CODESEARCH_SERVICE_VERSION =
  "CodeSearch-Java/" +
  CodeSearchService.class.getPackage().getImplementationVersion();


  /**
   * Constructs a CodeSearchService instance connecting to the CodeSearc service
   * for an application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */

  public CodeSearchService(String applicationName) {
    super(CODESEARCH_SERVICE, applicationName, "http", "www.google.com");
    // Configure the extension profile to expect CodeSearchFeed formatted
    // entries.
    new CodeSearchFeed().declareExtensions(getExtensionProfile());
  }

  public String getServiceVersion() {
    return CODESEARCH_SERVICE_VERSION + " " + super.getServiceVersion();
  }
}
