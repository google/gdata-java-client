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
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Feed;

import java.net.URL;

/**
 * Sample application using OAuth in the Google Data Java Client.  See the
 * comments below to learn about the details.
 *
 * 
 */
class OAuthExample {

  public static void main(String[] args) throws Exception {

    ////////////////////////////////////////////////////////////////////////////
    // STEP 1: Gather the user's information
    ////////////////////////////////////////////////////////////////////////////

    // This step collects information from the user, such as the consumer key
    // and which service to query.  This is just a general setup routine, and
    // the method by which you collect user information may be different in your
    // implementation.
    UserInputHelper inputController = new OAuthUserInputHelper();
    UserInputVariables variables = inputController.getVariables();


    ////////////////////////////////////////////////////////////////////////////
    // STEP 2: Set up the OAuth objects
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
    // STEP 3: Get the Authorization URL
    ////////////////////////////////////////////////////////////////////////////

    // Set the scope for this particular service.
    oauthParameters.setScope(variables.getScope());

    // This method also makes a request to get the unauthorized request token,
    // and adds it to the oauthParameters object, along with the token secret
    // (if it is present).
    oauthHelper.getUnauthorizedRequestToken(oauthParameters);

    // Get the authorization url.  The user of your application must visit
    // this url in order to authorize with Google.  If you are building a
    // browser-based application, you can redirect the user to the authorization
    // url.
    String requestUrl = oauthHelper.createUserAuthorizationUrl(oauthParameters);
    System.out.println(requestUrl);
    System.out.println("Please visit the URL above to authorize your OAuth "
        + "request token.  Once that is complete, press any key to "
        + "continue...");
    System.in.read();


    ////////////////////////////////////////////////////////////////////////////
    // STEP 4: Get the Access Token
    ////////////////////////////////////////////////////////////////////////////

    // Once the user authorizes with Google, the request token can be exchanged
    // for a long-lived access token.  If you are building a browser-based
    // application, you should parse the incoming request token from the url and
    // set it in GoogleOAuthParameters before calling getAccessToken().
    String token = oauthHelper.getAccessToken(oauthParameters);
    System.out.println("OAuth Access Token: " + token);
    System.out.println();


    ////////////////////////////////////////////////////////////////////////////
    // STEP 5: Make an OAuth authorized request to Google
    ////////////////////////////////////////////////////////////////////////////

    // Initialize the variables needed to make the request
    URL feedUrl = new URL(variables.getFeedUrl());
    System.out.println("Sending request to " + feedUrl.toString());
    System.out.println();
    GoogleService googleService =
        new GoogleService(variables.getGoogleServiceName(), "oauth-sample-app");

    // Set the OAuth credentials which were obtained from the step above.
    googleService.setOAuthCredentials(oauthParameters, signer);

    // Make the request to Google
    BaseFeed resultFeed = googleService.getFeed(feedUrl, Feed.class);
    System.out.println("Response Data:");
    System.out.println("=====================================================");
    System.out.println("| TITLE: " + resultFeed.getTitle().getPlainText());
    if (resultFeed.getEntries().size() == 0) {
      System.out.println("|\tNo entries found.");
    } else {
      for (int i = 0; i < resultFeed.getEntries().size(); i++) {
        BaseEntry entry = (BaseEntry) resultFeed.getEntries().get(i);
        System.out.println("|\t" + (i + 1) + ": "
            + entry.getTitle().getPlainText());
      }
    }
    System.out.println("=====================================================");
    System.out.println();


    ////////////////////////////////////////////////////////////////////////////
    // STEP 6: Revoke the OAuth token
    ////////////////////////////////////////////////////////////////////////////

    System.out.println("Revoking OAuth Token...");
    oauthHelper.revokeToken(oauthParameters);
    System.out.println("OAuth Token revoked...");
  }
}
