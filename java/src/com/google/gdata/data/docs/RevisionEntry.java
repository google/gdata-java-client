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


package com.google.gdata.data.docs;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Person;

/**
 * Defines an entry in a feed of revisions of a document.
 *
 * 
 */
public class RevisionEntry extends BaseEntry<RevisionEntry> {

  public static final String PUBLISH_NAMESPACE =
      DocsNamespace.DOCS_PREFIX + "publish";

  public RevisionEntry() {
    super();
  }

  public RevisionEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  /**
   * Revisions have only one author, the user who modified the document to
   * create that revision. These are convenience methods for setting and
   * getting a sole author.
   *
   * @param modifyingUser the user who modified the document/created the revision
   */
  public void setModifyingUser(Person modifyingUser) {
    getAuthors().clear();
    if (modifyingUser != null) {
      getAuthors().add(modifyingUser);
    }
  }

  /**
   * @return the user who modified the document/created the revision
   */
  public Person getModifyingUser() {
    if (getAuthors().size() > 0) {
      return getAuthors().get(0);
    }
    return null;
  }

}
