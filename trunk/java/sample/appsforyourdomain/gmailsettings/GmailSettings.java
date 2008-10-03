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


package sample.appsforyourdomain.gmailsettings;

import com.google.gdata.client.appsforyourdomain.AppsForYourDomainService;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 * This is the client library for the Google Apps Gmail Settings API. It
 * shows how to use the services for creating Gmail filters, send-as aliases, or
 * labels or changing Gmail forwarding, POP3, IMAP, vacation-responder,
 * signature or general settings.
 */
public class GmailSettings extends AppsForYourDomainService {

  protected static final Logger logger =
    Logger.getLogger(GmailSettings.class.getName());

  protected final String domain;

  /**
   * Constructs a GmailSettings for the given domain
   * using the given admin credentials.
   * 
   * @param applicationName The name of the application making the modifications.
   * @param domain The domain in which settings will be modified.
   * @param username The user name (not email) of a domain administrator.
   * @param password The user's password on the domain.
   * @throws AuthenticationException The Exception thrown when invalid credentials are supplied.
   */
  public GmailSettings(String applicationName, String domain, String username, String password) 
      throws AuthenticationException {
    super(applicationName, Constants.PROTOCOL, Constants.APPS_APIS_DOMAIN);
    this.domain = domain;

    new GenericFeed().declareExtensions(getExtensionProfile());

    this.setUserCredentials(username + "@" + domain, password);
  }

/**
 * Inserts a new Gmail settings entity - eg a filter
 *
 * @param domain the domain where the settings are being changed
 * @param entry an {@link GenericEntry} object containing all the properties
 *        of the new entity.
 * @return an entry with the result of the operation.
 * @throws IOException if an error occurs while communicating with the GData
 *       service.
 * @throws MalformedURLException if the batch feed URL cannot be constructed.
 * @throws ServiceException if the insert request failed due to system error.
 */
  public GenericEntry insertSettings(String userName, GenericEntry entry, String setting)
      throws IOException, MalformedURLException, ServiceException {
    URL singleUrl = new URL(Constants.PROTOCOL + "://" + Constants.APPS_APIS_DOMAIN 
        + Constants.APPS_APIS_URL + "/" + domain + "/" + userName + "/" + setting);
    return insert(singleUrl, entry);
  }

/**
 * Update Gmail settings.
 *
 * @param domain the domain where the settings are being changed.
 * @param entry a {@link GenericEntry} object containing the new Gmail
 *        settings.
 * @return an entry with the result of the operation.
 * @throws IOException if an error occurs while communicating with the GData
 *         service.
 * @throws ServiceException if the insert request failed due to system error.
 */
  public GenericEntry updateSettings(String userName, GenericEntry entry, String setting)
      throws IOException, MalformedURLException, ServiceException {
    URL singleUrl = new URL(Constants.PROTOCOL + "://" + Constants.APPS_APIS_DOMAIN 
        + Constants.APPS_APIS_URL + "/" + domain + "/" + userName + "/" + setting);
    return update(singleUrl, entry);
  }

  /**
   * Creates a filter
   *
   * @param users An array of the users to set the signature for.
   * @param from The email must come from this address in order to be filtered.
   * @param to The email must be sent to this address in order to be filtered.
   * @param subject A string the email must have in it's subject line to be filtered.
   * @param hasTheWord A string the email can have anywhere in it's subject or body.
   * @param doesNotHaveTheWord A string that the email cannot have anywhere in its subject or body. 
   * @param hasAttachment A boolean representing whether or not the email contains an attachment. 
   *     Values are "true" or "false".
   * @param shouldMarkAsRead a boolean field that represents automatically moving the message to 
   *     "Archived" state if it matches the specified filter criteria.
   * @param shouldArchive a boolean field that represents automatically moving the message to 
   *     "Archived" state if it matches the specified filter criteria.
   * @param label A string that represents the name of the label to apply if a message matches the 
   *     specified filter criteria.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createFilter(String[] users, String from, String to, String subject, 
      String hasTheWord, String doesNotHaveTheWord, boolean hasAttachment, boolean shouldMarkAsRead,
      boolean shouldArchive, String label) throws InvalidUserException, ServiceException, 
      MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("from", from);
    entry.addProperty("to", to);
    entry.addProperty("subject", subject);
    entry.addProperty("hasTheWord", hasTheWord);
    entry.addProperty("doesNotHaveTheWord", doesNotHaveTheWord);
    entry.addProperty("hasAttachment", String.valueOf(hasAttachment));
    entry.addProperty("shouldMarkAsRead", String.valueOf(shouldMarkAsRead));
    entry.addProperty("shouldArchive", String.valueOf(shouldArchive));
    entry.addProperty("label", label);

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Creating filter...");
      insertSettings(users[i], entry, "filter");
      logger.log(Level.INFO, "Successfully created filter.");
    }
  }

  /**
   * Creates a send-as alias
   *
   * @param users An array of the users to set the signature for.
   * @param name The name which e-mails sent using the alias are from.
   * @param address The e-mail address which e-mails sent using the alias are from.
   * @param replyTo (Optional) If set, this address will be included as the reply-to address in 
   *     e-mails sent using the alias.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createSendAs(String[] users, String name, String address, String replyTo) 
      throws InvalidUserException, ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("name", name);
    entry.addProperty("address", address);
    entry.addProperty("replyTo", replyTo);

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Creating send-as alias...");
      insertSettings(users[i], entry, "sendas");
      logger.log(Level.INFO, "Successfully created send-as alias.");
    }
  }

  /**
   * Creates a label
   *
   * @param users An array of the users to set the signature for.
   * @param label A string that represents the name of the label.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createLabel(String[] users, String label) throws InvalidUserException, 
      ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("label", label);
    
    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Creating label...");
      insertSettings(users[i], entry, "label");
      logger.log(Level.INFO, "Successfully created label.");
    }
  }

  /**
   * Changes forwarding settings
   *
   * @param users An array of the users to set the signature for.
   * @param enable Whether to enable forwarding of incoming mail.
   * @param forwardTo The email will be forwarded to this address.
   * @param action What gmail should do with its copy of the e-mail after forwarding it on.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeForwarding(String[] users, boolean enable, String forwardTo, String action) 
      throws InvalidUserException, ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty("enable", "true");
      entry.addProperty("forwardTo", forwardTo);
      entry.addProperty("action", action);
    } else {
      entry.addProperty("enable", "false");
    }

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Updating forwarding settings...");
      updateSettings(users[i], entry, "forwarding");
      logger.log(Level.INFO, "Successfully updated forwarding settings.");
    }
  }

  /**
   * Changes POP3 settings
   *
   * @param users An array of the users to set the signature for.
   * @param enable Whether to enable POP3 access.
   * @param enableFor Whether to enable POP3 for all mail, or mail from now on.
   * @param action What gmail should do with its copy of the e-mail after it is retrieved using POP.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changePop(String[] users, boolean enable, String enableFor, String action) 
      throws InvalidUserException, ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty("enable", "true");
      entry.addProperty("enableFor", enableFor);
      entry.addProperty("action", action);
    } else {
      entry.addProperty("enable", "false");
    }

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Updating POP3 settings...");
      updateSettings(users[i], entry, "pop");
      logger.log(Level.INFO, "Successfully updated POP3 settings.");
    }
  }

  /**
   * Changes IMAP settings
   *
   * @param users An array of the users to set the signature for.
   * @param enable Whether to enable IMAP access.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeImap(String[] users, boolean enable) throws InvalidUserException, 
      ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("enable", String.valueOf(enable));

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Updating IMAP settings...");
      updateSettings(users[i], entry, "imap");
      logger.log(Level.INFO, "Successfully updated IMAP settings.");
    }
  }

  /**
   * Changes vacation-responder settings
   *
   * @param users An array of the users to set the signature for.
   * @param enable Whether to enable the vacation responder.
   * @param subject The subject line of the vacation responder autoresponse.
   * @param message The message body of the vacation responder autoresponse.
   * @param contactsOnly Whether to only send the autoresponse to known contacts.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeVacation(String[] users, boolean enable, String subject, String message, 
      boolean contactsOnly) throws InvalidUserException, ServiceException, MalformedURLException, 
      IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("enable", String.valueOf(enable));
    entry.addProperty("subject", subject);
    entry.addProperty("message", message);
    entry.addProperty("contactsOnly", String.valueOf(contactsOnly));

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Updating vacation-responder settings...");
      updateSettings(users[i], entry, "vacation");
      logger.log(Level.INFO, "Successfully updated vacation-responder settings.");
    }
  }

  /**
   * Changes signature
   *
   * @param users An array of the users to set the signature for.
   * @param signature The signature to be appended to outgoing messages.  Don't want a signature? 
   *     Set the signature to "" (empty string).
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeSignature(String[] users, String signature) throws InvalidUserException, 
      ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("signature", signature);

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Updating signature...");
      updateSettings(users[i], entry, "signature");
      logger.log(Level.INFO, "Successfully updated signature.");
    }
  }

  /**
   * Changes general settings
   *
   * @param users An array of the users to set the signature for.
   * @param pageSize The number of conversations to be shown per page.
   * @param enableShortcuts Whether to enable keyboard shortcuts
   * @param enableArrows Whether to display arrow-shaped personal indicators next to emails that 
   *     were sent specifically to the user. (> and >>). 
   * @param enableSnippets Whether to display snippets of messages in the inbox and when searching.
   * @param enableUnicode Whether to use UTF-8 (unicode) encoding for all outgoing messages, 
   *     instead of the default text encoding.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeGeneral(String[] users, String pageSize, boolean enableShortcuts, 
      boolean enableArrows, boolean enableSnippets, boolean enableUnicode) 
      throws InvalidUserException, ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("pageSize", pageSize);
    entry.addProperty("shortcuts", String.valueOf(enableShortcuts));
    entry.addProperty("arrows", String.valueOf(enableArrows));
    entry.addProperty("snippets", String.valueOf(enableSnippets));
    entry.addProperty("unicode", String.valueOf(enableUnicode));

    for (int i = 0; i < users.length; i++) { 
      logger.log(Level.INFO, "Updating general settings...");
      updateSettings(users[i], entry, "general");
      logger.log(Level.INFO, "Successfully updated general settings.");
    }
  }

  /**
   * Changes language settings
   *
   * @param users An array of the users to set the signature for.
   * @param language Gmail's display language.
   * @throws InvalidUserException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeLanguage(String[] users, String language) throws InvalidUserException, 
      ServiceException, MalformedURLException, IOException {
    if (users.length == 0) {
      throw new InvalidUserException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("language", language);

    for (int i = 0; i < users.length; i++) {
      logger.log(Level.INFO, "Updating language settings...");
      updateSettings(users[i], entry, "language");
      logger.log(Level.INFO, "Successfully updated language settings.");
    }
  }
}
