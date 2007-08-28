/* Copyright (c) 2006 Google Inc.
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
package com.google.gdata.client.docs;

import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.docs.DocumentListFeed;

/**
 * The DocsService class extends the basic {@link MediaService}
 * abstraction to define a service that is preconfigured for access
 * to the Google Docs data API.
 *
 * 
 */
public class DocsService extends MediaService {

  /**
   * The abbreviated name of Docs recognized by Google.  The service
   * name is used while requesting an authentication token.
   */
  public static final String DOCS_SERVICE = "writely";

  /**
   * The version ID of the service.
   */
  public static final String DOCS_SERVICE_VERSION =
      "GDocs-Java/" +
      DocsService.class.getPackage().getImplementationVersion();

  /**
   * Constructs a DocsService instance connecting to the
   * Documents service for an application with the name
   * {@code applicationName}
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */
  public DocsService(String applicationName) {
    super(DOCS_SERVICE, applicationName);
    addExtensions();
  }

  /**
   * Constructs a GoogleService instance connecting to the service with name
   * {@code serviceName} for an application with the name
   * {@code applicationName}.  The service will authenticate at the provided
   * {@code domainName}.
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   * @param protocol        name of protocol to use for authentication
   *                        ("http"/"https")
   * @param domainName      the name of the domain hosting the login handler
   */
  public DocsService(String applicationName,
                            String protocol,
                            String domainName) {
    super(DOCS_SERVICE, applicationName, protocol, domainName);
    addExtensions();
  }

  /**
   * Adds the Google Docs extensions.
   */
  public void addExtensions() {
    ExtensionProfile extensionProfile = getExtensionProfile();
    new DocumentListFeed().declareExtensions(extensionProfile);
  }

  /**
   * Service version ID.
   *
   * @return service version ID
   */
  public String getServiceVersion() {
    return DOCS_SERVICE_VERSION + " " + super.getServiceVersion();
  }
}
