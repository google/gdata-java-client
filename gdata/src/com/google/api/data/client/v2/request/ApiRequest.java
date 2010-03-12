package com.google.api.data.client.v2.request;

import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.v2.jsonc.JsoncEntity;
import com.google.api.data.client.v2.jsonc.jackson.Jackson;

import java.io.IOException;

/**
 * Class for making requests.
 *
 * Requests are futures, and get() returns the actual value.
 * get() is aliased to execute(), both for compatiblity with client libs
 * and also because our Apiary APIs already have a get(), and calling
 * get().get() is just weird.
 *
 * Currently requests execute individually. Once requests are batched
 * (not supported yet), the batch will be responsible for setting the
 * response values.
 *
 * Requests can have parameters set in a few different ways:
 * .with(String) - params are string encoded in JSON
 * .with(Entity) - params are in an entity
 * .with(Object) - params will be converted from public fields on Object
 * .with(key, value) - adds parameters, can chain
 *
 * Currently you have to choose between .with(params) or .with(key, value)
 * but it would be easy to support both.
 *
 * Responses normally come back as an Entity, but you can set the
 * response class via:
 * .returning(Class) - return an instance of Class
 * .returningList(Class) - return a List of Class
 *
 * Setting will set public fields on all of the objects.
 *
 * Objects used in requests and responses can be hand-coded or generated
 * from Apiary services. Hand-coded classes can be useful if you only need
 * a small subset of data.
 * For example, class Person {public String name; public String email)
 * can be passed in to get names & emails if that's all you need.
 *
 * @author vbarathan@google.com (Prakash Barathan)
 */
public class ApiRequest<T> {
  /** Parameter map for use with .with(key, value) */
  private Entity paramMap = new Entity();

  private Entity defaultParamMap;

  /** Parameter set from .with(params) */
  private Object paramObject = null;

  /** The requested response class  */
  private Class<?> responseClass = JsoncEntity.class;

  /** Cached result */
  private Object result;

  /** True if called, to prevent double-submit */
  private boolean called = false;

  /** The fully qualified method name */
  private final String fullyQualifiedMethod;
  
  private final Discovery discovery;
  
  private final String resourceHandle;
  
  ApiRequest(Discovery discovery, String method, Entity defaultParams) {
    this(discovery, null, method, defaultParams);
  }
  
  /**
   * Create a request
   *
   * @param handle resource endpoint
   * @param transport ApiClient, used for transport
   * @param method The method
   * @param defaultParams default parameters to use in request
   */
  ApiRequest(
      Discovery discovery, String handle, String method, Entity defaultParams) {
    this.discovery = discovery;
    this.fullyQualifiedMethod = method;
    this.defaultParamMap = defaultParams;
    this.resourceHandle = handle;
  }
    
  /**
   * Execute the request.
   * If .returning() or .returningList() functions was called,
   * returns an object of the appropriate type. Otherwise, returns
   * and Entity.
   */
  @SuppressWarnings("unchecked")
  public T execute() {
    if (called) {
      return (T)result;
    }
    try {
      // Right now only allow paramObject || param map
      Entity params = (paramObject != null) ?
          Convert.fromObject(paramObject) : paramMap;

      Entity allParams = params.merge(defaultParamMap);
      Entity e;
      
      HttpResponse response = discovery.doRestRequest(
          resourceHandle, fullyQualifiedMethod, allParams);
      
      result = Jackson.parse(response, responseClass); 
      called = true;    
      return (T)result;
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  /**
   * Set the parameters to send with the request.
   * Params can either be an Entity or an aribtrary Object.
   *
   * If the params are an arbitrary object, then the values are
   * taken from public object fields.
   *
   * @param params The request parameters
   * @return The request
   */
  public ApiRequest<T> with(Object params) {
    this.paramObject = params;
    return this;
  }

  /**
   * Add a parameter by key to the request.
   *
   * @param key The key
   * @param value The parameter value
   * @return The request
   */
  public ApiRequest<T> with(String key, Object value) {
    this.paramMap.put(key, value);
    return this;
  }

  /**
   * Tell the request to return an instance of a specific class.
   *
   * @param cls The class type to return
   * @return The request.
   */
  @SuppressWarnings("unchecked")
  public <R> ApiRequest<R> returning(Class<? extends R> cls) {
    this.responseClass = cls;
    return (ApiRequest<R>)this;
  }
}
