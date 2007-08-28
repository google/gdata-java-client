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


package com.google.gdata.client.youtube;

import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.youtube.CommentFeed;
import com.google.gdata.data.youtube.FriendFeed;
import com.google.gdata.data.youtube.PlaylistFeed;
import com.google.gdata.data.youtube.PlaylistLinkFeed;
import com.google.gdata.data.youtube.SubscriptionFeed;
import com.google.gdata.data.youtube.UserProfileFeed;
import com.google.gdata.data.youtube.VideoFeed;

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
   * Creates a new instance of the service with the given application name.
   * 
   * @param applicationName should be a string identifying the application using
   *        the API, usually in this format:
   *        [company-id]-[app-name]-[app-version].
   */
  public YouTubeService(String applicationName) {
    this(applicationName, DEFAULT_AUTH_URL);
  }

  /**
   * Creates a new instance of the service with the given application name and a
   * custom user authentication URL.
   * 
   * @param applicationName should be a string identifying the application using
   *        the API, usually in this format:
   *        [company-id]-[app-name]-[app-version].
   * @param authBaseUrl the base URL pointing to the authentication server.
   */
  public YouTubeService(String applicationName, URL authBaseUrl) {
    super(SERVICE_NAME, applicationName, authBaseUrl.getProtocol(),
        authBaseUrl.getHost() + ":" + authBaseUrl.getPort()
            + authBaseUrl.getPath());

    ExtensionProfile profile = getExtensionProfile();

    profile.addDeclarations(new CommentFeed());
    profile.addDeclarations(new FriendFeed());
    profile.addDeclarations(new PlaylistFeed());
    profile.addDeclarations(new PlaylistLinkFeed());
    profile.addDeclarations(new SubscriptionFeed());
    profile.addDeclarations(new UserProfileFeed());
    profile.addDeclarations(new VideoFeed());
  }

  @Override
  public String getServiceVersion() {
    return new StringBuilder()
        .append(SERVICE_VERSION)
        .append(' ')
        .append(super.getServiceVersion())
        .toString();
  }
}
