// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.request;

import com.google.api.data.client.http.LowLevelHttpTransportInterface;
import com.google.api.data.client.http.net.NetGData;
import com.google.api.data.client.v2.GDataEntity;

import org.json.JSONException;

/**
 * Creates requests with default set of parameters.
 *
 * @author vbarathan@google.com (Parakash Barathan)
 */
public class ApiRequestFactory {

  public static class Builder {

    private String resource;
    private ServiceDocument serviceDoc = null;

    private LowLevelHttpTransportInterface transport;

    private Entity paramMap = new Entity();
    private Object paramObject;

    public Builder() {
    }

    public Builder rpcService(String rpcServiceUri) {
      this.resource = rpcServiceUri;
      return this;
    }

    public Builder restService(ServiceDocument doc) {
      this.serviceDoc = doc;
      return this;
    }

    public Builder transport(LowLevelHttpTransportInterface transport) {
      this.transport = transport;
      return this;
    }

    public Builder with(Object params) {
      this.paramObject = params;
      return this;
    }

    public Builder with(String key, String value) {
      paramMap.put(key, value);
      return this;
    }

    public ApiRequestFactory build() throws JSONException {
      // TODO(vbarathan): merge paramObject and paramMap
      if (transport == null) {
        transport = NetGData.HTTP_TRANSPORT;
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
  private LowLevelHttpTransportInterface transport;

  private Entity paramMap = new Entity();
  private Object paramObject;
  private Discovery discovery;
  
  private ApiRequestFactory(Discovery discovery, Entity params) {
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

  public ApiRequest<GDataEntity> insert(String resource, Entity data) {
    return request(resource, "insert").with("content", data);
  }

  public ApiRequest<GDataEntity> update(GDataEntity data) {
    String editUri = (String) ((GDataEntity) data.get("links")).get("edit");
    return request(editUri, "update").with("content", data);
  }

  public ApiRequest<GDataEntity> delete(GDataEntity data) {
    String editUri = (String) ((GDataEntity) data.get("links")).get("edit");
    return request(editUri, "delete");
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
