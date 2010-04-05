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

public final class Version {
  Client client;
  String id;
  public AtomInfo atom;

  String getJarName() {
    return client.id + "-v" + id;
  }

  String getPathRelativeToSrc() {
    return "com/google/api/data/" + client.id + "/v" + id;
  }

  String getPackageName() {
    return "com.google.api.data." + client.id + ".v" + id;
  }

  void validate(String id, Client client) {
    this.id = id;
    this.client = client;
    if (atom != null) {
      atom.validate();
    }
  }
}
