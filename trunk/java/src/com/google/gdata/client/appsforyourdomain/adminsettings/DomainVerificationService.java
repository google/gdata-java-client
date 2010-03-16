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
import java.net.URL;

/**
 * Specialization of {@link AppsPropertyService} for managing domain verification
 * options of a domain.
 *
 * 
 *
 */
public class DomainVerificationService extends AppsPropertyService {

  protected String domainUrlBase = null;
  protected String verificationBaseUrl;

  /**
   * Parameterized constructor to setup a Service object which can be used to
   * initialize the service without obtaining a token. The user should
   * explicitly authorize the service by calling either {@code
   * setUserCredentials} or {@code setUserToken} when using this constructor.
   * 
   * @param domain Domain being configured
   * @param applicationName Application name consuming the API
   */
  public DomainVerificationService(String domain, String applicationName) {
    super(applicationName);
    domainUrlBase = AdminSettingsConstants.APPS_FEEDS_URL_BASE + domain + "/";
    verificationBaseUrl = domainUrlBase + "verification/";
  }

  /**
   * Parameterized constructor for service authentication.
   * 
   * @param adminEmail the email id of the administrator.
   * @param password the administrator password.
   * @param domain the domain name to be configured.
   * @param applicationName the calling client application name.for e.g.
   *        mycompany-java
   * @throws AuthenticationException if an authentication related error occurs.
   */
  public DomainVerificationService(String adminEmail, String password, String domain,
      String applicationName) throws AuthenticationException {
    this(domain, applicationName);
    setUserCredentials(adminEmail, password);
  }

  /**
   * 
   * @return GenericEntry a GenericEntry instance with CNAME verification
   *         status.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the fetch request failed due to system error.
   */
  public GenericEntry retrieveCnameVerificationStatus() throws AppsForYourDomainException,
      IOException, ServiceException {
    return getEntry(new URL(verificationBaseUrl + "cname"), GenericEntry.class);
  }

  /**
   * 
   * @return GenericEntry a GenericEntry instance with CNAME verification
   *         status.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the fetch request failed due to system error.
   */
  public GenericEntry retrieveMxVerificationStatus() throws AppsForYourDomainException,
      IOException, ServiceException {
    return getEntry(new URL(verificationBaseUrl + "mx"), GenericEntry.class);
  }


  /**
   * Sets the verified status as true for a previously retrieved MX or CNAME
   * verification status entry;
   * 
   * @param entry a previously retrieved GenericEntry instance to be updated
   *        with status as verified.
   * 
   * @return GenericEntry instance with updated settings.
   * @throws AppsForYourDomainException if an Apps for your domain API error
   *         occurred.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the fetch request failed due to system error.
   */
  public GenericEntry updateVerifiedStatus(GenericEntry entry, boolean status)
      throws AppsForYourDomainException, IOException, ServiceException {
    entry.removeProperty("verified");
    entry.addProperty("verified", String.valueOf(status));
    return entry.update();
  }
}

