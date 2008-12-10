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
 * Loads the data related to making an OAuth request.  It isn't necessary to
 * understand the details of this class in order to understand the OAuth
 * examples.
 *
 * 
 */
public class OAuthUserInputHelper extends UserInputHelper {

  @Override
  public ArrayList<UserInputVariables.GoogleServiceType>
      getSupportedServices() {
    ArrayList<UserInputVariables.GoogleServiceType> services =
        new ArrayList<UserInputVariables.GoogleServiceType>();
    services.add(UserInputVariables.GoogleServiceType.Blogger);
    services.add(UserInputVariables.GoogleServiceType.Calendar);
    services.add(UserInputVariables.GoogleServiceType.Contacts);
    services.add(UserInputVariables.GoogleServiceType.Finance);
    services.add(UserInputVariables.GoogleServiceType.Picasa);
    return services;
  }

  @Override
  public UserInputVariables getVariables() {
    UserInputVariables variables = new UserInputVariables();
    printHeader();
    variables.setGoogleService(getGoogleServiceType());
    variables.setConsumerKey(getOAuthConsumerKey());
    variables.setSignatureMethod(getSignatureMethod());
    String key;
    switch (variables.getSignatureMethod()) {
      case RSA:
        key = getRsaPrivateKey();
        break;
      case HMAC:
        key = getConsumerSecret();
        break;
      default:
        throw new IllegalArgumentException("Invalid Signature Method: "
            + variables.getSignatureMethod().toString());
    }
    variables.setSignatureKey(key);
    return variables;
  }

}
