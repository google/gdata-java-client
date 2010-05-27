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

package com.google.api.data.client.generator.model;

import com.google.api.client.util.Key;

import java.util.Map;
import java.util.SortedMap;

/**
 * @author Yaniv Inbar
 */
public final class Client implements Comparable<Client> {

  @Key
  public String id;
  
  @Key
  public String name;
  
  @Key
  public String className;
  
  @Key
  public SortedMap<String, Version> versions;
  
  /** Client Login token type or {@code null}. */
  @Key
  public String authTokenType;

  /** OAuth information or {@code null}. */
  @Key("OAuth")
  public OAuthInfo oauth;

  public int compareTo(Client client) {
    if (client == this) {
      return 0;
    }
    return id.compareTo(client.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Client)) {
      return false;
    }
    Client other = (Client) obj;
    return id.equals(other.id);
  }

  public void validate() {
    if (id == null) {
      throw new IllegalArgumentException("id required");
    }
    if (versions == null || versions.size() < 1) {
      throw new NullPointerException("at least one version required");
    }
    for (Map.Entry<String, Version> entry : versions.entrySet()) {
      entry.getValue().validate(entry.getKey(), this);
    }
    if (className == null) {
      className = Character.toUpperCase(id.charAt(0)) + id.substring(1);
    }
  }
}
