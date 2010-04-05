/* Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.client.http.discovery;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Envelope for Apiary service discovery document.
 *
 * @author vbarathan
 */
public class ServiceDocument {

  private JSONObject document;

  public ServiceDocument(String json) {
    try {
      document = new JSONObject(json);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean hasService(String name) {
    return document.has(name);
  }

  private String getResourceUrl(String resource, boolean isItem) {

    String service = resource.substring(0, resource.indexOf("."));
    String collection = resource.substring(resource.indexOf(".") + 1);
    try {
      JSONObject serviceDocument = document.getJSONObject(service);
      String baseUrl = serviceDocument.getString("urlBase");
      JSONObject resourceDefinition = serviceDocument.getJSONObject("resources")
          .getJSONObject(collection);
      if (resourceDefinition.has("urlTemplate")) {
        baseUrl += resourceDefinition.getString("urlTemplate");
      }
      if (isItem && resourceDefinition.has("itemTemplate")) {
        baseUrl += resourceDefinition.getString("itemTemplate");
      }
      return baseUrl;
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }
  
  public String getResourceUrl(String resource) {
    return getResourceUrl(resource, false);
  }
  
  public String getResourceItemUrl(String resource) {
    return getResourceUrl(resource, true);
  }
  
}
