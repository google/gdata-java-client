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
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.Person;

/**
 * Defines an entry in a feed of revisions of a document.
 *
 * 
 */
public class RevisionEntry extends BaseEntry<RevisionEntry> {

  public static final String PUBLISH_NAMESPACE =
      DocsNamespace.DOCS_PREFIX + "publish";

  /**
   * Label for category.
   */
  public static final String LABEL = "revision";

  /**
   * Kind category term used to label revision entries.
   */
  public static final String KIND = DocsNamespace.DOCS_PREFIX + LABEL;

  /**
   * Category used to label revision entries.
   */
  public static final Category CATEGORY =
    new Category(com.google.gdata.util.Namespaces.gKind, KIND, LABEL);

  public RevisionEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  public RevisionEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(RevisionEntry.class, Md5Checksum.class);
    extProfile.declare(RevisionEntry.class, Publish.class);
    extProfile.declare(RevisionEntry.class, PublishAuto.class);
    extProfile.declare(RevisionEntry.class, PublishOutsideDomain.class);
  }

  /**
   * Returns the MD5 checksum calculated for the document.
   *
   * @return the MD5 checksum
   */
  public String getMd5Checksum() {
    Md5Checksum md5Checksum = getExtension(Md5Checksum.class);
    return md5Checksum == null ? null : md5Checksum.getValue();
  }

  /**
   * Set the MD5 checksum calculated for the document.
   *
   * @param md5Checksum the MD5 checksum
   */
  public void setMd5Checksum(String md5Checksum) {
    if (md5Checksum == null) {
      removeExtension(Md5Checksum.class);
    } else {
      setExtension(new Md5Checksum(md5Checksum));
    }
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

  /**
   * Returns the publically accessible link for the published revision.
   */
  public Link getPublishLink() {
    return getLink(PUBLISH_NAMESPACE, Link.Type.HTML);
  }

  /**
   * Returns whether the revision is published.
   *
   * @return whether revision is published
   */
  public Boolean isPublish() {
    Publish publish = getExtension(Publish.class);
    return publish == null ? null : publish.getValue();
  }

  /**
   * Sets whether the revision is published.
   *
   * @param publish true if revision is published
   */
  public void setPublish(Boolean publish) {
    if (publish == null) {
      removeExtension(Publish.class);
    } else {
      setExtension(new Publish(publish));
    }
  }

  /**
   * Returns whether changes to the document are automatically re-published.
   *
   * @return whether auto re-published
   */
  public Boolean isPublishAuto() {
    PublishAuto publishAuto = getExtension(PublishAuto.class);
    return publishAuto == null ? null : publishAuto.getValue();
  }

  /**
   * Sets whether changes to the document are automatically re-published.
   *
   * @param publishAuto true if auto re-published
   */
  public void setPublishAuto(Boolean publishAuto) {
    if (publishAuto == null) {
      removeExtension(PublishAuto.class);
    } else {
      setExtension(new PublishAuto(publishAuto));
    }
  }

  /**
   * Sets whether the document is published outside of its domain.
   *
   * @return whether outside domain
   */
  public Boolean isPublishOutsideDomain() {
    PublishOutsideDomain publishOutsideDomain = getExtension(PublishOutsideDomain.class);
    return publishOutsideDomain == null ? null : publishOutsideDomain.getValue();
  }

  /**
   * Sets whether the document is published outside of its domain.
   *
   * @param publishOutsideDomain true if outside domain
   */
  public void setPublishOutsideDomain(Boolean publishOutsideDomain) {
    if (publishOutsideDomain == null) {
      removeExtension(PublishOutsideDomain.class);
    } else {
      setExtension(new PublishOutsideDomain(publishOutsideDomain));
    }
  }
}
