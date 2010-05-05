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

package com.google.api.client.http;

import com.google.api.client.auth.Authorizer;
import com.google.api.client.util.ArrayMap;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Stores HTTP headers used in an HTTP request. {@code null} is not allowed as a
 * name or value of a header.
 */
public final class HttpHeaders implements Cloneable {

  /**
   * Authorization header provider to use for all requests or {@code null} for
   * none.
   */
  public volatile Authorizer authorizer;


  /** Mapping from header name to value. */
  public volatile ArrayMap<String, String> values = ArrayMap.create();

  /**
   * List of private keys whose value must not be logged unless the log level is
   * {@link Level#ALL}, for example headers related to authorization.
   */
  public volatile ArrayList<String> privateNames = new ArrayList<String>(0);

  /**
   * Sets the {@code "Accept-Encoding"} header or remove the header if value is
   * {@code null}.
   */
  public void setAcceptEncoding(String value) {
    setValue("Accept-Encoding", value);
  }

  /**
   * Sets the {@code "User-Agent"} header or remove the header if value is
   * {@code null}.
   */
  public void setUserAgent(String value) {
    setValue("User-Agent", value);
  }

  /**
   * Sets the {@code "Authorization"} header or remove the header if value is
   * {@code null}. This value will not be logged.
   */
  public void setAuthorization(String value) {
    setPrivateValue("Authorization", value);
  }

  /**
   * Sets the {@code "If-Match"} header to the given etag value or remove the
   * header if value is {@code null}.
   */
  public void setIfMatch(String etag) {
    setValue("If-Match", etag);
  }

  /**
   * Sets the {@code "If-None-Match"} header to the given etag value or remove
   * the header if value is {@code null}.
   */
  public void setIfNoneMatch(String etag) {
    setValue("If-None-Match", etag);
  }

  /**
   * Sets the {@code "MIME-Version"} header to the given etag value or remove
   * the header if value is {@code null}.
   */
  public void setMimeVersion(String value) {
    setValue("MIME-Version", value);
  }

  @Override
  public HttpHeaders clone() {
    try {
      HttpHeaders result = (HttpHeaders) super.clone();
      result.values = this.values.clone();
      @SuppressWarnings("unchecked")
      ArrayList<String> privateNamesClone =
          (ArrayList<String>) this.privateNames.clone();
      result.privateNames = privateNamesClone;
      return result;
    } catch (CloneNotSupportedException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Sets the header for the given name and value or remove the header if value
   * is {@code null}. This header's value may be logged.
   */
  public void setValue(String name, String value) {
    setValueInternal(name, value);
    this.privateNames.remove(name);
  }

  /**
   * Sets the private header for the given name and value or remove the header
   * if value is {@code null}. This header's value must not be logged, for
   * example if it is related to authorization
   */
  public void setPrivateValue(String name, String value) {
    setValueInternal(name, value);
    ArrayList<String> privateNames = this.privateNames;
    if (!privateNames.contains(name)) {
      privateNames.add(name);
    }
  }

  /**
   * Sets the header for the given name and value or remove the header if value
   * is {@code null}.
   */
  private void setValueInternal(String name, String value) {
    if (name == null) {
      throw new NullPointerException();
    }
    ArrayMap<String, String> values = this.values;
    if (value == null) {
      values.remove(name);
    } else {
      values.put(name, value);
    }
  }
}
