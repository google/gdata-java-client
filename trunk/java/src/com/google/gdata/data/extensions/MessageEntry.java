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


package com.google.gdata.data.extensions;

import com.google.gdata.data.Category;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Extension class for manipulating entries of the Message kind.
 *
 * 
 * 
 */
@Kind.Term(MessageEntry.MESSAGE_KIND)
public class MessageEntry extends BaseEntry<MessageEntry> {

  /**
   * Kind term value for Event category labels.
   */
  public static final String MESSAGE_KIND = Namespaces.gPrefix + "message";

  /** Category used to label any entry that contains Message extensions */
  public static final Category MESSAGE_CATEGORY =
    new Category(Namespaces.gKind, MESSAGE_KIND);

  /** Category used to label starred message feeds and entries. */
  public static final Category STARRED_CATEGORY =
    new Category(Namespaces.g, Namespaces.gPrefix + "message.starred");
  /** Category used to label unread message feeds and entries. */
  public static final Category UNREAD_CATEGORY =
    new Category(Namespaces.g, Namespaces.gPrefix + "message.unread");
  /** Category used to label chat message feeds and entries. */
  public static final Category CHAT_CATEGORY =
    new Category(Namespaces.g, Namespaces.gPrefix + "message.chat");
  /** Category used to label spam message feeds and entries. */
  public static final Category SPAM_CATEGORY =
    new Category(Namespaces.g, Namespaces.gPrefix + "message.spam");
  /** Category used to label sent message feeds and entries. */
  public static final Category SENT_CATEGORY =
    new Category(Namespaces.g, Namespaces.gPrefix + "message.sent");
  /** Category used to label inbox message feeds and entries. */
  public static final Category INBOX_CATEGORY =
    new Category(Namespaces.g, Namespaces.gPrefix + "message.inbox");

  /**
   * Constructs a new MessageEntry with the appropriate kind category
   * to indicate that it is a message.
   */
  public MessageEntry() {
    super();
    getCategories().add(MESSAGE_CATEGORY);
  }

  /**
   * Constructs a new MessaEntry instance by doing a shallow copy of data
   * from an existing BaseEntry instance.
   */
  public MessageEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    getCategories().add(MESSAGE_CATEGORY);
  }

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by an MessageEntry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(MessageEntry.class, Rating.getDefaultDescription(false));
    extProfile.declare(MessageEntry.class, When.getDefaultDescription(false));
    extProfile.declare(MessageEntry.class, GeoPt.getDefaultDescription(false));
    extProfile.declare(MessageEntry.class, Who.getDefaultDescription());
  }

  public Rating getRating() {
    return getExtension(Rating.class);
  }

  public void setRating(Rating rating) {
    setExtension(rating);
  }

  public When getTime() {
    return getExtension(When.class);
  }

  public void setTime(When when) {
    setExtension(when);
  }

  public GeoPt getGeoPt() {
    return getExtension(GeoPt.class);
  }

  public void setGeoPt(GeoPt geoPt) {
    setExtension(geoPt);
  }

  public List<Who> getWhoList() {
    return getRepeatingExtension(Who.class);
  }
}
