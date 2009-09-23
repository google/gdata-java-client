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


package sample.appsforyourdomain.gmailsettings;

/**
 * Contains default values for the GMail Settings API sample clients.
 */
public final class Defaults {

  /**
   * Prevents the class from being instantiated.
   */
  private Defaults() {}

  public static final String FILTER_FROM = "me@google.com";
  public static final String FILTER_TO = "you@google.com";
  public static final String FILTER_SUBJECT = "subject";
  public static final String FILTER_HAS_THE_WORD = "has";
  public static final String FILTER_DOES_NOT_HAVE_THE_WORD = "no";
  public static final boolean FILTER_HAS_ATTACHMENT = true;
  public static final boolean FILTER_SHOULD_MARK_AS_READ = true;
  public static final boolean FILTER_SHOULD_ARCHIVE = true;
  public static final String FILTER_LABEL = "label";

  public static final String SEND_AS_NAME = "test-alias";
  public static final String SEND_AS_ADDRESS = "johndoe@example.com";
  public static final String SEND_AS_REPLY_TO = "reply-to@someplace.com";
  public static final boolean SEND_AS_MAKE_DEFAULT = false;

  public static final String LABEL = "label";

  public static final boolean FORWARDING_ENABLE = true;
  public static final String FORWARDING_FORWARD_TO = "test@admin-api.com";
  public static final String FORWARDING_ACTION = "ARCHIVE";
  public static final boolean FORWARDING_MAKE_DEFAULT = false;

  public static final boolean POP_ENABLE = true;
  public static final String POP_ENABLE_FOR = "MAIL_FROM_NOW_ON";
  public static final String POP_ACTION = "ARCHIVE";

  public static final boolean IMAP_ENABLE = true;

  public static final boolean VACATION_ENABLE = true;
  public static final String VACATION_SUBJECT = "I'm on vacation";
  public static final String VACATION_MESSAGE = "Actually I'm just testing the vacation " +
      "responder";
  public static final boolean VACATION_CONTACTS_ONLY = true;

  public static final String SIGNATURE = "<Insert witty signature here>";

  public static final String GENERAL_PAGE_SIZE = "50";
  public static final boolean GENERAL_ENABLE_SHORTCUTS = true;
  public static final boolean GENERAL_ENABLE_ARROWS = true;
  public static final boolean GENERAL_ENABLE_SNIPPETS = true;
  public static final boolean GENERAL_ENABLE_UNICODE = true;

  public static final String LANGUAGE = "en-US";

  public static final boolean WEBCLIP_ENABLE = true;
}
