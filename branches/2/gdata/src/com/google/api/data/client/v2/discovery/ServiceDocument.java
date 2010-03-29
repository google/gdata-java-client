// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.discovery;

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
