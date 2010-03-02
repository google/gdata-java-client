// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.apache;

import com.google.api.data.client.v2.GDataClient;
import com.google.api.data.client.v2.GDataClientFactory;

import java.util.logging.Logger;

public final class ApacheGData {
  
  static final Logger LOGGER =
      Logger.getLogger(ApacheGData.class.getName());

  public static final GDataClientFactory CLIENT_FACTORY =
      new GDataClientFactory() {

        public GDataClient createClient(String contentType,
            String applicationName, String authToken, String version) {
          return new ApacheGDataClient(contentType, applicationName, authToken,
              version);
        }
      };
}
