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

import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.CommentEntry;
import com.google.gdata.data.photos.PhotoFeed;
import com.google.gdata.data.photos.TagEntry;
import com.google.gdata.data.photos.UserFeed;

/**
 * The PicasawebService class extends the basic {@link MediaService}
 * abstraction to define a service that is preconfigured for access
 * to the Google Picasaweb data API.
 *
 * 
 */
public class PicasawebService extends MediaService {

  /**
   * The abbreviated name of Picasaweb recognized by Google.  The service
   * name is used when requesting an authentication token.
   */
  public static final String PWA_SERVICE = "lh2";

  /**
   * Constructs a PicasawebService instance connecting to the Picasaweb service
   * for an application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */
  public PicasawebService(String applicationName) {
    this(applicationName, "https", "www.google.com");
  }

  /**
   * Constructs a GoogleService instance connecting to the pwa service
   * for an application with the name {@code applicationName}.
   * The service will authenticate at the provided {@code domainName}.
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
  public PicasawebService(String applicationName, String protocol,
      String domainName) {
    super(PWA_SERVICE, applicationName, protocol, domainName);

    // Turn on auto-extensions.
    extProfile.setAutoExtending(true);

    extProfile.addDeclarations(new UserFeed());
    extProfile.addDeclarations(new AlbumFeed());
    extProfile.addDeclarations(new PhotoFeed());
    extProfile.addDeclarations(new CommentEntry());
    extProfile.addDeclarations(new TagEntry());
  }

  @Override
  public String getServiceVersion() {
    return PWA_SERVICE + " " + super.getServiceVersion();
  }
}
