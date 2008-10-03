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
 * Contains constants used by the GMail Settings API sample clients. 
 */
public final class Constants {

  /**
   */
  private Constants() {}
  
  public static final int FILTER_FROM_MAX_LENGTH = 400;
  public static final int FILTER_TO_MAX_LENGTH = 400;
  public static final int FILTER_SUBJECT_MAX_LENGTH = 1000;
  public static final int FILTER_HAS_THE_WORD_MAX_LENGTH = 1000;
  public static final int FILTER_DOES_NOT_HAVE_THE_WORD_MAX_LENGTH = 1000;
  
  public static final String[] FORWARDING_ACTION = {"KEEP", "ARCHIVE", "DELETE"};
  
  public static final String[] GENERAL_ALLOWED_PAGE_SIZES = {"25", "50", "100"};
  
  public static final int LABEL_MIN_LENGTH = 1;
  public static final int LABEL_MAX_LENGTH = 40;
  
  public static final String[] LANGUAGE_VALID_KEY = {
    "ar", // Arabic
    "bn", // Bengali
    "bg", // Bulgarian
    "ca", // Catalan
    "zh-CN", // Chinese (Simplified)
    "zh-TW", // Chinese (Traditional)
    "hr", // Croatian
    "cs", // Czech
    "da", // Danish
    "nl", // Dutch
    "en-US", // English (United States)
    "en-GB", // English (United Kingdom)
    "et", // Estonian
    "fi", // Finnish
    "fr", // French
    "de", // German
    "el", // Greek
    "gu", // Gujarati
    "iw", // Hebrew
    "hi", // Hindi
    "hu", // Hungarian
    "is", // Icelandic
    "in", // Indonesian
    "it", // Italian
    "ja", // Japanese
    "kn", // Kannada
    "ko", // Korean
    "lv", // Latvian
    "lt", //Lithuanian
    "ms", // Malay
    "ml", // Malayalam
    "mr", // Marathi
    "no", // Norwegian
    "or", // Oriya
    "fa", // Persian
    "pl", // Polish
    "pt-BR", // Portuguese (Brazil)
    "pt-PT", // Portuguese (Portugal)
    "ro", // Romanian
    "ru", // Russian
    "sr", // Serbian
    "sk", // Slovak
    "sl", // Slovenian
    "es", // Spanish
    "sv", // Swedish
    "tl", // Tagalog
    "ta", // Tamil
    "te", // Telugu
    "th", // Thai
    "tr", // Turkish
    "uk", // Ukrainian
    "ur", // Urdu
    "vi" // Vietnamese
  };

  public static final String[] POP_ENABLE_FOR = {"ALL_MAIL", "MAIL_FROM_NOW_ON"};
  public static final String[] POP_ACTION = {"KEEP", "ARCHIVE", "DELETE"};
  
  public static final int SENDAS_NAME_MIN_LENGTH = 1;
  public static final int SENDAS_NAME_MAX_LENGTH = 250;
  
  public static final int SIGNATURE_MIN_LENGTH = 0;
  public static final int SIGNATURE_MAX_LENGTH = 1000;
  
  public static final int VACATION_SUBJECT_MIN_LENGTH = 0;
  public static final int VACATION_SUBJECT_MAX_LENGTH = 500;
  public static final int VACATION_MESSAGE_MIN_LENGTH = 0;
  public static final int VACATION_MESSAGE_MAX_LENGTH = 100 * 1024;

  public static final String DEFAULT_DOMAIN = "";
  public static final String DEFAULT_USERNAME = "";
  public static final String DEFAULT_PASSWORD = "";

  public static final String PROTOCOL = "https";
  public static final String APPS_APIS_DOMAIN = "apps-apis.google.com";
  public static final String APPS_APIS_URL = "/a/feeds/emailsettings/2.0";
}
