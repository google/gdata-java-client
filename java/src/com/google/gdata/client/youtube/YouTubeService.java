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


package com.google.gdata.client.youtube;

import com.google.gdata.client.Service;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.ParseSource;
import com.google.gdata.data.youtube.ChannelFeed;
import com.google.gdata.data.youtube.CommentFeed;
import com.google.gdata.data.youtube.ComplaintFeed;
import com.google.gdata.data.youtube.FormUploadToken;
import com.google.gdata.data.youtube.FriendFeed;
import com.google.gdata.data.youtube.PlaylistFeed;
import com.google.gdata.data.youtube.PlaylistLinkFeed;
import com.google.gdata.data.youtube.RatingFeed;
import com.google.gdata.data.youtube.SubscriptionFeed;
import com.google.gdata.data.youtube.UserEventFeed;
import com.google.gdata.data.youtube.UserProfileFeed;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Java client service for the YouTube GData APIs.
 * 
 * 
 */
public class YouTubeService extends MediaService {
  private static final String SERVICE_NAME = "youtube";

  private static final String SERVICE_VERSION = "YouTube-Java/1.0";

  private static final URL DEFAULT_AUTH_URL;

  static {
    try {
      DEFAULT_AUTH_URL = new URL("https://www.google.com/youtube");
    } catch (MalformedURLException abnormal) {
      throw new IllegalStateException(abnormal);
    }
  }

  /**
   * All released version of the YouTube API.
   */
  public static class Versions {
    /**
     * Initial version of the API, based on GData version 1.
     */
    public static final Version V1 =
        new Version(YouTubeService.class, "1.0", Service.Versions.V1);

    /**
     * Newer version of the API, based on GData version 2.
     */
    public static final Version V2 =
        new Version(YouTubeService.class, "2.0", Service.Versions.V2);

    /**
     * A shortcut to the latest version.
     */
    public static final Version LATEST = V2;

    public static final Version[] ALL = { V1, V2 };
  }

  /**
   * Version 2 is currently the default version for clients.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(YouTubeService.class, YouTubeService.Versions.V2);

  /**
   * Creates a new instance of the service with the given application name.
   * 
   * @param applicationName should be a string identifying the application using
   *        the API, usually in this format:
   *        [company-id]-[app-name]-[app-version].
   *        This is also used as the client id.
   */
  public YouTubeService(String applicationName) {
    this(applicationName, null, DEFAULT_AUTH_URL);
  }

  /**
   * Creates a new instance of the service with the given application name.
   * 
   * @param applicationName should be a string identifying the application using
   *        the API, usually in this format:
   *        [company-id]-[app-name]-[app-version].
   *        This is also used as the client id.
   * @param developerId the developer id to send in every request made through
   *        this client, can be null.
   */
  public YouTubeService(String applicationName, String developerId) {
    this(applicationName, developerId, DEFAULT_AUTH_URL);
  }

  /**
   * Creates a new instance of the service with the given application name and a
   * custom user authentication URL.
   * 
   * @param applicationName should be a string identifying the application using
   *        the API, usually in this format:
   *        [company-id]-[app-name]-[app-version].
   *        This is also used as the client id.
   * @param developerId the developer id to send in every request made through
   *        this client, can be null.
   * @param authBaseUrl the base URL pointing to the authentication server.
   */
  protected YouTubeService(String applicationName, String developerId, URL authBaseUrl) {
    super(SERVICE_NAME, applicationName, authBaseUrl.getProtocol(),
        authBaseUrl.getHost()
            + (authBaseUrl.getPort() == -1 ? "" : ":" + authBaseUrl.getPort())
            + authBaseUrl.getPath());
    getRequestFactory().setHeader("X-GData-Key", developerId != null ? "key=" + developerId : null);
    getRequestFactory().setHeader("X-GData-Client", applicationName);

    ExtensionProfile profile = getExtensionProfile();
    profile.addDeclarations(new ChannelFeed());
    profile.addDeclarations(new ComplaintFeed());
    profile.addDeclarations(new CommentFeed());
    profile.addDeclarations(new FriendFeed());
    profile.addDeclarations(new UserEventFeed());
    profile.addDeclarations(new PlaylistFeed());
    profile.addDeclarations(new PlaylistLinkFeed());
    profile.addDeclarations(new RatingFeed());
    profile.addDeclarations(new SubscriptionFeed());
    profile.addDeclarations(new UserProfileFeed());
    profile.addDeclarations(new VideoFeed());

    setStrictValidation(false);
  }

  @Override
  public String getServiceVersion() {
    return new StringBuilder()
        .append(SERVICE_VERSION)
        .append(' ')
        .append(super.getServiceVersion())
        .toString();
  }

  /**
   * Returns the current {@link Version} of the YouTube GData API.
   * 
   * @return version.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(YouTubeService.class);
  }

  /**
   * Returns true if the current YouTube GData API version {@link #getVersion()}
   * is compatible with the given version.
   * 
   * @param version version to check compatibility with.
   * @return true if the current version is compatible with the given version,
   *         false otherwise.
   */
  public static boolean isCompatible(Version version) {
    if (version == null) {
      throw new NullPointerException("Version cannot be null.");
    }
    
    return getVersion().isCompatible(version);
  }

  /**
   * Generate a form-upload token given the XML description of a new media entry.
   *
   * @param url link with rel={@link YouTubeNamespace#GET_UPLOAD_TOKEN_REL} on a user's
   *            upload feed
   * @param entry XML metadata of a new media entry
   */
  @SuppressWarnings("unchecked")
  public <E extends IEntry> FormUploadToken getFormUploadToken(URL url, E entry) 
      throws ServiceException, IOException {

    if (entry == null) {
      throw new NullPointerException("Must supply entry");
    }

    Service.GDataRequest request = createInsertRequest(url);
    writeRequestData(request, entry);
    request.execute();

    ParseSource resultEntrySource = request.getParseSource();
    try {
      return FormUploadToken.parse(resultEntrySource.getInputStream());
    } finally {
      request.end();
    }
  }
}
