package com.google.api.data.client.v2.request;

import com.google.api.data.client.v2.GDataEntity;
import com.google.api.data.client.http.HttpRequest;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.http.Response;
import com.google.api.data.client.http.Request;
import com.google.api.data.client.http.Transport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Discovery {
  
  private final Transport transport = new Transport("sample", null);
  private final ServiceDocument serviceDoc;
  private final String resource;

  Discovery(ServiceDocument serviceDoc, HttpTransport transport) {
    Transport.httpTransport = transport;
    this.serviceDoc = serviceDoc;
    this.resource = null;
  }
  
  Discovery(String resource, HttpTransport transport) {
    Transport.httpTransport = transport;
    this.resource = resource;
    this.serviceDoc = null;
  }
    
  boolean isRestHeader(String parameter) {
    if ("content-type".equals(parameter)
        || "content-length".equals(parameter)) {
      return true;
    }
    return false;
  }
  
  Response doRestRequest(
      String resource, String method, Entity params) throws IOException {
    String uri = getUrl(resource, params);
    
    // separate headers, query parameters and content body
    // TODO(vbarathan): determine this from discovery doc.  for now hardcoded.
    Map<String, String> headers = new HashMap();
    Object body = null;
    StringBuilder query = null;
    for (Map.Entry<String, Object> param : params.entrySet()) {
      if (param.getValue() instanceof String  && isRestHeader(param.getKey())) {
        headers.put(param.getKey(), (String) param.getValue());
      } else if ("content".equals(param.getKey())){
        body = param.getValue();
      } else if (param.getValue() instanceof String) {
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

    // Create transport request
    Request request;
    if ("query".equals(method)) {
      request = transport.buildGetRequest(uri);
    } else if ("insert".equals(method)) {
      request = transport.buildPostRequest(uri);
    } else if ("update".equals(method)) {
      request = transport.buildPutRequest(uri);
    } else if ("delete".equals(method)) {
      request = transport.buildDeleteRequest(uri);
    } else {
      throw new RuntimeException("Unknown REST method");
    }
    
    for(Map.Entry<String, String> header : headers.entrySet()) {
      request.addHeader(header.getKey(), header.getValue());
    }
    
    if (body != null) {
      // TODO: generate content body
    }
    
    return request.execute();
  }
  
  public String getUrl(String method, Entity params) {
    if (resource != null) {
      return resource;
    }
    String url;
    if (method.startsWith("http")) {
      url = method;
    } else if (serviceDoc != null) {
      url = serviceDoc.getResourceUrl(method);
    } else {
      throw new RuntimeException("cannot determine request uri");
    }
    
    Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
    Matcher matcher = pattern.matcher(url);
    while (matcher.find()) {
      String var = matcher.group(1);
      url = url.replace("{" + var + "}", (String) params.remove(var));
    }
    return url;
  }
}
