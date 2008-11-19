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


package com.google.gdata.client.blogger;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.data.blogger.BlogCommentFeed;
import com.google.gdata.data.blogger.BlogFeed;
import com.google.gdata.data.blogger.BlogPostFeed;
import com.google.gdata.data.blogger.PostCommentFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link GoogleService} abstraction to define a service that
 * is preconfigured for access to the Blogger data API.
 *
 * 
 */
public class BloggerService extends GoogleService {

  /**
   * The abbreviated name of Blogger recognized by Google.  The service name is
   * used when requesting an authentication token.
   */
  public static final String BLOGGER_SERVICE = "blogger";

  /**
   * The version ID of the service.
   */
  public static final String BLOGGER_SERVICE_VERSION = "GBlogger-Java/" +
      BloggerService.class.getPackage().getImplementationVersion();

  /**
   * GData versions supported by Blogger Service.
   */
  public static final class Versions {

    /** The original version of the Blogger service based on V1 of the GData
     * core protocol. */
    public static final Version V1 = new Version(BloggerService.class, "1.0",
        Service.Versions.V1);

    /** The V2 of the Blogger service based on V2 of the GData core protocol
     * that brings it to full alignment with the now standard Atom Publishing
     * Protocol specification. */
    public static final Version V2 = new Version(BloggerService.class, "2.0",
        Service.Versions.V2);

  }

  /**
   * Default GData version used by the Blogger service.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(BloggerService.class, Versions.V2);

  /**
   * Constructs an instance connecting to the Blogger service for an application
   * with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public BloggerService(String applicationName) {
    super(BLOGGER_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Blogger service for an application
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
  public BloggerService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Blogger service with name {@code
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
  public BloggerService(String applicationName, String protocol,
      String domainName) {
    super(BLOGGER_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return BLOGGER_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Blogger service.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(BloggerService.class);
  }

  /**
   * Declare the extensions of the feeds for the Blogger service.
   */
  private void declareExtensions() {
    new BlogCommentFeed().declareExtensions(extProfile);
    new BlogFeed().declareExtensions(extProfile);
    new BlogPostFeed().declareExtensions(extProfile);
    new PostCommentFeed().declareExtensions(extProfile);
  }

  /**
   * GData version used for feeds on Blog*Spot.
   */
  public static final Version BLOGSPOT_VERSION = Versions.V2;
}
