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

package com.google.api.data.client.generator;

import java.util.TreeSet;

public final class Client implements Comparable<Client> {

  public String id;
  public String name;
  public String className;
  public TreeSet<Version> versions;
  public String authTokenType;

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

  void validate() {
    if (id == null) {
      throw new IllegalArgumentException("id required");
    }
    if (versions == null || versions.size() < 1) {
      throw new NullPointerException("at least one version required");
    }
    for (Version version : versions) {
      version.validate();
    }
    if (className == null) {
      className = Character.toUpperCase(id.charAt(0)) + id.substring(1);
    }
  }
}
