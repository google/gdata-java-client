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


package com.google.gdata.client.spreadsheet;

import com.google.gdata.client.AuthTokenFactory;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.RecordFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.TableFeed;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.Version;
import com.google.gdata.util.VersionRegistry;

/**
 * Extends the basic {@link GoogleService} abstraction to define a service that
 * is preconfigured for access to the Google Spreadsheets data API.
 *
 * 
 */
public class SpreadsheetService extends GoogleService {

  /**
   * The abbreviated name of Google Spreadsheets recognized by Google.  The
   * service name is used when requesting an authentication token.
   */
  public static final String SPREADSHEET_SERVICE = "wise";

  /**
   * The version ID of the service.
   */
  public static final String SPREADSHEET_SERVICE_VERSION = "GSpread-Java/" +
      SpreadsheetService.class.getPackage().getImplementationVersion();

  /**
   * GData versions supported by Google Spreadsheets Service.
   */
  public static final class Versions {

    /** Version 1 of the Spreadsheet Google Data API. This is the initial
     * version of the API and is based upon Version 1 of the GData Protocol. */
    public static final Version V1 = new Version(SpreadsheetService.class,
        "1.0", Service.Versions.V1);

    /** Version 2 of the Spreadsheet Google Data API. This version of the API
     * adds full compliance with the Atom Publishing Protocol and is based upon
     * Version 2 of the GData protocol. */
    public static final Version V2 = new Version(SpreadsheetService.class,
        "2.0", Service.Versions.V2);

    /** Version 3 of the Spreadsheet Google Data API. This adds a new Table Feed
     * and deprecates the old feeds. */
    public static final Version V3 = new Version(SpreadsheetService.class,
        "3.0", Service.Versions.V2);

    private Versions() {}
  }

  /**
   * Default GData version used by the Google Spreadsheets service.
   */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(SpreadsheetService.class, Versions.V3);

  /**
   * Constructs an instance connecting to the Google Spreadsheets service for an
   * application with the name {@code applicationName}.
   *
   * @param applicationName the name of the client application accessing the
   *     service. Application names should preferably have the format
   *     [company-id]-[app-name]-[app-version]. The name will be used by the
   *     Google servers to monitor the source of authentication.
   */
  public SpreadsheetService(String applicationName) {
    super(SPREADSHEET_SERVICE, applicationName);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Spreadsheets service for an
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
  public SpreadsheetService(String applicationName,
      Service.GDataRequestFactory requestFactory,
      AuthTokenFactory authTokenFactory) {
    super(applicationName, requestFactory, authTokenFactory);
    declareExtensions();
  }

  /**
   * Constructs an instance connecting to the Google Spreadsheets service with
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
  public SpreadsheetService(String applicationName, String protocol,
      String domainName) {
    super(SPREADSHEET_SERVICE, applicationName, protocol, domainName);
    declareExtensions();
  }

  @Override
  public String getServiceVersion() {
    return SPREADSHEET_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Returns the current GData version used by the Google Spreadsheets service.
   */
  public static Version getVersion() {
    return VersionRegistry.get().getVersion(SpreadsheetService.class);
  }

  /**
   * Declare the extensions of the feeds for the Google Spreadsheets service.
   */
  private void declareExtensions() {
    new CellFeed().declareExtensions(extProfile);
    new ListFeed().declareExtensions(extProfile);
    new RecordFeed().declareExtensions(extProfile);
    new SpreadsheetFeed().declareExtensions(extProfile);
    new TableFeed().declareExtensions(extProfile);
    new WorksheetFeed().declareExtensions(extProfile);
  }

}
