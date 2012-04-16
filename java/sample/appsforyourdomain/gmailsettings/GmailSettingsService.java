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
 * This is the client library for the Google Apps Gmail Settings API. It shows
 * how to use the services for creating Gmail filters, send-as aliases, labels,
 * adding and removing email delegates, or changing Gmail forwarding, POP3,
 * IMAP, vacation-responder, signature, web clip or general settings.
 */
public class GmailSettingsService extends AppsForYourDomainService {

  protected static final Logger logger = Logger.getLogger(GmailSettingsService.class.getName());

  protected final String domain;

  /**
   * Constructs a GmailSettingsService for the given domain using the given
   * admin credentials.
   *
   * @param applicationName the name of the application making the
   *        modifications.
   * @param domain the domain in which settings will be modified.
   * @param username the user name (not email) of a domain administrator.
   * @param password the user's password on the domain.
   * @throws AuthenticationException the Exception thrown when invalid
   *         credentials are supplied.
   */
  public GmailSettingsService(
      String applicationName, String domain, String username, String password)
      throws AuthenticationException {
    super(applicationName, Constants.PROTOCOL, Constants.APPS_APIS_DOMAIN);
    this.domain = domain;

    new GenericFeed().declareExtensions(getExtensionProfile());

    this.setUserCredentials(username + "@" + domain, password);
  }

  /**
   * Retrieve the specified Gmail settings as a GenericFeed
   *
   * @param username the user name or email for which to get the settings.
   * @param setting the setting field to get.
   * @return a GenericEntry of requested settings
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public GenericFeed retrieveSettingsFeed(String username, String setting)
      throws IOException, ServiceException {
    URL singleUrl = buildSettingsUrl(username, setting);

    return getFeed(singleUrl, GenericFeed.class);
  }

  /**
   * Retrieve the specified Gmail settings as a GenericEntry
   *
   * @param username the user name or email for which to get the settings.
   * @param setting the setting field to get.
   * @return a GenericEntry of requested settings
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public GenericEntry retrieveSettingsEntry(String username, String setting)
      throws IOException, ServiceException {
    URL singleUrl = buildSettingsUrl(username, setting);

    return getEntry(singleUrl, GenericEntry.class);
  }

  /**
   * Inserts a new Gmail settings entity - eg a filter.
   *
   * @param username the user name or email for the affected user.
   * @param entry an {@link GenericEntry} object containing all the properties
   *        of the new entity.
   * @return an entry with the result of the operation.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public GenericEntry insertSettings(String username, GenericEntry entry, String setting)
      throws IOException, MalformedURLException, ServiceException {
    URL singleUrl = buildSettingsUrl(username, setting);
    return insert(singleUrl, entry);
  }

  /**
   * Update Gmail settings.
   *
   * @param username the user name or email for the affected user.
   * @param entry a {@link GenericEntry} object containing the new Gmail
   *        settings.
   * @return an entry with the result of the operation.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public GenericEntry updateSettings(String username, GenericEntry entry, String setting)
      throws IOException, MalformedURLException, ServiceException {
    URL singleUrl = buildSettingsUrl(username, setting);
    return update(singleUrl, entry);
  }

  /**
   * Builds the url to access the settings
   *
   * @param username the user name or email for which to access the settings.
   * @param setting the setting field to operate on.
   * @return a GenericEntry of requested settings
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   */
  private URL buildSettingsUrl(String username, String setting)
      throws IOException {
    String userDomain = domain;
    if (username.contains("@")) {
      String[] matches = username.split("@");
      username = matches[0];
      userDomain = matches[1];
    }
    URL url =
        new URL(
            Constants.PROTOCOL + "://" + Constants.APPS_APIS_DOMAIN + Constants.APPS_APIS_URL + "/"
                + userDomain + "/" + username + "/" + setting);
    
    return url;
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
   * @param forwardTo a boolean representing whether to automatically forward
   *        the message to the given verified email address if it matches the
   *        filter criteria.
   * @param neverSpam a boolean representing whether the message satisfying the
   *        filter criteria should never be marked as spam.
   * @param shouldStar a boolean representing whether to automatically star the
   *        message if it matches the specified filter criteria.
   * @param shouldTrash a boolean representing whether to automatically move the
   *        message to "Trash" state if it matches the specified filter criteria.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createFilter(List<String> users,
      String from,
      String to,
      String subject,
      String hasTheWord,
      String doesNotHaveTheWord,
      boolean hasAttachment,
      boolean shouldMarkAsRead,
      boolean shouldArchive,
      String label,
      String forwardTo,
      boolean neverSpam,
      boolean shouldStar,
      boolean shouldTrash)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.FROM, from);
    entry.addProperty(Constants.TO, to);
    entry.addProperty(Constants.SUBJECT, subject);
    entry.addProperty(Constants.HAS_THE_WORD, hasTheWord);
    entry.addProperty(Constants.DOESNT_HAVE_THE_WORD, doesNotHaveTheWord);
    entry.addProperty(Constants.HAS_ATTACHMENT, String.valueOf(hasAttachment));
    entry.addProperty(Constants.SHOULD_MARK_AS_READ, String.valueOf(shouldMarkAsRead));
    entry.addProperty(Constants.SHOULD_ARCHIVE, String.valueOf(shouldArchive));
    entry.addProperty(Constants.LABEL, label);
    entry.addProperty(Constants.FORWARD_TO, forwardTo);
    entry.addProperty(Constants.NEVER_SPAM, String.valueOf(neverSpam));
    entry.addProperty(Constants.SHOULD_STAR, String.valueOf(shouldStar));
    entry.addProperty(Constants.SHOULD_TRASH, String.valueOf(shouldTrash));

    for (String user : users) {
      logger.log(Level.INFO,
          "Creating filter ( " + "from: " + from + ", to: " + to + ", subject: " + subject
              + ", hasTheWord: " + hasTheWord + ", doesNotHaveTheWord: " + doesNotHaveTheWord
              + ", hasAttachment: " + hasAttachment + ", shouldMarkAsRead: " + shouldMarkAsRead
              + ", shouldArchive: " + shouldArchive + ", label: " + label
              + ", forwardTo: " + forwardTo + ", neverSpam: " + neverSpam
              + ", shouldStar: " + shouldStar + ", shouldTrash: " + shouldTrash
              + " ) for user " + user
              + " ...");
      insertSettings(user, entry, "filter");
      logger.log(Level.INFO, "Successfully created filter.");
    }
  }

  /**
   * Retrieves the send-as alias settings
   *
   * @param user
   * @return a list of send-as aliases
   * @throws IllegalArgumentException if the user hasn't been passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public List<Map<String, String>> retrieveSendAs(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting send-as settings for user " + user + " ...");

    GenericFeed sendAsFeed = retrieveSettingsFeed(user, Constants.SEND_AS);
    if (sendAsFeed != null) {
      List<Map<String, String>> sendAs = new ArrayList<Map<String, String>>();

      List<GenericEntry> sendAsEntries = sendAsFeed.getEntries();
      for (GenericEntry sendAsEntry : sendAsEntries) {
        Map<String, String> sendAsMap = new HashMap<String, String>();
        sendAsMap.put(Constants.ADDRESS, sendAsEntry.getProperty(Constants.ADDRESS));
        sendAsMap.put(Constants.NAME, sendAsEntry.getProperty(Constants.NAME));
        sendAsMap.put(Constants.REPLY_TO, sendAsEntry.getProperty(Constants.REPLY_TO));
        sendAsMap.put(Constants.IS_DEFAULT, sendAsEntry.getProperty(Constants.IS_DEFAULT));
        sendAsMap.put(Constants.VERIFIED, sendAsEntry.getProperty(Constants.VERIFIED));

        sendAs.add(sendAsMap);
      }
      return sendAs;
    }

    return null;
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
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createSendAs(
      List<String> users, String name, String address, String replyTo, boolean makeDefault)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.NAME, name);
    entry.addProperty(Constants.ADDRESS, address);
    entry.addProperty(Constants.REPLY_TO, replyTo);
    entry.addProperty(Constants.MAKE_DEFAULT, String.valueOf(makeDefault));

    for (String user : users) {
      logger.log(Level.INFO,
          "Creating send-as alias ( " + "name: " + name + ", address: " + address + ", replyTo: "
              + replyTo + ", makeDefault: " + makeDefault + " ) for user " + user + " ...");
      insertSettings(user, entry, "sendas");
      logger.log(Level.INFO, "Successfully created send-as alias.");
    }
  }

  /**
   * Retrieves all mail labels
   *
   * @param user
   * @return List of mail labels
   * @throws IllegalArgumentException if the user hasn't been passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public List<Map<String, String>> retrieveLabels(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting mail labels for user " + user + " ...");

    GenericFeed labelsFeed = retrieveSettingsFeed(user, Constants.LABEL);
    if (labelsFeed != null) {
      List<Map<String, String>> labels = new ArrayList<Map<String, String>>();

      List<GenericEntry> labelEntries = labelsFeed.getEntries();
      for (GenericEntry labelEntry : labelEntries) {
        Map<String, String> labelMap = new HashMap<String, String>();
        labelMap.put(Constants.LABEL, labelEntry.getProperty(Constants.LABEL));
        labelMap.put(Constants.UNREAD_COUNT, labelEntry.getProperty(Constants.UNREAD_COUNT));
        labelMap.put(Constants.VISIBILITY, labelEntry.getProperty(Constants.VISIBILITY));
        labels.add(labelMap);
      }
      return labels;
    }

    return null;
  }

  /**
   * Creates a label.
   *
   * @param users a list of the users to create the label for.
   * @param label a string that represents the name of the label.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void createLabel(List<String> users, String label)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.LABEL, label);

    for (String user : users) {
      logger.log(Level.INFO, "Creating label ( label: " + label + " ) for user " + user + " ...");
      insertSettings(user, entry, Constants.LABEL);
      logger.log(Level.INFO, "Successfully created label.");
    }
  }

  /**
   * Retrieves mail forwarding settings
   *
   * @param user
   * @return The value of forwarding settings
   * @throws IllegalArgumentException if the user hasn't been passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public Map<String, String> retrieveForwarding(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting forwarding settings for user " + user + " ...");

    GenericEntry forwardingEntry = retrieveSettingsEntry(user, Constants.FORWARDING);
    if (forwardingEntry != null) {
      Map<String, String> forwarding = new HashMap<String, String>();
      forwarding.put(Constants.ENABLE, forwardingEntry.getProperty(Constants.ENABLE));
      forwarding.put(Constants.FORWARD_TO, forwardingEntry.getProperty(Constants.FORWARD_TO));
      forwarding.put(Constants.ACTION, forwardingEntry.getProperty(Constants.ACTION));
      return forwarding;
    }

    return null;
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
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeForwarding(List<String> users, boolean enable, String forwardTo, String action)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty(Constants.ENABLE, Constants.TRUE);
      entry.addProperty(Constants.FORWARD_TO, forwardTo);
      entry.addProperty(Constants.ACTION, action);
    } else {
      entry.addProperty(Constants.ENABLE, Constants.FALSE);
    }

    for (String user : users) {
      if (enable) {
        logger.log(Level.INFO,
            "Updating forwarding settings ( " + "enable: true" + ", forwardTo: " + forwardTo
                + ", action: " + action + " ) for user" + user + " ...");
      } else {
        logger.log(Level.INFO,
            "Updating forwarding settings ( enable: false ) " + "for user" + user + " ...");
      }
      updateSettings(user, entry, Constants.FORWARDING);
      logger.log(Level.INFO, "Successfully updated forwarding settings.");
    }
  }

  /**
   * Retrieves POP3 settings
   *
   * @param user
   * @return The POP settings
   * @throws IllegalArgumentException if the user hasn't been passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public Map<String, String> retrievePop(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting POP settings for user " + user + " ...");

    GenericEntry popEntry = retrieveSettingsEntry(user, Constants.POP);
    if (popEntry != null) {
      Map<String, String> pop = new HashMap<String, String>();
      pop.put(Constants.ENABLE, popEntry.getProperty(Constants.ENABLE));
      pop.put(Constants.ACTION, popEntry.getProperty(Constants.ACTION));
      return pop;
    }

    return null;
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
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changePop(List<String> users, boolean enable, String enableFor, String action)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty(Constants.ENABLE, Constants.TRUE);
      entry.addProperty(Constants.ENABLE_FOR, enableFor);
      entry.addProperty(Constants.ACTION, action);
    } else {
      entry.addProperty(Constants.ENABLE, Constants.FALSE);
    }

    for (String user : users) {
      if (enable) {
        logger.log(Level.INFO,
            "Updating POP3 settings ( " + "enable: true" + ", enableFor: " + enableFor
                + ", action: " + action + " ) for user " + user + " ...");
      } else {
        logger.log(
            Level.INFO, "Updating POP3 settings ( enable: false ) for " + "user " + user + " ...");
      }
      updateSettings(user, entry, Constants.POP);
      logger.log(Level.INFO, "Successfully updated POP3 settings.");
    }
  }

  /**
   * Retrieves IMAP settings
   *
   * @param user
   * @return A boolean indicating whether IMAP settings are enabled
   * @throws IllegalArgumentException if the user hasn't been passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public boolean retrieveImap(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting IMAP settings for user " + user + " ...");

    GenericEntry imapEntry = retrieveSettingsEntry(user, Constants.IMAP);
    if (imapEntry != null && imapEntry.getProperty(Constants.ENABLE).equals(Constants.TRUE))
      return true;

    return false;
  }

  /**
   * Changes IMAP settings.
   *
   * @param users a list of the users to change the IMAP settings for.
   * @param enable whether to enable IMAP access.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeImap(List<String> users, boolean enable)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.ENABLE, String.valueOf(enable));

    for (String user : users) {
      logger.log(Level.INFO,
          "Updating IMAP settings ( enable: " + enable + " ) " + "for user " + user + " ...");
      updateSettings(user, entry, Constants.IMAP);
      logger.log(Level.INFO, "Successfully updated IMAP settings.");
    }
  }

  /**
   * Retrieves vacation settings
   *
   * @param user
   * @return The vacation auto-responder settings
   * @throws IllegalArgumentException if the user hasn't been passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public Map<String, String> retrieveVacation(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting vacation settings for user " + user + " ...");

    GenericEntry vacationEntry = retrieveSettingsEntry(user, Constants.VACATION);
    if (vacationEntry != null) {
      Map<String, String> vacation = new HashMap<String, String>();
      vacation.put(Constants.ENABLE, vacationEntry.getProperty(Constants.ENABLE));
      vacation.put(Constants.SUBJECT, vacationEntry.getProperty(Constants.SUBJECT));
      vacation.put(Constants.MESSAGE, vacationEntry.getProperty(Constants.MESSAGE));
      vacation.put(Constants.CONTACTS_ONLY, vacationEntry.getProperty(Constants.CONTACTS_ONLY));
      return vacation;
    }

    return null;
  }

  /**
   * Changes vacation-responder settings.
   *
   * @param users a list of the users to change the vacation-responder for.
   * @param enable whether to enable the vacation responder.
   * @param subject the subject line of the vacation responder autoresponse.
   * @param message the message body of the vacation responder autoresponse.
   * @param contactsOnly whether to only send the autoresponse to known
   *        contacts.
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeVacation(
      List<String> users, boolean enable, String subject, String message, boolean contactsOnly)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    if (enable) {
      entry.addProperty(Constants.ENABLE, Constants.TRUE);
      entry.addProperty(Constants.SUBJECT, subject);
      entry.addProperty(Constants.MESSAGE, message);
      entry.addProperty(Constants.CONTACTS_ONLY, String.valueOf(contactsOnly));
    } else {
      entry.addProperty(Constants.ENABLE, Constants.FALSE);
    }


    for (String user : users) {
      if (enable) {
        logger.log(Level.INFO,
            "Updating vacation-responder settings ( " + "enable: " + enable + ", subject: "
                + subject + ", message: " + message + ", contactsOnly: " + contactsOnly
                + " ) for user " + user + " ...");
      } else {
        logger.log(Level.INFO,
            "Updating vacation-responder settings ( " + "enable: false ) for user " + user
                + " ...");
      }
      updateSettings(user, entry, Constants.VACATION);
      logger.log(Level.INFO, "Successfully updated vacation-responder settings.");
    }
  }

  /**
   * Retrieves signature
   *
   * @param user
   * @return The signature
   * @throws IllegalArgumentException if the user hasn't been passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the retrieve request failed due to system
   *         error.
   */
  public String retrieveSignature(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting signature settings for user " + user + " ...");

    GenericEntry signatureEntry = retrieveSettingsEntry(user, Constants.SIGNATURE);
    if (signatureEntry != null) return signatureEntry.getProperty(Constants.SIGNATURE);

    return null;
  }

  /**
   * Changes signature.
   *
   * @param users a list of the users to change the signature for.
   * @param signature the signature to be appended to outgoing messages. Don't
   *        want a signature? Set the signature to "" (empty string).
   * @throws IllegalArgumentException if no users are passed in.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeSignature(List<String> users, String signature)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.SIGNATURE, signature);

    for (String user : users) {
      logger.log(Level.INFO,
          "Updating signature ( signature: " + signature + " ) for user " + user + " ...");
      updateSettings(user, entry, Constants.SIGNATURE);
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
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeGeneral(List<String> users,
      String pageSize,
      boolean enableShortcuts,
      boolean enableArrows,
      boolean enableSnippets,
      boolean enableUnicode)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.PAGE_SIZE, pageSize);
    entry.addProperty(Constants.SHORTCUTS, String.valueOf(enableShortcuts));
    entry.addProperty(Constants.ARROWS, String.valueOf(enableArrows));
    entry.addProperty(Constants.SNIPPETS, String.valueOf(enableSnippets));
    entry.addProperty(Constants.UNICODE, String.valueOf(enableUnicode));

    for (String user : users) {
      logger.log(Level.INFO,
          "Updating general settings ( " + "pageSize: " + pageSize + ", shortcuts: "
              + enableShortcuts + ", arrows: " + enableArrows + ", snippets: " + enableSnippets
              + ", unicode: " + enableUnicode + " ) for user " + user + " ...");
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
   *         service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws ServiceException if the insert request failed due to system error.
   */
  public void changeLanguage(List<String> users, String language)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.LANGUAGE, language);

    for (String user : users) {
      logger.log(Level.INFO,
          "Updating language settings ( language: " + language + " ) for user " + user + " ...");
      updateSettings(user, entry, Constants.LANGUAGE);
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
   *         GData service.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws IOException if the insert request failed due to system error.
   */
  public void changeWebClip(List<String> users, boolean enable)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (users.size() == 0) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.ENABLE, String.valueOf(enable));

    for (String user : users) {
      logger.log(Level.INFO,
          "Updating web clip settings ( enable: " + enable + " ) for user " + user + " ...");
      updateSettings(user, entry, "webclip");
      logger.log(Level.INFO, "Successfully updated web clip settings.");
    }
  }

  /**
   * Adds an email delegate.
   *
   * @param user The account for which an email delegate is to be added.
   * @param delegationEmail The emailId of the account to which delegate access
   *        is granted.
   * @throws IllegalArgumentException If no users are passed in.
   * @throws ServiceException If an error occurs while communicating with the
   *         GData service.
   * @throws MalformedURLException If the batch feed URL cannot be constructed.
   * @throws IOException If the insert request failed due to system error.
   */
  public void addEmailDelegate(String user, String delegationEmail)
      throws IllegalArgumentException, ServiceException, MalformedURLException, IOException {
    if (isBlankOrNullString(user) || isBlankOrNullString(delegationEmail)) {
      throw new IllegalArgumentException();
    }

    GenericEntry entry = new GenericEntry();
    entry.addProperty(Constants.ADDRESS, delegationEmail);

    logger.log(Level.INFO,
        "Adding " + delegationEmail + " as an email delegate for user " + user + " ...");
    insertSettings(user, entry, Constants.DELEGATION);
    logger.log(Level.INFO, "Successfully added an email delegate.");
  }

  /**
   * Retrieves the list of all email delegates of a user.
   *
   * @param user The account for which to get email delegates.
   * @return A list of all email delegates.
   * @throws IllegalArgumentException If no users are passed in.
   * @throws IOException If the insert request failed due to system error.
   * @throws ServiceException If an error occurs while communicating with the
   *         GData service.
   */
  public List<Map<String, String>> retrieveEmailDelegates(String user)
      throws IllegalArgumentException, IOException, ServiceException {
    if (isBlankOrNullString(user)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO, "Getting email delegation settings for user " + user + " ...");
    GenericFeed delegatesFeed = retrieveSettingsFeed(user, Constants.DELEGATION);
    if (delegatesFeed != null) {
      List<Map<String, String>> delegates = new ArrayList<Map<String, String>>();

      List<GenericEntry> delegateEntries = delegatesFeed.getEntries();
      for (GenericEntry delegateEntry : delegateEntries) {
        Map<String, String> delegateMap = new HashMap<String, String>();
        delegateMap.put(
            Constants.DELEGATION_ID, delegateEntry.getProperty(Constants.DELEGATION_ID));
        delegateMap.put(Constants.DELEGATE, delegateEntry.getProperty(Constants.DELEGATE));
        delegateMap.put(Constants.ADDRESS, delegateEntry.getProperty(Constants.ADDRESS));
        delegateMap.put(Constants.STATUS, delegateEntry.getProperty(Constants.STATUS));
        delegates.add(delegateMap);
      }
      return delegates;
    }

    return null;
  }

  /**
   * Removes the specified email delegate of a given user.
   *
   * @param user The user for which the delegate is to be removed.
   * @param delegationEmail The delegated emailId to be removed.
   * @throws IllegalArgumentException If no users are passed in.
   * @throws AppsForYourDomainException If an Apps for Your Domain API error
   *         occurred.
   * @throws ServiceException If an error occurs while communicating with the
   *         GData service.
   * @throws IOException If the insert request failed due to system error.
   */
  public void deleteEmailDelegate(String user, String delegationEmail)
      throws IllegalArgumentException, AppsForYourDomainException, ServiceException, IOException {
    if (isBlankOrNullString(user) || isBlankOrNullString(delegationEmail)) {
      throw new IllegalArgumentException();
    }

    logger.log(Level.INFO,
        "Deleting the email delegate " + delegationEmail + " for user " + user + " ...");
    URL deleteUrl =
        new URL(
            Constants.PROTOCOL + "://" + Constants.APPS_APIS_DOMAIN + Constants.APPS_APIS_URL + "/"
                + domain + "/" + user + "/" + Constants.DELEGATION + "/" + delegationEmail);
    delete(deleteUrl);
    logger.log(Level.INFO, "Successfully deleted an email delegate.");
  }

  /**
   * Checks if a given string is null or empty
   *
   * @param str
   * @return A boolean indicating whether the string is null or empty
   */
  private boolean isBlankOrNullString(String str) {
    return str == null || str.length() == 0 ? true : false;
  }
}
