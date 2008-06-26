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

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.appsforyourdomain.Namespaces;

/**
 * Google Apps name space element: {@code <apps:mailItemProperty>}. Used to
 * model an enumerable property about a mail item, such as whether it is to be
 * marked as starred, read, trash, etc. when migrated into Gmail.
 * 
 * 
 */
public class MailItemProperty extends ValueConstruct {

  /**
   * Defined attributes for mail items.
   */
  private enum MailItemAttribute {
    IS_DRAFT,
    IS_INBOX,
    IS_STARRED,
    IS_SENT,
    IS_TRASH,
    IS_UNREAD
  }

  
  /**
   * Property indicating that a mail item should be marked as a draft
   * when inserted into Gmail.
   */
  public static final MailItemProperty DRAFT = new MailItemProperty(
      MailItemAttribute.IS_DRAFT);
  
  /**
   * Property indicating that a mail item should appear in the inbox when
   * inserted into Gmail.  By default, a message will not be placed in the
   * inbox if it has any labels.
   */
  public static final MailItemProperty INBOX = new MailItemProperty(
      MailItemAttribute.IS_INBOX);
  
  /**
   * Property indicating that a mail item should appear in the list of sent
   * messages when inserted into Gmail.
   */
  public static final MailItemProperty SENT = new MailItemProperty(
      MailItemAttribute.IS_SENT);
  
  /**
   * Property indicating that a mail item should be moved to the trash when
   * inserted into Gmail.
   */
  public static final MailItemProperty TRASH = new MailItemProperty(
      MailItemAttribute.IS_TRASH);
  
  /**
   * Property indicating that a mail item should be marked as starred when
   * inserted into Gmail.  By default, migrated mail items are unstarred.
   */
  public static final MailItemProperty STARRED = new MailItemProperty(
      MailItemAttribute.IS_STARRED);
  
  /**
   * Property indicating that a mail item should be marked as unread when
   * inserted into Gmail.  By default, mail items are marked as read.
   */
  public static final MailItemProperty UNREAD = new MailItemProperty(
      MailItemAttribute.IS_UNREAD);
  
  /**
   * the name of the GData element
   */  
  private static final String EXTENSION_LOCAL_NAME = "mailItemProperty";
  private static final String ATTRIBUTE_VALUE = "value";
  
  /**
   * the extension description is shared by all instances of this class
   */  
  private static ExtensionDescription EXTENSION_DESC = 
      new ExtensionDescription();
  static {
    EXTENSION_DESC.setExtensionClass(MailItemProperty.class);
    EXTENSION_DESC.setNamespace(Namespaces.APPS_NAMESPACE);
    EXTENSION_DESC.setLocalName(EXTENSION_LOCAL_NAME);
    EXTENSION_DESC.setRepeatable(true);
  }
  
  /**
   * Constructs a {@code MailItemProperty} with no value.
   */
  public MailItemProperty() {    
    super(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, ATTRIBUTE_VALUE);
  }

  /**
   * Constructs a {@code MailItemProperty} with the specified value.
   * 
   * @param value the {@link MailItemAttribute} value to use
   */
  public MailItemProperty(MailItemAttribute value) { 
    super(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, ATTRIBUTE_VALUE,
        value.toString());
  }
  
  /**
   * @return the description of this extension
   */
  public static ExtensionDescription getDefaultDescription() {
    return EXTENSION_DESC;
  }
  
}
