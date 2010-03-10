// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.request;

import com.google.api.data.client.http.HttpTransport;
import com.google.api.data.client.http.net.NetGData;
import com.google.api.data.client.v2.GDataEntity;

import org.json.JSONException;

/**
 * Creates requests with default set of parameters.
 *
 * @author vbarathan@google.com (Parakash Barathan)
 */
public class RequestFactory {

  public static class Builder {

    private String resource;
    private ServiceDocument serviceDoc = null;

    private HttpTransport transport;

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

    public Builder transport(HttpTransport transport) {
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

    public RequestFactory build() throws JSONException {
      // TODO(vbarathan): merge paramObject and paramMap
      if (transport == null) {
        transport = NetGData.HTTP_TRANSPORT;
      }
      if (serviceDoc != null) {
        return new RequestFactory(
            new Discovery(serviceDoc, transport), paramMap);
      }
      return new RequestFactory(
          new Discovery(resource, transport), paramMap);
    }
  }

  private String resource = null;
  private ServiceDocument serviceDoc = null;
  private HttpTransport transport;

  private Entity paramMap = new Entity();
  private Object paramObject;
  private Discovery discovery;
  
  private RequestFactory(Discovery discovery, Entity params) {
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

  public Request<GDataEntity> request(String method) {
    return new Request<GDataEntity>(discovery, method, paramMap);
  }

  public Request<GDataEntity> request(String resource, String method) {
    return new Request<GDataEntity>(discovery, resource, method, paramMap);
  }

  public Request<GDataEntity> query(String resource) {
    return request(resource, "query");
  }

  public Request<GDataEntity> insert(String resource, Entity data) {
    return request(resource, "insert").with("content", data);
  }

  public Request<GDataEntity> update(GDataEntity data) {
    String editUri = (String) ((GDataEntity) data.get("links")).get("edit");
    return request(editUri, "update").with("content", data);
  }

  public Request<GDataEntity> delete(GDataEntity data) {
    String editUri = (String) ((GDataEntity) data.get("links")).get("edit");
    return request(editUri, "delete");
  }

  public Request<GDataEntity> batch(String resource) {
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
      return cls.getConstructor(RequestFactory.class).newInstance(this);
    } catch (Exception e) {
      throw new RuntimeException(
          "Could not call appropriate constructor for service " + cls, e);
    }
  }

}
