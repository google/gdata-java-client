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


package com.google.gdata.data.sites;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.dublincore.Publisher;
import com.google.gdata.data.extensions.Deleted;
import com.google.gdata.data.media.MediaEntry;

/**
 * An entry representing a single content item.
 *
 * @param <E> concrete entry type
 * 
 */
public abstract class BaseContentEntry<E extends BaseContentEntry<E>> extends
    MediaEntry<E> {

  /**
   * Default mutable constructor.
   */
  public BaseContentEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public BaseContentEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(BaseContentEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(BaseContentEntry.class,
        new ExtensionDescription(Deleted.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "deleted", false, false, false));
    extProfile.declare(BaseContentEntry.class,
        SitesLink.getDefaultDescription(true, true));
    extProfile.declare(BaseContentEntry.class, Publisher.class);
    extProfile.declare(BaseContentEntry.class, Revision.class);
  }

  /**
   * Returns the If present, indicates that an item has been deleted.  Deleted
   * entries are not shown by default.
   *
   * @return If present, indicates that an item has been deleted.  Deleted
   *     entries are not shown by default
   */
  public Deleted getDeleted() {
    return getExtension(Deleted.class);
  }

  /**
   * Sets the If present, indicates that an item has been deleted.  Deleted
   * entries are not shown by default.
   *
   * @param deleted If present, indicates that an item has been deleted.
   *     Deleted entries are not shown by default or <code>null</code> to reset
   */
  public void setDeleted(Deleted deleted) {
    if (deleted == null) {
      removeExtension(Deleted.class);
    } else {
      setExtension(deleted);
    }
  }

  /**
   * Returns whether it has the If present, indicates that an item has been
   * deleted.  Deleted entries are not shown by default.
   *
   * @return whether it has the If present, indicates that an item has been
   *     deleted.  Deleted entries are not shown by default
   */
  public boolean hasDeleted() {
    return hasExtension(Deleted.class);
  }

  /**
   * Returns the The authenticated user that uploaded the document if different
   * than the author.
   *
   * @return The authenticated user that uploaded the document if different than
   *     the author
   */
  public Publisher getPublisher() {
    return getExtension(Publisher.class);
  }

  /**
   * Sets the The authenticated user that uploaded the document if different
   * than the author.
   *
   * @param publisher The authenticated user that uploaded the document if
   *     different than the author or <code>null</code> to reset
   */
  public void setPublisher(Publisher publisher) {
    if (publisher == null) {
      removeExtension(Publisher.class);
    } else {
      setExtension(publisher);
    }
  }

  /**
   * Returns whether it has the The authenticated user that uploaded the
   * document if different than the author.
   *
   * @return whether it has the The authenticated user that uploaded the
   *     document if different than the author
   */
  public boolean hasPublisher() {
    return hasExtension(Publisher.class);
  }

  /**
   * Returns the revision.
   *
   * @return revision
   */
  public Revision getRevision() {
    return getExtension(Revision.class);
  }

  /**
   * Sets the revision.
   *
   * @param revision revision or <code>null</code> to reset
   */
  public void setRevision(Revision revision) {
    if (revision == null) {
      removeExtension(Revision.class);
    } else {
      setExtension(revision);
    }
  }

  /**
   * Returns whether it has the revision.
   *
   * @return whether it has the revision
   */
  public boolean hasRevision() {
    return hasExtension(Revision.class);
  }

  /**
   * Returns the link that provides the URI that can be used to post new entries
   * to the feed.
   *
   * @return Link that provides the URI that can be used to post new entries to
   *     the feed or {@code null} for none.
   */
  public Link getEntryPostLink() {
    return getLink(Link.Rel.ENTRY_POST, Link.Type.ATOM);
  }

  /**
   * Returns the revision sites link.
   *
   * @return Revision sites link or {@code null} for none.
   */
  public Link getRevisionLink() {
    return getLink(SitesLink.Rel.REVISION, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{BaseContentEntry " + super.toString() + "}";
  }

}

