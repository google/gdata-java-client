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

import java.util.ArrayList;

/**
 * Loads the data related to making a 2-Legged OAuth request.  It isn't
 * necessary to understand the details of this class in order to understand the
 * OAuth examples.
 *
 * 
 */
public class TwoLeggedOAuthUserInputHelper extends UserInputHelper {

  @Override
  public ArrayList<UserInputVariables.GoogleServiceType>
      getSupportedServices() {
    ArrayList<UserInputVariables.GoogleServiceType> services =
        new ArrayList<UserInputVariables.GoogleServiceType>();
    services.add(UserInputVariables.GoogleServiceType.Calendar);
    services.add(UserInputVariables.GoogleServiceType.Contacts);
    return services;
  }

  @Override
  public UserInputVariables getVariables() {
    UserInputVariables variables = new UserInputVariables();
    printHeader();
    variables.setGoogleService(getGoogleServiceType());
    variables.setConsumerKey(getOAuthConsumerKey());
    variables.setSignatureMethod(UserInputVariables.SignatureMethod.HMAC);
    variables.setSignatureKey(getConsumerSecret());
    System.out.println("Enter the full email address of the user who's data you"
        + " would like to load (for example, username@domain.com)");
    variables.setVariable("xoauth_requestor_id", readCommandLineInput());
    return variables;
  }

}
