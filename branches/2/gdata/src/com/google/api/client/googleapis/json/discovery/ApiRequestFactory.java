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
import com.google.api.client.googleapis.json.JsonParser;
import com.google.api.client.googleapis.json.JsonContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpTransport;
import com.google.api.client.util.GenericData;

/**
 * Creates requests with default parameters for specified service.
 *
 * @since 2.2
 * @author vbarathan@google.com (Parakash Barathan)
 */
public class ApiRequestFactory {

  public static class Builder {

    private String appName;
    private String resource;
    private ServiceDocument serviceDoc = null;
    private String serviceName;
    private String serviceVersion;

    private LowLevelHttpTransport lowLevelTransport;
    private GoogleTransport transport;

    private GenericData paramMap = new GenericData();

    public Builder() {
    }

    public Builder application(String appName) {
      this.appName = appName;
      return this;
    }

    public Builder discoveryDocument(String discoveryUri) {
      this.resource = discoveryUri;
      return this;
    }

    public Builder discoveryDocument(ServiceDocument doc) {
      this.serviceDoc = doc;
      return this;
    }

    public Builder forService(String service) {
      this.serviceName = service;
      this.serviceVersion = null;
      return this;
    }

    public Builder forService(String service, String version) {
      this.serviceName = service;
      this.serviceVersion = version;
      return this;
    }

    public Builder serviceDocument(ServiceDocument doc) {
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

    public Builder with(String key, Object value) {
      paramMap.set(key, value);
      return this;
    }

    public ApiRequestFactory build() {
      GoogleTransport newTransport;
      if (transport == null) {
        newTransport = new GoogleTransport();
        newTransport.applicationName = appName;
        HttpTransport.setLowLevelHttpTransport(lowLevelTransport);
        newTransport.addParser(new JsonParser());
      } else {
        newTransport = transport;
      }
      if (serviceDoc == null && resource != null) {
        serviceDoc = new ServiceDocument(resource);
      } else if (serviceDoc == null) {
        throw new IllegalStateException("No discovery document specified");
      }
      Discovery discovery = null;
      discovery = new Discovery(serviceDoc.getServiceDefinition(
          serviceName, serviceVersion), newTransport);
      discovery.setSerializer("application/json", JsonContent.class);
      return new ApiRequestFactory(discovery, paramMap);
    }
  }

  private GenericData paramMap = new GenericData();
  private Discovery discovery;

  private ApiRequestFactory(Discovery discovery, GenericData params) {
    this.discovery = discovery;
    this.paramMap = params;
  }

  public ApiRequest<GenericData> request(String method) {
    return new ApiRequest<GenericData>(discovery, method, paramMap)
        .returning(GenericData.class);
  }

  public ApiRequest<GenericData> request(String resource, String method) {
    return new ApiRequest<GenericData>(discovery, resource + "." + method, paramMap);
  }

  public ApiRequest<GenericData> query(String resource) {
    return request(resource, "query").returning(GenericData.class);
  }

  public ApiRequest<GenericData> insert(String resource, Object data) {
    return request(resource, "insert").withContent(data)
        .returning(GenericData.class);
  }

  public ApiRequest<GenericData> update(String resource, Object data) {
    return request(resource, "update").withContent(data)
        .returning(GenericData.class);
  }

  public ApiRequest<GenericData> delete(String resource) {
    return request(resource, "delete");
  }

  public ApiRequest<GenericData> batch(String resource) {
    return request(resource, "batch").returning(GenericData.class);
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
