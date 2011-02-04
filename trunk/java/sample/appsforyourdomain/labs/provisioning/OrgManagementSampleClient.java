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


package sample.appsforyourdomain.labs.provisioning;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.appsforyourdomain.AppsPropertyService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * This is a sample client with helper methods that demonstrates the usage of
 * Organization Management APIs. These APIs help you create organization units
 * and manage your units/users.
 * 
 * An OrgUnit path is the URL-encoding (e.g., using URLEncoder.encode) of an
 * OrgUnit's lineage, concatenated together with the slash ('/') character.
 * E.g.,
 * 
 * path = URLEncode.encode(parentName, "UTF-8") + "/" +
 * URLEncode.encode(childName, "UTF-8");
 * 
 * 
 * 
 */
public class OrgManagementSampleClient {

  public enum OrgUnitProperty {
    NAME, DESCRIPTION, PARENT_ORG_UNIT_PATH, BLOCK_INHERTANCE, USERS_TO_MOVE
  }

  private AppsPropertyService service;
  private String domain = null;

  public static Logger LOGGER = Logger.getLogger(OrgManagementSampleClient.class.getName());


  /**
   * Parameterized constructor for authentication.
   * 
   * @param adminEmail Domain administrator's email address
   * @param password admin account password
   * @param domain the primary domain name
   * @param appName application identifier
   * @throws AuthenticationException If an authentication error occurs.
   */
  public OrgManagementSampleClient(String adminEmail, String password, String domain,
      String appName)
      throws AuthenticationException {
    this(domain, appName);
    service.setUserCredentials(adminEmail, password);
  }

  /**
   * Parameterized constructor for authentication.
   * 
   * @param domain the primary domain name
   * @param appName application identifier
   * @throws AuthenticationException If an authentication error occurs.
   */
  public OrgManagementSampleClient(String domain, String appName) throws AuthenticationException {
    service = new AppsPropertyService(appName);
    this.domain = domain;
  }

  /**
   * A utility method to create an OrgPath from a list of OrgUnits with parent
   * as first list item, first child as second item and so on.
   * 
   * @param orgUnits
   * @return a OrgUnitPath from a given list of OrgUnit names.
   * @throws UnsupportedEncodingException
   */
  public String createOrgPathFromOrgUnits(List<String> orgUnits)
      throws UnsupportedEncodingException {
    StringBuilder path = new StringBuilder();
    for (String orgUnit : orgUnits) {
      if (path.length() != 0) {
        path.append('/');
      }
      path.append(URLEncoder.encode(orgUnit, "UTF-8"));
    }
    return path.toString();
  }

  /**
   * Retrieves the customer Id that will be used for all other operations.
   * 
   * @param domain
   * @return a GenericEntry
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericEntry retrieveCustomerId(String domain) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    GenericEntry entry =
        service.getEntry(new URL("https://apps-apis.google.com/a/feeds/customer/2.0/customerId"),
            GenericEntry.class);
    return entry;
  }

  /**
   * Create a new organization unit under the given parent.
   * 
   * @param customerId the unique Id of the customer retrieved through customer
   *        feed.
   * @param orgUnitName the new organization name.
   * @param parentOrgUnitPath the path of the parent organization unit where '/'
   *        denotes the root of the organization hierarchy. For any OrgUnits to
   *        be created directly under root, specify '/' as parent path.
   * @param description a description for the organization unit created.
   * @param blockInheritance if true, blocks inheritance of policies from parent
   *        units.
   * @return a GenericEntry instance of the newly created org unit.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericEntry createOrganizationUnit(String customerId, String orgUnitName,
      String parentOrgUnitPath, String description, boolean blockInheritance)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("parentOrgUnitPath", parentOrgUnitPath);
    entry.addProperty("description", description);
    entry.addProperty("name", orgUnitName);
    entry.addProperty("blockInheritance", String.valueOf(blockInheritance));
    entry =
        service.insert(new URL("https://apps-apis.google.com/a/feeds/orgunit/2.0/" + customerId),
            entry);
    return entry;
  }

  /**
   * Retrieves an organization unit from the customer's domain.
   * 
   * @param customerId
   * @param orgUnitPath the path of the unit to be retrieved for e.g /corp
   * @return a GenericEntry instance.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericEntry retrieveOrganizationUnit(String customerId, String orgUnitPath)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    GenericEntry entry =
        service.getEntry(new URL("https://apps-apis.google.com/a/feeds/orgunit/2.0/" + customerId
            + "/" + orgUnitPath), GenericEntry.class);
    return entry;

  }

  /**
   * Retrieves all organization units for the given customer account.
   * 
   * @param customerId
   * @return a List of organization unit entries
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public List<GenericEntry> retrieveAllOrganizationUnits(String customerId)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return retrieveAllPages(new URL("https://apps-apis.google.com/a/feeds/orgunit/2.0/"
        + customerId + "?get=all"));

  }

  /**
   * Utility method that follows the next link and retrieves all pages of a
   * feed.
   * 
   * @param feedUrl Url of the feed.
   * @return a List of GenericEntries in the feed queried.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  private List<GenericEntry> retrieveAllPages(URL feedUrl) throws IOException, ServiceException {
    List<GenericEntry> allEntries = new ArrayList<GenericEntry>();
    try {
      do {
        GenericFeed feed = service.getFeed(feedUrl, GenericFeed.class);
        allEntries.addAll(feed.getEntries());
        feedUrl = (feed.getNextLink() == null) ? null : new URL(feed.getNextLink().getHref());
      } while (feedUrl != null);
    } catch (ServiceException se) {
      AppsForYourDomainException ae = AppsForYourDomainException.narrow(se);
      throw (ae != null) ? ae : se;
    }
    return allEntries;
  }

  /**
   * Retrieves all the child units of the given organization unit.
   * 
   * @param customerId
   * @param orgUnitPath
   * @return a List of GenericEntry instances.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public List<GenericEntry> retrieveChildOrganizationUnits(String customerId, String orgUnitPath)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return retrieveAllPages(new URL("https://apps-apis.google.com/a/feeds/orgunit/2.0/"
        + customerId + "?get=children&orgUnitPath=" + URLEncoder.encode(orgUnitPath, "UTF-8")));
  }

  /**
   * Deletes the given organization unit. The unit must not have any OrgUsers or
   * any child OrgUnits to be deleted.
   * 
   * @param customerId
   * @param orgUnitPath
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public void deleteOrganizationUnit(String customerId, String orgUnitPath)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    service.delete(new URL("https://apps-apis.google.com/a/feeds/orgunit/2.0/" + customerId + "/"
        + orgUnitPath));
  }

  /**
   * Updates the given organization attributes. USERS_TO_MOVE is a comma
   * separated list of email addresses that are to be moved across orgUnits
   * 
   * @param customerId
   * @param orgUnitPath
   * @param attributes a map of <code>OrgUnitProperty</code> and value to be
   *        updated.
   * @return the updated GenericEntry
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericEntry updateOrganizationUnit(String customerId, String orgUnitPath,
      Map<OrgUnitProperty, String> attributes) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    for (Map.Entry<OrgUnitProperty, String> mapEntry : attributes.entrySet()) {
      String value = mapEntry.getValue();
      if (value == null || value.length() == 0) {
        continue;
      }
      switch (mapEntry.getKey()) {
        case NAME:
          entry.addProperty("name", value);
          break;
        case PARENT_ORG_UNIT_PATH:
          entry.addProperty("parentUnitPath", value);
          break;
        case DESCRIPTION:
          entry.addProperty("description", value);
          break;
        case BLOCK_INHERTANCE:
          entry.addProperty("blockInheritance", value);
          break;
        case USERS_TO_MOVE:
          entry.addProperty("usersToMove", value);
          break;
        default:
          break;
      }
    }
    return service.update(new URL("https://apps-apis.google.com/a/feeds/orgunit/2.0/" + customerId
        + "/" + orgUnitPath), entry);
  }

  /**
   * Updates the organization of the given user in a given organization.
   * 
   * @param customerId
   * @param orgUserEmail the email address of the user
   * @param oldOrgUnitPath optional: the old organization unit path. If
   *        specified, validates the OrgUser's current path.
   * @param newOrgUnitPath the new organization unit path.
   * @return a GenericEntry with the updated organization user.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericEntry updateOrganizationUser(String customerId, String orgUserEmail,
      String oldOrgUnitPath, String newOrgUnitPath) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    if (oldOrgUnitPath != null && oldOrgUnitPath.length() != 0) {
      entry.addProperty("oldOrgUnitPath", oldOrgUnitPath);
    }
    entry.addProperty("orgUnitPath", newOrgUnitPath);
    return service.update(new URL("https://apps-apis.google.com/a/feeds/orguser/2.0/" + customerId
        + "/" + orgUserEmail), entry);
  }

  /**
   * Retrieves the details of a given organization user.
   * 
   * @param customerId
   * @param orgUserEmail the email address of the organization user.
   * @return a GenericEntry instance
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericEntry retrieveOrganizaionUser(String customerId, String orgUserEmail)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return service.getEntry(new URL("https://apps-apis.google.com/a/feeds/orguser/2.0/"
        + customerId + "/" + orgUserEmail), GenericEntry.class);

  }

  /**
   * Retrieves the first page of OrgUnit entries. For subsequent pages, use
   * <code>retrieveNextPage</code>.
   * 
   * @param customerId
   * @return a GenericFeed with a single page of entries.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericFeed retrieveFirstPageOfOrganizationUsers(String customerId)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return service.getFeed(new URL("https://apps-apis.google.com/a/feeds/orguser/2.0/" + customerId
        + "?get=all"), GenericFeed.class);
  }

  /**
   * Retrieves a single page of entries given an atom:link.
   * 
   * @param next the next atom:link which can be obtained from a feed
   *        <code>GenericFeed.getNextLink()</code>
   * @return a GenericFeed with a single page of entries.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericFeed retrieveNextPage(Link next) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return service.getFeed(new URL(next.getHref()), GenericFeed.class);
  }

  /**
   * Retrieves all the users from all the organizations of the customer. If you
   * have more than a few hundred of OrgUsers, you should use pagination methods
   * - <code>retrieveFirstPageOfOrganizationUsers</code> and
   * <code>retrieveNextPage</code>
   * 
   * @param customerId
   * @return a List of GenericEntry instances
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public List<GenericEntry> retrieveAllOrganizationUsers(String customerId)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return retrieveAllPages(new URL("https://apps-apis.google.com/a/feeds/orguser/2.0/"
        + customerId + "?get=all"));
  }

  /**
   * Retrieves the first page of OrgUser entries in a given OrgUnit. For
   * subsequent pages, use
   * <code>retrieveNextPage(GenericFeed.getNextLink())</code>.
   * 
   * @param customerId
   * @param orgUnitPath
   * @return a GenericFeed with a single page of entries.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public GenericFeed retrieveFirstPageOfOrganizationUsersByOrgUnit(String customerId,
      String orgUnitPath) throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return service.getFeed(new URL("https://apps-apis.google.com/a/feeds/orguser/2.0/" + customerId
        + "?get=children&orgUnitPath=" + URLEncoder.encode(orgUnitPath, "UTF-8")),
        GenericFeed.class);
  }

  /**
   * Retrieves all the users under a given organization unit. If you have more
   * than a few hundred of OrgUsers, you should use pagination methods -
   * <code>retrieveFirstPageOfOrganizationUsersByOrgUnit</code> and
   * <code>retrieveNextPage</code>
   * 
   * @param customerId
   * @param orgUnitPath
   * @return a List of organization users.
   * @throws AppsForYourDomainException
   * @throws MalformedURLException
   * @throws IOException
   * @throws ServiceException
   */
  public List<GenericEntry> retrieveAllOrganizationUsersByOrgUnit(String customerId,
      String orgUnitPath) throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return retrieveAllPages(new URL("https://apps-apis.google.com/a/feeds/orguser/2.0/"
        + customerId + "?get=children&orgUnitPath=" + URLEncoder.encode(orgUnitPath, "UTF-8")));
  }

  /**
   * @param args
   * @throws AuthenticationException
   */
  public static void main(String[] args) throws AuthenticationException {

    try {

      if (args.length != 4) {
        System.out
            .println("Usage: java OrgManagementSampleClient <admin@example.com> <adminpassword> "
                + "<domain> <testUserEmail>");
        System.exit(1);
      }
      String adminEmail = args[0];
      String adminPassword = args[1];
      String domain = args[2];
      String user = args[3];
      String customerId = null;

      OrgManagementSampleClient client =
          new OrgManagementSampleClient(adminEmail, adminPassword, domain, "org-api-sample-"
              + domain);

      GenericEntry entry = null;
      GenericFeed feed = null;

      entry = client.retrieveCustomerId(customerId);
      customerId = entry.getProperty("customerId");
      LOGGER.log(Level.INFO, "Retrieved Customer ID - " + customerId);

      entry =
          client.createOrganizationUnit(customerId, "system", "/", "System Organization", false);
      LOGGER.log(Level.INFO, "Created new OrgUnit - " + entry.getAllProperties());

      entry = client.retrieveOrganizationUnit(customerId, "system");
      LOGGER.log(Level.INFO, "Retrieved OrgUnit - " + entry.getAllProperties());

      List<GenericEntry> allUnits = client.retrieveAllOrganizationUnits(customerId);
      LOGGER.log(Level.INFO, "Retrieved all OrgUnits - " + allUnits.size());

      allUnits = client.retrieveChildOrganizationUnits(customerId, "/");
      LOGGER.log(Level.INFO, "Retrieved all child units  - " + allUnits.size());

      LOGGER.log(Level.INFO, "Updating OrgUnit 'system'");
      Map<OrgUnitProperty, String> attributes = new HashMap<OrgUnitProperty, String>();
      attributes.put(OrgUnitProperty.DESCRIPTION, "testchanged");
      entry = client.updateOrganizationUnit(customerId, "system", attributes);
      LOGGER.log(Level.INFO, "Updated OrgUnit description - " + entry.getAllProperties());

      client.retrieveOrganizaionUser(customerId, user);
      LOGGER.log(Level.INFO, "Retrieved OrgUser - " + entry.getAllProperties());

      entry = client.updateOrganizationUser(customerId, user, "/", "system");
      LOGGER.log(Level.INFO, "Updated OrgUser - " + entry.getAllProperties());

      // For pagination, use retrieveFirstPageOfOrganizationUsers() and
      // retrieveNextPage(feed.getNextLink())

      List<GenericEntry> allUsers = client.retrieveAllOrganizationUsers(customerId);
      LOGGER.log(Level.INFO, "Retrieved User count: " + allUsers.size());

      allUsers = client.retrieveAllOrganizationUsersByOrgUnit(customerId, "system");
      LOGGER.log(Level.INFO, "Retrieved User count: " + allUsers.size());

      LOGGER.log(Level.INFO, "OrgPath construction - "
          + client.createOrgPathFromOrgUnits(Arrays.asList("parent", "firstChild",
              "childOfFirstChild")));

      // cleanup
      client.updateOrganizationUser(customerId, user, "system", "/");

      // cleanup
      client.deleteOrganizationUnit(customerId, "system");

    } catch (AppsForYourDomainException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }
}

