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
package com.google.gdata.client.docs;

import com.google.gdata.client.Service;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.Version;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The DocsService class extends the basic {@link MediaService}
 * abstraction to define a service that is preconfigured for access
 * to the Google Docs data API.
 *
 * 
 */
public class DocsService extends MediaService {

  /**
   * The abbreviated name of Docs recognized by Google.  The service
   * name is used while requesting an authentication token.
   */
  public static final String DOCS_SERVICE = "writely";

  /**
   * The version ID of the service.
   */
  public static final String DOCS_SERVICE_VERSION =
      "GDocs-Java/" +
      DocsService.class.getPackage().getImplementationVersion();

  /**
   * The Versions class contains all released versions of the Doclist Google
   * Data API
   */
  public static class Versions {

    /**
     * Version 1 of the Doclist Google Data API.  This is the initial version
     * of the API and is based on Version 1 of the GData protocol.
     */
    public static final Version V1 =
        new Version(DocsService.class, "1.0", Service.Versions.V1);

    /**
     * Version 2 of the Doclist Google Data API.  This version of the API adds
     * full compliance with the Atom Publishing Protocol and is based on
     * Version 2 of the GData protocol.
     */
    public static final Version V2 =
        new Version(DocsService.class, "2.0", Service.Versions.V1);
  }

  /** Version 1 is the current default version of DocsService. */
  public static final Version DEFAULT_VERSION =
      Service.initServiceVersion(DocsService.class, DocsService.Versions.V1);

  /**
   * Constructs a DocsService instance connecting to the
   * Documents service for an application with the name
   * {@code applicationName}
   *
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */
  public DocsService(String applicationName) {
    super(DOCS_SERVICE, applicationName);
    addExtensions();
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
  public DocsService(String applicationName,
                            String protocol,
                            String domainName) {
    super(DOCS_SERVICE, applicationName, protocol, domainName);
    addExtensions();
  }

  /**
   * Adds the Google Docs extensions.
   */
  public void addExtensions() {
    ExtensionProfile extensionProfile = getExtensionProfile();
    new DocumentListFeed().declareExtensions(extensionProfile);
    new AclFeed().declareExtensions(extensionProfile);
  }

  /**
   * Service version ID.
   *
   * @return service version ID
   */
  public String getServiceVersion() {
    return DOCS_SERVICE_VERSION + " " + super.getServiceVersion();
  }

  /**
   * Inserts a new {@link com.google.gdata.data.acl.AclEntry} into a feed
   * associated with the target service.  It will return the inserted AclEntry,
   * including any additional attributes or extensions set by the GData server.
   * <p>
   * This is a convenience method.  It constructs the AclEntry from the
   * specified AclScope and AclRole.
   *
   * @param aclFeedUrl the POST URL associated with the target acl feed
   * @param scope the scope of the new acl
   * @param role the desired role for scope
   *
   * @return the newly inserted AclEntry returned by the service
   *
   * @throws IOException an ill-formed URI, internal error.  See
   * makeEntryUrl
   * @throws ServiceException insert request failed due to lack of
   * permissions, scope already defined on this feed, unsupported role or scope,
   * system error, etc
   */
  public AclEntry insert(URL aclFeedUrl, AclScope scope, AclRole role)
      throws IOException, ServiceException {
    AclEntry entry = new AclEntry();
    entry.setScope(scope);
    entry.setRole(role);
    return insert(aclFeedUrl, entry);
  }

  /**
   * Updates an existing {@link com.google.gdata.data.acl.AclEntry} by writing
   * it to the specified feed URL.  The resulting AclEntry (after update)
   * will be returned.
   * <p>
   * This is a convenience method.  It constructs the entry edit URL from
   * the feed URL and scope, and also constructs the AclEntry.
   *
   * @param aclFeedUrl the POST URL associated with the target acl feed
   * @param scope the scope of the to-be-updated acl
   * @param role the desired role for scope
   *
   * @return the updated entry returned by the service
   *
   * @throws IOException an ill-formed URI, internal error.  See
   * makeEntryUrl
   * @throws ServiceException update request failed due to lack of
   * permissions, unsupported role or scope, system error, etc
   */
  public AclEntry update(URL aclFeedUrl, AclScope scope, AclRole role)
      throws IOException, ServiceException {
    URL entryUrl = makeEntryUrl(aclFeedUrl, scope);
    AclEntry entry = new AclEntry();
    entry.setScope(scope);
    entry.setRole(role);
    entry.setId(entryUrl.toExternalForm());
    return update(entryUrl, entry);
  }

  /**
   * Deletes an existing AclEntry from the specified feed URL.
   * specified edit URL.
   * <p>
   * This is a convenience method.  It constructs the entry edit URL from
   * the feed URL and scope.
   * 
   * @param aclFeedUrl the POST URI associated with the target acl feed
   * @param scope the scope of the to-be-deleted acl
   *
   * @throws IOException an ill-formed URI, internal error.  See
   * makeEntryUrl
   * @throws ServiceException delete request failed due to lack of
   * permissions, unsupported role or scope, system error, etc
   */
  public void delete(URL aclFeedUrl, AclScope scope)
      throws IOException, ServiceException {
    delete(makeEntryUrl(aclFeedUrl, scope));
  }

  private URL makeEntryUrl(URL aclFeedUrl, AclScope scope) throws IOException {
    try {
      URI uri = new URI(aclFeedUrl.getProtocol(), null, aclFeedUrl.getHost(),
          aclFeedUrl.getPort(),
          aclFeedUrl.getPath() + "/" + scope.toExternalForm(), null, null);
      return uri.toURL();
    } catch (URISyntaxException e) {
      throw new IOException();
    }
  }
}
