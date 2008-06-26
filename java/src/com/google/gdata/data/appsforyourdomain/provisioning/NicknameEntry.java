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
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Nickname;
import com.google.gdata.util.Namespaces;

/**
 * 
 * 
 */
@Kind.Term(NicknameEntry.NICKNAME_KIND)
public class NicknameEntry extends BaseEntry<NicknameEntry> {

  /**
   * Kind term value for Nickname category labels.
   */
  public static final String NICKNAME_KIND
      = com.google.gdata.data.appsforyourdomain.Namespaces.APPS_PREFIX
      + "nickname";

  /**
   * Kind category used to label feeds or entries that have Nickname
   * extension data.
   */
  public static final Category NICKNAME_CATEGORY =
      new Category(Namespaces.gKind, NICKNAME_KIND);

  /**
   * Constructs a new empty NicknameEntry with the appropriate kind category
   * to indicate that it is an nickname.
   */
  public NicknameEntry() {
    super();
    getCategories().add(NICKNAME_CATEGORY);
  }

  /**
   * Constructs a new NicknameEntry by doing a shallow copy of data from an
   * existing BaseEntry intance.
   */
  public NicknameEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
    getCategories().add(NICKNAME_CATEGORY);
  }

  @Override
  public void declareExtensions(ExtensionProfile extensionProfile) {

    // A Nickname extension is required for each entry
    ExtensionDescription description = Nickname.getDefaultDescription();
    description.setRequired(true);
    extensionProfile.declare(NicknameEntry.class, description);

    // A Login extension is required for each entry
    description = Login.getDefaultDescription();
    description.setRequired(true);
    extensionProfile.declare(NicknameEntry.class, description);

    // Declare our "apps" namespace
    extensionProfile.declareAdditionalNamespace(
        com.google.gdata.data.appsforyourdomain.Namespaces.APPS_NAMESPACE);
  }

  public Nickname getNickname() {
    return getExtension(Nickname.class);
  }
  public Login getLogin() {
    return getExtension(Login.class);
  }

}
