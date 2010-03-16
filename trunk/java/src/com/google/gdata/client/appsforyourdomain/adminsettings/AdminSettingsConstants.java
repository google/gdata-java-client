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


package com.google.gdata.client.appsforyourdomain.adminsettings;

/**
 * Declarations of constants.
 * 
 * 
 * 
 */
public final class AdminSettingsConstants {

  /**
   * Private constructor to prevent instantiation. 
   */
  private AdminSettingsConstants() {
  }

 public static final String APPS_FEEDS_URL_BASE = 
   "https://apps-apis.google.com/a/feeds/domain/2.0/";

 public static final String KEY_PROPERTY = "signingKey";

  public static final String APPS_SSO_GENERAL_URL_SUFFIX = "sso/general";

  public static final String APPS_SSO_SIGNING_URL_SUFFIX = "sso/signingkey";

  public static final String APPS_SETTINGS_ACCOUNT_URL_SUFFIX = "accountInformation/";

  public static final String APPS_SETTINGS_GENERAL_URL_SUFFIX = "general/";
  
  public static final String APPS_SETTINGS_APPEARANCE_URL_SUFFIX = "appearance/";

  public static final String SERVICE_VERSION = "2.0";
  
  public static final String OUTBOUND_SERVER = "archivalServer";

  public static final String OUTBOUND_EMAIL = "archivalEmailAddress";

  public static final String OUTBOUND_SMTP_MODE = "smtpMode";

  public static final String OUTBOUND_MAIL_FROM = "mailFromHeaderName";

  public static final String OUTBOUND_RCPT_TO = "rcptToHeaderName";

  public static final String OUTBOUND_DOMAIN_HEADER_NAME = "domainNameHeaderName";

  public static final String OUTBOUND_DOMAIN_HEADER_VALUE = "domainNameHeaderValue";

  public static final String PROP_SAML_SIGNON = "samlSignonUri";

  public static final String PROP_SAML_LOGOUT = "samlLogoutUri";

  public static final String PROP_SAML_CHANGEPWD = "changePasswordUri";

  public static final String PROP_SAML_ENABLE = "enableSSO";

  public static final String PROP_SAML_DOMAIN_SPECIFIC_ISSUER = "useDomainSpecificIssuer";

  public static final String PROP_SAML_WHITELIST = "ssoWhitelist";

  public static final String TEST_VALUE_SAML_SIGNON = "http://localhost/helloNewman";

  public static final String TEST_VALUE_SAML_LOGOUT = "http://localhost/goodbyeNewman";

  public static final String TEST_VALUE_SAML_CHANGEPW = "http://localhost/change";
  
  public static final String TEST_EMAIL_ROUTE = "example.com";

  public static final String TEST_SMTPMODE = "SMTP";
  
}
