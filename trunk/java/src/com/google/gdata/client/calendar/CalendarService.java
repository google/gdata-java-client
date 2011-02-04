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


package com.google.gdata.client.calendar;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link GoogleService} abstraction to define a service that
 * is preconfigured for access to the Google Calendar data API.
 *
 * 
 */
public class CalendarService extends GoogleService {

  /**
   * The abbreviated name of Google Calendar recognized by Google.  The service
   * name is used when requesting an authentication token.
   */
  public static final String CALENDAR_SERVICE = "cl";

  /**
   * The version ID of the service.
   */
  public static final String CALENDAR_SERVICE_VERSION = "GCalendar-Java/" +
      CalendarService.class.getPackage().getImplementationVersion();

  /**
   * GData versions supported by Google Calendar Service.
   */
  public static final class Versions {

    /** Version 1 of the Calendar Data API. */
    public static final Version V1 = new Version(CalendarService.class, "1.0",
        Service.Versions.V1);

    /** Version 2 of the Calendar Data API. */
    public static final Version V2 = new Version(CalendarService.class, "2.0",
        Service.Versions.V2);

    /** Version 2.1 of the Calendar Data API. */
    public static final Version V2_1 = new Version(CalendarService.class, "2.1",
        Service.Versions.V2);
  }

  /**
   * Default GData version used by the Google Calendar service.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(CalendarService.class, Versions.V2);

  /**
   * Constructs an instance connecting to the Google Calendar service for an
   * application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public CalendarService(String applicationName) {
    super(CALENDAR_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Calendar service for an
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
  public CalendarService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Calendar service with name
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
  public CalendarService(String applicationName, String protocol,
      String domainName) {
    super(CALENDAR_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return CALENDAR_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Google Calendar service.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(CalendarService.class);
  }

  /**
   * Declare the extensions of the feeds for the Google Calendar service.
   */
  private void declareExtensions() {
    new AclFeed().declareExtensions(extProfile);
    new CalendarEventFeed().declareExtensions(extProfile);
    new CalendarFeed().declareExtensions(extProfile);
    BatchUtils.declareExtensions(extProfile);
  }


  /**
   * The root URL of Calendar feeds.  This URL will be used to scope
   * the AuthSub (authentication for web services) tokens.
   */
  public static final String CALENDAR_ROOT_URL =
      "https://www.google.com/calendar/feeds/";

}
