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


package com.google.gdata.client.appsforyourdomain.migration;

import com.google.gdata.client.Service;
import com.google.gdata.client.appsforyourdomain.AppsForYourDomainService;
import com.google.gdata.client.batch.BatchInterruptedException;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.appsforyourdomain.migration.MailItemFeed;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.Version;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * The MailItemService class is a {@link MediaService} that can upload mail to
 * the Google Apps Email Migration API.
 * 
 * <p>
 * The recommended way to perform mail migration is to issue multipart POST
 * requests, where the first part contains an Atom entry specifying the message
 * properties and labels, and the second part contains the complete RFC822
 * message as UTF-8 text.
 */
public class MailItemService extends MediaService {
  
  public static class Versions {
    
    /**
     * Version 1 of the Email Migration API. This is the initial version of the
     * API and is based upon Version 1 of the GData Protocol.
     */
    public static final Version V1 = new Version(MailItemService.class,
        "1.0", Service.Versions.V1);
    
    /**
     * Version 2 of the Email Migration API. This version of the API adds full
     * compliance with the Atom Publishing Protocol and is based upon Version 2
     * of the GData protocol.
     */
    public static final Version V2 = new Version(MailItemService.class,
        "2.0", Service.Versions.V2);
  }
  
  /**
   * Version 1 is the current default version for MailItemService.
   */
  public static final Version DEFAULT_VERSION = Service.initServiceVersion(
      MailItemService.class, Versions.V1);
  
  /**
   * Domain for sending API requests.
   */
  public static final String APPS_APIS_DOMAIN = "apps-apis.google.com";
  
  /**
   * URL prefix for accessing migration feeds.
   */
  public static final String URL_PREFIX = "/a/feeds/migration/2.0";
  
  /**
   * URL suffix for the mail item batch feed.
   */
  public static final String URL_SUFFIX = "/mail/batch";

  /**
   * Constructs a {@code MailItemService} instance for an application with the
   * name {@code applicationName}.
   * 
   * @param applicationName the name of the client application accessing the
   *        service. Application names should preferably have the format
   *        [company-id]-[app-name]-[app-version]. The name will be used by the
   *        Google servers to monitor the source of authentication.
   */
  public MailItemService(String applicationName) {
    this(applicationName, AppsForYourDomainService.HTTPS_PROTOCOL,
        AppsForYourDomainService.DOMAIN_NAME);
  }
  
  /**
   * Constructs a {@code MailItemService} instance connecting to the service
   * with name {@code serviceName} for an application with the name
   * {@code applicationName}. The service will authenticate at the provided
   * {@code domainName}.
   * 
   * @param applicationName the name of the client application accessing the
   *        service. Application names should preferably have the format
   *        [company-id]-[app-name]-[app-version]. The name will be used by the
   *        Google servers to monitor the source of authentication.
   * @param protocol name of protocol to use for authentication ("http"/"https")
   * @param domainName the name of the domain hosting the login handler
   */
  public MailItemService(String applicationName, String protocol, String domainName) {
    super(AppsForYourDomainService.APPS_SERVICE, applicationName, protocol, domainName);
    
    BatchUtils.declareExtensions(getExtensionProfile());
    new MailItemFeed().declareExtensions(getExtensionProfile());
  }

  /**
   * Inserts one or more MailItem entries in a single batch operation.  Using
   * {@code batch} instead of repeated calls to {@code insert} is helpful in
   * reducing HTTP overhead.
   * 
   * @param domain the domain into which mail is being migrated
   * @param feed a feed containing one or more {@code MailItemEntry} objects,
   *             each of which has been tagged with
   *             {@link BatchUtils#setBatchId(com.google.gdata.data.BaseEntry,
   *             String)}.  The batch operation type of each entry must be
   *             {@code BatchOperationType.INSERT}; however, there should be no
   *             need to call {@code BatchUtils.setBatchOperationType} on each
   *             entry, as this operation is already the default.
   * @return a feed with the result of each operation in a separate
   *         {@code MailItemEntry} object.
   * @throws IOException if an error occurs while communicating with the GData
   *                     service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   * @throws BatchInterruptedException if an irrecoverable error was detected
   *                                   by the server while parsing the request,
   *                                   like invalid XML data. Some operations
   *                                   might have succeeded when this exception
   *                                   is thrown. Check {@link
   *                                   BatchInterruptedException#getFeed()}.
   */
  public MailItemFeed batch(String domain, String userName, MailItemFeed feed)
      throws BatchInterruptedException, IOException, MalformedURLException,
      ServiceException {
    URL batchUrl = new URL(AppsForYourDomainService.HTTPS_PROTOCOL + "://" + APPS_APIS_DOMAIN
        + URL_PREFIX + "/" + domain + "/" + userName + URL_SUFFIX);
    return batch(batchUrl, feed);
  }


}
