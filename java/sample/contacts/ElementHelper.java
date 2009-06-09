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

import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.contacts.BillingInformation;
import com.google.gdata.data.contacts.Birthday;
import com.google.gdata.data.contacts.CalendarLink;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactGroupEntry;
import com.google.gdata.data.contacts.DirectoryServer;
import com.google.gdata.data.contacts.Event;
import com.google.gdata.data.contacts.ExternalId;
import com.google.gdata.data.contacts.Gender;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.contacts.Hobby;
import com.google.gdata.data.contacts.Initials;
import com.google.gdata.data.contacts.Jot;
import com.google.gdata.data.contacts.Language;
import com.google.gdata.data.contacts.MaidenName;
import com.google.gdata.data.contacts.Mileage;
import com.google.gdata.data.contacts.Nickname;
import com.google.gdata.data.contacts.Occupation;
import com.google.gdata.data.contacts.Priority;
import com.google.gdata.data.contacts.Relation;
import com.google.gdata.data.contacts.Sensitivity;
import com.google.gdata.data.contacts.ShortName;
import com.google.gdata.data.contacts.Subject;
import com.google.gdata.data.contacts.UserDefinedField;
import com.google.gdata.data.contacts.Website;
import com.google.gdata.data.extensions.AdditionalName;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.extensions.Country;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.FamilyName;
import com.google.gdata.data.extensions.FormattedAddress;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.GivenName;
import com.google.gdata.data.extensions.Im;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.NamePrefix;
import com.google.gdata.data.extensions.NameSuffix;
import com.google.gdata.data.extensions.Neighborhood;
import com.google.gdata.data.extensions.OrgDepartment;
import com.google.gdata.data.extensions.OrgJobDescription;
import com.google.gdata.data.extensions.OrgName;
import com.google.gdata.data.extensions.OrgSymbol;
import com.google.gdata.data.extensions.OrgTitle;
import com.google.gdata.data.extensions.Organization;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PoBox;
import com.google.gdata.data.extensions.PostCode;
import com.google.gdata.data.extensions.Region;
import com.google.gdata.data.extensions.Street;
import com.google.gdata.data.extensions.StructuredPostalAddress;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.util.XmlBlob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class to parse, update and display a contact.
 * It uses a bunch of anonymous inner classes for every kind of element to 
 * perform the actual job. It is defined as an enum to take advantage of the 
 * automatic instantiation and constant-specific methods.
 *
 * 
 */
public enum ElementHelper implements ElementHelperInterface {

  BILLING_INFORMATION {
    public void parse(ContactEntry contact, ElementParser parser) {
      BillingInformation billingInformation = new BillingInformation();
      billingInformation.setValue(parser.get(PropertyName.VALUE));
      contact.setBillingInformation(billingInformation);
    }

    public void print(PrintStream out, ContactEntry contact) {
       if (contact.hasBillingInformation()) {
         out.println("billing information: "
             + contact.getBillingInformation().getValue());
       }
    }

    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasBillingInformation()) {
        dest.setBillingInformation(src.getBillingInformation());
      }
    }

    public String getUsage() {
      return "<billing_information>";
    }
  },
  
  BIRTHDAY {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Birthday birthday = new Birthday();
      birthday.setWhen(parser.get(PropertyName.VALUE));
      contact.setBirthday(birthday);
    }
  
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasBirthday()) {
        out.println("birthday: " + contact.getBirthday().getWhen());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasBirthday()) {
        dest.setBirthday(src.getBirthday());
      }
    }

    public String getUsage() {
      return "YYYY-MM-DD|--MM-DD";
    }
  },
  
  CALENDAR_LINK(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      CalendarLink calendarLink = new CalendarLink();
      calendarLink.setHref(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        calendarLink.setRel(
            CalendarLink.Rel.valueOf(
                parser.get(PropertyName.REL).toUpperCase()));
      }
      if (parser.has(PropertyName.LABEL)) {
        calendarLink.setLabel(parser.get(PropertyName.LABEL));
      }
      if (parser.has(PropertyName.PRIMARY)) {
        calendarLink.setPrimary(parser.is(PropertyName.PRIMARY));
      }
      contact.addCalendarLink(calendarLink);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasCalendarLinks()) {
        out.println("calendar links:");
        for (CalendarLink calendarLink : contact.getCalendarLinks()) {
          out.print("  " + calendarLink.getHref());
          if (calendarLink.hasRel()) {
            out.print(" rel:" + calendarLink.getRel());
          } else if (calendarLink.hasLabel()) {
            out.print(" label:" + calendarLink.getLabel());
          }
          if (calendarLink.hasPrimary() && calendarLink.getPrimary()) {
            out.print(" (primary)");
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasCalendarLinks()) {
        List<CalendarLink> calendarLinks = dest.getCalendarLinks();
        calendarLinks.clear();
        calendarLinks.addAll(src.getCalendarLinks());
      }
    }

    public String getUsage() {
      return "<href>"
          + "[,rel:<rel>]"
          + "[,label:<label>]"
          + "[,primary:true|false]";
    }
  },
  
  DIRECTORY_SERVER {
    public void parse(ContactEntry contact, ElementParser parser) { 
      DirectoryServer directoryServer = new DirectoryServer();
      directoryServer.setValue(parser.get(PropertyName.VALUE));
      contact.setDirectoryServer(directoryServer);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasDirectoryServer()) {
        out.println("directory server: " 
            + contact.getDirectoryServer().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasDirectoryServer()) {
        dest.setDirectoryServer(src.getDirectoryServer());
      }
    }

    public String getUsage() {
      return "<directory_server>";
    }
  },
  
  EMAIL(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Email email = new Email();
      email.setAddress(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        email.setRel(parser.get(PropertyName.REL));
      }
      if (parser.has(PropertyName.LABEL)) {
        email.setLabel(parser.get(PropertyName.LABEL));
      }
      if (parser.has(PropertyName.PRIMARY)) {
        email.setPrimary(parser.is(PropertyName.PRIMARY));
      }
      contact.addEmailAddress(email);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasEmailAddresses()) {
        out.println("email addresses:");
        for (Email email : contact.getEmailAddresses()) {
          out.print("  " + email.getAddress());
          if (email.getRel() != null) {
            out.print(" rel:" + email.getRel());
          }
          if (email.getLabel() != null) {
            out.print(" label:" + email.getLabel());
          }
          if (email.getPrimary()) {
            out.print(" (primary)");
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasEmailAddresses()) {
        List<Email> emailAddresses = dest.getEmailAddresses();
        emailAddresses.clear();
        emailAddresses.addAll(src.getEmailAddresses());
      }
    }

    public String getUsage() {
      return "<email>"
          + "[,rel:<rel>]"
          + "[,label:<label>]"
          + "[,primary:true|false]";
    }
  },
  
  EVENT(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Event event = new Event();
      When when = new When();
      when.setStartTime(DateTime.parseDate(parser.get(PropertyName.VALUE)));
      event.setWhen(when);
      if (parser.has(PropertyName.REL)) {
        event.setRel(parser.get(PropertyName.REL));
      }
      if (parser.has(PropertyName.LABEL)) {
        event.setLabel(parser.get(PropertyName.LABEL));
      }
      contact.addEvent(event);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasEvents()) {
        out.println("events:");
        for (Event event : contact.getEvents()) {
          out.print("  " + event.getWhen().getStartTime().toString());
          if (event.hasRel()) {
            out.print(" rel:" + event.getRel());
          }
          if (event.hasLabel()) {
            out.print(" label:" + event.getLabel());
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasEvents()) {
        List<Event> events = dest.getEvents();
        events.clear();
        events.addAll(src.getEvents());
      }
    }

    public String getUsage() {
      return "<YYYY-MM-DD>"
        + "[,rel:<rel>]"
        + "[,label:<label>]";
    }
  },
  
  EXTENDED_PROPERTY(true) {
    /**
     * Reads an XmlBlob from a file.
     * 
     * @param f the file to be readed.
     * @return the readed XmlBlog.
     * 
     * @throws IOException in case of any IO error.
     */
    private XmlBlob readFromFile(File f) throws IOException {
      StringBuffer xmlBuffer = new StringBuffer();
        BufferedReader reader = null;
      try {
        reader = new BufferedReader(new FileReader(f));
        String line;
        while ((line = reader.readLine()) != null) {
          xmlBuffer.append(line);
        }
      } finally {
        if (reader != null) {
          reader.close();
        }
      }
      XmlBlob xmlBlob = new XmlBlob();
      xmlBlob.setBlob(new String(xmlBuffer));
      return xmlBlob;
    }
    
    /**
     * Parses an ExtendedProperty.
     * 
     * @param parser the parser used for the parsing of the description. 
     * @return the parsed in ExtendedProperty.
     */
    private ExtendedProperty parse(ElementParser parser) {
      ExtendedProperty extendedProperty = new ExtendedProperty();
      extendedProperty.setName(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.FILE)) {
        File f = new File(parser.get(PropertyName.FILE));
        if (!f.exists()) {
          throw new RuntimeException("No Such File:" + 
              parser.get(PropertyName.FILE));
        }
        try {
          extendedProperty.setXmlBlob(readFromFile(f));
        } catch (IOException ex) {
          throw new RuntimeException("Failed to read file "
              + parser.get(PropertyName.FILE));
        }
      } else if (parser.has(PropertyName.TEXT)) {
        extendedProperty.setValue(parser.get(PropertyName.TEXT));
      }
      return extendedProperty;
    }
    
    public void parse(ContactEntry contact, ElementParser parser) { 
      ExtendedProperty extendedProperty = parse(parser);
      contact.addExtendedProperty(extendedProperty);
    }

    public void parseGroup(ContactGroupEntry group, ElementParser parser) {
      ExtendedProperty extendedProperty = parse(parser);
      group.addExtendedProperty(extendedProperty);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasExtendedProperties()) {
        out.println("extended properties:");
        for (ExtendedProperty property : contact.getExtendedProperties()) {
          out.print("  " + property.getName());
          if (property.hasValue()) {
            out.print(" value:" + property.getValue());
          } else {
            out.print(" xmlBlob:" + property.getXmlBlob());
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasExtendedProperties()) {
        List<ExtendedProperty> extendedProps = dest.getExtendedProperties();
        extendedProps.clear();
        extendedProps.addAll(src.getExtendedProperties());
      }

    }

    public String getUsage() {
      return "<name>,text:<value>|file:<XmlFilePath>";
    }
  },
  
  EXTERNAL_ID(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      ExternalId externalId = new ExternalId();
      externalId.setValue(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        externalId.setRel(parser.get(PropertyName.REL));
      }
      if (parser.has(PropertyName.LABEL)) {
        externalId.setLabel(parser.get(PropertyName.LABEL));
      }
      contact.addExternalId(externalId);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasExternalIds()) {
        out.println("external ids:");
        for (ExternalId externalId : contact.getExternalIds()) {
          out.print("  " + externalId.getValue());
          if (externalId.hasRel()) {
            out.print(" rel:" + externalId.getRel());
          }
          if (externalId.hasLabel()) {
            out.print(" label:" + externalId.getLabel());
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasExternalIds()) {
        List<ExternalId> externalIds = dest.getExternalIds();
        externalIds.clear();
        externalIds.addAll(src.getExternalIds());
      }
    }
    
    public String getUsage() {
      return "<external_id>,rel:<rel>|label:<label>";
    }
  },
  
  GENDER {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Gender gender = new Gender();
      String value = parser.get(PropertyName.VALUE);
      if (value.equals("male")) {
        gender.setValue(Gender.Value.MALE);
      } else if (value.equals("female")) {
        gender.setValue(Gender.Value.FEMALE);
      } else {
        throw new IllegalArgumentException("gender should be male or female");
      }
      contact.setGender(gender);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasGender()) {
        out.println("gender: " 
            + contact.getGender().getValue().toString().toLowerCase());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasGender()) {
        dest.setGender(src.getGender());
      }
    }

    public String getUsage() {
      return "male|female";
    }
  },
  
  GROUP_MEMBERSHIP_INFO(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      GroupMembershipInfo groupMembershipInfo = new GroupMembershipInfo();
      groupMembershipInfo.setHref(parser.get(PropertyName.VALUE));
      contact.addGroupMembershipInfo(groupMembershipInfo);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasGroupMembershipInfos()) {
        out.println("group membership info:");
        for (GroupMembershipInfo group : contact.getGroupMembershipInfos()) {
          out.print("  " + group.getHref());
          if (group.getDeleted()) {
            out.print(" (deleted)");
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasGroupMembershipInfos()) {
        List<GroupMembershipInfo> groupMembershipInfos 
            = dest.getGroupMembershipInfos();
        groupMembershipInfos.clear();
        groupMembershipInfos.addAll(src.getGroupMembershipInfos());
      }
    }

    public String getUsage() {
      return "<url>";
    }
  },
  
  HOBBY(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Hobby hobby = new Hobby();
      hobby.setValue(parser.get(PropertyName.VALUE));
      contact.addHobby(hobby);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasHobbies()) {
        out.println("hobbies:");
        for (Hobby hobby : contact.getHobbies()) {
          out.println("  " + hobby.getValue());
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasHobbies()) {
        List<Hobby> hobbies = dest.getHobbies();
        hobbies.clear();
        hobbies.addAll(src.getHobbies());
      }
    }

    public String getUsage() {
      return "<hobby>";
    }
  },
  
  IM(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Im im = new Im();
      im.setAddress(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        im.setRel(parser.get(PropertyName.REL));
      }
      if (parser.has(PropertyName.LABEL)) {
        im.setLabel(parser.get(PropertyName.LABEL));
      }
      if (parser.has(PropertyName.PROTOCOL)) {
        im.setProtocol(parser.get(PropertyName.PROTOCOL));
      }
      if (parser.has(PropertyName.PRIMARY)) {
        im.setPrimary(parser.is(PropertyName.PRIMARY));
      }
      contact.addImAddress(im);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasImAddresses()) {
        out.println("im addresses:");
        for (Im im : contact.getImAddresses()) {
          out.print("  " + im.getAddress());
          if (im.hasRel()) {
            out.print(" rel:" + im.getRel());
          } else if (im.hasLabel()) {
            out.print(" label:" + im.getLabel());
          }
          if (im.hasProtocol()) {
            out.print(" protocol:" + im.getProtocol());
          }
          if (im.getPrimary()) {
            out.print(" (primary)");
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasImAddresses()) {
        List<Im> ims = dest.getImAddresses();
        ims.clear();
        ims.addAll(src.getImAddresses());
      }
    }

    public String getUsage() {
      return "<im>"
          + "[,rel:<rel>]"
          + "[,label:<label>]"
          + "[,protocol:<protocol>]"
          + "[,primary:true|false]";
    }
  },

  INITIALS {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Initials initials = new Initials();
      initials.setValue(parser.get(PropertyName.VALUE));
      contact.setInitials(initials);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasInitials()) {
        out.println("initials: " + contact.getInitials().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasInitials()) {
        dest.setInitials(src.getInitials());
      }
    }

    public String getUsage() {
      return "<initials>";
    }
  },
  
  JOT(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Jot jot = new Jot();
      jot.setValue(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        jot.setRel(Jot.Rel.valueOf(parser.get(PropertyName.REL).toUpperCase()));
      }
      contact.addJot(jot);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasJots()) {
        out.println("jots:");
        for (Jot jot : contact.getJots()) {
          out.print("  " + jot.getValue());
          if (jot.hasRel()) {
            out.print(" rel:" + jot.getRel().toString().toLowerCase());
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasJots()) {
        List<Jot> jots = dest.getJots();
        jots.clear();
        jots.addAll(src.getJots());
      }
    }

    public String getUsage() {
      return "<jot>"
          + "[,rel:home|work|other|keywords|user]";
    }
  },
  
  LANGUAGE(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Language language = new Language();
      language.setLabel(parser.get(PropertyName.VALUE));
      contact.addLanguage(language);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasLanguages()) {
        out.println("languages:");
        for (Language language : contact.getLanguages()) {
          out.println("  " + language.getLabel());
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasLanguages()) {
        List<Language> languages = dest.getLanguages();
        languages.clear();
        languages.addAll(src.getLanguages());
      }
    }

    public String getUsage() {
      return "<language>";
    }
  },
  
  WHERE {
    public void parse(ContactEntry contact, ElementParser parser) {
      contact.setWhere(new Where(null, null, parser.get(PropertyName.VALUE)));
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasWhere()) {
        out.println("where: " + contact.getWhere().getValueString());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasWhere()) {
        dest.setWhere(src.getWhere());
      }
    }

    public String getUsage() {
      return "<where>";
    }
  },
 
  
  MAIDEN_NAME {
    public void parse(ContactEntry contact, ElementParser parser) { 
      MaidenName maidenName = new MaidenName();
      maidenName.setValue(parser.get(PropertyName.VALUE));
      contact.setMaidenName(maidenName);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasMaidenName()) {
        out.println("maiden name: " + contact.getMaidenName().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasMaidenName()) {
        dest.setMaidenName(src.getMaidenName());
      }
    }

    public String getUsage() {
      return "<maiden_name>";
    }
  },
    
  MILEAGE {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Mileage mileage = new Mileage();
      mileage.setValue(parser.get(PropertyName.VALUE));
      contact.setMileage(mileage);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasMileage()) {
        out.println("mileage: " + contact.getMileage().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasMileage()) {
        dest.setMileage(src.getMileage());
      }
    }

    public String getUsage() {
      return "<mileage>";
    }
  },
  
  NAME {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Name name = new Name();
      name.setFullName(new FullName(parser.get(PropertyName.VALUE), null));
      if (parser.has(PropertyName.GIVEN)) {
        name.setGivenName(new GivenName(parser.get(PropertyName.GIVEN), null));
      } 
      if (parser.has(PropertyName.FAMILY)) {
        name.setFamilyName(
            new FamilyName(parser.get(PropertyName.FAMILY), null));
      }
      if (parser.has(PropertyName.ADDITIONAL)) {
        name.setAdditionalName(
            new AdditionalName(parser.get(PropertyName.ADDITIONAL), null));
      }
      if (parser.has(PropertyName.PREFIX)) {
        name.setNamePrefix(new NamePrefix(parser.get(PropertyName.PREFIX)));
      }
      if (parser.has(PropertyName.SUFFIX)) {
        name.setNameSuffix(new NameSuffix(parser.get(PropertyName.SUFFIX)));
      }
      contact.setName(name);
    }

    public void parseGroup(ContactGroupEntry group, ElementParser parser) {
      group.setTitle(new PlainTextConstruct(parser.get(PropertyName.VALUE)));
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasName()) {
        out.println("structured name: ");
        Name name = contact.getName();
        if (name.hasFullName()) {
          out.print(" full name: " + name.getFullName().getValue());
        }
        if (name.hasGivenName()) {
          out.print(" given name: " + name.getGivenName().getValue());
        }
        if (name.hasFamilyName()) {
          out.print(" family name: " + name.getFamilyName().getValue());
        }
        if (name.hasAdditionalName()) {
          out.print(" additional name: " + name.getAdditionalName().getValue());
        }
        if (name.hasNamePrefix()) {
          out.print(" prefix: " + name.getNamePrefix().getValue());
        }
        if (name.hasNameSuffix()) {
          out.print(" suffix: " + name.getNameSuffix().getValue());
        }
        out.println();
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasName()) {
        dest.setName(src.getName());
      }
    }

    public String getUsage() {
      return "<name>" 
          + "[,given:<givenName]"
          + "[,family:<familyName>]"
          + "[,additional:additionalName]"
          + "[,prefix:<prefix>]"
          + "[,suffix:<suffix>]";
    }
  },

  NICKNAME {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Nickname nickname = new Nickname();
      nickname.setValue(parser.get(PropertyName.VALUE));
      contact.setNickname(nickname);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasNickname()) {
        out.println("nickname: " + contact.getNickname().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasNickname()) {
        dest.setNickname(src.getNickname());
      }
    }

    public String getUsage() {
      return "<nickname>";
    }
  },

  NOTES {
    public void parse(ContactEntry contact, ElementParser parser) { 
      contact.setContent(
          new PlainTextConstruct(parser.get(PropertyName.VALUE)));
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.getContent() != null) { 
        out.println("notes: " 
            + contact.getTextContent().getContent().getPlainText());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.getContent() != null) {
        dest.setContent(src.getContent());
      }
    }

    public String getUsage() {
      return "<notes>";
    }
  },
  
  OCCUPATION {
    
    public void parse(ContactEntry contact, ElementParser parser) { 
      Occupation occupation = new Occupation();
      occupation.setValue(parser.get(PropertyName.VALUE));
      contact.setOccupation(occupation);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasOccupation()) {
        out.println("occupation: " + contact.getOccupation().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasOccupation()) {
        dest.setOccupation(src.getOccupation());
      }
    }

    public String getUsage() {
      return "<occupation>";
    }
  },

  ORGANIZATION(true) {
    public void parse(ContactEntry contact, ElementParser parser) {
      Organization org = new Organization();
      org.setOrgName(new OrgName(parser.get(PropertyName.VALUE)));
      if (parser.has(PropertyName.DEPARTMENT)) {
        org.setOrgDepartment(
            new OrgDepartment(parser.get(PropertyName.DEPARTMENT)));
      }
      if (parser.has(PropertyName.REL)) {
        org.setRel(parser.get(PropertyName.REL));
      }
      if (parser.has(PropertyName.LABEL)) {
        org.setLabel(parser.get(PropertyName.LABEL));
      }
      if (parser.has(PropertyName.TITLE)) {
        org.setOrgTitle(new OrgTitle(parser.get(PropertyName.TITLE)));
      }
      if (parser.has(PropertyName.SYMBOL)) {
        org.setOrgSymbol(new OrgSymbol(parser.get(PropertyName.SYMBOL)));
      }
      if (parser.has(PropertyName.DESCRIPTION)) {
        org.setOrgJobDescription(
            new OrgJobDescription(parser.get(PropertyName.DESCRIPTION)));
      }
      if (parser.has(PropertyName.WHERE)) {
        Where where = new Where();
        where.setValueString(parser.get(PropertyName.WHERE));
        org.setWhere(where);
      }
      contact.addOrganization(org);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasOrganizations()) {
        out.println("organizations:");
        for (Organization organization : contact.getOrganizations()) {
          out.print("  " + organization.getOrgName().getValue());
          if (organization.hasRel()) {
            out.print(" rel:" + organization.getRel());
          }
          if (organization.hasLabel()) {
            out.print(" label:" + organization.getLabel());
          }
          if (organization.hasOrgDepartment()) {
            out.print(" department:"
                + organization.getOrgDepartment().getValue());
          }
          if (organization.hasOrgTitle()) {
            out.print(" title:" + organization.getOrgTitle().getValue());
          }
          if (organization.hasOrgSymbol()) {
            out.print(" symbol:" + organization.getOrgSymbol().getValue());
          }
          if (organization.hasWhere()) {
            out.print(" where:" + organization.getWhere().getValueString());
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasOrganizations()) {
        List<Organization> organizations = dest.getOrganizations();
        organizations.clear();
        organizations.addAll(src.getOrganizations());
      }
    }

    public String getUsage() {
      return "<name>"
          + "[,rel:<rel>]"
          + "[,label<label>]"
          + "[,primary:true|false]"
          + "[,department:<department>]"
          + "[,description:<description>]"
          + "[,symbol:<symbol>]"
          + "[,title:<title>]"
          + "[,where:<where>]";
    }
  },

  PHONE(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      PhoneNumber phone = new PhoneNumber();
      phone.setPhoneNumber(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        phone.setRel(parser.get(PropertyName.REL));
      }
      if (parser.has(PropertyName.LABEL)) {
        phone.setLabel(parser.get(PropertyName.LABEL));
      }
      if (parser.has(PropertyName.URI)) {
        phone.setUri(parser.get(PropertyName.URI));
      }
      if (parser.has(PropertyName.PRIMARY)) {
        phone.setPrimary(parser.is(PropertyName.PRIMARY));
      }
      contact.addPhoneNumber(phone);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasPhoneNumbers()) {
        out.println("phone numbers:");
        for (PhoneNumber phone : contact.getPhoneNumbers()) {
          out.print("  " + phone.getPhoneNumber());
          if (phone.getRel() != null) {
            out.print(" rel:" + phone.getRel());
          }
          if (phone.getLabel() != null) {
            out.print(" label:" + phone.getLabel());
          }
          if (phone.getUri() != null) {
            out.print(" uri:" + phone.getUri());
          }
          if (phone.getPrimary()) {
            out.print(" (primary)");
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasPhoneNumbers()) {
        List<PhoneNumber> phoneNumbers = dest.getPhoneNumbers();
        phoneNumbers.clear();
        phoneNumbers.addAll(src.getPhoneNumbers());
      }
    }

    public String getUsage() {
      return "<phone>"
          + "[,rel:<rel>]"
          + "[,label:<label>]"
          + "[,uri:<uri>]"
          + "[,primary:true|false]";
    }
  },
  
  PRIORITY {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Priority priority = new Priority();
      priority.setRel(
          Priority.Rel.valueOf(parser.get(PropertyName.VALUE).toUpperCase()));
      contact.setPriority(priority);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasPriority()) {
        out.println("priority: " 
            + contact.getPriority().getRel().toString().toLowerCase());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasPriority()) {
        dest.setPriority(src.getPriority());
      }
    }

    public String getUsage() {
      return "low|normal|high";
    }
  },
  
  RELATION(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Relation relation = new Relation();
      relation.setValue(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        relation.setRel(
            Relation.Rel.valueOf(parser.get(PropertyName.REL).toUpperCase()));
      }
      if (parser.has(PropertyName.LABEL)) {
        relation.setLabel(parser.get(PropertyName.LABEL));
      }
      contact.addRelation(relation);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasRelations()) {
        out.println("relations:");
        for (Relation relation : contact.getRelations()) {
          out.print("  " + relation.getValue());
          if (relation.hasLabel()) {
            out.print(" label:" + relation.getLabel());
          } else if (relation.hasRel()) {
            out.print(" rel:" + relation.getRel().toString().toLowerCase());
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasRelations()) {
        List<Relation> relations = dest.getRelations();
        relations.clear();
        relations.addAll(src.getRelations());
      }
    }

    public String getUsage() {
      return "<relation>"
          + "[,label:<label>]"
          + "[,rel:<rel>]";
    }
  },
  
  SENSITIVITY {
    
    public void parse(ContactEntry contact, ElementParser parser) { 
      Sensitivity sensitivity = new Sensitivity();
      sensitivity.setRel(
          Sensitivity.Rel.valueOf(
              parser.get(PropertyName.VALUE).toUpperCase()));
      contact.setSensitivity(sensitivity);
    }
  
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasSensitivity()) {
        out.println("sensitivity:" 
            + contact.getSensitivity().getRel().toString().toLowerCase());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasSensitivity()) {
        dest.setSensitivity(src.getSensitivity());
      }
    }

    public String getUsage() {
      return "confidental|normal|personal|private";
    }
  },
  
  SHORT_NAME {
    public void parse(ContactEntry contact, ElementParser parser) { 
      ShortName shortName = new ShortName();
      shortName.setValue(parser.get(PropertyName.VALUE));
      contact.setShortName(shortName);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasShortName()) {
        out.println("short name:" + contact.getShortName().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasShortName()) {
        dest.setShortName(src.getShortName());
      }
    }

    public String getUsage() {
      return "<short_name>";
    }
  },
  
  POSTAL(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      StructuredPostalAddress address = new StructuredPostalAddress();
      if (parser.has(PropertyName.REL)) {
        address.setRel(parser.get(PropertyName.REL));
      }
      if (parser.has(PropertyName.LABEL)) {
        address.setLabel(parser.get(PropertyName.LABEL));
      }
      if (parser.has(PropertyName.PRIMARY)) {
        address.setPrimary(parser.is(PropertyName.PRIMARY));
      }
      if (parser.has(PropertyName.CITY)) {
        address.setCity(new City(parser.get(PropertyName.CITY)));
      }
      if (parser.has(PropertyName.COUNTRY)) {
        // Don't care about country code
        address.setCountry(new Country(null, parser.get(PropertyName.COUNTRY)));
      }
      if (parser.has(PropertyName.FORMATTED)) {
        address.setFormattedAddress(
            new FormattedAddress(parser.get(PropertyName.FORMATTED)));
      }
      if (parser.has(PropertyName.NEIGHBORHOOD)) {
        address.setNeighborhood(
            new Neighborhood(parser.get(PropertyName.NEIGHBORHOOD)));
      }
      if (parser.has(PropertyName.POBOX)) {
        address.setPobox(new PoBox(parser.get(PropertyName.POBOX)));
      }
      if (parser.has(PropertyName.POSTCODE)) {
        address.setPostcode(new PostCode(parser.get(PropertyName.POSTCODE)));
      }
      if (parser.has(PropertyName.REGION)) {
        address.setRegion(new Region(parser.get(PropertyName.REGION)));
      }
      if (parser.has(PropertyName.STREET)) {
        address.setStreet(new Street(parser.get(PropertyName.STREET)));
      }
      contact.addStructuredPostalAddress(address);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasStructuredPostalAddresses()) {
        out.println("addresses:");
        for (StructuredPostalAddress address 
            : contact.getStructuredPostalAddresses()) {
          out.print("  ");
          if (address.hasRel()) {
            out.print(" rel:" + address.getRel());
          }
          if (address.hasLabel()) {
            out.print(" label:" + address.getLabel());
          }
          if (address.hasCity()) {
            out.print(" city:" + address.getCity().getValue());
          }
          if (address.hasCountry()) {
            out.print(" country:" + address.getCountry().getValue());
          }
          if (address.hasFormattedAddress()) {
            out.print(" formatted:" + address.getFormattedAddress().getValue());
          }
          if (address.hasNeighborhood()) {
            out.print(" neighborhood:" + address.getNeighborhood().getValue());
          }
          if (address.hasPobox()) {
            out.print(" pobox:" + address.getPobox().getValue());
          }
          if (address.hasPostcode()) {
            out.print(" postcode:" + address.getPostcode().getValue());
          }
          if (address.hasRegion()) {
            out.print(" region:" + address.getRegion().getValue());
          }
          if (address.hasStreet()) {
            out.print(" street:" + address.getStreet().getValue());
          }
          if (address.getPrimary()) {
            out.print(" (primary)");
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasStructuredPostalAddresses()) {
        List<StructuredPostalAddress> structuredPostalAddresses = 
            dest.getStructuredPostalAddresses();
        structuredPostalAddresses.clear();
        structuredPostalAddresses.addAll(src.getStructuredPostalAddresses());
      }
    }

    public String getUsage() {
      return "[rel:<rel>]"
          + "[,label:<label>]"
          + "[,primary:true|false]"
          + "[,city:<city>]"
          + "[,country:<country>]"
          + "[,formatted:<formattedAddress>]"
          + "[,neighborhood:<neighborhood>]"
          + "[,pobox:<poBox>]"
          + "[,postcode:<postCode>]"
          + "[,region:<region>]"
          + "[,street:<street>]";
    }
  },

  SUBJECT {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Subject subject = new Subject();
      subject.setValue(parser.get(PropertyName.VALUE));
      contact.setSubject(subject);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasSubject()) {
        out.println("subject:" + contact.getSubject().getValue());
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasSubject()) {
        dest.setSubject(src.getSubject());
      }
    }

    public String getUsage() {
      return "<subject>";
    }
  },
  
  USER_DEFINED_FIELD(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      UserDefinedField userDefinedField = new UserDefinedField();
      userDefinedField.setValue(parser.get(PropertyName.VALUE));
      userDefinedField.setKey(parser.get(PropertyName.KEY));
      contact.addUserDefinedField(userDefinedField);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasUserDefinedFields()) {
        out.println("user defined fields:");
        for (UserDefinedField field : contact.getUserDefinedFields()) {
          out.println("  " + field.getValue() + " key: " + field.getKey());
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasUserDefinedFields()) {
        List<UserDefinedField> userDefinedFields = dest.getUserDefinedFields();
        userDefinedFields.clear();
        userDefinedFields.addAll(src.getUserDefinedFields());
      }
    }

    public String getUsage() {
      return "<value>,key:<key>";
    }
  },
  
  WEBSITE(true) {
    public void parse(ContactEntry contact, ElementParser parser) { 
      Website website = new Website();
      website.setHref(parser.get(PropertyName.VALUE));
      if (parser.has(PropertyName.REL)) {
        website.setRel(
            Website.Rel.valueOf(parser.get(PropertyName.REL).toLowerCase()));
      }
      if (parser.has(PropertyName.LABEL)) {
        website.setLabel(parser.get(PropertyName.LABEL));
      }
      if (parser.has(PropertyName.PRIMARY)) {
        website.setPrimary(parser.is(PropertyName.PRIMARY));
      }
      contact.addWebsite(website);
    }
    
    public void print(PrintStream out, ContactEntry contact) { 
      if (contact.hasWebsites()) {
        out.println("websites:");
        for (Website website : contact.getWebsites()) {
          out.print("  " + website.getHref());
          if (website.hasRel()) {
            out.print(" ref:" + website.getRel().toString().toLowerCase());
          }
          if (website.hasLabel()) {
            out.print(" label:" + website.getLabel());
          }
          if (website.getPrimary()) {
            out.print(" (primary)");
          }
          out.println();
        }
      }
    }
    
    public void update(ContactEntry dest, ContactEntry src) {
      if (src.hasWebsites()) {
        List<Website> websites = dest.getWebsites();
        websites.clear();
        websites.addAll(src.getWebsites());
      }
    }

    public String getUsage() {
      return "<url>"
          + "[,rel:<rel>]"
          + "[label:<label>]"
          + "[,primary:true|false]";
    }
  };
  
  // Flag to indicate if the element can be repeated. 
  private final boolean repetable;
  
  // some regexp for parameter parsing/checking
  private final static Pattern REPEATED_ARG_PATTERN
      = Pattern.compile("^(\\D+)\\d*$");

  // Constructors.
  private ElementHelper(boolean repetable) { this.repetable = repetable; }
  private ElementHelper() { this(false); }
  
  /**
   * The default implementation just throws an UnsuportedOperationException, and
   * only those helpers override it, what are used in parsing groups elements.
   *
   * @param group  the group the parsed element should be added or set. 
   * @param parser the parser used for the parsing of the description.
   * 
   * @throws UnsupportedOperationException in case the specific element can not 
   *         be set on a ContactGroupEntry.
   * 
   * @see ElementParser
   */
  public void parseGroup(ContactGroupEntry group, ElementParser parser) {
    throw new UnsupportedOperationException("parseGroup not supported for" 
        + this.toString().toLowerCase() + " element");
  }
  
  private static ElementHelper find(String name) 
      throws IllegalArgumentException {
    Matcher m = REPEATED_ARG_PATTERN.matcher(name);
    if (!m.matches()) {
      throw new IllegalArgumentException("badly formated parameter: " + name);
    }
    try {
      return valueOf(m.group(1).toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("unknown parameter: " + name);
    }
  }
  
  /**
   * Builds a contact from the list of element descriptions.
   * It delegates the element specific parsing to the appropriate helper 
   * instances. The actual element type is matched by the name of the enum 
   * instance, so the element names specified in the parameters should (almost)
   * match the name of enum instances. The exceptions are those elements what
   * can be repeated, when the parameter format is "name<n>".* Due to this 
   * formating convention we cannot use directly the valueOf() facility of the 
   * enum.
   * 
   * @param contact    the contact to build.
   * @param parameters list of element descriptions.
   * 
   */
  public static void buildContact(ContactEntry contact, 
      List<String> parameters) {
    for (String string : parameters) {
      if (!string.startsWith("--")) {
        throw new IllegalArgumentException("unknown argument: " + string);
      }
      String param = string.substring(2);
      String params[] = param.split("=", 2);
      if (params.length != 2) {
        throw new IllegalArgumentException("badly formated argument: " 
            + string);
      }
      ElementHelper helper = find(params[0]);
      if (helper == null) {
        throw new IllegalArgumentException("unknown argument: " + string);
      }
      helper.parse(contact, new ElementParser(params[1]));
    }
  }
  
  /**
   * Builds a group from the list of element descriptions.
   * It delegates the element specific parsing to the appropriate helper 
   * instances. The actual element type is matched by the name of the enum 
   * instance, so the element names specified in the parameters should (almost)
   * match the name of enum instances. The exceptions are those elements what
   * can be repeated, when the parameter format is "name<n>".* Due to this 
   * formating convention we cannot use directly the valueOf() facility of the 
   * enum.
   * 
   * @param group      the group to build.
   * @param parameters list of element descriptions.
   * 
   */
  public static void buildGroup(ContactGroupEntry group, 
      List<String> parameters) {
    for (String string : parameters) {
      if (!string.startsWith("--")) {
        throw new IllegalArgumentException("unknown argument: " + string);
      }
      String param = string.substring(2);
      String params[] = param.split("=", 2);
      if (params.length != 2) {
        throw new IllegalArgumentException("badly formated argument: " 
            + string);
      }
      ElementHelper helper = find(params[0]);
      if (helper == null) {
        throw new IllegalArgumentException("unknown argument: " + string);
      }
      helper.parseGroup(group, new ElementParser(params[1]));
    }
  }
  
  /**
   * Updates the elements of a contact entry based on the elements of another
   * contact entry.
   * Those elements are replaced in the destination contact entry what are
   * exists in the source contact. Those elements not contained by the source
   * contact are left unchanged on the destination contact.
   * 
   * @param dest the destination contact to be updated.
   * @param src  the source contact
   */
  public static void updateContact(ContactEntry dest, ContactEntry src) {
    for (ElementHelper helper : values()) {
      helper.update(dest, src);
    }
  }
  
  /**
   * Prints the content of the contact in a human readable form.
   * 
   * @param out     the stream to print to.
   * @param contact the contact to be printed out.
   */
  public static void printContact(PrintStream out, ContactEntry contact) {
    for (ElementHelper helper : values()) {
      helper.print(out, contact);
    }
  }
  
  /**
   * Gives the usage help text of all elements.
   *
   * @return the usage help text for all elements.
   */
  public static String getUsageString() {
    StringBuffer buffer = new StringBuffer();
    for (ElementHelper helper : values()) {
      buffer.append("             --" + helper.toString().toLowerCase());
      if (helper.repetable) {
        buffer.append("<n>");
      }
      buffer.append("=" + helper.getUsage() + "\n");
    }
    buffer.append(
        "Notes! <n> is a unique number for the field - several fields\n"
        + " of the same type can be present (example: im1, im2, im3).\n"
        + " Available rels and protocols can be looked up in the \n"
        + " feed documentation.\n");
    return buffer.toString();
  }
}
