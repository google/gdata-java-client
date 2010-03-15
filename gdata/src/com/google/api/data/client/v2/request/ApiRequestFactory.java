// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.request;

import com.google.api.data.client.auth.Authorizer;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.http.LowLevelHttpTransportInterface;
import com.google.api.data.client.http.net.NetGData;
import com.google.api.data.client.v2.GDataEntity;
import com.google.api.data.client.v2.jsonc.jackson.JacksonHttpParser;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates requests with default set of parameters.
 *
 * @author vbarathan@google.com (Parakash Barathan)
 */
public class ApiRequestFactory {

  public static class Builder {

    private String appName;
    private Authorizer auth;
    private String resource;
    private ServiceDocument serviceDoc = null;

    private LowLevelHttpTransportInterface lowLevelTransport;
    private HttpTransport transport;

    private GDataEntity paramMap = new GDataEntity();

    public Builder() {
    }
    
    public Builder application(String appName) {
      this.appName = appName;
      return this;
    }

    public Builder rpcService(String rpcServiceUri) {
      this.resource = rpcServiceUri;
      return this;
    }

    public Builder restService(ServiceDocument doc) {
      this.serviceDoc = doc;
      return this;
    }

    public Builder transport(LowLevelHttpTransportInterface lowLevelTransport) {
      this.lowLevelTransport = lowLevelTransport;
      return this;
    }
    
    public Builder transport(HttpTransport transport) {
      this.transport = transport;
      return this;
    }

    public Builder with(String key, String value) {
      paramMap.set(key, value);
      return this;
    }
    
    public Builder withAuth(Authorizer auth) {
      paramMap.set("auth", auth);
      return this;
    }

    public ApiRequestFactory build() {
      if (transport == null) {
        transport = new HttpTransport(
            appName != null ? appName : "Apiary_Java");
        transport.lowLevelHttpTransportInterface =
            lowLevelTransport != null ? lowLevelTransport :
            NetGData.HTTP_TRANSPORT;
        JacksonHttpParser.set(transport);
      }
      if (serviceDoc != null) {
        return new ApiRequestFactory(
            new Discovery(serviceDoc, transport), paramMap);
      }
      return new ApiRequestFactory(
          new Discovery(resource, transport), paramMap);
    }
  }

  private String resource = null;
  private ServiceDocument serviceDoc = null;
  private GDataEntity paramMap = new GDataEntity();
  private Discovery discovery;
  
  private ApiRequestFactory(Discovery discovery, GDataEntity params) {
    this.discovery = discovery;
    this.paramMap = params;
  }

  private String getRequestUri(String method) {
    if (method.startsWith("http")) {
      return method;
    } else if (serviceDoc != null) {
      return serviceDoc.getResourceUrl(method);
    }
    return resource;
  }

  public ApiRequest<GDataEntity> request(String method) {
    return new ApiRequest<GDataEntity>(discovery, method, paramMap);
  }

  public ApiRequest<GDataEntity> request(String resource, String method) {
    return new ApiRequest<GDataEntity>(discovery, resource, method, paramMap);
  }

  public ApiRequest<GDataEntity> query(String resource) {
    return request(resource, "query");
  }

  public ApiRequest<GDataEntity> insert(String resource, Object data) {
    return request(resource, "insert").withContent(data);
  }

  public ApiRequest<GDataEntity> update(String resource, Object data) {
    return request(resource, "update").withContent(data);
  }

  public ApiRequest<GDataEntity> delete(String resource) {
    return request(resource, "delete");
  }

  public ApiRequest<GDataEntity> batch(String resource) {
    return request(resource, "batch");
  }

  /**
   * Create a typed service.
   *
   * @param cls Class of the service
   * @return The service
   */
  public <T> T service(Class<T> cls) {
    try {
      return cls.getConstructor(ApiRequestFactory.class).newInstance(this);
    } catch (Exception e) {
      throw new RuntimeException(
          "Could not call appropriate constructor for service " + cls, e);
    }
  }

}
