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


package sample.contacts;

import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.http.HttpGDataRequest;
import sample.contacts.ContactsExampleParameters.Actions;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.ContactGroupEntry;
import com.google.gdata.data.contacts.ContactGroupFeed;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.OrgName;
import com.google.gdata.data.extensions.OrgTitle;
import com.google.gdata.data.extensions.Organization;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PostalAddress;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlBlob;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Example command-line utility that demonstrates how to use the Google Data API
 * Java client libraries for Contacts. The example allows to run all the basic
 * contact related operations such as adding new contact, listing all contacts,
 * updating existing contacts, deleting the contacts.
 * <p/>
 * Full documentation about the API can be found at:
 * http://code.google.com/apis/contacts/
 *
 * 
 * 
 */
public class ContactsExample {
  /**
   * Components used in parsing contact attributes
   */
  private static final String TITLE = "title:";
  private static final String PRIMARY_FALSE = "primary:false";
  private static final String PRIMARY_TRUE = "primary:true";
  private static final String PROTOCOL = "protocol:";
  private static final String LABEL = "label:";
  private static final String REL = "rel:";
  private static final String EXT_PROPERTY_FILE = "file:";
  private static final String EXT_PROPERTY_TEXT = "text:";

  /**
   * Reusable componentiser that parses element specification.
   */
  public static class ComponentParser {
    private String value;
    private String rel;
    private String label;
    private boolean primary;
    private String title;
    private String protocol;
    private String extPropertyFile;
    private String extPropertyText;

    public ComponentParser(String specification) {
      String s[] = specification.split(",");
      value = s[0];
      boolean first = true;
      for (String component : s) {
        if (first) {
          first = false;
          continue;
        }
        if (component.startsWith(REL)) {
          rel = component.substring(REL.length());
        } else if (component.startsWith(LABEL)) {
          label = component.substring(LABEL.length());
        } else if (component.equals(PRIMARY_TRUE)) {
          primary = true;
        } else if (component.equals(PRIMARY_FALSE)){
          primary = false;
        } else if (component.equals(PROTOCOL)){
          protocol = component.substring(PROTOCOL.length());
        } else if (component.startsWith(TITLE)) {
          title = component.substring(TITLE.length());
        } else if (component.startsWith(EXT_PROPERTY_FILE)) {
          extPropertyFile = component.substring(EXT_PROPERTY_FILE.length());
        } else if (component.startsWith(EXT_PROPERTY_TEXT)) {
          extPropertyText = component.substring(EXT_PROPERTY_TEXT.length());
        } else {
          printWarning(component, specification);
        }
      }
    }

    public String getRel() {
      return rel;
    }

    public String getLabel() {
      return label;
    }

    public boolean isPrimary() {
      return primary;
    }

    public String getTitle() {
      return title;
    }

    public String getProtocol() {
      return protocol;
    }

    public String getValue() {
      return value;
    }
    
    public String getExtPropertyFile() {
      return extPropertyFile;
    }
    
    public String getExtPropertyText() {
      return extPropertyText;
    }

    public boolean isSetRel() {
      return rel != null;
    }

    public boolean isSetLabel() {
      return label != null;
    }

    public boolean isSetTitle() {
      return title != null;
    }

    public boolean isSetProtocol() {
      return protocol != null;
    }
    
    public boolean isSetExtPropertyFile() {
      return extPropertyFile != null;
    }
    
    public boolean isSetExtPropertyText() {
      return extPropertyText != null;
    }

    public void printWarning(String component, String specification) {
      System.err.println("WARNING! Wrong component " + component +
          " in field specification:" + specification);
    }
  }

  /**
   * Base URL for the feed
   */
  private final URL feedUrl;

  /**
   * Service used to communicate with contacts feed.
   */
  private final ContactsService service;
  
  /**
   * Projection used for the feed
   */
  private final String projection;

  /**
   * The ID of the last added contact or group.
   * Used in case of script execution - you can add and remove contact just
   * created.
   */
  private static String lastAddedId;

  /**
   * Contacts Example.
   *
   * @param parameters command line parameters
   */
  public ContactsExample(ContactsExampleParameters parameters)
      throws MalformedURLException, AuthenticationException {
    projection = parameters.getProjection();
    String url = parameters.getBaseUrl()
        + (parameters.isGroupFeed() ? "groups/" : "contacts/")
        + parameters.getUserName() + "/" + projection;

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
   * Deletes a contact or a group
   *
   * @param parameters the parameters determining contact to delete.
   */
  private void deleteEntry(ContactsExampleParameters parameters)
    throws IOException, ServiceException {
    URL entryURL;
    if (parameters.isGroupFeed()) {
      // get the Group then delete it
      ContactGroupEntry group = getGroupInternal(parameters.getId());
      if (group == null) {
        System.err.println("No Group found with id: " + parameters.getId());
        return;
      }
      entryURL = new URL(group.getEditLink().getHref());
    } else {
      // get the contact then delete them
      ContactEntry contact = getContactInternal(parameters.getId());
      if (contact == null) {
        System.err.println("No contact found with id: " + parameters.getId());
        return;
      }
      entryURL = new URL(contact.getEditLink().getHref());
    }
    service.delete(entryURL);
  }

  /**
   * Updates a contact or a group. Presence of any property of a given kind 
   * (im, phone, mail, etc.) causes the existing properties of that kind to be 
   * replaced.
   *
   * @param parameters parameters storing updated contact values.
   */
  public void updateEntry(ContactsExampleParameters parameters)
      throws IOException, ServiceException {
    if (parameters.isGroupFeed()) {
      ContactGroupEntry group = buildGroup(parameters);
      // get the group then update it
      ContactGroupEntry canonicalGroup = getGroupInternal(parameters.getId());
  
      canonicalGroup.setTitle(group.getTitle());
      canonicalGroup.setContent(group.getContent());
      // update fields
      List<ExtendedProperty> extendedProperties = 
          canonicalGroup.getExtendedProperties();
      extendedProperties.clear();
      if (group.hasExtendedProperties()) {
        extendedProperties.addAll(group.getExtendedProperties());
      }
      URL entryUrl = new URL(canonicalGroup.getEditLink().getHref());
      printGroup(service.update(entryUrl, canonicalGroup)); 
    } else {
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
    
      List<GroupMembershipInfo> groups = 
          canonicalContact.getGroupMembershipInfos();
      groups.clear();
      if (contact.hasGroupMembershipInfos()) {
        groups.addAll(contact.getGroupMembershipInfos());
      }
    
      List<ExtendedProperty> extendedProperties = 
          canonicalContact.getExtendedProperties();
      extendedProperties.clear();
      if (contact.hasExtendedProperties()) {
        extendedProperties.addAll(contact.getExtendedProperties());
      }

      URL entryUrl = new URL(canonicalContact.getEditLink().getHref());
      printContact(service.update(entryUrl, canonicalContact));  
    }
  }

  /**
   * Gets a contact by it's id.
   *
   * @param id the id of the contact.
   * @return the ContactEntry or null if not found.
   */
  private ContactEntry getContactInternal(String id)
      throws IOException, ServiceException {
    return service.getEntry(new URL(id.replace("/base/", "/" + projection + "/")),
        ContactEntry.class);
  }

  /**
   * Gets a Group by it's id.
   *
   * @param id the id of the group.
   * @return the GroupEntry or null if not found.
   */
  private ContactGroupEntry getGroupInternal(String id)
      throws IOException, ServiceException {
    return service.getEntry(new URL(id.replace("/base/", "/" + projection + "/")),
        ContactGroupEntry.class);
  }
  
  /**
   * Print the contents of a ContactEntry to System.err.
   *
   * @param contact The ContactEntry to display.
   */
  private static void printContact(ContactEntry contact) {
    System.err.println("Id: " + contact.getId()); 
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
    System.err.println("Groups:");
    for (GroupMembershipInfo group : contact.getGroupMembershipInfos()) {      
      System.err.println("  Id: " + group.getHref() + " Deleted? " + group.getDeleted());
    }
    System.err.println("Extended Properties:");
    for (ExtendedProperty property : contact.getExtendedProperties()) {
      if (property.getValue() != null) {
        System.err.println("  " + property.getName() + "(value) = " + 
            property.getValue());
      } else if (property.getXmlBlob() != null) {
        System.err.println("  " + property.getName() + "(xmlBlob)= " + 
            property.getXmlBlob().getBlob());
      }
    }
    if (contact.getLink("http://schemas.google.com/contacts/2008/rel#photo",
        "image/*") != null) {
      String photoLink = contact.getLink(
          "http://schemas.google.com/contacts/2008/rel#photo", 
          "image/*").getHref();
      System.err.println("Photo Link:" + photoLink);
    }
    System.err.println("Edit-Photo link: " + contact.getLink(
        "http://schemas.google.com/contacts/2008/rel#edit-photo", "image/*").getHref() );
    System.err.println("Self link: " + contact.getSelfLink().getHref());
    System.err.println("Edit link: " + contact.getEditLink().getHref());
    System.err.println("-------------------------------------------\n");
  }
  
  /**
   * Prints the contents of a GroupEntry to System.err
   * 
   * @param groupEntry The GroupEntry to display
   */
  private static void printGroup(ContactGroupEntry groupEntry) {
    System.err.println("Id: " + groupEntry.getId());
    System.err.println("Group Name: " + groupEntry.getTitle().getPlainText());
    System.err.println("Last Updated: " + groupEntry.getUpdated());
    System.err.println("Extended Properties:");
    for (ExtendedProperty property : groupEntry.getExtendedProperties()) {
      if (property.getValue() != null) {
        System.err.println("  " + property.getName() + "(value) = " + 
            property.getValue());
      } else if (property.getXmlBlob() != null) {
        System.err.println("  " + property.getName() + "(xmlBlob) = " + 
            property.getXmlBlob().getBlob());
      }
    }
    System.err.println("Self Link: " + groupEntry.getSelfLink().getHref());
    System.err.println("Edit Link: " + groupEntry.getEditLink().getHref());
    System.err.println("-------------------------------------------\n");
  }

  
  /**
   * Processes script consisting of sequence of parameter lines in the same
   * form as command line parameters.
   *
   * @param example object controlling the execution
   * @param parameters parameters passed from command line
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
   *
   * @param example object controlling the execution
   * @param parameters parameters from command line or script
   */
  private static void processAction(ContactsExample example,
      ContactsExampleParameters parameters) throws IOException,
      ServiceException {
    Actions action = parameters.getAction();
    System.err.println("Executing action: " + action);
    switch (action) {
      case LIST:
        example.listEntries(parameters);
        break;
      case QUERY:
        example.queryEntries(parameters);
        break;
      case ADD:
        example.addEntry(parameters);
        break;
      case DELETE:
        example.deleteEntry(parameters);
        break;
      case UPDATE:
        example.updateEntry(parameters);
        break;
      default:
        System.err.println("No such action");
    }
  }

  /**
   * Query entries (Contacts/Groups) according to parameters specified.
   *
   * @param parameters parameter for contact quest
   */
  private void queryEntries(ContactsExampleParameters parameters)
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
    if (parameters.getGroup() != null) {
      myQuery.setStringCustomParameter("group", parameters.getGroup());
    }
    if (parameters.isGroupFeed()) {
      ContactGroupFeed groupFeed = service.query(
          myQuery, ContactGroupFeed.class);
      for (ContactGroupEntry entry : groupFeed.getEntries()) {
        printGroup(entry);
      }
      System.err.println("Total: " + groupFeed.getEntries().size()
          + " entries found");
    } else {
      ContactFeed resultFeed = service.query(myQuery, ContactFeed.class);
      for (ContactEntry entry : resultFeed.getEntries()) {
        printContact(entry);
      }
      System.err.println("Total: " + resultFeed.getEntries().size()
          + " entries found");
    }
  }

  /**
   * List Contacts or Group entries (no parameter are taken into account)
   * Note! only 25 results will be returned - this is default.
   *
   * @param parameters
   */
  private void listEntries(ContactsExampleParameters parameters)
      throws IOException, ServiceException {
    if (parameters.isGroupFeed()) {
      ContactGroupFeed groupFeed = 
          service.getFeed(feedUrl, ContactGroupFeed.class);    
      System.err.println(groupFeed.getTitle().getPlainText());
      for (ContactGroupEntry entry : groupFeed.getEntries()) {
        printGroup(entry);
      }
      System.err.println("Total: " + groupFeed.getEntries().size() + 
          " groups found");
    } else {
      ContactFeed resultFeed = service.getFeed(feedUrl, ContactFeed.class);
      // Print the results
      System.err.println(resultFeed.getTitle().getPlainText());
      for (ContactEntry entry : resultFeed.getEntries()) {
        printContact(entry);
        // Get the photo if photolink is present
        Link photoLink = entry.getLink(
            "http://schemas.google.com/contacts/2008/rel#photo", "image/*");
        if (photoLink != null) {
          InputStream in = service.getStreamFromLink(photoLink);
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          RandomAccessFile file = new RandomAccessFile(
              "/tmp/" + entry.getSelfLink().getHref().substring(
              entry.getSelfLink().getHref().lastIndexOf('/') + 1), "rw");
          byte[] buffer = new byte[4096];
          for (int read = 0; (read = in.read(buffer)) != -1;
              out.write(buffer, 0, read));
          file.write(out.toByteArray());
          file.close();
        }
      }
    System.err.println("Total: " + resultFeed.getEntries().size()
        + " entries found");
    }
  }

  /**
   * Adds contact or group entry according to the parameters specified.
   *
   * @param parameters parameters for contact adding
   */
  private void addEntry(ContactsExampleParameters parameters)
      throws IOException, ServiceException {
    if (parameters.isGroupFeed()) {
      ContactGroupEntry addedGroup = 
          service.insert(feedUrl, buildGroup(parameters));
      printGroup(addedGroup);
      lastAddedId = addedGroup.getId();
    } else {
      ContactEntry addedContact =
          service.insert(feedUrl, buildContact(parameters));
      printContact(addedContact);
      // Store id of the added contact so that scripts can use it in next steps
      lastAddedId = addedContact.getId();
    }
  }


  /**
   * Parses email command line parameter
   *
   * @param value parameter value in the form of
   *  email[,rel:REL|,label:LABEL]][,primary:[true|false]]
   * @return the email object parsed
   */
  private static Email parseEmail(String value) {
    ComponentParser c = new ComponentParser(value);
    Email mail = new Email();
    mail.setAddress(c.getValue());
    if (c.isSetRel()) {
      mail.setRel(c.getRel());
    }
    if (c.isSetLabel()) {
      mail.setLabel(c.getLabel());
    }
    if (c.isSetProtocol()) {
      c.printWarning(c.getProtocol(), value);
    }
    if (c.isSetTitle()) {
      c.printWarning(c.getTitle(), value);
    }
    mail.setPrimary(c.isPrimary());
    return mail;
  }

  /**
   * Parses im command line parameter
   *
   * @param value parameter value in the form of
   * address:[,rel:REL|label:LABEL][,protocol:PROTOCOL][,primary:true|false]
   * @return the im object parsed
   */
  private static Im parseIm(String value) {
    ComponentParser c = new ComponentParser(value);
    Im im = new Im();
    im.setAddress(c.getValue());
    if (c.isSetRel()) {
      im.setRel(c.getRel());
    }
    if (c.isSetLabel()) {
      im.setLabel(c.getLabel());
    }
    if (c.isSetProtocol()) {
      im.setProtocol(c.getProtocol());
    }
    if (c.isSetTitle()) {
      c.printWarning(c.getTitle(), value);
    }
    im.setPrimary(c.isPrimary());
    return im;
  }

  /**
   * Parses phone command line parameter
   *
   * @param value parameter value in the form of
   * phone:[,rel:REL|label:LABEL][,primary:true|false]
   * @return the phone object parsed
   */
  private static PhoneNumber parsePhone(String value) {
    ComponentParser c = new ComponentParser(value);
    PhoneNumber phone = new PhoneNumber();
    phone.setPhoneNumber(c.getValue());
    if (c.isSetRel()) {
      phone.setRel(c.getRel());
    }
    if (c.isSetLabel()) {
      phone.setLabel(c.getLabel());
    }
    if (c.isSetProtocol()) {
      c.printWarning(c.getProtocol(), value);
    }
    if (c.isSetTitle()) {
      c.printWarning(c.getTitle(), value);
    }
    phone.setPrimary(c.isPrimary());
    return phone;
  }

  /**
   * Parses postal address command line parameter
   *
   * @param value parameter value in the form of
   * address:[,rel:REL|label:LABEL][,primary:true|false]
   * @return the postal address object parsed
   */
  private static PostalAddress parsePostalAddress(String value) {
    ComponentParser c = new ComponentParser(value);
    PostalAddress address = new PostalAddress();
    address.setValue(c.getValue());
    if (c.isSetRel()) {
      address.setRel(c.getRel());
    }
    if (c.isSetLabel()) {
      address.setLabel(c.getLabel());
    }
    if (c.isSetProtocol()) {
      c.printWarning(c.getProtocol(), value);
    }
    if (c.isSetTitle()) {
      c.printWarning(c.getTitle(), value);
    }
    address.setPrimary(c.isPrimary());
    return address;
  }

  /**
   * Parses organization command line parameter
   * @param value parameter value
   * @return the organization object parsed
   */
  private static Organization parseOrganization(String value) {
    ComponentParser c = new ComponentParser(value);
    Organization organization = new Organization();
    organization.setOrgName(new OrgName(c.getValue()));
    if (c.isSetRel()) {
      organization.setRel(c.getRel());
    }
    if (c.isSetLabel()) {
      organization.setLabel(c.getLabel());
    }
    if (c.isSetProtocol()) {
      c.printWarning(c.getProtocol(), value);
    }
    if (c.isSetTitle()) {
      organization.setOrgTitle(new OrgTitle(c.getTitle()));
    }
    organization.setPrimary(c.isPrimary());
    return organization;
  }
  
  /**
   * Parses Group command line parameter, needs the Username and baseUrl for 
   * creating HREF for the group
   *  
   * @param value Parameter value
   * @param baseUrl URL parameter
   * @param username Username of the authenticated user 
   * @return GroupMembershipInfo object parsed
   */
  private static GroupMembershipInfo parseGroup(String value, String baseUrl,
      String username) {
    ComponentParser c = new ComponentParser(value);
    GroupMembershipInfo groupMembershipInfo = new GroupMembershipInfo();
    groupMembershipInfo.setDeleted(false);
    groupMembershipInfo.setHref(baseUrl + "groups/" + username + "/full/" + 
        c.getValue());
    return groupMembershipInfo;
  }
  
  /**
   * Parses ExtendedProperty command line parameter
   * @param value Parameter value
   * @return ExtendedProperty object parsed
   * @throws FileNotFoundException When File specified for the Property value 
   *                               does not exist
   * @throws IOException           When error occurs while reading file 
   */
  private static ExtendedProperty parseExtendedProperty(String value) 
      throws FileNotFoundException, IOException {
    ComponentParser c = new ComponentParser(value);
    ExtendedProperty extendedProperty = new ExtendedProperty();
    extendedProperty.setName(c.getValue());
    if (c.isSetExtPropertyFile()) {
      File f = new File(c.getExtPropertyFile());
      if (!f.exists()) {
        throw new FileNotFoundException("No Such File:" + 
            c.getExtPropertyFile());
      }
      BufferedReader reader = new BufferedReader(new FileReader(f));
      StringBuffer xmlBuffer = new StringBuffer();
      String line;
      while ((line = reader.readLine()) != null) {
        xmlBuffer.append(line);
      }
      XmlBlob xmlBlob = new XmlBlob();
      xmlBlob.setBlob(new String(xmlBuffer));
      extendedProperty.setXmlBlob(xmlBlob); 
    } 
    if (c.isSetExtPropertyText()) {
      extendedProperty.setValue(c.getExtPropertyText());
    }
    return extendedProperty;
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
    for (String string : parameters.getEmails()) {
      contact.addEmailAddress(parseEmail(string));
    }
    for (String string : parameters.getPhones()) {
      contact.addPhoneNumber(parsePhone(string));
    }
    for (String string : parameters.getOrganizations()) {
      contact.addOrganization(parseOrganization(string));
    }
    for (String string : parameters.getIms()) {
      contact.addImAddress(parseIm(string));
    }
    for (String string : parameters.getPostal()) {
      contact.addPostalAddress(parsePostalAddress(string));
    }
    for (String string : parameters.getGroups()) {
      contact.addGroupMembershipInfo(parseGroup(string, 
          parameters.getBaseUrl(), parameters.getUserName()));
    }
    for (String string : parameters.getExtendedProperties()) {
      try {
        contact.addExtendedProperty(parseExtendedProperty(string));
      } catch (FileNotFoundException e) {
        System.err.println("File Not Found!" + e.getMessage());
      } catch (IOException e) {
        System.err.println("Exception while reading file" + e.getMessage());
      }
    }
    return contact;
  }

  /**
   * Builds GroupEntry from parameters
   *  
   * @param parameters ContactExamplParameters
   * @return GroupEntry Object
   */
  private static ContactGroupEntry buildGroup(
      ContactsExampleParameters parameters) {
    ContactGroupEntry groupEntry = new ContactGroupEntry();
    groupEntry.setTitle(new PlainTextConstruct(parameters.getName()));
    for (String string : parameters.getExtendedProperties()) {
      try {
        groupEntry.addExtendedProperty(parseExtendedProperty(string));
      } catch (FileNotFoundException e) {
        System.err.println("File Not Found!" + e.getMessage());
      } catch (IOException e) {
        System.err.println("Exception while reading file" + e.getMessage());
      }
    }
    return groupEntry;
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
            + "             --groupid<n>=<groupid>\n"
            + "             --extendedProperty<n>=<name>,"
            + "text:<value>|file:<XmlFilePath> \n"
            + " Notes! <n> is a unique number for the field - several fields\n"
            + " of the same type can be present (example: im1, im2, im3).\n"
            + " Available rels and protocols can be looked up in the \n"
            + " feed documentation.\n";


    String usageInstructions =
        "USAGE:\n"
            + " -----------------------------------------------------------\n"
            + "  Basic command line usage:\n"
            + "    ContactsExample [<options>] <authenticationInformation> "
            + "<--contactfeed|--groupfeed> "
            + "--action=<action> [<action options>]  "
            + "(default contactfeed)\n"
            + "  Scripting commands usage:\n"
            + "    contactsExample [<options>] <authenticationInformation> "
            + "<--contactfeed|--groupfeed>   --script=<script file>  "
            + "(default contactFeed) \n"
            + "  Print usage (this screen):\n"
            + "   --help\n"
            + " -----------------------------------------------------------\n\n"
            + "  Options: \n"
            + "    --base-url=<url to connect to> "
            + "(default http://www.google.com/m8/feeds/) \n"
            + "    --projection=[thin|full|property-KEY] "
            + "(default thin)\n"
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
            + "             --querygroupid=<groupid> : return results from the "
            + "group\n"
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
    
    // Check that at most one of contactfeed and groupfeed has been provided
    if (parameters.isContactFeed() && parameters.isGroupFeed()) {
      throw new RuntimeException("Only one of contactfeed / groupfeed should" +
          "be specified");
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
