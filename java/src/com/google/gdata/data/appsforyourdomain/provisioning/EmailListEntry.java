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


package com.google.gdata.data.appsforyourdomain.provisioning;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.appsforyourdomain.EmailList;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.util.Namespaces;

/**
 * 
 * 
 */
@Kind.Term(EmailListEntry.EMAIL_LIST_KIND)
public class EmailListEntry extends BaseEntry<EmailListEntry> {

  /**
   * Kind term value for EmailList category labels.
   */
  public static final String EMAIL_LIST_KIND
      = com.google.gdata.data.appsforyourdomain.Namespaces.APPS_PREFIX
      + "emailList";

  /**
   * Kind category used to label feeds or entries that have EmailList
   * extension data.
   */
  public static final Category EMAIL_LIST_CATEGORY =
      new Category(Namespaces.gKind, EMAIL_LIST_KIND);

  /**
   * Constructs a new empty EmailListEntry with the appropriate kind category
   * to indicate that it is an email list.
   */
  public EmailListEntry() {
    super();
    getCategories().add(EMAIL_LIST_CATEGORY);
  }

  /**
   * Constructs a new EmailListEntry by doing a shallow copy of data from an
   * existing BaseEntry intance.
   */
  public EmailListEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(EMAIL_LIST_CATEGORY);
  }

  /**
   * Declares an Extension.
   */
  @Override
  public void declareExtensions(ExtensionProfile extensionProfile) {

    // An EmailList extension is required for each entry
    ExtensionDescription desc = EmailList.getDefaultDescription();
    desc.setRequired(true);
    extensionProfile.declare(EmailListEntry.class, desc);

    // FeedLink extension is not repeatable by default.
    desc = FeedLink.getDefaultDescription();
    desc.setRepeatable(true);
    extensionProfile.declare(EmailListEntry.class, desc);

    // Declare our "apps" namespace
    extensionProfile.declareAdditionalNamespace(
        com.google.gdata.data.appsforyourdomain.Namespaces.APPS_NAMESPACE);
  }
  
  public EmailList getEmailList() {
    return getExtension(EmailList.class);
  }
}
