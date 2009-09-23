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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the client library for the Google Apps Gmail Settings API. It
 * shows how to use the services for creating Gmail filters, send-as aliases, or
 * labels or changing Gmail forwarding, POP3, IMAP, vacation-responder,
 * signature, web clip or general settings.
 */
public class GmailSettingsService extends AppsForYourDomainService {

  protected static final Logger logger =
    Logger.getLogger(GmailSettingsService.class.getName());

  protected final String domain;

  /**
   * Constructs a GmailSettingsService for the given domain using the given 
   * admin credentials.
   * 
   * @param applicationName the name of the application making the modifications.
   * @param domain the domain in which settings will be modified.
   * @param username the user name (not email) of a domain administrator.
   * @param password the user's password on the domain.
   * @throws AuthenticationException the Exception thrown when invalid 
   *        credentials are supplied.
   */
  public GmailSettingsService(String applicationName, String domain, 
      String username, String password) throws AuthenticationException {
    super(applicationName, Constants.PROTOCOL, Constants.APPS_APIS_DOMAIN);
    this.domain = domain;

    new GenericFeed().declareExtensions(getExtensionProfile());

    this.setUserCredentials(username + "@" + domain, password);
  }

  /**
   * Inserts a new Gmail settings entity - eg a filter.
   *
   * @param username the user name of a domain administrator.
   * @param entry an {@link GenericEntry} object containing all the properties
   *        of the new entity.
   * @return an entry with the result of the operation.
   * @throws IOException if an error occurs while communicating with the GData 
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public GenericEntry insertSettings(String username, GenericEntry entry, 
      String setting) throws IOException, MalformedURLException, 
      ServiceException {  
    URL singleUrl = new URL(Constants.PROTOCOL + "://" + 
        Constants.APPS_APIS_DOMAIN + Constants.APPS_APIS_URL + "/" + domain + 
        "/" + username + "/" + setting);
    return insert(singleUrl, entry);
  }

  /**
   * Update Gmail settings.
   *
   * @param username the user name of a domain administrator.
   * @param entry a {@link GenericEntry} object containing the new Gmail
   *        settings.
   * @return an entry with the result of the operation.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public GenericEntry updateSettings(String username, GenericEntry entry, 
      String setting) throws IOException, MalformedURLException, 
      ServiceException {
    URL singleUrl = new URL(Constants.PROTOCOL + "://" + 
        Constants.APPS_APIS_DOMAIN + Constants.APPS_APIS_URL + "/" + domain + 
        "/" + username + "/" + setting);
    return update(singleUrl, entry);
  }

  /**
   * Creates a filter.
   *
   * @param users a list of the users to create the filter for.
   * @param from the email must come from this address in order to be filtered.
   * @param to the email must be sent to this address in order to be filtered.
   * @param subject a string the email must have in it's subject line to be 
   *        filtered.
   * @param hasTheWord a string the email can have anywhere in it's subject or 
   *        body.
   * @param doesNotHaveTheWord a string that the email cannot have anywhere in 
   *        its subject or body. 
   * @param hasAttachment a boolean representing whether or not the email 
   *        contains an attachment. Values are "true" or "false".
   * @param shouldMarkAsRead a boolean field that represents automatically 
   *        moving the message to. "Archived" state if it matches the specified 
   *        filter criteria.
   * @param shouldArchive a boolean field that represents automatically moving 
   *        the message to. "Archived" state if it matches the specified filter 
   *        criteria.
   * @param label a string that represents the name of the label to apply if a 
   *        message matches the specified filter criteria.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createFilter(List<String> users, String from, String to, 
      String subject, String hasTheWord, String doesNotHaveTheWord, 
      boolean hasAttachment, boolean shouldMarkAsRead, boolean shouldArchive, 
      String label) throws IllegalArgumentException, ServiceException, 
      MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
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

    for (String user : users) {
      logger.log(Level.INFO, "Creating filter ( " +
          "from: " + from + 
          ", to: " + to + 
          ", subject: " + subject + 
          ", hasTheWord: " + hasTheWord + 
          ", doesNotHaveTheWord: " + doesNotHaveTheWord + 
          ", hasAttachment: " + hasAttachment + 
          ", shouldMarkAsRead: " + shouldMarkAsRead + 
          ", shouldArchive: " + shouldArchive + 
          ", label: " + label + 
          " ) for user " + user + " ...");
      insertSettings(user, entry, "filter");
      logger.log(Level.INFO, "Successfully created filter.");
    }
  }

  /**
   * Creates a send-as alias.
   *
   * @param users a list of the users to create the send-as alias for.
   * @param name the name which e-mails sent using the alias are from.
   * @param address the e-mail address which e-mails sent using the alias are 
   *        from.
   * @param replyTo (Optional) if set, this address will be included as the 
   *        reply-to address in e-mails sent using the alias.
   * @param makeDefault (Optional) if set to true, this user will have this 
   *        send-as alias selected by default from now on.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createSendAs(List<String> users, String name, String address, 
      String replyTo, boolean makeDefault) throws IllegalArgumentException, 
      ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("name", name);
    entry.addProperty("address", address);
    entry.addProperty("replyTo", replyTo);
    entry.addProperty("makeDefault", String.valueOf(makeDefault));

    for (String user : users) {
      logger.log(Level.INFO, "Creating send-as alias ( " +
          "name: " + name + 
          ", address: " + address + 
          ", replyTo: " + replyTo + 
          ", makeDefault: " + makeDefault + 
          " ) for user " + user + " ...");
      insertSettings(user, entry, "sendas");
      logger.log(Level.INFO, "Successfully created send-as alias.");
    }
  }

  /**
   * Creates a label.
   *
   * @param users a list of the users to create the label for.
   * @param label a string that represents the name of the label.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createLabel(List<String> users, String label) 
      throws IllegalArgumentException, ServiceException, MalformedURLException, 
      IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("label", label);
    
    for (String user : users) {
      logger.log(Level.INFO, "Creating label ( label: " + label + " ) for user "
          + user + " ...");
      insertSettings(user, entry, "label");
      logger.log(Level.INFO, "Successfully created label.");
    }
  }

  /**
   * Changes forwarding settings.
   *
   * @param users a list of the users to change the forwarding for.
   * @param enable whether to enable forwarding of incoming mail.
   * @param forwardTo the email will be forwarded to this address.
   * @param action what Gmail should do with its copy of the e-mail after 
   *        forwarding it on.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeForwarding(List<String> users, boolean enable, 
      String forwardTo, String action) throws IllegalArgumentException, 
      ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty("enable", "true");
      entry.addProperty("forwardTo", forwardTo);
      entry.addProperty("action", action);
    } else {
      entry.addProperty("enable", "false");
    }
    
    for (String user : users) {
      if (enable) {
        logger.log(Level.INFO, "Updating forwarding settings ( " +
            "enable: true" + 
            ", forwardTo: " + forwardTo + 
            ", action: " + action + 
            " ) for user" + 
            user + " ...");
      } else {
        logger.log(Level.INFO, "Updating forwarding settings ( enable: false ) " +
            "for user" + user + " ...");
      }
      updateSettings(user, entry, "forwarding");
      logger.log(Level.INFO, "Successfully updated forwarding settings.");
    }
  }

  /**
   * Changes POP3 settings.
   *
   * @param users a list of the users to change the POP3 settings for.
   * @param enable whether to enable POP3 access.
   * @param enableFor whether to enable POP3 for all mail, or mail from now on.
   * @param action what Gmail should do with its copy of the e-mail after it is 
   *        retrieved using POP.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changePop(List<String> users, boolean enable, String enableFor, 
      String action) throws IllegalArgumentException, ServiceException, 
      MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty("enable", "true");
      entry.addProperty("enableFor", enableFor);
      entry.addProperty("action", action);
    } else {
      entry.addProperty("enable", "false");
    }

    for (String user : users) {
      if (enable) {
        logger.log(Level.INFO, "Updating POP3 settings ( " +
            "enable: true" +
            ", enableFor: " + enableFor + 
            ", action: " + action + 
            " ) for user " + user + " ...");
      } else {
        logger.log(Level.INFO, "Updating POP3 settings ( enable: false ) for " +
            "user " + user + " ...");
      }     
      updateSettings(user, entry, "pop");
      logger.log(Level.INFO, "Successfully updated POP3 settings.");
    }
  }

  /**
   * Changes IMAP settings.
   *
   * @param users a list of the users to change the IMAP settings for.
   * @param enable whether to enable IMAP access.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeImap(List<String> users, boolean enable) 
      throws IllegalArgumentException, ServiceException, MalformedURLException, 
      IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("enable", String.valueOf(enable));

    for (String user : users) {
      logger.log(Level.INFO, "Updating IMAP settings ( enable: " + enable + " ) " +
          "for user " + user + " ...");
      updateSettings(user, entry, "imap");
      logger.log(Level.INFO, "Successfully updated IMAP settings.");
    }
  }

  /**
   * Changes vacation-responder settings.
   *
   * @param users a list of the users to change the vacation-responder for.
   * @param enable whether to enable the vacation responder.
   * @param subject the subject line of the vacation responder autoresponse.
   * @param message the message body of the vacation responder autoresponse.
   * @param contactsOnly whether to only send the autoresponse to known contacts.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeVacation(List<String> users, boolean enable, String subject, 
      String message, boolean contactsOnly) throws IllegalArgumentException, 
      ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty("enable", "true");
      entry.addProperty("subject", subject);
      entry.addProperty("message", message);
      entry.addProperty("contactsOnly", String.valueOf(contactsOnly));
    } else {
      entry.addProperty("enable", "false");
    }
    

    for (String user : users) {
      if (enable) {
        logger.log(Level.INFO, "Updating vacation-responder settings ( " + 
            "enable: " + enable +
            ", subject: " + subject + 
            ", message: " + message + 
            ", contactsOnly: " + contactsOnly +
            " ) for user " + 
            user + " ...");
      } else {
        logger.log(Level.INFO, "Updating vacation-responder settings ( " + 
            "enable: false ) for user " + user + " ...");
      }
      updateSettings(user, entry, "vacation");
      logger.log(Level.INFO, "Successfully updated vacation-responder settings.");
    }
  }

  /**
   * Changes signature.
   *
   * @param users a list of the users to change the signature for.
   * @param signature the signature to be appended to outgoing messages. Don't 
   *        want a signature? Set the signature to "" (empty string).
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeSignature(List<String> users, String signature) 
      throws IllegalArgumentException, ServiceException, MalformedURLException, 
      IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("signature", signature);

    for (String user : users) {
      logger.log(Level.INFO, "Updating signature ( signature: " + signature + 
          " ) for user " + user + " ...");
      updateSettings(user, entry, "signature");
      logger.log(Level.INFO, "Successfully updated signature.");
    }
  }

  /**
   * Changes general settings.
   *
   * @param users a list of the users to change the general settings for.
   * @param pageSize the number of conversations to be shown per page.
   * @param enableShortcuts whether to enable keyboard shortcuts
   * @param enableArrows whether to display arrow-shaped personal indicators 
   *        next to emails that were sent specifically to the user. (> and >>). 
   * @param enableSnippets whether to display snippets of messages in the inbox 
   *        and when searching.
   * @param enableUnicode whether to use UTF-8 (unicode) encoding for all 
   *        outgoing messages, instead of the default text encoding.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeGeneral(List<String> users, String pageSize, 
      boolean enableShortcuts, boolean enableArrows, boolean enableSnippets, 
      boolean enableUnicode) throws IllegalArgumentException, ServiceException, 
      MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("pageSize", pageSize);
    entry.addProperty("shortcuts", String.valueOf(enableShortcuts));
    entry.addProperty("arrows", String.valueOf(enableArrows));
    entry.addProperty("snippets", String.valueOf(enableSnippets));
    entry.addProperty("unicode", String.valueOf(enableUnicode));
    
    for (String user : users) {
      logger.log(Level.INFO, "Updating general settings ( " +
          "pageSize: " + pageSize + 
          ", shortcuts: " + enableShortcuts + 
          ", arrows: " + enableArrows + 
          ", snippets: " + enableSnippets + 
          ", unicode: " + enableUnicode + 
          " ) for user " + user + 
          " ...");
      updateSettings(user, entry, "general");
      logger.log(Level.INFO, "Successfully updated general settings.");
    }
  }

  /**
   * Changes language settings.
   *
   * @param users a list of the users to change the language for.
   * @param language Gmail's display language.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *        service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeLanguage(List<String> users, String language) 
      throws IllegalArgumentException, ServiceException, MalformedURLException, 
      IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty("language", language);
    
    for (String user : users) {
      logger.log(Level.INFO, "Updating language settings ( language: " + 
          language + " ) for user " + user + " ...");
      updateSettings(user, entry, "language");
      logger.log(Level.INFO, "Successfully updated language settings.");
    }
  }
  
  /**
   * Change web clip settings.
   * 
   * @param users a list of the users to change the web clip settings for.
   * @param enable whether to enable web clip.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws ServiceException if an error occurs while communicating with the 
   *        GData service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws IOException if the insert request failed due to system error.
   */
  public void changeWebClip(List<String> users, boolean enable) 
      throws IllegalArgumentException, ServiceException, MalformedURLException, 
      IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }
    
    GenericEntry entry = new GenericEntry();
    entry.addProperty("enable", String.valueOf(enable));
    
    for (String user : users) {
      logger.log(Level.INFO, "Updating web clip settings ( enable: " + enable + 
          " ) for user " + user + " ...");
      updateSettings(user, entry, "webclip");
      logger.log(Level.INFO, "Successfully updated web clip settings.");
    }
  }
  
}
