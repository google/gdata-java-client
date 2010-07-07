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
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Yaniv Inbar
 */
public class DiscoverySample {

  private static final Pattern PATTERN =
      Pattern.compile("(\\w+)(\\.(\\w+)(\\.(\\w+))?)?");

  public static void main(String[] args) throws IOException {
    HttpTransport.setLowLevelHttpTransport(ApacheHttpTransport.INSTANCE);
    Debug.enableLogging();
    System.out.println("Discovery v0.1 Sample");
    if (args.length == 0) {
      System.out.println("Usage: discover [name]");
      System.out.println();
      System.out.println("Examples:");
      System.out.println("  discover buzz");
      System.out.println("  discover buzz.activities");
      System.out.println("  discover buzz.activities.list");
    } else {
      String param = args[0];
      Matcher m = PATTERN.matcher(param);
      if (!m.matches()) {
        System.err.println("ERROR: invalid input");
      } else {
        String api = m.group(1);
        DiscoveryDocument doc = null;
        try {
          doc = DiscoveryDocument.execute(api);
        } catch (HttpResponseException e) {
          if (e.response.statusCode == 404) {
            System.err.println("ERROR: API not found: " + api);
            e.response.ignore();
          } else {
            throw e;
          }
        }
        if (doc != null) {
          String resourceName = m.group(3);
          if (resourceName == null) {
            System.out.println("Resources in " + api + ":");
            String first = null;
            for (String name : doc.serviceDefinition.resources.keySet()) {
              if (first == null) {
                first = name;
              }
              System.out.println("  " + name);
            }
            System.out.println();
            System.out.println(
                "Example to list methods for a resource: discover " + api + "."
                    + first);
          } else {
            ServiceResource resource =
                doc.serviceDefinition.resources.get(resourceName);
            String fullResourceName = api + "." + resourceName;
            if (resource == null) {
              System.err.println(
                  "ERROR: Resource not found: " + fullResourceName);
            } else {
              String methodName = m.group(5);
              if (methodName == null) {
                System.out.println("Methods in " + fullResourceName + ":");
                String first = null;
                for (String name : resource.methods.keySet()) {
                  if (first == null) {
                    first = name;
                  }
                  System.out.println("  " + name);
                }
                System.out.println();
                System.out.println(
                    "Example to list parameters of a method: discover "
                        + fullResourceName + "." + first);
              } else {
                ServiceMethod method = resource.methods.get(methodName);
                String fullMethodName = fullResourceName + "." + methodName;
                if (method == null) {
                  System.err.println(
                      "ERROR: Method not found: " + fullMethodName);
                } else {
                  System.out.println("Parameters for " + fullMethodName + ":");
                  for (Map.Entry<String, ServiceParameter> entry :
                      method.parameters.entrySet()) {
                    String name = entry.getKey();
                    System.out.print("  " + name);
                    ServiceParameter parameter = entry.getValue();
                    if (parameter.required) {
                      System.out.print(" (required)");
                    }
                    System.out.println();
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
