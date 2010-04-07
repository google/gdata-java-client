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

package com.google.api.client.http.discovery;

import com.google.api.client.Entity;
import com.google.api.client.http.LowLevelHttpTransport;
import com.google.api.client.http.googleapis.GoogleTransport;
import com.google.api.client.http.json.googleapis.JsonHttpParser;
import com.google.api.client.http.json.googleapis.JsonSerializer;
import com.google.api.client.http.net.NetGData;

/**
 * Creates requests with default set of parameters.
 *
 * @author vbarathan@google.com (Parakash Barathan)
 */
public class ApiRequestFactory {

  public static class Builder {

    private String appName;
    private String resource;
    private ServiceDocument serviceDoc = null;

    private LowLevelHttpTransport lowLevelTransport;
    private GoogleTransport transport;

    private Entity paramMap = new Entity();

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

    public Builder transport(LowLevelHttpTransport lowLevelTransport) {
      this.lowLevelTransport = lowLevelTransport;
      return this;
    }
    
    public Builder transport(GoogleTransport transport) {
      this.transport = transport;
      return this;
    }

    public Builder with(String key, String value) {
      paramMap.set(key, value);
      return this;
    }
    
    public ApiRequestFactory build() {
      GoogleTransport newTransport;
      if (transport == null) {
        newTransport = new GoogleTransport(
            appName != null ? appName : "Apiary_Java");
        newTransport.lowLevelHttpTransport =
            lowLevelTransport != null ? lowLevelTransport :
            NetGData.HTTP_TRANSPORT;
        newTransport.setParser(new JsonHttpParser());
      } else {
        newTransport = transport;
      }
      Discovery discovery = null;
      if (serviceDoc != null) {
        discovery = new Discovery(serviceDoc, newTransport);
      } else {
        discovery = new Discovery(resource, newTransport);
      }
      discovery.setSerializer("application/json", JsonSerializer.class);
      return new ApiRequestFactory(discovery, paramMap);
    }
  }

  private String resource = null;
  private ServiceDocument serviceDoc = null;
  private Entity paramMap = new Entity();
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

  public ApiRequest<Entity> request(String method) {
    return new ApiRequest<Entity>(discovery, method, paramMap)
        .returning(Entity.class);
  }

  public ApiRequest<Entity> request(String resource, String method) {
    return new ApiRequest<Entity>(discovery, resource, method, paramMap);
  }

  public ApiRequest<Entity> query(String resource) {
    return request(resource, "query").returning(Entity.class);
  }

  public ApiRequest<Entity> insert(String resource, Object data) {
    return request(resource, "insert").withContent(data)
        .returning(Entity.class);
  }

  public ApiRequest<Entity> update(String resource, Object data) {
    return request(resource, "update").withContent(data)
        .returning(Entity.class);
  }

  public ApiRequest<Entity> delete(String resource) {
    return request(resource, "delete");
  }

  public ApiRequest<Entity> batch(String resource) {
    return request(resource, "batch").returning(Entity.class);
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
