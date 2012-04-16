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

import com.google.gdata.client.appsforyourdomain.AppsPropertyService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sample client that uses multi-domain feeds to create users, aliases in one of
 * the user's domains.
 * 
 * 
 * 
 */
public class ProvisioningApiMultiDomainSampleClient {

  private AppsPropertyService service;
  private String domain = null;

  public enum UserProperty {
    USER_EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, HASH_FUNCTION, ADMIN, SUSPENDED,
    CHANGE_PASSWORD_AT_NEXT_LOGIN, QUOTA, IP_WHITELIST
  }

  public static Logger LOGGER =
      Logger.getLogger(ProvisioningApiMultiDomainSampleClient.class.getName());

  /**
   * Parameterized constructor for authentication.
   * 
   * @param adminEmail Domain administrator's email address
   * @param password admin account password
   * @param domain the primary domain name
   * @param appName application identifier
   * @throws AuthenticationException If an authentication error occurs.
   */
  public ProvisioningApiMultiDomainSampleClient(String adminEmail, String password, String domain,
      String appName) throws AuthenticationException {
    this(domain, appName);
    service.setUserCredentials(adminEmail, password);

  }

  /**
   * Constructor for credential-less services - OAuth.
   */
  public ProvisioningApiMultiDomainSampleClient(String domain, String appName) {
    service = new AppsPropertyService(appName);
    this.domain = domain;
  }

  /**
   * Creates an alias email for the user identified by the given email address.
   * 
   * @param aliasEmail The alias email to create for the given user.
   * @param userEmail User's primary email address.
   * @return the newly created alias GenericEntry instance.
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericEntry createAlias(String aliasEmail, String userEmail)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("userEmail", userEmail);
    entry.addProperty("aliasEmail", aliasEmail);
    return service.insert(new URL("https://apps-apis.google.com/a/feeds/alias/2.0/" + domain),
        entry);
  }

  /**
   * Retrieves the alias entry for the given email alias.
   * 
   * @param aliasEmail the user email alias.
   * @return GenericEntry
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericEntry retrieveAlias(String aliasEmail) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {

    return service.getEntry(new URL("https://apps-apis.google.com/a/feeds/alias/2.0/" + domain
        + "/" + aliasEmail), GenericEntry.class);
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
   * Retrieves all email aliases created in the customer domain. If you have more than
   * a few hundred aliases, you must use <code>retrieveFirstPageOfAliases</code> and
   * <code>retrieveNextPage</code>.
   * 
   * @return a List of GenericEntry objects.
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public List<GenericEntry> retrieveAllAliases() throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {

    return retrieveAllPages(new URL("https://apps-apis.google.com/a/feeds/alias/2.0/" + domain));
  }

  /**
   * Retrieves first page of email aliases created in the customer domain. To
   * retrieve subsequent pages, use <code>retrieveNextPage</code> with
   * <code>GenericFeed.getNextLink()</code>
   * 
   * @return a List of GenericEntry objects.
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericFeed retrieveFirstPageOfAliases() throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return service.getFeed(new URL("https://apps-apis.google.com/a/feeds/alias/2.0/" + domain),
        GenericFeed.class);
  }
  /**
   * Retrieves all aliases created for the user identified by the given email
   * address.
   * 
   * @param userEmail
   * @return a List of GenericEntry objects
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public List<GenericEntry> retrieveAllUserAliases(String userEmail)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {

    return retrieveAllPages(new URL(
        "https://apps-apis.google.com/a/feeds/alias/2.0/" + domain + "?userEmail=" + userEmail));
  }

  /**
   * Deletes the given alias.
   * 
   * @param aliasEmail
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public void deleteAlias(String aliasEmail) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    service.delete(new URL("https://apps-apis.google.com/a/feeds/alias/2.0/" + domain + "/"
        + aliasEmail));
  }

  /**
   * Creates a new user with the given email address in the customer domain.
   * 
   * @param email User email of the new account.
   * @param password
   * @param firstName
   * @param lastName
   * @return a GenericEntry instance of the newly created user.
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericEntry createUser(String email, String password, String firstName, String lastName)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("userEmail", email);
    entry.addProperty("password", password);
    entry.addProperty("firstName", firstName);
    entry.addProperty("lastName", lastName);
    return service
        .insert(new URL("https://apps-apis.google.com/a/feeds/user/2.0/" + domain), entry);
  }

  /**
   * Creates a user with one or more optional attributes set.
   * 
   * @param email
   * @param password
   * @param firstName
   * @param lastName
   * @param optionalAttributes a hashmap of user property
   *        <code>UserProperty</code> and values.
   * @return a GenericEntry instance of the newly created user.
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericEntry createUser(String email, String password, String firstName, String lastName,
      Map<UserProperty, String> optionalAttributes) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("userEmail", email);
    entry.addProperty("password", password);
    entry.addProperty("firstName", firstName);
    entry.addProperty("lastName", lastName);
    for (Map.Entry<UserProperty, String> mapEntry : optionalAttributes.entrySet()) {
      String value = mapEntry.getValue();
      if (value == null || value.length() == 0) {
        continue;
      }
      switch (mapEntry.getKey()) {
        case HASH_FUNCTION:
          entry.addProperty("name", value);
          break;
        case ADMIN:
          entry.addProperty("isAdmin", value);
          break;
        case SUSPENDED:
          entry.addProperty("isSuspended", value);
          break;
        case CHANGE_PASSWORD_AT_NEXT_LOGIN:
          entry.addProperty("isChangePasswordAtNextLogin", value);
          break;
        case QUOTA:
          entry.addProperty("quotaInGb", value);
          break;
        case IP_WHITELIST:
          entry.addProperty("ipWhitelisted", value);
          break;
        default:
          break;
      }
    }
    return service
        .insert(new URL("https://apps-apis.google.com/a/feeds/user/2.0/" + domain), entry);
  }

  /**
   * 
   * @param email
   * @return a GenricEntry instance
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericEntry retrieveUser(String email) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {

    return service.getEntry(new URL("https://apps-apis.google.com/a/feeds/user/2.0/" + domain + "/"
        + email), GenericEntry.class);
  }

  /**
   * Retrieves a feed of all users in the customer domain. If you have more than
   * a few hundred users, you must use <code>retrieveFirstPageOfUsers</code> and
   * <code>retrieveNextPage</code>.
   * 
   * @return a list of GenericEntry objects
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public List<GenericEntry> retrieveAllUsers() throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return retrieveAllPages(new URL("https://apps-apis.google.com/a/feeds/user/2.0/" + domain));
  }

  /**
   * Retrieves the first page of users. To retrieve subsequent pages, use
   * <code>retrieveNextPage</code> with <code>GenericFeed.getNextLink()</code>
   * 
   * @return a page of user entries.
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericFeed retrieveFirstPageOfUsers() throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return service.getFeed(new URL("https://apps-apis.google.com/a/feeds/user/2.0/" + domain),
        GenericFeed.class);
  }

  /**
   * Retrieves a single page of entries given an atom:link.
   * 
   * @param next the next atom:link which can be obtained from a feed
   *        <code>GenericFeed.getNextLink()</code>
   * @return a GenericFeed with a single page of entries.
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericFeed retrieveNextPage(Link next) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return service.getFeed(new URL(next.getHref()), GenericFeed.class);
  }

  /**
   * Updates the given user attributes.
   * 
   * @param email
   * @param updatedAttributes a key-Value map of attributes to be updated
   * @return updated GenericEntry
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericEntry updateUser(String email, Map<String, String> updatedAttributes)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperties(updatedAttributes);
    return service.update(new URL("https://apps-apis.google.com/a/feeds/user/2.0/" + domain + "/"
        + email), entry);
  }

  /**
   * deletes the given user account from the customer domain.
   * 
   * @param email
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public void deleteUser(String email) throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    service
        .delete(new URL("https://apps-apis.google.com/a/feeds/user/2.0/" + domain + "/" + email));
  }

  /**
   * changes the primary email address of the user to the given email address
   * 
   * @param oldEmailAddress
   * @param newEmailAddress
   * @return the updated GenericEntry
   * @throws AppsForYourDomainException If a Provisioning API specific error
   *         occurs.
   * @throws ServiceException If a generic GData framework error occurs.
   * @throws IOException If an error occurs communicating with the GData
   *         service.
   */
  public GenericEntry updateEmailAddress(String oldEmailAddress, String newEmailAddress)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("userEmail", oldEmailAddress);
    entry.addProperty("newEmail", newEmailAddress);
    return service.update(new URL("https://apps-apis.google.com/a/feeds/user/userEmail/2.0/"
        + domain + "/" + oldEmailAddress), entry);
  }

  /**
   * The main driver for the sample.
   * 
   * @param args java ProvisioningApiMultiDomainSampleClient [admin@example.com]
   *        [adminpassword] [primarydomain] [secondarydomain]
   */
  public static void main(String[] args) {
    if (args.length != 4) {
      System.out
          .println("Usage: java ProvisioningApiMultiDomainSampleClient <admin@example.com> " 
              + "<adminpassword> <primarydomain> <secondarydomain>");
      System.exit(1);
    }
    String adminEmail = args[0];
    String adminPassword = args[1];
    String primaryDomain = args[2];
    String secondaryDomain = args[3];

    try {
      ProvisioningApiMultiDomainSampleClient client =
          new ProvisioningApiMultiDomainSampleClient(adminEmail, adminPassword, primaryDomain,
              "multidomain-api-sample-" + primaryDomain);
      long time = System.currentTimeMillis();
      String userEmail = "test-" + time + "@" + secondaryDomain;
      GenericEntry entry = client.createUser(userEmail, "p@ssw0rd", "firstName", "lastName");
      LOGGER.log(Level.INFO, "Created user - " + entry.getProperty("userEmail"));
      entry = client.retrieveUser(userEmail);
      LOGGER.log(Level.INFO, "Retrieved user - " + entry.getProperty("userEmail"));
      
      // create user with optional attributes
      Map<UserProperty, String> optionalAttributes = new HashMap<UserProperty, String>();
      optionalAttributes.put(UserProperty.ADMIN, String.valueOf(true));
      optionalAttributes.put(UserProperty.SUSPENDED, String.valueOf(false));
      optionalAttributes.put(UserProperty.HASH_FUNCTION, "MD5");
      entry =
          client.createUser("test2-" + time + "@" + secondaryDomain,
              "0f359740bd1cda994f8b55330c86d845", "firstName", "lastName", optionalAttributes);
      LOGGER.log(Level.INFO, "Created user with optional attributes- "
          + entry.getProperty("userEmail"));
      
      Map<String, String> updatedAttributes = new HashMap<String, String>();
      updatedAttributes.put("lastName", "Smith");
      updatedAttributes.put("isSuspended", "true");
      entry = client.updateUser(userEmail, updatedAttributes);
      LOGGER.log(Level.INFO, "Updated user - " + entry.getProperty("lastName"));

      List<GenericEntry> users = client.retrieveAllUsers();
      LOGGER.log(Level.INFO, "Retrieved all users - " + users.size());

      // Alias operations
      String aliasEmail = "alias-" + time + "@" + secondaryDomain;
      entry = client.createAlias(aliasEmail, userEmail);
      LOGGER.log(Level.INFO, "Created alias - " + entry.getProperty("aliasEmail"));
      entry = client.retrieveAlias(aliasEmail);
      LOGGER.log(Level.INFO, "Retrieved alias - " + entry.getProperty("aliasEmail"));

      List<GenericEntry> aliases = client.retrieveAllAliases();
      LOGGER.log(Level.INFO, "Retrieved all aliases - " + users.size());

      // cleanup
      client.deleteAlias(aliasEmail);
      LOGGER.log(Level.INFO, "Deleted  - " + aliasEmail);

      client.deleteUser(userEmail);
      LOGGER.log(Level.INFO, "Deleted  - " + userEmail);
      
      client.deleteUser("test2-" + time + "@" + secondaryDomain);

    } catch (AuthenticationException e) {
      e.printStackTrace();
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

