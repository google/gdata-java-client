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


package com.google.gdata.client.appsforyourdomain.adminsettings;

import com.google.gdata.client.appsforyourdomain.AppsPropertyService;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Specialization of {@link AppsPropertyService} for managing general, account
 * related and appearance settings of a domain.
 * 
 * 
 */
public class DomainSettingsService extends AppsPropertyService {

  /**
   * Base URL for various feeds.
   */
  protected String generalSettingsUrl = null;
  protected String domainBaseUrl = null;
  protected String appearanceUrl = null;
  protected String accountSettingsUrl = null;

  /**
   * Parameterized constructor to setup a Service object which can be used to
   * initialize the service without obtaining a token. The user should
   * explicitly authorize the service by calling either {@code
   * setUserCredentials} or {@code setUserToken} when using this constructor.
   * 
   * @param domain Domain being configured
   * @param applicationName Application name consuming the API
   */
  public DomainSettingsService(String domain, String applicationName) {
    super(applicationName);
    domainBaseUrl = AdminSettingsConstants.APPS_FEEDS_URL_BASE + domain + "/";
    accountSettingsUrl = domainBaseUrl + AdminSettingsConstants.APPS_SETTINGS_ACCOUNT_URL_SUFFIX;
    generalSettingsUrl = domainBaseUrl + AdminSettingsConstants.APPS_SETTINGS_GENERAL_URL_SUFFIX;
    appearanceUrl = domainBaseUrl + AdminSettingsConstants.APPS_SETTINGS_APPEARANCE_URL_SUFFIX;
  }

  /**
   * Parameterized constructor for service authentication.
   * 
   * @param adminUser the email id of the administrator.
   * @param adminPassword the administrator password.
   * @param domain the domain name to be configured.
   * @param applicationName the calling client application name.for e.g.
   *        mycompany-java
   * @throws AuthenticationException if an authentication related error occurs.
   */
  public DomainSettingsService(String adminUser, String adminPassword, String domain,
      String applicationName) throws AuthenticationException {
    this(domain, applicationName);
    setUserCredentials(adminUser, adminPassword);
  }

  /**
   * Sets the default language for Google Apps domain.
   * 
   * @param language
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the update request failed due to system error.
   */
  public GenericEntry setDefaultLanguage(String language) throws AppsForYourDomainException,
      IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("defaultLanguage", language);
    return update(new URL(generalSettingsUrl + "defaultLanguage"), entry);
  }

  /**
   * Retrieves the default language for the Google Apps domain
   * 
   * @return The default language code
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the fetch request failed due to system error.
   */
  public String getDefaultLanguage() throws AppsForYourDomainException, IOException,
      ServiceException {
    return getEntry(new URL(generalSettingsUrl + "defaultLanguage"), GenericEntry.class)
        .getProperty("defaultLanguage");
  }

  /**
   * Set the organization name for the domain
   * 
   * @param organizationName The name of the organization associated with the
   *        domain
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the update request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public GenericEntry setOrganizationName(String organizationName)
      throws AppsForYourDomainException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("organizationName", organizationName);
    return update(new URL(generalSettingsUrl + "organizationName"), entry);
  }

  /**
   * Retrieves the organization name associated with the domain
   * 
   * @return The name of the organization
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getOrganizationName() throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    return getEntry(new URL(generalSettingsUrl + "organizationName"), GenericEntry.class)
        .getProperty("organizationName");
  }

  /**
   * Retrieves the maximum number of users that can be created in the domain
   * 
   * @return Maximum users count.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public int getMaxUserCount() throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    String usersCount =
        getEntry(new URL(generalSettingsUrl + "maximumNumberOfUsers"), GenericEntry.class)
            .getProperty("maximumNumberOfUsers");
    return Integer.parseInt(usersCount);
  }

  /**
   * Retrieves the status of verification for the domain.
   * 
   * @return Status of domain verification
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getDomainVerificationStatus() throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return getEntry(new URL(accountSettingsUrl + "isVerified"), GenericEntry.class).getProperty(
        "isVerified");
  }

  /**
   * Retrieves the Support PIN for the domain.
   * 
   * @return String Support PIN
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getSupportPIN() throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    return getEntry(new URL(accountSettingsUrl + "supportPIN"), GenericEntry.class).getProperty(
        "supportPIN");
  }

  /**
   * Retrieves the domain edition.
   * 
   * @return String Domain edition
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getDomainEdition() throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    return getEntry(new URL(accountSettingsUrl + "edition"), GenericEntry.class).getProperty(
        "edition");
  }

  /**
   * Retrieves Customer PIN associated with the domain.
   * 
   * @return Customer PIN
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getCustomerPIN() throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    return getEntry(new URL(accountSettingsUrl + "customerPIN"), GenericEntry.class).getProperty(
        "customerPIN");
  }

  /**
   * Gets the time domain was created.
   * 
   * @return Time of domain creation
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getDomainCreationTime() throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    return getEntry(new URL(accountSettingsUrl + "creationTime"), GenericEntry.class).getProperty(
        "creationTime");
  }

  /**
   * Retrieves the country code for the domain registered.
   * 
   * @return Country code
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getCountryCodeForDomain() throws AppsForYourDomainException, MalformedURLException,
      IOException, ServiceException {
    return getEntry(new URL(accountSettingsUrl + "countryCode"), GenericEntry.class).getProperty(
        "countryCode");
  }

  /**
   * Retrieves the secondary Email address registered with the domain
   * 
   * @return Secondary Email address
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the feed URL cannot be constructed.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   */
  public String getDomainSecondaryEmailAddress() throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return getEntry(new URL(accountSettingsUrl + "adminSecondaryEmail"), GenericEntry.class)
        .getProperty("adminSecondaryEmail");
  }

  /**
   * Registers the given Email as domain's secondary Email address
   * 
   * @param adminSecondaryEmail Email address
   * @return GenericEntry an updated GenericEntry secondary email address.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred
   */
  public GenericEntry setDomainSecondaryEmailAddress(String adminSecondaryEmail)
      throws AppsForYourDomainException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("adminSecondaryEmail", adminSecondaryEmail);
    return update(new URL(accountSettingsUrl + "adminSecondaryEmail"), entry);
  }

  /**
   * Updates the custom logo with the new image provided.
   * 
   * @param base64EncodedImage image file in base64 encoding.
   * @return GenericEntry an updated GenericEntry with appearance settings
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the fetch request failed due to system error.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred
   */
  public GenericEntry setDomainLogo(String base64EncodedImage) throws IOException,
      AppsForYourDomainException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("logoImage", base64EncodedImage);
    return update(new URL(appearanceUrl + "customLogo"), entry);
  }
}
