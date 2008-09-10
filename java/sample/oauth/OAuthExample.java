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

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.authn.oauth.GoogleOAuthHelper;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthRsaSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.client.finance.FinanceService;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.contacts.ContactFeed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Sample application using OAuth in the Google Data Java Client.  See the
 * comments below to learn how to use OAuth in the Java Client.
 *
 * 
 */
class OAuthExample {

  public static void main(String[] args) throws Exception {

    UserInputVariables variables = getUserInputVariables();

    ////////////////////////////////////////////////////////////////////////////
    // STEP 1: Set up the OAuth objects
    ////////////////////////////////////////////////////////////////////////////

    // You first need to initialize a few OAuth-related objects.
    // GoogleOAuthParameters holds all the parameters related to OAuth.
    // OAuthSigner is responsible for signing the OAuth base string.
    GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();

    // Set your OAuth Consumer Key (which you can register at
    // https://www.google.com/accounts/ManageDomains).
    oauthParameters.setOAuthConsumerKey(variables.getConsumerKey());

    // Initialize the OAuth Signer.  If you are using RSA-SHA1, you must provide
    // your private key as a Base-64 string conforming to the PKCS #8 standard.
    // Visit http://code.google.com/apis/gdata/authsub.html#Registered to learn
    // more about creating a key/certificate pair.  If you are using HMAC-SHA1,
    // you must set your OAuth Consumer Secret, which can be obtained at
    // https://www.google.com/accounts/ManageDomains.
    OAuthSigner signer;
    switch (variables.getSignatureMethod()) {
      case RSA:
        signer = new OAuthRsaSha1Signer(variables.getSignatureKey());
        break;
      case HMAC:
        oauthParameters.setOAuthConsumerSecret(variables.getSignatureKey());
        signer = new OAuthHmacSha1Signer();
        break;
      default:
        throw new IllegalArgumentException("Invalid Signature Method");
    }

    // Finally create a new GoogleOAuthHelperObject.  This is the object you
    // will use for all OAuth-related interaction.
    GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);


    ////////////////////////////////////////////////////////////////////////////
    // STEP 2: Get the Authorization URL
    ////////////////////////////////////////////////////////////////////////////

    // Set the scope for this particular service.
    oauthParameters.setScope(variables.getScope());

    // Get the authorization url.  The user of your application must visit
    // this url in order to authorize with Google.  If you are building a
    // browser-based application, you can redirect the user to the authorization
    // url.  This method also makes a behind-the-scenes request to get the
    // request token, and adds it to the oauthParameters object.
    String requestUrl = oauthHelper.getUserAuthorizationUrl(oauthParameters);
    System.out.println(requestUrl);
    System.out.println("Please visit the URL above to authorize your OAuth "
        + "request token.  Once that is complete, press any key to "
        + "continue...");
    System.in.read();


    ////////////////////////////////////////////////////////////////////////////
    // STEP 3: Get the Access Token
    ////////////////////////////////////////////////////////////////////////////

    // Once the user authorizes with Google, the request token can be exchanged
    // for a long-lived access token.  If you are building a browser-based
    // application, you should parse the incoming request token from the url and
    // set it in GoogleOAuthParameters before calling getAccessToken().
    String token = oauthHelper.getAccessToken(oauthParameters);
    System.out.println("OAuth Access Token: " + token);
    System.out.println();


    ////////////////////////////////////////////////////////////////////////////
    // STEP 4: Make an OAuth authorized request to Google
    ////////////////////////////////////////////////////////////////////////////

    // Initialize the variables needed to make the request
    URL feedUrl = new URL(variables.getFeedUrl());
    System.out.println("Sending request to " + feedUrl.toString());
    System.out.println();
    GoogleService googleService = variables.getGoogleService();

    // Set the OAuth credentials which were obtained from the step above.
    googleService.setOAuthCredentials(oauthParameters, signer);

    // Make the request to Google
    ContactFeed resultFeed = googleService.getFeed(feedUrl, ContactFeed.class);
    System.out.println("Response Data:");
    System.out.println("=====================================================");
    System.out.println("| TITLE: " + resultFeed.getTitle().getPlainText());
    if (resultFeed.getEntries().size() == 0) {
      System.out.println("|\tNo entries found.");
    } else {
      for (int i = 0; i < resultFeed.getEntries().size(); i++) {
        BaseEntry entry = resultFeed.getEntries().get(i);
        System.out.println("|\t" + (i + 1) + ": "
            + entry.getTitle().getPlainText());
      }
    }
    System.out.println("=====================================================");
  }

  /** The various Google services enabled in this sample. */
  private enum GoogleServiceType {
    Blogger,
    Contacts,
    Finance,
    Picasa
  }

  /** The signature methods supported by the Java client. */
  private enum SignatureMethod {
    HMAC,
    RSA
  }

  /** A helper class to store all the user variables related to OAuth. */
  private static class UserInputVariables {
    private static final String APPLICATION_NAME = "oauth-sample-app";
    private GoogleServiceType serviceType;
    private String scope;
    private String feedUrl;
    private GoogleService googleService;
    private SignatureMethod signatureMethod;
    private String signatureKey;
    private String consumerKey;

    public UserInputVariables() {
    }

    /** Sets the variables related to a Google Service. */
    public void setServiceVariables(GoogleServiceType stype) {
      serviceType = stype;
      switch (stype) {
        case Contacts:
          scope = "http://www.google.com/m8/feeds/";
          feedUrl = "http://www.google.com/m8/feeds/contacts/default/base";
          googleService = new ContactsService(APPLICATION_NAME);
          break;
        case Blogger:
          scope = "http://www.blogger.com/feeds/";
          feedUrl = "http://www.blogger.com/feeds/default/blogs";
          googleService = new BloggerService(APPLICATION_NAME);
          break;
        case Finance:
          scope = "http://finance.google.com/finance/feeds/";
          feedUrl =
              "http://finance.google.com/finance/feeds/default/portfolios";
          googleService = new FinanceService(APPLICATION_NAME);
          break;
        case Picasa:
          scope = "http://picasaweb.google.com/data/";
          feedUrl = "http://picasaweb.google.com/data/feed/api/user/default";
          googleService = new PicasawebService(APPLICATION_NAME);
          break;
        default:
          throw new IllegalArgumentException("Unsupported Google Service");
      }
    }

    public String getFeedUrl() {
      return feedUrl;
    }

    public GoogleService getGoogleService() {
      return googleService;
    }

    public GoogleServiceType getGoogleServiceType() {
      return serviceType;
    }

    public String getScope() {
      return scope;
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
  }

  /** Helper method to read input from the command line. */
  private static String readCommandLineInput() {
    System.out.print("> ");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String userInput = null;
    try {
       userInput = br.readLine();
    } catch (IOException ioe) {
       System.out.println("IO error trying to read input!");
       System.exit(1);
    }
    System.out.println();
    return userInput;
  }

  /** Helper method to gather input from the user. */
  private static UserInputVariables getUserInputVariables() {
    UserInputVariables variables = new UserInputVariables();
    System.out.println();
    System.out.println("=============");
    System.out.println("Testing OAuth");
    System.out.println("=============");
    System.out.println();
    System.out.println("This sample will show you how to use OAuth to retrieve "
        + "information from a Google Data service.  Follow the instructions "
        + "below to continue");
    System.out.println();
    System.out.println("Please select a Google service to query:");
    for (GoogleServiceType t : GoogleServiceType.values()) {
      System.out.println("\t" + (t.ordinal() + 1) + ") " + t.toString());
    }
    variables.setServiceVariables(GoogleServiceType
        .values()[Integer.parseInt(readCommandLineInput()) - 1]);
    System.out.println("Please select a signature method:");
    for (SignatureMethod m : SignatureMethod.values()) {
      System.out.println("\t" + (m.ordinal() + 1) + ") " + m.toString());
    }
    variables.setSignatureMethod(SignatureMethod.values()[Integer
        .parseInt(readCommandLineInput()) - 1]);
    System.out.println("Please enter your OAuth consumer key (usually your "
        + "domain, visit https://www.google.com/accounts/ManageDomains to "
        + "manage your OAuth parameters)");
    variables.setConsumerKey(readCommandLineInput());
    switch (variables.getSignatureMethod()) {
      case RSA:
        System.out.println("Please enter your RSA private key (the key should "
            + "be a Base-64 encoded string conforming to the PKCS #8 standard");
        break;
      case HMAC:
        System.out.println("Please enter your OAuth consumer secret (visit "
            + "https://www.google.com/accounts/ManageDomains to view your "
            + "consumer secret)");
        break;
    }
    variables.setSignatureKey(readCommandLineInput());
    return variables;
  }
}
