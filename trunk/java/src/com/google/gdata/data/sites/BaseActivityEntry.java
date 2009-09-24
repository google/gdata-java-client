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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.dublincore.Publisher;

/**
 * An entry representing a user action in a site.
 *
 * @param <E> concrete entry type
 * 
 */
public abstract class BaseActivityEntry<E extends BaseActivityEntry<E>> extends
    BaseEntry<E> {

  /**
   * Default mutable constructor.
   */
  public BaseActivityEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public BaseActivityEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(BaseActivityEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(BaseActivityEntry.class,
        SitesLink.getDefaultDescription(false, true));
    extProfile.declare(BaseActivityEntry.class, Publisher.class);
  }

  /**
   * Returns the The authenticated user that performe the activity if different
   * than the author.
   *
   * @return The authenticated user that performe the activity if different than
   *     the author
   */
  public Publisher getPublisher() {
    return getExtension(Publisher.class);
  }

  /**
   * Sets the The authenticated user that performe the activity if different
   * than the author.
   *
   * @param publisher The authenticated user that performe the activity if
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
   * Returns whether it has the The authenticated user that performe the
   * activity if different than the author.
   *
   * @return whether it has the The authenticated user that performe the
   *     activity if different than the author
   */
  public boolean hasPublisher() {
    return hasExtension(Publisher.class);
  }

  /**
   * Returns the current sites link.
   *
   * @return Current sites link or {@code null} for none.
   */
  public Link getCurrentLink() {
    return getLink(SitesLink.Rel.CURRENT, Link.Type.ATOM);
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
    return "{BaseActivityEntry " + super.toString() + "}";
  }

}

