/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.sample.prediction;

import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.clientlogin.ClientLogin;
import com.google.api.client.googleapis.json.JsonCContent;
import com.google.api.client.googleapis.json.JsonCParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.sample.prediction.model.Debug;
import com.google.api.client.sample.prediction.model.InputData;
import com.google.api.client.sample.prediction.model.OutputData;
import com.google.api.client.sample.prediction.model.PredictionUrl;

import java.io.IOException;

/**
 * @author Yaniv Inbar
 */
public class PredictionSample {

  public static void main(String[] args) {
    Debug.enableLogging();
    HttpTransport transport = GoogleTransport.create();
    transport.addParser(new JsonCParser());
    try {
      authenticateWithClientLogin(transport);
      predict(transport, "Is this sentence in English?");
      predict(transport, "¿Es esta frase en Español?");
      predict(transport, "Est-ce cette phrase en Français?");
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void authenticateWithClientLogin(HttpTransport transport)
      throws IOException {
    ClientLogin authenticator = new ClientLogin();
    authenticator.authTokenType = "ndev";
    authenticator.username = ClientLoginCredentials.ENTER_USERNAME;
    authenticator.password = ClientLoginCredentials.ENTER_PASSWORD;
    authenticator.authenticate().setAuthorizationHeader(transport);
  }

  private static void predict(HttpTransport transport, String text)
      throws IOException {
    HttpRequest request = transport.buildPostRequest();
    request.url = PredictionUrl.fromBucketAndObjectNames(
        ClientLoginCredentials.OBJECT_PATH);
    JsonCContent content = new JsonCContent();
    InputData inputData = new InputData();
    inputData.input.text.add(text);
    content.data = inputData;
    request.content = content;
    OutputData outputData = request.execute().parseAs(OutputData.class);
    System.out.println("Text: " + text);
    System.out.println("Predicted language: " + outputData.output.outputLabel);
  }
}
