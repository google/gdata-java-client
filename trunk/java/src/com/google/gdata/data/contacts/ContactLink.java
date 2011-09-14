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


package com.google.gdata.data.contacts;

/**
 * Extends the base Link class with contact extensions.
 *
 * 
 */
public class ContactLink {

  /** Link relation type. */
  public static final class Rel {

    /** Link that provides the contact photo. */
    public static final String CONTACT_PHOTO =
        "http://schemas.google.com/contacts/2008/rel#photo";

    /** Link to edit contact photo. */
    public static final String EDIT_CONTACT_PHOTO =
        "http://schemas.google.com/contacts/2008/rel#edit-photo";

  }

  /** MIME type of link target. */
  public static final class Type {

    /** Image/* Contact Link class. */
    public static final String IMAGE = "image/*";

  }

  /** Private constructor to ensure class is not instantiated. */
  private ContactLink() {}

}

