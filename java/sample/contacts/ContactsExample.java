/* Copyright (c) 2006 Google Inc.
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


package sample.contacts;

import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.OrgName;
import com.google.gdata.data.extensions.OrgTitle;
import com.google.gdata.data.extensions.Organization;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PostalAddress;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example command-line utility that demonstrates how to use the Google Data API
 * Java client libraries for Contacts. The example allows to run all the basic
 * contact related operations such as adding new contact, listing all contacts,
 * updating existing contacts, deleting the contacts.
 *
 * Full documentation about the API can be found at:
 * http://code.google.com/apis/contacts/
 *
 * 
 * 
 */
public class ContactsExample {

  /**
   * Base URL for the feed
   */
  private final URL feedUrl;
  private final ContactsService service;

  /**
   * The ID that of last added contact.
   * Used in case of script execution - you can add and remove contact just
   * created.
   */
  private static String lastAddedId;


  /**
   * Contacts Example.
   *
   * @param parameters command line parameters
   * @throws IOException
   * @throws ServiceException
   */
  public ContactsExample(ContactsExampleParameters parameters)
      throws IOException, ServiceException {

    String url =
        parameters.getBaseUrl() + "contacts/" + parameters.getUserName()
            + "/base";

    feedUrl = new URL(url);
    service = new ContactsService("Google-contactsExampleApp-1");
    String userName = parameters.getUserName();
    String password = parameters.getPassword();
    if (userName == null || password == null) {
      return;
    }
    service.setUserCredentials(userName, password);
  }

  /**
   * Deletes a contact
   *
   * @param parameters the parameters determining contact to delete.
   * @throws IOException
   * @throws ServiceException
   */
  private void deleteContact(ContactsExampleParameters parameters)
      throws IOException, ServiceException {
    // get the contact then delete them
    ContactEntry contact = getContactInternal(parameters.getId());
    if (contact == null) {
      System.err.println("No contact found with id: " + parameters.getId());
    }
    URL entryUrl = new URL(contact.getEditLink().getHref());
    service.delete(entryUrl);
  }

  /**
   * Updates a contact. Presence of any property of a given kind (im, phone,
   * mail, etc.) causes the existing properties of that kind to be replaced.
   *
   * @param parameters parameters storing updated contact values.
   * @throws IOException
   * @throws ServiceException
   */
  public void updateContact(ContactsExampleParameters parameters)
      throws IOException, ServiceException {

    ContactEntry contact = buildContact(parameters);
    // get the contact then update it
    ContactEntry canonicalContact = getContactInternal(parameters.getId());

    canonicalContact.setTitle(contact.getTitle());
    canonicalContact.setContent(contact.getContent());
    // update fields
    List<Email> emails = canonicalContact.getEmailAddresses();
    emails.clear();
    if (contact.hasEmailAddresses()) {
      emails.addAll(contact.getEmailAddresses());
    }

    List<Im> ims = canonicalContact.getImAddresses();
    ims.clear();
    if (contact.hasImAddresses()) {
      ims.addAll(contact.getImAddresses());
    }

    List<Organization> organizations = canonicalContact.getOrganizations();
    organizations.clear();
    if (contact.hasOrganizations()) {
      organizations.addAll(contact.getOrganizations());
    }

    List<PhoneNumber> phones = canonicalContact.getPhoneNumbers();
    phones.clear();
    if (contact.hasPhoneNumbers()) {
      phones.addAll(contact.getPhoneNumbers());
    }

    List<PostalAddress> addresses = canonicalContact.getPostalAddresses();
    addresses.clear();
    if (contact.hasPostalAddresses()) {
      addresses.addAll(contact.getPostalAddresses());
    }

    URL entryUrl = new URL(canonicalContact.getEditLink().getHref());
    printContact(service.update(entryUrl, canonicalContact));
  }

  /**
   * Gets a contact by it's id.
   *
   * @param id the id of the contact.
   * @return the ContactEntry or null if not found.
   * @throws IOException
   * @throws ServiceException
   */
  private ContactEntry getContactInternal(String id) throws IOException,
      ServiceException {
    return service.getEntry(new URL(feedUrl.toExternalForm() + "/" + id),
            ContactEntry.class);
  }

  /**
   * Print the contents of a ContactEntry to System.err.
   *
   * @param contact The ContactEntry to display.
   */
  private static void printContact(ContactEntry contact) {
    System.err.println("Id: " +
        contact.getId().substring(contact.getId().lastIndexOf("/")+1));
    String contactName =
        (contact.getTitle() == null) ? "" : contact.getTitle().getPlainText();
    System.err.println("Contact name: " + contactName);
    String contactNotes =
        (contact.getContent() == null) ? "" : contact.getTextContent()
            .getContent().getPlainText();
    System.err.println("Contact notes: " + contactNotes);
    System.err.println("Last updated: " + contact.getUpdated().toUiString());
    if (contact.hasDeleted()) {
      System.err.println("Deleted:");
    }
    System.err.println("Email addresses:");
    for (Email email : contact.getEmailAddresses()) {
      System.err.print("  " + email.getAddress());
      if (email.getRel() != null) {
        System.err.print(" rel:" + email.getRel());
      }
      if (email.getLabel() != null) {
        System.err.print(" label:" + email.getLabel());
      }
      if (email.getPrimary()) {
        System.err.print(" (primary) ");
      }
      System.err.print("\n");
    }

    System.err.println("IM addresses:");
    for (Im im : contact.getImAddresses()) {
      System.err.print("  " + im.getAddress());
      if (im.getLabel() != null) {
        System.err.print(" label:" + im.getLabel());
      }
      if (im.getRel() != null) {
        System.err.print(" rel:" + im.getRel());
      }
      if (im.getProtocol() != null) {
        System.err.print(" protocol:" + im.getProtocol());
      }
      if (im.getPrimary()) {
        System.err.print(" (primary) ");
      }
      System.err.print("\n");
    }

    System.err.println("Phone numbers:");
    for (PhoneNumber phone : contact.getPhoneNumbers()) {
      System.err.print("  " + phone.getPhoneNumber());
      if (phone.getRel() != null) {
        System.err.print(" rel:" + phone.getRel());
      }
      if (phone.getLabel() != null) {
        System.err.print(" label:" + phone.getLabel());
      }
      if (phone.getPrimary()) {
        System.err.print(" (primary) ");
      }
      System.err.print("\n");
    }

    System.err.println("Addressses:");
    for (PostalAddress address : contact.getPostalAddresses()) {
      System.err.print("  " + address.getValue());
      if (address.getRel() != null) {
        System.err.print(" rel:" + address.getRel());
      }
      if (address.getLabel() != null) {
        System.err.print(" label:" + address.getLabel());
      }
      if (address.getPrimary()) {
        System.err.print(" (primary) ");
      }
      System.err.print("\n");
    }
    System.err.println("Organizations:");
    for (Organization organization : contact.getOrganizations()) {
      System.err.print(" Name: " + organization.getOrgName().getValue());
      if (organization.getOrgTitle() != null) {
        System.err.print(" Title: " + organization.getOrgTitle().getValue());
      }
      if (organization.getRel() != null) {
        System.err.print(" rel:" + organization.getRel());
      }
      if (organization.getLabel() != null) {
        System.err.print(" label:" + organization.getLabel());
      }
      if (organization.getPrimary()) {
        System.err.print(" (primary) ");
      }
      System.err.print("\n");
    }

    System.err.println("Self link: " + contact.getSelfLink().getHref());
    System.err.println("Edit link: " + contact.getEditLink().getHref());
    System.err.println("-------------------------------------------\n");
  }

  /**
   * Processes script consisting of sequence of parameter lines in the same
   * form as command line parameters
   * @param example object controlling the execution
   * @param parameters parameters passed from command line
   * @throws IOException
   * @throws ServiceException
   */
  private static void processScript(ContactsExample example,
      ContactsExampleParameters parameters) throws IOException,
      ServiceException {
    BufferedReader reader =
        new BufferedReader(new FileReader(parameters.getScript()));
    String line;
    while ((line = reader.readLine()) != null) {
      ContactsExampleParameters newParams =
          new ContactsExampleParameters(parameters, line);
      processAction(example, newParams);
      if (lastAddedId != null) {
        parameters.setId(lastAddedId);
        lastAddedId = null;
      }
    }
  }

  /**
   * Performs action specified as action parameter.
   * @param example object controlling the execution
   * @param parameters parameters from command line or script
   * @throws IOException
   * @throws ServiceException
   */
  private static void processAction(ContactsExample example,
      ContactsExampleParameters parameters) throws IOException,
      ServiceException {
    String action = parameters.getAction();
    System.err.println("Executing action: " + action);
    if ("list".equals(action)) {
      example.listContacts(parameters);
    } else if ("query".equals(action)) {
      example.queryContacts(parameters);
    } else if ("add".equals(action)) {
      example.addContact(parameters);
    } else if ("delete".equals(action)) {
      example.deleteContact(parameters);
    } else if ("update".equals(action)) {
      example.updateContact(parameters);
    } else  {
      System.err.println("No such action");
    }
  }

  /**
   * Query contacts according to parameters specified.
   *
   * @param parameters parameter for contact quest
   * @throws IOException
   * @throws ServiceException
   */
  private void queryContacts(ContactsExampleParameters parameters)
      throws IOException, ServiceException {
    Query myQuery = new Query(feedUrl);
    if (parameters.getUpdatedMin() != null) {
      DateTime startTime = DateTime.parseDateTime(parameters.getUpdatedMin());
      myQuery.setUpdatedMin(startTime);
    }
    if (parameters.getMaxResults() != null) {
      myQuery.setMaxResults(parameters.getMaxResults().intValue());
    }
    if (parameters.getStartIndex() != null) {
      myQuery.setStartIndex(parameters.getStartIndex());
    }
    if (parameters.isShowDeleted()) {
      myQuery.setStringCustomParameter("showdeleted", "true");
    }
    if (parameters.getSortorder() != null) {
      myQuery.setStringCustomParameter("sortorder", parameters.getSortorder());
    }
    if (parameters.getOrderBy() != null) {
      myQuery.setStringCustomParameter("orderby", parameters.getOrderBy());
    }
    ContactFeed resultFeed = service.query(myQuery, ContactFeed.class);
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      ContactEntry entry = resultFeed.getEntries().get(i);
      printContact(entry);
    }
    System.err.println("Total: " + resultFeed.getEntries().size()
        + " entries found");
  }

  /**
   * List contacts (no parameter are taken into account)
   * Note! only 25 results will be returned - this is default.
   * @param parameters
   * @throws IOException
   * @throws ServiceException
   */
  private void listContacts(ContactsExampleParameters parameters)
      throws IOException, ServiceException {
    ContactFeed resultFeed = service.getFeed(feedUrl, ContactFeed.class);
    // Print the results
    System.err.println(resultFeed.getTitle().getPlainText());
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      ContactEntry entry = resultFeed.getEntries().get(i);
      printContact(entry);
    }
    System.err.println("Total: " + resultFeed.getEntries().size()
        + " entries found");
  }

  /**
   * Adds contact according to the parameters specified.
   *
   * @param parameters parameters for contact adding
   * @throws IOException
   * @throws ServiceException
   */
  private void addContact(ContactsExampleParameters parameters)
      throws IOException, ServiceException {
    ContactEntry addedContact =
        service.insert(feedUrl, buildContact(parameters));
    printContact(addedContact);
    // Store id of the added contact so that scripts can use it in next steps
    String longId[] = addedContact.getId().split("/");
    lastAddedId = longId[longId.length - 1];
  }

  /**
   * Parses email command line parameter
   * @param value parameter value
   * @return the email object parsed
   */
  private static Email parseEmail(String value) {
    String s[] = value.split(",");
    Email mail = new Email();
    mail.setAddress(s[0]);
    for (String component : s) {
      if (component.startsWith("rel:")) {
        mail.setRel(component.substring(4));
      }
      if (component.startsWith("label:")) {
        mail.setLabel(component.substring(6));
      }
      if (component.equals("primary:true")) {
        mail.setPrimary(true);
      } else if (component.equals("primary:false")){
        mail.setPrimary(false);
      }
    }
    return mail;
  }

  /**
   * Parses im command line parameter
   * @param value parameter value
   * @return the im object parsed
   */
  private static Im parseIm(String value) {
    String s[] = value.split(",");
    Im im = new Im();
    im.setAddress(s[0]);
    for (String component : s) {
      if (component.startsWith("rel:")) {
        im.setRel(component.substring(4));
      }
      if (component.startsWith("label:")) {
        im.setLabel(component.substring(6));
      }
      if (component.startsWith("protocol:")) {
        im.setProtocol(component.substring(9));
      }
      if (component.equals("primary:true")) {
        im.setPrimary(true);
      } else if (component.equals("primary:false")){
        im.setPrimary(false);
      }
    }
    return im;
  }

  /**
   * Parses phone command line parameter
   * @param value parameter value
   * @return the phone object parsed
   */
  private static PhoneNumber parsePhone(String value) {
    String s[] = value.split(",");
    PhoneNumber phone = new PhoneNumber();
    phone.setPhoneNumber(s[0]);
    for (String component : s) {
      if (component.startsWith("rel:")) {
        phone.setRel(component.substring(4));
      }
      if (component.startsWith("label:")) {
        phone.setLabel(component.substring(6));
      }
      if (component.equals("primary:true")) {
        phone.setPrimary(true);
      } else if (component.equals("primary:false")){
        phone.setPrimary(false);
      }
    }
    return phone;
  }

  /**
   * Parses postal address command line parameter
   * @param value parameter value
   * @return the postal address object parsed
   */
  private static PostalAddress parsePostalAddress(String value) {
    String s[] = value.split(",");
    PostalAddress address = new PostalAddress();
    address.setValue(s[0]);
    for (String component : s) {
      if (component.startsWith("rel:")) {
        address.setRel(component.substring(4));
      }
      if (component.startsWith("label:")) {
        address.setLabel(component.substring(6));
      }
      if (component.equals("primary:true")) {
        address.setPrimary(true);
      } else if (component.equals("primary:false")){
        address.setPrimary(false);
      }
    }
    return address;
  }

  /**
   * Parses organization command line parameter
   * @param value parameter value
   * @return the organization object parsed
   */

  private static Organization parseOrganization(String value) {
    String s[] = value.split(",");
    Organization organization = new Organization();
    organization.setOrgName(new OrgName(s[0]));
    for (String component : s) {
      if (component.startsWith("title:")) {
        organization.setOrgTitle(new OrgTitle(component.substring(6)));
      }
      if (component.startsWith("rel:")) {
        organization.setRel(component.substring(4));
      }
      if (component.startsWith("label:")) {
        organization.setLabel(component.substring(6));
      }
      if (component.equals("primary:true")) {
        organization.setPrimary(true);
      } else if (component.equals("primary:false")){
        organization.setPrimary(false);
      }
    }
    return organization;
  }

  /**
   * Build ContactEntry from parameters.
   *
   * @param parameters parameters
   * @return A contact.
   */
  private static ContactEntry buildContact(
                 ContactsExampleParameters parameters) {
    ContactEntry contact = new ContactEntry();
    contact.setTitle(new PlainTextConstruct(parameters.getName()));
    if (parameters.getNotes() != null) {
      contact.setContent(new PlainTextConstruct(parameters.getNotes()));
    }

    LinkedList<String> emails = parameters.getEmails();
    for (String string : emails) {
      contact.addEmailAddress(parseEmail(string));
    }
    LinkedList<String> phones = parameters.getPhones();
    for (String string : phones) {
      contact.addPhoneNumber(parsePhone(string));
    }
    LinkedList<String> organizations = parameters.getOrganizations();
    for (String string : organizations) {
      contact.addOrganization(parseOrganization(string));
    }
    LinkedList<String> ims = parameters.getIms();
    for (String string : ims) {
      contact.addImAddress(parseIm(string));
    }
    LinkedList<String> postal = parameters.getPostal();
    for (String string : postal) {
      contact.addPostalAddress(parsePostalAddress(string));
    }
    return contact;
  }

  /**
   * Displays usage information.
   */
  private static void displayUsage() {
    String contactParameters =
        "             --name=<name> : contact name\n"
            + "             --notes=<notes> : notes about the contact\n"
            + "             --email<n>=<email>,"
            + "rel:<rel>|label:<label>[,primary:true|false]\n"
            + "             --phone<n>=<phone>,"
            + "rel:<rel>|label:<label>[,primary:true|false]\n"
            + "             --organization<n>=<organization>,"
            + "rel:<rel>|label:<label>[,title:<title>][,primary:true|false]\n"
            + "             --im<n>=<im>,rel:<rel>|label:<label>"
            + "[,protocol:<protocol>][,primary:true|false]\n"
            + "             --postal<n>=<postal>,"
            + "rel:<rel>|label:<label>[,primary:true|false]\n"
            + " Notes! <n> is a unique number for the field - several fields\n"
            + " of the same type can be present (example: im1, im2, im3).\n"
            + " Available rels and protocols can be looked up in the \n"
            + " feed documentation.\n";


    String usageInstructions =
        "USAGE:\n"
            + " -----------------------------------------------------------\n"
            + "  Basic command line usage:\n"
            + "    ContactsExample [<options>] <authenticationInformation> "
            + "--action=<action> [<action options>]\n"
            + "  Scripting commands usage:\n"
            + "    contactsExample [<options>] <authenticationInformation> "
            + "   --script=<script file>\n"
            + "  Print usage (this screen):\n"
            + "   --help\n"
            + " -----------------------------------------------------------\n\n"
            + "  Options: \n"
            + "    --base-url=<url to connect to> "
            + "(default http://www.google.com/m8/feeds/) \n"
            + "    --verbose : dumps communication information\n"
            + "  Authentication Information (obligatory on command line): \n"
            + "    --username=<username email> --password=<password>\n"
            + "  Actions: \n"
            + "     * list  list all contacts\n"
            + "     * query  query contacts\n"
            + "        options:\n"
            + "             --showdeleted : shows also deleted contacts\n"
            + "             --updated-min=YYYY-MM-DDTHH:MM:SS : only updated "
            + "after the time specified\n"
            + "             --orderby=lastmodified : order by last modified\n"
            + "             --sortorder=[ascending|descending] : sort order\n"
            + "             --max-results=<n> : return maximum n results\n"
            + "             --start-index=<n> : return results starting from "
            + "the starting index\n"
            + "    * add  add new contact\n"
            + "        options:\n"
            + contactParameters
            + "    * delete  delete contact\n"
            + "        options:\n"
            + "             --id=<contact id>\n"
            + "    * update  updates contact\n"
            + "        options:\n"
            + "             --id=<contact id>\n"
            + contactParameters
            ;

    System.err.println(usageInstructions);
  }


  /**
   * Run the example program.
   *
   * @param args Command-line arguments.
   */
  public static void main(String[] args) throws ServiceException, IOException {
    Logger httpRequestLogger =
        Logger.getLogger(HttpGDataRequest.class.getName());

    ContactsExampleParameters parameters = new ContactsExampleParameters(args);
    if (parameters.isVerbose()) {
      httpRequestLogger.setLevel(Level.FINEST);
      ConsoleHandler handler = new ConsoleHandler();
      handler.setLevel(Level.FINEST);
      httpRequestLogger.addHandler(handler);
      httpRequestLogger.setUseParentHandlers(false);
    }

    if (parameters.numberOfParameters() == 0 || parameters.isHelp()
        || (parameters.getAction() == null && parameters.getScript() == null)) {
      displayUsage();
      return;
    }

    if (parameters.getUserName() == null || parameters.getPassword() == null) {
      System.err.println("Both username and password must be specified.");
      return;
    }

    ContactsExample example = new ContactsExample(parameters);

    if (parameters.getScript() != null) {
      processScript(example, parameters);
    } else {
      processAction(example, parameters);
    }
    System.out.flush();
  }


}
