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


package com.google.gdata.client.health;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.data.health.ProfileFeed;
import com.google.gdata.data.health.RegisterFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link GoogleService} abstraction to define a service that
 * is preconfigured for access to the Health data API.
 *
 * 
 */
public class HealthService extends GoogleService {

  /**
   * The abbreviated name of Health recognized by Google.  The service name is
   * used when requesting an authentication token.
   */
  public static final String HEALTH_SERVICE = "health";

  /**
   * The version ID of the service.
   */
  public static final String HEALTH_SERVICE_VERSION = "GHealth-Java/" +
      HealthService.class.getPackage().getImplementationVersion();

  /**
   * GData versions supported by Health Service.
   */
  public static final class Versions {

    /** Initial version of the API, based on GData version 1. */
    public static final Version V1 = new Version(HealthService.class, "1.0",
        Service.Versions.V1);

    /** Newer version of the API, based on GData version 2. */
    public static final Version V2 = new Version(HealthService.class, "2.0",
        Service.Versions.V2);

  }

  /**
   * Default GData version used by the Health service.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(HealthService.class, Versions.V2);

  /**
   * Constructs an instance connecting to the Health service for an application
   * with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public HealthService(String applicationName) {
    super(HEALTH_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Health service for an application
   * with the name {@code applicationName} and the given {@code
   * GDataRequestFactory} and {@code AuthTokenFactory}. Use this constructor to
   * override the default factories.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   * @param requestFactory the request factory that generates gdata request
   *     objects
   * @param authTokenFactory the factory that creates auth tokens
   */
  public HealthService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Health service with name {@code
   * serviceName} for an application with the name {@code applicationName}.  The
   * service will authenticate at the provided {@code domainName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   * @param protocol        name of protocol to use for authentication
   *     ("http"/"https")
   * @param domainName      the name of the domain hosting the login handler
   */
  public HealthService(String applicationName, String protocol,
      String domainName) {
    super(HEALTH_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return HEALTH_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Health service.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(HealthService.class);
  }

  /**
   * Declare the extensions of the feeds for the Health service.
   */
  private void declareExtensions() {
    new ProfileFeed().declareExtensions(extProfile);
    new RegisterFeed().declareExtensions(extProfile);
  }

}
