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

package com.google.gdata.data.appsforyourdomain.migration;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Used by a MailItemFeedProvider to represent a single MailItemEntry in a list
 * of MailItems. MailItems are representations of email messages from a domain's
 * legacy systems destined for Google.
 * 
 * 
 */
@Kind.Term(MailItemEntry.MAILITEM_KIND)
public class MailItemEntry extends MediaEntry<MailItemEntry> {
  
  /**
   * Kind term value for MailItem category labels.
   */
  public static final String MAILITEM_KIND
      = com.google.gdata.data.appsforyourdomain.Namespaces.APPS_PREFIX
      + "mailItem";

  /**
   * Kind category used to label feeds or entries that have MailItem
   * extension data.
   */
  public static final Category MAILITEM_CATEGORY =
      new Category(Namespaces.gKind, MAILITEM_KIND);   
  
  /**
   * Constructs a new MailItemEntry instance
   */
  public MailItemEntry() {
    super();
    getCategories().add(MAILITEM_CATEGORY);
  }
  
  /**
   * Constructs a new MailItemEntry instance by doing a shallow copy of data
   * from an existing BaseEntry instance.
   * 
   * @param sourceEntry the BaseEntry object to copy from
   */
  public MailItemEntry(BaseEntry<MailItemEntry> sourceEntry) {
    super(sourceEntry);
    getCategories().add(MAILITEM_CATEGORY);
  }

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a MailItemEntry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    
    super.declareExtensions(extProfile);
    extProfile.declare(MailItemEntry.class, Email.getDefaultDescription());
    extProfile.declare(MailItemEntry.class, Label.getDefaultDescription());
    extProfile.declare(MailItemEntry.class,
        MailItemProperty.getDefaultDescription());
    extProfile.declare(MailItemEntry.class, Rfc822Msg.getDefaultDescription());
  }
  
  /**
   * @return the rfc822 compliant encoding of the mail message
   */
  public Rfc822Msg getRfc822Msg() {
    return getExtension(Rfc822Msg.class);
  }  
  
  /**
   * Sets the rfc822Msg for this MailItem
   */
  public void setRfc822Msg(Rfc822Msg rfc822Msg) {
    setExtension(rfc822Msg);
  }
  
  /**
   * @return the mail settings (read status, starred status, etc.) of the mail
   * message
   */
  public List<MailItemProperty> getMailProperties() {
    return getRepeatingExtension(MailItemProperty.class);
  }
  
  /**
   * Sets mail settings (read status, starred status, etc.) for this message.
   */
  public void addMailProperty(MailItemProperty mailProperty) {
    addRepeatingExtension(mailProperty);
  }
    
  /**
   * @return a list of Labels to be applied to this message upon insertion in 
   *         GMail
   */
  public List<Label> getLabels() {
    return getRepeatingExtension(Label.class);
  }
  
  /**
   * Adds a label to this MailItem
   * 
   * @param label the label to add to the message when it is imported into GMail
   */
  public void addLabel(Label label) {
    addRepeatingExtension(label);
  }

  /**
   * This is relevant only for multi recipient feed. The single recipient
   * case will not contain the email address of recipient. 
   *
   * @return a list of recipients' email addresses for this mail item
   */
  public List<Email> getRecipients() {
    return getRepeatingExtension(Email.class);
  }

  /**
   * Adds a recipient email to the Mail Item.
   *
   * @param recipient the recipient email to be attached with the message
   */
  public void addRecipient(Email recipient) {
    addRepeatingExtension(recipient);
  }
}
