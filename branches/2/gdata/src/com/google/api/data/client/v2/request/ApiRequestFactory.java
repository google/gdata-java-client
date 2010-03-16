// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.request;

import com.google.api.data.client.auth.Authorizer;
import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.http.LowLevelHttpTransportInterface;
import com.google.api.data.client.http.net.NetGData;
import com.google.api.data.client.v2.GDataEntity;
import com.google.api.data.client.v2.jsonc.jackson.JacksonHttpParser;
import com.google.api.data.client.v2.jsonc.jackson.JsoncSerializer;

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
      HttpTransport newTransport;
      if (transport == null) {
        newTransport = new HttpTransport(
            appName != null ? appName : "Apiary_Java");
        newTransport.lowLevelHttpTransportInterface =
            lowLevelTransport != null ? lowLevelTransport :
            NetGData.HTTP_TRANSPORT;
        JacksonHttpParser.set(newTransport);
      } else {
        newTransport = transport;
      }
      Discovery discovery = null;
      if (serviceDoc != null) {
        discovery = new Discovery(serviceDoc, newTransport);
      } else {
        discovery = new Discovery(resource, newTransport);
      }
      discovery.setSerializer("application/json", JsoncSerializer.class);
      return new ApiRequestFactory(discovery, paramMap);
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
    return new ApiRequest<GDataEntity>(discovery, method, paramMap)
        .returning(GDataEntity.class);
  }

  public ApiRequest<GDataEntity> request(String resource, String method) {
    return new ApiRequest<GDataEntity>(discovery, resource, method, paramMap);
  }

  public ApiRequest<GDataEntity> query(String resource) {
    return request(resource, "query").returning(GDataEntity.class);
  }

  public ApiRequest<GDataEntity> insert(String resource, Object data) {
    return request(resource, "insert").withContent(data)
        .returning(GDataEntity.class);
  }

  public ApiRequest<GDataEntity> update(String resource, Object data) {
    return request(resource, "update").withContent(data)
        .returning(GDataEntity.class);
  }

  public ApiRequest<GDataEntity> delete(String resource) {
    return request(resource, "delete");
  }

  public ApiRequest<GDataEntity> batch(String resource) {
    return request(resource, "batch").returning(GDataEntity.class);
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
