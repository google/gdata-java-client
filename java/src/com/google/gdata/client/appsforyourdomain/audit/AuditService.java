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


package com.google.gdata.client.appsforyourdomain.audit;

import com.google.gdata.client.appsforyourdomain.AppsPropertyService;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainErrorCode;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.model.atom.Link;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AuditService allows Google Apps administrators to audit user's emails, email
 * drafts, and archived chats.
 * 
 * 
 * 
 */
public class AuditService extends AppsPropertyService {

  /*
   * Service URL of the API.
   */
  public static final String BASE_URL =
    "https://apps-apis.google.com/a/feeds/compliance/audit/";

  private String domain = null;

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd%20hh:mm");

  static {
    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    DATE_FORMAT.setLenient(false);
  }

  private static Logger LOGGER = Logger.getLogger(AuditService.class.toString());

  /**
   * Parameterized constructor for AuditService.
   * 
   * @param adminEmail email id of the administrator
   * @param adminPassword password for the administrator account
   * @param domain the domain to be monitored
   * @param applicationName application name for e.g audit-mycompany
   * @throws AuthenticationException if an authentication related error occurs.
   */
  public AuditService(String adminEmail, String adminPassword, String domain,
      String applicationName) throws AuthenticationException {
    this(domain, applicationName);
    setUserCredentials(adminEmail, adminPassword);
  }

  /**
   * Parameterized constructor to setup a Service object which can be used to
   * initialize the service without obtaining a token. The user should
   * explicitly authorize the service by calling either {@code
   * setUserCredentials} or {@code setUserToken} when using this constructor.
   * 
   * @param domain Domain being configured
   * @param applicationName Application name consuming the API
   * @throws AuthenticationException If an authentication error occurs
   */
  public AuditService(String domain, String applicationName) throws AuthenticationException {
    super(applicationName);
    this.domain = domain;
  }

  /**
   * Creates a new monitoring task to begin an audit.
   * 
   * Note: The number of daily requests are limited per domain, and can include
   * requests from several administrators during the day
   * 
   * @param sourceUser is the user who receives or sends messages that are being
   *        audited.
   * @param mailMonitor a POJO with details of the monitoring task
   * @return a GenericEntry with details of the task created
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry createMailMonitor(String sourceUser, MailMonitor mailMonitor)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    GenericEntry entry = mailMonitor.toGenericEntry();
    return insert(new URL(BASE_URL + "mail/monitor/" + domain + "/" + sourceUser), entry);
  }

  /**
   * Retrieves all the monitors of a given user.
   * 
   * @param sourceUser user whose monitors are to be retrieved
   * @return a GenericFeed object with details of monitors created.
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrieveMonitors(String sourceUser) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return getFeed(new URL(BASE_URL + "mail/monitor/" + domain + "/" + sourceUser),
        GenericFeed.class);
  }

  /**
   * Removes the monitor configured for the given source and destination user.
   * 
   * Note: The number of daily requests are limited per domain, and can include
   * requests from several administrators during the day
   * 
   * @param sourceUser the user who is being monitored
   * @param destUser this user is the auditor who receives the audited email
   *        messages as blind carbon copies (Bcc).
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public void deleteMonitor(String sourceUser, String destUser) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    delete(new URL(BASE_URL + "mail/monitor/" + domain + "/" + sourceUser + "/" + destUser));
  }

  /**
   * Upload a public key for signing mailbox dump archives. This public
   * encryption key should be a PGP format ascii-encoded RSA key. Before
   * uploading the public key, convert the key to a base64 encoded string
   * 
   * @param base64encodedKey a Base64 encoded, PGP format ASCII read RSA key
   * @return a GenericEntry instance with the inserted entry.
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry uploadPublicKey(String base64encodedKey) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("publicKey", base64encodedKey);
    return insert(new URL(BASE_URL + "publickey/" + domain), entry);
  }

  /**
   * Creates a new request for obtaining a user mailbox dump. The mailbox files
   * are encrypted using the key uploaded {@code uploadPublicKey} and are
   * available in <a href="http://en.wikipedia.org/wiki/Mbox">mbox format</a>
   * 
   * @param mailBoxDumpRequest a POJO with request details.
   * @return the inserted entry
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry createMailboxDumpRequest(MailBoxDumpRequest mailBoxDumpRequest)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    String user = mailBoxDumpRequest.getUserEmailAddress().split("@")[0];
    URL url = new URL(BASE_URL + "mail/export/" + domain + "/" + user);
    return insert(url, mailBoxDumpRequest.toGenericEntry());
  }

  /**
   * Retrieves all mailbox dump requests from the given start date.
   * 
   * @param fromDate optional date in the format 'yyyy-MM-dd hh:mm' in UTC. Pass
   *        <code>null</code> to retrieve without date filter.
   * @return a List of all mailbox dump request entries for the domain
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public List<GenericEntry> retrieveAllMailboxDumpRequests(Date fromDate)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    String url = BASE_URL + "mail/export/" + domain;
    if (fromDate != null) {
      url += "?fromDate=" + DATE_FORMAT.format(fromDate);
    }
    return getAllPages(new URL(url), GenericFeed.class);
  }


  /**
   * Retrieves a page of mailbox dump requests from the given start date.
   * 
   * @param fromDate optional date in the format 'yyyy-MM-dd hh:mm' in UTC. Pass
   *        <code>null</code> to retrieve without date filter
   * @return a page of mailbox dump request entries for the domain
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrievePageOfMailboxDumpRequests(Date fromDate)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    String url = BASE_URL + "mail/export/" + domain;
    if (fromDate != null) {
      url += "?fromDate=" + DATE_FORMAT.format(fromDate);
    }
    return getFeed(new URL(url), GenericFeed.class);
  }

  /**
   * Retrieves the next page of mailbox dump requests by following the atom next
   * link.
   * 
   * @param next The feed link for next page
   * @return a page of mailbox dump request entries for the domain
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrieveNextPageOfMailboxDumpRequests(Link next)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return getFeed(new URL(next.getHref()), GenericFeed.class);
  }

  /**
   * Retrieves the mailbox dump request for the given ID and user.
   * 
   * @param user the user whose dump requests need to be retrieved
   * @param id the requestId of the mailbox dump request
   * @return a GenericEntry instance of the requested dump request
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry retrieveMailboxDumpRequest(String user, String id)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return getEntry(new URL(BASE_URL + "mail/export/" + domain + "/" + user + "/" + id),
        GenericEntry.class);
  }

  /**
   * Deletes the mailbox dump request for the given ID and user.
   * 
   * @param user the user whose dump requests need to be deleted.
   * @param id the requestId of the mailbox dump request.
   * @return true, if delete succeeds.
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public boolean deleteMailboxDumpRequest(String user, String id)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    try {
      delete(new URL(BASE_URL + "mail/export/" + domain + "/" + user + "/" + id));
      return true;
    } catch (AppsForYourDomainException ex) {
      if (ex.getErrorCode() == AppsForYourDomainErrorCode.InvalidValue) {
        LOGGER.log(Level.INFO, "The request should either have status COMPLETED or"
            + "MARKED_DELETE before being deleted");
      }
    }
    return false;
  }

  /**
   * Creates a new Account Information request. When completed, the account
   * info is available for download.
   * 
   * @param user the domain user whose account information is to be audited
   * @return GenericEntry with request details.
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry createAccountInfoRequest(String user) throws AppsForYourDomainException,
      IOException, ServiceException {
    URL url = new URL(BASE_URL + "account/" + domain + "/" + user);
    return insert(url, new GenericEntry());
  }

  /**
   * Retrieves a previously created account/services related information request
   * for the given user.
   * 
   * @param user the user whose account info is being retrieved
   * @param requestId the unique requestId identifying the request
   * @return a GenericEntry instance with request details
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry retrieveAccountInfoRequest(String user, String requestId)
      throws AppsForYourDomainException, IOException, ServiceException {
    URL url = new URL(BASE_URL + "account/" + domain + "/" + user + "/" + requestId);
    return getEntry(url, GenericEntry.class);
  }

  /**
   * Retrieve all the Account info requests from the given start date.
   * 
   * @param fromDate optional date in the format 'yyyy-MM-dd hh:mm' in UTC. Pass
   *        <code>null</code> to retrieve without date filter.
   * @return Entries created starting the requested date
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public List<GenericEntry> retrieveAllAccountInfoRequests(Date fromDate)
      throws AppsForYourDomainException, IOException, ServiceException {
    String url = BASE_URL + "account/" + domain;
    if (fromDate != null) {
      url += "?fromDate=" + DATE_FORMAT.format(fromDate);
    }
    return getAllPages(new URL(url), GenericFeed.class);
  }

  /**
   * Retrieves a page of account info requests from the given optional start
   * date. Pass <code>null</code> for date to retrieve requests without date
   * filter. 
   * 
   * @param fromDate optional date in the format 'yyyy-MM-dd hh:mm' in UTC. Pass
   *        <code>null</code> to retrieve without date filter
   * @return a page of account info request entries for the domain
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrievePageOfAccountInfoRequests(Date fromDate)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    String url = BASE_URL + "account/" + domain;
    if (fromDate != null) {
      url += "?fromDate=" + DATE_FORMAT.format(fromDate);
    }
    return getFeed(new URL(url), GenericFeed.class);
  }

  /**
   * Retrieves the next page of account info requests by following the atom next
   * link.
   * 
   * @param next The feed link for next page
   * @return a page of account info request entries for the domain
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrieveNextPageOfAccountInfoRequests(Link next)
      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
    return getFeed(new URL(next.getHref()), GenericFeed.class);
  }
  /**
   * Deletes the account info request for the given user
   * 
   * @param user the user whose request is to be deleted
   * @param requestId the unique id of the request
   * @throws AppsForYourDomainException If an Audit API error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public void deleteAccountInfoRequest(String user, String requestId)
      throws AppsForYourDomainException, IOException, ServiceException {
    URL url = new URL(BASE_URL + "account/" + domain + "/" + user + "/" + requestId);
    delete(url);
  }
}
