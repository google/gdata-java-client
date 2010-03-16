package com.google.api.data.client.v2.request;

import com.google.api.data.client.auth.AuthorizedRequest;
import com.google.api.data.client.auth.Authorizer;
import com.google.api.data.client.http.HttpRequest;
import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.http.HttpSerializer;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.http.InputStreamHttpSerializer;
import com.google.api.data.client.http.MultipartHttpSerializer;
import com.google.api.data.client.v2.GDataEntity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Discovery {

  private final ConcurrentHashMap<String, Class<? extends HttpSerializer>>
  contentTypeToSerializerMap
      = new ConcurrentHashMap<String, Class<? extends HttpSerializer>>();
  private HttpTransport transport = new HttpTransport("sample");
  
  private final ServiceDocument serviceDoc;
  private final String resource;

  Discovery(ServiceDocument serviceDoc, HttpTransport transport) {
    this.transport = transport;
    this.serviceDoc = serviceDoc;
    this.resource = null;
  }
  
  Discovery(String resource, HttpTransport transport) {
    this.transport = transport;
    this.resource = resource;
    this.serviceDoc = null;
  }
  
  public <T extends HttpSerializer> void setSerializer(
      String contentType, Class<T> serializerClazz) {
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
    return "update".equals(method)
        || "patch".equals(method)
        || "delete".equals(method);
  }
  
  HttpRequest buildRestRequest(
      String resource, String method, GDataEntity params) throws IOException {
    String uri = getUrl(resource, method, params);
    
    // separate headers, query parameters and content body
    // TODO(vbarathan): determine this from discovery doc.  for now hardcoded.
    Map<String, String> headers = new HashMap<String, String>();
    Object body = null;
    Authorizer auth = null;
    String etag = null;
    StringBuilder query = null;
    for (Map.Entry<String, Object> param :
          params.getUnknownKeyMap().entrySet()) {
      if (param.getValue() == null) {
        continue;
      }
      if ("auth".equals(param.getKey())) {
        auth = (Authorizer) param.getValue();
      } else if ("etag".equals(param.getKey())) {
        etag = (String) param.getValue();
      } else if ("content".equals(param.getKey())){
        body = param.getValue();
      } else if (param.getValue() instanceof String
          && isRestHeader(param.getKey())) {
        headers.put(param.getKey(), (String) param.getValue());
      } else  if (param.getValue() instanceof String) {
        // TODO(vbarathan): exclude duplicate query parameters
        if (uri.contains(param.getKey() + "=" + param.getValue())) {
          continue;
        }
        if (query == null) {
          query = uri.contains("?") ?
              new StringBuilder("&") : new StringBuilder("?");
        } else {
          query.append("&");
        }
        query.append(param.getKey()).append("=").append(param.getValue());
      }
    }
    if (query != null) {
      uri += query;
    }
    
    String httpMethod = null;
    if ("query".equals(method)) {
      httpMethod = "GET";
    } else if ("insert".equals(method)) {
      httpMethod = "POST";
    } else if ("update".equals(method)) {
      httpMethod = "PUT";
    } else if ("patch".equals(method)) {
      httpMethod = "PATCH";
    } else if ("delete".equals(method)) {
      httpMethod = "DELETE";
    } else {
      throw new RuntimeException("Unknown REST method: " + method);
    }
    
    if (etag != null && isItemRequest(method)) {
      headers.put("If-Match", etag);
    }
    
    // Authenticate if required
    if (auth != null) {
      AuthorizedRequest authRequest = auth.getAuthorizedRequest(httpMethod, uri);
      uri = authRequest.getUri();
      for (int i = 0; i < authRequest.getHeaderCount(); i++) {
        headers.put(
            authRequest.getHeaderName(i), authRequest.getHeaderValue(i));
      }
    }
    
    // Create transport request
    HttpRequest request = transport.buildRequest(httpMethod, uri);
    for(Map.Entry<String, String> header : headers.entrySet()) {
      request.addHeader(header.getKey(), header.getValue());
    }
    
    if (body != null) {
      @SuppressWarnings("unchecked")
      List<GDataEntity> contents = (List<GDataEntity>) body;
      // cannot handle more than two parts
      if (contents.size() == 0 || contents.size() > 2) {
        throw new RuntimeException(
            "Cannot handle 0 or more than 2 parts in request body");
      }
      
      HttpSerializer serializer;
      if (contents.size() == 1) {
        GDataEntity content = contents.get(0);
        serializer = getSerializer(
            (String) content.get("type"), content.get("value"));
      } else {
        GDataEntity content = contents.get(0);
        HttpSerializer serializer1 = getSerializer(
            (String) content.get("type"), content.get("value"));
        content = contents.get(1);
        HttpSerializer serializer2 = getSerializer(
            (String) content.get("type"), content.get("value"));
        serializer = new MultipartHttpSerializer(serializer1, serializer2);
      }
      request.setContent(serializer);
    }
    return request;
  }
  
  HttpResponse doRestRequest(
      String resource, String method, GDataEntity params) throws IOException {
    return buildRestRequest(resource, method, params).execute();
  }
  
  <T> T doRestRequest(
      String resource, String method, GDataEntity params, Class<T> resultType)
      throws IOException {
    if (resultType == null) {
      buildRestRequest(resource, method, params).execute();
      return null;
    }
    return buildRestRequest(resource, method, params).execute(resultType);
  }
  
  <T> T doRequest(
      String resource, String method, GDataEntity params, Class<T> resultType)
      throws IOException {
    // determine REST versus RPC request
    // if rest request
    return doRestRequest(resource, method, params, resultType);
    
    // TODO(vbarathan): RPC request
  }
  
  public String getUrl(String resource, String method, GDataEntity params) {
    String url;
    if (resource.startsWith("http")) {
      url = resource;
    } else if (serviceDoc != null) {
      if (isItemRequest(method)) {
        url = serviceDoc.getResourceItemUrl(resource);
      } else {
        url = serviceDoc.getResourceUrl(resource);
      }
    } else {
      throw new RuntimeException("cannot determine request uri");
    }
    
    Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
    Matcher matcher = pattern.matcher(url);
    System.out.println(url);
    while (matcher.find()) {
      String var = matcher.group(1);
      if (params.get(var) == null) {
        throw new RuntimeException(
            "Required parameter '" + var + "' is not defined.");
      }
      System.out.println(var);
      url = url.replace("{" + var + "}", (String) params.get(var));
      params.set(var, null);
    }
    return url;
  }
  
  public HttpSerializer getSerializer(String contentType, Object content) {
    if (contentType == null) {
      return null;
    }
    if (content instanceof InputStream) {
      return new InputStreamHttpSerializer(
          (InputStream) content, -1, contentType, null);
    }
    int semicolon = contentType.indexOf(';');
    if (semicolon != -1) {
      contentType = contentType.substring(0, semicolon);
    }
    Class<? extends HttpSerializer> clazz =
      this.contentTypeToSerializerMap.get(contentType);
    if (clazz == null) {
      throw new RuntimeException(
          "Dont know how to generate for: " + contentType);
    }
    HttpSerializer serializer = null;
    try {
      serializer = clazz.getConstructor(
          new Class[] {Object.class}).newInstance(content);
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
