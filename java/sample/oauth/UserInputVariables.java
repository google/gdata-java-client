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


package sample.oauth;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for all user input variables related to the OAuth examples.  It
 * isn't necessary to understand the details of this class in order to
 * understand the OAuth examples.
 *
 * 
 */
public class UserInputVariables {

  /** The various Google services enabled in this sample. */
  public enum GoogleServiceType {
    Blogger,
    Calendar,
    Contacts,
    Finance,
    Picasa
  }

  /** The signature methods supported by OAuth in the Java client. */
  public enum SignatureMethod {
    HMAC,
    RSA
  }

  /** The service the user is accessing. */
  private GoogleServiceType serviceType;

  /** The authentication scope of the request. */
  private String scope;

  /** The feed url of the request. */
  private String feedUrl;

  /** The Google service name for the request. */
  private String googleServiceName;

  /** The signature method. */
  private SignatureMethod signatureMethod;

  /**
   * The key to use when signing the request.  This is either the private key
   * when {@link #signatureMethod} equals RSA, or the consumer secret when
   * {@link #signatureMethod} equals HMAC.
   */
  private String signatureKey;

  /** The OAuth consumer key. */
  private String consumerKey;

  /** A storage area for other example-specific variables. */
  private Map<String, String> otherVars = new HashMap<String, String>();

  public String getGoogleServiceName() {
    return googleServiceName;
  }

  public String getFeedUrl() {
    return feedUrl;
  }

  public String getScope() {
    return scope;
  }

  public GoogleServiceType getServiceType() {
    return serviceType;
  }

  public void setConsumerKey(String key) {
    consumerKey = key;
  }

  public String getConsumerKey() {
    return consumerKey;
  }

  public void setSignatureKey(String key) {
    this.signatureKey = key;
  }

  public String getSignatureKey() {
    return signatureKey;
  }

  public void setSignatureMethod(SignatureMethod signatureMethod) {
    this.signatureMethod = signatureMethod;
  }

  public SignatureMethod getSignatureMethod() {
    return signatureMethod;
  }

  public void setVariable(String key, String value) {
    otherVars.put(key, value);
  }

  public String getVariable(String key) {
    return otherVars.get(key);
  }

  /**
   * Sets the {@link #scope}, {@link #feedUrl} and {@link #googleServiceName}
   * for a given {@link GoogleServiceType}.
   */
  public void setGoogleService(GoogleServiceType stype) {
    this.serviceType = stype;
    switch (stype) {
      case Contacts:
        scope = "http://www.google.com/m8/feeds/";
        feedUrl = "http://www.google.com/m8/feeds/contacts/default/base";
        googleServiceName = "cp";
        break;
      case Calendar:
        scope = "http://www.google.com/calendar/feeds/";
        feedUrl =
            "http://www.google.com/calendar/feeds/default/allcalendars/full";
        googleServiceName = "cl";
        break;
      case Blogger:
        scope = "http://www.blogger.com/feeds/";
        feedUrl = "http://www.blogger.com/feeds/default/blogs";
        googleServiceName = "blogger";
        break;
      case Finance:
        scope = "http://finance.google.com/finance/feeds/";
        feedUrl =
            "http://finance.google.com/finance/feeds/default/portfolios";
        googleServiceName = "finance";
        break;
      case Picasa:
        scope = "http://picasaweb.google.com/data/";
        feedUrl = "http://picasaweb.google.com/data/feed/api/user/default";
        googleServiceName = "lh2";
        break;
      default:
        throw new IllegalArgumentException("Unsupported Google Service");
    }
  }
}
