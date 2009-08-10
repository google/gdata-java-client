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


package com.google.gdata.data.photos;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Person;
import com.google.gdata.util.Namespaces;

/**
 * Comment author, contains commentor name and image.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = "atom",
    nsUri = Namespaces.atom,
    localName = CommentAuthor.XML_NAME)
public class CommentAuthor extends Person {

  /** XML element name */
  static final String XML_NAME = "author";

  /**
   * Default mutable constructor.
   */
  public CommentAuthor() {
    super();
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(CommentAuthor.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(CommentAuthor.class, GphotoNickname.class);
    extProfile.declare(CommentAuthor.class, GphotoThumbnail.class);
    extProfile.declare(CommentAuthor.class, GphotoUsername.class);
  }

  /**
   * Returns the gphoto nickname.
   *
   * @return gphoto nickname
   */
  public GphotoNickname getNickname() {
    return getExtension(GphotoNickname.class);
  }

  /**
   * Sets the gphoto nickname.
   *
   * @param nickname gphoto nickname or <code>null</code> to reset
   */
  public void setNickname(GphotoNickname nickname) {
    if (nickname == null) {
      removeExtension(GphotoNickname.class);
    } else {
      setExtension(nickname);
    }
  }

  /**
   * Returns whether it has the gphoto nickname.
   *
   * @return whether it has the gphoto nickname
   */
  public boolean hasNickname() {
    return hasExtension(GphotoNickname.class);
  }

  /**
   * Returns the gphoto thumbnail.
   *
   * @return gphoto thumbnail
   */
  public GphotoThumbnail getThumbnail() {
    return getExtension(GphotoThumbnail.class);
  }

  /**
   * Sets the gphoto thumbnail.
   *
   * @param thumbnail gphoto thumbnail or <code>null</code> to reset
   */
  public void setThumbnail(GphotoThumbnail thumbnail) {
    if (thumbnail == null) {
      removeExtension(GphotoThumbnail.class);
    } else {
      setExtension(thumbnail);
    }
  }

  /**
   * Returns whether it has the gphoto thumbnail.
   *
   * @return whether it has the gphoto thumbnail
   */
  public boolean hasThumbnail() {
    return hasExtension(GphotoThumbnail.class);
  }

  /**
   * Returns the username of the user who made the comment.
   *
   * @return username of the user who made the comment
   */
  public GphotoUsername getUsername() {
    return getExtension(GphotoUsername.class);
  }

  /**
   * Sets the username of the user who made the comment.
   *
   * @param username username of the user who made the comment or
   *     <code>null</code> to reset
   */
  public void setUsername(GphotoUsername username) {
    if (username == null) {
      removeExtension(GphotoUsername.class);
    } else {
      setExtension(username);
    }
  }

  /**
   * Returns whether it has the username of the user who made the comment.
   *
   * @return whether it has the username of the user who made the comment
   */
  public boolean hasUsername() {
    return hasExtension(GphotoUsername.class);
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
        ExtensionDescription.getDefaultDescription(CommentAuthor.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  public String toString() {
    return "{CommentAuthor " + super.toString() + "}";
  }

}
