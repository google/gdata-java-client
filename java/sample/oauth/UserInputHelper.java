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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Helper class for loading data related to making an OAuth request.  It isn't
 * necessary to understand the details of this class in order to understand the
 * OAuth examples.
 *
 * 
 */
public abstract class UserInputHelper {

  /** Helper method to read input from the command line. */
  protected static String readCommandLineInput() {
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

  /** Loads the variables from the user for a specific OAuth request. */
  public abstract UserInputVariables getVariables();

  /** Loads the services that a supported by this specific example. */
  protected abstract ArrayList<UserInputVariables.GoogleServiceType>
      getSupportedServices();

  /** Print out the header that begins each OAuth example. */
  protected void printHeader() {
    System.out.println();
    System.out.println("=============");
    System.out.println("Testing OAuth");
    System.out.println("=============");
    System.out.println();
    System.out.println("This sample will show you how to use OAuth to retrieve "
        + "information from a Google Data service.  Follow the instructions "
        + "below to continue");
    System.out.println();
  }

  /** Get the Google service type from the user. */
  protected UserInputVariables.GoogleServiceType getGoogleServiceType() {
    System.out.println("Please select a Google service to query:");
    ArrayList<UserInputVariables.GoogleServiceType> services =
        getSupportedServices();
    for (int i = 0; i < services.size(); i++) {
      System.out.println("\t" + (i + 1) + ") " + services.get(i).toString());
    }
    return services.get(Integer.parseInt(readCommandLineInput()) - 1);
  }

  /** Get the signature method from the user. */
  protected UserInputVariables.SignatureMethod getSignatureMethod() {
    System.out.println("Please select a signature method:");
    for (UserInputVariables.SignatureMethod m :
        UserInputVariables.SignatureMethod.values()) {
      System.out.println("\t" + (m.ordinal() + 1) + ") " + m.toString());
    }
    return UserInputVariables.SignatureMethod.values()[
        Integer.parseInt(readCommandLineInput()) - 1];
  }

  /** Get the consumer key from the user. */
  protected String getOAuthConsumerKey() {
    System.out.println("Please enter your OAuth consumer key (usually your "
        + "domain, visit https://www.google.com/accounts/ManageDomains to "
        + "manage your OAuth parameters)");
    return readCommandLineInput();
  }

  /** Get the RSA private key from the user. */
  protected String getRsaPrivateKey() {
    System.out.println("Please enter your RSA private key (the key should "
        + "be a Base-64 encoded string conforming to the PKCS #8 standard");
    return readCommandLineInput();
  }

  /** Get the consumer secret from the user. */
  protected String getConsumerSecret() {
    System.out.println("Please enter your OAuth consumer secret (visit "
        + "https://www.google.com/accounts/ManageDomains to view your "
        + "consumer secret)");
    return readCommandLineInput();
  }
}
