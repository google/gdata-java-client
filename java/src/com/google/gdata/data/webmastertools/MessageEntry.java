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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.extensions.EntryLink;

/**
 * This class represents a message entry. It contains information of a message
 * of the Notification Console. The message is identified by the MessageId
 * (accessed through getId() and setId(), which are implemented in BaseEntry)
 * and its fields are: subject, date, read / unread, language and message body.
 *
 * Example (common Atom nodes are omitted):
 * <pre class="code">
 *   <entry>
 *     <id>1234-12345678/</id>
 *     <wt:language>en</wt:language>
 *     <wt:subject>Crawl rate change request for
 *       http://www.example.com/</wt:subject>
 *     <wt:date>2007-1-1T18:30:00.000Z</wt:date>
 *     <wt:read>false</wt:read>
 *     <wt:body>We've received a request from a site owner to change the rate
 *       at which Googlebot crawls this site: http://www.example.com/.
 *       Old crawl rate: Slower, New crawl rate: Normal, This new crawl rate
 *       will stay in effect for 90 days.</wt:body>
 *   </entry>
 * </pre>
 *
 * 
 */
public class MessageEntry extends BaseEntry<MessageEntry> {

  /**
   * Names for the XML nodes defined in the entry.
   */
  private static final String LANGUAGE = "language";
  private static final String SUBJECT = "subject";
  private static final String DATE = "date";
  private static final String READ = "read";
  private static final String BODY = "body";

  /**
   * Kind category used to label this entry.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_MESSAGE);

  /**
   * Constructs a new MessageEntry.
   */
  public MessageEntry() {
    super();
    this.getCategories().add(CATEGORY);
  }

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a MessageEntry. It specifies which nodes are expected.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Include extension, link to self
    ExtensionDescription desc = EntryLink.getDefaultDescription();
    desc.setRepeatable(true);
    extProfile.declare(MessageEntry.class, desc);

    // Include extensions for the data
    extProfile.declare(MessageEntry.class,
        ExtensionDescription.getDefaultDescription(LanguageExtension.class));
    extProfile.declare(MessageEntry.class,
        ExtensionDescription.getDefaultDescription(SubjectExtension.class));
    extProfile.declare(MessageEntry.class,
        ExtensionDescription.getDefaultDescription(DateExtension.class));
    extProfile.declare(MessageEntry.class,
        ExtensionDescription.getDefaultDescription(ReadExtension.class));
    extProfile.declare(MessageEntry.class,
        ExtensionDescription.getDefaultDescription(BodyExtension.class));
  }

  /**
   * Changes the Language field of the message.
   *
   * @param language is a new Language field for the message.
   */
  public void setLanguage(String language) {
    LanguageExtension languageExtension = getExtension(LanguageExtension.class);
    if (languageExtension == null) {
      languageExtension = new LanguageExtension();
      setExtension(languageExtension);
    }

    languageExtension.setValue(language);
  }

  /**
   * Returns the Language field of the message.
   *
   * @return the Language field of the message.
   */
  public String getLanguage() {
    LanguageExtension languageExtension = getExtension(LanguageExtension.class);
    if (languageExtension == null) {
      return null;
    }

    return languageExtension.getValue();
  }

  /**
   * Changes the Subject field of the message.
   *
   * @param subject is a new Subject field for the message.
   */
  public void setSubject(String subject) {
    SubjectExtension subjectExtension = getExtension(SubjectExtension.class);
    if (subjectExtension == null) {
      subjectExtension = new SubjectExtension();
      setExtension(subjectExtension);
    }

    subjectExtension.setValue(subject);
  }

  /**
   * Returns the Subject field of the message.
   *
   * @return the Subject field of the message.
   */
  public String getSubject() {
    SubjectExtension subjectExtension = getExtension(SubjectExtension.class);
    if (subjectExtension == null) {
      return null;
    }

    return subjectExtension.getValue();
  }

  /**
   * Changes the Date field of the message.
   *
   * @param date is a new {@link DateTime} for the message.
   */
  public void setDate(DateTime date) {
    DateExtension dateExtension = getExtension(DateExtension.class);
    if (dateExtension == null) {
      dateExtension = new DateExtension();
      setExtension(dateExtension);
    }

    dateExtension.setDateTime(date);
  }

  /**
   * Returns the Date field of the message.
   *
   * @return the {@link DateTime} Date field of the message.
   */
  public DateTime getDate() {
    DateExtension dateExtension = getExtension(DateExtension.class);
    if (dateExtension == null) {
      return null;
    }

    return dateExtension.getDateTime();
  }

  /**
   * Changes the Read status of the message.
   *
   * @param read is the new Read status of the message.
   */
  public void setRead(boolean read) {
    ReadExtension readExtension = getExtension(ReadExtension.class);
    if (readExtension == null) {
      readExtension = new ReadExtension();
      setExtension(readExtension);
    }

    readExtension.setBooleanValue(read);
  }

  /**
   * Get the Read status of the message.
   *
   * @return {@code true} if the message is marked as read and {@code false}
   * otherwise.
   */
  public boolean getRead() {
    ReadExtension readExtension = getExtension(ReadExtension.class);
    return ((readExtension != null) && readExtension.getBooleanValue());
  }

  /**
   * Changes the body of the message.
   *
   * @param body is a new body for the message.
   */
  public void setBody(String body) {
    BodyExtension bodyExtension = getExtension(BodyExtension.class);
    if (bodyExtension == null) {
      bodyExtension = new BodyExtension();
      setExtension(bodyExtension);
    }

    bodyExtension.setValue(body);
  }

  /**
   * Returns the body of the message.
   *
   * @return the body of the message.
   */
  public String getBody() {
    BodyExtension bodyExtension = getExtension(BodyExtension.class);
    if (bodyExtension == null) {
      return null;
    }

    return bodyExtension.getValue();
  }

  /**
   * Value construct to represent <language> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = LANGUAGE)
  public static class LanguageExtension extends ValueConstruct {
    public LanguageExtension() {
      super(LANGUAGE);
    }
  }

  /**
   * Value construct to represent <subject> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = SUBJECT)
  public static class SubjectExtension extends ValueConstruct {
    public SubjectExtension() {
      super(SUBJECT);
    }
  }

  /**
   * DateTimeValueConstruct to represent <date> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = DATE)
  public static class DateExtension extends DateTimeValueConstruct {
    public DateExtension() {
      super(DATE);
    }
  }

  /**
   * BoolValueConstruct to represent <read> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = READ)
  public static class ReadExtension extends BoolValueConstruct {
    public ReadExtension() {
      super(READ);
    }
  }

  /**
   * Value construct to represent <body> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = BODY)
  public static class BodyExtension extends ValueConstruct {
    public BodyExtension() {
      super(BODY);
    }
  }
}
