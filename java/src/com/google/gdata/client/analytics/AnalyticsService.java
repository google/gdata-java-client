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


package com.google.gdata.client.analytics;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.data.analytics.AbPageVariationFeed;
import com.google.gdata.data.analytics.AccountFeed;
import com.google.gdata.data.analytics.CombinationFeed;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.ExperimentFeed;
import com.google.gdata.data.analytics.ManagementFeed;
import com.google.gdata.data.analytics.SectionFeed;
import com.google.gdata.data.analytics.VariationFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link GoogleService} abstraction to define a service that
 * is preconfigured for access to the Google Analytics Data Export API.
 *
 * 
 */
public class AnalyticsService extends GoogleService {

  /**
   * The abbreviated name of Google Analytics Data Export API recognized by
   * Google.  The service name is used when requesting an authentication token.
   */
  public static final String ANALYTICS_SERVICE = "analytics";

  /**
   * The version ID of the service.
   */
  public static final String ANALYTICS_SERVICE_VERSION = "GAnalytics-Java/" +
      AnalyticsService.class.getPackage().getImplementationVersion();

  /** GData versions supported by the Google Analytics Data Export API. */
  public static final class Versions {

    /** Version 1.  Based on GData version 1. */
    public static final Version V1 = new Version(AnalyticsService.class, "1.0",
        Service.Versions.V1);

    /** Version 2.  This version adds OAuth support, fully complies with the
     * Atom Publishing Protocol, upgrades OpenSearch to 1.1,
     * and is based on GData version 2. */
    public static final Version V2 = new Version(AnalyticsService.class, "2.0",
        Service.Versions.V2);

    /** Version {@code 2.1}.  This version adds generalized account feeds,
     * gd:kind attributes, advanced segment support, expanded goal information,
     * and Website Optimizer support, and is based on GData version 2.1. */
    public static final Version V2_1 = new Version(AnalyticsService.class,
        "2.1", Service.Versions.V2_1);

    /** Version {@code 2.2}.  This version adds Management API feeds, and is
     * based on GData version 2.1. */
    public static final Version V2_2 = new Version(AnalyticsService.class,
        "2.2", Service.Versions.V2_1);

    /** Version {@code 2.3}.  This version adds containsSampledData, and is
     * based on GData version 2.1. */
    public static final Version V2_3 = new Version(AnalyticsService.class,
        "2.3", Service.Versions.V2_1);

    private Versions() {}
  }

  /**
   * Default GData version used by the Google Analytics Data Export API.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(AnalyticsService.class, Versions.V2_3);

  /**
   * Constructs an instance connecting to the Google Analytics Data Export API
   * for an application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public AnalyticsService(String applicationName) {
    super(ANALYTICS_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Analytics Data Export API
   * for an application with the name {@code applicationName} and the given
   * {@code GDataRequestFactory} and {@code AuthTokenFactory}. Use this
   * constructor to override the default factories.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   * @param requestFactory the request factory that generates gdata request
   *     objects
   * @param authTokenFactory the factory that creates auth tokens
   */
  public AnalyticsService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Analytics Data Export API
   * with name {@code serviceName} for an application with the name {@code
   * applicationName}.  The service will authenticate at the provided {@code
   * domainName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   * @param protocol        name of protocol to use for authentication
   *     ("http"/"https")
   * @param domainName      the name of the domain hosting the login handler
   */
  public AnalyticsService(String applicationName, String protocol,
      String domainName) {
    super(ANALYTICS_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return ANALYTICS_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Google Analytics Data Export
   * API.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(AnalyticsService.class);
  }

  /**
   * Declare the extensions of the feeds for the Google Analytics Data Export
   * API.
   */
  private void declareExtensions() {
    new AbPageVariationFeed().declareExtensions(extProfile);
    new AccountFeed().declareExtensions(extProfile);
    new CombinationFeed().declareExtensions(extProfile);
    new DataFeed().declareExtensions(extProfile);
    new ExperimentFeed().declareExtensions(extProfile);
    new ManagementFeed().declareExtensions(extProfile);
    new SectionFeed().declareExtensions(extProfile);
    new VariationFeed().declareExtensions(extProfile);
  }

}
