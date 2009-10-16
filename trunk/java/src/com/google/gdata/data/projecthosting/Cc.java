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


package com.google.gdata.data.projecthosting;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;

/**
 * Entry in the issue cc list.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ProjectHostingNamespace.ISSUES_ALIAS,
    nsUri = ProjectHostingNamespace.ISSUES,
    localName = Cc.XML_NAME)
public class Cc extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "cc";

  /**
   * Default mutable constructor.
   */
  public Cc() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(Cc.class)) {
      return;
    }
    extProfile.declare(Cc.class, Uri.class);
    extProfile.declare(Cc.class, Username.getDefaultDescription(true, false));
  }

  /**
   * Returns the uri.
   *
   * @return uri
   */
  public Uri getUri() {
    return getExtension(Uri.class);
  }

  /**
   * Sets the uri.
   *
   * @param uri uri or <code>null</code> to reset
   */
  public void setUri(Uri uri) {
    if (uri == null) {
      removeExtension(Uri.class);
    } else {
      setExtension(uri);
    }
  }

  /**
   * Returns whether it has the uri.
   *
   * @return whether it has the uri
   */
  public boolean hasUri() {
    return hasExtension(Uri.class);
  }

  /**
   * Returns the username.
   *
   * @return username
   */
  public Username getUsername() {
    return getExtension(Username.class);
  }

  /**
   * Sets the username.
   *
   * @param username username or <code>null</code> to reset
   */
  public void setUsername(Username username) {
    if (username == null) {
      removeExtension(Username.class);
    } else {
      setExtension(username);
    }
  }

  /**
   * Returns whether it has the username.
   *
   * @return whether it has the username
   */
  public boolean hasUsername() {
    return hasExtension(Username.class);
  }

  @Override
  protected void validate() {
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(Cc.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{Cc}";
  }

}

