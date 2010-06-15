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

package com.google.api.client.googleapis.json;

import com.google.api.client.escape.CharEscapers;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.util.DataUtil;
import com.google.api.client.util.Key;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages a Google API discovery document based on the JSON format.
 * 
 * @since 2.2
 * @author vbarathan@google.com (Prakash Barathan)
 * @author Yaniv Inbar
 */
public final class DiscoveryDocument {

  /** Container for the discovery document model. */
  static final class Model {

    /**
     * Version specific Service Definition for a service
     */
    public static final class ServiceDefinition {
      /** Base url for service endpoint */
      @Key
      String baseUrl;

      /** List of supported resources */
      @Key
      Map<String, Resource> resources;

      /**
       * Returns {@link Method} definition for given method name. Method
       * identifier is of format "resourceName.methodName"
       */
      Method getResourceMethod(String methodIdentifier) {
        int dot = methodIdentifier.indexOf('.');
        String resourceName = methodIdentifier.substring(0, dot);
        String methodName = methodIdentifier.substring(dot + 1);
        Resource resource = this.resources.get(resourceName);
        return resource == null ? null : resource.methods.get(methodName);
      }

      /**
       * Returns url for requested method. Method identifier is of format
       * "resourceName.methodName"
       */
      public String getResourceUrl(String methodIdentifier) {
        return baseUrl + getResourceMethod(methodIdentifier).pathUrl;
      }
    }

    /**
     * Defines an available resource in a service.
     */
    public static final class Resource {
      @Key
      Map<String, Method> methods;
    }

    /**
     * Defines an available method for a service.
     */
    public static final class Method {
      /** Method path url relative to base url */
      @Key
      String pathUrl;

      /** Method's Http verb name */
      @Key
      String httpMethod;

      /** Method type. */
      @Key
      final String methodType = "rest";
    }
  }

  /** API service definition parsed from discovery document */
  private final Model.ServiceDefinition serviceDefinition;

  /**
   * Google transport required by {@link #buildRequest}.
   */
  public HttpTransport transport;

  DiscoveryDocument(Model.ServiceDefinition serviceDefinition) {
    this.serviceDefinition = serviceDefinition;
  }

  /**
   * Executes a request for the JSON-formatted discovery document.
   * 
   * @param api API name
   * @return discovery document
   * @throws IOException I/O exception executing request
   */
  public static DiscoveryDocument execute(String api) throws IOException {
    GenericUrl discoveryUrl =
        new GenericUrl("http://www.googleapis.com/discovery/0.1/describe");
    discoveryUrl.put("api", api);
    HttpTransport transport = GoogleTransport.create();
    HttpRequest request = transport.buildGetRequest();
    request.url = discoveryUrl;
    JsonParser parser = JsonCParser.parserForResponse(request.execute());
    Json.skipToKey(parser, api);
    Json.skipToKey(parser, "1.0");
    Model.ServiceDefinition serviceDefinition = new Model.ServiceDefinition();
    Json.parseAndClose(parser, serviceDefinition, null);
    return new DiscoveryDocument(serviceDefinition);
  }

  /**
   * Creates an HTTP request based on the given method name and parameters.
   * 
   * @param fullyQualifiedMethodName name of method as defined in Discovery
   *        document of format "resourceName.methodName"
   * @param parameters user defined key / value data mapping
   * @return HTTP request
   * @throws IOException I/O exception reading
   */
  public HttpRequest buildRequest(String fullyQualifiedMethodName,
      Object parameters) throws IOException {
    HttpTransport transport = this.transport;
    if (transport == null) {
      throw new IllegalArgumentException("missing transport");
    }
    // Create request for specified method
    Model.ServiceDefinition serviceDefinition = this.serviceDefinition;
    Model.Method method =
        serviceDefinition.getResourceMethod(fullyQualifiedMethodName);
    if (method == null) {
      throw new IllegalArgumentException("unrecognized method: "
          + fullyQualifiedMethodName);
    }
    HttpRequest request = transport.buildRequest();
    request.method = method.httpMethod;
    HashMap<String, String> requestMap = new HashMap<String, String>();
    for (Map.Entry<String, Object> entry : DataUtil.mapOf(parameters)
        .entrySet()) {
      Object value = entry.getValue();
      if (value != null) {
        requestMap.put(entry.getKey(), value.toString());
      }
    }
    GenericUrl url = new GenericUrl(serviceDefinition.baseUrl);
    // parse path URL
    String pathUrl = method.pathUrl;
    StringBuilder pathBuf = new StringBuilder();
    int cur = 0;
    int length = pathUrl.length();
    while (cur < length) {
      int next = pathUrl.indexOf('{', cur);
      if (next == -1) {
        pathBuf.append(pathUrl.substring(cur));
        break;
      }
      pathBuf.append(pathUrl.substring(cur, next));
      int close = pathUrl.indexOf('}', next + 2);
      String varName = pathUrl.substring(next + 1, close);
      cur = close + 1;
      String value = requestMap.remove(varName);
      if (value == null) {
        throw new IllegalArgumentException("missing required path parameter: "
            + varName);
      }
      pathBuf.append(CharEscapers.escapeUriPath(value));
    }
    url.appendRawPath(pathBuf.toString());
    // all other parameters are assumed to be query parameters
    url.putAll(requestMap);
    request.url = url;
    return request;
  }
}
