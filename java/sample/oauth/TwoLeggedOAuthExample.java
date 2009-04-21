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
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Feed;

import java.net.URL;

/**
 * Sample application demonstrating how to do 2-Legged OAuth in the Google Data
 * Java Client.  See the comments below to learn about the details.
 *
 * 
 */
public class TwoLeggedOAuthExample {

  public static void main(String[] args) throws Exception {

    ////////////////////////////////////////////////////////////////////////////
    // STEP 1: Gather the user's information
    ////////////////////////////////////////////////////////////////////////////

    // This step collects information from the user, such as the consumer key
    // and which service to query.  This is just a general setup routine, and
    // the method by which you collect user information may be different in your
    // implementation.
    UserInputHelper inputController =
        new TwoLeggedOAuthUserInputHelper();
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

    // Initialize the OAuth Signer.  2-Legged OAuth must use HMAC-SHA1, which
    // uses the OAuth Consumer Secret to sign the request.  The OAuth Consumer
    // Secret can be obtained at https://www.google.com/accounts/ManageDomains.
    oauthParameters.setOAuthConsumerSecret(variables.getSignatureKey());
    OAuthSigner signer = new OAuthHmacSha1Signer();

    // Finally create a new GoogleOAuthHelperObject.  This is the object you
    // will use for all OAuth-related interaction.
    GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);


    ////////////////////////////////////////////////////////////////////////////
    // STEP 3: Make a request to Google
    ////////////////////////////////////////////////////////////////////////////

    // Set the scope for this particular service.
    oauthParameters.setScope(variables.getScope());

    // Append the "xoauth_requestor_id" parameter to the feed url.  This
    // parameter indicates which user you are loading the data for.
    String feedUrlString = variables.getFeedUrl();
    feedUrlString += "?xoauth_requestor_id="
        + variables.getVariable("xoauth_requestor_id");
    URL feedUrl = new URL(feedUrlString);

    System.out.println("Sending request to " + feedUrl.toString());
    System.out.println();
    GoogleService googleService =
        new GoogleService(variables.getGoogleServiceName(),
            "2-legged-oauth-sample-app");

    // Set the OAuth credentials which were obtained from the steps above.
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
  }
}
