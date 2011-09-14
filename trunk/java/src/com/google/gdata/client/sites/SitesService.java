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


package com.google.gdata.client.sites;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.Service;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.sites.ActivityFeed;
import com.google.gdata.data.sites.AnnouncementEntry;
import com.google.gdata.data.sites.AnnouncementsPageEntry;
import com.google.gdata.data.sites.AttachmentEntry;
import com.google.gdata.data.sites.CommentEntry;
import com.google.gdata.data.sites.ContentFeed;
import com.google.gdata.data.sites.CreationActivityEntry;
import com.google.gdata.data.sites.DeletionActivityEntry;
import com.google.gdata.data.sites.EditActivityEntry;
import com.google.gdata.data.sites.FileCabinetPageEntry;
import com.google.gdata.data.sites.ListItemEntry;
import com.google.gdata.data.sites.ListPageEntry;
import com.google.gdata.data.sites.MoveActivityEntry;
import com.google.gdata.data.sites.RecoveryActivityEntry;
import com.google.gdata.data.sites.RevisionFeed;
import com.google.gdata.data.sites.SiteFeed;
import com.google.gdata.data.sites.WebAttachmentEntry;
import com.google.gdata.data.sites.WebPageEntry;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link MediaService} abstraction to define a service that
 * is preconfigured for access to the Google Sites Data API.
 *
 * 
 */
public class SitesService extends MediaService {

  /**
   * The abbreviated name of Google Sites Data API recognized by Google.  The
   * service name is used when requesting an authentication token.
   */
  public static final String SITES_SERVICE = "jotspot";

  /**
   * The version ID of the service.
   */
  public static final String SITES_SERVICE_VERSION = "GSites-Java/" +
      (SitesService.class.getPackage() == null ? "unknown" :
      SitesService.class.getPackage().getImplementationVersion());

  /** GData versions supported by the Google Sites Data API. */
  public static final class Versions {

    /** Version 1. */
    public static final Version V1 = new Version(SitesService.class, "1.0",
        Service.Versions.V2);

    /** Version {@code 1.1}. */
    public static final Version V1_1 = new Version(SitesService.class, "1.1",
        Service.Versions.V2);

    /** Version {@code 1.2}. */
    public static final Version V1_2 = new Version(SitesService.class, "1.2",
        Service.Versions.V2);

    /** Version {@code 1.3}. */
    public static final Version V1_3 = new Version(SitesService.class, "1.3",
        Service.Versions.V2);

    /** Version {@code 1.4}. */
    public static final Version V1_4 = new Version(SitesService.class, "1.4",
        Service.Versions.V2);

    private Versions() {}
  }

  /**
   * Default GData version used by the Google Sites Data API.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(SitesService.class, Versions.V1_4);

  /**
   * Constructs an instance connecting to the Google Sites Data API for an
   * application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public SitesService(String applicationName) {
    super(SITES_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Sites Data API for an
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
  public SitesService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Sites Data API with name
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
  public SitesService(String applicationName, String protocol,
      String domainName) {
    super(SITES_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return SITES_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Google Sites Data API.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(SitesService.class);
  }

  /**
   * Declare the extensions of the feeds for the Google Sites Data API.
   */
  private void declareExtensions() {
    new AclFeed().declareExtensions(extProfile);
    new SiteFeed().declareExtensions(extProfile);
    /* Declarations for extensions that need to be handled as specific type
     * should be done before call to {@see ExtensionProfile#setAutoExtending}.
     * Order of declaration is important. */
    extProfile.setAutoExtending(true);
    new ActivityFeed().declareExtensions(extProfile);
    new AnnouncementEntry().declareExtensions(extProfile);
    new AnnouncementsPageEntry().declareExtensions(extProfile);
    new AttachmentEntry().declareExtensions(extProfile);
    new CommentEntry().declareExtensions(extProfile);
    new ContentFeed().declareExtensions(extProfile);
    new CreationActivityEntry().declareExtensions(extProfile);
    new DeletionActivityEntry().declareExtensions(extProfile);
    new EditActivityEntry().declareExtensions(extProfile);
    new FileCabinetPageEntry().declareExtensions(extProfile);
    new ListItemEntry().declareExtensions(extProfile);
    new ListPageEntry().declareExtensions(extProfile);
    new MoveActivityEntry().declareExtensions(extProfile);
    new RecoveryActivityEntry().declareExtensions(extProfile);
    new RevisionFeed().declareExtensions(extProfile);
    new WebAttachmentEntry().declareExtensions(extProfile);
    new WebPageEntry().declareExtensions(extProfile);
    BatchUtils.declareExtensions(extProfile);
  }

}

