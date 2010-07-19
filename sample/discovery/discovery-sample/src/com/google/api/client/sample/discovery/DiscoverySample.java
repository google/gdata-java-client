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

package com.google.api.client.sample.discovery;

import com.google.api.client.apache.ApacheHttpTransport;
import com.google.api.client.googleapis.json.DiscoveryDocument;
import com.google.api.client.googleapis.json.DiscoveryDocument.ServiceMethod;
import com.google.api.client.googleapis.json.DiscoveryDocument.ServiceParameter;
import com.google.api.client.googleapis.json.DiscoveryDocument.ServiceResource;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yaniv Inbar
 */
public class DiscoverySample {

  private static final Pattern API_NAME_PATTERN = Pattern.compile("\\w+");

  private static final Pattern METHOD_PATTERN =
      Pattern.compile("(\\w+)(\\.(\\w+)(\\.(\\w+))?)?");

  public static void main(String[] args) throws Exception {
    // initialize HTTP transport
    HttpTransport.setLowLevelHttpTransport(ApacheHttpTransport.INSTANCE);
    Debug.enableLogging();
    // parse command argument
    if (args.length == 0) {
      showMainHelp();
    } else {
      String command = args[0];
      if (command.equals("help")) {
        help(args);
      } else if (command.equals("call")) {
        call(args);
      } else if (command.equals("discover")) {
        discover(args);
      } else {
        error(null, "unknown command: " + command);
      }
    }
  }

  private static void help(String[] args) {
    if (args.length == 1) {
      showMainHelp();
    } else {
      String helpCommand = args[1];
      if (helpCommand.equals("call")) {
        System.out.println("Usage: google call methodName [parameters]");
        System.out.println();
        System.out.println("Examples:");
        System.out.println(
            "  google call discovery.apis.get --api buzz --prettyprint true");
        System.out
            .println(
                "  google call buzz.activities.list --scope @self --userId @me --alt json --prettyprint true");
        System.out
            .println(
                "  echo {\\\"data\\\":{\\\"object\\\":{\\\"content\\\":\\\"Posting using google command-line tool \\(http://bit.ly/9KFkzF\\)\\\"}}} > buzzpost.json && google call buzz.activities.insert --userId @me buzzpost.json");
      } else if (helpCommand.equals("discover")) {
        System.out.println("Usage: google discover apiName");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  google discover buzz");
        System.out.println("  google discover moderator");
      } else {
        error(null, "unknown command: " + helpCommand);
      }
    }
  }

  private static void showMainHelp() {
    System.out.println("Google Discovery API v0.1");
    System.out.println();
    System.out.println("For more help on a specific command, type one of:");
    System.out.println();
    System.out.println("  google help call");
    System.out.println("  google help discover");
  }

  private static void error(String command, String detail) {
    System.err.println("ERROR: " + detail);
    System.err.println(
        "For help, type: google" + (command == null ? "" : " help " + command));
    System.exit(1);
  }

  private static void call(String[] args) throws Exception {
    if (args.length == 1) {
      error("call", "missing method name");
    }
    String fullMethodName = args[1];
    Matcher m = METHOD_PATTERN.matcher(fullMethodName);
    if (!m.matches()) {
      error("call", "invalid method name: " + fullMethodName);
    }
    String apiName = m.group(1);
    String resourceName = m.group(3);
    String methodName = m.group(5);
    DiscoveryDocument doc = loadDiscoveryDocument(apiName);
    Map<String, ServiceResource> resources = doc.serviceDefinition.resources;
    ServiceMethod method = null;
    if (resources != null) {
      ServiceResource resource = resources.get(resourceName);
      Map<String, ServiceMethod> methods = resource.methods;
      if (methods != null) {
        method = methods.get(methodName);
      }
    }
    if (method == null) {
      error("call", "method not found: " + fullMethodName);
    }
    HashMap<String, String> parameters = new HashMap<String, String>();
    HashMap<String, String> queryParameters = new HashMap<String, String>();
    File requestBodyFile = null;
    int i = 2;
    while (i < args.length) {
      String argName = args[i++];
      if (argName.startsWith("--")) {
        String parameterName = argName.substring(2);
        if (i == args.length) {
          error("call", "missing parameter value for: " + argName);
        }
        String parameterValue = args[i++];
        if (method.parameters == null
            || !method.parameters.containsKey(parameterName)) {
          queryParameters.put(parameterName, parameterValue);
        } else {
          String oldValue = parameters.put(parameterName, parameterValue);
          if (oldValue != null) {
            error("call", "duplicate parameter: " + argName);
          }
        }
      } else {
        if (requestBodyFile != null) {
          error(
              "call", "multiple HTTP request body files specified: " + argName);
        }
        String fileName = argName;
        requestBodyFile = new File(fileName);
        if (!requestBodyFile.canRead()) {
          error("call", "unable to read file: " + argName);
        }
      }
    }
    for (Map.Entry<String, ServiceParameter> parameter :
        method.parameters.entrySet()) {
      String paramName = parameter.getKey();
      if (parameter.getValue().required && !parameters.containsKey(paramName)) {
        error("call", "missing required parameter: " + paramName);
      }
    }
    doc.transport = new HttpTransport();
    HttpRequest request =
        doc.buildRequest(resourceName + "." + methodName, parameters);
    request.url.putAll(queryParameters);
    if (requestBodyFile != null) {
      InputStreamContent fileContent = new InputStreamContent();
      // TODO: support other content types?
      fileContent.type = "application/json";
      fileContent.setFileInput(requestBodyFile);
      request.content = fileContent;
    }
    try {
      if (apiName.equals("bigquery") || apiName.equals("prediction")
          || apiName.equals("latitude")) {
        error("call", "API not supported: " + apiName);
      }
      if (!apiName.equals("discovery") && !apiName.equals("diacritize")) {
        Auth.authorize(doc, apiName, method);
      }
      String response = request.execute().parseAsString();
      System.out.println(response);
      Auth.revoke();
    } catch (HttpResponseException e) {
      System.err.println(e.response.parseAsString());
      Auth.revoke();
      System.exit(1);
    } catch (Throwable t) {
      t.printStackTrace();
      Auth.revoke();
      System.exit(1);
    }
  }

  private static DiscoveryDocument loadDiscoveryDocument(String apiName)
      throws IOException {
    try {
      return DiscoveryDocument.load(apiName);
    } catch (HttpResponseException e) {
      if (e.response.statusCode == 404) {
        error("discover", "API not found: " + apiName);
      }
      throw e;
    }
  }

  private static void discover(String[] args) throws IOException {
    System.out.println("Google Discovery API Client v0.1");
    // load discovery doc
    if (args.length == 1) {
      error("discover", "missing api name");
    }
    String apiName = args[1];
    if (!API_NAME_PATTERN.matcher(apiName).matches()) {
      System.err.println("ERROR: invalid API name: " + apiName);
    }
    DiscoveryDocument doc = loadDiscoveryDocument(apiName);
    // compute method details
    ArrayList<MethodDetails> result = new ArrayList<MethodDetails>();
    Map<String, ServiceResource> resources = doc.serviceDefinition.resources;
    if (resources != null) {
      for (Map.Entry<String, ServiceResource> resourceEntry :
          resources.entrySet()) {
        String resourceName = apiName + "." + resourceEntry.getKey();
        ServiceResource resource = resourceEntry.getValue();
        Map<String, ServiceMethod> methods = resource.methods;
        if (methods != null) {
          for (Map.Entry<String, ServiceMethod> methodEntry :
              methods.entrySet()) {
            MethodDetails details = new MethodDetails();
            details.name = resourceName + "." + methodEntry.getKey();
            Map<String, ServiceParameter> parameters =
                methodEntry.getValue().parameters;
            if (parameters != null) {
              for (Map.Entry<String, ServiceParameter> parameterEntry :
                  parameters.entrySet()) {
                String parameterName = parameterEntry.getKey();
                if (parameterEntry.getValue().required) {
                  details.requiredParameters.add(parameterName);
                } else {
                  details.optionalParameters.add(parameterName);
                }
              }
              Collections.sort(details.requiredParameters);
              Collections.sort(details.optionalParameters);
              result.add(details);
            }
          }
        }
      }
    }
    Collections.sort(result);
    // display method details
    for (MethodDetails methodDetail : result) {
      System.out.println();
      System.out.println(methodDetail.name);
      System.out.println("  required parameters: "
          + showParams(methodDetail.requiredParameters));
      System.out.println("  optional parameters: "
          + showParams(methodDetail.optionalParameters));
    }
  }

  private static String showParams(ArrayList<String> params) {
    StringBuilder buf = new StringBuilder();
    for (int i = 0; i < params.size(); i++) {
      if (i != 0) {
        buf.append(", ");
      }
      buf.append(params.get(i));
    }
    return buf.toString();
  }
}
