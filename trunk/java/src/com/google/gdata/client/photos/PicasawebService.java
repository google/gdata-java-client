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


package com.google.gdata.client.photos;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.Service;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.CommentEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.PhotoFeed;
import com.google.gdata.data.photos.TagEntry;
import com.google.gdata.data.photos.UserEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link MediaService} abstraction to define a service that
 * is preconfigured for access to the Picasa Web Albums Data API.
 *
 * 
 */
public class PicasawebService extends MediaService {

  /**
   * The abbreviated name of Picasa Web Albums Data API recognized by Google.
   * The service name is used when requesting an authentication token.
   */
  public static final String PWA_SERVICE = "lh2";

  /** GData versions supported by the Picasa Web Albums Data API. */
  public static final class Versions {

    /** Version 1. */
    public static final Version V1 = new Version(PicasawebService.class, "1.0",
        Service.Versions.V1);

    /** Version 2. */
    public static final Version V2 = new Version(PicasawebService.class, "2.0",
        Service.Versions.V2);

    /** Version 3.  Under development. */
    public static final Version V3 = new Version(PicasawebService.class, "3.0",
        Service.Versions.V2);

    private Versions() {}
  }

  /**
   * Default GData version used by the Picasa Web Albums Data API.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(PicasawebService.class, Versions.V2);

  /**
   * Constructs an instance connecting to the Picasa Web Albums Data API for an
   * application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public PicasawebService(String applicationName) {
    super(PWA_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Picasa Web Albums Data API for an
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
  public PicasawebService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Picasa Web Albums Data API with
   * name {@code serviceName} for an application with the name {@code
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
  public PicasawebService(String applicationName, String protocol,
      String domainName) {
    super(PWA_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return PWA_SERVICE + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Picasa Web Albums Data API.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(PicasawebService.class);
  }

  /**
   * Declare the extensions of the feeds for the Picasa Web Albums Data API.
   */
  private void declareExtensions() {
    extProfile.setAutoExtending(true);
    new AlbumEntry().declareExtensions(extProfile);
    new AlbumFeed().declareExtensions(extProfile);
    new CommentEntry().declareExtensions(extProfile);
    new PhotoEntry().declareExtensions(extProfile);
    new PhotoFeed().declareExtensions(extProfile);
    new TagEntry().declareExtensions(extProfile);
    new UserEntry().declareExtensions(extProfile);
    new UserFeed().declareExtensions(extProfile);
  }

}
