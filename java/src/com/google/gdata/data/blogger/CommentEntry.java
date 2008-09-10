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


package com.google.gdata.data.blogger;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.threading.InReplyTo;

/**
 * Describes an entry for a comment on a post.
 *
 * 
 */
public class CommentEntry extends BaseEntry<CommentEntry> {

  /**
   * Default mutable constructor.
   */
  public CommentEntry() {
    super();
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public CommentEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(CommentEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(CommentEntry.class, InReplyTo.class);
  }

  /**
   * Returns the in reply to.
   *
   * @return in reply to
   */
  public InReplyTo getInReplyTo() {
    return getExtension(InReplyTo.class);
  }

  /**
   * Sets the in reply to.
   *
   * @param inReplyTo in reply to or <code>null</code> to reset
   */
  public void setInReplyTo(InReplyTo inReplyTo) {
    if (inReplyTo == null) {
      removeExtension(InReplyTo.class);
    } else {
      setExtension(inReplyTo);
    }
  }

  /**
   * Returns whether it has the in reply to.
   *
   * @return whether it has the in reply to
   */
  public boolean hasInReplyTo() {
    return hasExtension(InReplyTo.class);
  }

  /**
   * Returns the link that provides the URI of the web content.
   *
   * @return Link that provides the URI of the web content or {@code null} for
   *     none.
   */
  public Link getRepliesLink() {
    return getLink(BloggerLink.Rel.REPLIES, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{CommentEntry " + super.toString() + "}";
  }

}
