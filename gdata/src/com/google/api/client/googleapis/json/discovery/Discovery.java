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

import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.googleapis.json.discovery.ServiceDocument.Parameter;
import com.google.api.client.googleapis.json.discovery.ServiceDocument.ServiceDefinition;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.util.GenericData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @since 2.2
 * @author vbarathan@google.com (Prakash Barathan)
 */
public class Discovery {

  private final ConcurrentHashMap<String, Class<? extends HttpContent>>
      contentTypeToSerializerMap =
      new ConcurrentHashMap<String, Class<? extends HttpContent>>();
  private final GoogleTransport transport;

  private final ServiceDefinition serviceDefinition;

  Discovery(ServiceDefinition serviceDefinition, GoogleTransport transport) {
    this.transport = transport;
    this.serviceDefinition = serviceDefinition;
  }

  public <T extends HttpContent> void setSerializer(String contentType,
      Class<T> serializerClazz) {
    this.contentTypeToSerializerMap.put(contentType, serializerClazz);
  }

  boolean isRestHeader(String parameter) {
    if ("content-type".equals(parameter)
        || "content-length".equals(parameter)) {
      return true;
    }
    return false;
  }

  boolean isItemRequest(String method) {
    return "update".equals(method) || "patch".equals(method)
        || "delete".equals(method);
  }

  HttpRequest buildRpcRequest(
      String resource, String method, GenericData params) {
    Map<String, String> headers = new HashMap<String, String>();
    String auth = null;
    GenericData body = new GenericData();
    GenericData bodyParam = new GenericData();

    body.set("method", method);
    for (Map.Entry<String, Object> param : params.entrySet()) {
      if ("auth".equals(param.getKey())) {
        auth = (String) param.getValue();
        continue;
      }
      bodyParam.set(param.getKey(), param.getValue());
    }
    body.set("param", bodyParam);
    // Create transport request
    HttpRequest request = transport.buildPostRequest();
    request.setUrl(resource);
    request.content = getSerializer("application/json", bodyParam);
    return request;
  }

  HttpRequest buildRestRequest(String methodName, GenericData params) {
    ServiceDocument.Method method =
      serviceDefinition.getResourceMethod(methodName);
    GoogleUrl uriEntity = getRestUrl(method, params);

    Map<String, String> headersMap = new HashMap<String, String>();
    Object body = null;
    String auth = null;
    String etag = null;
    for (Map.Entry<String, Object> param : params.entrySet()) {
      Object value = param.getValue();
      if (value == null) {
        continue;
      }
      String name = param.getKey();
      if ("auth".equals(name)) {
        auth = (String) value;
      } else if ("etag".equals(name)) {
        etag = (String) value;
      } else if ("content".equals(name)) {
        body = value;
      } else if (value instanceof String && isRestHeader(name)) {
        headersMap.put(name, (String) value);
      } else if (value instanceof String) {
        uriEntity.put(name, value);
      }
    }
    // build HTTP request
    HttpRequest request;
    String uri = uriEntity.build();
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
    request.setUrl(uri);
    // HTTP headers
    HttpHeaders headers = request.headers;
    if (etag != null) {
      headers.ifMatch = etag;
    }
    if (auth != null) {
      headers.authorization = auth;
    }
    headers.putAll(headersMap);
    // HTTP request body
    if (body != null) {
      @SuppressWarnings("unchecked")
      List<GenericData> contents = (List<GenericData>) body;
      // cannot handle more than two parts
      if (contents.size() == 0 || contents.size() > 2) {
        throw new RuntimeException(
            "Cannot handle 0 or more than 2 parts in request body");
      }

      HttpContent serializer;
      if (contents.size() == 1) {
        GenericData content = contents.get(0);
        serializer =
            getSerializer((String) content.get("type"), content.get("value"));
      } else {
        GenericData content = contents.get(0);
        HttpContent serializer1 =
            getSerializer((String) content.get("type"), content.get("value"));
        content = contents.get(1);
        HttpContent serializer2 =
            getSerializer((String) content.get("type"), content.get("value"));
        serializer = new MultipartContent(serializer1, serializer2);
      }
      request.content = serializer;
    }
    return request;
  }

  HttpResponse doRestRequest(String method, GenericData params)
      throws IOException {
    return buildRestRequest(method, params.clone()).execute();
  }

  <T> T doRestRequest(String method, GenericData params,
      Class<T> resultType) throws IOException {
    if (resultType == null) {
      buildRestRequest(method, params).execute();
      return null;
    }
    return buildRestRequest(method, params).execute().parseAs(
        resultType);
  }
  
  public String getRpcUrl(String methodName, GenericData paramValues) {
    if (serviceDefinition == null) {
      throw new RuntimeException("cannot determine request uri");
    }

    ServiceDocument.Method method =
        serviceDefinition.getResourceMethod(methodName);
    String url = serviceDefinition.baseUrl + "rpc?method=" + method.rpcName;
    
    for(Map.Entry<String, Parameter> param : method.parameters.entrySet()) {
      String paramName = param.getKey();
      String paramType = param.getValue().parameterType;
      if ("path".equals(paramType)) {
        url += "&" + paramName + "=" + paramValues.get(paramName);
        paramValues.remove(paramName);
      } else if ("query".equals(paramName)
          && paramValues.containsKey(paramName)) {
        url += "&" + paramName + "=" + paramValues.get(paramName);
        paramValues.remove(paramName);
      }
    }
    return url;
  }

  public GoogleUrl getRestUrl(
      ServiceDocument.Method method, GenericData paramValues) {
    if (serviceDefinition == null) {
      throw new RuntimeException("cannot determine request uri");
    }

    String url = serviceDefinition.baseUrl + method.pathUrl;
    for(Map.Entry<String, Parameter> param : method.parameters.entrySet()) {
      String paramName = param.getKey();
      String paramType = param.getValue().parameterType;
      if ("path".equals(paramType)) {
        if (!paramValues.containsKey(paramName)) {
          throw new IllegalStateException(
              "Required path parameter '" + paramName + "' not specified");
        }
        url = url.replace("{" + paramName + "}",
            (String) paramValues.get(paramName));
        paramValues.remove(paramName);
      }
    }
    GoogleUrl uri = new GoogleUrl(url);
    for(Map.Entry<String, Parameter> param : method.parameters.entrySet()) {
      String paramName = param.getKey();
      String paramType = param.getValue().parameterType;
      if ("query".equals(paramType)
          && paramValues.containsKey(paramName)) {
        uri.put(paramName, paramValues.get(paramName));
        paramValues.remove(paramName);
      }
    }
    
    // Handle user specified query parameters
    for (String paramName : paramValues.unknownFields.keySet()) {
      if (!(paramValues.get(paramName) instanceof String)) {
        // skip complex types
        continue;
      }
      if (method.parameters.containsKey(paramName)
          && "query".equals(method.parameters.get(paramName).parameterType)) {
        uri.put(paramName, paramValues.get(paramName));
        paramValues.remove(paramName);
      } else if (paramName.startsWith("q:")) {
        uri.put(paramName.substring(2), paramValues.get(paramName));
        paramValues.remove(paramName);
      }
    }
    
    return uri;
  }

  public HttpContent getSerializer(String contentType, Object content) {
    if (contentType == null) {
      return null;
    }
    if (content instanceof String) {
      content = new ByteArrayInputStream(((String) content).getBytes());
    }
    if (content instanceof InputStream) {
      InputStreamContent streamContent = new InputStreamContent();
      streamContent.inputStream = (InputStream) content;
      streamContent.type = contentType;
      return streamContent;
    }
    int semicolon = contentType.indexOf(';');
    if (semicolon != -1) {
      contentType = contentType.substring(0, semicolon);
    }
    Class<? extends HttpContent> clazz =
        this.contentTypeToSerializerMap.get(contentType);
    if (clazz == null) {
      throw new RuntimeException("Dont know how to generate for: "
          + contentType);
    }
    HttpContent serializer = null;
    try {
      serializer =
          clazz.getConstructor(new Class[] {Object.class}).newInstance(content);
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (serializer == null) {
      throw new RuntimeException("content is not serializable");
    }
    return serializer;
  }
}
