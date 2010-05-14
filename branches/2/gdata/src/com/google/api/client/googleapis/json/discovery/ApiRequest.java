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

import com.google.api.client.util.DataUtil;
import com.google.api.client.util.GenericData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Makes API requests based on Google API service discovery.
 *
 * Requests provide interface to specify uri, query, header, content parameters.
 * The parameters can be set in a few different ways:
 *    .with(GenericData) - params are in an entity
 *    .with(Object) - params will be converted from public fields on Object
 *    .with(key, value) - adds parameters, can chain
 *
 * Responses normally come back as GenericData, but you can set the
 * response class via:
 *   .returning(Class) - return an instance of Class
 *
 * @since 2.2
 * @author vbarathan@google.com (Prakash Barathan)
 */
public class ApiRequest<T> {
  
  /** Request type */
  public static enum RequestType {
    REST,
    RPC
  }
  
  /** Default wireformat content type */
  private static final String DEFAULT_CONTENT_TYPE = "application/json";

  /** Request type for this request */
  private ApiRequest.RequestType requestType =
      ApiRequest.RequestType.REST;
  
  /** Parameter map for use with .with(key, value) */
  private GenericData paramMap = new GenericData();

  /** The requested response class  */
  private Class<?> responseClass = null;

  /** Cached result */
  private Object result;

  /** True if called, to prevent double-submit */
  private boolean called = false;

  /** The fully qualified method name */
  private final String fullyQualifiedMethod;
  
  private final Discovery discovery;
  
  /**
   * Create a request
   *
   * @param method The method
   * @param defaultParams default parameters to use in request
   */
  ApiRequest(Discovery discovery, String method, GenericData defaultParams) {
    this.discovery = discovery;
    this.fullyQualifiedMethod = method;
    this.paramMap.putAll(defaultParams);
  }
  
  public ApiRequest<T> rpcRequest() {
    requestType = ApiRequest.RequestType.RPC;
    return this;
  }
    
  /**
   * Execute the request.
   */
  @SuppressWarnings("unchecked")
  public T execute() {
    if (called) {
      return (T)result;
    }
    try {
      if (requestType == RequestType.REST) {
        return (T) discovery.doRestRequest(
            fullyQualifiedMethod, paramMap, responseClass);
      } else {
        throw new IllegalStateException("TODO Handle RPC request");
      }
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Set the parameters to send with the request.
   * Params can either be an GenericData or an aribtrary Object.
   *
   * If the params are an arbitrary object, then the values are
   * taken from public object fields.
   *
   * @param params The request parameters
   * @return the request
   */
  public ApiRequest<T> with(Object params) {
    if (params instanceof String) {
      // TODO(vbarathan): Parse json string
      throw new RuntimeException("Raw string parameter not yet supported");
    } else { 
      paramMap.putAll(DataUtil.mapOf(params));
    }
    return this;
  }

  /**
   * Add a parameter by key to the request.
   *
   * @param key The key
   * @param value The parameter value
   * @return the request
   */
  public ApiRequest<T> with(String key, Object value) {
    paramMap.set(key, value);
    return this;
  }
  
  /**
   * Add request content to the request with specified mime-type.
   * 
   * @param type mime-type for the content
   * @param value content value
   * @return the request
   */
  @SuppressWarnings("unchecked")
  public ApiRequest<T> withContent(String type, Object value) {
    GenericData content = new GenericData();
    content.set("type", type);
    content.set("value", value);
    if (paramMap.get("content") != null) {
      List<GenericData> contents = (List<GenericData>) paramMap.get("content");
      contents.add(content);
    } else {
      List<GenericData> contents = new ArrayList<GenericData>();
      contents.add(content);
      paramMap.set("content", contents);
    }
    return this;
  }
  
  /**
   * Add request content to the request with default mime type.
   * 
   * @param value content value
   * @return the request
   */
  public ApiRequest<T> withContent(Object value) {
    return withContent(DEFAULT_CONTENT_TYPE, value);
  }
  
  /**
   * Tell the request to return an instance of a specific class.
   *
   * @param cls The class type to return
   * @return the request
   */
  @SuppressWarnings("unchecked")
  public <R> ApiRequest<R> returning(Class<? extends R> cls) {
    this.responseClass = cls;
    return (ApiRequest<R>)this;
  }
}
