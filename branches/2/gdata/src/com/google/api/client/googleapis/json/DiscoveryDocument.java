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

import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.DataUtil;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

import org.codehaus.jackson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class representing a Google API Service description document.  This is java
 * representation of Google API Service discovery from JSON format.
 *
 * @since 2.2
 * @author vbarathan@google.com (Prakash Barathan)
 */
public class DiscoveryDocument {
  
  /** API discovery url */
  public static final String DEFAULT_DISCOVERY_URL =
    "http://www.googleapis.com/discovery/0.1/describe";
  
  /** Service name query parameter for discovery request */
  public static final String API_QUERY_PARAMETER = "api";
  
  /** Service version query parameter for discovery request */
  public static final String API_VERSION_QUERY_PARAMETER = "apiVersion";
  
  /** List of standard query parameters supported by all services */
  public static final List<String> STANDARD_QUERY_PARAMETERS =
      Arrays.asList("alt", "prettyprint");
  
  /** List of standard header parameters supported by all services */
  public static final List<String> STANDARD_HEADER_PARAMETERS =
      Arrays.asList("Etag");
  
  /**
   * Map of Service name to supported Service Version definitions
   */
  public static class ServiceDefinitions extends ArrayMap<String, Versions> {
  }
  
  /**
   * Map of Service Version to Service Version definition 
   */
  public static class Versions extends ArrayMap<String, ServiceDefinition> {
  }

  /**
   *  Version specific Service Definition for a service
   */
  public static class ServiceDefinition {
    /** Base url for service endpoint */
    @Key
    public String baseUrl;
    
    /** List of supported resources */
    @Key
    public Map<String, Resource> resources;

    /**
     * Returns {@link Method} definition for given method name.
     * Method identifier is of format "resourceName.methodName" */
    public Method getResourceMethod(String methodIdentifier) {
      String resourceName = methodIdentifier.substring(
          0, methodIdentifier.indexOf("."));
      String methodName = methodIdentifier.substring(
          methodIdentifier.indexOf(".") + 1);

      Resource resource = resources.get(resourceName);
      return resource.methods.get(methodName);
    }

    /**
     * Returns url for requested method.
     * Method identifier is of format "resourceName.methodName" */
    public String getResourceUrl(String methodIdentifier) {
      return baseUrl + getResourceMethod(methodIdentifier).pathUrl;
    }
  }

  /**
   * Defines an available resource in a service.
   */
  public static class Resource {
    @Key
    public Map<String, Method> methods;
  }
  
  /**
   * Defines a {key, value} map of parameters.
   */
  public static class Parameter extends GenericData {
    @Key
    public String parameterType;
  }

  /**
   * Defines an available method for a service. 
   */
  public static class Method {
    /** Method parth url relative to base url */
    @Key
    public String pathUrl;
    
    /** Method's rpc name */
    @Key 
    public String rpcName;
    
    /** Method's Http verb name */
    @Key
    public String httpMethod;
    
    /** Method type. One of http or rpc */
    @Key
    public String methodType;
    
    /** Parameters supported by the method */
    @Key
    public Map<String, Parameter> parameters;
  }
  
  /** API service definition parsed from discovery document */
  private final ServiceDefinition serviceDefinition;
  
  /** Default {@link HttpTransport} to use for API requests */
  public HttpTransport transport;
  
  /** Default request parameters to include in all requests */
  public GenericData defaultRequestParameters = new GenericData();
  
  /**
   * Creates a {@link DiscoveryDocument} for requested service version.
   * 
   * @param api name of api service.  not {@code null}
   */
  public DiscoveryDocument(String api) {
    this(api, null);
  }
  /**
   * Creates a {@link DiscoveryDocument} for requested service version.
   * 
   * @param api name of api service. not {@code null}
   * @param apiVersion api version. {@code null} to select latest version
   */
  public DiscoveryDocument(String api, String apiVersion) {
    try {
      GenericUrl discoveryUrl = new GenericUrl(DEFAULT_DISCOVERY_URL);
      discoveryUrl.put(API_QUERY_PARAMETER, api);
      discoveryUrl.put(API_VERSION_QUERY_PARAMETER, apiVersion);
      
      HttpTransport transport = new HttpTransport();
      HttpRequest request = transport.buildGetRequest();
      request.url = discoveryUrl;
      ServiceDefinitions definitions = new ServiceDefinitions();
      Json.parseAndClose(
          createJsonParser(request.execute().getContent(), "data"),
          definitions, null);
      Versions versions = definitions.get(api);
      if (apiVersion  == null) {
        apiVersion = versions.getKey(versions.size() - 1);
      }
      serviceDefinition = definitions.get(api).get(apiVersion);
    } catch (IOException ie) {
      throw new RuntimeException(ie);
    }
  }

  /** 
   * Creates a {@link DiscoveryDocument} from JSON discovery document string.
   * discoveryDocument is expected to have exactly one service definition and
   * one version.
   * 
   * @param discoveryDocument full discovery document json string
   */
  public DiscoveryDocument(InputStream discoveryDocument) {
    try {
      ServiceDefinitions definitions = new ServiceDefinitions();
      Json.parseAndClose(
          createJsonParser(discoveryDocument, "data"), definitions, null);
      serviceDefinition = definitions.getValue(0).getValue(0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Creates a {@link HttpRequest} for an api method.
   * 
   * @param fullyQualifiedMethodName name of method as defined in discovery
   *            document of format "resourceName.methodName"
   * @return {@link HttpRequest}
   */
  public HttpRequest createRequest(String fullyQualifiedMethodName) {
    return createRequest(fullyQualifiedMethodName, (GenericData) null);
  }
  
  /**
   * Creates a {@link HttpRequest} for an api method with method parameters
   * specified as JSON string.
   * 
   * @param fullyQualifiedMethodName name of method as defined in discovery
   *           document of format "resourceName.methodName"
   * @param requestParameters user defined key, value parameters in JSON format
   * @return {@link HttpRequest}
   */
  public HttpRequest createRequest(String fullyQualifiedMethodName,
      String requestParameters) throws IOException {
    GenericData requestParametersData = 
      Json.parse(
          createJsonParser(new ByteArrayInputStream(
              requestParameters.getBytes()), null),
          GenericJson.class, null);
    return createRequest(fullyQualifiedMethodName, requestParametersData);
  }
  
  /**
   * Creates a {@link HttpRequest} for specified api method with specified
   * method parameters.
   * 
   * @param fullyQualifiedMethodName name of method as defined in
   *           Discovery document of format "resourceName.methodName" 
   * @param requestParameters user defined key, value parameters to populate the
   *           request. not {@code null}.
   * @return {@link HttpRequest}
   */
  public HttpRequest createRequest(String fullyQualifiedMethodName,
      GenericData requestParameters) {
    // Preconditions
    if (transport == null) {
      throw new IllegalStateException("Transport is not specified.");
    }
    
    // Merge request specific parameters with default parameters
    GenericData allParameters = defaultRequestParameters.clone();
    if (requestParameters != null) {
      allParameters.putAll(DataUtil.mapOf(requestParameters));
    }
    
    // Create request for specified method
    Method method = serviceDefinition.getResourceMethod(
        fullyQualifiedMethodName);
    if (!"rest".equals(method.methodType)) {
      throw new IllegalStateException("Attempt to build REST request for a"
          + " non-rest method '" + fullyQualifiedMethodName + "'");
    }
    
    HttpRequest request;
    String restMethod = method.httpMethod;
    if ("GET".equalsIgnoreCase(restMethod)) {
      request = transport.buildGetRequest();
    } else if ("POST".equalsIgnoreCase(restMethod)) {
      request = transport.buildPostRequest();
    } else if ("PUT".equalsIgnoreCase(restMethod)) {
      request = transport.buildPutRequest();
    } else if ("DELETE".equalsIgnoreCase(restMethod)) {
      request = transport.buildDeleteRequest();
    } else {
      throw new RuntimeException("Unknown REST method: " + restMethod);
    }
    
    request.url = getRestRequestUrl(method, allParameters);
    appendQueryParameters(method, allParameters, request.url);
    addHeaders(method, allParameters, request);
    return request;
  }
  
  /** Parses given json string to GenericJson instnace */
  public static GenericJson parseFromJsonString(String json, String skipToKey)
      throws IOException {
    return Json.parse(
        createJsonParser(new ByteArrayInputStream(json.getBytes()), null),
        GenericJson.class, null);
  }
  
  private GenericUrl getRestRequestUrl(
      Method method, GenericData requestParameters) {
    String methodUrl = serviceDefinition.baseUrl + method.pathUrl;
    for (Map.Entry<String, Parameter> param: method.parameters.entrySet()) {
      String paramName = param.getKey();
      String paramType = param.getValue().parameterType;
      if ("path".equals(paramType)) {
        if (requestParameters == null
            || !requestParameters.containsKey(paramName)) {
          throw new IllegalStateException(
              "Required path parameter '" + paramName + "' not specified");
        }
        methodUrl = methodUrl.replace("{" + paramName + "}",
            (String) requestParameters.get(paramName));
      }      
    }
    return new GoogleUrl(methodUrl);
  }
  
  private void appendQueryParameters(
      Method method, GenericData requestParameters, GenericUrl baseUrl) {
    // map query parameters from discovery document
    for (Map.Entry<String, Parameter> param: method.parameters.entrySet()) {
      String paramName = param.getKey();
      String paramType = param.getValue().parameterType;
      if ("query".equals(paramType)) {
        if (requestParameters.containsKey(paramName)) {
          baseUrl.put(paramName, requestParameters.get(paramName));
          continue;
        }
      }
    }
    // map standard query parameters
    for (String param: STANDARD_QUERY_PARAMETERS) {
      if (requestParameters.containsKey(param)) {
        baseUrl.put(param, requestParameters.get(param));
      }
    }
  }
  
  private void addHeaders(
      Method method, GenericData requestParameters, HttpRequest request) {
    // map headers from discovery document
    for (Map.Entry<String, Parameter> param: method.parameters.entrySet()) {
      String paramName = param.getKey();
      String paramType = param.getValue().parameterType;
      if ("header".equals(paramType)) {
        if (requestParameters.containsKey(paramName)) {
          request.headers.put(paramName, requestParameters.get(paramName));
        }
      }      
    }
    // map standard header parameters
    for (String param: STANDARD_HEADER_PARAMETERS) {
      if (requestParameters.containsKey(param)) {
        request.headers.put(param, requestParameters.get(param));
      }
    }
  }

  private static JsonParser createJsonParser(
      InputStream content, String skipToKey)
      throws IOException {
    try {
      JsonParser parser = Json.JSON_FACTORY.createJsonParser(content);
      content = null;
      parser.nextToken();
      if (skipToKey != null) {
        Json.skipToKey(parser, skipToKey);
      }
      return parser;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

}
