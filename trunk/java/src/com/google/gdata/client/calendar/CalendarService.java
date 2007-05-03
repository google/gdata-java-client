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


package com.google.gdata.client.calendar;

import com.google.gdata.client.GoogleService;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.extensions.EventFeed;


/**
 * The CalendarService class extends the basic {@link GoogleService}
 * abstraction to define a service that is preconfigured for access
 * to the Google Calendar data API.
 *
 * 
 */
public class CalendarService extends GoogleService {

  /**
   * The abbreviated name of Calendar recognized by Google.  The service
   * name is used while requesting an authentication token.
   */
  public static final String CALENDAR_SERVICE = "cl";

  /**
   * The root URL of Calendar feeds.  This URL will be used to scope
   * the AuthSub (authentication for web services) tokens.
   */
  public static final String CALENDAR_ROOT_URL =
    "http://www.google.com/calendar/feeds/";

  public static final String CALENDAR_SERVICE_VERSION =
    "GCalendar-Java/" +
    CalendarService.class.getPackage().getImplementationVersion();


  /**
   * Constructs a CalendarService instance connecting to the Calendar service
   * for an application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */
  public CalendarService(String applicationName) {

    this(applicationName, "https", "www.google.com");
  }


  /**
   * Constructs a GoogleService instance connecting to the service with name
   * {@code serviceName} for an application with the name
   * {@code applicationName}.  The service will authenticate at the provided
   * {@code domainName}.
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
  public CalendarService(String applicationName,
                         String protocol,
                         String domainName) {

    super(CALENDAR_SERVICE, applicationName, protocol, domainName);

    // Configure the extension profile to expect EventFeed formatted entries
    // or Acl entries.
    new CalendarFeed().declareExtensions(getExtensionProfile());
    new EventFeed().declareExtensions(getExtensionProfile());
    new CalendarEventFeed().declareExtensions(getExtensionProfile());
    new AclFeed().declareExtensions(getExtensionProfile());
  }

  public String getServiceVersion() {
    return CALENDAR_SERVICE_VERSION + " " + super.getServiceVersion();
  }
}
