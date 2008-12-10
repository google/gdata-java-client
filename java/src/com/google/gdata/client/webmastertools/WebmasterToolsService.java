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


package com.google.gdata.client.webmastertools;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.data.webmastertools.CrawlIssuesFeed;
import com.google.gdata.data.webmastertools.KeywordsFeed;
import com.google.gdata.data.webmastertools.MessagesFeed;
import com.google.gdata.data.webmastertools.SitemapsFeed;
import com.google.gdata.data.webmastertools.SitesFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link GoogleService} abstraction to define a service that
 * is preconfigured for access to the Webmaster Tools data API.
 *
 * 
 */
public class WebmasterToolsService extends GoogleService {

  /**
   * The abbreviated name of Webmaster Tools recognized by Google.  The service
   * name is used when requesting an authentication token.
   */
  public static final String WEBMASTERTOOLS_SERVICE = "sitemaps";

  /**
   * The version ID of the service.
   */
  public static final String WEBMASTERTOOLS_SERVICE_VERSION =
      "GWebmasterTools-Java/" +
      WebmasterToolsService.class.getPackage().getImplementationVersion();

  /**
   * GData versions supported by Webmaster Tools Service.
   */
  public static final class Versions {

    /** Version 1 of the Webmaster Tools Google Data API. This is the initial
     * version of the API and is based in Version 1 of the GData protocol. */
    public static final Version V1 = new Version(WebmasterToolsService.class,
        "1.0", Service.Versions.V1);

    /** Version 2 of the Webmaster Tools GData API. This version adds full
     * compliance with the Atom Publishing Protocol and is based on Version 2 of
     * the GData protocol. */
    public static final Version V2 = new Version(WebmasterToolsService.class,
        "2.0", Service.Versions.V2);

  }

  /**
   * Default GData version used by the Webmaster Tools service.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(WebmasterToolsService.class, Versions.V2);

  /**
   * Constructs an instance connecting to the Webmaster Tools service for an
   * application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public WebmasterToolsService(String applicationName) {
    super(WEBMASTERTOOLS_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Webmaster Tools service for an
   * application with the name {@code applicationName} and the given {@code
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
  public WebmasterToolsService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Webmaster Tools service with name
   * {@code serviceName} for an application with the name {@code
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
  public WebmasterToolsService(String applicationName, String protocol,
      String domainName) {
    super(WEBMASTERTOOLS_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return WEBMASTERTOOLS_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Webmaster Tools service.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(WebmasterToolsService.class);
  }

  /**
   * Declare the extensions of the feeds for the Webmaster Tools service.
   */
  private void declareExtensions() {
    new CrawlIssuesFeed().declareExtensions(extProfile);
    new KeywordsFeed().declareExtensions(extProfile);
    new MessagesFeed().declareExtensions(extProfile);
    new SitemapsFeed().declareExtensions(extProfile);
    new SitesFeed().declareExtensions(extProfile);
  }

}
