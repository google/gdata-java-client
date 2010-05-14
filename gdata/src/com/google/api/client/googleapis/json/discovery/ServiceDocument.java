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

package com.google.api.client.googleapis.json.discovery;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;

import org.codehaus.jackson.JsonParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Class representing a Google API Service description document.
 *
 * @since 2.2
 * @author vbarathan@google.com (Prakash Barathan)
 */
public class ServiceDocument {

  public static class ServiceDefinitions extends ArrayMap<String, Versions> {
  }
  
  public static class Versions extends ArrayMap<String, ServiceDefinition> {
  }

  ServiceDefinitions serviceDefinitions =
      new ServiceDefinitions();

  public static class ServiceDefinition {
    @Key public String baseUrl;
    @Key public Map<String, Resource> resources;

    public Method getResourceMethod(String methodName) {
      String collection = methodName.substring(0, methodName.indexOf("."));
      String method = methodName.substring(methodName.indexOf(".") + 1);

      Resource resource = resources.get(collection);
      return resource.methods.get(method);
    }

    public String getResourceUrl(String resourceName) {
      return getResourceMethod(resourceName).pathUrl;
    }
  }

  public static class Resource {
    @Key public Map<String, Method> methods;
  }

  public static class Parameter extends GenericData {
    @Key public String parameterType;
  }

  public static class Method {
    @Key public String pathUrl;
    @Key public String rpcName;
    @Key public String httpMethod;
    @Key public String methodType;
    @Key public Map<String, Parameter> parameters;
  }

  public ServiceDocument(String resource) {
    try {
      if (resource.startsWith("http")) { 
        HttpTransport transport = new HttpTransport();
        HttpRequest request = transport.buildGetRequest();
        request.setUrl(resource);
        Json.parseAndClose(
            processAsInputStream(request.execute().parseAsString()),
            serviceDefinitions, null);
      } else {
        Json.parseAndClose(
            processAsInputStream(resource), serviceDefinitions, null);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean hasService(String name) {
    return serviceDefinitions.containsKey(name);
  }

  public ServiceDefinition getServiceDefinition(
      String serviceName, String version) {
    Versions serviceVersions = serviceDefinitions.get(serviceName);
    if (version ==  null) {
      return serviceVersions.getValue(serviceVersions.size() - 1);
    }
    return serviceVersions.get(version);
  }

  public static JsonParser processAsInputStream(String jsonString)
      throws IOException {
    InputStream content = new ByteArrayInputStream(jsonString.getBytes());
    try {
      JsonParser parser = Json.JSON_FACTORY.createJsonParser(content);
      content = null;
      parser.nextToken();
      Json.skipToKey(parser, "data");
      return parser;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

}
